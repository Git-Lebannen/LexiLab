package lexilab;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBInterface {
    private final String folderPath = "src/main/resources";
    private static final DBInterface instance = new DBInterface();
    private Connection currentDB;

    private DBInterface() {}

    public static DBInterface getInstance() { return instance; }

    // Function to return SQLite all existing databases' names as a String list
    public List<String> getDatabaseNames() throws DBException {
        List<String> databaseNames = new ArrayList<>();
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new DBException("Resources folder not found at: " + folderPath + ".");
        }

        File[] files = folder.listFiles((_, name) -> name.endsWith(".db"));
        if (files != null) {
            for (File file : files) {
                databaseNames.add(file.getName());
            }
        } else {
            throw new DBException("Error listing database files in directory: " + folderPath + ".");
        }

        return databaseNames;
    }

    // create a new database with a given name and initialize it with a vocabulary table
    public void createNewDB() throws DBException {
        String name;
        do {
            System.out.println("What do you want to call your vocabulary set?");
            name = InputReader.readStringNoSpace();
            System.out.println("Are you sure you want to call your new vocabulary set: " + name + "?");
        } while (InputReader.selectNoYes() == 0);

        String dbUrl = "jdbc:sqlite:" + folderPath + "/" + name + ".db";

        try {
            currentDB = DriverManager.getConnection(dbUrl);
            // Check if connection is successful
            if (currentDB != null) {
                System.out.println("Database created successfully.");
                // create the required columns
                String creationString = """
                            CREATE TABLE IF NOT EXISTS vocab (
                                id INTEGER PRIMARY KEY AUTOINCREMENT,
                                word TEXT NOT NULL,
                                wordTranslation TEXT NOT NULL,
                                tag TEXT
                            );
                        """;
                try {
                    Statement stmt = currentDB.createStatement();
                    stmt.executeUpdate(creationString);
                    stmt.close();
                    System.out.println("Vocabulary set columns created successfully.");
                } catch (Exception _) {
                    throw new DBException("Error creating vocabulary set columns.");
                }
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
            throw new DBException("Error linking database.");
        }
    }

    // add a vocab to the current connections vocab table
    public void add(String word, String wordTranslation, String tag) throws DBException {
        String insertString = "INSERT INTO vocab (word, wordTranslation, tag) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = currentDB.prepareStatement(insertString)) {
            stmt.setString(1, word);              // Set the word
            stmt.setString(2, wordTranslation);   // Set the translation

            // Set the tag, even if it's null (optional value)
            if (tag != null) {
                stmt.setString(3, tag);          // Set tag value if it's not null
            } else {
                stmt.setNull(3, java.sql.Types.NULL);  // Set NULL if tag is null
            }

            stmt.executeUpdate();  // Execute the insertion
            stmt.close();
            System.out.println("Word added successfully.");
        } catch (Exception _) {
            throw new DBException("Error adding vocab.");
        }
    }

    // remove a word and its translation
    public void rem(String word) throws DBException {
        String removeString = "DELETE FROM vocab WHERE word = ?";

        try (PreparedStatement stmt = currentDB.prepareStatement(removeString)) {
            stmt.setString(1, word);
            stmt.executeUpdate();
            System.out.println("Removed successfully.");
        } catch (Exception _) {
            throw new DBException("Error removing word.");
        }
    }

    // remove a translation and its word
    public void remT(String translation) throws DBException {
        String removeString = "DELETE FROM vocab WHERE wordTranslation = ?";

        try (PreparedStatement stmt = currentDB.prepareStatement(removeString)) {
            stmt.setString(1, translation);
            stmt.executeUpdate();
            System.out.println("Removed successfully.");
        } catch (Exception _) {
            throw new DBException("Error removing translation.");
        }
    }
}