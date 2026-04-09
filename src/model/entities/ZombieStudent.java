package model.entities;


import java.awt.*;
import java.awt.image.BufferedImage;

public class ZombieStudent extends Enemy{


    private BufferedImage[] framesCaminarDerecha;
    private BufferedImage[] framesCaminarIzquierda;
    private BufferedImage[] framesAtacarDerecha;
    private BufferedImage[] framesAtacarIzquierda;


    public ZombieStudent(int x, int y) {
        super(x, y, 75, 115, 150, 1, 190, 45, 10, 100);
        framesCaminarDerecha = cargarFrames("zombie_estudiante/caminar_derecha");
        framesCaminarIzquierda = cargarFrames("zombie_estudiante/caminar_izquierda");
        framesAtacarDerecha = cargarFrames("zombie_estudiante/atacar_derecha");
        framesAtacarIzquierda = cargarFrames("zombie_estudiante/atacar_izquierda");

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
