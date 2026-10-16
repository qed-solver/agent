# FoldEqualsAnyNull

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 10  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldEqualsAnyNull, converts a scalar ANY operation to NULL if the right-hand
side tuple is NULL, e.g. x = ANY(NULL::int[]). See #42562.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldEqualsAnyNull`, not the other rules in that file):

```
# FoldEqualsAnyNull, converts a scalar ANY operation to NULL if the right-hand
# side tuple is NULL, e.g. x = ANY(NULL::int[]). See #42562.
[FoldEqualsAnyNull, Normalize]
(AnyScalar * (Null) *)
=>
(Null (BoolType))
```
```

## Independent verifier review

**Verdict:** AGREE

I verified directly in the DSL and type sources that QED's theory has no array sort (RelType.java states all virtual types are translated to integers in the prover) and no ANY/SOME/IN operator at all — the paper's SOME is defined only over relations, where an empty relation yields False (the wrong result for a NULL array) and an all-null relation yields Null purely as a 3-valued-logic tautology, so the "NULL array" premise can only be expressed as an uninterpreted symbol (making the conclusion an assumption) or a substituted premise (which proves a tautology independent of the backend's actual null-array behavior, as the prior review already rejected). The rule's validity therefore rests on CockroachDB's bespoke `evalAny` evaluation semantics (a NULL array datum evaluates to NULL, per #42562), which is a genuine "bespoke internal semantics of a backend operator" limitation QED cannot see through, not a missing DSL builder an `extend_dsl_file` gap-fill could close.
