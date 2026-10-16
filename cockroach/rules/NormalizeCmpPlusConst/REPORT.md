# NormalizeCmpPlusConst

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 65  **Verification rounds used:** 4
**Scope detail:** only the Lt operator of the original rule's Eq|Ge|Gt|Le|Lt comparison family is covered


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeCmpPlusConst builds up constant expression trees on one side of the
comparison, in cases like this:
cmp          cmp
/   \        /   \
[+]    2  ->  a   [-]
/   \             /   \
a     1           2     1

The rule can only perform this transformation if all of the following criteria
are met:

1. The generated Minus expression will error if there is an overflow (see
ArithmeticErrorsOnOverflow).
2. A Minus overload for the given input types exists and has an appropriate
volatility.
2. There is no error when evaluating the new binary expression.

NOTE: Ne is not part of the operator choices because it wasn't handled in
normalize.go either. We can add once we've proved it's OK to do so.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeCmpPlusConst`, not the other rules in that file):

```
# NormalizeCmpPlusConst builds up constant expression trees on one side of the
# comparison, in cases like this:
#       cmp          cmp
#      /   \        /   \
#    [+]    2  ->  a   [-]
#   /   \             /   \
#  a     1           2     1
#
# The rule can only perform this transformation if all of the following criteria
# are met:
#
#   1. The generated Minus expression will error if there is an overflow (see
#      ArithmeticErrorsOnOverflow).
#   2. A Minus overload for the given input types exists and has an appropriate
#      volatility.
#  2. There is no error when evaluating the new binary expression.
#
# NOTE: Ne is not part of the operator choices because it wasn't handled in
#       normalize.go either. We can add once we've proved it's OK to do so.
[NormalizeCmpPlusConst, Normalize]
(Eq | Ge | Gt | Le | Lt
    (Plus $leftLeft:^(ConstValue) $leftRight:(Const))
    $right:(Const) &
        (ArithmeticErrorsOnOverflow
            (TypeOf $right)
            (TypeOf $leftRight)
        ) &
        (CanConstructBinary Minus $right $leftRight) &
        (Let
            ($result $ok):(FoldBinary Minus $right $leftRight)
            $ok
        )
)
=>
((OpName) $leftLeft $result)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's core transformation — (a + c1) < c2 ⇒ a < (c2 − c1), with correct Plus/Minus operand directions — and concrete INTEGER arithmetic is forced by provability, since QED cannot reason about uninterpreted +/−/< and its no-wraparound integer theory (with matching null propagation) corresponds exactly to the source rule's ArithmeticErrorsOnOverflow guard. The 3-column scan is sound because QED's bag-equality proof over all table contents establishes the pointwise predicate equivalence for all value triples, and the only restriction — Lt rather than the full Eq|Ge|Gt|Le|Lt family — is a genuine DSL gap (no scalar-operator-family mechanism exists, unlike MetaJoinType for joins) that is accurately and specifically tagged as PARTIAL; before/after are structurally distinct and the proof is non-vacuous, so this is a useful, non-degenerate special case.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5468126
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 8557625
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 37125
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 248250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 9999625
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 8594375
  },
  "total_duration": {
    "secs": 0,
    "nanos": 21330042
  }
}
```
