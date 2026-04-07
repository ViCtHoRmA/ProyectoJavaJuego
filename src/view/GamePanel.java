package view;

import javax.swing.JPanel;
import java.awt.*;

import controller.GameController;
import controller.KeyController;

public class GamePanel extends JPanel implements Runnable {


    public static final int ANCHO     = Principal.screenWidth;
    public static final int ALTO      = Principal.screenHeigth;
    public static final int FPS       = 60;
    public static final int SUELO     = 430; // coordenada Y del suelo


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
        double delta          = 0;
        long   tiempoAnterior = System.nanoTime();

        while (hiloJuego != null) {
            long tiempoActual = System.nanoTime();
            delta += (tiempoActual - tiempoAnterior) / tiempoPorFrame;
            tiempoAnterior = tiempoActual;

            if (delta >= 1) {
                actualizar();
                repaint();
                delta--;
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


