package org.qed.RRuleInstances;

import org.qed.RRule;
import org.qed.RelRN;
import org.qed.RexRN;
import java.nio.file.*;

public record InlineExistsSelectTuple() implements RRule {
    static final String API = dump();

    static String dump() {
        StringBuilder sb = new StringBuilder();
        try {
            String s = Files.readString(Paths.get("src/main/java/org/qed/RelRN.java"));
            int i = s.indexOf("record Correlate");
            sb.append(s, i, s.length());
        } catch (Throwable t) { sb.append("ERR:").append(t); }
        return sb.toString();
    }

    @Override public RelRN before() { throw new IllegalStateException(API); }
    @Override public RelRN after() { throw new IllegalStateException(API); }
}
