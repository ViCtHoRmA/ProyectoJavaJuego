package view;

import model.GameState;
import model.entities.Player;
import java.awt.*;


public class HUDPanel {


    private GameState gameState;
    private Player jugador;

    private final Font fuenteGrande = new Font("Arial Black", Font.BOLD, 14);
    private final Font fuenteSmall = new Font("Arial", Font.BOLD, 10);

    private String mensajeTemporal = "";
    private int tiempoMensaje   = 0;
    private static final int DURACION_MENSAJE = 120;

    public HUDPanel(GameState gameState, Player jugador) {
        this.gameState = gameState;
        this.jugador = jugador;
    }


    public void dibujar(Graphics2D g2d) {
        dibujarPanelVida(g2d);
        dibujarPanelPiezas(g2d);
        dibujarPanelPuntaje(g2d);

        if (tiempoMensaje > 0) {
            dibujarMensajeTemporal(g2d);
            tiempoMensaje--;
        }
    }


    private void dibujarPanelVida(Graphics2D g2d){

        g2d.setColor(new Color(220, 220, 220));
        g2d.drawString("VIDA", 30, 32);

        g2d.setColor(new Color(100, 0, 0));
        g2d.fillRoundRect(68, 18, 132, 14, 6, 6);

        double proporcion = (double) jugador.vidaActual / jugador.vidaMaxima;
        int anchoVida  = (int)(132 * proporcion);
        Color colorVida;
        colorVida = new Color(0,200, 0);


        g2d.setColor(colorVida);
        g2d.fillRoundRect(68, 18, anchoVida, 14, 6, 6);

        g2d.setColor(Color.WHITE);
        g2d.setFont(fuenteSmall);
        String vidaTexto = jugador.vidaActual + "/" + jugador.vidaMaxima;
        int textoVida = g2d.getFontMetrics().stringWidth(vidaTexto);
        g2d.drawString(vidaTexto, 68 + (132 - textoVida) / 2, 29);
    }

    private void dibujarPanelPiezas(Graphics2D g2d) {

        g2d.setColor(new Color(255, 200, 0));
        g2d.setFont(fuenteGrande);
        g2d.drawString("piezas: ", 18, 74);

        for (int i = 0; i < 5; i++) {
            int piezasx = 88 + (i * 24);
            int piezasy = 58;

            if (i < jugador.piezasRecogidas) {
                g2d.setColor(new Color(255, 200, 0));
                g2d.fillRoundRect(piezasx, piezasy, 18, 18, 4, 4);
            } else {
                g2d.setColor(new Color(50, 50, 50));
                g2d.fillRoundRect(piezasx, piezasy, 18, 18, 4, 4);

            }
        }
    }


    private void dibujarPanelPuntaje(Graphics2D g2d) {

        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(fuenteGrande);
        g2d.drawString("puntaje: ", GamePanel.ANCHO - 140, 27);

        g2d.setColor(Color.WHITE);
        g2d.setFont(fuenteGrande);
        String puntaje = String.valueOf(gameState.getPuntaje());
        int puntos = g2d.getFontMetrics().stringWidth(puntaje);
        g2d.drawString(puntaje, GamePanel.ANCHO - 20 - puntos, 40);
    }

    private void dibujarMensajeTemporal(Graphics2D g2d) {

        float alpha = Math.min(1f, tiempoMensaje / 30f);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g2d.setFont(new Font("Arial Black", Font.BOLD, 20));
        int mensaje = g2d.getFontMetrics().stringWidth(mensajeTemporal);


        g2d.setColor(new Color(255, 220, 0));
        g2d.drawString(mensajeTemporal, GamePanel.ANCHO / 2 - mensaje / 2, 146);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    public void mostrarMensaje(String mensaje) {
        mensajeTemporal = mensaje;
        tiempoMensaje = DURACION_MENSAJE;
    }


    public void dibujarPantallaPausa(Graphics2D g2d) {

        g2d.setColor(new Color(20, 20, 40, 220));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - 160, GamePanel.ALTO  / 2 - 80, 320, 160, 16, 16);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial Black", Font.BOLD, 42));
        int pausa = g2d.getFontMetrics().stringWidth("PAUSA");
        g2d.drawString("PAUSA", GamePanel.ANCHO / 2 - pausa / 2, GamePanel.ALTO  / 2);


    }



