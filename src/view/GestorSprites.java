package view;

public class GestorSprites {

    // ── Sprites de cada personaje ─────────────────────────────────────────────
    public AnimacionSprite jugador;
    public AnimacionSprite zombieEstudiante;
    public AnimacionSprite zombieProfesor;

    // ── Instancia unica (patron Singleton) ────────────────────────────────────
    // Solo necesitamos cargar los sprites una vez en todo el juego.
    // El Singleton garantiza que solo existe un GestorSprites.
    private static GestorSprites instancia;

    public static GestorSprites getInstance() {
        if (instancia == null) {
            instancia = new GestorSprites();
        }
        return instancia;
    }

    // ── Constructor privado — carga todos los sprites ─────────────────────────
    private GestorSprites() {
        cargarSprites();
    }

    private void cargarSprites() {

        // ── Jugador ───────────────────────────────────────────────────────────
        // Sprite sheet: 500x500px, 4 columnas x 4 filas, cada frame 125x125px
        // Fila 0: idle de frente (4 frames)
        // Fila 1: caminar de lado (4 frames)
        // Fila 2: idle de lado   (4 frames)
        // Fila 3: caminar        (4 frames)
        jugador = new AnimacionSprite(
                "sprites/jugador.png",
                125, 125,  // ancho y alto de cada frame
                4, 4,      // filas y columnas
                8          // velocidad: cambia frame cada 8/60 seg
        );

        // ── Zombie Estudiante ─────────────────────────────────────────────────
        // Sprite sheet: 421x592px
        // Fila 0-1: IDLE     (5+3 = 8 frames aprox)
        // Fila 2:   WALK     (4 frames)
        // Fila 3-4: ATTACK   (4+3 = 7 frames aprox)
        zombieEstudiante = new AnimacionSprite(
                "sprites/zombie_estudiante.png",
                84, 118,   // ancho y alto de cada frame
                5, 5,      // filas y columnas
                10         // un poco mas lento que el jugador
        );

        // ── Zombie Profesor ───────────────────────────────────────────────────
        // Sprite sheet: 339x737px
        // Fila 0-1: IDLE     (4+4 frames)
        // Fila 2-3: WALK     (5+1 frames)
        // Fila 4-5: ATTACK   (3+3 frames)
        zombieProfesor = new AnimacionSprite(
                "sprites/zombie_profesor.png",
                84, 122,   // ancho y alto de cada frame
                6, 4,      // filas y columnas
                10
        );
    }
}
