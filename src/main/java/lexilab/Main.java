package lexilab;

import java.util.List;

public class Main {

    private static final DBInterface dbi = DBInterface.getInstance();
    private static final String commands = """
            At any time, input 'help' to checkout possible commands.
            Input 'exit' to return to start screen.
            Supported commands:
            "add (word) (translation) (optional: tag)": Adds a word, its translation and an optional tag, used to sort vocabulary into groups.
            "rem (word/translation)": Removes the word and its translation.
            "tags": Lists the tags you have used in the vocabulary set.
            "lab (optional: tag or tags)": The program starts querying you vocabulary from the set, filtered by tag.
            """;

    public static void main(String[] args) throws DBException {
        String[] commandInput;
        boolean startScreen = false;
        while (true) {
            // only print start screen if user has yet to enter the input loop
            if (!startScreen) {

                // if there are no vocab sets (databases yet)
                if (dbi.getDatabaseNames().isEmpty()) {
                    do {
                        System.out.println("Without a vocabulary set, you can't utilize the programs functionality.");
                        System.out.println("You don't have any vocabulary sets yet, would you like to create one?");
                    } while (InputReader.selectNoYes() == 0);

                    dbi.createNewDB();
                }

                // once the user has existing databases
                int setIndex;
                List<String> vocabSets;

                // give option to add more sets
                do {
                    vocabSets = dbi.getDatabaseNames();
                    vocabSets.add("Add vocabulary set");
                    System.out.println("Select or add a vocabulary set: ");
                    setIndex = InputReader.select(vocabSets);
                    if (setIndex == vocabSets.size() - 2) {
                        dbi.createNewDB();
                    }
                } while (setIndex == vocabSets.size() - 2);

                // by this point a vocab set has been selected
                dbi.selectDB(dbi.getDatabaseNames().get(setIndex));
                System.out.print(commands);

                // make sure the start setup isn't printed for users in the input loop
                startScreen = true;
            }

            // user input loop
            commandInput = InputReader.readString().split("\\s+");
            switch (commandInput[0].toLowerCase()) {
                case "exit": startScreen = false; break;
                case "help": System.out.print(commands); break;
                case "add":
                    System.out.println("Add a vocab");
                    break;
                case "rem":
                    System.out.println("Remove a vocab");
                    break;
                case "tags":
                    System.out.println("Print all tags");
                    break;
                case "lab":
                    System.out.println("Start quizzing");
                    break;
            }
        }
    }
}