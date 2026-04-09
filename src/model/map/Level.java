package model.map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.entities.*;
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
        int suelo = GamePanel.SUELO_NIVEL1;

        enemigos.add(new ZombieStudent(300, suelo - 115));
        enemigos.add(new ZombieStudent(450, suelo - 115));
        enemigos.add(new ZombieStudent(600, suelo - 115));
        enemigos.add(new ZombieStudent(750, suelo - 115));
        enemigos.add(new ZombieProfessor(650, suelo - 130));

        piezas.add(new MachinePiece(250, suelo - 175, 1));
        piezas.add(new MachinePiece(650, suelo - 175, 2));
    }


    private void configurarNivel2() {
        int suelo = GamePanel.SUELO_NIVEL2;

        enemigos.add(new ZombieStudent(300, suelo - 115));
        enemigos.add(new ZombieStudent(440,suelo - 115));
        enemigos.add(new ZombieStudent(580, suelo - 115));
        enemigos.add(new ZombieStudent(720, suelo - 115));
        enemigos.add(new ZombieProfessor(380, suelo - 130));
        enemigos.add(new ZombieProfessor(650, suelo - 130));

        piezas.add(new MachinePiece(280, suelo - 175, 3));
        piezas.add(new MachinePiece(660, suelo - 175, 4));

    }


    private void configurarNivel3() {
        int suelo = GamePanel.SUELO_NIVEL3;

        enemigos.add(new ZombieStudent(300, suelo - 115));
        enemigos.add(new ZombieStudent(440,suelo - 115));
        enemigos.add(new ZombieStudent(580,  suelo - 115));
        enemigos.add(new ZombieStudent(720,suelo - 115));
        enemigos.add(new ZombieProfessor(370, suelo - 115));
        enemigos.add(new ZombieProfessor(550, suelo - 115));
        enemigos.add(new ZombieProfessor(730, suelo - 115));

        piezas.add(new MachinePiece(430, suelo - 175, 5));
    }


    public void dibujarFondo(Graphics2D g2d) {
        g2d.drawImage(imagenFondo, 0, 0, GamePanel.ANCHO, GamePanel.ALTO, null);

    }


    public void dibujarNombre(Graphics2D g2d) {
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
