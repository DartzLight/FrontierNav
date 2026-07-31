# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

FrontierNav Optimizer is a standalone Java tool that finds a near-optimal probe layout for the FrontierNav
system in *Xenoblade Chronicles X*, using a genetic algorithm. It optimizes layouts to maximize Miranium
production, revenue, and/or targets for specific "precious resources".

## Build, test, run

- Requires Java 25 (Maven toolchain via `maven.compiler.release`).
- Build + test: `mvn -B clean verify`
- Run a single test class: `mvn -Dtest=FrontierNavTest test`
- Run a single test method: `mvn -Dtest=FrontierNavTest#someMethod test`
- Package the runnable shaded jar (`target/frontiernav.jar`): `mvn -DskipTests package`
- Run locally: `java -jar target/frontiernav.jar` (reads `input/*.tsv` at runtime, relative to the working directory)
- Run via Docker: `docker-compose up --build` (mounts `./input` read-only into the container)
- The entry point / shaded main class is `cardinal.xenoblade.frontiernav.optimization.genetic.FrontierNavGeneticOptimizer`.
- CI (`.github/workflows/ci.yml`) runs `mvn -B -ntp clean verify` on every push/PR.
- `release.yml` rebuilds `target/frontiernav.jar` on every push to `main` and republishes it under the floating
  `latest` GitHub release tag — there is no separate versioned release process yet.

## Input configuration model

The program reads tab-separated `.tsv` files (and a `.properties` file) from `input/`, falling back to the
bundled defaults in `input/default/` file-by-file when a given file is missing (see `FileHelper.findOrDefault`,
used in `FrontierNavGeneticOptimizer.main`). Key files: `sites.tsv`, `inventory.tsv`, `network.tsv`,
`resources.tsv`, `genetic.properties`. See the README's "Configuration" section for the exact TSV column
layouts, the probe-code table, and the list of `PreciousResource` enum values — don't re-derive these from
the loader code if the README already documents them.

## Architecture

The domain model and the optimizer are cleanly separated:

- **`site` package** — `Mira` is the site graph (built via `Mira.Builder`, backed by a JGraphT `SimpleGraph`).
  It knows site adjacency, connected-component "chains" for a given probe layout (`computeChain`), and the
  theoretical maximum precious-resource output across all sites. `Site` is a single node (id, miranium/revenue
  rank, precious resources, unexplored territories). Loaded from TSV by `MiraLoader`.
- **`probe` package** — `Probe` and its subtypes (`BasicProbe`, `MiningProbe`, `ResearchProbe`, `BoosterProbe`,
  `DuplicatorProbe`, `StorageProbe`) encode each probe grade's multipliers/bonuses. `ProbeParser` converts the
  TSV probe codes (e.g. `M3`, `R1`, `D`) to `Probe` instances.
- **`probe.layout` package** — `ProbeLayout` is an immutable `Map<Site, Probe>` assignment (missing sites default
  to `BasicProbe.DEFAULT`).
- **`inventory` package** — `Inventory`/`MutableInventory` track how many of each probe grade are available;
  `ProbeLayoutValidator` checks a layout doesn't exceed inventory. The optimizer draws from a `MutableInventory`
  copy when generating/mutating layouts so probe counts stay respected.
- **`FrontierNav`** (top-level) — the actual game-rule calculator. Given a `Mira` + `ProbeLayout`, it computes
  Miranium, revenue, storage, and precious-resource output per site or in total. This is where chain multipliers
  (`computeChainMultiplier`, based on connected same-probe components), duplicator-probe pass-through logic
  (`getEffectiveProbes`), and booster-probe propagation to *neighboring* sites (`computeIncomingBoostMultiplier`
  / `computeOutgoingBoostMultiplier`) live. Read this class closely before changing any scoring behavior — the
  interactions between chains, boosts, and duplicators are the core game-mechanics logic of the whole project.
- **`FrontierNavResult`** — a computed snapshot (Miranium/revenue/storage/precious resources) for a given
  `ProbeLayout`, used as the genetic algorithm's per-individual state so `FrontierNav` doesn't need to be
  recomputed repeatedly.
- **`optimization.Fitness`** — turns a `FrontierNavResult` into a scalar score: a weighted sum of Miranium and
  revenue (`miraniumCoef`/`revenueCoef`), multiplied down by malus factors when configured precious-resource
  `threshold.*`/`ratio.*` targets aren't met (see `GeneticParameters`/`genetic.properties`).
- **`optimization.random`** — `ProbeLayoutGenerator` builds fully random valid layouts from an inventory;
  used both for initial population seeding and for random injection each generation.
- **`optimization.genetic`** — the GA itself. `GeneticAlgorithm.optimize()` runs the generational loop:
  `GeneticSelection` (elitism + tournament), `GeneticCrossover`, `GeneticMutation` (swap/replace rates), and
  random injection each combine to produce the next population, sized/weighted by the counts in
  `GeneticParameters` (loaded from `genetic.properties` via `GeneticParametersLoader`). `Selection`/`Crossover`/
  `Mutation` are interfaces (see the `Dummy*` test doubles) so alternative strategies could be swapped in.
- **`serialization`** — `FrontierNavExporter`/`FrontierNavImporter` convert a `ProbeLayout` to/from the compact
  numeric-code string format used by frontiernav.net's probe-guide map visualizer
  (`https://frontiernav.net/wiki/.../probe-guides/FN?map=...`).

## Notes for making changes

- Game-mechanics correctness (multipliers, chains, boosts, duplicators, storage) is concentrated in
  `FrontierNav`; when touching scoring, prefer extending/reading that class over duplicating logic elsewhere.
- The GA's tunable knobs are all routed through `GeneticParameters`/`genetic.properties` — avoid hardcoding
  values that a user should be able to tune from the input file.
- `input/default/*` are the shipped fallback data files consumed by the loader classes at runtime, not just
  documentation — keep them in sync with any changes to TSV parsing or the `PreciousResource`/probe-code enums.
