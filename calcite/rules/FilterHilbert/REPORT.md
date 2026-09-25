# FilterHilbert

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 26  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SpatialRules.java
```

## Independent verifier review

**Verdict:** AGREE

FilterHilbert's soundness is exactly the concrete spatial/Hilbert-curve fact — that every point within distance d of a constant geometry has a HilbertCurve2D(8) index inside the BETWEEN ranges computed from the buffered envelope (plus the ST_DWithin degenerate cases: negative distance ⟹ FALSE, zero distance ⟹ equality) — and in RuleScript ST_DWithin, HILBERT, ST_Point and the range predicate can only be introduced as independent uninterpreted symbols, with no arithmetic, ordering, or axiom mechanism in the core language that could express the coverage implication "DWithin(g,p,d) ∧ h=HILBERT(lon,lat) ⟹ h∈ranges". That implication is refutable by a trivial SMT countermodel (make the range predicate false on a row where DWithin holds), and the only DSL extension that would make it provable is asserting the rule's own geometric claim as an axiom, which would be circular rather than a proof — this is precisely the "backend operator's bespoke internal semantics / no predicate inference between independent symbols" limitation, so the porter's UNSUPPORTED verdict is correct. ```
