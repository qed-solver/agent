package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;

// Direct encoding of NormalizeLikeAny: `x LIKE '%'` has no string/glob theory in
// QED, so it can only be introduced as an uninterpreted predicate symbol. This
// tests whether QED can nonetheless derive filter-equivalence with x IS NOT NULL.
public record NormalizeLikeAny() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");

    @Override
    public RelRN before() {
        // x LIKE '%' modeled as an uninterpreted predicate over the row
        return source.filter(source.pred("like_all"));
    }

    @Override
    public RelRN after() {
        return source.filter(source.field(0).pred(SqlStdOperatorTable.IS_NOT_NULL));
    }
}
