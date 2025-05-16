package karate.utils;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class KarateRepor {
    private static final String[] FILES_JSON = {"json"};

    private static final String PROJECT_NAME = "Acceptance Test Report";
    private static final String OUTPUT_DIR = "target";

    public static void generateReport(String karateOutputPath) {
        File reportDir = new File(karateOutputPath);
        if (!reportDir.exists() || !reportDir.isDirectory()) {
            System.err.println("[KarateReport] El directorio de salida no es válido: " + karateOutputPath);
            return;
        }

        List<String> jsonPaths = FileUtils.listFiles(reportDir, FILES_JSON, true)
                .stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());

        if (jsonPaths.isEmpty()) {
            System.err.println("[KarateReport] No se encontraron archivos JSON en: " + karateOutputPath);
            return;
        }

        try {
            Configuration config = new Configuration(new File(OUTPUT_DIR), PROJECT_NAME);
            ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
            reportBuilder.generateReports();
            System.out.println("[KarateReport] Reporte generado correctamente en: " + OUTPUT_DIR);
        } catch (Exception e) {
            System.err.println("[KarateReport] Error al generar el reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

