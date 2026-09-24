# MinusToDistinct

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/MinusToDistinctRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rule's correctness rests on the algebra of COUNT: the post-rewrite plan's `count_0 > 0` and `count_i = 0` tests are what implement the "present in the first operand / absent from every other" membership test of a set difference, so proving equivalence requires knowing that a group's count is 0 iff no row of that group exists in the corresponding input (and > 0 iff it does) — an aggregate algebraic identity QED explicitly does not have (it only knows bag equality of aggregate inputs). Since the before pattern (distinct MINUS) contains no aggregate at all, there is no bag-equal counterpart aggregate on the other side for the prover to equate against, so the SMT decision procedure has nothing to bridge the count-based membership test with EXCEPT semantics. The DSL gaps (no filtered aggregate call in `AggCall`, no literal-constant projection or equality Rex to build the branch-index labeling) are secondary: extending the DSL cannot help because the fixed Rust prover would still lack COUNT's semantics to decide the filter conditions. ```
