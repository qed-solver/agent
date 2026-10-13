# CommuteConst

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2
**Scope detail:** only the Eq variant is covered, as a top-level EQUALS filter over a two-column row (inner cross join of two single-column scans of one shared type), proving the universal commutativity law x = y <-> y = x under three-valued semantics; the Plus/Mult/Bit* variants would be uninterpreted projections whose commutativity QED fundamentally cannot know


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

CommuteConst ensures that "constant expression trees" are on the right side
of commutative comparison and binary operators. A constant expression tree
has no unbound variables that refer to outer columns. It therefore always
evaluates to the same result. Note that this is possible even if the tree
contains variable expressions, as long as they are bound, such as in
uncorrelated subqueries:

SELECT * FROM a WHERE a.x = (SELECT SUM(b.x) FROM b)

The right side of the equality expression is a constant expression tree, even
though it contains an entire subquery, because it always evaluates to the same
result. The left side is not a constant expression tree, even though it
contains just a single variable, because its value can be different for each
row in the table "a".

The goal of this and related patterns is to push constant expression trees to
the right side until only a Variable remains on the left (if possible). Other
patterns can rely on this normal form and only handle one combination.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `CommuteConst`, not the other rules in that file):

```
# CommuteConst ensures that "constant expression trees" are on the right side
# of commutative comparison and binary operators. A constant expression tree
# has no unbound variables that refer to outer columns. It therefore always
# evaluates to the same result. Note that this is possible even if the tree
# contains variable expressions, as long as they are bound, such as in
# uncorrelated subqueries:
#
#   SELECT * FROM a WHERE a.x = (SELECT SUM(b.x) FROM b)
#
# The right side of the equality expression is a constant expression tree, even
# though it contains an entire subquery, because it always evaluates to the same
# result. The left side is not a constant expression tree, even though it
# contains just a single variable, because its value can be different for each
# row in the table "a".
#
# The goal of this and related patterns is to push constant expression trees to
# the right side until only a Variable remains on the left (if possible). Other
# patterns can rely on this normal form and only handle one combination.
[CommuteConst, Normalize]
(Eq | Ne | Is | IsNot | Plus | Mult | Bitand | Bitor | Bitxor
    $left:(ConstValue)
    $right:^(ConstValue)
)
=>
((OpName) $right $left)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the rule's Eq-case semantic core—commutativity of equality—by deliberately using the concrete SqlStdOperatorTable.EQUALS operator (not an uninterpreted pred), which is exactly what lets QED know the operator is commutative and prove the non-vacuous swap x = y ↔ y = x under three-valued logic; before() and after() are structurally distinct (operand order in the predicate), so the proof is not vacuous. The join is identical scaffolding on both sides, x and y are independent columns from two distinct scans, and no source precondition is silently dropped, since the Eq swap holds under full three-valued semantics (verified across the NULL cases). The scope is honestly and specifically PARTIAL—only the Eq variant of the 9-operator rule, with the arithmetic/bitwise variants correctly identified as beyond QED's commutativity reasoning—so this is a genuine, non-degenerate special case rather than a misleading or trivially-provable encoding. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6261999
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 7431291
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 51750
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 362792
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 12257459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 7479250
  },
  "total_duration": {
    "secs": 0,
    "nanos": 22556750
  }
}
```
