package lexilab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class InputReader {

    // select between no and yes
    public static int select() { return select(new String[] {"no", "yes"}); }

    // return the index of a selection within an array that the user makes
    public static int select(String[] s) {
        // always add a quit option
        List<String> options = new ArrayList<>(Arrays.asList(s));
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
        Scanner scanner = new Scanner(System.in);
        int input = -1;

        do {
            String userInput = scanner.nextLine();
            try { input = Integer.parseInt(userInput); } catch (Exception _) {}
        } while (input < 1 || input > max);

        return input;
    }

    // read CLI user input as String, no spaces allowed
    public static String readString() {
        Scanner scanner = new Scanner(System.in);
        String input;

        while (true) {
            input = scanner.nextLine().trim();  // Read the input and remove leading/trailing spaces
            if (input.contains(" ")) {
                System.out.println("Input cannot contain spaces.");
            } else { break; }
        }

        return input;
    }
}
