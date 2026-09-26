# EliminateOffset

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/limit.opt

EliminateOffset discards an Offset operator if its offset value is zero.

Extracted from `limit.opt` (which defines multiple rules — implement specifically `EliminateOffset`, not the other rules in that file):

```
# EliminateOffset discards an Offset operator if its offset value is zero.
[EliminateOffset, Normalize]
(Offset $input:* (Const 0))
=>
$input
```
```

## Independent verifier review

**Verdict:** AGREE

EliminateOffset's entire content is that skipping 0 rows is the identity, which is purely a row-ordering/list-semantic fact, and QED explicitly does not model ordering semantics — `Sort`/`Limit`/`Offset` have no bag-semantic meaning, and Offset is only an uninterpreted operator with no axioms (qed.pdf §3.1/§6.2). Hence `Offset(input) ≡ input` is unentailed for every input, including any degenerate special case, because the node being deleted is itself the unconstrained uninterpreted symbol that can't be factored out of both sides. The missing Offset builder in `RelRN` is a DSL gap, but closing it via `extend_dsl_file` (the JSON `sort` node with `offset` is already serializable) would not change the verdict, since the limitation sits in the prover's semantics, not the builder surface. ```
