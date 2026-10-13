// SCOPE: PARTIAL — inner join only, one input column per side, exactly one equi conjunct of the join's residual filter moved into the equi-conditions
package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RexRN.Pred;
import org.qed.RuleBuilder;

// Port of DataFusion's optimizer rule ExtractEquijoinPredicate
// (datafusion/optimizer/src/extract_equijoin_predicate.rs):
//
//   L  JOIN  R  ON  K   WHERE  (eq AND rest)
//         =>
//   L  JOIN  R  ON  (K AND eq)   WHERE  rest
//
// where `eq` is a conjunct of the join's residual filter that is itself an
// equijoin predicate (a left-only expression = a right-only expression).
// Moving it into the equi-conditions lets the physical join use it as a key.
//
// Modeled for INNER joins, the only case where the move is sound (for outer
// joins, a row null-extended because it matched no pair is dropped by the
// filter before the rewrite but preserved after it): the join's residual
// filter is applied to the join's output, so it is exactly a Filter above
// the join. The existing equi-conditions K, the extracted conjunct eq, and
// the remaining filter rest are uninterpreted predicates over the joined
// (L.col, R.col) row:
//
//   before = Filter(eq AND rest, Join_K(L, R))
//   after  = Filter(rest,        Join_{K AND eq}(L, R))
//
// both denote the bag of pairs (x, y) with K(x,y) AND eq(x,y) AND rest(x,y).
public record ExtractEquijoinPredicate() implements RRule {
    static final RelRN left  = RelRN.scan("L", "L_Type");
    static final RelRN right = RelRN.scan("R", "R_Type");

    // Template join over the 1-column inputs, only used to build field
    // references into the join's output layout:
    //   field 0 = L.col,  field 1 = R.col
    static final RelRN joinRow = left.join(JoinRelType.INNER, RexRN.trueLiteral(), right);

    // Uninterpreted predicates over the joined row:
    //   on   = the join's existing equi-conditions (K)
    //   eq   = the conjunct of the filter that IS an equijoin predicate
    //   rest = the remaining conjuncts of the filter
    static final SqlOperator onOp   = RuleBuilder.create().genericPredicateOp("on", true);
    static final SqlOperator eqOp   = RuleBuilder.create().genericPredicateOp("eq", true);
    static final SqlOperator restOp = RuleBuilder.create().genericPredicateOp("rest", true);

    static final RexRN on   = new Pred(onOp,   Seq.of(joinRow.field(0), joinRow.field(1)));
    static final RexRN eq   = new Pred(eqOp,   Seq.of(joinRow.field(0), joinRow.field(1)));
    static final RexRN rest = new Pred(restOp, Seq.of(joinRow.field(0), joinRow.field(1)));

    @Override
    public RelRN before() {
        // Join(on = K, filter = eq AND rest)
        return left.join(JoinRelType.INNER, on, right)
                   .filter(RexRN.and(eq, rest));
    }

    @Override
    public RelRN after() {
        // Join(on = K AND eq, filter = rest)
        return left.join(JoinRelType.INNER, RexRN.and(on, eq), right)
                   .filter(rest);
    }
}