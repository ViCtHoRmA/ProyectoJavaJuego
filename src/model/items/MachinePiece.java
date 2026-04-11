package model.items;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;


public class MachinePiece {

    public int x;
    public int y;
    public boolean recogida = false;
    private int numero;

    private BufferedImage[] frames;
    private int frameActual = 0;
    private int contadorAnim = 0;
    private final int VELOCIDAD_ANIM = 8;


    private float piezaFlotacion = 0;
    private float tiempoAnim = 0;

    public MachinePiece(int x, int y, int numero) {
        this.x = x;
        this.y = y;
        this.numero = numero;
        cargarFrames();
    }

    private void cargarFrames(){
        frames = new BufferedImage[4];
        for (int i = 0; i < 4; i++) {
            try {
                String ruta = "/sprites/pieza/frame_" + i + ".png";
                var stream = getClass().getResourceAsStream(ruta);
                frames[i] = ImageIO.read(stream);
            } catch (Exception e) {
                System.out.println("No se pudo cargar pieza_maquina/frame_" + i + "  " + e.getMessage());
                frames[i] = null;
            }
        }
    }


    public void actualizar() {
        if (recogida) return;

        tiempoAnim += 0.05f;
        piezaFlotacion = (float) Math.sin(tiempoAnim) * 5;

        contadorAnim++;
        if (contadorAnim >= VELOCIDAD_ANIM){
            contadorAnim = 0;
            frameActual = (frameActual + 1) % 4;
        }
    }

    public void dibujar(Graphics2D g2d) {
        if (recogida) return;

        if (frames != null && frames[frameActual] != null){
            g2d.drawImage(frames[frameActual],x,y,40,40,null);
        }
    }


    public Rectangle getHitbox() {
        return new Rectangle(x, y, 30, 30);
    }

    public int getNumero() {
        return numero;
    }

}
