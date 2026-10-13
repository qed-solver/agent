package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — the LEFT-join case in which the null-generating (right) input's column is declared non-nullable and the filter sits directly above the join (a surviving left-only conjunct is not modeled).
public record OuterJoinToAntiJoin() implements RRule {
    static final RelRN left = RelRN.scan("L", new RelType.VarType("L_T", true), false);
    static final RelRN right = RelRN.scan("R", new RelType.VarType("R_T", false), false);
    static final RexRN cond = left.joinPred("cond", right);
    static final RelRN lJoin = left.join(JoinRelType.LEFT, cond, right);

    // Calcite's RelBuilder.join(LEFT, ...) does not widen the null-generating
    // (right) side's declared nullability on the join's own row type, so a
    // plain field(1) reference still carries "NOT NULL" and Calcite's own
    // RexSimplify constant-folds IS_NULL(it) to FALSE before QED ever sees it.
    // RelDataTypeFactory.createTypeWithNullability is a no-op on RelType.VarType
    // (it overrides isNullable() directly rather than participating in the
    // factory's normal type-rebuilding), so the fix has to construct the
    // nullable VarType by hand rather than ask the factory to widen it.
    record ForcedTypeRef(RelType type, int ordinal) implements RexRN {
        @Override
        public RexNode semantics() {
            return RuleBuilder.create().getRexBuilder().makeInputRef(type, ordinal);
        }
    }

    static final RexRN rightRefNullable = new ForcedTypeRef(new RelType.VarType("R_T", true), 1);

    @Override
    public RelRN before() {
        // L LEFT JOIN R ON cond, keeping only the null-extended (unmatched) rows,
        // projected back to the left columns.
        return lJoin
                .filter(new RexRN.Pred(SqlStdOperatorTable.IS_NULL, Seq.of(rightRefNullable)))
                .project(Seq.of(lJoin.field(0)));
    }

    @Override
    public RelRN after() {
        // L ANTI JOIN R ON cond — exactly the left rows with no matching right row.
        return left.join(JoinRelType.ANTI, cond, right);
    }
}
