package model;

public class GameState {

    public static final int JUGANDO = 0;
    public static final int PAUSADO = 1;
    public static final int GAME_OVER = 2;
    public static final int VICTORIA = 3;


    private int estadoActual = JUGANDO;
    private int puntaje = 0;
    private int nivelActual = 1;



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


    public void reiniciar() {
        estadoActual = JUGANDO;
        puntaje = 0;
        nivelActual = 1;
    }


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

}
