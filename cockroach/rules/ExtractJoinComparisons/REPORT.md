# ExtractJoinComparisons

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 28  **Verification rounds used:** 2
**Scope detail:** inner join only, one input column per side, exactly one comparison between a projection over the left column and a projection over the right column


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

ExtractJoinComparisons finds equality and inequality conditions such that
one side only depends on left columns and the other only on right columns
and pushes the expressions down into Project operators. The result is a
join that has an equality or inequality constraint, which is much more
efficient. For example:

SELECT * FROM abc JOIN xyz ON a=x+1

This join would be quadratic because we have no equality columns.
This rule rewrites it as:

SELECT a,b,c,x,y,z FROM abc JOIN (SELECT *, x+1 AS x1 FROM xyz) ON a=x1

This join can use hash join or lookup on the equality columns.

Depending on the expressions involved, one or both sides require a projection.

Extracted from `join.opt` (which defines multiple rules — implement specifically `ExtractJoinComparisons`, not the other rules in that file):

```
# ExtractJoinComparisons finds equality and inequality conditions such that
# one side only depends on left columns and the other only on right columns
# and pushes the expressions down into Project operators. The result is a
# join that has an equality or inequality constraint, which is much more
# efficient. For example:
#
#   SELECT * FROM abc JOIN xyz ON a=x+1
#
# This join would be quadratic because we have no equality columns.
# This rule rewrites it as:
#
#   SELECT a,b,c,x,y,z FROM abc JOIN (SELECT *, x+1 AS x1 FROM xyz) ON a=x1
#
# This join can use hash join or lookup on the equality columns.
#
# Depending on the expressions involved, one or both sides require a projection.
[ExtractJoinComparisons, Normalize]
(JoinNonApply
    $left:* & ^(HasOuterCols $left)
    $right:* & ^(HasOuterCols $right)
    $on:[
        ...
        $item:(FiltersItem
            (Eq | Lt | Le | Gt | Ge
                    $a:^(ConstValue)
                    $b:^(ConstValue)
                ) &
                (CanExtractJoinComparison
                    $a
                    $b
                    (OutputCols $left)
                    (OutputCols $right)
                )
        )
        ...
    ]
    $private:*
)
=>
(ExtractJoinComparison (OpName) $left $right $on $item $private)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() = L ⋈_{cmp(f(L),g(R))} R and after() = push f,g into per-side Projections, join on the synthesized columns, then drop them, are structurally distinct yet both denote {(x,y) : L(x)∧R(y)∧cmp(f(x),g(y))}, so the proof is non-vacuous and captures the rule's actual rewrite (moving computed comparison operands from the ON clause into pre-join Projections so the join key becomes a plain column). The comparison and its two operands are correctly modeled as uninterpreted symbols (cmp standing in for Eq/Lt/Le/Gt/Ge, f and g for the arbitrary side-bound expressions), the no-outer-columns guard is respected by using uncorrelated scans, and the side-split precondition (CanExtractJoinComparison) is built into the shape (f over L's column, g over R's column), so no precondition is silently dropped and no spurious symbol-sharing is making an unsound rewrite look sound. The narrowing to inner join / one column per side / a single comparison is narrower than the full JoinNonApply rule, but it is specifically and honestly labeled in the SCOPE line and remains a non-degenerate core of the optimization, so the provable result is a faithful encoding of its claimed scope rather than a misleading one. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8124209
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 34893333
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 877292
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 613250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 21608000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 34995667
  },
  "total_duration": {
    "secs": 0,
    "nanos": 72561125
  }
}
```
