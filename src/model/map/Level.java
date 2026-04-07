package model.map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.entities.ZombieProfessor;
import model.entities.Enemy;
import model.entities.ZombieStudent;
import model.items.MachinePiece;
import view.GamePanel;

public class Level {

    private int numero;
    private String nombre;
    private String rutaFondo;

    private BufferedImage imagenFondo;

    private List<Enemy> enemigos;
    private List<MachinePiece> piezas;

    private boolean completado = false;

    public Level(int numero, String nombre, String rutaFondo) {
        this.numero = numero;
        this.nombre = nombre;
        this.rutaFondo = rutaFondo;
        this.enemigos = new ArrayList<>();
        this.piezas = new ArrayList<>();

        cargarFondo();
        configurarNivel();

    }


    //cargar imagen de fondo

    private void cargarFondo(){
        try{
            imagenFondo = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(rutaFondo)));
        }catch (IOException | NullPointerException e){
            System.out.println("Error al cargar fondo: " + e.getMessage());
            imagenFondo = null;
        }


    }

    //configurar enemigos y piezas segun el numero de nivel
    private void configurarNivel(){

     switch (numero){
         case 1 -> configurarNivel1();
         case 2 -> configurarNivel2();
         case 3 -> configurarNivel3();
     }
    }

    // ── NIVEL 1: Laboratorio ──────────────────────────────────────────────────
    // Nivel introductorio — pocos enemigos, facil
    // Solo hay zombies estudiantes, ningún profesor
    private void configurarNivel1() {
        enemigos.add(new ZombieStudent(300, GamePanel.SUELO));
        enemigos.add(new ZombieStudent(450, GamePanel.SUELO));
        enemigos.add(new ZombieStudent(600, GamePanel.SUELO));
        enemigos.add(new ZombieStudent(720, GamePanel.SUELO));

        piezas.add(new MachinePiece(200, GamePanel.SUELO - 40, 1));
        piezas.add(new MachinePiece(580, GamePanel.SUELO - 40, 2));
    }

    // ── NIVEL 2: Pasillos de la facultad ─────────────────────────────────────
    // Nivel intermedio — mas enemigos y aparece el primer profesor
    private void configurarNivel2() {
        enemigos.add(new ZombieStudent(280,  GamePanel.SUELO));
        enemigos.add(new ZombieStudent(420,  GamePanel.SUELO));
        enemigos.add(new ZombieStudent(560,  GamePanel.SUELO));
        enemigos.add(new ZombieStudent(700,  GamePanel.SUELO));
        enemigos.add(new ZombieProfessor(480, GamePanel.SUELO));
        enemigos.add(new ZombieProfessor(660, GamePanel.SUELO));

        piezas.add(new MachinePiece(350, GamePanel.SUELO - 40, 3));
        piezas.add(new MachinePiece(620, GamePanel.SUELO - 40, 4));
    }

    // ── NIVEL 3: Salon de clases ──────────────────────────────────────────────
    // Nivel final — mas dificil, dos profesores
    private void configurarNivel3() {
        enemigos.add(new ZombieStudent(300,  GamePanel.SUELO));
        enemigos.add(new ZombieStudent(450,  GamePanel.SUELO));
        enemigos.add(new ZombieStudent(600,  GamePanel.SUELO));
        enemigos.add(new ZombieStudent(720,  GamePanel.SUELO));
        enemigos.add(new ZombieProfessor(380, GamePanel.SUELO));
        enemigos.add(new ZombieProfessor(540, GamePanel.SUELO));
        enemigos.add(new ZombieProfessor(680, GamePanel.SUELO));

        piezas.add(new MachinePiece(400, GamePanel.SUELO - 40, 5));
    }


    public void dibujarFondo(Graphics2D g2d) {
        if (imagenFondo != null) {
            g2d.drawImage(imagenFondo, 0, 0, GamePanel.ANCHO, GamePanel.ALTO, null);
        } else {
            // Fondo de respaldo con color diferente por nivel
            Color color = switch (numero) {
                case 1 -> new Color(10, 30, 10);  // verde oscuro — laboratorio
                case 2 -> new Color(20, 20, 40);  // azul oscuro  — pasillos
                default -> new Color(30, 10, 10);  // rojo oscuro  — salon
            };

            g2d.setColor(color);
            g2d.fillRect(0, 0, GamePanel.ANCHO, GamePanel.ALTO);

            // Suelo de respaldo
            g2d.setColor(new Color(60, 40, 40));
            g2d.fillRect(0, GamePanel.SUELO + 60, GamePanel.ANCHO, 200);
            g2d.setColor(new Color(100, 60, 60));
            g2d.fillRect(0, GamePanel.SUELO + 57, GamePanel.ANCHO, 4);
        }
    }

    // ── Dibujar el nombre del nivel en pantalla ───────────────────────────────
    public void dibujarNombre(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - 120, 10, 240, 30, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial Black", Font.BOLD, 14));
        String texto = "Nivel " + numero + ": " + nombre;
        int    tw    = g2d.getFontMetrics().stringWidth(texto);
        g2d.drawString(texto, GamePanel.ANCHO / 2 - tw / 2, 31);
    }

    // ── Verificar si todos los enemigos estan muertos ─────────────────────────
    // El nivel se completa cuando no queda ningun enemigo vivo
    public boolean todosEnemigosDerrotados() {
        for (Enemy e : enemigos) {
            if (e.estaVivo()) return false;
        }
        return true;
    }

    // Verifica si todas las piezas del nivel fueron recogidas
    public boolean todasPiezasRecogidas() {
        for (MachinePiece p : piezas) {
            if (!p.recogida) return false;
        }
        return true;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public List<Enemy> getEnemigos(){
        return enemigos;
    }
    public List<MachinePiece> getPiezas(){
        return piezas;
    }
    public int getNumero(){
        return numero;
    }
    public String getNombre(){
        return nombre;
    }
    public boolean isCompletado(){
        return completado;
    }
    public void setCompletado(){
        completado = true;
    }


}
