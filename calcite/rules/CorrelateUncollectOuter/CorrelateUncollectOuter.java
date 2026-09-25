package org.qed.RRuleInstances;

// SCOPE: PARTIAL — left input is a single (nullable) array column and the element relation a single (nullable) column, with the uncollect's list expansion abstracted as element membership under one shared uninterpreted predicate M(array, elem).
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

public record CorrelateUncollectOuter() implements RRule {
    // L: the correlate's left input; its single column a is the array read
    // through the correlation ($cor0.f) in the source rule.
    static final RelRN left = RelRN.scan("L", RexRN.varType("A", true), false);

    // E: one row per element value of an array (single nullable column).
    static final RelRN elements = RelRN.scan("E", RexRN.varType("E", true), false);

    // M(array, elem): the uninterpreted membership predicate standing in for
    // "elem is an element of array", shared by both sides.
    static final SqlOperator mOp = RuleBuilder.create().genericPredicateOp("M", true);

    @Override
    public RelRN before() {
        // Correlate(LEFT, L, Uncollect(isOuter=[any], $cor0.f)): the LEFT
        // correlate null-pads left rows with no matching element, which is
        // exactly what the isOuter=true uncollect does (and what the
        // isOuter=false uncollect leaves to the correlate), so both flag
        // values of the source pattern collapse to this same LEFT-correlate.
        return left.correlate(JoinRelType.LEFT, mOp, elements);
    }

    @Override
    public RelRN after() {
        // Correlate(INNER, L, Uncollect(isOuter=true, $cor0.f)): the
        // isOuter=true uncollect's NULL row is precisely the LEFT-join's
        // null padding, so the inner correlate is vacuous and the whole
        // right side is the plain LEFT-join of L with E on M(l.a, e).
        return left.join(JoinRelType.LEFT,
                new RexRN.Pred(mOp, left.joinFields(elements)), elements);
    }
}