# FilterHilbert

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SpatialRules.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness is exactly the entailment "under h = HILBERT(lon, lat), ST_DWithin/ST_Contains(g, ST_Point(lon, lat), d) ⇒ (h BETWEEN c1 AND c2 OR ...)", which holds only because of Calcite's Java-side knowledge of HilbertCurve2D's index ranges and ST_Buffer geometry — none of which appear in the relational expression. In any RuleScript encoding, ST_DWithin, ST_Point, HILBERT, ST_Buffer, and the spatial predicate are all independent uninterpreted symbols to QED, so the added range conjunct is not validly implied for all instantiations (SMT will model lon/lat with the equality h = HILBERT(lon,lat) true while the spatial predicate holds and h falls outside the range), making the faithful encoding correctly unprovable. No DSL extension can close this because QED's fixed prover cannot be taught bespoke spatial/Hilbert-curve operator semantics (and the prover itself must not be modified), and every sub-case (zero-distance → equality, negative-distance → false) rests on the same internal operator semantics.
