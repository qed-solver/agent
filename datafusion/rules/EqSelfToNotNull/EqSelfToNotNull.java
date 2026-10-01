package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.SqlTypeName;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — only the nullable branch of the source rule is encoded (A = A → A IS NOT NULL OR NULL for nullable A); the non-nullable branch (A = A → true) is a distinct rewrite target a single before/after pair cannot cover
public record EqSelfToNotNull() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");
    static final RexRN x = source.field(0);

    // lit_bool_null()
    static final RexRN nullLit = new RexRN() {
        @Override
        public RexNode semantics() {
            var b = RuleBuilder.create();
            return b.getRexBuilder().makeNullLiteral(b.getTypeFactory().createSqlType(SqlTypeName.BOOLEAN));
        }
    };

    @Override
    public RelRN before() {
        // A = A: both sides the same (non-volatile) expression x
        return source.filter(new RexRN.Pred(SqlStdOperatorTable.EQUALS, Seq.of(x, x)));
    }

    @Override
    public RelRN after() {
        // A IS NOT NULL OR NULL
        var isNotNull = new RexRN.Pred(SqlStdOperatorTable.IS_NOT_NULL, Seq.of(x));
        return source.filter(new RexRN.Or(Seq.of(isNotNull, nullLit)));
    }
}