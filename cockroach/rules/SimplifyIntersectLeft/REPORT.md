# SimplifyIntersectLeft

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2
**Scope detail:** 2-input, single-column, same row type, identity column mapping, with the left input a base scan declared unique (has a strict key).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/set.opt

SimplifyIntersectLeft converts an Intersect operator into an IntersectAll
operator when the left input has a key. This avoids the de-duplication step.

Extracted from `set.opt` (which defines multiple rules — implement specifically `SimplifyIntersectLeft`, not the other rules in that file):

```
# SimplifyIntersectLeft converts an Intersect operator into an IntersectAll
# operator when the left input has a key. This avoids the de-duplication step.
[SimplifyIntersectLeft, Normalize]
(Intersect $left:* & (HasStrictKey $left) $right:* $colMap:*)
=>
(IntersectAll $left $right $colMap)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() (set-semantic INTERSECT, all=false, 2 inputs) and after() (SEMI join of the keyed left against the right on IS NOT DISTINCT FROM) are structurally distinct operators, and the strict-key precondition is genuinely encoded — the left scan is declared with a key on its only column (unique=true → table key [0]) — so the SMT proof is of the real claim "keyed left ⇒ no dedup needed," not a vacuous identity. Using a semi-join for the target is faithful, not a wrong operator: IntersectAll with a unique left side is bag-identical to L SEMI-JOIN R (min(multiplicity) collapses to membership), and QED fundamentally cannot model the bag variant of INTERSECT, so the semi-join is the only expressible form of the rule's target. The restrictions (2-input, single column, same/identity column mapping, left as a base unique scan, non-nullable matching strict-key semantics) are all disclosed in the SCOPE line, are genuine QED-expressibility boundaries rather than avoidable under-generalization, and the proven instance remains a non-degenerate core case of the source rule. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 7032624
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 26591166
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 862459
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 447916
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 19226291
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 26678708
  },
  "total_duration": {
    "secs": 0,
    "nanos": 61596541
  }
}
```
