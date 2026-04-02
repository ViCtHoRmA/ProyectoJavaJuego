package model.entities;

import controller.KeyController;
import view.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import java.awt.*;

public class Player extends Entity{

    // ── Referencia al teclado ─────────────────────────────────────────────────
    // El jugador necesita saber que teclas estan presionadas para moverse
    private KeyController keyController;

    // ── Fisica del salto ──────────────────────────────────────────────────────
    // velocidadY = que tan rapido sube o baja en este momento
    // Cuando salta, velocidadY se vuelve negativo (sube)
    // La gravedad lo va aumentando hasta que toca el suelo
    private float velocidadY = 0;
    private final float GRAVEDAD = 0.5f; // cuanto aumenta velocidadY cada frame
    private final float FUERZA_SALTO = -10; // velocidadY inicial al saltar
    private boolean enAire = false; // true si esta saltando o cayendo, false si esta en el suelo


    //Sistema de ataque
    public boolean atacando = false;
    private int tiempoAtaque = 0; // contador para controlar la duracion del ataque
    private int cooldownAtaque = 0; // contador para controlar el tiempo entre ataques
    private final int DURACION_ATAQUE = 15; // frames que dura el ataque
    private final int COOLDOWN_ATAQUE = 20; // frames que debe esperar

    //tipo de ataque
    // punio = false, patada = true
    public boolean esPatada = false;

    //invencibilidad temporal
    // Despues de recibir un golpe el jugador es invencible por unos frames
    // Esto evita que los enemigos quiten toda la vida de golpe
    private int frameInvencible = 0; // contador para controlar la duracion de la invencibilidad
    private final int DURACION_INVENCIBLE = 60; // frames que dura la inv

    // piezas recolectadas
    private int piezasRecogidas = 0;


    // ── Estados visuales ──────────────────────────────────────────────────────
    // Usamos un String para saber que animacion mostrar
    // "idle"    = quieto
    // "walk"    = caminando
    // "attack"  = atacando
    // "hurt"    = recibio un golpe
    // "jump"    = en el aire
    private String estado = "idle";


    // ── Constructor ───────────────────────────────────────────────────────────
    public Player(int x, int y, KeyController keyController) {
        // Llamamos al constructor de Entity con los valores del jugador:
        // posicion x, posicion y, ancho 40px, alto 60px, vida 100, velocidad 4
        super(x, y, 40, 60, 100, 4);
        this.keyController = keyController;
    }

    @Override
    public void actualizar() {
        if (!vivo) return; // si esta muerto no hace nada

        manejarMovimiento();
        manejarAtaque();
        aplicarGravedad();
        actualizarInvencibilidad();
        actualizarEstado();

    }

    public void manejarMovimiento(){

        if (keyController.moverIzquierda){
            x -= velocidad;
            miraDerecha = false;
        }
        if (keyController.moverDerecha){
            x += velocidad;
            miraDerecha = true;
        }
        if (keyController.saltar && !enAire) {
            velocidadY = FUERZA_SALTO;
            enAire = true;
        }
        // Limitar al jugador dentro de los bordes de la pantalla
        if (x < 0) {
            x = 0;
        }
        if (x > GamePanel.ANCHO - ancho) {
            x = GamePanel.ANCHO - ancho;
        }

    }

    public void manejarAtaque(){


        // Reducir el cooldown cada frame
        if (cooldownAtaque > 0) cooldownAtaque--;

        // Solo puede atacar si no esta atacando ya y el cooldown llego a 0
        if (!atacando && cooldownAtaque == 0) {

            if (keyController.atacarPunio) {
                iniciarAtaque(false);
            } else if (keyController.atacarPatada) {
                iniciarAtaque(true);
            }
        }

        if (atacando){
            tiempoAtaque--;
            if (tiempoAtaque <= 0) {
                atacando = false;
                cooldownAtaque = COOLDOWN_ATAQUE;
            }
        }

    }

    private void iniciarAtaque(boolean esPatada){

        this.atacando = true;
        this.esPatada = esPatada;
        this.tiempoAtaque = DURACION_ATAQUE;


    }

    //gravedad y suelo
    private void aplicarGravedad(){
        if (enAire) {
            velocidadY += GRAVEDAD;
            y += (int) velocidadY;

            // Si toca el suelo, se detiene y se pone en la posicion del suelo
            if (y >= GamePanel.SUELO) {
                y = GamePanel.SUELO;
                velocidadY = 0;
                enAire = false;
            }
        }
    }

    // ── Invencibilidad tras recibir daño ──────────────────────────────────────
    private void actualizarInvencibilidad() {
        if (frameInvencible > 0) frameInvencible--;
    }


