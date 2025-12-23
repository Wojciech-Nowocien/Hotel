package util;

import java.util.Scanner;

public class BetterScanner {
    private Scanner scanner;

    public BetterScanner() {
        scanner = new Scanner(System.in);
    }

    public String nextLine() {
        return scanner.nextLine();
    }

    public int nextInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim().strip());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
