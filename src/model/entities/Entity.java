package model.entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Entity {

    // posicion y tamaño en pantalla
    // x = distancia desde el borde izquierdo de la pantalla
    // y = distancia desde el borde superior de la pantalla


    public int x;
    public int y;
    public int ancho;
    public int alto;

    // vidaActual = cuanta vida tiene ahora mismo
    // vidaMaxima = cuanta vida tenia al inicio (para calcular la barra)
    public int vidaMaxima;
    public int vidaActual;


    // cuantos pixeles se mueve por frame
    public int velocidad;

    // ── Direccion a la que mira ───────────────────────────────────────────────
    // true = mira a la derecha, false = mira a la izquierda
    // lo usaremos para voltear el sprite o el dibujo
    public boolean miraDerecha = true;

    // ── Estado de vida ────────────────────────────────────────────────────────
    public boolean vivo = true;

    // ── Constructor ───────────────────────────────────────────────────────────
    // Cuando creamos cualquier entidad le pasamos su posicion y tamaño inicial
    public Entity(int x, int y, int ancho, int alto, int vidaMaxima, int velocidad) {
        this.x          = x;
        this.y          = y;
        this.ancho      = ancho;
        this.alto       = alto;
        this.vidaMaxima = vidaMaxima;
        this.vidaActual = vidaMaxima; // empieza con vida completa
        this.velocidad  = velocidad;
    }


    // ── Metodos abstractos ────────────────────────────────────────────────────
    // Estas dos palabras "abstract" significan que cada subclase
    // ESTA OBLIGADA a escribir su propia version de estos metodos.
    // Entity no sabe como se actualiza un jugador vs un enemigo,
    // por eso los deja sin implementar y obliga a cada hijo a hacerlo.

    // actualizar() se llamara 60 veces por segundo para mover la entidad,
    // aplicar gravedad, cambiar estados, etc.
    public abstract void actualizar();

    // dibujar() se llamara 60 veces por segundo para pintar la entidad
    // en su posicion actual dentro del GamePanel
    public abstract void dibujar(Graphics2D g2d);

    // ── Metodos concretos (ya implementados) ──────────────────────────────────
    // Estos metodos SI tienen codigo porque son iguales para todos.
    // El jugador y los enemigos los heredan sin necesidad de reescribirlos.

    // Resta vida al recibir un golpe
    // Si la vida baja a 0 o menos, marca la entidad como muerta
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


    // Devuelve un rectangulo que representa el area fisica de la entidad.
    // Este rectangulo se usa para detectar cuando dos entidades se tocan.
    // En Java, Rectangle tiene un metodo intersects() que compara dos
    // rectangulos y dice si se superponen.
    public Rectangle getHitbox() {
        return new Rectangle(x, y, ancho, alto);
    }


    // Dibuja la barra de vida encima de la entidad.
    // La usamos tanto en el jugador como en los enemigos,
    // por eso la ponemos aqui en la clase base.
    public void dibujarBarraVida(Graphics2D g2d) {
        int barraAncho = ancho;
        int barraAlto = 6;
        int barraX = x;
        int barraY = y - 12; // un poco por encima de la cabeza

        // Fondo rojo (vida perdida)
        g2d.setColor(new java.awt.Color(180, 0, 0));
        g2d.fillRect(barraX, barraY, barraAncho, barraAlto);

        // Verde encima (vida restante) — se calcula como proporcion
        // Si tiene 75 de 100 de vida, el verde ocupa el 75% del ancho

        double proporcion = (double) vidaActual / vidaMaxima;
        int verdeAncho = (int) (barraAncho * proporcion);

        g2d.setColor(new java.awt.Color(0, 200, 0));
        g2d.fillRect(barraX, barraY, verdeAncho, barraAlto);

        // Borde de la barra
        g2d.setColor(new java.awt.Color(0, 0, 0));
        g2d.drawRect(barraX, barraY, barraAncho, barraAlto);

    }


}
