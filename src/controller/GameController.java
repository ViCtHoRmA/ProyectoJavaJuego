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
    private Player jugador;
    private List<Level> niveles;
    private Level nivelActual;
    private GameState gameState;
    private CollisionController collisionController;


    private int indiceNivelActual = 0;
    private boolean enTransicion = false;
    private int tiempoTransicion = 0;
    private static final int DURACION_TRANSICION = 180;
    private int sueloActual = GamePanel.SUELO_NIVEL1;
    private String mensajeTemporal = "";
    private int tiempoMensaje = 0;
    private static final int DURACION_MENSAJE = 120;
    private int piezasAnteriores = 0;



    public GameController(GamePanel panel, KeyController keyController) {
        this.panel = panel;
        this.keyController = keyController;

        inicializarJuego();
    }



    private void inicializarJuego(){
        gameState = new GameState();
        jugador = new Player(80, GamePanel.SUELO_NIVEL1 - 120, keyController);

        niveles = new ArrayList<>();
        niveles.add(new Level(1, "Edificio A UNET", "fondo_nivel1.png"));
        niveles.add(new Level(2, "Plaza Bolivar UNET", "fondo_nivel2.jpg"));
        niveles.add(new Level(3, "Laboratorio UNET", "fondo_nivel3.jpg"));

        indiceNivelActual = 0;
        hud = new HUDPanel(gameState, jugador);
        cargarNivel(indiceNivelActual);


    }

    private void cargarNivel(int indice){
        nivelActual = niveles.get(indice);
        switch (indiceNivelActual){
            case 0 -> sueloActual = GamePanel.SUELO_NIVEL1;
            case 1 -> sueloActual = GamePanel.SUELO_NIVEL2;
            case 2 -> sueloActual = GamePanel.SUELO_NIVEL3;
        }

        jugador.setSuelo(sueloActual);
        jugador.y = sueloActual - jugador.alto;

        gameState.setNivelActual(nivelActual.getNumero());

        jugador.setSuelo(sueloActual);
        jugador.x = 80;
        jugador.y = sueloActual - jugador.alto;


        collisionController = new CollisionController(jugador, nivelActual.getEnemigos(), nivelActual.getPiezas(), gameState);
        mostrarMensaje("Nivel " + nivelActual.getNumero() + ": " + nivelActual.getNombre());
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

        jugador.actualizar();

        for (Enemy e : nivelActual.getEnemigos()) {
            e.actualizar(jugador.x, jugador.y);
        }

        for (MachinePiece pieza : nivelActual.getPiezas()) {
            pieza.actualizar();
        }

        collisionController.verificarColisiones();

        if (jugador.piezasRecogidas > piezasAnteriores) {
            piezasAnteriores = jugador.piezasRecogidas;
            mostrarMensaje("Pieza " + jugador.piezasRecogidas + "/5 recogida");
        }

        if (tiempoMensaje>0) tiempoMensaje--;


        verificarCompletarNivel();
        verificarMensajePieza();

    }

    private void verificarCompletarNivel() {
        if (nivelActual.isCompletado()) return;

        boolean enemigosDerrotados = nivelActual.todosEnemigosDerrotados();
        boolean piezasRecogidas    = nivelActual.todasPiezasRecogidas();

        if (enemigosDerrotados && piezasRecogidas) {
            nivelActual.setCompletado();

            if (indiceNivelActual == niveles.size() - 1) {
                gameState.setEstadoActual(GameState.VICTORIA);
            } else {
                enTransicion     = true;
                tiempoTransicion = DURACION_TRANSICION;
                mostrarMensaje("Nivel completado, llendo al siguiente nivel");
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
            mostrarMensaje("Pieza " + jugador.piezasRecogidas + " /5 recogida");
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
        nivelActual.dibujarFondo(g2d);

        for (MachinePiece p : nivelActual.getPiezas()) {
            p.dibujar(g2d);
        }

        for (Enemy e : nivelActual.getEnemigos()) {
            e.dibujar(g2d);
        }

        jugador.dibujar(g2d);
        nivelActual.dibujarNombre(g2d);
        hud.dibujar(g2d);
        if (enTransicion) dibujarTransicion(g2d);
        if (gameState.estaGameOver()){
            hud.dibujarPantallaGameOver(g2d);
        }else if (gameState.estaVictoria()){
            hud.dibujarPantallaVictoria(g2d);
        }else if (gameState.estaPausado()){
            hud.dibujarPantallaPausa(g2d);
        }
    }

    private void dibujarTransicion(Graphics2D g2d) {


        float oscuridad = 1f - ((float) tiempoTransicion / DURACION_TRANSICION);
        g2d.setColor(new Color(0, 0, 0, Math.min(200, (int)(oscuridad * 255))));
        g2d.fillRect(0, 0, GamePanel.ANCHO, GamePanel.ALTO);


        if (oscuridad > 0.4f) {
            int siguiente = indiceNivelActual + 2;
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial Black", Font.BOLD, 36));
            String txt = "Nivel " + siguiente;
            int tw = g2d.getFontMetrics().stringWidth(txt);
            g2d.drawString(txt, GamePanel.ANCHO / 2 - tw / 2, GamePanel.ALTO  / 2 - 20);

            g2d.setFont(new Font("Arial", Font.ITALIC, 18));
            String sub = niveles.get(indiceNivelActual + 1).getNombre();
            int sw  = g2d.getFontMetrics().stringWidth(sub);
            g2d.setColor(new Color(200, 200, 200));
            g2d.drawString(sub, GamePanel.ANCHO / 2 - sw / 2, GamePanel.ALTO  / 2 + 20);
        }
    }





}
