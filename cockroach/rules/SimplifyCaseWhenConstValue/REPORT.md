# SimplifyCaseWhenConstValue

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

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

QED's scalar fragment only interprets boolean structure (AND/OR/NOT, true/false literals); a CASE node reaches the prover only via JSONSerializer's generic RexCall path as an uninterpreted function symbol, so SMT cannot derive that Case(true, a, b) selects a or that a const-false WHEN branch is dead — the rule's soundness rests entirely on CASE's first-match branch-selection semantics, i.e. bespoke internal operator semantics QED cannot see through. Since the Rust prover is the fixed arbiter and a Java-side DSL extension (e.g. a RexRN.Case builder) would merely serialize another uninterpreted symbol, no encoding is provable — not even a narrower special case, because any faithful instance must contain an actual CASE node in the before pattern, and no non-trivial identity is derivable between an uninterpreted Case term and its branch-simplified form. ```
