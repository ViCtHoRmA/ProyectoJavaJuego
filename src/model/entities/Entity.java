package model.entities;

import java.awt.*;

public abstract class Entity {

    public int x;
    public int y;
    public int ancho;
    public int alto;

    public int vidaMaxima;
    public int vidaActual;
    public int velocidad;
    public boolean miraDerecha = true;
    public boolean vivo = true;



    public Entity(int x, int y, int ancho, int alto, int vidaMaxima, int velocidad) {
        this.x          = x;
        this.y          = y;
        this.ancho      = ancho;
        this.alto       = alto;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima; // empieza con vida completa
        this.velocidad  = velocidad;
    }


    public abstract void actualizar();
    public abstract void dibujar(Graphics2D g2d);

    public void recibirDanio(int cantidad) {
        vidaActual -= cantidad;
        if (vidaActual <= 0) {
            vidaActual = 0;
            vivo = false;
        }
    }

    public boolean estaVivo() {
        return vivo;
    }
    public Rectangle getHitbox() {
        return new Rectangle(x, y, ancho, alto);
    }

    public void dibujarBarraVida(Graphics2D g2d) {
        int barraAncho = ancho;
        int barraAlto = 6;
        int barraX = x;
        int barraY = y - 12;

        g2d.setColor(new java.awt.Color(180, 0, 0));
        g2d.fillRect(barraX, barraY, barraAncho, barraAlto);


        double proporcion = (double) vidaActual / vidaMaxima;
        int verdeAncho = (int) (barraAncho * proporcion);

        g2d.setColor(new java.awt.Color(0, 200, 0));
        g2d.fillRect(barraX, barraY, verdeAncho, barraAlto);


        g2d.setColor(new java.awt.Color(0, 0, 0));
        g2d.drawRect(barraX, barraY, barraAncho, barraAlto);

    }


}
