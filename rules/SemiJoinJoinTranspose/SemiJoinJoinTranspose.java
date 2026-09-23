package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — single-column X/Y/Z, INNER lower join, and all semi-join keys referencing only X (the source rule's left-push branch; keys from both X and Y are rejected by the rule itself)
public record SemiJoinJoinTranspose() implements RRule {
    static final RelRN X = RelRN.scan("X", "Key_Type");
    static final RelRN Y = RelRN.scan("Y", "Key_Type");
    static final RelRN Z = RelRN.scan("Z", "Key_Type");

    // Shared uninterpreted predicate symbols; reusing the same operator
    // (hence name) in before() and after() ties the occurrences for QED.
    static final SqlOperator cOp = RuleBuilder.create().genericPredicateOp("C", true);
    static final SqlOperator sOp = RuleBuilder.create().genericPredicateOp("S", true);

    // Bottom inner join J = X ⋈_C Y, row layout (X, Y): flat 0 = X, flat 1 = Y.
    static final RexRN C = new RexRN.Pred(cOp,
            Seq.of(X.joinField(0, Y), X.joinField(1, Y)));
    static final RelRN J = X.join(JoinRelType.INNER, C, Y);

    // Semi-join condition S over (X, Z): in the (X, Y, Z) row of the top
    // join, X is flat 0 and Z is flat 2 (J has 2 columns, so flat 2 binds
    // to Z column 0 via J.joinField(2, Z)); no Y reference, matching the
    // X-keys branch precondition of the source rule.
    static final RexRN S = new RexRN.Pred(sOp,
            Seq.of(J.joinField(0, Z), J.joinField(2, Z)));

    @Override
    public RelRN before() {
        // (X ⋈_C Y) ⋈_Sᵗ(SEMI) Z — semi-join emits only J's fields (X, Y).
        return J.join(JoinRelType.SEMI, S, Z);
    }

    @Override
    public RelRN after() {
        // New semi-join X ⋈ᵗ(SEMI) Z over the (X, Z) row: re-reference the
        // same S symbol onto flat 0 (X) and flat 1 (Z).
        RelRN xz = X.join(JoinRelType.SEMI,
                new RexRN.Pred(sOp, Seq.of(X.joinField(0, Z), X.joinField(1, Z))), Z);
        // xz emits only X's field; re-reference the same C symbol onto the
        // (X, Y) row of the new inner join (flat 0 = X, flat 1 = Y), keeping
        // the original join type and condition as the source rule does.
        return xz.join(JoinRelType.INNER,
                new RexRN.Pred(cOp, Seq.of(xz.joinField(0, Y), xz.joinField(1, Y))), Y);
    }
}