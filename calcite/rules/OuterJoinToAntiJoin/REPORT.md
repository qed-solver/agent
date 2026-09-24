# OuterJoinToAntiJoin

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 1  **Verification rounds used:** 1
**Scope detail:** the LEFT-join case in which the null-generating (right) input's column is declared non-nullable and the filter sits directly above the join (a surviving left-only conjunct is not modeled).


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/OuterJoinToAntiJoinRule.java
```

## Independent verifier review

**Verdict:** MANUAL

Hand-implemented by the harness operator. This is the same LEFT-join/non-nullable-right-column special case worked out across this rule's automated rounds (round_05_summary.md) and stashed under UnprovableRRuleInstances/OuterJoinToAntiJoin.java.rejected — but that exact stashed source was re-verified fresh here and genuinely returned provable=false (its own stale result.json, claiming provable=true, did not match a fresh run and must have been left over from an earlier, different candidate never actually finalized). Root cause found by direct probing of the real Calcite RelBuilder: RelBuilder.join(LEFT, ...) does NOT widen the null-generating (right) side's declared nullability on the join's own row type, so a plain field(1) reference to the right column still carries a NOT NULL Calcite type, and Calcite's own RexSimplify constant-folds IS_NULL(it) to FALSE before QED ever sees the rule, collapsing before() to an empty relation. A first fix attempt (bypassing the RelRN.Join.field() override to read the join's own row-type field via RexRN.Field instead of RexRN.JoinField) did not help, since the join's OWN row type is equally unwidened. A second fix attempt using RelDataTypeFactory.createTypeWithNullability also silently failed, confirmed by direct probe (forced-nullable type still printed nullable=false) because RelType.VarType overrides isNullable() directly and doesn't participate in the factory's normal type-rebuilding. The working fix constructs a fresh `new RelType.VarType("R_T", true)` by hand and builds the RexInputRef directly from it (see the rule-local ForcedTypeRef record), bypassing both the buggy field-access path and the no-op factory method. With that, before()'s plan genuinely retains the LogicalFilter(IS NULL($1)) over the real LEFT join (verified via the explain-plan `help` field, not just trusting the provable flag), and QED proved the equivalence to the ANTI join in `after()` on the first true attempt (smt_duration > 0, not vacuous). SCOPE: PARTIAL is honest — single uninterpreted binary predicate, no additional filter conjuncts, non-nullable right column, LEFT join only (not RIGHT).

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 6689086
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 7452917
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 70375
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 644666
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 14230416
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 7502541
  },
  "total_duration": {
    "secs": 0,
    "nanos": 24485417
  }
}
```
