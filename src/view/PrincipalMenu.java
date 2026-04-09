package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PrincipalMenu extends JPanel {



    private static final Color colorFondo = new Color(0, 14, 92);
    private static final Color colorTitulo = new Color(255, 255, 255);
    private static final Color colorSubtitulo = new Color(200, 200, 200);
    private static final Color colorBtnFondo = new Color(40, 40, 80);
    private static final Color colorBtnTexto = Color.WHITE;


    private Principal ventana;

    public PrincipalMenu(Principal ventana) {
        this.ventana = ventana;

        setPreferredSize(new Dimension(Principal.screenWidth, Principal.screenHeigth));
        setBackground(colorFondo);
        setLayout(new BorderLayout());

        add(crearPanelTitulo(), BorderLayout.NORTH);
        add(crearPanelBotones(), BorderLayout.CENTER);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setBackground(colorFondo);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createRaisedBevelBorder());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setBorder(BorderFactory.createEmptyBorder(60, 20, 20, 20));

        JLabel titulo = new JLabel("DIMENSION UNET", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial Black", Font.BOLD, 48));
        titulo.setForeground(colorTitulo);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("Mas peligroso que cualquier parcial", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.BOLD, 16));
        subtitulo.setForeground(colorSubtitulo);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitulo);

        return panel;
    }


    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setBackground(colorFondo);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        JButton btnJugar    = crearBoton("JUGAR");
        JButton btnControles = crearBoton("CONTROLES");
        JButton btnSalir    = crearBoton("SALIR");



        btnJugar.addActionListener((ActionEvent e) -> iniciarJuego());
        btnControles.addActionListener((ActionEvent e) -> mostrarControles());
        btnSalir.addActionListener((ActionEvent e) -> System.exit(0));

        panel.add(btnJugar);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnControles);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnSalir);

        return panel;
    }


    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setForeground(colorBtnTexto);
        btn.setBackground(colorBtnFondo);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setPreferredSize(new Dimension(250, 50));
        return btn;
    }


    private void iniciarJuego() {
        ventana.iniciarJuego();
    }

    private void mostrarControles() {

        JOptionPane.showMessageDialog(this,          """
                            CONTROLES
                ─────────────────────────────────
                Mover izquierda  ->  Tecla A  o  flecha izquierda
                Mover derecha    ->  Tecla D  o  flecha derecha
                Saltar           ->  Tecla W  o  flecha arriba
                Ataque debil     ->  J
                Ataque fuerte    ->  K
                Pausa            ->  ESC
                """, "Controles", JOptionPane.PLAIN_MESSAGE);
    }


}

