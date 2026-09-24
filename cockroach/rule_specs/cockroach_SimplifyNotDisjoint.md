# Name: SimplifyNotDisjoint
# Backend: CockroachDB
# Source: pkg/sql/opt/norm/rules/scalar.opt

SimplifyNotDisjoint converts !st_disjoint to st_intersects so that
the query can be index-accelerated if a suitable inverted index exists.

Extracted from `scalar.opt` (which defines multiple rules — implement specifically `SimplifyNotDisjoint`, not the other rules in that file):

```
# SimplifyNotDisjoint converts !st_disjoint to st_intersects so that
# the query can be index-accelerated if a suitable inverted index exists.
[SimplifyNotDisjoint, Normalize]
(Not (Function $args:* $private:(FunctionPrivate "st_disjoint")))
=>
(MakeIntersectionFunction $args)
```
