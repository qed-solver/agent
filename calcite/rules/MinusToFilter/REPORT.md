# MinusToFilter

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 45  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToFilterRule.java
```

## Independent verifier review

**Verdict:** AGREE

I independently verified the rewrite is mathematically sound, but any faithful encoding forces QED to relate its EXCEPT (set-minus) primitive to a group-all/distinct aggregate over a filtered scan, and both sides reduce to zero/positivity tests on semiring products of the form S·[predicate] (S·P_R = 0 on the before side, S·P_L·¬P_R > 0 from the distinct on the after side) — a zero-of-product / aggregate-algebra step beyond bag-equality that QED's semiring decision procedure cannot perform, and which no DSL extension or re-encoding can close since the prover itself is off-limits and the DSL already exposes every operator the rule needs. The porter's four fast, non-timed-out "not provable" results across the full encodable 2×2 space (plain vs filtered left × unique vs non-unique base) are consistent with this: even the unique-key variant still requires 0/1-multiplication facts under a support test, and the only distinct alternative (union(false, F, F)) serializes into QED's same "distinct" wrapper. ```
