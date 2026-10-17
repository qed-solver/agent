package org.qed.RRuleInstances;

import kala.collection.Seq;
import kala.collection.Set;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.sql.type.SqlTypeName;
import org.qed.QedTable;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — A is modeled as a non-nullable boolean column of the scanned source, not an arbitrary uninterpreted non-null boolean expression
public record OrNotSelfTautology() implements RRule {
    static final RelRN source = () -> {
        var table = new QedTable("Source",
                Seq.of("col-Source"),
                Seq.of(new org.qed.RelType.BaseType(SqlTypeName.BOOLEAN, false)),
                Set.empty(), Set.empty());
        return RuleBuilder.create().addTable(table).scan("Source").build();
    };

    @Override
    public RelRN before() {
        RelNode n = source.semantics(); // touch
        RexRN a = source.field(0);
        return source.filter(new RexRN.Or(Seq.of(a, new RexRN.Not(a))));
    }

    @Override
    public RelRN after() {
        return source;
    }
}
