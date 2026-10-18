# BitwiseXorByZero

**Status:** SKIPPED
**Source backend:** Apache DataFusion
**Porter attempts used:** 31  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: datafusion/optimizer/src/simplify_expressions/expr_simplifier.rs, lines 1376-1381
```

## Independent verifier review

**Verdict:** AGREE

The rule's core claim is a semantic identity of DataFusion's XOR operator (`A ^ 0 = A`, and in the recursive branch `X ^ X = 0` plus associativity/commutativity and structural occurrence-counting), but in RuleScript the XOR can only be introduced as an uninterpreted function symbol, and QED's fixed theory (equality/UF, natural-number +, ite, 3-valued And/Or/Not) contains no bitwise interpretation and no mechanism to attach such axioms to a symbol — so `xor(a, 0) = a` is false for some UF instantiations and SMT will always produce a countermodel, making the empirically observed provable=false a genuine limitation rather than an encoding bug (the Const/VarType NPE was a red herring; the final encoding serialized fine and still failed for the fundamental reason). No `extend_dsl_file` change can close the gap because the missing knowledge (XOR's group-identity law) would have to live in the immutable prover's SMT theory, and no non-trivial special case (integer, constant, or Boolean) avoids needing that operator's internal semantics.
