import com.intuit.karate.Results;
import com.intuit.karate.junit5.Karate;
import static org.junit.Assert.*;
import org.junit.Test;


public class TestRunner {

    @Test
    public void testParallel() {
        Results results = Karate.run("src/test/java/features").parallel(4);
        assertTrue(results.getErrorMessages(), results.getFailCount() == 0);
    }

}
