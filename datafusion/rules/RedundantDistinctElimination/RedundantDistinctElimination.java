package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the DISTINCT's input is assumed to be already unique on all of its columns, modelled as a GROUP BY over exactly all of a fixed two-column input's columns (DataFusion's prototypical Dependency::Single-over-all-fields case), so the DISTINCT is a no-op and is simply removed.
public record RedundantDistinctElimination() implements RRule {
    static final RelRN source = RelRN.scanMany("Source", Seq.of(
            RexRN.varType("Col0_Type", false),
            RexRN.varType("Col1_Type", false)));

    // The Distinct's input: a GROUP BY over all of the source's columns with no
    // aggregate calls, whose output therefore contains each distinct tuple
    // exactly once — the branch's "input is already unique on all columns" case
    // (e.g. a former GROUP BY over exactly these columns).
    static final RelRN uniqueInput =
            new RelRN.Aggregate(source, source.fields(0, 1), Seq.empty());

    @Override
    public RelRN before() {
        // Distinct(All(uniqueInput)): the set-variant self-intersection emits
        // each distinct row once, which over an already-unique input is the
        // input itself.
        return uniqueInput.intersect(false, uniqueInput);
    }

    @Override
    public RelRN after() {
        // Redundant distinct eliminated: just the (already unique) input.
        return uniqueInput;
    }
}