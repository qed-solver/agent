package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — fixed 4-way inner multi-join with a single shared filter, rewritten from the left-deep tree (((A⋈B)⋈C)⋈_F D) to the genuinely bushy tree (A⋈B)⋈_F (C⋈D), with the filter kept as one uninterpreted predicate over the full concatenated row rather than the heuristic's cost-driven tree choice and per-conjunct condition distribution
public record MultiJoinOptimizeBushy() implements RRule {
    static final RelRN A = RelRN.scan("A", "A_Type");
    static final RelRN B = RelRN.scan("B", "B_Type");
    static final RelRN C = RelRN.scan("C", "C_Type");
    static final RelRN D = RelRN.scan("D", "D_Type");

    // The MultiJoin's single filter: one shared uninterpreted 4-argument
    // predicate symbol F applied to the full (A,B,C,D) row.
    static final SqlOperator F = RuleBuilder.create().genericPredicateOp("F", true);

    // before: left-deep 4-way join. The inner true-condition joins stand in
    // for the MultiJoin's pairwise-unconnected inputs; the root carries the
    // filter. abc has layout (A,B,C), so joinField(0..2) = A,B,C and
    // joinField(3) = D — F is applied to (A,B,C,D).
    static final RelRN ab = A.join(JoinRelType.INNER, RexRN.trueLiteral(), B);
    static final RelRN abc = ab.join(JoinRelType.INNER, RexRN.trueLiteral(), C);
    static final RexRN beforeCond = new RexRN.Pred(F, Seq.of(
            abc.joinField(0, D),
            abc.joinField(1, D),
            abc.joinField(2, D),
            abc.joinField(3, D)));
    static final RelRN beforeJoin = abc.join(JoinRelType.INNER, beforeCond, D);

    // after: genuinely bushy tree — both children of the root join are
    // non-leaf joins, (A⋈B) and (C⋈D). cd has layout (C,D), so the top
    // join ab⋈cd has layout (A,B,C,D) and joinField(0..1)=A,B,
    // joinField(2..3)=C,D — identical F argument order to the before side,
    // hence no trailing project is needed.
    static final RelRN cd = C.join(JoinRelType.INNER, RexRN.trueLiteral(), D);
    static final RexRN afterCond = new RexRN.Pred(F, Seq.of(
            ab.joinField(0, cd),
            ab.joinField(1, cd),
            ab.joinField(2, cd),
            ab.joinField(3, cd)));
    static final RelRN afterJoin = ab.join(JoinRelType.INNER, afterCond, cd);

    @Override
    public RelRN before() {
        return beforeJoin;
    }

    @Override
    public RelRN after() {
        return afterJoin;
    }
}