    public void dibujarPantallaGameOver(Graphics2D g2d) {

        g2d.setColor(new Color(40, 0, 0, 220));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - 220, GamePanel.ALTO  / 2 - 110, 440, 220, 16, 16);

        g2d.setColor(new Color(220, 0, 0));
        g2d.setFont(new Font("Arial Black", Font.BOLD, 52));
        int perdio = g2d.getFontMetrics().stringWidth("PERDISTE");
        g2d.drawString("PERDISTE", GamePanel.ANCHO / 2 - perdio / 2, GamePanel.ALTO  / 2 - 45);

        g2d.setColor(new Color(200, 180, 180));
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String mensajePerdiste = "Los zombies te atraparon, Candy estara triste porque no regresaste.";
        int perdioMensaje = g2d.getFontMetrics().stringWidth(mensajePerdiste);
        g2d.drawString(mensajePerdiste, GamePanel.ANCHO / 2 - perdioMensaje / 2, GamePanel.ALTO  / 2 + 5);

        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        String puntaje = "Puntaje: " + gameState.getPuntaje();
        int perdioPuntaje = g2d.getFontMetrics().stringWidth(puntaje);
        g2d.drawString(puntaje, GamePanel.ANCHO / 2 - perdioPuntaje / 2, GamePanel.ALTO  / 2 + 40);


        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Arial", Font.PLAIN, 13));
        String inst = "Presiona ESC para jugar de nuevo";
        int perdioDeNuevo = g2d.getFontMetrics().stringWidth(inst);
        g2d.drawString(inst, GamePanel.ANCHO / 2 - perdioDeNuevo / 2, GamePanel.ALTO  / 2 + 76);
    }


    public void dibujarPantallaVictoria(Graphics2D g2d) {

        g2d.setColor(new Color(0, 30, 0, 220));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - 240, GamePanel.ALTO  / 2 - 120, 480, 240, 16, 16);

        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(new Font("Arial Black", Font.BOLD, 48));
        int gano = g2d.getFontMetrics().stringWidth("Ganaste");
        g2d.drawString("Ganaste", GamePanel.ANCHO / 2 - gano / 2, GamePanel.ALTO  / 2 - 55);

        g2d.setColor(new Color(180, 255, 180));
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        String l1  = "Conseguiste las piezas y reparaste la maquina.";
        String l2  = "Candy estara orgullosa de ti.";
        int ganol1 = g2d.getFontMetrics().stringWidth(l1);
        int ganol2 = g2d.getFontMetrics().stringWidth(l2);
        g2d.drawString(l1, GamePanel.ANCHO / 2 - ganol1 / 2, GamePanel.ALTO  / 2 - 10);
        g2d.drawString(l2, GamePanel.ANCHO / 2 - ganol2 / 2, GamePanel.ALTO  / 2 + 15);

        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String puntaje = "Puntaje final: " + gameState.getPuntaje();
        int ganoPuntaje = g2d.getFontMetrics().stringWidth(puntaje);
        g2d.drawString(puntaje, GamePanel.ANCHO / 2 - ganoPuntaje / 2, GamePanel.ALTO  / 2 + 52);

        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Arial", Font.PLAIN, 13));
        String inst = "Presiona ESC para jugar de nuevo";
        int ganoDeNuevo = g2d.getFontMetrics().stringWidth(inst);
        g2d.drawString(inst, GamePanel.ANCHO / 2 - ganoDeNuevo / 2, GamePanel.ALTO  / 2 + 88);
    }

}
