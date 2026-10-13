# EliminateGroupByProject

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2
**Scope detail:** assumes the GroupBy is a DistinctOn (no aggregate output columns) over a fixed 2-column base whose project child only drops the second column (synthesizing no new expressions, so OutputCols(project) ⊆ OutputCols(base)) and the group key is the retained first column.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/groupby.opt

EliminateGroupByProject discards a nested Project operator that is only
removing columns from its input (and not synthesizing new ones). That's
something the GroupBy operators can do on their own. This rule does not match
UpsertDistinctOn expressions because they are not built with a Project as a
child, so there is no Project to eliminate.

Note: EliminateGroupByProject should be located above
EliminateJoinUnderGroupByLeft so that it can remove any interfering Projects.

Extracted from `groupby.opt` (which defines multiple rules — implement specifically `EliminateGroupByProject`, not the other rules in that file):

```
# EliminateGroupByProject discards a nested Project operator that is only
# removing columns from its input (and not synthesizing new ones). That's
# something the GroupBy operators can do on their own. This rule does not match
# UpsertDistinctOn expressions because they are not built with a Project as a
# child, so there is no Project to eliminate.
#
# Note: EliminateGroupByProject should be located above
# EliminateJoinUnderGroupByLeft so that it can remove any interfering Projects.
[EliminateGroupByProject, Normalize]
(GroupBy | ScalarGroupBy | DistinctOn | EnsureDistinctOn
        | EnsureUpsertDistinctOn
    $input:(Project $innerInput:*) &
        (ColsAreSubset
            (OutputCols $input)
            (OutputCols $innerInput)
        )
    $aggregations:*
    $groupingPrivate:*
)
=>
((OpName) $innerInput $aggregations $groupingPrivate)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

`before()` (Aggregate over the column-dropping Project) and `after()` (Aggregate directly over the base) are structurally distinct, so the proof is non-vacuous and genuinely establishes that a pure column-dropping projection is transparent to grouping on the retained column — exactly the source rule's core claim, with its ColsAreSubset precondition satisfied by construction and the shared `S` scan/field references correctly mapping the pre- and post-elimination group keys. The only hard-coding is structural width (2 columns, fixed drop pattern), which the DSL cannot express symbolically, plus the deliberate DistinctOn/no-aggs instance, and the `// SCOPE: PARTIAL` line accurately and specifically names every assumption, so the provable result is faithful, non-degenerate, and not misleading.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8127667
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35842708
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 897292
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 439208
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 22340000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35943083
  },
  "total_duration": {
    "secs": 0,
    "nanos": 74522375
  }
}
```
