package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;

// SCOPE: PARTIAL — the DistinctOn's already-distinct constant input is modeled as a base table whose single grouping column is a declared strict key (unique, non-nullable: the bag-semantic counterpart of AreValuesDistinct), with plain DistinctOn null semantics, no aggregate outputs, and no Select/Project/LeftJoin wrappers.
public record EliminateDistinctOnValues() implements RRule {
    static final RelRN source = RelRN.scan("S", RexRN.varType("S_Type", false), true);

    @Override
    public RelRN before() {
        return new RelRN.Aggregate(source, Seq.of(source.field(0)), Seq.empty());
    }

    @Override
    public RelRN after() {
        return source.project(source.field(0));
    }
}
