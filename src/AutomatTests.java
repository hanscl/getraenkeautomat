import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class AutomatTests {

    @Test
    public void testTest() {
        TestClass tester = new TestClass();
        assertEquals(1, tester.test(), "result must be 0");
    }
}
