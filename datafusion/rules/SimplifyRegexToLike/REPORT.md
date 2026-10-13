# SimplifyRegexToLike

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 13  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1710-1723
```

## Independent verifier review

**Verdict:** AGREE

The rule's entire semantic content is a string pattern-matching axiom (e.g. `x ~ '^foo$'` ⟺ `x = 'foo'`, `x ~ 'foo'` ⟺ `x LIKE '%foo%'`), but QED lowers all column types to integers and treats each operator as a distinct uninterpreted predicate, so `regex(x, p)`, `like(x, p')`, `eq(x, c)`, and `isNotNull(x)` remain unrelated symbols over which the equivalence must hold for *all* instantiations — and the SMT layer has no string/regex/glob theory to connect them, making the before/after pairs inequivalent under QED's universal quantification. No DSL extension can close this: a new builder would only emit yet another uninterpreted symbol into the prover's JSON, and the only encoding that would be provable is one where both sides share a single predicate symbol, which is the vacuous identity rule, not the rewrite — so the porter's UNSUPPORTED verdict and its diagnosis (gap in the unmodifiable prover's theory, not a missing operator shape) are correct.
