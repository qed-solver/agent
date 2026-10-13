# MergeSelectInnerJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 21  **Verification rounds used:** 2
**Scope detail:** non-Apply InnerJoin only; the ON condition and the Select's filter list are each abstracted as a single uninterpreted predicate


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/select.opt

MergeSelectInnerJoin merges a Select operator with an InnerJoin input by
AND'ing the filter conditions of each and creating a new InnerJoin with that
On condition. This is only safe to do with InnerJoin in the general case
where the conditions could filter either left or right rows. The special case
where a condition filters only one or the other is already taken care of by
the PushSelectIntoJoin rules.
NOTE: Keep this rule ordered before the PushSelectIntoJoin rules to avoid
missing out on the potential for new filter inference based on
equivalent columns.

Extracted from `select.opt` (which defines multiple rules — implement specifically `MergeSelectInnerJoin`, not the other rules in that file):

```
# MergeSelectInnerJoin merges a Select operator with an InnerJoin input by
# AND'ing the filter conditions of each and creating a new InnerJoin with that
# On condition. This is only safe to do with InnerJoin in the general case
# where the conditions could filter either left or right rows. The special case
# where a condition filters only one or the other is already taken care of by
# the PushSelectIntoJoin rules.
# NOTE: Keep this rule ordered before the PushSelectIntoJoin rules to avoid
#       missing out on the potential for new filter inference based on
#       equivalent columns.
[MergeSelectInnerJoin, Normalize]
(Select
    $input:(InnerJoin | InnerJoinApply
        $left:*
        $right:*
        $on:*
        $private:*
    )
    $filters:*
)
=>
((OpName $input)
    $left
    $right
    (ConcatFilters $on $filters)
    $private
)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The proof is non-vacuous and captures the rule's exact logical core — before() is Filter(F, InnerJoin(L, R, ON)) and after() is InnerJoin(L, R, ON ∧ F), with ON and F as distinct uninterpreted predicates over the full joined (L++R) row, the correct INNER join kind (the source rule is by design restricted to inner joins), and no silently-added preconditions, since Optgen's rule has none either (and abstracting the filter *list* as one uninterpreted predicate is harmless, as the rule's claim is only about relocating the conjunction). The single genuine narrowing — omitting the InnerJoinApply (correlated) variant — is explicitly and specifically tagged in the SCOPE line, is a real limitation of the current DSL (Correlate's right-side filter is one monolithic condition and cannot AND in an independently built predicate without an extension), and leaves a useful, fully general, non-degenerate rule, so the "provable" result is neither vacuous nor misleading.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6837582
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 33847583
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 900917
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 434959
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 18788583
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 33957666
  },
  "total_duration": {
    "secs": 0,
    "nanos": 68062375
  }
}
```
