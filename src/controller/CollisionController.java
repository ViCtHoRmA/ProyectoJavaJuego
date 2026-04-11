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
    private SoundManager soundManager;

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
                SoundManager.getInstance().reproducir("golpe");

                if (!enemigo.estaVivo()){
                    gameState.setPuntaje(enemigo.getPuntos());
                    SoundManager.getInstance().reproducir("muerte_zombie");
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
                SoundManager.getInstance().reproducir("recoger");
            }
        }
    }


    private void verificarCondicionesFinales(){
        if (!jugador.estaVivo()) {
            SoundManager.getInstance().detener("musica");
            SoundManager.getInstance().reproducir("game_over");
            gameState.setEstadoActual(GameState.GAME_OVER);
        }
    }


}




