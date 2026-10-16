# SimplifyNotDisjoint

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 26  **Verification rounds used:** 2

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

The rule is purely scalar, and its entire validity rests on Cockroach's library identity st_intersects(args) ≡ ¬st_disjoint(args); in QED's model these are two independent uninterpreted symbols, so the faithful encoding (a Filter over a shared scan, which the porter compiled and ran to completion with no timeout) admits a genuine SMT countermodel, making provable=false the correct verdict rather than a symbol-naming or shape bug. The only route to a proof — postulating that identity as a table "guaranteed" constraint — would import the rule's own premise as an axiom, turning the verification circular, which is precisely the "bespoke internal semantics of a backend operator" limitation QED fundamentally cannot model, not a missing DSL capability worth extending.
