package view;

import model.GameState;
import model.entities.Player;

import java.awt.*;


public class HUDPanel {


    private GameState gameState;
    private Player jugador;

    private final Font fuenteGrande = new Font("Arial Black", Font.BOLD, 14);
    private final Font fuenteNormal = new Font("Arial", Font.BOLD, 12);
    private final Font fuenteSmall = new Font("Arial", Font.BOLD, 10);

    private String mensajeTemporal = "";
    private int tiempoMensaje   = 0;
    private static final int DURACION_MENSAJE = 120;

    public HUDPanel(GameState gameState, Player jugador) {
        this.gameState = gameState;
        this.jugador = jugador;
    }


    // ── Metodo principal — dibuja todo el HUD ─────────────────────────────────
    // Se llama desde GameController en cada frame
    public void dibujar(Graphics2D g2d) {
        dibujarPanelVida(g2d);
        dibujarPanelPiezas(g2d);
        dibujarPanelPuntaje(g2d);
        dibujarNivelActual(g2d);

        if (tiempoMensaje > 0) {
            dibujarMensajeTemporal(g2d);
            tiempoMensaje--;
        }
    }


    private void dibujarPanelVida(Graphics2D g2d){
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRoundRect(10, 10, 200, 36, 10, 10);

        // Icono de corazon
        g2d.setColor(new Color(220, 50, 50));
        g2d.setFont(fuenteNormal);
        g2d.drawString("♥", 18, 32);

        // Etiqueta VIDA
        g2d.setColor(new Color(220, 220, 220));
        g2d.drawString("VIDA", 34, 32);

        // Fondo de la barra — rojo oscuro
        g2d.setColor(new Color(100, 0, 0));
        g2d.fillRoundRect(68, 18, 132, 14, 6, 6);

        // Barra de vida — color cambia segun cuanta vida queda
        double proporcion = (double) jugador.vidaActual / jugador.vidaMaxima;
        int    anchoVida  = (int)(132 * proporcion);

        Color colorVida;
        if (proporcion > 0.6) colorVida = new Color(0,   200, 0);
        else if (proporcion > 0.3) colorVida = new Color(255, 180, 0);
        else colorVida = new Color(255, 40,  40);


        g2d.setColor(colorVida);
        g2d.fillRoundRect(68, 18, anchoVida, 14, 6, 6);

        // Brillo encima de la barra
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.fillRoundRect(68, 18, anchoVida, 7, 6, 6);

        // Borde de la barra
        g2d.setColor(new Color(255, 255, 255, 60));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(68, 18, 132, 14, 6, 6);
        g2d.setStroke(new BasicStroke(1));

        // Numero de vida encima de la barra
        g2d.setColor(Color.WHITE);
        g2d.setFont(fuenteSmall);
        String vidaTexto = jugador.vidaActual + "/" + jugador.vidaMaxima;
        int    tw        = g2d.getFontMetrics().stringWidth(vidaTexto);
        g2d.drawString(vidaTexto, 68 + (132 - tw) / 2, 29);
    }

    // ── Panel de piezas ───────────────────────────────────────────────────────
    private void dibujarPanelPiezas(Graphics2D g2d) {

        // Fondo semitransparente
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRoundRect(10, 52, 200, 36, 10, 10);

        // Etiqueta
        g2d.setColor(new Color(255, 200, 0));
        g2d.setFont(fuenteNormal);
        g2d.drawString("PIEZAS:", 18, 74);

        // Cuadraditos de piezas — 5 en total
        for (int i = 0; i < 5; i++) {
            int px = 88 + (i * 24);
            int py = 58;

            if (i < jugador.piezasRecogidas) {
                // Pieza recogida — amarillo brillante con brillo
                g2d.setColor(new Color(255, 200, 0));
                g2d.fillRoundRect(px, py, 18, 18, 4, 4);
                g2d.setColor(new Color(255, 255, 150, 120));
                g2d.fillRoundRect(px, py, 18, 9, 4, 4);
                g2d.setColor(new Color(180, 120, 0));
                g2d.drawRoundRect(px, py, 18, 18, 4, 4);

                // Simbolo de engranaje
                g2d.setColor(new Color(120, 70, 0));
                g2d.setFont(fuenteSmall);
                g2d.drawString("✦", px + 3, py + 13);
            } else {
                // Pieza pendiente — gris oscuro
                g2d.setColor(new Color(50, 50, 50));
                g2d.fillRoundRect(px, py, 18, 18, 4, 4);
                g2d.setColor(new Color(80, 80, 80));
                g2d.drawRoundRect(px, py, 18, 18, 4, 4);
            }
        }
    }

