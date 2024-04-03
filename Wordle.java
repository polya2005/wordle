import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Wordle {
    private static LetterBlock[][] grid = new LetterBlock[6][5];
    private static HashSet<String> allowedWords = new HashSet<>();
    private static ArrayList<String> possibleWords = new ArrayList<>();
    private static final String CSI = "" + (char) (0x1B) + "[";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String args[]) throws FileNotFoundException {
        // obtain the list of allowed words
        Scanner allowedWordsScanner = new Scanner(new File("allowed_words.txt"));
        while (allowedWordsScanner.hasNext()) {
            allowedWords.add(allowedWordsScanner.next());
        }
        allowedWordsScanner.close();

        // obtain the list of possible words
        Scanner possibleWordsScanner = new Scanner(new File("possible_words.txt"));
        while (possibleWordsScanner.hasNext()) {
            possibleWords.add(possibleWordsScanner.next());
        }
        possibleWordsScanner.close();

        // main game loop
        String response;
        do {
            playGame();
            System.out.print("Do you want to play again? (y/n): ");
            response = scanner.nextLine().toLowerCase();
        } while (response.equals("y"));
        scanner.close();
    }

    private static void playGame() {
        String actualWord = possibleWords.get((int) (Math.random() * possibleWords.size())); // get a random word
        // Initialze the printing mechanism
        LetterBlock.initLetterBlocks();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new LetterBlock(i, j);
            }
        }

        for (int i = 0; i < 6; i++) {
            // prompt user to reguess until the guess is valid
            String guess = scanner.nextLine().toLowerCase();
            while (!allowedWords.contains(guess)) {
                System.out.print(CSI + "8H"); // move cursor to row 8
                System.out.print(CSI + "2K"); // clear the 8th row
                System.out.print("Invalid guess: " + guess + ". Try again.");
                System.out.print(CSI + (i + 1) + "H"); // move cursor back to the original row
                System.out.print(CSI + "2K"); // clear the input line
                guess = scanner.nextLine().toLowerCase();
            }
            System.out.print(CSI + "8H"); // move cursor to row 8
            System.out.print(CSI + "2K"); // clear the 8th row
            String colors = detectWord(actualWord, guess);
            for (int j = 0; j < 5; j++) {
                grid[i][j].setCharacter(guess.charAt(j));
                grid[i][j].setColor(LetterBlock.BoxColor.getBoxColor(colors.charAt(j)));
                grid[i][j].showBox();
            }
            System.out.println();
            if (colors.equals("GGGGG")) {
                System.out.print(CSI + "8H"); // move cursor to row 8
                System.out.println("You won in " + (i + 1) + (i == 0 ? " move!" : " moves!"));
                return;
            }
        }
        System.out.print(CSI + "8H"); // move cursor to row 8
        System.out.println("The word is " + actualWord + ".");
    }

    /**
     * Determines the coloring of 5 letter blocks.
     * Precondition: the guess is valid.
     * 
     * @param actual the actual word
     * @param guess  the guess
     * @return a string of 5 characters indicating the coloring of each letter.
     *         G=Green, Y=Yellow, and R=Gray.
     */
    private static String detectWord(String actual, String guess) {
        String result = "";
        // detect green position
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == actual.charAt(i)) {
                result += "G";
            } else {
                result += "R";
            }
        }
        // detect yellow position
        for (int i = 0; i < 5; i++) {
            StringBuffer tempResult = new StringBuffer(result);
            StringBuffer tempActual = new StringBuffer(actual);
            // Use the grey position to find yellow position and replace it.
            if (result.charAt(i) == 'R') {
                for (int j = 0; j < 5; j++) {
                    if (guess.charAt(i) == actual.charAt(j) && result.charAt(j) != 'G') {
                        tempActual.setCharAt(j, '#');
                        actual = tempActual.toString();
                        tempResult.setCharAt(i, 'Y');
                        result = tempResult.toString();
                    }
                }
            }
        }
        return result;
    }
}