import javax.swing.*;
import java.util.Random;

/**
 * Main class
 */
public final class Main {

    /**
     * Program entry point
     *
     * @param args
     */
    public static void main(String[] args) {

        /*
            For fun we will try to find and use a random LookAndFeel at each startup
         */
        UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
        int random = new Random().nextInt(looks.length);

        try {
            UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[random].getClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Launching the UI
        final CalcFrame mainFrame = new CalcFrame();

    }
}