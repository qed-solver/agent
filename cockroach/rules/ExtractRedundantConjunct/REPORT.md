# ExtractRedundantConjunct

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** assumes the two OR operands are each a binary conjunction sharing exactly one common conjunct, i.e. the (A AND B) OR (A AND C) => A AND (B OR C) instance


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/bool.opt

ExtractRedundantConjunct matches an OR expression in which the same conjunct
appears in both the left and right OR conditions:

A OR (A AND B)          =>  A
(A AND B) OR (A AND C)  =>  A AND (B OR C)

In both these cases, the redundant conjunct is A.

This transformation is useful for finding a conjunct that can be pushed down
in the query tree. For example, if the redundant conjunct A is fully bound by
one side of a join, it can be pushed through the join, even if B AND C cannot.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `ExtractRedundantConjunct`, not the other rules in that file):

```
# ExtractRedundantConjunct matches an OR expression in which the same conjunct
# appears in both the left and right OR conditions:
#
#   A OR (A AND B)          =>  A
#   (A AND B) OR (A AND C)  =>  A AND (B OR C)
#
# In both these cases, the redundant conjunct is A.
#
# This transformation is useful for finding a conjunct that can be pushed down
# in the query tree. For example, if the redundant conjunct A is fully bound by
# one side of a join, it can be pushed through the join, even if B AND C cannot.
[ExtractRedundantConjunct, Normalize]
(Or
    $left:^(Or)
    $right:^(Or) &
        (Let
            ($conjunct $ok):(FindRedundantConjunct $left $right)
            $ok
        )
)
=>
(ExtractRedundantConjunct $conjunct $left $right)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is exactly the source rule's documented second example, (A AND B) OR (A AND C) ⟹ A AND (B OR C), lifted to a filter over a scan with a, b, c as three independent uninterpreted predicates — `a` is shared as the redundant conjunct precisely as the FindRedundantConjunct guard requires, b and c are distinct symbols, and before()/after() are structurally different, so the proof is non-vacuous and not over-constrained. The source rule's guards (operands not themselves Or, a common conjunct exists) are pure pattern-matching restrictions satisfied by this shape rather than semantic preconditions, and the factoring identity is valid in Kleene three-valued logic as well as classical logic, so no null- or key-related assumption was silently dropped. The SCOPE: PARTIAL line is honest: restricting both OR operands to binary conjunctions is a genuine narrowing (the full rule also covers absorption shapes like A OR (A AND B) => A), but the chosen instance is the join-pushdown case the rule's own comment highlights, remains non-degenerate, and is fully general in the Boolean structure via the uninterpreted predicates. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6415831
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35209291
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 882042
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 369667
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18468583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35317000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 69940667
  }
}
```
