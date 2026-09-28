# NormalizeTupleEquality

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 88  **Verification rounds used:** 5

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

The rule's entire correctness rests on the semantic identity "tuple equality = conjunction of element-wise equalities," but QED has no tuple/row type (all types flatten to INTEGER) and no defined tuple-equality operator, so the left-hand `(a,b,c)=(x,y,z)` can only be introduced as an uninterpreted predicate symbol that the SMT solver cannot see through as element-wise equality. I independently checked the DSL source and found no alternate encoding that avoids this: modeling the tuples as separate multi-column relations makes the before-side join condition already element-wise (identical to the after, a tautology rather than the rule), and `extend_dsl_file` can only add another uninterpreted symbol since defining tuple-equality semantics would require modifying the trusted prover. This falls squarely under the documented QED limitation of being unable to reason about a backend operator's bespoke internal semantics as an uninterpreted function, so the UNSUPPORTED claim is sound. ```
