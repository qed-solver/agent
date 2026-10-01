package org.qed.RRuleInstances;

import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.type.SqlTypeName;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — only the And branch of the rule (NULL AND NULL => NULL) is encoded; the Or branch is structurally identical but not expressible in a single before()/after() pair
public record NullAndOrToNull() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");

    // the null literal: a bare NULL of boolean type
    static final RexRN nullLit = new RexRN() {
        @Override
        public RexNode semantics() {
            var b = RuleBuilder.create();
            return b.getRexBuilder().makeNullLiteral(
                b.getTypeFactory().createSqlType(SqlTypeName.BOOLEAN));
        }
    };

    @Override
    public RelRN before() {
        // NULL AND NULL: and-join of two boolean null constants
        return source.filter(RexRN.and(nullLit, nullLit));
    }

    @Override
    public RelRN after() {
        // folded to a bare NULL (BoolType)
        return source.filter(nullLit);
    }
}
