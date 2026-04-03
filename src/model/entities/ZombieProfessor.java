package model.entities;


import view.AnimacionSprite;
import view.GestorSprites;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;


public class ZombieProfessor extends Enemy{

    private AnimacionSprite sprite = new AnimacionSprite(
            "sprites/zombie_profesor.png",
            84, 122, 6, 4, 10
    );

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


        switch (estadoIA) {
            case "persiguiendo" -> sprite.setAnimacion(2, 4); // walk
            case "atacando"     -> sprite.setAnimacion(4, 3); // attack
            default             -> sprite.setAnimacion(0, 4); // idle
        }

        sprite.actualizar();

        int spriteAncho = 75;
        int spriteAlto  = 100;
        int offsetX     = (spriteAncho - ancho) / 2;

        sprite.dibujar(g2d,
                x - offsetX,
                y - (spriteAlto - alto),
                spriteAncho, spriteAlto,
                !miraDerecha
        );

        dibujarBarraVida(g2d);

        g2d.setColor(new java.awt.Color(255, 200, 200));
        g2d.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 9));
        g2d.drawString("Profesor", x, y - 16);
    }


}
