package model.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class ZombieStudent extends Enemy{

    public ZombieStudent(int x, int y) {
        super(
                x, y,
                38, 58,   // ancho, alto
                50,       // vida — es el mas debil
                2,        // velocidad — lento
                180,      // radioDeteccion — ve al jugador a 180px
                45,       // radioAtaque — golpea a 45px
                8,        // danio — hace poco daño
                100       // puntos al morir
        );
    }

    @Override
    public void dibujar(Graphics2D g2d) {
        if (!vivo) return;

        // ── Cuerpo del zombie estudiante ──────────────────────────────────────
        // Color verde palido — tipico zombie

        // Piernas
        g2d.setColor(new Color(60, 90, 60));
        g2d.fillRoundRect(x + 4,           y + alto - 20, 12, 20, 4, 4);
        g2d.fillRoundRect(x + ancho - 16,  y + alto - 20, 12, 20, 4, 4);

        // Torso (ropa de estudiante — camisa verde oscuro)
        g2d.setColor(new Color(50, 100, 50));
        g2d.fillRoundRect(x + 4, y + 20, ancho - 8, alto - 38, 6, 6);

        // Cabeza
        g2d.setColor(new Color(140, 190, 130)); // verde grisaceo
        g2d.fillOval(x + 6, y - 8, ancho - 12, 28);

        // Ojos rojos de zombie
        g2d.setColor(new Color(220, 0, 0));
        if (miraDerecha) {
            g2d.fillOval(x + 16, y - 2, 6, 6);
        } else {
            g2d.fillOval(x + ancho - 22, y - 2, 6, 6);
        }

        // Boca — expresion de zombie con dientes
        g2d.setColor(new Color(80, 20, 20));
        g2d.fillRect(x + 10, y + 12, ancho - 20, 4);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x + 12, y + 12, 4, 4);
        g2d.fillRect(x + 18, y + 12, 4, 4);

        // Brazo extendido (postura clasica de zombie)
        g2d.setColor(new Color(140, 190, 130));
        if (miraDerecha) {
            g2d.fillRoundRect(x + ancho - 4, y + 22, 18, 10, 4, 4);
        } else {
            g2d.fillRoundRect(x - 14, y + 22, 18, 10, 4, 4);
        }

        // Barra de vida
        dibujarBarraVida(g2d);

        // Etiqueta encima del enemigo
        g2d.setColor(new Color(200, 255, 200));
        g2d.setFont(new Font("Arial", Font.PLAIN, 9));
        g2d.drawString("Estudiante", x, y - 16);
    }
}