    // ── Panel de puntaje ──────────────────────────────────────────────────────
    private void dibujarPanelPuntaje(Graphics2D g2d) {

        // Panel en la esquina superior derecha
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRoundRect(GamePanel.ANCHO - 150, 10, 140, 36, 10, 10);

        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(fuenteNormal);
        g2d.drawString("SCORE", GamePanel.ANCHO - 140, 27);

        g2d.setColor(Color.WHITE);
        g2d.setFont(fuenteGrande);
        String score = String.valueOf(gameState.getPuntaje());
        int    tw    = g2d.getFontMetrics().stringWidth(score);
        g2d.drawString(score, GamePanel.ANCHO - 20 - tw, 40);
    }

    // ── Indicador del nivel actual ────────────────────────────────────────────
    private void dibujarNivelActual(Graphics2D g2d) {

        // Panel centrado en la parte inferior de la pantalla
        g2d.setColor(new Color(0, 0, 0, 130));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - 60, GamePanel.ALTO - 30,
                120, 22, 8, 8);

        g2d.setColor(new Color(180, 180, 180));
        g2d.setFont(fuenteSmall);
        String nivelTexto = "Nivel " + gameState.getNivelActual() + " / 3";
        int    tw         = g2d.getFontMetrics().stringWidth(nivelTexto);
        g2d.drawString(nivelTexto,
                GamePanel.ANCHO / 2 - tw / 2,
                GamePanel.ALTO - 14);
    }

    // ── Mensaje temporal centrado en pantalla ─────────────────────────────────
    private void dibujarMensajeTemporal(Graphics2D g2d) {

        // Opacidad que se reduce suavemente al final
        float alpha = Math.min(1f, tiempoMensaje / 30f);
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, alpha));

        g2d.setFont(new Font("Arial Black", Font.BOLD, 20));
        int tw = g2d.getFontMetrics().stringWidth(mensajeTemporal);

        // Fondo del mensaje
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - tw / 2 - 16,
                120, tw + 32, 38, 12, 12);

        // Borde dorado
        g2d.setColor(new Color(255, 200, 0, 180));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(GamePanel.ANCHO / 2 - tw / 2 - 16,
                120, tw + 32, 38, 12, 12);
        g2d.setStroke(new BasicStroke(1));

        // Texto
        g2d.setColor(new Color(255, 220, 0));
        g2d.drawString(mensajeTemporal,
                GamePanel.ANCHO / 2 - tw / 2,
                146);

        // Restaurar opacidad completa
        g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1f));
    }

    public void mostrarMensaje(String mensaje) {
        mensajeTemporal = mensaje;
        tiempoMensaje   = DURACION_MENSAJE;
    }

    // ── Pantalla de pausa ─────────────────────────────────────────────────────
    public void dibujarPantallaPausa(Graphics2D g2d) {

        // Overlay oscuro
        g2d.setColor(new Color(0, 0, 0, 170));
        g2d.fillRect(0, 0, GamePanel.ANCHO, GamePanel.ALTO);

        // Panel central
        g2d.setColor(new Color(20, 20, 40, 220));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - 160,
                GamePanel.ALTO  / 2 - 80,
                320, 160, 16, 16);
        g2d.setColor(new Color(100, 100, 200, 150));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(GamePanel.ANCHO / 2 - 160,
                GamePanel.ALTO  / 2 - 80,
                320, 160, 16, 16);
        g2d.setStroke(new BasicStroke(1));

        // Titulo
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial Black", Font.BOLD, 42));
        int tw = g2d.getFontMetrics().stringWidth("PAUSADO");
        g2d.drawString("PAUSADO",
                GamePanel.ANCHO / 2 - tw / 2,
                GamePanel.ALTO  / 2 - 20);

        // Instruccion
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.setColor(new Color(180, 180, 200));
        int tw2 = g2d.getFontMetrics().stringWidth("Presiona ESC para continuar");
        g2d.drawString("Presiona ESC para continuar",
                GamePanel.ANCHO / 2 - tw2 / 2,
                GamePanel.ALTO  / 2 + 30);

        // Controles recordatorio
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(new Color(120, 120, 140));
        String controles = "A/D Mover  |  W Saltar  |  J Puño  |  K Patada";
        int    tw3       = g2d.getFontMetrics().stringWidth(controles);
        g2d.drawString(controles,
                GamePanel.ANCHO / 2 - tw3 / 2,
                GamePanel.ALTO  / 2 + 56);
    }


    // ── Pantalla Game Over ────────────────────────────────────────────────────
    public void dibujarPantallaGameOver(Graphics2D g2d) {

        g2d.setColor(new Color(0, 0, 0, 190));
        g2d.fillRect(0, 0, GamePanel.ANCHO, GamePanel.ALTO);

        // Panel central
        g2d.setColor(new Color(40, 0, 0, 220));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - 220,
                GamePanel.ALTO  / 2 - 110,
                440, 220, 16, 16);
        g2d.setColor(new Color(180, 0, 0, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(GamePanel.ANCHO / 2 - 220,
                GamePanel.ALTO  / 2 - 110,
                440, 220, 16, 16);
        g2d.setStroke(new BasicStroke(1));

        // Titulo
        g2d.setColor(new Color(220, 0, 0));
        g2d.setFont(new Font("Arial Black", Font.BOLD, 52));
        int tw = g2d.getFontMetrics().stringWidth("GAME OVER");
        g2d.drawString("GAME OVER",
                GamePanel.ANCHO / 2 - tw / 2,
                GamePanel.ALTO  / 2 - 45);

        // Historia
        g2d.setColor(new Color(200, 180, 180));
        g2d.setFont(new Font("Arial", Font.ITALIC, 14));
        String historia = "Los zombies te atraparon... la maquina queda incompleta.";
        int    tw2      = g2d.getFontMetrics().stringWidth(historia);
        g2d.drawString(historia,
                GamePanel.ANCHO / 2 - tw2 / 2,
                GamePanel.ALTO  / 2 + 5);

        // Puntaje
        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        String puntaje = "Puntaje final: " + gameState.getPuntaje();
        int    tw3     = g2d.getFontMetrics().stringWidth(puntaje);
        g2d.drawString(puntaje,
                GamePanel.ANCHO / 2 - tw3 / 2,
                GamePanel.ALTO  / 2 + 40);

        // Instruccion
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Arial", Font.PLAIN, 13));
        String inst = "Presiona ESC para volver a intentarlo";
        int    tw4  = g2d.getFontMetrics().stringWidth(inst);
        g2d.drawString(inst,
                GamePanel.ANCHO / 2 - tw4 / 2,
                GamePanel.ALTO  / 2 + 76);
    }

    // ── Pantalla Victoria ─────────────────────────────────────────────────────
    public void dibujarPantallaVictoria(Graphics2D g2d) {

        g2d.setColor(new Color(0, 0, 0, 190));
        g2d.fillRect(0, 0, GamePanel.ANCHO, GamePanel.ALTO);

        // Panel central
        g2d.setColor(new Color(0, 30, 0, 220));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - 240,
                GamePanel.ALTO  / 2 - 120,
                480, 240, 16, 16);
        g2d.setColor(new Color(0, 180, 0, 180));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(GamePanel.ANCHO / 2 - 240,
                GamePanel.ALTO  / 2 - 120,
                480, 240, 16, 16);
        g2d.setStroke(new BasicStroke(1));

        // Titulo
        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(new Font("Arial Black", Font.BOLD, 48));
        int tw = g2d.getFontMetrics().stringWidth("¡ESCAPASTE!");
        g2d.drawString("¡ESCAPASTE!",
                GamePanel.ANCHO / 2 - tw / 2,
                GamePanel.ALTO  / 2 - 55);

        // Historia
        g2d.setColor(new Color(180, 255, 180));
        g2d.setFont(new Font("Arial", Font.ITALIC, 14));
        String l1  = "Reparaste la maquina y volviste a tu dimension.";
        String l2  = "Los zombies quedaron atrapados para siempre.";
        int    tw2 = g2d.getFontMetrics().stringWidth(l1);
        int    tw3 = g2d.getFontMetrics().stringWidth(l2);
        g2d.drawString(l1, GamePanel.ANCHO / 2 - tw2 / 2,
                GamePanel.ALTO  / 2 - 10);
        g2d.drawString(l2, GamePanel.ANCHO / 2 - tw3 / 2,
                GamePanel.ALTO  / 2 + 15);

        // Puntaje
        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String puntaje = "Puntaje final: " + gameState.getPuntaje();
        int    tw4     = g2d.getFontMetrics().stringWidth(puntaje);
        g2d.drawString(puntaje,
                GamePanel.ANCHO / 2 - tw4 / 2,
                GamePanel.ALTO  / 2 + 52);

        // Instruccion
        g2d.setColor(new Color(150, 150, 150));
        g2d.setFont(new Font("Arial", Font.PLAIN, 13));
        String inst = "Presiona ESC para jugar de nuevo";
        int    tw5  = g2d.getFontMetrics().stringWidth(inst);
        g2d.drawString(inst,
                GamePanel.ANCHO / 2 - tw5 / 2,
                GamePanel.ALTO  / 2 + 88);
    }

}
