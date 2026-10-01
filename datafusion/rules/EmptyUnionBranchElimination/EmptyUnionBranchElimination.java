package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — assumes the union has exactly one zero-row empty branch among three branches with two non-empty branches remaining (the >=2-remain case; the collapse-to-single-branch and all-empty cases are not modeled).
public record EmptyUnionBranchElimination() implements RRule {
    static final RelRN a = RelRN.scan("A", "T");
    static final RelRN b = RelRN.scan("B", "T");

    @Override
    public RelRN before() {
        // Union(A, B, Empty) — the empty branch is a zero-row relation of the
        // shared row type T (the branch the DataFusion rule filters out).
        return a.union(true, b, a.empty());
    }

    @Override
    public RelRN after() {
        // Union(A, B) — the empty branch eliminated, union of the remaining branches.
        return a.union(true, b);
    }
}
