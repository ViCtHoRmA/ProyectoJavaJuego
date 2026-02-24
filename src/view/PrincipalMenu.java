package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuPrincipal extends JPanel {

    // Colores temáticos del juego
    private static final Color colorFondo = new Color(10, 10, 30);
    private static final Color colorTitulo = new Color(180, 0, 0);
    private static final Color colorSubtitulo = new Color(200, 200, 200);
    private static final Color colorBtnFondo = new Color(40, 40, 80);
    private static final Color colorBtnHover = new Color(180, 0, 0);
    private static final Color colorBtnTexto = Color.WHITE;

    public MenuPrincipal() {
        setPreferredSize(new Dimension(Principal.SCREEN_WIDTH, Principal.SCREEN_HEIGHT));
        setBackground(colorFondo);
        setLayout(new BorderLayout());

        add(crearPanelTitulo(), BorderLayout.NORTH);
        add(crearPanelBotones(), BorderLayout.CENTER);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setBackground(colorFondo);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(60, 20, 20, 20));

        JLabel titulo = new JLabel("DIMENSION UNET", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial Black", Font.BOLD, 48));
        titulo.setForeground(colorTitulo);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("¿Puedes volver a tu dimensión?", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitulo.setForeground(colorSubtitulo);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitulo);

        return panel;
    }

    // ── Panel central con los botones del menú ──────────────────────────────
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel();
        panel.setBackground(colorFondo);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        JButton btnJugar    = crearBoton("JUGAR");
        JButton btnControles = crearBoton("CONTROLES");
        JButton btnSalir    = crearBoton("SALIR");

        // Acción: Jugar — por ahora muestra un mensaje, luego lanzará GamePanel
        btnJugar.addActionListener((ActionEvent e) -> iniciarJuego());

        // Acción: Controles
        btnControles.addActionListener((ActionEvent e) -> mostrarControles());

        // Acción: Salir
        btnSalir.addActionListener((ActionEvent e) -> System.exit(0));

        panel.add(btnJugar);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnControles);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnSalir);

        return panel;
    }

    // ── Fábrica de botones con estilo personalizado ─────────────────────────
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setForeground(colorBtnTexto);
        btn.setBackground(colorBtnFondo);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setPreferredSize(new Dimension(250, 50));

        // Efecto hover: cambia color al pasar el mouse
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(colorBtnHover);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(colorBtnFondo);
            }
        });

        return btn;
    }

    // ── Acciones de los botones ─────────────────────────────────────────────
    private void iniciarJuego() {
        // TODO: En el siguiente paso reemplazamos esto con el GamePanel real
        JOptionPane.showMessageDialog(
                this,
                "¡El juego está en construcción!\nPróximo paso: crear el GamePanel y el Game Loop.",
                "Dimension UNET",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarControles() {

        JOptionPane.showMessageDialog(this,          """
                            CONTROLES
                ─────────────────────────────────
                Mover izquierda  →  Tecla A  o  ←
                Mover derecha    →  Tecla D  o  →
                Saltar           →  Tecla W  o  ↑
                Atacar (puño)    →  J
                Atacar (patada)  →  K
                Recoger ítem     →  E
                Pausa            →  ESC
                """, "Controles", JOptionPane.PLAIN_MESSAGE);
    }


}

