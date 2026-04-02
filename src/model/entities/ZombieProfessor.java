package model.entities;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class ZombieProfessor extends Enemy{

    public ZombieProfessor(int x, int y) {
        super(
                x, y,
                42, 65,   // ancho, alto — mas grande que el estudiante
                120,      // vida — mucho mas resistente
                3,        // velocidad — un poco mas rapido
                220,      // radioDeteccion — ve mas lejos
                55,       // radioAtaque — alcance mayor
                18,       // danio — golpea mucho mas fuerte
                300       // puntos al morir
        );
        this.intervaloAtaque = 70; // ataca mas seguido que el estudiante
    }


    @Override
    public void dibujar(Graphics2D g2d) {
        if (!vivo) return;

        // ── Cuerpo del zombie profesor ────────────────────────────────────────
        // Mas alto, con traje oscuro

        // Piernas con pantalon de traje
        g2d.setColor(new Color(40, 40, 80));
        g2d.fillRoundRect(x + 4,          y + alto - 22, 14, 22, 4, 4);
        g2d.fillRoundRect(x + ancho - 18, y + alto - 22, 14, 22, 4, 4);

        // Torso con traje oscuro
        g2d.setColor(new Color(30, 30, 70));
        g2d.fillRoundRect(x + 3, y + 22, ancho - 6, alto - 40, 6, 6);

        // Corbata roja (detalle del profesor)
        g2d.setColor(new Color(180, 0, 0));
        int[] corbataX = {x + ancho/2 - 3, x + ancho/2 + 3,
                x + ancho/2 + 5, x + ancho/2 - 5};
        int[] corbataY = {y + 24, y + 24, y + 45, y + 45};
        g2d.fillPolygon(corbataX, corbataY, 4);

        // Cabeza mas grande — tono zombie
        g2d.setColor(new Color(120, 170, 110));
        g2d.fillOval(x + 4, y - 10, ancho - 8, 32);

        // Anteojos del profesor
        g2d.setColor(new Color(60, 60, 60));
        g2d.drawOval(x + 8,          y - 4, 10, 8);
        g2d.drawOval(x + ancho - 18, y - 4, 10, 8);
        g2d.drawLine(x + 18, y,  x + ancho - 18, y); // puente de los lentes

        // Ojos rojos brillantes dentro de los lentes
        g2d.setColor(new Color(255, 0, 0));
        g2d.fillOval(x + 10,         y - 2, 6, 4);
        g2d.fillOval(x + ancho - 16, y - 2, 6, 4);

        // Maletín como arma
        g2d.setColor(new Color(100, 70, 20));
        if (miraDerecha) {
            g2d.fillRoundRect(x + ancho, y + 30, 20, 16, 4, 4);
            g2d.setColor(new Color(140, 100, 40));
            g2d.drawRoundRect(x + ancho, y + 30, 20, 16, 4, 4);
            // manija del maletin
            g2d.setColor(new Color(80, 50, 10));
            g2d.drawArc(x + ancho + 4, y + 25, 12, 10, 0, 180);
        } else {
            g2d.fillRoundRect(x - 20, y + 30, 20, 16, 4, 4);
            g2d.setColor(new Color(140, 100, 40));
            g2d.drawRoundRect(x - 20, y + 30, 20, 16, 4, 4);
            g2d.setColor(new Color(80, 50, 10));
            g2d.drawArc(x - 16, y + 25, 12, 10, 0, 180);
        }

        // Barra de vida
        dibujarBarraVida(g2d);

        // Etiqueta
        g2d.setColor(new Color(255, 200, 200));
        g2d.setFont(new Font("Arial", Font.BOLD, 9));
        g2d.drawString("Profesor", x, y - 16);
    }


}
