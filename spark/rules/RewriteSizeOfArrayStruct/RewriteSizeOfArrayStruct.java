package org.qed.RRuleInstances;

import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactoryImpl;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlStdOperatorTable;
import org.apache.calcite.sql.SqlOperator;
import org.qed.RRule;
import org.qed.RelDataTypeFactory;
import org.qed.RelRN;
import org.qed.RexRN;
import org.qed.RuleBuilder;
import org.qed.RelType;
import org.qed.QedTable;
import kala.collection.Seq;
import kala.collection.Set;

// SCOPE: FULL
public record RewriteSizeOfArrayStruct() implements RRule {

    // The array/struct column flowing through the rule (child's single column).
    static final RelType colType = RexRN.varType("ArrayStructType", true);
    // The (integer) length result of Size / Size(GASF(...)).
    static final RelType sizeType = RexRN.varType("SizeType", true);

    // One shared RuleBuilder so the same SqlOperator instances appear both in
    // the table constraint (the axiom) and in the before/after projections.
    static final RuleBuilder RB = RuleBuilder.create();
    static final SqlOperator sizeOp =
            RB.genericProjectionOp("Size", sizeType);
    static final SqlOperator gasfOp =
            RB.genericProjectionOp("GetArrayStructFields", sizeType);

    /**
     * The scan of the child relation, carrying as a table constraint the
     * semantic axiom of the rule: extraction of the cheapest struct field
     * preserves array length and null-ness, i.e. for every row,
     * Size(GASF(c)) IS NOT DISTINCT FROM Size(c) (NDF so the axiom also
     * covers rows where c, and hence both sizes, is NULL).
     */
    static final class AxiomScan implements RelRN {
        @Override
        public org.apache.calcite.rel.RelNode semantics() {
            RelDataType col = colType.toType(RelDataTypeFactory.INSTANCE);
            RexNode c = RB.getRexBuilder().makeInputRef(
                    RelDataTypeFactoryImpl.createStructType(Seq.<RelDataType>empty().toArray(RelDataType[]::new)),
                    0);
            RexNode s1 = RB.call(sizeOp, Seq.of(c));
            RexNode s2 = RB.call(gasfOp, Seq.of(c));
            RexNode axiom = RB.getRexBuilder().makeCall(
                    SqlStdOperatorTable.IS_NOT_DISTINCT_FROM, s1, s2);
            QedTable t = new QedTable(
                    "Child",
                    Seq.of("col-Child"),
                    Seq.of(colType),
                    Set.empty(),
                    Set.of(axiom));
            return RB.addTable(t).scan("Child").build();
        }
    }

    static final RelRN child = new AxiomScan();
    static final RexRN colRef = new RexRN.Field(0, child);

    @Override
    public RelRN before() {
        return child.project(sizeOp, Seq.of(colRef));
    }

    @Override
    public RelRN after() {
        return child.project(sizeOp, Seq.of(new RexRN.Proj(gasfOp, Seq.of(colRef))));
    }
}