    // ── Actualizar el estado visual segun lo que esta haciendo
    private void actualizarEstado() {
        if (atacando) {
            estado = "attack";
        } else if (enAire) {
            estado = "jump";
            
        } else if (frameInvencible > 0 && !atacando) {
            estado = "hurt";
        } else if (keyController.moverIzquierda || keyController.moverDerecha) {
            estado = "walk";
        } else {
            estado = "idle";
        }
    }


    @Override
    public void recibirDanio(int cantidad) {
        // Solo recibe daño si no esta en periodo de invencibilidad
        if (frameInvencible == 0) {
            super.recibirDanio(cantidad);
            frameInvencible = DURACION_INVENCIBLE;
        }
    }

    // ── Hitbox del ataque ─────────────────────────────────────────────────────
    // Es el rectangulo que representa el area donde golpea el punio o patada.
    // Aparece delante del jugador segun la direccion que mira.
    public Rectangle getHitboxAtaque() {
        if (!atacando) return new Rectangle(0, 0, 0, 0);// no hay hitbox si no esta atacando

        int hitX;
        int hitY; // un poco mas alto que el centro del jugador
        int hitAncho;
        int hitAlto;

        if (esPatada){
            hitAncho = 45;
            hitAlto = 25;
            hitY = y + alto - 30;
        }else {
            hitAncho = 35;
            hitAlto = 20;
            hitY = y + 15;
        }


        // Si mira a la derecha el golpe aparece a su derecha, si no a su izquierda
        if (miraDerecha){
            hitX = x + ancho; // empieza justo al lado derecho del jugador
        }else {
            hitX = x - hitAncho; // empieza a la izquierda del jugador, por eso restamos el ancho
        }



        return new Rectangle(hitX, hitY, hitAncho, hitAlto);
    }


//dibujar() se llamara 60 veces por segundo para pintar el jugador en su posicion actual dentro del GamePanel
    @Override
    public void dibujar(Graphics2D g2d) {
        if (!vivo) return;

        // Efecto de parpadeo cuando esta invencible:
        // cada 6 frames alterna entre visible e invisible
        if (frameInvencible > 0 && (frameInvencible / 6) % 2 == 0) return;

        dibujarCuerpo(g2d);
        dibujarBarraVida(g2d);

        // Mostrar el area de golpe en DEBUG (puedes quitarlo despues)
        // g2d.setColor(Color.RED);
        // g2d.draw(getHitboxAtaque());



    }

    private void dibujarCuerpo(Graphics2D g2d) {
        // Por ahora dibujamos formas geometricas simples.
        // En la Fase 7 reemplazaremos esto con sprites reales.

        // Color del cuerpo segun el estado
        Color colorCuerpo;
        switch (estado) {
            case "attack" -> colorCuerpo = new Color(255, 200, 0);  // amarillo al atacar
            case "hurt"   -> colorCuerpo = new Color(255, 80, 80);  // rojo al recibir daño
            case "jump"   -> colorCuerpo = new Color(80, 180, 255); // azul claro al saltar
            default       -> colorCuerpo = new Color(60, 120, 220); // azul normal
        }

        // Cuerpo (torso)
        g2d.setColor(colorCuerpo);
        g2d.fillRoundRect(x + 5, y + 20, ancho - 10, alto - 20, 6, 6);

        // Cabeza
        g2d.setColor(new Color(255, 220, 177));
        g2d.fillOval(x + 8, y - 10, ancho - 16, 30);

        // Ojos — cambian segun la direccion
        g2d.setColor(Color.BLACK);
        if (miraDerecha) {
            g2d.fillOval(x + 18, y - 4, 5, 5);
        } else {
            g2d.fillOval(x + ancho - 23, y - 4, 5, 5);
        }

        // Piernas
        g2d.setColor(new Color(40, 80, 160));
        if (estado.equals("walk")) {
            // Simula caminar alternando la posicion de las piernas
            // usando el tiempo (System.currentTimeMillis) para oscilar
            long t = System.currentTimeMillis() / 100;
            int offset = (int)(Math.sin(t) * 5);
            g2d.fillRoundRect(x + 5,          y + alto - 22, 12, 22, 4, 4);
            g2d.fillRoundRect(x + ancho - 17, y + alto - 22 + offset, 12, 22, 4, 4);
        } else {
            g2d.fillRoundRect(x + 5,          y + alto - 22, 12, 22, 4, 4);
            g2d.fillRoundRect(x + ancho - 17, y + alto - 22, 12, 22, 4, 4);
        }

        // Brazo de ataque
        if (atacando) {
            g2d.setColor(new Color(255, 220, 177));
            if (miraDerecha) {
                g2d.fillOval(x + ancho - 5, y + 18, 18, 14);
            } else {
                g2d.fillOval(x - 13, y + 18, 18, 14);
            }
        }
    }


}
