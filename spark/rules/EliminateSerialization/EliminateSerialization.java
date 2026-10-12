// SCOPE: PARTIAL — only the sub-case where a DeserializeToObject sits directly above a SerializeFromObject, with a fixed two-column child; faithful uninterpreted model of the object-column round-trip
package org.qed.RRuleInstances;

import kala.collection.Seq;
import org.qed.RelRN;
import org.qed.RelType;
import org.qed.RRule;

public record EliminateSerialization() implements RRule {
    static final RelType.VarType objType = new RelType.VarType("Obj_Type", true);
    static final RelType.VarType colType = new RelType.VarType("Col_Type", true);
    static final RelRN child = RelRN.scanMany("Child", Seq.of(objType, colType));

    // SerializeFromObject: the object column is mapped through an
    // uninterpreted projection `ser`; the pass-through column is preserved.
    static final RelRN serialize = child.project(Seq.of(
            child.proj("ser", "Ser_Type"),
            child.field(1)));

    // DeserializeToObject: the serialized column is mapped through an
    // independent uninterpreted projection `deser`.
    static final RelRN deserialize = serialize.project(serialize.proj("deser", "Obj_Type"));

    @Override
    public RelRN before() {
        return deserialize;
    }

    @Override
    public RelRN after() {
        // The rule's conclusion: a project over the serializer's child keeping
        // only its original object column.
        return child.project(child.field(0));
    }
}
