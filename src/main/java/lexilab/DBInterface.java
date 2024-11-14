package lexilab;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class DBInterface {
    private static final String path = "src/main/resources";
    private static final DBInterface instance = new DBInterface();

    private DBInterface() {}

    public static DBInterface getInstance() { return instance; }

    // Function to return SQLite all existing databases' names as a String array
    public String[] getDatabaseNames() throws DBException {
        List<String> databaseNames = new ArrayList<>();
        File folder = new File(path);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new DBException("Resources folder not found at: " + path);
        }

        File[] files = folder.listFiles((_, name) -> name.endsWith(".db"));
        if (files != null) {
            for (File file : files) {
                databaseNames.add(file.getName());
            }
        } else {
            throw new DBException("Error listing database files in directory: " + path);
        }

        return databaseNames.toArray(new String[0]);
    }

    // create a new database with a given name
    public void createNewDB(String name) throws DBException {
        String dbUrl = "jdbc:sqlite:" + path + "/" + name + ".db";

        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            // Check if connection is successful
            if (connection != null) {
                System.out.println("Database created successfully!");
            }
        } catch (Exception _) {
            throw new DBException("Error creating database");
        }
    }
}