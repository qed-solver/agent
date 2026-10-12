# ReplaceCTERefWithRepartition

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Spark Catalyst
**Porter attempts used:** 7  **Verification rounds used:** 1
**Scope detail:** only the per-reference bag-level core is covered: a CTE reference (a transparent logical alias of the CTE definition) is replaced by a Repartition over the definition, proved as the repartition shuffle being bag-identical to its input; the multi-reference shuffle reuse, the canSkipExtraRepartition/underSubquery guards, and the output-set-mismatch self-join re-aliasing are physical/attribute-level effects QED does not model.


## Source rule (as given to the porter)

```
Source: sql/catalyst/src/main/scala/org/apache/spark/sql/catalyst/optimizer/ReplaceCTERefWithRepartition.scala, lines 37-95
```

## Independent verifier review

**Verdict:** CONFIRMED

The only bag-level content of ReplaceCTERefWithRepartition is that a CTERelationRef transparently denotes the CTE definition's bag and the RepartitionByExpression it is swapped in for is bag-identical to its input — the encoding captures exactly that with one uninterpreted definition relation (correctly shared between before/after, since the ref is a transparent alias) and an identity projection standing in for the shuffle, and `cte` vs `cte.project(cte.fields())` are structurally distinct so the proof is not vacuous. No concrete predicates, types, or join kinds are baked in, and the genuinely unmodelable residue — single-shuffle reuse across multiple refs, the canSkipExtraRepartition/underSubquery guards, and the self-join re-aliasing on output-set mismatch — is physical/attribute-level (invisible to bag semantics, including the guards, since adding or skipping the repartition is a bag no-op either way) and is specifically enumerated in the SCOPE: PARTIAL line, so the narrower scope is both honest and the strongest claim QED's bag model can support for this rule. ```

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
    "nanos": 265875
  }
}
```
