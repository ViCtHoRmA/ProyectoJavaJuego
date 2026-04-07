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



    private void cargarFondo(){
        try{
            imagenFondo = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(rutaFondo)));
        }catch (IOException | NullPointerException e){
            System.out.println("Error al cargar fondo: " + e.getMessage());
            imagenFondo = null;
        }


    }


    private void configurarNivel(){

     switch (numero){
         case 1 -> configurarNivel1();
         case 2 -> configurarNivel2();
         case 3 -> configurarNivel3();
     }
    }


    private void configurarNivel1() {
        enemigos.add(new ZombieStudent(300, GamePanel.SUELO));
        enemigos.add(new ZombieStudent(450, GamePanel.SUELO));
        enemigos.add(new ZombieStudent(600, GamePanel.SUELO));
        enemigos.add(new ZombieStudent(720, GamePanel.SUELO));

        piezas.add(new MachinePiece(200, GamePanel.SUELO - 40, 1));
        piezas.add(new MachinePiece(580, GamePanel.SUELO - 40, 2));
    }


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
        g2d.drawImage(imagenFondo, 0, 0, GamePanel.ANCHO, GamePanel.ALTO, null);

    }


    public void dibujarNombre(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRoundRect(GamePanel.ANCHO / 2 - 120, 10, 240, 30, 10, 10);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial Black", Font.BOLD, 14));
        String texto = "Nivel " + numero + ": " + nombre;
        int tw = g2d.getFontMetrics().stringWidth(texto);
        g2d.drawString(texto, GamePanel.ANCHO / 2 - tw / 2, 31);
    }


    public boolean todosEnemigosDerrotados() {
        for (Enemy e : enemigos) {
            if (e.estaVivo()) return false;
        }
        return true;
    }

    public boolean todasPiezasRecogidas() {
        for (MachinePiece p : piezas) {
            if (!p.recogida) return false;
        }
        return true;
    }


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
