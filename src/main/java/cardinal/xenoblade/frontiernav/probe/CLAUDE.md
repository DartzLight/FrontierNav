# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Scope

This file covers `cardinal.xenoblade.frontiernav.probe` (probe grade data) and its `probe.layout` subpackage
(site-to-probe assignments). See the repo-root `CLAUDE.md` for build/test commands and overall architecture.

## Model

- **`Probe`** is the interface every probe grade implements: `getMiraniumMultiplier`, `getMiraniumStorage`,
  `getRevenueMultiplier`, `getRevenueBonus`, `getBoostMultiplier`. All values are plain `int`s (multipliers are
  percentages, e.g. `100` = ×1.0) — there is no behavior here, just per-grade constants. `FrontierNav` is the
  only place that interprets these values (chain multipliers, boost propagation, duplicator pass-through);
  don't add game-mechanics logic to this package.
- Each concrete type is an `enum implements Probe`, one enum per probe *category*, one constant per *grade*:
  - `BasicProbe.DEFAULT` — the no-probe fallback (`ProbeLayout.getProbe` returns this for unassigned sites).
  - `MiningProbe` (G1–G10) — scales Miranium output; flat revenue multiplier.
  - `ResearchProbe` (G1–G6) — scales revenue output/bonus; flat Miranium multiplier.
  - `BoosterProbe` (G1–G2) — near-zero own output; its `getBoostMultiplier` is what neighboring sites read.
  - `StorageProbe.DEFAULT` — adds `getMiraniumStorage()` capacity, minimal own output.
  - `DuplicatorProbe.DEFAULT` — has **no** own multipliers/storage/bonus; every getter throws
    `UnsupportedOperationException`. `FrontierNav.computeEffectiveProbes` special-cases it by substituting the
    probes of the site's graph neighbors instead of calling these getters directly — never call a
    `DuplicatorProbe` getter without that substitution.
  - Adding a new probe grade/category means updating both the enum and `ProbeParser` (and the README's
    probe-code table per the root `CLAUDE.md`); a probe that isn't in `ProbeParser` can't be loaded from TSV.
- **`ProbeParser`** maps the TSV probe codes (`M1`–`M10`, `R1`–`R6`, `S`, `B1`, `B2`, `D`, `-`) to `Probe`
  constants. `tryParseProbe` returns `Optional.empty()` for unknown codes; `parseProbe` throws
  `NoSuchElementException`. Prefer `tryParseProbe` when validating untrusted input.

## `probe.layout`

- **`ProbeLayout`** is an immutable record wrapping `Map<Site, Probe>` (defensively copied via `Map.copyOf`).
  `getProbe(Site)` is the only accessor and defaults missing sites to `BasicProbe.DEFAULT` — always go through
  it rather than reading `probes()` directly, so the default is applied consistently.
- **`ProbeLayoutLoader.loadProbes(Path, Mira)`** parses a two-column `siteId\tprobeCode` TSV into a
  `ProbeLayout`, resolving site IDs against an already-built `Mira` graph. It uses `ProbeParser.parseProbe`
  (throwing), so a malformed probe code fails the whole load rather than being silently skipped.
