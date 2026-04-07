package model.entities;


import java.awt.*;

public class ZombieStudent extends Enemy{


    public ZombieStudent(int x, int y) {
        super(x, y, 38, 58, 100, 1, 180, 45, 8, 100);
    }

    @Override
    public void dibujar(Graphics2D g2d) {
        if (!vivo) return;
        // Piernas
        g2d.setColor(new Color(60, 90, 60));
        g2d.fillRoundRect(x + 4,          y + alto - 20, 12, 20, 4, 4);
        g2d.fillRoundRect(x + ancho - 16, y + alto - 20, 12, 20, 4, 4);

        // Torso
        g2d.setColor(new Color(50, 100, 50));
        g2d.fillRoundRect(x + 4, y + 20, ancho - 8, alto - 38, 6, 6);

        // Cabeza
        g2d.setColor(new Color(140, 190, 130));
        g2d.fillOval(x + 6, y - 8, ancho - 12, 28);

        // Ojos rojos
        g2d.setColor(new Color(220, 0, 0));
        if (miraDerecha) g2d.fillOval(x + 16, y - 2, 6, 6);
        else             g2d.fillOval(x + ancho - 22, y - 2, 6, 6);

        // Boca
        g2d.setColor(new Color(80, 20, 20));
        g2d.fillRect(x + 10, y + 12, ancho - 20, 4);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x + 12, y + 12, 4, 4);
        g2d.fillRect(x + 18, y + 12, 4, 4);

        // Brazo
        g2d.setColor(new Color(140, 190, 130));
        if (miraDerecha) g2d.fillRoundRect(x + ancho - 4, y + 22, 18, 10, 4, 4);
        else             g2d.fillRoundRect(x - 14,         y + 22, 18, 10, 4, 4);


        dibujarBarraVida(g2d);

        g2d.setColor(new Color(200, 255, 200));
        g2d.setFont(new Font("Arial", Font.PLAIN, 9));
        g2d.drawString("Estudiante", x, y - 16);
    }
}
