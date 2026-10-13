# NormalizeFloatingNumbers

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/NormalizeFloatingNumbers.scala, lines 67-201
```

## Independent verifier review

**Verdict:** AGREE

The equi-join and NAAJ branches' soundness rests entirely on relating the normalization function f to the join's equality semantics (f(x)=f(y) coinciding with x=y under IEEE-754 float equality, −0.0≡0.0, NaN≢NaN), but QED's model reduces all values to integers with f and the join condition as universally-quantified uninterpreted symbols — the required relation is not valid for arbitrary f (a constant f breaks it), cannot be added as an axiom without presupposing the conclusion, and no narrower special case can constrain f since QED quantifies universally over it; the window-partition branch additionally requires window/ordering semantics that QED does not model at all. ```
