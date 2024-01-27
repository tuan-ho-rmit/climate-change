package com.studio.climatechange;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Helper {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/climatechange";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "SAL@04011981";
    private static final int CHUNK_SIZE = 10000; // Adjust based on your file size and memory availability

    public static void main(String[] args) {
        List<String> csvFiles = List.of("Student.csv", "Persona.csv", "Country.csv", "City.csv", "State.csv", "Population.csv", "Global.csv", "Temp.csv");
        List<String> tableNames = List.of("Student", "Persona", "Country", "City", "State", "Population", "Global", "Temperature");

        for (int i = 0; i < csvFiles.size() - 1; i++) { // Process all files except Temperature.csv
            insertCsvIntoTable(csvFiles.get(i), tableNames.get(i));
        }

        // Process Temperature.csv with chunk processing
        processTemperatureFile("C:\\Users\\Admin\\Downloads\\climate-change-develop\\climate-change-develop\\data\\Temperature.csv");
    }

    private static void processTemperatureFile(String filePath) {
        try {
            List<String[]> csvChunks = splitCsvFileIntoChunks(filePath, CHUNK_SIZE);

            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            for (String[] chunk : csvChunks) {
                executor.submit(() -> processAndInsertChunk(chunk, JDBC_URL, USERNAME, PASSWORD));
            }

            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static String createInsertStatement(String tableName, String[] columnNames) {
        StringJoiner columns = new StringJoiner(", ");
        StringJoiner placeholders = new StringJoiner(", ");

        System.out.println("Creating SQL for table: " + tableName); // Debugging

        for (String col : columnNames) {
            columns.add(col.trim()); // Trim any leading/trailing whitespaces
            placeholders.add("?");
            System.out.println("Column: " + col.trim()); // Debugging
        }

        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
        System.out.println("SQL: " + sql); // Debugging
        return sql;
    }

    private static void insertCsvIntoTable(String csvFile, String tableName) {
        String filePath = "C:\\Users\\Admin\\Downloads\\climate-change-develop\\climate-change-develop\\data\\" + csvFile;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             BufferedReader lineReader = new BufferedReader(new FileReader(filePath))) {

            String lineText = lineReader.readLine(); // Read header line
            if (lineText == null) return;

            String[] columnNames = lineText.split(",", -1);
            String sql = createInsertStatement(tableName, columnNames);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                connection.setAutoCommit(false);

                while ((lineText = lineReader.readLine()) != null) {
                    String[] data = lineText.split(",", -1);

                    for (int i = 0; i < data.length; i++) {
                        if (data[i].trim().isEmpty()) {
                            statement.setNull(i + 1, Types.VARCHAR); // Assume VARCHAR for simplicity
                        } else {
                            statement.setString(i + 1, data[i]);
                        }
                    }
                    statement.addBatch();
                }

                statement.executeBatch();
                connection.commit();
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void processAndInsertChunk(String[] chunk, String jdbcURL, String username, String password) {
        String sql = "INSERT INTO temperature (id, average_temperature, minimum_temperature, maximum_temperature, Year, country_id, city_id, state_id, global_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password);
             Statement stmt = connection.createStatement();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Disable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS=0");

            connection.setAutoCommit(false);

            for (String line : chunk) {
                String[] data = line.split(",", -1);

                for (int i = 0; i < data.length; i++) {
                    if (data[i].trim().isEmpty()) {
                        statement.setNull(i + 1, java.sql.Types.VARCHAR);
                    } else {
                        statement.setString(i + 1, data[i]);
                    }
                }
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();

            // Re-enable foreign key checks
            stmt.execute("SET FOREIGN_KEY_CHECKS=1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static List<String[]> splitCsvFileIntoChunks(String filePath, int chunkSize) throws IOException {
        List<String[]> chunks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            List<String> currentChunk = new ArrayList<>();

            reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                currentChunk.add(line);
                if (++count % chunkSize == 0) {
                    chunks.add(currentChunk.toArray(new String[0]));
                    currentChunk.clear();
                }
            }

            // Add the last chunk if it has data
            if (!currentChunk.isEmpty()) {
                chunks.add(currentChunk.toArray(new String[0]));
            }
        }
        return chunks;
    }
    // Rest of the methods (splitCsvFileIntoChunks, processAndInsertChunk, createInsertStatement) remain the same
}

