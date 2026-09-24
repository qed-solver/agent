# Name: FoldFunctionWithNullArg
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/fold_constants.opt

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
