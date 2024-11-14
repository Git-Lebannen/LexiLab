package lexilab;

import java.io.IOException;

public class Main {

    private static final DBInterface dbi = DBInterface.getInstance();

    public static void main(String[] args) throws DBException {
        String[] vocabSets = dbi.getDatabaseNames();

        // if there are no vocab sets (databases yet)
        if (vocabSets.length == 0) {
            do {
                System.out.println("Without a vocabulary set, you can't utilize the programs functionality.");
                System.out.println("You don't have any vocabulary sets yet, would you like to create one?");
            } while (InputReader.select() == 0);
            String name;
            do {
                System.out.println("What do you want to call your vocabulary set?");
                name = InputReader.readString();
                System.out.println("Are you sure you want to call your new vocabulary set: " + name + "?");
            } while (InputReader.select() == 0);
            dbi.createNewDB(name);
        }

        // if the user has existing databases
        else {
            System.out.println("Select a vocabulary set: ");
            int setIndex = InputReader.select(dbi.getDatabaseNames());
        }
    }
}