package view;

import javax.swing.JFrame;
import java.awt.*;

public class Principal extends JFrame {

    public static final int screenWidth = 900;
    public static final int screenHeigth = 700;
    public static final String gameTitle = "Dimension UNET";


    private CardLayout cardLayout;
    private Container contenedor;
    private GamePanel gamePanel;
    private PrincipalMenu principalMenu;


    public Principal() {
        setTitle(gameTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout  = new CardLayout();
        contenedor = getContentPane();
        contenedor.setLayout(cardLayout);


        principalMenu = new PrincipalMenu(this);
        gamePanel = new GamePanel();

        contenedor.add(principalMenu, "Menu");
        contenedor.add(gamePanel, "Juego");

        // Mostrar el menú primero
        cardLayout.show(contenedor, "Menu");

        pack();
        setLocationRelativeTo(null);
    }

    public void iniciarJuego() {
        cardLayout.show(contenedor, "Juego");
        gamePanel.requestFocusInWindow();
        gamePanel.iniciarJuego();
    }
}