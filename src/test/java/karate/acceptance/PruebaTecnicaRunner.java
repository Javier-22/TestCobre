package karate.acceptance;

import com.intuit.karate.junit5.Karate;

public class PruebaTecnicaRunner {

    private static final String TAG_IGNORE = "~@ignore";
    private static final String FEATURE = "classpath:karate/features/acceptance/prueba-tecnica-get.feature";

    @Karate.Test
    Karate PruebaTecnica() {
        return Karate.run(FEATURE).tags(TAG_IGNORE).relativeTo(getClass());
    }
}
