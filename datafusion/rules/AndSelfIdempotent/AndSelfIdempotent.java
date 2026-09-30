package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the duplicated right operand is a single uninterpreted predicate occurring as a nested conjunct inside the left operand, not an arbitrary subexpression of any shape or depth.
public record AndSelfIdempotent() implements RRule {

    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN A = source.pred("A");
    static final RexRN B = source.pred("B");
    static final RexRN C = source.pred("C");
    // left = (A AND B) AND C; B is a subexpression of left, as in DataFusion's expr_contains(&left, &right, And)
    static final RexRN left = RexRN.and(RexRN.and(A, B), C);

    @Override
    public RelRN before() {
        // (..B..) AND B
        return source.filter(RexRN.and(left, B));
    }

    @Override
    public RelRN after() {
        // (..B..)
        return source.filter(left);
    }
}
