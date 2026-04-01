package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import view.GamePanel;

import javax.imageio.ImageIO;

public class GameController {

    private GamePanel    panel;
    private KeyController keyController;

    private BufferedImage imagenFondo;

    private boolean jugando  = true;
    private boolean pausado  = false;

    public GameController(GamePanel panel, KeyController keyController) {
        this.panel = panel;
        this.keyController = keyController;

        cargarImagenes();
    }


    private void cargarImagenes() {
        try {
            imagenFondo = ImageIO.read(
                    Objects.requireNonNull(
                            getClass().getClassLoader().getResourceAsStream("fondo.jpeg"),
                            "No se encontró fondo.jpg en resources"
                    )
            );
        } catch (IOException e) {
            System.err.println("Error al cargar fondo.jpg: " + e.getMessage());
        }
    }


    public void actualizar() {
        if (pausado) return;


        if (keyController.pausa) {
            pausado = true;
        }

        if (!keyController.pausa) pausado = false;


    }


    public void dibujar(Graphics2D g2d) {
        dibujarFondo(g2d);


        if (pausado) dibujarPausa(g2d);
    }




    private void dibujarFondo(Graphics2D g2d) {

        g2d.drawImage(imagenFondo, 0, 0, GamePanel.ANCHO, GamePanel.ALTO, null);

    }





    private void dibujarPausa(Graphics2D g2d) {

        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(0, 0, GamePanel.ANCHO, GamePanel.ALTO);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial Black", Font.BOLD, 48));
        g2d.drawString("PAUSADO", 280, 280);

        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.drawString("Presiona ESC para continuar", 260, 330);

        if (!keyController.pausa) pausado = false;
    }

}
