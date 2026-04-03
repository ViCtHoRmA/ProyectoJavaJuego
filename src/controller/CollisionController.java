package controller;

import model.GameState;
import model.items.MachinePiece;
import model.entities.Enemy;
import model.entities.Player;

import java.util.List;

public class CollisionController {

    //instancias de objetos
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

    // ── Metodo principal — se llama cada frame ────────────────────────────────
    // Revisa todos los tipos de colision en orden
    public void verificarColisiones() {
        verificarAtaqueJugador();
        verificarDanioJugador();
        verificarRecoleccionPiezas();
        verificarCondicionesFinales();

    }


    // ── 1. El ataque del jugador golpea a los enemigos ─────────────────
    private void verificarAtaqueJugador() {
        if (!jugador.atacando) return;

        // Obtenemos el rectangulo del area de golpe del jugador
        // y revisamos si se superpone con algun enemigo

        for (Enemy enemigo : enemigos){
            if (!enemigo.estaVivo()) continue; //ignorar enemigos muertos

            // intersects() devuelve true si los dos rectangulos se superponen
            if (jugador.getHitboxAtaque().intersects(enemigo.getHitbox())) {

                //el dano depende del tipo de ataque
                int danio = jugador.esPatada ? 20 : 12;
                enemigo.recibirDanio(danio);

                //si el enemigo murio sumar puntos
                if (!enemigo.estaVivo()){
                    gameState.setPuntaje(enemigo.getPuntos());
                }
            }
        }
    }

    // ── 2. Los enemigos golpean al jugador ───────────────────────────────
    private void verificarDanioJugador(){
        for (Enemy enemigo : enemigos){
            if (!enemigo.estaVivo()) continue; //ignorar enemigos muertos


            // Si el enemigo puede atacar en este frame y esta tocando al jugador
            if (enemigo.puedeAtacar() && enemigo.getHitbox().intersects(jugador.getHitbox())) {

                jugador.recibirDanio(enemigo.getDanio());

            }



        }
    }

    // ── 3. El jugador recoge piezas de la maquina ─
    private void verificarRecoleccionPiezas(){
        for (MachinePiece pieza : piezas){
            if (pieza.recogida) continue; //ignorar piezas ya recogidas

            // Si el jugador toca la pieza, la recoge
            if (jugador.getHitbox().intersects(pieza.getHitbox())) {
                pieza.recogida = true; // marca la pieza como recogida
                jugador.piezasRecogidas++; // suma 1 a las piezas recogidas
            }
        }
    }

    // ── 4. Revisar si el juego termino ─────────────────────
    private void verificarCondicionesFinales(){
        if (!jugador.estaVivo()) {
            gameState.setEstadoActual(GameState.GAME_OVER);
        } else if (jugador.piezasRecogidas >= 5) {
            gameState.setEstadoActual(GameState.VICTORIA);
        }
    }


}




