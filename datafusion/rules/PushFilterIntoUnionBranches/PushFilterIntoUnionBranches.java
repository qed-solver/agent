package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — exactly two one-column branches of the same type T (DataFusion's rule handles any number of possibly multi-column branches); the predicate is pushed unchanged (identity column renaming) into both branches.
public record PushFilterIntoUnionBranches() implements RRule {
    static final RelRN left = RelRN.scan("A", "T");
    static final RelRN right = RelRN.scan("B", "T");

    @Override
    public RelRN before() {
        // Filter(P, UNION ALL(A, B))
        final RelRN union = left.union(true, right);
        return union.filter(union.pred("P"));
    }

    @Override
    public RelRN after() {
        // UNION ALL(Filter(P, A), Filter(P, B)) — same predicate symbol P in each branch
        // (reusing the name "P" tells QED these are the same uninterpreted predicate,
        // matching the rule's per-branch column renaming, which is identity here).
        return left.filter(left.pred("P"))
                .union(true, right.filter(right.pred("P")));
    }
}
