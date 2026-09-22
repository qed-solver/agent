package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.core.JoinRelType;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.RelRN;
import org.qed.RRule;
import org.qed.RexRN;

// SCOPE: PARTIAL — the join is a LEFT join of two one-column inputs on a concrete equality of the two columns, the right (non-preserved) input's column is unique (a key), and the project selects only the left (preserved) input's column.
public record ProjectJoinRemove() implements RRule {
    // Both inputs' single column carries the same uninterpreted join-key type.
    static final RelRN left = RelRN.scan("L", RexRN.varType("Key_Type", true), false);

    // R's single column is unique (a key): the rule's premise that no left row
    // can be duplicated by matching more than one right row.
    static final RelRN right = RelRN.scan("R", RexRN.varType("Key_Type", true), true);

    // Concrete equi-join condition referencing the two join inputs' columns.
    static final RexRN joinCond = new RexRN.Pred(
            SqlStdOperatorTable.EQUALS,
            Seq.of(left.joinField(0, right), left.joinField(1, right)));

    static final RelRN join = left.join(JoinRelType.LEFT, joinCond, right);

    @Override
    public RelRN before() {
        // Project the join's OWN output column 0 (the left input's column),
        // referenced as a plain field over the join relation.
        return join.project(new RexRN.Field(0, join));
    }

    @Override
    public RelRN after() {
        return left;
    }
}
