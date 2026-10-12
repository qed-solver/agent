// SCOPE: PARTIAL — only the top-level Sort removal (removeTopLevelSort, "tuples produced by a subquery are un-ordered") applied to the EXISTS subquery plan is encoded; the scalar-subquery COUNT-bug preservation and general subquery re-optimization branches are not
package org.qed.RRuleInstances;

import org.apache.calcite.rel.RelCollation;
import org.apache.calcite.rel.RelCollations;
import org.apache.calcite.rel.RelFieldCollation;
import org.qed.RRule;
import org.qed.RelRN;

import java.util.List;

public record OptimizeSubqueries() implements RRule {
    static final RelRN sub = RelRN.scan("Sub", "Sub_Type");
    static final RelCollation collation = RelCollations.of(
            List.of(new RelFieldCollation(0, RelFieldCollation.Direction.ASCENDING)));

    @Override
    public RelRN before() {
        return sub.sort(collation);
    }

    @Override
    public RelRN after() {
        return sub;
    }
}
