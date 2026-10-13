package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// Direct encoding of the core SimplifyRegexToLike transformation:
// `a ~ 'foo'` (unanchored literal regex)  <=>  `a LIKE '%foo%'`.
// Both the regex-match operator and the LIKE operator can only be
// introduced as uninterpreted predicate symbols over the same column.
public record SimplifyRegexToLike() implements RRule {
    static final RelRN source = RelRN.scan("Source", "Source_Type");

    @Override
    public RelRN before() {
        // a ~ 'foo' : uninterpreted regex-match predicate over the column
        return source.filter(source.pred("regex_match_foo"));
    }

    @Override
    public RelRN after() {
        // a LIKE '%foo%' : uninterpreted like predicate over the column
        return source.filter(source.pred("like_foo"));
    }
}
