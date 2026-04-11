package controller;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    private Map<String, Clip> clips = new HashMap<>();
    private static SoundManager instancia;

    public static SoundManager getInstance() {
        if (instancia == null) {
            instancia = new SoundManager();
        }
        return instancia;
    }

    private SoundManager() {

        cargar("musica", "sounds/musica_fondo.wav");
        cargar("zombie", "sounds/zombie_movimiento.wav");

        cargar("golpe", "sounds/golpe.wav");
        cargar("recoger", "sounds/recoger.wav");
        cargar("muerte_zombie", "sounds/muerte_zombie.wav");
        cargar("danio_jugador", "sounds/danio_jugador.wav");
        cargar("game_over", "sounds/game_over.wav");
        cargar("victoria", "sounds/victoria.wav");
    }

    // Carga un archivo WAV en memoria y lo guarda con un nombre clave
    private void cargar(String nombre, String ruta) {
        try {
            var url = getClass().getClassLoader().getResource(ruta);
            if (url == null) {
                System.err.println("No se encontro el sonido: " + ruta);
                return;
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clips.put(nombre, clip);
            System.out.println("Sonido cargado: " + ruta);
        } catch (UnsupportedAudioFileException | IOException |
                 LineUnavailableException e) {
            System.err.println("Error cargando " + ruta + ": " + e.getMessage());
        }
    }

    // Reproduce un sonido una sola vez desde el inicio
    public void reproducir(String nombre) {
        Clip clip = clips.get(nombre);
        if (clip == null) return;
        clip.setFramePosition(0); // rebobinar al inicio
        clip.start();
    }

    // Reproduce un sonido en bucle infinito (para musica de fondo)
    public void reproducirLoop(String nombre) {
        Clip clip = clips.get(nombre);
        if (clip == null) return;
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Detiene un sonido que este reproduciendose
    public void detener(String nombre) {
        Clip clip = clips.get(nombre);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // Verifica si un sonido esta reproduciendose actualmente
    public boolean estaReproduciendo(String nombre) {
        Clip clip = clips.get(nombre);
        return clip != null && clip.isRunning();
    }

}
