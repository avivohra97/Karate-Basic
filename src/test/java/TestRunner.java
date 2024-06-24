import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TestRunner {
    String karateOutPut = "target/cucumber";

    /**
     * Below User of runner class was done inorder to support parallel runs
     * Cucumber JSON reports can be generated, except that the extension will be .json instead of .xml.
     * Note that you have to call the outputCucumberJson(true) method on the Runner "builder".
     *
     * The Runner.path() "builder" method in karate-core is how you refer to the package you want to execute,
     * and all feature files within sub-directories will be picked up
     *
     * Runner.path() takes multiple string parameters, so you can refer to multiple packages
     * or even individual *.feature files and easily "compose" a test-suite
     * e.g. Runner.path("classpath:animals", "classpath:some/other/package.feature")
     *
     *
     * To choose tags, call the tags() API, note that by default,
     * any *.feature file tagged with the special (built-in) tag: @ignore will be skipped. You can also specify tags on the command-line. The tags() method also takes multiple arguments, for e.g.
     * this is an "AND" operation: tags("@customer", "@smoke")
     * and this is an "OR" operation: tags("@customer,@smoke")
     *
     * There is an optional reportDir() method if you want to customize the directory to which the HTML, XML and JSON files will be output, it defaults to target/karate-reports
     */

    @Test
    public void testParallel() throws IOException {
        Results results = Runner.path("classpath:features")
                .reportDir(karateOutPut)
                .outputCucumberJson(true)
                .outputJunitXml(true)
                .tags("@firstTest")
                .parallel(1);
        generateReport(karateOutPut);
        ZephyrUtils.uploadResult();
        assertTrue("Failures in suite", results.getFailCount()==0);
    }

    public static void generateReport(String karateOutputPath) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        Collection<File> jsonFiles = FileUtils.listFiles(new File(karateOutputPath), new String[] {"json"}, true);
        List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
        jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
        Configuration config = new Configuration(new File(System.getProperty("user.dir") + "/reports/TestExecution" + formatter.format(localDateTime)), "Karate-Basic");
        ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
        reportBuilder.generateReports();
    }

}
