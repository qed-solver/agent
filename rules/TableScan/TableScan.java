package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// Candidate encoding: view V over base table T, V defined as Filter(P, Scan(T)).
// before = Scan(V), after = Filter(P, Scan(T)).
public record TableScan() implements RRule {
    static final RelRN t = RelRN.scan("T", "Row_Type");
    static final RelRN v = RelRN.scan("V", "Row_Type");
    static final RexRN p = t.pred("P");

    @Override
    public RelRN before() {
        return v;              // LogicalTableScan(V)
    }

    @Override
    public RelRN after() {
        return t.filter(p);    // V's definition: Filter(P, Scan(T))
    }
}
