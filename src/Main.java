import view.Principal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Principal window = new Principal();
            window.setVisible(true);
        });
    }
}