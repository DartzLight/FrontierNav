# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Scope

This file covers `cardinal.xenoblade.frontiernav.inventory` (probe stock tracking and layout validation). See
the repo-root `CLAUDE.md` for build/test commands and overall architecture, and `probe/CLAUDE.md` for what
`Probe` values mean.

## Model

- **`Inventory`** is the immutable "starting stock": a `List<Probe>` built from a `Collection<Probe>`, silently
  dropping any `BasicProbe` entries in its constructor (there's no limit on how many `BasicProbe`s a layout can
  use, so they aren't tracked as a finite resource). It's loaded once per run and never mutated directly —
  callers get a working copy via `getMutableInventory()` (fresh copy) or `retrieveCurrentInventory(ProbeLayout)`
  (a fresh copy with the probes already used by that layout removed, via `MutableInventory::removeProbe`).
- **`MutableInventory`** is the consumable working copy the GA draws from when generating/mutating layouts:
  - `takeRandomProbe(Random)` removes and returns a random probe, or `BasicProbe.DEFAULT` if the inventory is
    empty — this is the fallback path when trying to assign a probe but none are left.
  - `swapRandomProbe(Probe, Random)` returns a random probe from the inventory while putting `probeToSwap`
    back in — used by mutation to trade one site's probe for another without changing total probe counts.
  - `removeProbe(Probe)` throws `IllegalStateException` if the exact probe isn't present — probes are matched
    by value (enum constant identity), not by grade/category, so removing e.g. a `MiningProbe.G3` requires that
    exact grade to still be in the list.
  - There's no capacity check anywhere in this class; going over budget is prevented by callers only ever
    drawing from a `MutableInventory` that started from a real `Inventory`/layout state.
- **`InventoryLoader.loadInventory(Path)`** parses a two-column `probeCode\tcount` TSV (see the README's
  probe-code table) into an `Inventory`, expanding each `count` into that many individual `Probe` list entries
  via `ProbeParser.parseProbe` (throwing — a malformed probe code fails the whole load).
- **`ProbeLayoutValidator.searchForInvalidProbes(ProbeLayout, Inventory)`** checks a *finished* layout against
  an `Inventory` rather than consuming a `MutableInventory` as it goes: it walks the layout's non-`BasicProbe`
  assignments (sorted by `Site::id` for deterministic output) and removes each from a scratch copy of
  `inventory.getProbes()`; any probe that fails to remove (already used up) is collected and returned. An empty
  result means the layout is affordable; this is a read-only check used for validating externally-supplied
  layouts (e.g. imported ones), not part of the GA's hot path.

## Notes for making changes

- `Inventory` vs `MutableInventory` is a deliberate immutable/mutable split: `Inventory` is the fixed reference
  loaded from `inventory.tsv`, `MutableInventory` is the per-candidate-layout scratch state consumed during
  generation/mutation. Don't add mutation methods to `Inventory` or a "reset to full" method to
  `MutableInventory` — get a new one from `Inventory` instead.
- Probe equality here is by enum constant (grade), not by category — a `MiningProbe.G3` and `MiningProbe.G4`
  are different inventory entries even though both are mining probes.
