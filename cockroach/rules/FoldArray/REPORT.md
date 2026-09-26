# FoldArray

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 41  **Verification rounds used:** 3

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldArray evaluates an Array expression with constant inputs. It replaces the
Array with a Const datum with type TArray.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldArray`, not the other rules in that file):

```
# FoldArray evaluates an Array expression with constant inputs. It replaces the
# Array with a Const datum with type TArray.
[FoldArray, Normalize]
(Array $elems:* & (IsListOfConstants $elems) $typ:*)
=>
(FoldArray $elems $typ)
```
```

## Independent verifier review

**Verdict:** AGREE

FoldArray rewrites an uninterpreted Array constructor applied to constant arguments into a concrete TArray ground constant, but QED models the Array constructor as an uninterpreted function with no axioms relating its application to any ground term, so the SMT solver can never establish Array(c1,c2,…)=arrayConstant; additionally the DSL has no array-literal builder (RexRN exposes only boolean true/false literals, and the QED JSON format carries no array type or array-value semantics), so even the RHS is inexpressible — the only encodable encoding reuses the same uninterpreted symbol on both sides, yielding a vacuous identity rather than a genuine rewrite. ```
