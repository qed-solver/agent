package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;

// UNSUPPORTED: QED has no Limit/Sort builder and treats Limit/Offset/Sort purely as uninterpreted query operators (QOp) with bag semantics and no row-count/ordering axioms (qed.pdf 6.2: "does not support queries that make use of the ordering semantics of Limit, Offset, or Order By"); the LeftJoin push-down identity Limit(skip, fetch, L LEFT JOIN R ON c) = Limit(skip, fetch, Limit(0, fetch+skip, L) LEFT JOIN R ON c), whose entire soundness is the row-count argument "a left join preserves every left row, so the outer limit's rows come from within the capped left input", is refuted by SMT for every instantiation because the before/after sides are structurally distinct applications of uninterpreted operators over different join trees with no axiom relating them, and no narrower special case is derivable (same AGREE-verified conclusion for the identical Cockroach PushLimitIntoJoinLeft and for datafusion LimitZeroToEmpty / MergeNestedLimits / FoldLimits); the gap is in the trusted prover's bag-semantic model, not in the DSL surface: JSONSerializer already carries offset/limit via LogicalSort that QED still treats as opaque, so extend_dsl_file cannot change the prover's semantics; a new Limit record would only serialize to the same uninterpreted QOp.
public record PushLimitIntoLeftJoin() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");

    @Override
    public RelRN before() {
        return source;
    }

    @Override
    public RelRN after() {
        return source;
    }
}
