package org.qed.RRuleInstances;

import kala.collection.Seq;
import kala.collection.Set;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rex.RexBuilder;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.qed.QedTable;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RexRN;
import org.qed.RRule;
import org.qed.RuleBuilder;

// SCOPE: PARTIAL — models only the `rand() < 1.0` redundant-filter-removal branch (encoded as a check constraint on the rand output); the `rand() < 0.0 → empty` branch and the nondeterministic scalar semantics of `rand()` are out of scope.

public record OptimizeRand() implements RRule {

    static final RelType.VarType VAR = new RelType.VarType("rand_type", true);

    static final ConstrainedScan SCAN = new ConstrainedScan();

    private record ConstrainedScan() implements RelRN {

        @Override
        public RelNode semantics() {
            RexBuilder rb = RuleBuilder.create().getRexBuilder();
            RexNode col = rb.makeInputRef(VAR, 0);
            RexNode one = rb.makeLiteral(1, VAR);
            RexNode constraint = rb.makeCall(SqlStdOperatorTable.LESS_THAN, col, one);
            QedTable table = new QedTable("Rand",
                    Seq.of("col0"),
                    Seq.of((RelDataType) VAR),
                    Set.of(),
                    Set.of(constraint));
            RuleBuilder b = RuleBuilder.create();
            try {
                b.addTable(table);
            } catch (Exception ignored) {
                // table already registered with the same constraints
            }
            return b.scan("Rand").build();
        }

        @Override
        public RelRN.Filter filter(RexRN cond) {
            return new RelRN.Filter(cond, this);
        }

        @Override
        public RexRN.Pred pred(String name) {
            throw new UnsupportedOperationException("pred not needed for this rule");
        }

        @Override
        public RexRN field(int ordinal) {
            return new RexRN.Field(ordinal, this);
        }
    }

    private record LitOne() implements RexRN {
        @Override
        public RexNode semantics() {
            return RuleBuilder.create().getRexBuilder().makeLiteral(1, VAR);
        }
    }

    @Override
    public RelRN before() {
        return SCAN.filter(new RexRN.Pred(
                SqlStdOperatorTable.LESS_THAN,
                Seq.of(SCAN.field(0), new LitOne())));
    }

    @Override
    public RelRN after() {
        return SCAN;
    }
}
