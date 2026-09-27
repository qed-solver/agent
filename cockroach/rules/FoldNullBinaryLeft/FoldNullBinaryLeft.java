package org.qed.RRuleInstances;

import kala.collection.Seq;
import kala.collection.Set;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.qed.QedTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — restricts FoldNullBinaryLeft to the single built-in null-lifting operator (integer +) with a NULL left operand, observed in filter position via IS_NULL; the general form over all non-null-accepting operators is unprovable because QED supplies no null-propagation axiom to uninterpreted operators
public record FoldNullBinaryLeft() implements RRule {
    static final RelType intCol = new RelType.BaseType(SqlTypeName.INTEGER, true);
    static final RelRN source = new RelRN() {
        @Override
        public RelNode semantics() {
            var table = new QedTable("Source", Seq.of("col-Source"),
                Seq.of((RelDataType) intCol), Set.empty(), Set.empty());
            return RuleBuilder.create().addTable(table).scan("Source").build();
        }
    };
    static final RexRN x = source.field(0);

    static final RexRN nullInt = new RexRN() {
        @Override
        public RexNode semantics() {
            var b = RuleBuilder.create();
            return b.getRexBuilder().makeNullLiteral(
                b.getTypeFactory().createSqlType(SqlTypeName.INTEGER));
        }
    };

    @Override
    public RelRN before() {
        return source.filter(new RexRN.Pred(SqlStdOperatorTable.IS_NULL,
            Seq.of(new RexRN.Proj(SqlStdOperatorTable.PLUS, Seq.of(nullInt, x)))));
    }

    @Override
    public RelRN after() {
        return source.filter(new RexRN.Pred(SqlStdOperatorTable.IS_NULL, Seq.of(nullInt)));
    }
}
