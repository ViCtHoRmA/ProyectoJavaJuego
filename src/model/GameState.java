package model;

public class GameState {

    // ── Estados posibles del juego ────────────────────────────────────────────
    // Usamos constantes enteras para representar cada estado.
    // El juego siempre esta en exactamente uno de estos estados.

    public static final int JUGANDO = 0;
    public static final int PAUSADO = 1;
    public static final int GAME_OVER = 2;
    public static final int VICTORIA = 3;

    // ── Estado actual ─────────────────────────────────────────────────────────
    // Empieza en JUGANDO cuando se crea
    private int estadoActual = JUGANDO;

    //puntaje
    private int puntaje = 0;

    // ── Nivel actual ──────────────────────────────────────────────────────────
    // Empieza en el nivel 1
    private int nivelActual = 1;



    public int getEstadoActual() {
        return estadoActual;
    }
    public int getPuntaje() {
        return puntaje;
    }
    public int getNivelActual() {
        return nivelActual;
    }


    public void setEstadoActual(int nuevoEstado) {
        estadoActual = nuevoEstado;
    }
    public void setPuntaje(int cantidad) {
        puntaje += cantidad;
    }
    public void setNivelActual(int nivel) {
        nivelActual = nivel;
    }


    // ── Metodos de consulta rapida ────────────────────────────────────────────
    // En vez de escribir gameState.getEstadoActual() == GameState.JUGANDO
    // podemos escribir gameState.estaJugando() — mas legible

    public boolean estaJugando() {
        return estadoActual == JUGANDO;
    }
    public boolean estaPausado() {
            return estadoActual == PAUSADO;
    }
    public boolean estaGameOver() {
            return estadoActual == GAME_OVER;
    }
    public boolean estaVictoria() {
            return estadoActual == VICTORIA;
    }

    //riniciar todo para empezar de nuevo
    public void reiniciar() {
        estadoActual = JUGANDO;
        puntaje = 0;
        nivelActual = 1;
    }

}
