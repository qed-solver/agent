# Name: PushFilterIntoExtension
# Backend: Apache DataFusion
# Source: datafusion/optimizer/src/push_down_filter.rs

Registered DataFusion optimizer rule this was extracted from: `PushDownFilter`.

`Filter(predicate, input: Extension(extension_plan))` where the extension node's own `PushDownFilter`-style trait hook accepts some of `predicate`'s conjuncts moves those conjuncts down into (or below) the opaque Extension node, keeping the rest above. Since `Extension` wraps a user-defined, opaque `LogicalPlan` node with no fixed relational-algebra semantics, this arm is not expected to be portable/provable in RuleScript/QED -- included here for completeness of the source's own rule inventory, not because it's expected to port.
