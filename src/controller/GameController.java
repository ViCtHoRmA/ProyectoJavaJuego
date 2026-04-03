package controller;

import model.GameState;
import model.entities.Enemy;
import model.entities.Player;
import model.entities.ZombieProfessor;
import model.entities.ZombieStudent;
import model.items.MachinePiece;
import view.GamePanel;

import javax.imageio.ImageIO;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameController {

    private GamePanel panel;
    private KeyController keyController;

    //entidades del juego
    private Player jugador;
    private List<Enemy> enemigos;
    private List<MachinePiece> piezas;

    //estado del juego
    private GameState gameState;
    private CollisionController collisionController;

    //imagen de fondo
    private BufferedImage imagenFondo;

    // ── Mensaje temporal en pantalla ──────────────────────────────────────────
    // Cuando el jugador recoge una pieza mostramos un mensaje por unos segundos
    private String mensajeTemporal = "";
    private int tiempoMensaje = 0; // contador para controlar cuanto tiempo mostrar el mensaje
    private static final int DURACION_MENSAJE = 120; // frames que dura el mensaje (2 segundos a 60 FPS)



    private boolean jugando  = true;
    private boolean pausado  = false;

    public GameController(GamePanel panel, KeyController keyController) {
        this.panel = panel;
        this.keyController = keyController;

        cargarImagenes();
        inicializarJuego();
    }


    private void cargarImagenes() {
        try {
            imagenFondo = ImageIO.read(
                    Objects.requireNonNull(
                            getClass().getClassLoader().getResourceAsStream("fondo.jpeg"),
                            ""
                    )
            );
        } catch (IOException | NullPointerException e) {
            System.err.println("Error al cargar fondo.jpg: " + e.getMessage());
            imagenFondo = null;
        }
    }

    private void inicializarJuego(){

        //estado global
        gameState = new GameState();

        //jugador, aparece en el lado izquierdo sobre el suelo
        jugador = new Player(80, GamePanel.SUELO, keyController);

        //lista de enemigos, repartidos por el nivel
        enemigos = new ArrayList<>();
        enemigos.add(new ZombieStudent(300, GamePanel.SUELO));
        enemigos.add(new ZombieStudent(500, GamePanel.SUELO));
        enemigos.add(new ZombieStudent(650, GamePanel.SUELO));
        enemigos.add(new ZombieProfessor(420, GamePanel.SUELO));
        enemigos.add(new ZombieProfessor(720, GamePanel.SUELO));

        //lista de piezas, repartidas por el nivel
        piezas = new ArrayList<>();
        piezas.add(new MachinePiece(150, GamePanel.SUELO - 40, 1));
        piezas.add(new MachinePiece(350, GamePanel.SUELO - 40, 2));
        piezas.add(new MachinePiece(530, GamePanel.SUELO - 40, 3));
        piezas.add(new MachinePiece(680, GamePanel.SUELO - 40, 4));
        piezas.add(new MachinePiece(740, GamePanel.SUELO - 40, 5));

        //controlador de colisiones, le pasamos las entidades y el estado del juego
        collisionController = new CollisionController(jugador, enemigos, piezas, gameState);


    }


    public void actualizar() {

        System.out.println("Vida: " + jugador.vidaActual +
                " | Estado: " + gameState.getEstadoActual() +
                " | Vivo: " + jugador.vivo);

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

        //1. actualizar jugador
        jugador.actualizar();
        //2. actualizar enemigos pasandole la posicion del jugador para que puedan perseguirlo
        for (Enemy enemigo : enemigos) {
            enemigo.actualizar(jugador.x, jugador.y);
        }
        //3. actualizar animacion de las piezas
        for (MachinePiece pieza : piezas) {
            pieza.actualizar();
        }
        //4. verificar colisiones
        collisionController.verificarColisiones();
        //5. actualizar mensaje temporal
        if (tiempoMensaje>0) tiempoMensaje--;

        // 6. Detectar cuando se recoge una pieza para mostrar mensaje
        // Comparamos la cantidad anterior con la actual
        // (esto lo hacemos de forma simple revisando el estado)
        verificarMensajePieza();



    }


    //muestra un mensaje temporal cuando el jugador recoge una pieza
    private int piezasAnteriores = 0;
    private void verificarMensajePieza() {
        if (jugador.piezasRecogidas > piezasAnteriores) {
            piezasAnteriores = jugador.piezasRecogidas;
            mostrarMensaje("¡Pieza " + jugador.piezasRecogidas + " /5 recogida");
        }
    }

    private void mostrarMensaje(String mensaje) {
        mensajeTemporal = mensaje;
        tiempoMensaje = DURACION_MENSAJE;
    }


    private void reiniciarJuego(){
        inicializarJuego();
        piezasAnteriores = 0;
        mensajeTemporal = "";

    }

    public void dibujar(Graphics2D g2d) {
        // 1. Fondo
        dibujarFondo(g2d);

        // 2. Piezas
        for (MachinePiece pieza : piezas) {
            pieza.dibujar(g2d);
        }

        // 3. Enemigos
        for (Enemy enemigo : enemigos) {
            enemigo.dibujar(g2d);
        }

        // 4. Jugador
        jugador.dibujar(g2d);

        // 5. HUD
        dibujarHUD(g2d);

        // 6. Mensaje temporal
        if (tiempoMensaje > 0) {
            dibujarMensajeTemporal(g2d);
        }

        // 7. Pantallas de estado — solo muestra UNA a la vez
        //    El orden importa: Game Over y Victoria tienen prioridad sobre Pausa
        if (gameState.estaGameOver()) {
            dibujarPantallaGameOver(g2d);
        } else if (gameState.estaVictoria()) {
            dibujarPantallaVictoria(g2d);
        } else if (gameState.estaPausado()) {
            dibujarPantallaPausa(g2d);
        }
    }




    private void dibujarFondo(Graphics2D g2d) {

        g2d.drawImage(imagenFondo, 0, 0, GamePanel.ANCHO, GamePanel.ALTO, null);

    }

    private void dibujarHUD(Graphics2D g2d) {
        // Panel semitransparente en la esquina superior izquierda
        g2d.setColor(new Color(0, 0, 0, 140));
        g2d.fillRoundRect(8, 8, 220, 70, 10, 10);


        // ── Barra de vida ──────────────────────────────────────────────────
        g2d.setColor(new Color(200, 200, 200));
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("VIDA", 18, 28);

        // Fondo de la barra (rojo oscuro)
        g2d.setColor(new Color(120, 0, 0));
        g2d.fillRoundRect(55, 16, 160, 14, 6, 6);

        // vida actual
        double proporcionVida = (double) jugador.vidaActual / jugador.vidaMaxima;
        int anchoVida = (int) (160 * proporcionVida);

        //color de la barra cambia segun la vida (verde a rojo)
        if (proporcionVida > 0.5) g2d.setColor(new Color(0, 200, 0));
        else if (proporcionVida > 0.25) g2d.setColor(new Color(255, 180, 0));
        else g2d.setColor(new Color(255, 50, 50));
        g2d.fillRoundRect(55, 16, anchoVida, 14, 6, 6);

        //borde de la barra
        g2d.setColor(new Color(255, 255, 255, 80));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRoundRect(55, 16, 160, 14, 6, 6);

        // Numero de vida
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.drawString(jugador.vidaActual + "/" + jugador.vidaMaxima, 110, 27);

        // ── Piezas recolectadas ────────────────────────────────────────────
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawString("PIEZAS", 18, 50);

        //dibujar 5 cuadritos - amarillo si esta recogida, gris si no
        for (int i = 0; i < 5; i++) {
            if (i < jugador.piezasRecogidas){
                g2d.setColor(new Color(255, 200, 0));
            } else {
                g2d.setColor(new Color(80, 80, 80));
            }
            g2d.fillRoundRect(80 + i * 30, 36, 20, 20, 4, 4);
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.drawRoundRect(70 + (i * 30), 38, 22, 18, 4, 4);

            // ── Puntaje ───────────────────────────────────────────────────────
            g2d.setColor(new Color(0, 0, 0, 140));
            g2d.fillRoundRect(GamePanel.ANCHO - 140, 8, 132, 32, 10, 10);
            g2d.setColor(new Color(255, 220, 0));
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("SCORE: " + gameState.getPuntaje(), GamePanel.ANCHO - 130, 29);

        }



    }

    //mensaje temporal centrado en la pantalla
    private void dibujarMensajeTemporal(Graphics2D g2d) {

        //opacidad se va reduciendo con el tiempo
        float alpha = Math.min(1f, tiempoMensaje / 30f);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2d.setFont(new Font("Arial Black", Font.BOLD, 20));
        int anchoTexto = g2d.getFontMetrics().stringWidth(mensajeTemporal);
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - anchoTexto / 2 - 14, GamePanel.ALTO  / 2 - 45, anchoTexto + 28, 36, 10, 10);

        // Texto del mensaje
        g2d.setColor(new Color(255, 220, 0));
        g2d.drawString(mensajeTemporal, GamePanel.ANCHO / 2 - anchoTexto / 2, GamePanel.ALTO  / 2 - 22);

        // Restaurar opacidad completa
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }




    //pantalla pausa
    private void dibujarPantallaPausa(Graphics2D g2d) {

        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(0, 0, GamePanel.ANCHO, GamePanel.ALTO);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial Black", Font.BOLD, 52));
        g2d.drawString("PAUSADO", GamePanel.ANCHO / 2 - 140, GamePanel.ALTO  / 2 - 20);

        g2d.setFont(new Font("Arial", Font.PLAIN, 18));
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawString("Presiona ESC para continuar", GamePanel.ANCHO / 2 - 130, GamePanel.ALTO  / 2 + 30);
    }

    //pantalla game over
    private void dibujarPantallaGameOver(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, GamePanel.ANCHO, GamePanel.ALTO);

        // Titulo
        g2d.setColor(new Color(220, 0, 0));
        g2d.setFont(new Font("Arial Black", Font.BOLD, 58));
        g2d.drawString("GAME OVER", GamePanel.ANCHO / 2 - 185, GamePanel.ALTO  / 2 - 40);

        // Historia
        g2d.setColor(new Color(200, 200, 200));
        g2d.setFont(new Font("Arial", Font.ITALIC, 16));
        g2d.drawString("Los zombies te atraparon... la maquina queda incompleta.", GamePanel.ANCHO / 2 - 220, GamePanel.ALTO  / 2 + 10);

        // Puntaje final
        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Puntaje final: " + gameState.getPuntaje(), GamePanel.ANCHO / 2 - 80, GamePanel.ALTO  / 2 + 50);

        // Instruccion
        g2d.setColor(new Color(180, 180, 180));
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Presiona ESC para volver a intentarlo", GamePanel.ANCHO / 2 - 130, GamePanel.ALTO  / 2 + 85);


    }

    //pantalla victoria
    private void dibujarPantallaVictoria(Graphics2D g2d) {

        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, GamePanel.ANCHO, GamePanel.ALTO);

        // Titulo
        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(new Font("Arial Black", Font.BOLD, 50));
        g2d.drawString("¡ESCAPASTE!", GamePanel.ANCHO / 2 - 175, GamePanel.ALTO  / 2 - 60);

        // Historia
        g2d.setColor(new Color(200, 255, 200));
        g2d.setFont(new Font("Arial", Font.ITALIC, 15));
        g2d.drawString("Reparaste la maquina y volviste a tu dimension.",
                GamePanel.ANCHO / 2 - 185,
                GamePanel.ALTO  / 2 - 15);
        g2d.drawString("Los profesores y estudiantes zombie quedaron atrapados para siempre.",
                GamePanel.ANCHO / 2 - 255,
                GamePanel.ALTO  / 2 + 15);

        // Puntaje
        g2d.setColor(new Color(255, 220, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Puntaje final: " + gameState.getPuntaje(),
                GamePanel.ANCHO / 2 - 85,
                GamePanel.ALTO  / 2 + 55);

        // Instruccion
        g2d.setColor(new Color(180, 180, 180));
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Presiona ESC para jugar de nuevo",
                GamePanel.ANCHO / 2 - 115,
                GamePanel.ALTO  / 2 + 90);

    }




}
