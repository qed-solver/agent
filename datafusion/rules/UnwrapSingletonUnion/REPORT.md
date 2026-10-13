# UnwrapSingletonUnion

**Status:** PROVED  **Scope:** FULL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** public record UnwrapSingletonUnion() implements RRule {


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/optimize_unions.rs, lines 60-62
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is faithful and non-trivial: `before()` is a `Union(all=true)` with exactly one source and `after()` is that same source, precisely mirroring the `inputs.len() == 1` guard, and DataFusion's `Union` node is plain concatenation (UNION ALL; distinct `UNION` is built as a separate Distinct/Aggregate above it), so `union(true)` is the correct bag-semantic operator. The single uninterpreted scan "Input" is the right stand-in for the arbitrary input plan — the rule rewrites only the Union node and passes the input through untouched, so the proof over all instantiations of the uninterpreted relation covers the full rule with no structural generality lost. There are no hidden preconditions in the source rule (no key/NOT NULL assumptions), and the shared symbol is the single, necessary relation on both sides, so SCOPE: FULL is honest.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 0
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 0
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 0
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 0
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 0
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 280292
  }
}
```
