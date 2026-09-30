# Name: PushFilterIntoJoin
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: Join(join))` or a top-level `Join(join)` with its own residual `filter` splits `predicate`'s conjuncts (plus any pre-existing `on`/`filter` conjuncts on the join itself) by which side's columns they reference: a conjunct referencing only left columns is pushed into `Filter(left)`, one referencing only right columns into `Filter(right)`, one referencing both sides becomes (or stays) a join-level `on`/`filter` condition (further split into equi vs non-equi via `infer_join_predicates`, see ExtractEquijoinPredicate). See `push_down_all_join`. This mirrors cockroach's already-ported PushSelectIntoJoinLeft/PushSelectIntoJoinRight/PushSelectCondLeftIntoJoinLeftAndRight family -- implement one narrowed representative instance, e.g. an INNER join where the filter is a single conjunct referencing only the left side's columns, pushed into Filter(left).
