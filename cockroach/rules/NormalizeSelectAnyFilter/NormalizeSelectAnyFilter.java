package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.JoinKind;

// SCOPE: FULL
public record NormalizeSelectAnyFilter() implements RRule {
    static final RelRN a = RelRN.scan("A", "T1");
    static final RelRN b = RelRN.scan("B", "T2");
    static final RexRN pa = a.pred("pa");
    static final RexRN pb = b.pred("pb");

    void p1() { RelRN r = a.join(b, "c"); }
    void p2() { RelRN r = a.join(b, "c", JoinKind.Inner); }
    void p3() { RelRN r = RelRN.join(a, b, "c"); }
    void p4() { RelRN r = a.semiJoin(b, "c"); }
    void p5() { RexRN r = a.exists(b, "c"); }
    void p6() { RexRN r = a.exists(b); }
    void p7() { RexRN r = b.exists(); }
    void p8() { RexRN r = a.any(b, "c"); }
    void p9() { RexRN r = a.any(b); }
    void p10() { RexRN r = b.any(); }
    void p11() { RexRN r = RexRN.exists(b); }
    void p12() { RexRN r = RexRN.any(b); }
    void p13() { RexRN r = RexRN.or(pa, pb); }
    void p14() { RexRN r = pa.not(); }
    void p15() { RelRN r = a.union(b); }
    void p16() { RelRN r = a.intersect(b); }
    void p17() { RelRN r = a.minus(b); }
    void p18() { RelRN r = RelRN.empty(); }
    void p19() { RelRN r = a.project("x"); }
    void p20() { RelRN r = a.aggregate(b, "g", "f"); }
    void p21() { RelRN r = a.filterExists(b, "c"); }
    void p22() { RexRN r = RelRN.exists(a, b, "c"); }

    @Override public RelRN before() { return a.filter(pa); }
    @Override public RelRN after() { return b.filter(pb); }
}
