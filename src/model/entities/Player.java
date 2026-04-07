package model.entities;

import controller.KeyController;
import view.GamePanel;

import java.awt.*;

public class Player extends Entity{

    private float velocidadY = 0;
    private final float GRAVEDAD = 0.5f;
    private final float FUERZA_SALTO = -9f;
    private boolean enAire = false;
    public boolean atacando = false;
    private int tiempoAtaque = 0;
    private int cooldownAtaque = 0;
    private final int DURACION_ATAQUE = 15;
    private final int COOLDOWN_ATAQUE = 20;
    public boolean esPatada = false;
    private int frameInvencible = 0;
    private final int DURACION_INVENCIBLE = 60;
    public int piezasRecogidas = 0;
    private String estado = "idle";


    private KeyController keyController;



    public Player(int x, int y, KeyController keyController) {
        super(x, y, 40, 60, 100, 2);
        this.keyController = keyController;
    }

    @Override
    public void actualizar() {
        if (!vivo) return;

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

        if (x < 0) {
            x = 0;
        }
        if (x > GamePanel.ANCHO - ancho) {
            x = GamePanel.ANCHO - ancho;
        }

    }

    public void manejarAtaque(){

        if (cooldownAtaque > 0) cooldownAtaque--;

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

    private void aplicarGravedad(){
        if (enAire) {
            velocidadY += GRAVEDAD;
            y += (int) velocidadY;

            if (y >= GamePanel.SUELO) {
                y = GamePanel.SUELO;
                velocidadY = 0;
                enAire = false;
            }
        }
    }


    private void actualizarInvencibilidad() {
        if (frameInvencible > 0) frameInvencible--;
    }


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
        if (frameInvencible == 0) {
            super.recibirDanio(cantidad);
            frameInvencible = DURACION_INVENCIBLE;
        }
    }


    public Rectangle getHitboxAtaque() {
        if (!atacando) return new Rectangle(0, 0, 0, 0);// no hay hitbox si no esta atacando

        int hitX;
        int hitY;
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

        if (miraDerecha){
            hitX = x + ancho;
        }else {
            hitX = x - hitAncho;
        }

        return new Rectangle(hitX, hitY, hitAncho, hitAlto);
    }



    @Override
    public void dibujar(Graphics2D g2d) {
        if (!vivo) return;

        // Efecto parpadeo al recibir daño
        if (frameInvencible > 0 && (frameInvencible / 6) % 2 == 0) return;

        Color colorCuerpo;
        switch (estado) {
            case "attack" -> colorCuerpo = new Color(255, 200, 0);
            case "hurt" -> colorCuerpo = new Color(255, 80, 80);
            case "jump" -> colorCuerpo = new Color(80, 180, 255);
            default -> colorCuerpo = new Color(60, 120, 220);
        }

        // Cuerpo
        g2d.setColor(colorCuerpo);
        g2d.fillRoundRect(x + 5, y + 20, ancho - 10, alto - 20, 6, 6);

        // Cabeza
        g2d.setColor(new Color(255, 220, 177));
        g2d.fillOval(x + 8, y - 10, ancho - 16, 30);

        // Ojo
        g2d.setColor(Color.BLACK);
        if (miraDerecha) g2d.fillOval(x + 18, y - 4, 5, 5);
        else             g2d.fillOval(x + ancho - 23, y - 4, 5, 5);

        // Piernas
        g2d.setColor(new Color(40, 80, 160));
        if (estado.equals("walk")) {
            long t      = System.currentTimeMillis() / 100;
            int  offset = (int)(Math.sin(t) * 5);
            g2d.fillRoundRect(x + 5,          y + alto - 22,          12, 22, 4, 4);
            g2d.fillRoundRect(x + ancho - 17, y + alto - 22 + offset, 12, 22, 4, 4);
        } else {
            g2d.fillRoundRect(x + 5,          y + alto - 22, 12, 22, 4, 4);
            g2d.fillRoundRect(x + ancho - 17, y + alto - 22, 12, 22, 4, 4);
        }

        // Puño al atacar
        if (atacando) {
            g2d.setColor(new Color(255, 220, 177));
            if (miraDerecha) g2d.fillOval(x + ancho - 5, y + 18, 18, 14);
            else             g2d.fillOval(x - 13,         y + 18, 18, 14);
        }

        dibujarBarraVida(g2d);
    }


}
