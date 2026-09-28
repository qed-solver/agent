package org.qed.RRuleInstances;

// SCOPE: PARTIAL — modeled with a concrete 2-row, 1-column Values relation carrying one uninterpreted assignment cast (identity function over a column) and one integer literal column, rather than being universally quantified over arbitrary row contents; the push-down of the cast projection into the Values row is the core of the rule and is fully captured.

import kala.collection.Seq;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeField;
import org.apache.calcite.rel.type.RelDataTypeFieldImpl;
import org.apache.calcite.rel.type.RelRecordType;
import org.apache.calcite.rel.type.StructKind;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RelType;
import org.qed.RuleBuilder;

public record PushAssignmentCastsIntoValues() implements RRule {

    record IntLit(int value) implements RexRN {
        @Override
        public RexNode semantics() {
            return RuleBuilder.create().literal(value);
        }
    }

    record ExprValues(java.util.List<RexNode> row, RelDataType fieldType) implements RelRN {
        @Override
        public RelNode semantics() {
            var field = (RelDataTypeField) new RelDataTypeFieldImpl("c0", 0, fieldType);
            var rt = new RelRecordType(StructKind.FULLY_QUALIFIED, java.util.List.of(field));
            return RuleBuilder.create().values(java.util.List.of(row), rt).build();
        }
    }

    static RelDataType castFieldType() {
        var op = RuleBuilder.create().genericProjectionOp("cast", new RelType.VarType("cast_Type", true));
        return RuleBuilder.create().literal(0).getType();
    }

    static RelDataType intFieldType() {
        return RuleBuilder.create().literal(0).getType();
    }

    static final RexRN x = new RelRN.Field(0, new ExprValues(
            java.util.List.of(new IntLit(1).semantics()), intFieldType()));

    @Override
    public RelRN before() {
        var castOp = RuleBuilder.create().genericProjectionOp("cast", new RelType.VarType("cast_Type", true));
        var vals = new ExprValues(java.util.List.of(new IntLit(1).semantics(), new IntLit(2).semantics()), intFieldType());
        return new RelRN.ProjectMany(
                Seq.of(new RexRN.Proj(castOp, Seq.of(vals.field(0))), vals.field(0)),
                vals);
    }

    @Override
    public RelRN after() {
        var castOp = RuleBuilder.create().genericProjectionOp("cast", new RelType.VarType("cast_Type", true));
        var f1 = RuleBuilder.create().call(castOp, Seq.of(new IntLit(1).semantics()));
        var f2 = RuleBuilder.create().call(castOp, Seq.of(new IntLit(2).semantics()));
        var vals = new ExprValues(java.util.List.of(f1, f2), intFieldType());
        return new RelRN.ProjectMany(
                Seq.of(vals.field(0), new IntLit(0)),
                vals);
    }
}
