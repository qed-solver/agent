# InlineWith

**Status:** SKIPPED
**Source backend:** CockroachDB
**Porter attempts used:** 50  **Verification rounds used:** 4

## Source rule (as given to the porter)

```
Source: pkg/sql/opt/norm/rules/with.opt

InlineWith replaces use of a With which is referenced at most one time with
the contents of the With itself.

Extracted from `with.opt` (which defines multiple rules — implement specifically `InlineWith`, not the other rules in that file):

```
# InlineWith replaces use of a With which is referenced at most one time with
# the contents of the With itself.
[InlineWith, Normalize]
(With
    $binding:*
    $input:*
    $withPrivate:* & (CanInlineWith $binding $input $withPrivate)
)
=>
(InlineWith $binding $input $withPrivate)
```
```

## Independent verifier review

**Verdict:** AGREE

QED's relational fragment contains no let/With/alias operator — JSONSerializer only emits scan/values/filter/project/join/correlate/group/union/intersect/except/sort, and the prover knows no defined-relation node, so a CTE reference is an uninterpreted table whose contents can be neither defined by nor equated to its defining expression (table constraints are only single-table integrity checks). The only encodings are to treat reference and definition as unrelated symbols (QED correctly cannot prove equality) or as the same symbol (before and after become the identical plan, a vacuous proof), and since the prover is the unmodifiable arbiter no DSL extension can add a with node — the rule's entire content is precisely that binding substitution, so it is genuinely outside what QED can prove. ```
