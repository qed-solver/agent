# ReorderPredicatesByCost

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache DataFusion
**Porter attempts used:** 5  **Verification rounds used:** 1
**Scope detail:** models the rule as transposing one cheap and one expensive conjunct in a Filter's conjunction (the minimal non-sorted two-conjunct case) rather than stably partitioning an arbitrary-length conjunct list, and omits the backend's firing-only guards (volatile-predicate check, cheap-node allow-list) which have no bag-semantic content


## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/reorder_predicates.rs, lines 1-133
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding is non-vacuous and faithful to the rule's only bag-semantic core: `before()` and `after()` differ structurally (conjunct order swapped), and the two conjuncts are independent uninterpreted predicates (`cheap`/`expensive`, distinct symbols over the shared `Source` scan), so QED genuinely proved that reordering filter conjuncts preserves the relation for all instantiations — which is exactly what the DataFusion rule must guarantee, and is in fact stronger than the rule needs (it holds for arbitrary conjuncts, not just allow-listed cheap/expensive pairs). The narrowing to the two-conjunct swap is forced by the DSL (an `And` is a fixed-arity tree; there is no symbolic conjunct-list construct that a DSL extension could expose to the prover), is precisely the rule's minimal firing case, and the omitted volatile-check/allow-list guards are side-effect and cost-model matters with no content in QED's pure bag semantics. The `// SCOPE: PARTIAL` line is specific and accurate about both the arity restriction and the dropped guards, so the provable result is a genuine, non-degenerate special case, not a vacuous one.

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": true,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 5250333
  },
  "equiv_class_timed_out": false,
  "smt_duration": {
    "secs": 0,
    "nanos": 35806083
  },
  "smt_timed_out": false,
  "nontrivial_perms": false,
  "translate_duration": {
    "secs": 0,
    "nanos": 823250
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 282250
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 16092459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 35901958
  },
  "total_duration": {
    "secs": 0,
    "nanos": 67480333
  }
}
```
