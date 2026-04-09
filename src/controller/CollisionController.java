package controller;

import model.GameState;
import model.items.MachinePiece;
import model.entities.Enemy;
import model.entities.Player;

import java.util.List;

public class CollisionController {


    private Player jugador;
    private List<Enemy> enemigos;
    private List<MachinePiece> piezas;
    private GameState gameState;

    public CollisionController(Player jugador, List<Enemy> enemigos, List<MachinePiece> piezas, GameState gameState) {
        this.jugador = jugador;
        this.enemigos = enemigos;
        this.piezas = piezas;
        this.gameState = gameState;
    }


    public void verificarColisiones() {
        verificarAtaqueJugador();
        verificarDanioJugador();
        verificarRecoleccionPiezas();
        verificarCondicionesFinales();

    }



    private void verificarAtaqueJugador() {
        if (!jugador.atacando) return;

        for (Enemy enemigo : enemigos){
            if (!enemigo.estaVivo()) continue;

            if (jugador.getHitboxAtaque().intersects(enemigo.getHitbox())) {

                int danio = jugador.esPatada ? 7 : 5;
                enemigo.recibirDanio(danio);

                if (!enemigo.estaVivo()){
                    gameState.setPuntaje(enemigo.getPuntos());
                }
            }
        }
    }


    private void verificarDanioJugador(){
        for (Enemy enemigo : enemigos) {
            if (!enemigo.estaVivo()) continue;

            if (enemigo.danioAplicado) {
                jugador.recibirDanio(enemigo.getDanio());
                enemigo.danioAplicado = false;
            }
        }
    }

    private void verificarRecoleccionPiezas(){
        for (MachinePiece pieza : piezas){
            if (pieza.recogida) continue;

            if (jugador.getHitbox().intersects(pieza.getHitbox())) {
                pieza.recogida = true;
                jugador.piezasRecogidas++;
            }
        }
    }


    private void verificarCondicionesFinales(){
        if (!jugador.estaVivo()) {
            gameState.setEstadoActual(GameState.GAME_OVER);
        }
    }


}




