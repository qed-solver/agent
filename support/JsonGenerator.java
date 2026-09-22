import org.qed.RRule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.file.*;

/**
 * Standalone helper used by the porting agent: instantiate a single RRule
 * (by fully-qualified class name) and dump its QED JSON to <outDir>/<RuleName>.json
 *
 * Usage: java -cp <classpath> JsonGenerator <fully.qualified.ClassName> <outDir>
 */
public class JsonGenerator {
    public static void main(String[] args) throws Exception {
        String className = args[0];
        String outDir = args[1];
        Class<?> clazz = Class.forName(className);
        RRule rule = (RRule) clazz.getDeclaredConstructor().newInstance();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode jsonNode = rule.toJson();
        Files.createDirectories(Path.of(outDir));
        mapper.writerWithDefaultPrettyPrinter().writeValue(
            Path.of(outDir, rule.name() + ".json").toFile(),
            jsonNode
        );
    }
}
