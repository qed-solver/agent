package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — single-key-column child, fixed two-branch Expand, upper aggregate modeled group-by-only (its aggregate expressions omitted), and each branch's gid modeled as an uninterpreted function of the key
public record OptimizeExpand() implements RRule {
    // Child of the Expand: a bag of rows with one reference column k.
    static final RelRN src = RelRN.scan("src", "k_type");

    // Expand modeled as a bag-union of two branches; branch i outputs
    // [k, slot_i(k), gid_i(k)] where slot_i and gid_i are branch-specific
    // uninterpreted functions of the row (reused names = same symbol on both sides).
    static final RelRN branch1 = src.project(Seq.of(src.field(0), src.proj("d1", "d_type"), src.proj("g1", "gid_type")));
    static final RelRN branch2 = src.project(Seq.of(src.field(0), src.proj("d2", "d_type"), src.proj("g2", "gid_type")));
    static final RelRN expand = branch1.union(true, branch2);

    // Pre-aggregate inserted below the Expand by the rule: group-by-only over
    // the reference column, collapsing duplicate tuples the Expand branches consume.
    static final RelRN preAgg = new RelRN.Aggregate(src, Seq.of(src.field(0)), Seq.of());

    // The same two Expand branches, now over the pre-aggregate's output.
    static final RelRN preBranch1 = preAgg.project(Seq.of(preAgg.field(0), preAgg.proj("d1", "d_type"), preAgg.proj("g1", "gid_type")));
    static final RelRN preBranch2 = preAgg.project(Seq.of(preAgg.field(0), preAgg.proj("d2", "d_type"), preAgg.proj("g2", "gid_type")));
    static final RelRN preExpand = preBranch1.union(true, preBranch2);

    // Upper aggregate groups over all Expand-produced attributes
    // (key + distinct-expression slot + gid); group-by-only, so it dedupes.
    static final RelRN before = new RelRN.Aggregate(expand, Seq.of(expand.field(0), expand.field(1), expand.field(2)), Seq.of());
    static final RelRN after = new RelRN.Aggregate(preExpand, Seq.of(preExpand.field(0), preExpand.field(1), preExpand.field(2)), Seq.of());

    @Override
    public RelRN before() { return before; }

    @Override
    public RelRN after() { return after; }
}