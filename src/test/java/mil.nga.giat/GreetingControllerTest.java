package mil.nga.giat;

import org.junit.Test;

import static junit.framework.TestCase.*;

public class GreetingControllerTest {

    @Test
    public void testIndex() {
        assertEquals(new GreetingController().index().getContent(), "Hello World!");
    }
}
