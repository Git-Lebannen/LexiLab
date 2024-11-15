package lexilab;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class DBInterface {
    private final String folderPath = "src/main/resources";
    private static final DBInterface instance = new DBInterface();
    private Connection currentDB;

    private DBInterface() {}

    public static DBInterface getInstance() { return instance; }

    // Function to return SQLite all existing databases' names as a String array
    public List<String> getDatabaseNames() throws DBException {
        List<String> databaseNames = new ArrayList<>();
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new DBException("Resources folder not found at: " + folderPath);
        }

        File[] files = folder.listFiles((_, name) -> name.endsWith(".db"));
        if (files != null) {
            for (File file : files) {
                databaseNames.add(file.getName());
            }
        } else {
            throw new DBException("Error listing database files in directory: " + folderPath);
        }

        return databaseNames;
    }

    // create a new database with a given name
    public void createNewDB() throws DBException {
        String name = "";
        do {
            System.out.println("What do you want to call your vocabulary set?");
            name = InputReader.readStringNoSpace();
            System.out.println("Are you sure you want to call your new vocabulary set: " + name + "?");
        } while (InputReader.selectNoYes() == 0);

        String dbUrl = "jdbc:sqlite:" + folderPath + "/" + name + ".db";

        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            // Check if connection is successful
            if (connection != null) {
                System.out.println("Database created successfully!");
            }
        } catch (Exception _) {
            throw new DBException("Error creating database");
        }
    }

    // link DBInterface with a database
    public void selectDB(String name) throws DBException {
        String dbUrl = "jdbc:sqlite:" + folderPath + "/" + name;
        try {
            currentDB = DriverManager.getConnection(dbUrl);
        } catch (Exception _) {
            throw new DBException("Error linking database");
        }
    }
}