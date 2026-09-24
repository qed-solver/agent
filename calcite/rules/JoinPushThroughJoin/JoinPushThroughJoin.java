package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — single-column table inputs, and both join conditions are a fixed representative conjunction of uninterpreted conjuncts (SA, SB, ST, TC) split by B-reference, not an arbitrary condition decomposition

public record JoinPushThroughJoin() implements RRule {
    static final RelRN A = RelRN.scan("A", "A_Type");
    static final RelRN B = RelRN.scan("B", "B_Type");
    static final RelRN C = RelRN.scan("C", "C_Type");

    // Shared uninterpreted predicate symbols: SA only over A, SB only over B,
    // TC only over C, ST over A and B. Reusing the same name in before() and
    // after() tells QED the occurrences are the same symbol.
    static final SqlOperator SA = RuleBuilder.create().genericPredicateOp("SA", true);
    static final SqlOperator SB = RuleBuilder.create().genericPredicateOp("SB", true);
    static final SqlOperator ST = RuleBuilder.create().genericPredicateOp("ST", true);
    static final SqlOperator TC = RuleBuilder.create().genericPredicateOp("TC", true);

    // before: (A ⋈_{SA∧SB} B) ⋈_{ST∧TC} C, row layout (A, B, C).
    // Bottom condition conjuncts: SA (non-intersecting, A only) and SB
    // (intersecting, uses B); top condition conjuncts: ST (intersecting,
    // uses A and B) and TC (non-intersecting, C only).
    static final RelRN abJoin = A.join(JoinRelType.INNER, RexRN.and(
            new RexRN.Pred(SA, Seq.of(A.joinField(0, B))),
            new RexRN.Pred(SB, Seq.of(A.joinField(1, B)))), B);

    static final RelRN beforeJoin = abJoin.join(JoinRelType.INNER, RexRN.and(
            new RexRN.Pred(ST, Seq.of(abJoin.joinField(0, C), abJoin.joinField(1, C))),
            new RexRN.Pred(TC, Seq.of(abJoin.joinField(2, C)))), C);

    // after: (A ⋈_{SA∧TC} C) ⋈_{ST∧SB} B, row layout (A, C, B).
    // New bottom condition: SA (pulled from bottom) ∧ TC (pushed down from top).
    // New top condition: ST ∧ SB, re-referenced onto the permuted row
    // (A, C, B): A is column 0, B is column 2.
    static final RelRN acJoin = A.join(JoinRelType.INNER, RexRN.and(
            new RexRN.Pred(SA, Seq.of(A.joinField(0, C))),
            new RexRN.Pred(TC, Seq.of(A.joinField(1, C)))), C);

    static final RelRN afterJoin = acJoin.join(JoinRelType.INNER, RexRN.and(
            new RexRN.Pred(ST, Seq.of(acJoin.joinField(0, B), acJoin.joinField(2, B))),
            new RexRN.Pred(SB, Seq.of(acJoin.joinField(2, B)))), B);

    @Override
    public RelRN before() {
        return beforeJoin;
    }

    @Override
    public RelRN after() {
        // Reorder (A, C, B) back to the original (A, B, C) layout, as
        // Calcite's final project(topMapping) does.
        return afterJoin.project(Seq.of(
                afterJoin.field(0),
                afterJoin.field(2),
                afterJoin.field(1)));
    }
}
