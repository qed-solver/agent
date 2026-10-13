# LikeSimplification

**Status:** SKIPPED
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 23  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/expressions.scala, lines 878-984
```

## Independent verifier review

**Verdict:** AGREE

Every rewrite in LikeSimplification (LIKE 'p%' → StartsWith, '%s' → EndsWith, '%m%' → Contains, 'p%q' → Length>=k ∧ StartsWith ∧ EndsWith, 'x' → =, and the LikeAll/Any fan-out to AND/OR) is an entailment between distinct predicate symbols that QED encodes as independent uninterpreted functions, and the prover has no string theory or axioms linking a glob pattern to the string built-ins — so no encoding of before/after can make an SMT model separate the two sides. The `p%q` case additionally bakes pattern-content-derived constants (byte/code-point length guard) and the multi-LIKE cases depend on the bespoke semantics of LikeAll/LikeAny, which QED cannot see through as uninterpreted n-ary operators. Sharing a symbol name between the two sides would only prove a tautology, and a DSL extension cannot help because new operators still serialize as uninterpreted names and string semantics would have to live in the off-limits prover. ```
