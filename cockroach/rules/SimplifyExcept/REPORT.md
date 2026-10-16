# SimplifyExcept

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 14  **Verification rounds used:** 1
**Scope detail:** 2-input, single-column, same row type, identity column mapping, with the left input a base scan declared unique (has a strict key).


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/set.opt

SimplifyExcept converts an Except operator into an ExceptAll operator when the
left input has a key. This avoids the de-duplication step.

Extracted from `set.opt` (which defines multiple rules — implement specifically `SimplifyExcept`, not the other rules in that file):

```
# SimplifyExcept converts an Except operator into an ExceptAll operator when the
# left input has a key. This avoids the de-duplication step.
[SimplifyExcept, Normalize]
(Except $left:* & (HasStrictKey $left) $right:* $colMap:*)
=>
(ExceptAll $left $right $colMap)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

before() correctly encodes set EXCEPT (`minus(all=false)`), and although after() uses an ANTI join rather than the source's `ExceptAll` (bag MINUS is unmodelable by QED's serializer, which only handles the set variant), the two are semantically identical under the encoded `HasStrictKey` precondition (unique left scan), so the proof captures the rule's actual claim — "no de-duplication step needed, output is left rows absent from right" — as a stronger, genuine universal statement that is non-vacuous (multiplicities diverge if the left key is dropped, e.g. L=[a,a], R=[b]). The two scans are properly independent symbols sharing a type (as required by minus compatibility), the key precondition is present, and the SCOPE: PARTIAL line honestly and specifically lists the real restrictions (single column, identity colmap, left a declared-unique scan).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 8133916
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 25844125
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1008708
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 566750
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 20908625
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 25953084
  },
  "total_duration": {
    "secs": 0,
    "nanos": 62428750
  }
}
```
