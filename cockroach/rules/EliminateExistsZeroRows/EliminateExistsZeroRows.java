package org.qed.RRuleInstances;

import org.apache.calcite.rel.core.JoinRelType;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RRule;

// SCOPE: PARTIAL — uncorrelated EXISTS only (modeled as a left-semi join on a constant-true condition), with the subquery input known to have zero rows
public record EliminateExistsZeroRows() implements RRule {
    // The outer relation whose rows each test the EXISTS subquery. An
    // uncorrelated EXISTS observes only the subquery's non-emptiness, so it is
    // modeled as a left-semi join with a constant-true condition: every outer
    // row is kept iff the subquery is non-empty.
    static final RelRN outer = RelRN.scan("Outer", "Outer_Type");

    // The subquery's base input, whose type gives the row type of the
    // zero-row relation; the HasZeroRows condition says it produces no rows.
    static final RelRN input = RelRN.scan("Input", "Input_Type");

    // The zero-row input: the same row type as Input but empty.
    static final RelRN zeroRowsInput = input.empty();

    @Override
    public RelRN before() {
        // Exists(input) with (HasZeroRows input): a semi join of the outer
        // relation against the empty subquery input on a constant-true
        // condition keeps no outer row.
        return outer.join(JoinRelType.SEMI, RexRN.trueLiteral(), zeroRowsInput);
    }

    @Override
    public RelRN after() {
        // Replacing the Exists with the constant False: filtering the outer
        // relation by a constant-false predicate keeps no outer row.
        return outer.filter(RexRN.falseLiteral());
    }
}
