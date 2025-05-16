package karate.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CSVEditor {
    private static final Logger logger = LoggerFactory.getLogger(CSVEditor.class);
    private static final char DELIMITER = ';';

    public static void modifyCsv(String filePath, String documentColumn, String documentValue,
                                 String targetColumn, String newValue) throws IOException {

        Path csvPath = Paths.get(filePath);
        validateFileExists(csvPath);

        List<Map<String, String>> updatedRecords = new ArrayList<>();
        List<String> headers;
        boolean recordFound = false;

        try (Reader reader = Files.newBufferedReader(csvPath);
             CSVParser parser = CSVFormat.DEFAULT
                     .builder()
                     .setDelimiter(DELIMITER)
                     .setHeader()
                     .setSkipHeaderRecord(true)
                     .setIgnoreSurroundingSpaces(true)
                     .build()
                     .parse(reader)) {

            headers = parser.getHeaderNames();

            validateHeaders(headers, documentColumn, targetColumn);

            for (CSVRecord record : parser) {
                Map<String, String> row = record.toMap();
                if (documentValue.equals(row.get(documentColumn))) {
                    row.put(targetColumn, newValue);
                    recordFound = true;
                }
                updatedRecords.add(row);
            }

        } catch (FileSystemException e) {
            String msg = "El archivo está siendo utilizado por otro proceso (¿abierto en Excel?): " + filePath;
            logger.error(msg);
            throw new IOException(msg, e);
        } catch (IOException e) {
            logger.error("Error al leer el archivo CSV: {}", e.getMessage(), e);
            throw new IOException("No se pudo leer el archivo CSV.", e);
        }

        if (updatedRecords.isEmpty()) {
            logger.warn("El archivo CSV no contiene registros de datos.");
            throw new IllegalStateException("El archivo CSV no tiene registros que procesar.");
        }

        if (!recordFound) {
            String msg = "No se encontró el valor '" + documentValue + "' en la columna '" + documentColumn + "'";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }

        writeCsv(csvPath, headers, updatedRecords);
        logger.info("El archivo CSV ha sido modificado correctamente.");
    }

    private static void writeCsv(Path path, List<String> headers, List<Map<String, String>> records) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.TRUNCATE_EXISTING);
             CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .builder()
                     .setDelimiter(DELIMITER)
                     .setHeader(headers.toArray(new String[0]))
                     .build())) {

            for (Map<String, String> row : records) {
                List<String> values = new ArrayList<>();
                for (String header : headers) {
                    values.add(row.getOrDefault(header, ""));
                }
                printer.printRecord(values);
            }

        } catch (FileSystemException e) {
            String msg = "No se pudo escribir en el archivo porque está abierto en otra aplicación.";
            logger.error(msg);
            throw new IOException(msg, e);
        } catch (IOException e) {
            logger.error("Error al escribir el archivo CSV: {}", e.getMessage(), e);
            throw new IOException("No se pudo escribir el archivo CSV.", e);
        }
    }

    private static void validateFileExists(Path path) throws IOException {
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            String msg = "El archivo especificado no existe: " + path;
            logger.error(msg);
            throw new FileNotFoundException(msg);
        }
    }

    private static void validateHeaders(List<String> headers, String... requiredHeaders) {
        for (String required : requiredHeaders) {
            if (!headers.contains(required)) {
                String msg = "El archivo no contiene la columna requerida: " + required;
                logger.error(msg);
                throw new IllegalArgumentException(msg);
            }
        }
    }
}
