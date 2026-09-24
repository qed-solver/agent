# AggregateRemoveDuplicateKeys

**Status:** SKIPPED
**Source backend:** Apache Calcite
**Porter attempts used:** 30  **Verification rounds used:** 1

## Source rule (as given to the porter)

```
Source: core/src/main/java/org/apache/calcite/rel/rules/AggregateRemoveDuplicateKeysRule.java
```

## Independent verifier review

**Verdict:** AGREE

The rewrite's soundness rests on ANY_VALUE's choice semantics — its result must be the group's functionally-determined value — but QED treats aggregate functions as uninterpreted functions of their input bag and, per the reference, "knows nothing about a specific aggregate function's algebra beyond bag equality of its input," so it cannot prove ANY_VALUE(group-bag) = f(a). The mq.determinesSet functional-dependency premise can at best be baked into a narrower source shape, but the rule's `after` always contains an ANY_VALUE call for each removed key, so no non-degenerate instance of this exact before/after shape is provable; the only provable variant (re-deriving the removed key as f(A) in a trailing project instead of ANY_VALUE) is a different rewrite, not a special case of this rule. ```
