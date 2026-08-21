# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Scope

This file covers `cardinal.xenoblade.frontiernav.site` (the FrontierNav site graph and static site/resource
data). See the repo-root `CLAUDE.md` for build/test commands and overall architecture.

## Model

- **`Site`** is an immutable record: `id`, `miraniumRank`, `revenueRank`, `unexploredTerritories`, and a
  `Map<PreciousResource, Double>` of the precious resources present at that site with their yield. It has no
  behavior — `FrontierNav` interprets these values. `Site` instances are used as graph vertices and as map keys
  throughout the optimizer, so treat them as value objects (record equality is by all fields, including the
  `preciousResources` map).
- **`MiraniumRank`** (`A`/`B`/`C`) and **`RevenueRank`** (`S`/`A`/`B`/`C`/`D`/`E`/`F`) are enums that carry a
  single `getBaseMiranium()`/`getBaseRevenue()` int constant per rank — the base output `FrontierNav` scales by
  probe multipliers. Both have an `of(String)` factory that matches by exact enum-name equality and throws
  `NoSuchElementException` on no match; there is no lenient/`Optional` variant like `PreciousResource.of`.
- **`PreciousResource`** is the enum of all "precious resource" types (ore/mineral names) trackable per site.
  `of(String)` matches case-insensitively and returns `Optional.empty()` on no match — prefer it over
  `valueOf` when parsing untrusted input. Adding a new precious resource means updating this enum, the
  `resources.tsv` default data, and the README's `PreciousResource` list per the root `CLAUDE.md`.
- **`Mira`** is the site graph: an immutable wrapper around a JGraphT `SimpleGraph<Site, DefaultEdge>`, built
  only via `Mira.Builder` (`addSite`/`addConnection`/`build`) — there's no public mutator afterward.
  - `getSites()` returns all sites as a `SequencedSet`, sorted by `Site::id`.
  - `getSite(int siteID)` looks up by ID via a precomputed `Map<Integer, Site>`.
  - `getConnectedSites(Site)` returns the site's direct graph neighbors (precomputed at construction via
    `Graphs.neighborSetOf`) — this is adjacency, not connected-component membership; `FrontierNav` uses it for
    booster-probe propagation to neighbors.
  - `getMaximumPreciousResources()` returns, per `PreciousResource`, the sum of that resource's yield across
    every site in the graph — the theoretical max if every site producing it were exploited. Precomputed once
    at construction (not layout-dependent).
  - `computeChains(Function<Site, T> siteClassifier, Predicate<Site> condition)` is the connected-component
    query used by `FrontierNav` to compute probe chain multipliers: sites failing `condition` get chain size
    `1`; sites passing it are grouped by `siteClassifier` (typically "same probe grade"), and within each group
    connected subgraphs are found via `ConnectivityInspector` — every site in a connected subgraph gets that
    subgraph's size as its chain size. This is graph-connectivity (chains must be physically adjacent), not
    just "same classifier value" — don't replace the `AsSubgraph`/`ConnectivityInspector` logic with a plain
    grouping-by-key.
- **`MiraLoader`** parses `sites.tsv` + `network.tsv` + `resources.tsv` into a `Mira` (see the README for exact
  column layouts). Load order matters: `resources.tsv` is parsed first (`PreciousResource` → siteID → yield),
  then folded into each `Site` while parsing `sites.tsv`, then `network.tsv` connections are resolved against
  the resulting site-ID map. `loadNetwork` silently skips (`Stream.empty()`) any connection referencing an
  unknown site ID rather than failing — malformed `sites.tsv`/`network.tsv` pairs won't error, they'll just
  drop edges.

## Notes for making changes

- This package is pure data/graph structure with no game-mechanics logic (multipliers, chains-as-scoring,
  boosts) — that all lives in `FrontierNav` per the root `CLAUDE.md`. Keep it that way; if you're computing a
  Miranium/revenue/precious-resource *value*, it probably belongs in `FrontierNav`, not here.
- `Mira` is built once per run and treated as immutable/precomputed (neighbor sets, max precious resources) for
  performance — the GA calls `computeChains`/`getConnectedSites` many times per generation, so avoid
  reintroducing per-call graph traversal for data that can be precomputed at `build()` time instead.
