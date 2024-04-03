public class LetterBlock {
    private static final String CSI = "" + (char) (0x1B) + "[";
    private char character;
    private final int row, column; // 0 = first
    private BoxColor color;

    /**
     * An enum that represents the background color of the letter block. The
     * possible values are <code>BoxColor.GRAY</code>, <code>BoxColor.YELLOW</code>,
     * and <code>BoxColor.GREEN</code>.
     */
    public enum BoxColor {
        GRAY(100),
        YELLOW(103),
        GREEN(42);

        private final int colorCode;

        private BoxColor(int colorCode) {
            this.colorCode = colorCode;
        }

        public int getColorCode() {
            return colorCode;
        }

        public static BoxColor getBoxColor(char color) {
            switch (color) {
                case 'G':
                    return BoxColor.GREEN;
                case 'Y':
                    return BoxColor.YELLOW;
                case 'R':
                    return BoxColor.GRAY;
                default:
                    return null;
            }
        }
    }

    /**
     * Creates a new letter block positioned at the specified row and column
     * 
     * @param row    row number, starting from 0
     * @param column column number, starting from 0
     */
    public LetterBlock(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Initializes the terminal screen. Call this method before using the
     * <code>LetterBlock</code> class.
     */
    public static void initLetterBlocks() {
        System.out.print(CSI + "2J"); // clear screen
        System.out.print(CSI + "1;1H"); // move the cursor to the top left corner
        System.out.print(CSI + "0m"); // clear formatting
    }

    /**
     * Sets the background color of the letter block.
     * 
     * @param color a <code>BoxColor</code> value of the background
     * @see BoxColor
     */
    public void setColor(BoxColor color) {
        this.color = color;
    }

    /**
     * Sets the character in the letter box.
     * 
     * @param character
     */
    public void setCharacter(char character) {
        this.character = character;
    }

    public void showBox() {
        System.out.print(CSI + (row + 1) + ";" + (column + 1) + "H"); // move the cursor to (row, column)
        System.out.print(CSI + "37;" + color.getColorCode() + "m"); // set foreground color to white and background
                                                                    // color to color.getColorCode()
        System.out.print(character);
        System.out.print(CSI + "0m"); // clear formatting
    }
}
