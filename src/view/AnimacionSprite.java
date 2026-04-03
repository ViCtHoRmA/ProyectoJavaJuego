package view;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class AnimacionSprite {

    // ── Sprite sheet completo ─────────────────────────────────────────────────
    // Es la imagen PNG completa cargada en memoria
    private BufferedImage spriteSheet;

    // ── Frames recortados ─────────────────────────────────────────────────────
    // Cada posicion del array es un frame individual ya recortado
    // frames[fila][columna] = una imagen pequeña
    private BufferedImage[][] frames;

    // ── Tamaño de cada frame ──────────────────────────────────────────────────
    private int frameAncho;
    private int frameAlto;

    // ── Cuantas filas y columnas tiene el sprite sheet ────────────────────────
    private int filas;
    private int columnas;

    // ── Control de animacion ──────────────────────────────────────────────────
    // frameActual = que frame estamos mostrando ahora mismo
    private int frameActual = 0;

    // contador = cuantos frames del juego han pasado desde el ultimo cambio
    private int contador    = 0;

    // velocidadAnim = cada cuantos frames del juego cambiamos al siguiente frame
    // Si es 8, cambiamos de frame cada 8/60 = 0.13 segundos
    private int velocidadAnim;

    // ── Fila activa ───────────────────────────────────────────────────────────
    // Cada fila del sprite sheet es una animacion diferente
    // (idle, caminar, atacar, etc.)
    private int filaActiva = 0;


    // ── Cuantos frames tiene la animacion activa ──────────────────────────────
    private int framesEnFilaActiva;

    // ── Constructor ───────────────────────────────────────────────────────────
    public AnimacionSprite(String rutaImagen, int frameAncho, int frameAlto,
                           int filas, int columnas, int velocidadAnim) {
        this.frameAncho    = frameAncho;
        this.frameAlto     = frameAlto;
        this.filas         = filas;
        this.columnas      = columnas;
        this.velocidadAnim = velocidadAnim;
        this.framesEnFilaActiva = columnas; // por defecto todos los frames

        cargarSpriteSheet(rutaImagen);
        recortarFrames();
    }


    // ── Carga el sprite sheet completo desde resources ────────────────────────
    private void cargarSpriteSheet(String ruta) {
        try {
            spriteSheet = ImageIO.read(
                    Objects.requireNonNull(
                            getClass().getClassLoader().getResourceAsStream(ruta),
                            "No se encontro: " + ruta
                    )
            );
        } catch (IOException | NullPointerException e) {
            System.err.println("Error cargando sprite: " + e.getMessage());
            spriteSheet = null;
        }
    }


    // ── Recorta cada frame del sprite sheet ───────────────────────────────────
    // getSubimage(x, y, ancho, alto) recorta un pedazo de la imagen grande
    private void recortarFrames() {
        if (spriteSheet == null) return;

        frames = new BufferedImage[filas][columnas];

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {

                // La posicion x e y de este frame dentro del sprite sheet
                int x = col  * frameAncho;
                int y = fila * frameAlto;

                // Verificar que no nos salimos de los bordes de la imagen
                if (x + frameAncho <= spriteSheet.getWidth() &&
                        y + frameAlto  <= spriteSheet.getHeight()) {

                    frames[fila][col] = spriteSheet.getSubimage(
                            x, y, frameAncho, frameAlto
                    );
                }
            }
        }
    }

    // ── Actualizar — se llama 60 veces por segundo ────────────────────────────
    // Avanza al siguiente frame cuando el contador llega a velocidadAnim
    public void actualizar() {
        contador++;
        if (contador >= velocidadAnim) {
            contador = 0;
            frameActual++;

            // Cuando llega al ultimo frame vuelve al primero (loop)
            if (frameActual >= framesEnFilaActiva) {
                frameActual = 0;
            }
        }
    }

    // ── Cambiar la animacion activa ───────────────────────────────────────────
    // Llamamos a este metodo cuando el personaje cambia de estado
    // por ejemplo de "idle" a "caminar"
    public void setAnimacion(int fila, int cantidadFrames) {

        // Solo reiniciamos si realmente cambio la animacion
        // para que no se reinicie constantemente
        if (this.filaActiva != fila) {
            this.filaActiva          = fila;
            this.framesEnFilaActiva  = cantidadFrames;
            this.frameActual         = 0;
            this.contador            = 0;
        }
    }

    // ── Dibujar el frame actual ───────────────────────────────────────────────
    public void dibujar(Graphics2D g2d, int x, int y,
                        int ancho, int alto, boolean voltear) {

        if (frames == null) return;

        BufferedImage frame = frames[filaActiva][frameActual];
        if (frame == null) return;

        if (voltear) {
            // Para voltear la imagen horizontalmente usamos una transformacion:
            // dibujamos desde x+ancho hacia la izquierda con ancho negativo
            g2d.drawImage(frame,
                    x + ancho, y,      // punto de inicio (derecha)
                    -ancho,    alto,   // ancho negativo = voltea horizontalmente
                    null);
        } else {
            g2d.drawImage(frame, x, y, ancho, alto, null);
        }
    }

    // Devuelve si la animacion termino su ciclo completo
    // util para saber cuando termino la animacion de ataque
    public boolean animacionTermino() {
        return frameActual == framesEnFilaActiva - 1;
    }

}
