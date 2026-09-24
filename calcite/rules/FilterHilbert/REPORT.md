# FilterHilbert

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/SpatialRules.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's soundness rests on the bespoke internal semantics of Calcite's HilbertCurve2D/SpaceFillingCurve2D and JTS spatial functions (points within distance d of a constant geometry ⇒ Hilbert-8 index in specific numeric ranges), which QED can only model as uninterpreted symbols; under the sole expressible constraint `h = hilbert(lon, lat)` (a UF equality), no entailment from `ST_DWithin(g, pt(lon,lat), d)` to a range predicate on `h` is derivable, and the concrete bounds are computed in Java outside the pattern language, so no DSL extension could close the gap (adding the implication itself as a constraint would be circular and vacuous). The porter's transcript shows it had already independently reached exactly this fundamental point before the HTTP 400 context-length error cut it off, so the UNSUPPORTED conclusion is correct despite the garbled termination reason. ```
