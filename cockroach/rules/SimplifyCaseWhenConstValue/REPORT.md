# SimplifyCaseWhenConstValue

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 22  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyCaseWhenConstValue removes branches known to not match. Any
branch known to match is used as the ELSE and further WHEN conditions
are skipped. If all WHEN conditions have been removed, the ELSE
expression is used.
This transforms

CASE WHEN v THEN 1 WHEN false THEN a WHEN true THEN b ELSE c END

to

CASE WHEN v THEN 1 ELSE b END

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyCaseWhenConstValue`, not the other rules in that file):

```
# SimplifyCaseWhenConstValue removes branches known to not match. Any
# branch known to match is used as the ELSE and further WHEN conditions
# are skipped. If all WHEN conditions have been removed, the ELSE
# expression is used.
# This transforms
#
#   CASE WHEN v THEN 1 WHEN false THEN a WHEN true THEN b ELSE c END
#
# to
#
#   CASE WHEN v THEN 1 ELSE b END
#
[SimplifyCaseWhenConstValue, Normalize]
(Case
    $condition:(ConstValue)
    $whens:[ ... (When (ConstValue)) ... ]
    $orElse:*
)
=>
(SimplifyWhens $condition $whens $orElse)
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests entirely on CASE's internal evaluation semantics (ordered first-true-branch selection with SQL NULL-skip), which is exactly the class of idiosyncratic operator semantics QED cannot see through: a CASE node serializes as a name-keyed uninterpreted function in the JSON, and interpretation of names happens inside the fixed Rust prover, so no `extend_dsl_file` change on the Java side can supply it. There is also no desugaring workaround — CASE cannot be rewritten in the interpreted AND/OR/NOT vocabulary, since under SQL three-valued logic `CASE WHEN NULL THEN t ELSE e END` yields `e` while `(NULL AND t) OR (NOT NULL AND e)` yields `NULL` — so even the narrowest instance (`CASE WHEN true THEN a ELSE b END` ⟹ `a`) would demand the prover interpret `Case(true,a,b)=a`. And because every matching instance of the rule strictly changes the CASE's structure (branch dropped or collapsed into ELSE), before and after are always structurally distinct uninterpreted terms, so no special case is provable. ```
