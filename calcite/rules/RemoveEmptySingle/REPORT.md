# RemoveEmptySingle

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** Apache Calcite
**Porter attempts used:** 10  **Verification rounds used:** 1
**Scope detail:** encodes the AGGREGATE variant of Calcite's RemoveEmptySingleRule: a non-grand-total (grouped) aggregate whose single input is empty collapses to the empty relation of the aggregate's output row type; the project/filter/calc/sort/window variants of the same rule are not covered here.


## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/PruneEmptyRules.java

Note: PruneEmptyRules.java defines multiple distinct rule variants as separate static nested classes. Implement specifically the `RemoveEmptySingleRule` variant (not PruneEmptyRule, which are separate rules ported under their own spec names).
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding faithfully captures the AGGREGATE variant of RemoveEmptySingleRule — a non-grand-total aggregate (group set non-empty, matching the isNotGrandTotal precondition) over an empty Values of the input row type, rewritten to an empty Values of the aggregate's output row type (group column + f column), exactly as the source onMatch builds — with no hidden preconditions, no concrete predicates, and f/S_Type left uninterpreted. The proof is non-vacuous (before() contains a real group operator that after() eliminates; they are structurally distinct, not a trivial before()==after()), and no over-constrained symbol sharing changes the claim, since the input is empty and the aggregate's algebra is irrelevant. The scope is honestly PARTIAL: the variant choice is declared in the SCOPE line (project/filter/calc are also expressible but unchosen; sort/window are genuinely out of QED's bag-semantic reach), and the code comment discloses the further instantiation to one group key plus one aggregate call on column 0 — a specific, non-degenerate special case, since the theorem "grouped aggregate over empty input = empty" is independent of the number of keys/calls. ```

## QED prover result

```json
{
  "provable": true,
  "panicked": false,
  "complete_fragment": false,
  "equiv_class_duration": {
    "secs": 0,
    "nanos": 112667
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
    "nanos": 845959
  },
  "normal_duration": {
    "secs": 0,
    "nanos": 327583
  },
  "stable_duration": {
    "secs": 0,
    "nanos": 12353459
  },
  "unify_duration": {
    "secs": 0,
    "nanos": 0
  },
  "total_duration": {
    "secs": 0,
    "nanos": 28738167
  }
}
```
