# FoldArray

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

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

In any encoding, FoldArray reduces to a value equation between an uninterpreted function application and a stored constant: the `Array` constructor must be an uninterpreted projection symbol (no array operator exists in the core language), and both its constant elements and the RHS constant-array datum are, by construction of `RexRN.Const`, just the literal 0 in the prover — so QED would have to prove `arr(0,…,0) = 0` universally for an arbitrary uninterpreted `arr`, which a countermodel refutes and which no axiom/constraint mechanism in the DSL can override. QED also has no list/array value semantics (a tabulated "List semantic" failure category), and since the Rust prover is the unmodifiable trusted arbiter, `extend_dsl_file` cannot supply the missing semantics. The narrowest possible special case (fixed arity/types) has exactly the same unprovable shape, so no non-vacuous encodable form exists. ```
