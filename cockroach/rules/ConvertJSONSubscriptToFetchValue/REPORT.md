# ConvertJSONSubscriptToFetchValue

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 25  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

ConvertJSONSubscriptToFetchValue converts json subscripting
`[...]` into a fetch value operator `->`.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `ConvertJSONSubscriptToFetchValue`, not the other rules in that file):

```
# ConvertJSONSubscriptToFetchValue converts json subscripting
# `[...]` into a fetch value operator `->`.
[ConvertJSONSubscriptToFetchValue, Normalize]
(Indirection $input:* $index:* & (IsJSON $input))
=>
(FetchVal $input $index)
```
```

## Independent verifier review

**Verdict:** AGREE

This rule is purely an operator-aliasing identity: it asserts that two syntactically distinct scalar operators (JSON indirection `[...]` and fetch value `->`) agree on JSON inputs. In RuleScript/QED that forces two *distinct* uninterpreted projection symbols, and QED's theory (bag semantics over uninterpreted functions) contains no axiom relating distinct function symbols — so a countermodel exists even restricted to the guarded (IsJSON) domain, since the guard is itself uninterpreted and the two functions can be instantiated to differ on it. No encoding or DSL extension can close this: the JSON format has no notion of "these two operator names denote the same function," and the prover (the unchangeable arbiter) is the only place such an axiom could live — this is exactly the "backend operator's bespoke internal semantics" limitation, and the porter's complete (non-timeout) SMT refutation on the fixed two-column encoding confirms it. ```
