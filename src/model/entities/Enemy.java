package model.entities;

import view.GamePanel;

public abstract class Enemy extends Entity{

    // ── Comportamiento ────────────────────────────────────────────────────────
    // distancia a la que el enemigo "ve" al jugador y empieza a perseguirlo
    protected int radioDeteccion;
    // distancia a la que el enemigo puede golpear al jugador
    protected int radioAtaque;
    // cuanto daño hace al jugador por golpe
    protected int danio;
    // puntos que da al morir
    protected int puntos;
    public boolean danioAplicado = false;

    // ── Estado de la IA ───────────────────────────────────────────────────────
    // "patrullando" = camina de un lado a otro sin ver al jugador
    // "persiguiendo" = detecto al jugador y va hacia el
    // "atacando"    = esta lo suficientemente cerca para golpear
    public String estadoIA = "patrullando";

    // ── Patrullaje ────────────────────────────────────────────────────────────
    // El enemigo patrulla entre dos puntos: su posicion inicial +/- rangoPatrulla
    protected int posicionInicialX;
    protected int rangoPatrulla = 120; // pixeles que patrulla a cada lado
    protected int direccionPatrulla = 1; // 1 = derecha, -1 = izquierda

    // ── Cooldown de ataque ────────────────────────────────────────────────────
    public int cooldownAtaque  = 0;
    protected int intervaloAtaque = 90; // frames entre cada ataque del enemigo

    // ── Constructor ───────────────────────────────────────────────────────────
    public Enemy(int x, int y, int ancho, int alto, int vidaMaxima, int velocidad, int radioDeteccion, int radioAtaque, int danio, int puntos) {
        super(x, y, ancho, alto, vidaMaxima, velocidad);
        this.radioDeteccion  = radioDeteccion;
        this.radioAtaque     = radioAtaque;
        this.danio           = danio;
        this.puntos          = puntos;
        this.posicionInicialX = x; // guarda donde empezo para patrullar
    }

    // ── actualizar() recibe la posicion del jugador ───────────────────────────
    // Los enemigos necesitan saber donde esta el jugador para perseguirlo.
    // Este metodo lo llamaremos desde GameController pasandole las coordenadas.
    public void actualizar(int jugadorX, int jugadorY) {
        if (!vivo) return;

        actualizarIA(jugadorX, jugadorY);
    }

    // ── Logica de IA ──────────────────────────────────────────────────────────
    private void actualizarIA(int jugadorX, int jugadorY) {
        int distanciaX = Math.abs(jugadorX - x);

        // Si esta cerca lo suficiente, dejar de moverse y atacar
        if (distanciaX <= radioAtaque) {
            estadoIA = "atacando";
            // Aplicar daño directamente aqui mismo sin esperar CollisionController
            if (cooldownAtaque == 0) {
                cooldownAtaque = intervaloAtaque;
                danioAplicado = true; // flag para que CollisionController lo lea
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

    // ── Perseguir al jugador ──────────────────────────────────────────────────
    private void perseguir(int jugadorX) {
        if (jugadorX < x) {
            x           -= velocidad;
            miraDerecha  = false;
        } else {
            x          += velocidad;
            miraDerecha = true;
        }
        // Mantener dentro de pantalla
        if (x < 0)                       x = 0;
        if (x > GamePanel.ANCHO - ancho) x = GamePanel.ANCHO - ancho;
    }

    // ── Patrullar de un lado a otro ───────────────────────────────────────────
    private void patrullar() {
        x += velocidad * direccionPatrulla;
        miraDerecha = direccionPatrulla == 1;

        // Cuando llega al limite del rango, da la vuelta
        if (x > posicionInicialX + rangoPatrulla) direccionPatrulla = -1;
        if (x < posicionInicialX - rangoPatrulla) direccionPatrulla =  1;
    }

    // ── El enemigo puede atacar si el cooldown llego a cero ───────────────────
    public boolean puedeAtacar() {
        if (estadoIA.equals("atacando") && cooldownAtaque == 0) {
            cooldownAtaque = intervaloAtaque;
            return true;
        }
        return false;
    }

    public int getDanio()  { return danio; }
    public int getPuntos() { return puntos; }

    // actualizar() sin argumentos — requerido por Entity, no lo usamos en enemigos
    @Override
    public void actualizar() {
        // No hacemos nada aquí, usamos actualizar(int jugadorX, int jugadorY) en su lugar
    }



}
