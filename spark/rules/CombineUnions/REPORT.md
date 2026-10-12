# CombineUnions

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 4  **Verification rounds used:** 1
**Scope detail:** fixed-shape instance of the flatten: a Distinct over a 3-input UNION ALL whose middle input is a nested 2-input Distinct(UNION ALL), flattened to a single 4-branch union with the redundant inner Distinct removed (Spark's rule handles arbitrary nesting depth/arity and also collapses nested projects and pushes projections through the union).


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/Optimizer.scala, lines 2314-2388
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is a faithful fixed-shape instance of Spark's rule: running Spark's stack algorithm on exactly this input (Distinct over Union(A, Distinct(Union(B,C)), D)) yields precisely Distinct(UNION ALL(A,B,C,D)), so the encoding matches the rule's actual rewrite, not a superficially similar one. Operators are correct — `all=true` for UNION ALL (Spark's `Union`), and both top and nested `Distinct`/`Deduplicate(keys=output)` are modeled as group-by-all-columns with no aggregates, which captures the rule's `AttributeSet(keys) == u.outputSet` precondition; the shared type "T" across scans reflects the required same-schema union (`isSameType`) while A–D remain independent uninterpreted relations, and before()/after() are structurally distinct so the proof is non-vacuous. The proof therefore establishes the genuinely non-trivial content of the rule — that a nested distinct is redundant under an outer distinct after union-all flattening — and the PARTIAL scope tag is honest: the full rule additionally handles arbitrary arity/depth, bare-union flattening (flattenDistinct=false), and project collapse/pushdown, which this fixed 3/2-shape instance does not cover. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 17860414
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 28662834
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 1165542
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 936791
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 40859000
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 28811292
  },
  "total_duration": {
    "secs": 0,
    "nanos": 86774625
  }
}
```
