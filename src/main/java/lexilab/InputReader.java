package lexilab;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InputReader {

    private static final Scanner scanner = new Scanner(System.in);

    // select between no and yes
    public static int selectNoYes() {
        List<String> noYes = new ArrayList<>();
        noYes.add("no");
        noYes.add("yes");
        return select(noYes);
    }

    // return the index of a selection within an array that the user makes
    public static int select(List<String> options) {
        // always add a quit option
        options.add("quit program");

        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ": " + options.get(i));
        }
        System.out.print("Please choose an option by its corresponding number: ");

        // account for difference in array interpretation: beginning at 0 vs beginning at 1 (as displayed for user)
        int selection = getValidIntegerInput(options.size()) - 1;

        // exit program if that's what the user selects
        if (selection + 1 == options.size()) {
            System.out.println("Exiting program.");
            System.exit(0);
        }

        System.out.println("You've selected: " + options.get(selection) + ".");
        return selection;
    }

    // read valid integer input from the cli
    public static int getValidIntegerInput(int max) {
        int input = -1;

        do {
            String userInput = scanner.nextLine();
            try { input = Integer.parseInt(userInput); } catch (Exception _) {}
        } while (input < 1 || input > max);

        return input;
    }

    // read CLI user input as String
    public static String readString() { return scanner.nextLine(); }

    // read CLI user input as String, no spaces allowed
    public static String readStringNoSpace() {
        String input = "";

        do {
            System.out.print("Input cannot contain spaces: ");
            input = readString().trim();
        } while (input.contains(" "));

        return input;
    }
}
