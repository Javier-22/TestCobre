package karate.runners;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import karate.utils.KarateRepor;
import org.junit.jupiter.api.Test;


public class GeneralRunner {


    private static final String CLASSPATH_KARATE_ACCEPTANCE = "classpath:karate/features/acceptance";
    private static final String CLASSPATH_KARATE_E2E = "classpath:karate/features/karate.e2e";
    private static final String WITH_TAGS = System.getProperty("withTags", "~@ignore");
    private static final String TEST_SUITE = System.getProperty("test-suite");
    private static final int THREAD_NUMBER = Integer.parseInt(System.getProperty("threads-count", "1"));
    @Test
    void testParallel() {
        String testSuitePath = "acceptance".equals(TEST_SUITE) ? CLASSPATH_KARATE_ACCEPTANCE : CLASSPATH_KARATE_E2E;
        Results results = Runner.path(testSuitePath)
                .outputCucumberJson(true)
                .tags(WITH_TAGS)
                .parallel(THREAD_NUMBER);

        KarateRepor.generateReport(results.getReportDir());

        if (results.getFailCount() > 0) {
            System.err.println("Errores detectados en pruebas: " + results.getErrorMessages());
            throw new AssertionError("Fallaron " + results.getFailCount() + " pruebas.");
        }
    }

}
