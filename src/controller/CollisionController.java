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
                int danio = jugador.esPatada ? 9 : 6;
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
        for (Enemy enemigo : enemigos) {
            if (!enemigo.estaVivo()) continue;

            // El enemigo ya decidio en su propia IA si puede atacar
            // Solo leemos el flag y aplicamos el daño
            if (enemigo.danioAplicado) {
                jugador.recibirDanio(enemigo.getDanio());
                enemigo.danioAplicado = false; // consumir el flag
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
        }
    }


}




