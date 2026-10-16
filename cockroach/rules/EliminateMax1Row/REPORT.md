# EliminateMax1Row

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/max1row.opt

EliminateMax1Row discards the Max1Row operator if its input is statically
guaranteed to have no more than one row. Removing the Max1Row operator is
important when decorrelating subqueries, as it interferes with ApplyJoin
pushdown when it's present.

Extracted from `max1row.opt` (which defines multiple rules — implement specifically `EliminateMax1Row`, not the other rules in that file):

```
# EliminateMax1Row discards the Max1Row operator if its input is statically
# guaranteed to have no more than one row. Removing the Max1Row operator is
# important when decorrelating subqueries, as it interferes with ApplyJoin
# pushdown when it's present.
[EliminateMax1Row, Normalize]
(Max1Row $input:* & (HasZeroOrOneRow $input))
=>
$input
```
```

## Independent verifier review

**Verdict:** AGREE

Max1Row is an order-dependent LIMIT-1 operator ("keep the first row"), and QED's bag-semantic theory explicitly models no list/ordering semantics (Sort/Limit/Offset/Window/Sample are out of scope, and no such builder exists in RelRN), so the LHS can only be represented as an opaque uninterpreted symbol with no Max1Row(X)≡X axiom, making the equivalence unprovable even under any precondition. The HasZeroOrOneRow guard is a cardinality side condition the before/after pattern format cannot express — uninterpreted scans can't be constrained to ≤1 rows (the unique/key flag only gives functional dependency, not cardinality), and even the only structural 0-or-1-row shapes (Empty, or a no-group-key aggregate output) leave an uninterpreted Max1Row wrapper whose identity is unprovable, e.g. Max1Row(Empty)≢Empty. Dropping Max1Row from the encoding collapses the rule to the vacuous identity X≡X, and this is the identical blocker the sister rule EliminateLimit in the same .opt family hit and got an independent AGREE on — so a further attempt cannot yield a genuine, non-vacuous encoding. ```
