package model.entities;


import java.awt.*;
import java.awt.image.BufferedImage;


public class ZombieProfessor extends Enemy{

    private BufferedImage[] framesCaminarDerecha;
    private BufferedImage[] framesCaminarIzquierda;
    private BufferedImage[] framesAtacarDerecha;
    private BufferedImage[] framesAtacarIzquierda;



    public ZombieProfessor(int x, int y) {
        super(x, y, 85, 130, 250, 2, 220, 55, 18, 300);
        this.intervaloAtaque = 80;
        framesCaminarDerecha = cargarFrames("zombie_profesor/caminar_derecha");
        framesCaminarIzquierda = cargarFrames("zombie_profesor/caminar_izquierda");
        framesAtacarDerecha = cargarFrames("zombie_profesor/atacar_derecha");
        framesAtacarIzquierda = cargarFrames("zombie_profesor/atacar_izquierda");

        framesActuales = framesCaminarDerecha;
    }


    @Override
    public void dibujar(Graphics2D g2d) {
        if (!vivo) return;

        BufferedImage[] nuevos;
        if (estadoIA.equals("atacando")){
            nuevos = miraDerecha ? framesAtacarDerecha : framesAtacarIzquierda;
        }else {
            nuevos = miraDerecha ? framesCaminarDerecha : framesCaminarIzquierda;
        }
        cambiarAnimacion(nuevos);
        actualizarAnimacion();

        g2d.drawImage(framesActuales[frameActual], x, y, ancho, alto, null);



        dibujarBarraVida(g2d);


    }


}
