package view;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class GameWindow extends JFrame {

    public static final int SCREEN_WIDTH = 900;
    public static final int SCREEN_HEIGHT = 700;
    public static final String GAME_TITLE = "Dimension UNET";

    public GameWindow() {
        initWindow();
    }

    public void initWindow() {
        setTitle(GAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        // Añadimos el panel del menú principal como primer panel visible
        MainMenuPanel menuPanel = new MainMenuPanel();
        add(menuPanel);
        pack();
    }
}