package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;

// SCOPE: PARTIAL — round-trip sub-rewrite only: JsonToStructs(StructsToJson(x)) = x
public record OptimizeCsvJsonExprs() implements RRule {

    static final RelRN source = RelRN.scan("Source", "Json_Type");

    @Override
    public RelRN before() {
        return source
            .project("StructsToJson", "Json_Type")
            .project("JsonToStructs", "Json_Type");
    }

    @Override
    public RelRN after() {
        return source;
    }
}
