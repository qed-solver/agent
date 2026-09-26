# FoldNonNullTupleIsTupleNull

**Status:** PROVED  **Scope:** PARTIAL
**Source backend:** CockroachDB
**Porter attempts used:** 3  **Verification rounds used:** 1
**Scope detail:** assumes the tuple's non-null constant element is the first element (second element arbitrary, nullable)


## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/comp.opt

FoldNonNullTupleIsTupleNull replaces x IS NULL with False if x is a tuple
with at least one constant, non-null element.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `FoldNonNullTupleIsTupleNull`, not the other rules in that file):

```
# FoldNonNullTupleIsTupleNull replaces x IS NULL with False if x is a tuple
# with at least one constant, non-null element.
[FoldNonNullTupleIsTupleNull, Normalize]
(IsTupleNull $input:(Tuple) & (HasNonNullElement $input))
=>
(False)
```
```

## Independent verifier review

**Verdict:** CONFIRMED

The encoding correctly relationalizes the scalar fold: before() is Filter(AND(e0 IS NULL, e1 IS NULL), source) — the exact semantics of IsTupleNull expanded over the tuple's elements — and after() is Filter(False, source), a non-vacuous proof that genuinely requires QED to use the non-nullable type of element 0 (had it ignored nullability, a (null, null) row would be a countermodel). The non-nullable VarType is the faithful stand-in for the side condition (HasNonNullElement): a non-null constant element implies a non-nullable column, so the proven family subsumes the rule's applicable instances with no missing precondition, while element 1 is left fully arbitrary (any value, nullable), exactly as the rule requires. The only restrictions are tuple arity 2 and the non-null element being at position 0, which are inherent to a single fixed-arity record (you cannot express "at least one of N positions" in one rule, and marking both columns non-nullable would over-restrict) and are honestly and specifically disclosed in the PARTIAL scope line. ```

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
    "nanos": 342875
  }
}
```
