package model.entities;


import java.awt.*;


public class ZombieProfessor extends Enemy{



    public ZombieProfessor(int x, int y) {
        super(x, y, 42, 65, 200, 2, 220, 55, 18, 300);
        this.intervaloAtaque = 80;
    }


    @Override
    public void dibujar(Graphics2D g2d) {
        if (!vivo) return;


        // Piernas
        g2d.setColor(new Color(40, 40, 80));
        g2d.fillRoundRect(x + 4,          y + alto - 22, 14, 22, 4, 4);
        g2d.fillRoundRect(x + ancho - 18, y + alto - 22, 14, 22, 4, 4);

        // Torso
        g2d.setColor(new Color(30, 30, 70));
        g2d.fillRoundRect(x + 3, y + 22, ancho - 6, alto - 40, 6, 6);

        // Corbata
        g2d.setColor(new Color(180, 0, 0));
        int[] cx = {x+ancho/2-3, x+ancho/2+3, x+ancho/2+5, x+ancho/2-5};
        int[] cy = {y+24, y+24, y+45, y+45};
        g2d.fillPolygon(cx, cy, 4);

        // Cabeza
        g2d.setColor(new Color(120, 170, 110));
        g2d.fillOval(x + 4, y - 10, ancho - 8, 32);

        // Lentes
        g2d.setColor(new Color(60, 60, 60));
        g2d.drawOval(x + 8,          y - 4, 10, 8);
        g2d.drawOval(x + ancho - 18, y - 4, 10, 8);
        g2d.drawLine(x + 18, y, x + ancho - 18, y);

        // Ojos rojos
        g2d.setColor(new Color(255, 0, 0));
        g2d.fillOval(x + 10,         y - 2, 6, 4);
        g2d.fillOval(x + ancho - 16, y - 2, 6, 4);

        // Maletin
        g2d.setColor(new Color(100, 70, 20));
        if (miraDerecha) {
            g2d.fillRoundRect(x + ancho, y + 30, 20, 16, 4, 4);
            g2d.setColor(new Color(80, 50, 10));
            g2d.drawArc(x + ancho + 4, y + 25, 12, 10, 0, 180);
        } else {
            g2d.fillRoundRect(x - 20, y + 30, 20, 16, 4, 4);
            g2d.setColor(new Color(80, 50, 10));
            g2d.drawArc(x - 16, y + 25, 12, 10, 0, 180);
        }


        dibujarBarraVida(g2d);

        g2d.setColor(new Color(255, 200, 200));
        g2d.setFont(new Font("Arial", Font.BOLD, 9));
        g2d.drawString("Profesor", x, y - 16);
    }


}
