package model.entities;

import controller.SoundManager;
import view.GamePanel;

public abstract class Enemy extends Entity{


    protected int radioDeteccion;
    protected int radioAtaque;
    protected int danio;
    protected int puntos;
    public boolean danioAplicado = false;
    public String estadoIA = "patrullando";


    protected int posicionInicialX;
    protected int rangoPatrulla = 120;
    protected int direccionPatrulla = 1;
    public int cooldownAtaque  = 0;
    protected int intervaloAtaque = 90;


    public Enemy(int x, int y, int ancho, int alto, int vidaMaxima, int velocidad, int radioDeteccion, int radioAtaque, int danio, int puntos) {
        super(x, y, ancho, alto, vidaMaxima, velocidad);
        this.radioDeteccion  = radioDeteccion;
        this.radioAtaque     = radioAtaque;
        this.danio           = danio;
        this.puntos          = puntos;
        this.posicionInicialX = x;
    }


    public void actualizar(int jugadorX, int jugadorY) {
        if (!vivo) return;
        actualizarIA(jugadorX, jugadorY);
    }


    private void actualizarIA(int jugadorX, int jugadorY) {
        int distanciaX = Math.abs(jugadorX - x);

        if (distanciaX <= radioAtaque) {
            estadoIA = "atacando";
            if (cooldownAtaque == 0) {
                cooldownAtaque = intervaloAtaque;
                danioAplicado = true;
                SoundManager.getInstance().reproducir("zombie");

            }
        } else if (distanciaX <= radioDeteccion) {
            estadoIA = "persiguiendo";
            danioAplicado = false;
            perseguir(jugadorX);
        } else {
            estadoIA = "patrullando";
            danioAplicado = false;
            patrullar();

        }

        if (cooldownAtaque > 0) cooldownAtaque--;

    }

    private void perseguir(int jugadorX) {
        if (jugadorX < x) {
            x -= velocidad;
            miraDerecha = false;
        } else {
            x += velocidad;
            miraDerecha = true;
        }
        if (x < 0){
            x = 0;
        }
        if (x > GamePanel.ANCHO - ancho){
            x = GamePanel.ANCHO - ancho;
        }
    }

    private void patrullar() {
        x += velocidad * direccionPatrulla;
        miraDerecha = direccionPatrulla == 1;

        if (x > posicionInicialX + rangoPatrulla) direccionPatrulla = -1;
        if (x < posicionInicialX - rangoPatrulla) direccionPatrulla =  1;
    }

    public boolean puedeAtacar() {
        if (estadoIA.equals("atacando") && cooldownAtaque == 0) {
            cooldownAtaque = intervaloAtaque;
            return true;
        }
        return false;
    }

    public int getDanio()  {
        return danio;
    }
    public int getPuntos() {
        return puntos;
    }
    @Override
    public void actualizar() {}



}
