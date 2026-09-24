# Name: NormalizeNestedAnds
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/bool.opt

NormalizeNestedAnds ensures that And expressions are normalized into a left-
deep tree. For example, the expression:

A AND (B AND (C AND D))

would be normalized to:

And
/   \
And   D
/   \
And   C
/   \
A     B

This normalization makes conjuncts easier to traverse for other rules, such as
the ExtractRedundantConjunct rule.

Extracted from `bool.opt` (which defines multiple rules — implement specifically `NormalizeNestedAnds`, not the other rules in that file):

```
# NormalizeNestedAnds ensures that And expressions are normalized into a left-
# deep tree. For example, the expression:
#
#   A AND (B AND (C AND D))
#
# would be normalized to:
#
#         And
#        /   \
#       And   D
#      /   \
#     And   C
#    /   \
#   A     B
#
# This normalization makes conjuncts easier to traverse for other rules, such as
# the ExtractRedundantConjunct rule.
[NormalizeNestedAnds, Normalize]
(And $left:* (And $innerLeft:* $innerRight:*))
=>
(And (ConcatLeftDeepAnds $left $innerLeft) $innerRight)
```
