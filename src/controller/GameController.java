package controller;

import model.GameState;
import model.entities.Enemy;
import model.entities.Player;
import model.items.MachinePiece;
import view.GamePanel;
import model.map.Level;
import view.HUDPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    private GamePanel panel;
    private KeyController keyController;
    private HUDPanel hud;

    //entidades del juego
    private Player jugador;
    private List<Level> niveles;
    // El nivel que se esta jugando ahora mismo (indice 0, 1 o 2)
    private int indiceNivelActual = 0;
    private Level nivelActual;


    //estado del juego
    private GameState gameState;
    private CollisionController collisionController;

    private boolean enTransicion = false;
    private int tiempoTransicion = 0;
    private static final int DURACION_TRANSICION = 180; // frames que dura la


    // ── Mensaje temporal en pantalla ──────────────────────────────────────────
    // Cuando el jugador recoge una pieza mostramos un mensaje por unos segundos
    private String mensajeTemporal = "";
    private int tiempoMensaje = 0; // contador para controlar cuanto tiempo mostrar el mensaje
    private static final int DURACION_MENSAJE = 120; // frames que dura el mensaje (2 segundos a 60 FPS)



    private int piezasAnteriores = 0;

    public GameController(GamePanel panel, KeyController keyController) {
        this.panel = panel;
        this.keyController = keyController;

        inicializarJuego();
    }



    private void inicializarJuego(){

        //estado global
        gameState = new GameState();

        //jugador, aparece en el lado izquierdo sobre el suelo
        jugador = new Player(80, GamePanel.SUELO, keyController);

        niveles = new ArrayList<>();
        niveles.add(new Level(1, "Biblioteca", "fondo_nivel1.jpeg"));
        niveles.add(new Level(2, "Pasillos de la UNET", "fondo_nivel2.jpg"));
        niveles.add(new Level(3, "Laboratorio", "fondo_nivel3.jpg"));

        indiceNivelActual = 0;
        hud = new HUDPanel(gameState, jugador);
        cargarNivel(indiceNivelActual);


    }

    private void cargarNivel(int indice){
        nivelActual = niveles.get(indice);
        gameState.setNivelActual(nivelActual.getNumero());

        // Reiniciar posicion del jugador al inicio del nivel
        jugador.x = 80;
        jugador.y = GamePanel.SUELO;

        // Crear el controlador de colisiones con las entidades del nivel actual
        collisionController = new CollisionController(jugador, nivelActual.getEnemigos(), nivelActual.getPiezas(), gameState);
        mostrarMensaje("Nivel " + nivelActual.getNumero()
                + ": " + nivelActual.getNombre());
    }


    public void actualizar() {



        if (gameState.estaVictoria() || gameState.estaGameOver()) {
            if (keyController.pausa) {
                reiniciarJuego();
            }
            return;
        }

        if (gameState.estaPausado()) {
            if (!keyController.pausa) {
                gameState.setEstadoActual(GameState.JUGANDO);
            }

            return;
        }


        if (keyController.pausa) {
            gameState.setEstadoActual(GameState.PAUSADO);
            return;
        }

        if (enTransicion) {
            tiempoTransicion--;
            if (tiempoTransicion <= 0) {
                enTransicion = false;
                avanzarAlSiguienteNivel();
            }
            return;
        }

        //1. actualizar jugador
        jugador.actualizar();
        //2. actualizar enemigos pasandole la posicion del jugador para que puedan perseguirlo
        for (Enemy e : nivelActual.getEnemigos()) {
            e.actualizar(jugador.x, jugador.y);
        }
        //3. actualizar animacion de las piezas
        for (MachinePiece pieza : nivelActual.getPiezas()) {
            pieza.actualizar();
        }
        //4. verificar colisiones
        collisionController.verificarColisiones();

        if (jugador.piezasRecogidas > piezasAnteriores) {
            piezasAnteriores = jugador.piezasRecogidas;
            mostrarMensaje("¡Pieza " + jugador.piezasRecogidas + "/5 recogida!");
        }

        //5. actualizar mensaje temporal
        if (tiempoMensaje>0) tiempoMensaje--;

        // 6. Detectar cuando se recoge una pieza para mostrar mensaje
        // Comparamos la cantidad anterior con la actual
        // (esto lo hacemos de forma simple revisando el estado)
        verificarCompletarNivel();
        verificarMensajePieza();

    }

    private void verificarCompletarNivel() {
        if (nivelActual.isCompletado()) return;

        boolean enemigosDerrotados = nivelActual.todosEnemigosDerrotados();
        boolean piezasRecogidas    = nivelActual.todasPiezasRecogidas();

        // Mostrar pistas al jugador sobre qué le falta
        if (enemigosDerrotados && !piezasRecogidas) {
            mostrarMensaje("¡Derrota los zombies y recoge todas las piezas!");
        }

        // Solo avanza si se cumplen las DOS condiciones
        if (enemigosDerrotados && piezasRecogidas) {
            nivelActual.setCompletado();

            if (indiceNivelActual == niveles.size() - 1) {
                gameState.setEstadoActual(GameState.VICTORIA);
            } else {
                enTransicion     = true;
                tiempoTransicion = DURACION_TRANSICION;
                mostrarMensaje("¡Nivel completado! Siguiente nivel...");
            }
        }
    }

    private void avanzarAlSiguienteNivel(){
        indiceNivelActual++;
        cargarNivel(indiceNivelActual);
    }




    private void verificarMensajePieza() {
        if (jugador.piezasRecogidas > piezasAnteriores) {
            piezasAnteriores = jugador.piezasRecogidas;
            mostrarMensaje("¡Pieza " + jugador.piezasRecogidas + " /5 recogida");
        }
    }

    private void mostrarMensaje(String mensaje) {
        mensajeTemporal = mensaje;        // para dibujarTransicion()
        tiempoMensaje   = DURACION_MENSAJE;
        hud.mostrarMensaje(mensaje);
    }


    private void reiniciarJuego(){
        inicializarJuego();
        piezasAnteriores = 0;
        mensajeTemporal = "";

    }

    public void dibujar(Graphics2D g2d) {
        // 1. Fondo del nivel actual
        nivelActual.dibujarFondo(g2d);

        // 2. Piezas del nivel actual
        for (MachinePiece p : nivelActual.getPiezas()) {
            p.dibujar(g2d);
        }

        // 3. Enemigos del nivel actual
        for (Enemy e : nivelActual.getEnemigos()) {
            e.dibujar(g2d);
        }

        // 4. Jugador
        jugador.dibujar(g2d);

        // 5. Nombre del nivel
        nivelActual.dibujarNombre(g2d);

        // 6. HUD completo
        hud.dibujar(g2d);

        // 7. Pantalla de transicion entre niveles
        if (enTransicion) dibujarTransicion(g2d);

        // 8. Pantallas de estado — solo una a la vez
        if      (gameState.estaGameOver())  hud.dibujarPantallaGameOver(g2d);
        else if (gameState.estaVictoria())  hud.dibujarPantallaVictoria(g2d);
        else if (gameState.estaPausado()) hud.dibujarPantallaPausa(g2d);
    }


    // ── Pantalla de transicion entre niveles ──────────────────────────────────
    private void dibujarTransicion(Graphics2D g2d) {

        // Overlay que se va oscureciendo conforme pasa el tiempo
        float oscuridad = 1f - ((float) tiempoTransicion / DURACION_TRANSICION);
        g2d.setColor(new Color(0, 0, 0,
                Math.min(200, (int)(oscuridad * 255))));
        g2d.fillRect(0, 0, GamePanel.ANCHO, GamePanel.ALTO);

        // Solo mostrar el texto cuando ya esta suficientemente oscuro
        if (oscuridad > 0.4f) {
            int siguiente = indiceNivelActual + 2; // +2 porque el indice es 0-based
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial Black", Font.BOLD, 36));
            String txt = "Nivel " + siguiente;
            int    tw  = g2d.getFontMetrics().stringWidth(txt);
            g2d.drawString(txt,
                    GamePanel.ANCHO / 2 - tw / 2,
                    GamePanel.ALTO  / 2 - 20);

            g2d.setFont(new Font("Arial", Font.ITALIC, 18));
            String sub = niveles.get(indiceNivelActual + 1).getNombre();
            int    sw  = g2d.getFontMetrics().stringWidth(sub);
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawString(sub,
                    GamePanel.ANCHO / 2 - sw / 2,
                    GamePanel.ALTO  / 2 + 20);
        }
    }





}
