import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.IOException;

import static java.util.Objects.*;


public final class CalcFrame implements KeyEventDispatcher, ActionListener {

    /**
     * References all operations possible and a NONE state when no operation is currently selected
     */
    private enum Operand {
        PLUS,
        MINUS,
        MULTIPLY,
        DIVIDE,
        NONE
    }

    /**
     * Constants for display
     */
    private static final int JFRAME_WIDTH = 440;
    private static final int JFRAME_HEIGHT = 550;

    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 50;
    /*
     * End constants
     */

    /**
     * Logical memory state
     */

    /**
     * Left side number
     */
    private String memoryA = null;

    /**
     * Right side number
     */
    private String memoryB = null;

    /**
     * Currently selected operation
     */
    private Operand currentOperand = null;

    /**
     * The text inside the display
     */
    private String displayBuffer = "";

    /**
     * In memory variable holding the current user input
     */
    private String inputBuffer = "";
    /*
     * end logical memory state
     */

    /**
     * UI component
     */

    /**
     * Calculator screen
     */
    private final JTextField display = new JTextField();

    /**
     * Button 1
     */
    private final JButton one = new JButton();

    /**
     * Button 2
     */
    private final JButton two = new JButton();

    /**
     * Button 3
     */
    private final JButton three = new JButton();

    /**
     * Button +
     */
    private final JButton plus = new JButton();

    /**
     * Button 4
     */
    private final JButton four = new JButton();

    /**
     * Button 5
     */
    private final JButton five = new JButton();

    /**
     * Button 6
     */
    private final JButton six = new JButton();

    /**
     * Button -
     */
    private final JButton minus = new JButton();

    /**
     * Button 7
     */
    private final JButton seven = new JButton();

    /**
     * Button 8
     */
    private final JButton eight = new JButton();

    /**
     * Button (Knights of the) 9
     */
    private final JButton nine = new JButton();

    /**
     * Button *
     */
    private final JButton multiply = new JButton();

    /**
     * Button 0
     */
    private final JButton zero = new JButton();

    /**
     * Button .
     *
     * Yes, it's called coma because I used a coma at the beginning before
     * falling back to a dot
     */
    private final JButton coma = new JButton();

    /**
     * Button AC
     */
    private final JButton reset = new JButton();

    /**
     * Button /
     */
    private final JButton divide = new JButton();

    /**
     * Button =
     */
    private final JButton equals = new JButton();
    /*
     * End graphical objects
     */

    /**
     * Fun
     */
    private final Font jetbrainsMonoResized;
    /**
     * End fun
     */

