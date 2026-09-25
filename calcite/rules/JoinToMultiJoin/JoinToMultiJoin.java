package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — only the INNER-join case (the real rule also refuses outer/null-producing inputs), fixed 3-way flattening of Join(C, X, MultiJoin(Y, Z, filter F_M)) into the flat 3-input MultiJoin with the combined filter F_M ∧ C
public record JoinToMultiJoin() implements RRule {
    static final RelRN X = RelRN.scan("X", "X_Type");
    static final RelRN Y = RelRN.scan("Y", "Y_Type");
    static final RelRN Z = RelRN.scan("Z", "Z_Type");

    // The MultiJoin's own filter (uninterpreted predicate over the full
    // (Y,Z) row) and the outer Join's condition (uninterpreted predicate
    // over the full (X,Y,Z) row) — the two filters the rule combines.
    static final SqlOperator Fm = RuleBuilder.create().genericPredicateOp("Fm", true);
    static final SqlOperator C = RuleBuilder.create().genericPredicateOp("C", true);

    // before: Join(C, X, MultiJoin(Y, Z, filter Fm)). The MultiJoin is its
    // binary-tree rendering Y ⋈_{Fm} Z (its filter carried at the subtree
    // root); the outer join X ⋈_C has the (x,y,z) row layout.
    static final RelRN yz = Y.join(JoinRelType.INNER,
            new RexRN.Pred(Fm, Seq.of(Y.joinField(0, Z), Y.joinField(1, Z))), Z);
    static final RelRN beforeJoin = X.join(JoinRelType.INNER,
            new RexRN.Pred(C, Seq.of(X.joinField(0, yz), X.joinField(1, yz), X.joinField(2, yz))), yz);

    // after: the flat 3-input MultiJoin(X, Y, Z) with the combined filter
    // Fm ∧ C. Rendered as the binary tree X ⋈_{Fm∧C} (Y ⋈_{true} Z): the
    // inner edge is now an unconstrained multi-join factor and the root
    // carries the combined filter — Fm and C are the same predicate
    // symbols, re-indexed onto the same (x,y,z) columns as before, so no
    // trailing project is needed.
    static final RelRN yzFree = Y.join(JoinRelType.INNER, RexRN.trueLiteral(), Z);
    static final RelRN afterJoin = X.join(JoinRelType.INNER, RexRN.and(
            new RexRN.Pred(Fm, Seq.of(X.joinField(1, yzFree), X.joinField(2, yzFree))),
            new RexRN.Pred(C, Seq.of(X.joinField(0, yzFree), X.joinField(1, yzFree), X.joinField(2, yzFree)))), yzFree);

    @Override
    public RelRN before() {
        return beforeJoin;
    }

    @Override
    public RelRN after() {
        return afterJoin;
    }
}