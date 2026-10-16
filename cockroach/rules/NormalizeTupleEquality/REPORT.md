# NormalizeTupleEquality

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 44  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeTupleEquality breaks up expressions like:
(a, b, c) = (x, y, z)
into
(a = x) AND (b = y) AND (c = z)

This rule makes it easier to extract constraints from boolean expressions,
so that recognition code doesn't have to handle the tuple case separately.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeTupleEquality`, not the other rules in that file):

```
# NormalizeTupleEquality breaks up expressions like:
#   (a, b, c) = (x, y, z)
# into
#   (a = x) AND (b = y) AND (c = z)
#
# This rule makes it easier to extract constraints from boolean expressions,
# so that recognition code doesn't have to handle the tuple case separately.
[NormalizeTupleEquality, Normalize]
(Eq (Tuple $left:*) (Tuple $right:*))
=>
(NormalizeTupleEquality $left $right)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests on tuple extensionality — pack(a,b,c)=pack(x,y,z) ⟺ a=x ∧ b=y ∧ c=z — but QED's theory is equality over uninterpreted symbols with every uninterpreted type flattened to an opaque INTEGER atom (per RelType.java), with no product type and no extensionality/injectivity axiom for uninterpreted functions or predicates. Any RuleScript encoding therefore either uses independent symbols (an uninterpreted tuple-equality predicate vs. a conjunction of field equalities), which QED can refute with a countermodel, or reuses a single symbol for both sides, which is a vacuous identity rather than the rule; closing the gap would require adding an extensionality axiom to the Rust prover's theory, which is off-limits (extend_dsl_file can only add Java builders serializing into the prover's existing JSON, not new theory axioms). ```
