// SCOPE: PARTIAL — source family covers Projection/Filter/Window/Sort/SubqueryAlias/Repartition/Limit nodes whose child is a zero-row EmptyRelation; this encoding pins the Filter member with an uninterpreted predicate over an arbitrary input type (Window/Sort/Limit have no bag semantics and SubqueryAlias/Repartition are identity aliases absent from the DSL).
package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;

/**
 * Port of the passthrough-node branch of DataFusion's PropagateEmptyRelation
 * (datafusion/optimizer/src/propagate_empty_relation.rs, lines 62-74): a
 * passthrough node (Projection | Filter | Window | Sort | SubqueryAlias |
 * Repartition | Limit) whose single child is EmptyRelation
 * { produce_one_row = false } is replaced by EmptyRelation
 * { produce_one_row = false } carrying the node's own schema.
 *
 * Representative member: the Filter branch. A Filter's output schema is
 * exactly its input's schema, so the replacement empty relation carries the
 * child's row type — the node contributes nothing but a predicate test,
 * which can never resurrect the zero rows below it.
 */
public record PassthroughEmptyRelation() implements RRule {
    // The passthrough node's child; its uninterpreted row type stands in for
    // the child relation's schema.
    static final RelRN child = RelRN.scan("Child", "Child_Type");

    @Override
    public RelRN before() {
        // Filter(P, EmptyRelation(produce_one_row = false)): an uninterpreted
        // predicate P applied over a zero-row relation of the child's row type.
        return child.empty().filter("pred");
    }

    @Override
    public RelRN after() {
        // EmptyRelation { produce_one_row = false, schema: filter.schema() } —
        // the filter preserves its input's schema, so this is the same
        // zero-row relation of the child's row type.
        return child.empty();
    }
}