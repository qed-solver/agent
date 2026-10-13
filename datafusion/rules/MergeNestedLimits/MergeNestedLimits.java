package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;

// UNSUPPORTED: QED has no Limit/Sort builder and treats Limit/Offset/Sort purely as uninterpreted query operators (QOp) with bag semantics and no row-count/ordering axioms (qed.pdf: "Qed currently does not support queries that make use of the ordering semantics of Limit, Offset, or Order By"); the nested-Limit merge identity Limit(s, f, Limit(cs, cf, X)) = Limit(combine_limit(s,f,cs,cf), X) is refuted by SMT for every instantiation because the two sides are structurally distinct applications of uninterpreted functions over X with no axiom relating them, even in the degenerate idempotent case Limit(n, Limit(n, X)) = Limit(n, X), and no narrower special case is provable (same conclusion independently verified for datafusion LimitZeroToEmpty and cockroach FoldLimits, both AGREE). The gap is in the trusted prover's bag-semantic model, not in the DSL surface: JSONSerializer already carries offset/limit via LogicalSort and JSONDeserializer round-trips it as sortLimit, so extend_dsl_file cannot change the prover's semantics; a new Limit record would only serialize to the same uninterpreted QOp.
public record MergeNestedLimits() implements RRule {
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
