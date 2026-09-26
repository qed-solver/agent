# FoldInEmpty

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 49  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldInEmpty replaces the In with False when the the right input is empty. Note
that this is correct even if the left side is Null, since even an unknown
value can't be in an empty set.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldInEmpty`, not the other rules in that file):

```
# FoldInEmpty replaces the In with False when the the right input is empty. Note
# that this is correct even if the left side is Null, since even an unknown
# value can't be in an empty set.
[FoldInEmpty, Normalize]
(In * (Tuple []))
=>
(False)
```
```

## Independent verifier review

**Verdict:** AGREE

FoldInEmpty's soundness rests on the scalar IN operator's list-membership algebra (x ∈ [] = false), which QED does not model and RuleScript cannot axiomatize: naming In as an uninterpreted symbol makes the claim unprovable (symbols are universally quantified, so Filter(in, R) ≡ Filter(False, R) fails under in ≡ true), and the only encodings QED could prove (e.g., semi-join against a relation built with empty() ≡ false filter) are vacuous bag identities where both sides are empty by construction and the fold itself never appears. This is a genuine "operator's bespoke internal semantics" limitation — the core RexRN/RelRN language has no scalar In, no tuple/list constant, and no axiom mechanism — not a missing builder that extend_dsl_file could close, since the unchanging prover has no list-membership semantics to attach to such a symbol. ```
