# Name: NormalizeTupleEquality
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/comp.opt

NormalizeTupleEquality breaks up expressions like:
(a, b, c) = (x, y, z)
into
(a = x) AND (b = y) AND (c = z)

This rule makes it easier to extract constraints from boolean expressions,
so that recognition code doesn't have to handle the tuple case separately.

Extracted from `comp.opt` (which defines multiple rules — implement specifically `NormalizeTupleEquality`, not the other rules in that file):

```
# NormalizeTupleEquality breaks up expressions like:
#   (a, b, c) = (x, y, z)
# into
#   (a = x) AND (b = y) AND (c = z)
#
# This rule makes it easier to extract constraints from boolean expressions,
# so that recognition code doesn't have to handle the tuple case separately.
[NormalizeTupleEquality, Normalize]
(Eq (Tuple $left:*) (Tuple $right:*))
=>
(NormalizeTupleEquality $left $right)
```
