package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// Port of DataFusion's push_down_filter AsOfJoin case
// (datafusion/optimizer/src/push_down_filter.rs, lines 1156-1223):
//
//   Filter(F, AsOfJoin(L, R, on))
//        =>
//   Filter(keep, AsOfJoin(Filter(push, L), [Filter(mirror, R)], on))
//
// where `push` are the deterministic left-only conjuncts (pushed into the
// left input), `keep` is everything else (stays above the join), and
// `mirror` re-states a `key = literal` push-predicate on the right key.
//
// Encoded soundness core: the reason left-only conjuncts may run before
// matching is that ASOF emits exactly one output row per left row without
// changing left values -- a property of LEFT joins in general. So the ASOF
// join is modeled as a LEFT JOIN with the key/time condition as an
// uninterpreted ON predicate, and we prove:
//
//   before = Filter(f AND g, L  LEFT JOIN  R  ON  on)
//   after  = Filter(g,   Filter(f, L)  LEFT JOIN  R  ON  on)
//
// with f a deterministic predicate referencing only L's columns (left-only
// conjunct, the pushed one) and g the remaining conjuncts, kept above the
// join on both sides. The right-input mirror of a key-equality conjunct
// requires entailment reasoning between independent symbols (matched pairs
// share key values) that QED cannot perform, so it is not modeled.
// SCOPE: PARTIAL — ASOF modeled as a LEFT JOIN (left rows preserved once with values unchanged); proves only the left-input pushdown of one deterministic left-only conjunct with the remaining conjuncts kept above the join; the right-input mirror of a key-equality predicate is not modeled
public record PushFilterIntoAsOfJoin() implements RRule {
    // Key columns of both inputs share one type (ASOF requires equal,
    // same-typed keys); each side has one further, unrelated column.
    static final RelType.VarType keyTy    = RexRN.varType("Key_Type", true);
    static final RelType.VarType lOtherTy = RexRN.varType("L_Other_Type", true);
    static final RelType.VarType rOtherTy = RexRN.varType("R_Other_Type", true);

    // col 0 = ASOF join key, col 1 = another column
    static final RelRN left  = RelRN.scanMany("L", Seq.of(keyTy, lOtherTy));
    static final RelRN right = RelRN.scanMany("R", Seq.of(keyTy, rOtherTy));

    // Shared uninterpreted symbols:
    //   on = the ASOF join condition (equal key plus the time constraint)
    //   f  = the deterministic left-only conjunct of the filter (references
    //        only L's columns) -- the conjunct pushed below the join
    //   g  = the remaining conjuncts of the filter, kept above the join
    static final SqlOperator onOp = RuleBuilder.create().genericPredicateOp("asof_on", true);
    static final SqlOperator fOp  = RuleBuilder.create().genericPredicateOp("left_only_conj", true);
    static final SqlOperator gOp  = RuleBuilder.create().genericPredicateOp("keep_conj", true);

    // References into the join output row: 0=L.key 1=L.other 2=R.key 3=R.other
    static final RexRN lKey   = left.joinField(0, right);
    static final RexRN lOther = left.joinField(1, right);
    static final RexRN rKey   = left.joinField(2, right);
    static final RexRN rOther = left.joinField(3, right);

    // ON condition, unchanged by the rewrite.
    static final RexRN on = new RexRN.Pred(onOp, Seq.of(lKey, rKey));

    // f over the join row (filter above the join, in before()) and over L's
    // own row (filter below the join, in after()) -- same symbol, left-only.
    static final RexRN fJoin = new RexRN.Pred(fOp, Seq.of(lKey, lOther));
    static final RexRN fRow  = new RexRN.Pred(fOp, Seq.of(left.field(0), left.field(1)));

    // g over the full join row, above the join on both sides.
    static final RexRN gJoin = new RexRN.Pred(gOp, Seq.of(lKey, lOther, rKey, rOther));

    @Override
    public RelRN before() {
        // Filter(f AND g, L ASOF JOIN R ON on)
        return left.join(JoinRelType.LEFT, on, right)
                   .filter(RexRN.and(fJoin, gJoin));
    }

    @Override
    public RelRN after() {
        // Filter(g, Filter(f, L) ASOF JOIN R ON on)
        return left.filter(fRow)
                   .join(JoinRelType.LEFT, on, right)
                   .filter(gJoin);
    }
}
