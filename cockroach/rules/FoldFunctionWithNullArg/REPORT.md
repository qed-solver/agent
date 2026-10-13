# FoldFunctionWithNullArg

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 61  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/fold_constants.opt

FoldFunctionWithNullArg folds a Function to Null when one of its arguments is
Null and all of the following are true:

1. The function is not called when any of its inputs are null
(CalledOnNullInput=false).
2. The function is a normal function not an aggregate, window, or generator.

It is safe to fold functions to Null in this case because a function with
CalledOnNullInput=false would never error with a Null argument, even if the
other args are invalid. For example, calling encode with NULL bytes and an
invalid encoding format does not error:

SELECT encode(NULL::BYTES, 'foo')
=> NULL

Stable and volatile functions that rely on context or produce side-effects can
also be folded to Null in this case because a function with
CalledOnNullInput=false is never evaluated if any of its arguments are Null.
The function results directly in Null without being invoked, so it is
guaranteed not to rely on context or produce side-effects. See
Overload.CalledOnNullInput for more details.

FoldFunctionWithNullArg is defined before FoldFunction so that we can avoid
the overhead of evaluating the function in FoldFunction if it has any Null
arguments.

Extracted from `fold_constants.opt` (which defines multiple rules — implement specifically `FoldFunctionWithNullArg`, not the other rules in that file):

```
# FoldFunctionWithNullArg folds a Function to Null when one of its arguments is
# Null and all of the following are true:
#
#   1. The function is not called when any of its inputs are null
#      (CalledOnNullInput=false).
#   2. The function is a normal function not an aggregate, window, or generator.
#
# It is safe to fold functions to Null in this case because a function with
# CalledOnNullInput=false would never error with a Null argument, even if the
# other args are invalid. For example, calling encode with NULL bytes and an
# invalid encoding format does not error:
#
#     SELECT encode(NULL::BYTES, 'foo')
#       => NULL
#
# Stable and volatile functions that rely on context or produce side-effects can
# also be folded to Null in this case because a function with
# CalledOnNullInput=false is never evaluated if any of its arguments are Null.
# The function results directly in Null without being invoked, so it is
# guaranteed not to rely on context or produce side-effects. See
# Overload.CalledOnNullInput for more details.
#
# FoldFunctionWithNullArg is defined before FoldFunction so that we can avoid
# the overhead of evaluating the function in FoldFunction if it has any Null
# arguments.
[FoldFunctionWithNullArg, Normalize]
(Function
    $args:*
    $private:* &
        (CanFoldFunctionWithNullArg $private) &
        (HasNullArg $args)
)
=>
(Null (FunctionReturnType $private))
```
```

## Independent verifier review

**Verdict:** AGREE

The rule's soundness rests on backend metadata about the function itself (CalledOnNullInput=false ⇒ a NULL argument forces a NULL result), but in QED's SMT encoding every scalar function symbol introduced by the DSL is a pure uninterpreted function, and null-propagation axioms are baked in only for QED's own primitives — so for any uninterpreted 1- or 2-arg f, `Project(f(…, NULL, …), S) ≡ Project(NULL, S)` is refuted by a model where f returns a non-NULL value on a NULL-containing tuple, and no relational trick (filters, joins, or table "guaranteed" constraints) can supply the missing axiom, since the gap is in the prover's semantics of the symbol, not in DSL expressiveness (granting blanket null-propagation to all uninterpreted ops would itself be unsound, e.g. for COALESCE/NULLIF). The only conceivable special case is instantiating the function with a built-in operator that genuinely propagates NULL — of the DSL-exposable And/Or/Not, only Not does (Not(NULL)→NULL), a degenerate single-operator instance that isn't really this rule and that the DSL can't even state without adding a NULL-literal builder. The porter's complete-fragment counterexamples on the 1-arg and 2-arg probes are consistent with this, so the UNSUPPORTED conclusion is correct.
