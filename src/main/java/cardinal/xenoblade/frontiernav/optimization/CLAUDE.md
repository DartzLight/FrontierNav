# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

Scope: this file covers `cardinal.xenoblade.frontiernav.optimization` and its `genetic`/`random` subpackages —
the search layer that finds a good `ProbeLayout` for a given `Mira`/`Inventory`. It does not cover the game-rule
calculator (`FrontierNav`) or the domain model (`site`, `probe`, `inventory` packages) — see the repo-root
`CLAUDE.md` for those and for build/test commands.

## Package layout

- **`optimization`** (this package root) — `Fitness`, the scalar scoring function shared by both optimizers below.
- **`optimization.genetic`** — the genetic algorithm (`GeneticAlgorithm`) and its pluggable strategies
  (`Selection`/`Crossover`/`Mutation` interfaces, implemented by `GeneticSelection`/`GeneticCrossover`/
  `GeneticMutation`), plus `GeneticParameters`/`GeneticParametersLoader` for tuning from `genetic.properties`,
  and `FrontierNavGeneticOptimizer`, the entry point used by the shipped jar.
- **`optimization.random`** — `ProbeLayoutGenerator` (build one valid random layout from an `Inventory`) and
  `FrontierNavRandomOptimizer`, a simple repeated-random-sampling baseline optimizer (not the shipped entry point,
  but useful for comparing against the GA or for quick experiments).

## Fitness

`Fitness` (functional interface, `evaluate(FrontierNavResult) -> double`) is the single scoring function both
optimizers maximize:

- `Fitness.of(miraniumCoef, revenueCoef, thresholds, ratios)` computes a weighted-sum base score
  (`miraniumCoef * effectiveMiranium + revenueCoef * revenue`), then multiplies it down by a *malus* in `[0, 1]`
  for every configured `PreciousResource` target that isn't met:
  - `thresholds` (from `threshold.<RESOURCE>` in `genetic.properties`) are absolute targets, capped at the
    resource's theoretical max (`Mira.getMaximumPreciousResources()`).
  - `ratios` (from `ratio.<RESOURCE>`, in `[0, 1]`) are targets expressed as a fraction of that theoretical max.
  - Each unmet target multiplies the score by `actual / threshold` (never > 1), so missing several targets at
    once compounds the penalty rather than just taking the worst one.
- `Fitness.cached(delegate)` wraps a `Fitness` with a `WeakHashMap<FrontierNavResult, Double>` memo — `GeneticAlgorithm`
  always uses the cached form since the same `FrontierNavResult` (elites) gets re-evaluated across generations.
- `ascendingComparator()`/`descendingComparator()` are the comparators `Selection` and `extractBest` sort on.

## Genetic algorithm flow (`GeneticAlgorithm.optimize()`)

One generation (`evolve`) assembles the next population purely by *counts* pulled from `GeneticParameters`
(all tunable via `genetic.properties`, never hardcode a knob here):

1. **Selection** — carry over `selectionByElitismCount` best (by fitness) plus `selectionByTournamentCount`
   tournament winners (`tournamentSize` random contenders each, best wins, sampled without replacement).
2. **Crossover** — `crossoverOnSelectionCount` offspring from two parents both drawn from the selected pool, and
   `crossoverOnRandomCount` offspring from one selected parent + one freshly random layout. `GeneticCrossover`
   picks each site's probe from one parent at random, then *repairs* the result against `Inventory` (removes
   probes that exceed stock, refills unassigned sites from what's left) — crossover offspring are always
   inventory-valid, never rejected/retried.
3. **Mutation** — `mutationCount` layouts derived from a selected parent via `GeneticMutation`, which per-site
   independently rolls `swapRate` (swap this site's probe with another random site's) and `replaceRate`
   (swap the probe for a random one still available in inventory).
4. **Random injection** — `randomInjectionCount` fully new random layouts from `ProbeLayoutGenerator`, to fight
   premature convergence.

Elites are carried over as already-evaluated `FrontierNavResult`s; every newly produced `ProbeLayout` (crossover/
mutation/injection) is evaluated in one batch via `parallelStream()` in `evaluate()` — keep new layout-producing
code batched through here rather than evaluating one at a time, since `FrontierNavResult.compute` is the
expensive step.

`initialize()` seeds the population entirely from `ProbeLayoutGenerator.generateRandom()` — there's no warm-start
from a previous run.

## Adding a new Selection/Crossover/Mutation strategy

Implement the respective interface (see `Dummy*` classes under `src/test/.../genetic` for the minimal test-double
shape) and wire it in wherever a `GeneticAlgorithm` is constructed (currently only `FrontierNavGeneticOptimizer`).
Strategies must stay inventory-valid on their own — `GeneticAlgorithm` does not re-validate what `Crossover`/
`Mutation` return.

## Gotchas

- `ProbeLayoutGenerator.generateRandom()` and inventory "current inventory" tracking always iterate sites sorted
  by `Site::id` — this makes random layout generation deterministic given the same `Random` seed/call sequence;
  don't reorder iteration without considering reproducibility of GA runs.
- `FrontierNavRandomOptimizer` uses a plain (non-cached, non-malus) `Fitness.of(miraniumCoef, revenueCoef)` — it's
  a baseline, not feature-equivalent to the GA path; don't assume parity between the two `main()`s.
