package model.entities;

import view.AnimacionSprite;
import view.GestorSprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class ZombieStudent extends Enemy{

    // Cada enemigo tiene su propia animacion independiente
    private AnimacionSprite sprite = new AnimacionSprite(
            "sprites/zombie_estudiante.png",
            84, 118, 5, 5, 10
    );

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


        // Elegir animacion segun el estado de la IA
        switch (estadoIA) {
            case "persiguiendo" -> sprite.setAnimacion(2, 4); // walk
            case "atacando"     -> sprite.setAnimacion(3, 4); // attack
            default             -> sprite.setAnimacion(0, 5); // idle
        }

        sprite.actualizar();

        int spriteAncho = 70;
        int spriteAlto  = 90;
        int offsetX     = (spriteAncho - ancho) / 2;

        sprite.dibujar(g2d,
                x - offsetX,
                y - (spriteAlto - alto),
                spriteAncho, spriteAlto,
                !miraDerecha
        );

        dibujarBarraVida(g2d);

        // Etiqueta
        g2d.setColor(new java.awt.Color(200, 255, 200));
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 9));
        g2d.drawString("Estudiante", x, y - 16);
    }
}
