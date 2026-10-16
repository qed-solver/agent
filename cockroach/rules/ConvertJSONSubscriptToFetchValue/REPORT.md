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

The rule's entire content is the semantic identity `Indirection(x, y) ≡ FetchVal(x, y)` on JSON inputs — i.e., a backend operator's bespoke internal semantics relating two distinct scalar operators — and QED encodes every named operator as an independent uninterpreted function, with no axiom/alias channel anywhere in the pipeline: the JSON format carries only operator name, operands, and type, the RRule interface exposes no assumptions, and the only place such an equivalence could be declared (the Rust prover) is fixed and unmodifiable, so no `extend_dsl_file` change could close the gap. The porter also correctly avoided the only degenerate workaround — reusing a single symbol name on both sides, which would collapse before() and after() into identical trees and prove a vacuous tautology rather than the rule — so no faithful, non-trivial encoding (or meaningful special case) of this rule exists in RuleScript/QED. ```
