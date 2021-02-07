package traces;

import org.openjdk.btrace.core.BTraceUtils;
import org.openjdk.btrace.core.annotations.*;

import java.util.Deque;

import static io.btrace.extensions.one.MainEntry.*;

/**
 * @author Jaroslav Bachorik
 */
@BTrace(trusted = true)
public class SimpleExtensionTest {
    @OnMethod(clazz = "resources.Main", method = "callA")
    public static void noargs(@Self Object self) {
        ext_test("hey ho");
    }
}