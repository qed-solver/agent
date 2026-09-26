# FoldCollate

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

FoldCollate converts a Collate expr over an uncollated string into a collated
string constant.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `FoldCollate`, not the other rules in that file):

```
# FoldCollate converts a Collate expr over an uncollated string into a collated
# string constant.
[FoldCollate, Normalize]
(Collate $input:(Const) $locale:*)
=>
(CastToCollatedString $input $locale)
```
```

## Independent verifier review

**Verdict:** AGREE

FoldCollate's entire content is the backend-bespoke identity `Collate(c, locale) ≡ CastToCollatedString(c, locale)` on a constant input, which holds only by virtue of CockroachDB's definitions of those two operators, not by any bag-algebra fact. In QED's uninterpreted-function theory they must be two distinct symbol names (a single shared name would assume the rule's conclusion as a premise, and the input's constant-ness gives no leverage — distinct uninterpreted functions can disagree even at ground terms), so the equivalence is not a logical validity the SMT solver can establish; the only "proofs" available are vacuous encodings that stop expressing the rule. This lands squarely in the documented "backend operator's bespoke internal semantics" limitation, and no `extend_dsl_file` addition (new `Collate`/`CastToCollatedString` builders, a non-boolean constant literal) could close the gap, since the DSL has no axiom mechanism to relate two distinct symbols — the UNSUPPORTED verdict is correct.
