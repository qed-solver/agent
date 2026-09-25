package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — a 3-way inner-join tree whose left child is a 2-way join denoting an existing HyperGraph, flattened into the 3-input hypergraph and enumerated as the right-deep tree A⋈(B⋈C) rather than the original left-deep (A⋈B)⋈C, rather than the rule's general flattening of arbitrary input trees and all its supported join types
public record JoinToHyperGraph() implements RRule {
    static final RelRN A = RelRN.scan("A", "A_Type");
    static final RelRN B = RelRN.scan("B", "B_Type");
    static final RelRN C = RelRN.scan("C", "C_Type");

    // Shared uninterpreted 2-argument predicate symbols; each occurrence below
    // applies them in the SAME argument order on both sides: pAB(A,B), pAC(A,C), pBC(B,C).
    static final SqlOperator pAB = RuleBuilder.create().genericPredicateOp("pAB", true);
    static final SqlOperator pAC = RuleBuilder.create().genericPredicateOp("pAC", true);
    static final SqlOperator pBC = RuleBuilder.create().genericPredicateOp("pBC", true);

    // before: Join(HyperGraph, C). The HyperGraph over inputs (A,B) with the
    // single edge pAB has no QED node of its own, so it is denoted by its
    // defining join A ⋈_{pAB} B (layout (A,B): field 0=A, field 1=B).
    // The top join condition pAC ∧ pBC is what the rule turns into the new
    // hyperedges connecting C to A and C to B.
    static final RexRN abCond = new RexRN.Pred(pAB, Seq.of(A.joinField(0, B), A.joinField(1, B)));
    static final RelRN hyper = A.join(JoinRelType.INNER, abCond, B);

    static final RexRN beforeCond = RexRN.and(
            new RexRN.Pred(pAC, Seq.of(hyper.joinField(0, C), hyper.joinField(2, C))),
            new RexRN.Pred(pBC, Seq.of(hyper.joinField(1, C), hyper.joinField(2, C))));
    static final RelRN before = hyper.join(JoinRelType.INNER, beforeCond, C);

    // after: the flattened HyperGraph over inputs (A,B,C) with edges
    // {pAB, pAC, pBC}, denoted by a different enumeration of the same
    // hypergraph — the right-deep tree A ⋈ (B ⋈_{pBC} C). bc has layout
    // (B,C), so the top join has layout (A,B,C): field 0=A, 1=B, 2=C,
    // matching the before side's row order, so no trailing project is needed.
    static final RexRN bcCond = new RexRN.Pred(pBC, Seq.of(B.joinField(0, C), B.joinField(1, C)));
    static final RelRN bc = B.join(JoinRelType.INNER, bcCond, C);

    static final RexRN afterCond = RexRN.and(
            new RexRN.Pred(pAB, Seq.of(A.joinField(0, bc), A.joinField(1, bc))),
            new RexRN.Pred(pAC, Seq.of(A.joinField(0, bc), A.joinField(2, bc))));
    static final RelRN after = A.join(JoinRelType.INNER, afterCond, bc);

    @Override
    public RelRN before() {
        return before;
    }

    @Override
    public RelRN after() {
        return after;
    }
}