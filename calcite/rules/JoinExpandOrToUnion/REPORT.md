# JoinExpandOrToUnion

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 0  **Verification rounds used:** 0

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/JoinExpandOrToUnionRule.java
```

## Independent verifier review

**Verdict:** AGREE (hand-evaluated, empirically tested)

The rewrite's core identity — Join(Or(P,Q)) ⟹ UnionAll(Join(P), Join(And(Q, ¬P))) — was tested directly against QED under both candidate negation operators and both failed identically: provable=false, complete_fragment=true (QED confidently explored the whole fragment, not a timeout). This was tried twice each, including in the simplest possible reduced form (a single relation, Filter(Or(P,Q)) vs Union(Filter(P), Filter(And(Q,¬P))), no join at all):

1. Using RexRN.Not (plain negation): not provable.
2. Using a new, correctly-implemented RexRN.IsNotTrue node (QED's Rust source at pipeline/relation.rs:618 confirms "IS NOT TRUE" is a real, recognized operator with proper 3-valued semantics distinct from NOT): not provable, same signature.

Since the reduced single-relation form is a textbook propositional tautology (P ∨ (Q ∧ ¬P) ≡ P ∨ Q, trivial by case analysis on P), a hard "not provable" on it rules out both the specific-negation-operator hypothesis and any join-specific cause. The most likely explanation is a genuine QED equivalence-checking limitation: it does not correlate the same uninterpreted predicate symbol referenced both positively and negatively across two separate UNION branches of the *same* side, unlike a plain before()/after() pair (which is how every already-proved rule in this project uses shared symbols, and which QED handles fine). This is not a missing DSL operator that extend_dsl_file could add — both required operators (NOT, IS NOT TRUE) already exist and are correctly wired to QED's own recognized semantics; the gap is in how QED relates an uninterpreted symbol's occurrences across independent branches of a bag union, which is outside what a DSL-level change can address. UNSUPPORTED.
