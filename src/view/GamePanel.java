package view;

import javax.swing.JPanel;
import java.awt.*;

import controller.GameController;
import controller.KeyController;

public class GamePanel extends JPanel implements Runnable {


    public static final int ANCHO = Principal.screenWidth;
    public static final int ALTO = Principal.screenHeigth;
    public static final int FPS = 60;
    public static final int SUELO_NIVEL1 = 550;
    public static final int SUELO_NIVEL2 = 660;
    public static final int SUELO_NIVEL3 = 480;


    private Thread hiloJuego;
    private GameController gameController;
    private KeyController keyController;

    public GamePanel() {
        setPreferredSize(new Dimension(ANCHO, ALTO));
        setBackground(Color.BLACK);
        setFocusable(true);
        keyController = new KeyController();
        addKeyListener(keyController);

        gameController = new GameController(this, keyController);
    }

    public void iniciarJuego() {
        hiloJuego = new Thread(this);
        hiloJuego.start();
    }



    @Override
    public void run() {

        double tiempoPorFrame = 1_000_000_000.0 / FPS;
        double time = 0;
        long tiempoAnterior = System.nanoTime();

        while (hiloJuego != null) {
            long tiempoActual = System.nanoTime();
            time += (tiempoActual - tiempoAnterior) / tiempoPorFrame;
            tiempoAnterior = tiempoActual;

            if (time >= 1) {
                actualizar();
                repaint();
                time--;
            }
        }
    }


    private void actualizar() {
        gameController.actualizar();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        gameController.dibujar(g2d);

        g2d.dispose();
    }
}


