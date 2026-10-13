# EliminateAntiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** covers the non-correlated AntiJoin operator; the correlated AntiJoinApply variant of the source rule is not modeled.


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/join.opt

EliminateAntiJoin discards an AntiJoin operator when it's known that the right
input never returns any rows.

Extracted from `join.opt` (which defines multiple rules — implement specifically `EliminateAntiJoin`, not the other rules in that file):

```
# EliminateAntiJoin discards an AntiJoin operator when it's known that the right
# input never returns any rows.
[EliminateAntiJoin, Normalize]
(AntiJoin | AntiJoinApply
    $left:*
    $right:* & (HasZeroRows $right)
)
=>
$left
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful and non-trivial: it uses an uninterpreted left relation, a zero-row right (the canonical `Empty` form, which is exactly the `HasZeroRows` precondition and the only way the DSL can express it), an arbitrary uninterpreted anti-join condition (covering any ON-list, including empty, since QED quantifies over the predicate), and `JoinRelType.ANTI` (left anti-join) is the correct kind for CockroachDB's AntiJoin, with the condition correctly dropped in `after()` — so bag semantics give `left` exactly, with no symbol-sharing or missing-precondition issues. The single narrowing, the correlated `AntiJoinApply` variant, is explicitly disclosed in the SCOPE line, is a genuine shape-based restriction, and is semantically subsumed by the proven case (with a provably empty right side the correlation is moot, so the lemma covers the rule's entire semantic content).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 3516834
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 5496667
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 35625
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 208375
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 7699917
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 5531000
  },
  "total_duration": {
    "secs": 0,
    "nanos": 15469000
  }
}
```
