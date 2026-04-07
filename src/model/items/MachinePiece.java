package model.items;

import java.awt.*;


public class MachinePiece {

    public int x, y;
    public boolean recogida = false;
    private int numero;   // del 1 al 5

    // Animacion de flotacion
    private float offsetY = 0;  // desplazamiento vertical actual
    private float tiempoAnim = 0;  // contador que avanza cada frame

    public MachinePiece(int x, int y, int numero) {
        this.x = x;
        this.y = y;
        this.numero = numero;
    }

    // Se llama 60 veces por segundo para animar la pieza
    public void actualizar() {
        if (recogida) return;

        // La pieza flota: sube y baja suavemente usando seno
        // Math.sin devuelve valores entre -1 y 1 que usamos como offset
        tiempoAnim += 0.05f;
        offsetY = (float) Math.sin(tiempoAnim) * 5; // oscila 5px arriba y abajo
    }

    public void dibujar(Graphics2D g2d) {
        if (recogida) return;

        // La posicion real incluye el offset de flotacion
        int posY = (int) (y + offsetY);

        // Sombra debajo de la pieza
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillOval(x + 2, y + 32, 26, 6);

        // Fondo amarillo brillante — engranaje
        g2d.setColor(new Color(255, 200, 0));
        g2d.fillRoundRect(x, posY, 30, 30, 8, 8);

        // Borde dorado
        g2d.setColor(new Color(200, 140, 0));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x, posY, 30, 30, 8, 8);
        g2d.setStroke(new BasicStroke(1)); // restaurar el grosor

        // Simbolo de engranaje (circulo central + numero)
        g2d.setColor(new Color(120, 80, 0));
        g2d.fillOval(x + 8, posY + 8, 14, 14);
        g2d.setColor(new Color(255, 220, 100));
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.drawString(String.valueOf(numero), x + 12, posY + 20);

        // Brillo en la esquina superior
        g2d.setColor(new Color(255, 255, 200, 180));
        g2d.fillOval(x + 4, posY + 4, 8, 6);
    }


    // Hitbox para detectar cuando el jugador la toca
    public Rectangle getHitbox() {
        return new Rectangle(x, y, 30, 30);
    }

    public int getNumero() {
        return numero;
    }

}