    /**
     * Constructor for the UI
     */
    public CalcFrame() {
        // We want to use a custom font, so we have to load it from the file
        // Since java load custom font with size point 1
        // We need a new object to hold the resized font
        try {

            // Font
            Font jetbrainsMono = Font.createFont(Font.TRUETYPE_FONT, requireNonNull(getClass().getResourceAsStream("JetBrainsMono-Bold.ttf")));
            jetbrainsMonoResized = jetbrainsMono.deriveFont(Font.BOLD, 30f);

        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        // We used the KeyboardFocusManager to handle all the KeyboardEvent without having to worry about
        // which element have the focus
        KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        keyboardFocusManager.addKeyEventDispatcher(this);

        /*
         * Graphical objects
         */
        JPanel mainPanel = new JPanel();
        BorderLayout mainBorderLayout = new BorderLayout(0, 5);
        mainPanel.setLayout(mainBorderLayout);
        // Display
        mainPanel.add(display, BorderLayout.NORTH);
        // Keyboard
        JPanel centralPanel = new JPanel();
        mainPanel.add(centralPanel, BorderLayout.CENTER);
        // equals button
        JPanel bottomPanel = new JPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        JFrame frame = new JFrame();
        frame.setTitle("JCalculatrice");
        frame.setSize(JFRAME_WIDTH, JFRAME_HEIGHT);
        // Center of screen
        frame.setLocationRelativeTo(null);

        // Prevent resizing
        frame.setResizable(false);

        // Make sure the process end with the window closing
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(mainPanel);

        // BORDER TOP
        display.setBackground(Color.yellow);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setSize(JFRAME_WIDTH, 100);
        display.setFont(jetbrainsMonoResized);

        // Prevent user from messing with the field
        display.setEditable(false);

        // For the copy/paste
        display.addActionListener(this);
        display.setText("0");


        // CENTRAL BORDER
        GridLayout centralGridLayout = new GridLayout(4, 4);
        centralPanel.setLayout(centralGridLayout);
        one.setText("1");
        styleButton(one, centralPanel);

        two.setText("2");
        styleButton(two, centralPanel);

        three.setText("3");
        styleButton(three, centralPanel);

        plus.setText("+");
        styleButton(plus, centralPanel);

        four.setText("4");
        styleButton(four, centralPanel);

        five.setText("5");
        styleButton(five, centralPanel);

        six.setText("6");
        styleButton(six, centralPanel);

        minus.setText("-");
        styleButton(minus, centralPanel);

        seven.setText("7");
        styleButton(seven, centralPanel);

        eight.setText("8");
        styleButton(eight, centralPanel);

        nine.setText("9");
        styleButton(nine, centralPanel);

        multiply.setText("*");
        styleButton(multiply, centralPanel);

        zero.setText("0");
        styleButton(zero, centralPanel);

        coma.setText(",");
        styleButton(coma, centralPanel);

        reset.setText("AC");
        styleButton(reset, centralPanel);

        divide.setText("/");
        styleButton(divide, centralPanel);


        // BOTTOM BORDER
        equals.setText("=");
        equals.setSize(JFRAME_WIDTH, BUTTON_HEIGHT);
        equals.addActionListener(this);
        equals.setFont(jetbrainsMonoResized);

        frame.add(equals, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Style a button with a given with and height
     * Center the content
     * Add an action listener
     * Add the button to a panel
     *
     * @param button to style
     * @param panel  in which the button will be added
     */
    private void styleButton(JButton button, JPanel panel) {
        button.setFont(jetbrainsMonoResized);
        button.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.addActionListener(this);
        panel.add(button);
    }

    /**
     * Handle mouse click to the UI
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        if (source == display) {
            // copy to clipboard
            StringSelection selection = new StringSelection(display.getText());
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Clipboard clipboard = toolkit.getSystemClipboard();
            clipboard.setContents(selection, selection);
        } else if (source == one) {
            handleNumericCLick("1");
        } else if (source == two) {
            handleNumericCLick("2");
        } else if (source == three) {
            handleNumericCLick("3");
        } else if (source == four) {
            handleNumericCLick("4");
        } else if (source == five) {
            handleNumericCLick("5");
        } else if (source == six) {
            handleNumericCLick("6");
        } else if (source == seven) {
            handleNumericCLick("7");
        } else if (source == eight) {
            handleNumericCLick("8");
        } else if (source == nine) {
            handleNumericCLick("9");
        } else if (source == zero) {
            handleNumericCLick("0");
        } else if (source == coma) {
            handleNumericCLick(".");
        } else if (source == reset) {
            flushMemory();
        } else if (source == minus || source == multiply || source == plus || source == divide || source == equals) {
            handleOperandClick((JButton) source);
        }

    }


    /**
     * Give the possibility to use the keyboard some keys doesn't work
     *
     * @param e the KeyEvent to dispatch
     * @return a boolean to indicate if the event should go to children
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {

        if (e.getID() == KeyEvent.KEY_RELEASED) {
                switch (e.getKeyChar()) {
                    case '1' -> one.doClick(50);
                    case '2' -> two.doClick(50);
                    case '3' -> three.doClick(50);
                    case '4' -> four.doClick(50);
                    case '5' -> five.doClick(50);
                    case '6' -> six.doClick(50);
                    case '7' -> seven.doClick(50);
                    case '8' -> eight.doClick(50);
                    case '9' -> nine.doClick(50);
                    case '0' -> zero.doClick(50);
                    case '+' -> plus.doClick(50);
                    case '-' -> minus.doClick(50);
                    case '*' -> multiply.doClick(50);
                    case '/' -> divide.doClick(50);
                    case ',', '.' -> coma.doClick(50);
                    case KeyEvent.VK_ENTER, KeyEvent.VK_EQUALS -> equals.doClick(50);
                    case KeyEvent.VK_ESCAPE -> reset.doClick(50);
                    case KeyEvent.VK_DELETE, KeyEvent.VK_UNDO, KeyEvent.VK_BACK_SPACE -> {
                        if (!inputBuffer.isBlank() && !inputBuffer.isEmpty() && inputBuffer.length() - 1 >= 0) {
                            displayBuffer = displayBuffer.substring(0, displayBuffer.length() - 1);
                            inputBuffer = inputBuffer.substring(0, inputBuffer.length() - 1);
                            updateDisplay();
                        }
                    }
                    case KeyEvent.VK_UNDEFINED -> System.out.println("UNDEF : " + e.getExtendedKeyCode() + " " + e.getKeyChar());
                    default -> System.out.println(e.getExtendedKeyCode() + " " + e.getKeyChar());
                }
        }
            return true;
    }

    /**
     * Handle a click on a numeric button
     * OR on the minus button in a case where it can be interpreted as the marker of negativity
     * @param str symbol to display
     */
    private void handleNumericCLick(String str) {

        // Prevent from starting with a .
        // Also prevent to write -.
        if (str.equals(".")) {
            if (!inputBuffer.isBlank()
                    && !inputBuffer.isEmpty()
                    && inputBuffer.charAt(inputBuffer.length() - 1) != '-'
                    && !inputBuffer.contains(".")) {
                inputBuffer = inputBuffer + str;
                displayBuffer = displayBuffer + str;
                updateDisplay();
            }
        } else {
            inputBuffer = inputBuffer + str;
            displayBuffer = displayBuffer + str;
            updateDisplay();
        }

    }

    /**
     * Handle the click on a "Operand button"
     * which include + - * / =
     *
     * it's ... messy
     * @param source - The button
     */
    private void handleOperandClick(JButton source) {

        // If every buffer and memory is empty prevent to write anything apart -
        if((inputBuffer.isEmpty() || inputBuffer.isBlank()) && memoryA == null) {
            if(source == minus) {
                handleNumericCLick("-");
            }
        } else {
            // Define the operand depending on the source
            if(source == plus) {
                currentOperand = Operand.PLUS;
            } else if (source == minus) {
                // Prevent to override a previously selected operand with minus
                // in case we want to have x operand -y
                if(!inputBuffer.isEmpty() || !inputBuffer.isBlank()) {
                    currentOperand = Operand.MINUS;
                }
            } else if (source == multiply) {
                currentOperand = Operand.MULTIPLY;
            } else if (source == divide) {
                currentOperand = Operand.DIVIDE;
            } else if (source == equals) {
                // Pressing equals if nothing have been typed will end nowhere
                if(memoryA == null || inputBuffer.isBlank() || inputBuffer.isEmpty()) {
                    return;

                    // On a operand click perform the current operation
                    // display the result and use current source as operand for the next operation
                    // if a number already exists as left number
                    // and the input buffer isn't empty
                    // and an operand is set
                } else if (memoryA != null
                        && !inputBuffer.isBlank()
                        && !inputBuffer.isEmpty()
                        && currentOperand != Operand.NONE) {

                    // Get the buffer into right number
                    memoryB = inputBuffer;

                    // Compute
                    double result = computeResult();

                    // Dsiplay result
                    displayBuffer = String.valueOf(result);
                    updateDisplay();
                    return;
                    //Edge case where memoryA is and someone try to click =
                 } else if(memoryB == null && inputBuffer.isBlank() && inputBuffer.isEmpty()) {
                    return;
                }
            }

            // if memoryA is empty that mean either AC has been use
            // or we're in the first operation
            if(memoryA == null) {
                // push number into left number
                memoryA = inputBuffer;

                // clear buffer
                inputBuffer = "";

                // update display
                displayBuffer = displayBuffer + source.getText();
                updateDisplay();

                // If memory is set and we click an operand and the input buffer isn't empty
                // meaning the display is memoryA operand xxxxx
            } else if (memoryB == null && !inputBuffer.isEmpty() && !inputBuffer.isBlank()) {

                // push into memory
                memoryB = inputBuffer;

                // compute
                double result = computeResult();

                // display result and the operand symbol
                displayBuffer = result + source.getText();
                updateDisplay();
            } else if(memoryB == null && inputBuffer.isEmpty() && inputBuffer.isBlank()) {
                // in case we want to use a negative number for memoryB
                // allow only the - symbol
                if(source == minus) {
                    handleNumericCLick("-");
                } else {
                    displayBuffer = displayBuffer + source.getText();
                    updateDisplay();
                }
                } else {
                memoryB = inputBuffer;

                double result = computeResult();

                displayBuffer = String.valueOf(result);
                updateDisplay();

            }
        }

    }

    /**
     * Compute the result of memoryA operand memoryB
     * Both value as casted as double
     *
     * Result is set as memoryA acting a new base for the next operation
     * Reset memoryB and the inputbuffer
     *
     * @return result
     */
    private double computeResult() {
        double a = Double.parseDouble(memoryA.replace(',', '.')),
                b = Double.parseDouble(memoryB.replace(',', '.')),
                result  = switch (currentOperand) {
                    case PLUS -> a + b;
                    case MINUS -> a - b;
                    case MULTIPLY -> a * b;
                    case DIVIDE -> a / b;
                    case NONE -> 0;
                };

        memoryA = String.valueOf(result);
        memoryB = null;
        inputBuffer = "";
        currentOperand = Operand.NONE;

        return result;
    }

    /**
     * Reset the display when pressing AC button
     * Reset also the memory and buffers
     */
    private void flushMemory() {
        inputBuffer = "";
        displayBuffer = "";
        memoryA = null;
        memoryB = null;
        updateDisplay();
    }

    /**
     *
     * Update the display with the displayBuffer
     *
     */
    private void updateDisplay() {
        display.setText(displayBuffer);
    }


}
