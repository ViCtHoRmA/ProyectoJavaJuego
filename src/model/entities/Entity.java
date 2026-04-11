package model.entities;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

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
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima;
        this.velocidad = velocidad;
    }


    protected BufferedImage[] framesActuales;
    protected int frameActual = 0;
    protected int contadorAnim = 0;
    protected int velocidadAnim = 11;


    public abstract void actualizar();
    public abstract void dibujar(Graphics2D g2d);


    protected BufferedImage[] cargarFrames(String carpeta){
        BufferedImage[] frames = new BufferedImage[4];
        for (int i = 0; i < 4; i++){
            try {
                String ruta = "/sprites/" + carpeta + "/frame_" + i + ".png";
                var imagen = getClass().getResourceAsStream(ruta);
                frames[i] = ImageIO.read(imagen);
            }catch (Exception e){
                System.out.println("mo se pudo cargar: " + carpeta + "/frame_" + i + "  " + e.getMessage());
                frames[i] = null;
            }
        }
        return frames;


    }


    protected void actualizarAnimacion(){
        if (framesActuales == null) return;
        contadorAnim++;
        if (contadorAnim >= velocidadAnim){
            contadorAnim = 0;
            frameActual = (frameActual + 1) % framesActuales.length;
        }
    }

    protected void cambiarAnimacion(BufferedImage[] nuevos){
        if (nuevos != framesActuales){
            framesActuales = nuevos;
            frameActual = 0;
            contadorAnim = 0;
        }

    }



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
