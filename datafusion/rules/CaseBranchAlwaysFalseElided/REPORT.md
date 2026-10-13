# CaseBranchAlwaysFalseElided

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 8  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1525-1562
```

## Independent verifier review

**Verdict:** AGREE

The rule is pure scalar CASE algebra whose correctness rests entirely on CASE's branch-selection semantics — a value-level ite (first-TRUE branch wins; false/NULL conditions fall through to the next branch/ELSE) — and QED's interpreted fragment has no ite: I independently verified `RexRN` offers only And/Or/Not/boolean literals plus uninterpreted `pred`/`proj` symbols, and `JSONSerializer` passes any scalar operator to the fixed Rust prover as a bare name that unmodeled operators receive uninterpreted treatment for, so a CASE minted via `extend_dsl_file` could never be universally equated with its then/else/NULL forms (e.g. `case(false,a,b) ≡ b` fails for an arbitrary function symbol). No relational reconstruction is faithful either: a Filter/Union re-encoding of branch selection drops NULL-condition rows that CASE would send to ELSE (unsound for the non-literal branches the rule preserves), and for the constant-condition branches the only interpretable "encoding" collapses to the already-rewritten relation, making the proof vacuous — the before side of every instance of the rule contains a CASE that nothing in the fragment can name, so no provable special case exists. The gap is therefore in the prover's fixed operator interpretations, which are off-limits to modify (extend_dsl_file only touches the Java builder/serializer layer), so the porter's UNSUPPORTED verdict is correct.
