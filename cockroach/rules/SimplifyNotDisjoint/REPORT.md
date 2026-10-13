# SimplifyNotDisjoint

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 24  **Verification rounds used:** 2

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/scalar.opt

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
```

## Independent verifier review

**Verdict:** AGREE

This rule's validity rests entirely on CockroachDB's definitional axiom st_intersects(a,b) ≡ ¬st_disjoint(a,b), but RuleScript can only express these geo built-ins as distinct name-keyed uninterpreted symbols, and QED treats differently named operators as independent functions with no axiom mechanism to relate them — a gap in the fixed prover that no DSL extension can close. Since ¬P(x) ≡ Q(x) is false under some joint instantiation of independent P and Q (a row where P is true and Q is false), no encoding or narrower special case is universally valid, so the porter's UNSUPPORTED conclusion and reasoning are correct. ```
