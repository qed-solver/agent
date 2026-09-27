package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlOperator;
import org.apache.calcite.sql.type.SqlTypeName;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: FULL
public record FoldNullCast() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN nullLit = new RexRN() {
        @Override
        public RexNode semantics() {
            var b = RuleBuilder.create();
            return b.getRexBuilder().makeNullLiteral(b.getTypeFactory().createSqlType(SqlTypeName.INTEGER));
        }
    };
    static final SqlOperator castOp = RuleBuilder.create()
            .genericProjectionOp("cast", new RelType.VarType("Target_Type", true));

    @Override
    public RelRN before() {
        return source.project(new RexRN.Proj(castOp, Seq.of(nullLit)));
    }

    @Override
    public RelRN after() {
        return source.project(nullLit);
    }
}
