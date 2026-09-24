# ConvertJSONSubscriptToFetchValue

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 20  **Verification rounds used:** 1

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

ConvertJSONSubscriptToFetchValue rewrites `Indirection(input, index)` into `FetchVal(input, index)` — two distinct backend operators — and its correctness rests entirely on the internal JSON semantics of those two functions, which RuleScript can only introduce as two distinct uninterpreted projection symbols. QED has no axioms relating different uninterpreted function symbols (and the `IsJSON` guard is at best a type tag, since all virtual types map to INTEGER), so a countermodel exists where the two functions differ on JSON-valued inputs, making the equivalence unprovable for every instantiation. This is a fundamental limitation rather than an encoding bug: the JSON serialization carries only operator names, so no `extend_dsl_file` change to RelRN/RexRN can supply the required axiom — that would live in the QED prover itself, which is off-limits (the porter's recorded "reason" was actually an LLM context-length crash, not an analysis, but a fresh attempt would hit this same wall). ```
