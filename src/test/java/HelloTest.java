import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloTest {
    @Test
    void fooTest() {
        assertEquals(new Hello().foo(), 1, "Should be 1");
    }
}