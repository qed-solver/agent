package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — only the Eq branch with a null right operand (eq(value, Null) ⇒ Null) is encoded; DataFusion's rule covers ~30 returns_null_on_null operators in either operand position, and each concrete operator needs its own before()/after() pair since QED cannot derive null propagation for an uninterpreted operator symbol
public record BinaryOpNullPropagation() implements RRule {
    static final RelType.VarType V = new RelType.VarType("V", true);
    static final RelRN source = RelRN.scan("Source", V, false);
    static final RexRN x = source.field(0);

    // null literal of the operand's type (the "NULL" operand of the comparison)
    static final RexRN nullV = new RexRN() {
        @Override
        public RexNode semantics() {
            return RuleBuilder.create().getRexBuilder().makeNullLiteral(V);
        }
    };

    // the folded result: a bare NULL of boolean type (the comparison's type, in filter position)
    static final RexRN nullBool = new RexRN() {
        @Override
        public RexNode semantics() {
            var b = RuleBuilder.create();
            return b.getRexBuilder().makeNullLiteral(
                b.getTypeFactory().createSqlType(SqlTypeName.BOOLEAN));
        }
    };

    @Override
    public RelRN before() {
        // value op NULL: equality whose right input is the null constant
        return source.filter(new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(x, nullV)));
    }

    @Override
    public RelRN after() {
        // folded to a bare NULL (BoolType)
        return source.filter(nullBool);
    }
}
