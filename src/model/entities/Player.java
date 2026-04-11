package model.entities;

import controller.KeyController;
import controller.SoundManager;
import view.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    private BufferedImage[] framesCaminarDerecha;
    private BufferedImage[] framesCaminarIzquierda;
    private BufferedImage[] framesAtacarDerecha;
    private BufferedImage[] framesAtacarIzquierda;
    private BufferedImage[] framesSaltarDerecha;
    private BufferedImage[] framesSaltarIzquierda;
    private BufferedImage[] framesIdle;


    private KeyController keyController;
    private int sueloY = GamePanel.SUELO_NIVEL1;


    public Player(int x, int y, KeyController keyController) {
        super(x, y, 80, 120, 100, 2);
        this.keyController = keyController;
        framesCaminarDerecha = cargarFrames("jugador/caminar_derecha");
        framesCaminarIzquierda = cargarFrames("jugador/caminar_izquierda");
        framesAtacarDerecha = cargarFrames("jugador/atacar_derecha");
        framesAtacarIzquierda = cargarFrames("jugador/atacar_izquierda");
        framesSaltarDerecha = cargarFrames("jugador/saltar_derecha");
        framesSaltarIzquierda = cargarFrames("jugador/saltar_izquierda");
        framesIdle = cargarFrames("jugador/idle");

        framesActuales = framesCaminarDerecha;



    }

    @Override
    public void actualizar() {
        if (!vivo) return;

        movimientoPersonaje();
        ataquePersonaje();
        aplicarGravedad();
        actualizarInvencibilidad();
        actualizarEstado();
        actualizarAnimacionPersonaje();

    }

    public void movimientoPersonaje(){

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

    public void ataquePersonaje(){

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

            if (y >= sueloY - alto) {
                y = sueloY - alto;
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
            SoundManager.getInstance().reproducir("danio_jugador");
        }
    }


    public Rectangle getHitboxAtaque() {
        if (!atacando) return new Rectangle(0, 0, 0, 0);

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


    private void actualizarAnimacionPersonaje(){
        BufferedImage[] nuevos;
        if (atacando){
            nuevos = miraDerecha ? framesAtacarDerecha : framesAtacarIzquierda;
        } else if (enAire) {
            nuevos = miraDerecha ? framesSaltarDerecha : framesSaltarIzquierda;

        }else if (keyController.moverDerecha || keyController.moverIzquierda) {
            nuevos = miraDerecha ? framesCaminarDerecha : framesCaminarIzquierda;
        }else {
            nuevos = framesIdle;
        }

        cambiarAnimacion(nuevos);
        actualizarAnimacion();
    }


    @Override
    public void dibujar(Graphics2D g2d) {
        if (!vivo) return;


        if (framesActuales != null && framesActuales[frameActual] != null){
            g2d.drawImage(framesActuales[frameActual], x, y, ancho, alto, null);
        }else {
            Color colorCuerpo;
            switch (estado){
                case "attack" -> colorCuerpo = new Color(255,200,0);
                case "hurt" -> colorCuerpo = new Color(255,80,80);
                case "jump" -> colorCuerpo = new Color(80,180,255);
                default -> colorCuerpo = new Color(60,120,220);
            }
            g2d.setColor(colorCuerpo);
            g2d.fillRoundRect(x + 5, y + 20, ancho - 10, alto - 20, 6,6);
            g2d.setColor(new Color(255,220,177));
            g2d.fillOval(x + 8, y - 10, ancho - 16, 30);

        }
    }

    public void setSuelo(int sueloY){
        this.sueloY = sueloY;
    }


}
