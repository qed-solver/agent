package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.apache.calcite.rel.RelNode;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — the project over the concrete non-empty Values is an input-reference-only column
// selection/reorder (no operator expressions), which the rule folds into a smaller concrete Values
public record ValuesReduce() implements RRule {

    @Override
    public RelRN before() {
        // Concrete Values: 3 columns (a, b, c) with tuples (1,2,3) and (4,5,6).
        RelNode node = RuleBuilder.create()
                .values(new String[]{"a", "b", "c"}, 1, 2, 3, 4, 5, 6)
                .build();
        RelRN values = () -> node;
        // Project that only reorders/selects input references: emit (c, a).
        return values.project(Seq.of(values.field(2), values.field(0)));
    }

    @Override
    public RelRN after() {
        // The rule folds the input-reference-only project into the Values:
        // (a,b,c) -> (c,a) turns tuples (1,2,3),(4,5,6) into (3,1),(6,4).
        RelNode node = RuleBuilder.create()
                .values(new String[]{"c", "a"}, 3, 1, 6, 4)
                .build();
        return () -> node;
    }
}
