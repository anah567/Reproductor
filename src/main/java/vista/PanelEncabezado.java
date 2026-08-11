package vista;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.net.URL;

public class PanelEncabezado extends JPanel {

    private final JLabel etiquetaContador = new JLabel("0 canciones");
    private final JLabel titulo = new JLabel("Reproductor Musical");
    private final JLabel subtitulo = new JLabel("—  Camellos vs Enanos Music  —");
    private final JLabel tituloContador = new JLabel("Total de canciones");

    private final JPanel tarjetaContador = new JPanel();
    private final javax.swing.JButton botonModo = new javax.swing.JButton();

    public PanelEncabezado(VentanaPrincipal ventana) {
        setLayout(new BorderLayout());
        setBackground(TemaOscuro.FONDO_PRINCIPAL);
        setBorder(BorderFactory.createEmptyBorder(6, 22, 6, 22));
        setPreferredSize(new Dimension(0, 90));

        add(construirPanelIzquierdo(), BorderLayout.WEST);
        add(construirPanelDerecho(ventana), BorderLayout.EAST);
    }

    private JPanel construirPanelIzquierdo() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);

        JLabel imagenPersonajes = new JLabel();
        imagenPersonajes.setPreferredSize(new Dimension(150, 78));
        imagenPersonajes.setHorizontalAlignment(JLabel.CENTER);
        imagenPersonajes.setVerticalAlignment(JLabel.CENTER);

        URL recurso = getClass().getResource("/imagenes/camello_enano.png");

        if (recurso != null) {
            ImageIcon original = new ImageIcon(recurso);
            Image escalada = original.getImage().getScaledInstance(145, 75, Image.SCALE_SMOOTH);
            imagenPersonajes.setIcon(new ImageIcon(escalada));
        } else {
            imagenPersonajes.setText("♪");
            imagenPersonajes.setFont(new Font("Helvetica Neue", Font.PLAIN, 34));
            imagenPersonajes.setForeground(TemaOscuro.COLOR_ACENTO);
        }

        JPanel panelTitulos = new JPanel();
        panelTitulos.setLayout(new BoxLayout(panelTitulos, BoxLayout.Y_AXIS));
        panelTitulos.setOpaque(false);

        titulo.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        titulo.setForeground(TemaOscuro.TEXTO_PRINCIPAL);
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        subtitulo.setFont(new Font("Helvetica Neue", Font.BOLD, 17));
        subtitulo.setForeground(TemaOscuro.COLOR_ACENTO);
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);

        panelTitulos.add(titulo);
        panelTitulos.add(Box.createVerticalStrut(2));
        panelTitulos.add(subtitulo);

        JPanel contenedorTitulos = new JPanel(new GridBagLayout());
        contenedorTitulos.setOpaque(false);
        contenedorTitulos.add(panelTitulos);

        panel.add(imagenPersonajes, BorderLayout.WEST);
        panel.add(contenedorTitulos, BorderLayout.CENTER);

        return panel;
    }

    private JPanel construirPanelDerecho(VentanaPrincipal ventana) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 11));
        panel.setOpaque(false);

        construirTarjetaContador();

        botonModo.setText("☾  Modo oscuro");
        botonModo.setPreferredSize(new Dimension(155, 46));
        botonModo.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        botonModo.setFocusPainted(false);
        botonModo.setBorderPainted(false);
        botonModo.setOpaque(true);
        botonModo.setContentAreaFilled(true);
        botonModo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        botonModo.addActionListener(e -> {
            Tema.cambiarTema();
            ventana.actualizarTema();
        });

        panel.add(tarjetaContador);
        panel.add(botonModo);

        return panel;
    }

    private void construirTarjetaContador() {
        tarjetaContador.setLayout(new BoxLayout(tarjetaContador, BoxLayout.Y_AXIS));
        tarjetaContador.setPreferredSize(new Dimension(155, 64));
        tarjetaContador.setBackground(TemaOscuro.PANEL_DESTACADO);
        tarjetaContador.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaOscuro.BORDE, 1),
                BorderFactory.createEmptyBorder(7, 12, 7, 12)));

        tituloContador.setFont(new Font("Helvetica Neue", Font.PLAIN, 11));
        tituloContador.setForeground(TemaOscuro.TEXTO_SECUNDARIO);
        tituloContador.setAlignmentX(CENTER_ALIGNMENT);

        etiquetaContador.setFont(new Font("Helvetica Neue", Font.BOLD, 17));
        etiquetaContador.setForeground(TemaOscuro.COLOR_ACENTO);
        etiquetaContador.setAlignmentX(CENTER_ALIGNMENT);

        tarjetaContador.add(Box.createVerticalGlue());
        tarjetaContador.add(tituloContador);
        tarjetaContador.add(Box.createVerticalStrut(3));
        tarjetaContador.add(etiquetaContador);
        tarjetaContador.add(Box.createVerticalGlue());
    }

    public void actualizarContador(int total) {
        etiquetaContador.setText(total == 1 ? "1 canción" : total + " canciones");
    }

    public void actualizarTema() {

        if (Tema.esOscuro()) {

            setBackground(TemaOscuro.FONDO_PRINCIPAL);

            titulo.setForeground(TemaOscuro.TEXTO_PRINCIPAL);
            subtitulo.setForeground(TemaOscuro.COLOR_ACENTO);

            tarjetaContador.setBackground(TemaOscuro.PANEL_DESTACADO);
            tarjetaContador.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TemaOscuro.BORDE, 1),
                    BorderFactory.createEmptyBorder(7, 12, 7, 12)
            ));

            tituloContador.setForeground(TemaOscuro.TEXTO_SECUNDARIO);
            etiquetaContador.setForeground(TemaOscuro.COLOR_ACENTO);

            botonModo.setText("☾  Modo oscuro");
            botonModo.setBackground(TemaOscuro.PANEL_DESTACADO);
            botonModo.setForeground(TemaOscuro.TEXTO_PRINCIPAL);
            botonModo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        } else {

            setBackground(TemaClaro.FONDO_PRINCIPAL);

            titulo.setForeground(TemaClaro.TEXTO_PRINCIPAL);
            subtitulo.setForeground(TemaClaro.COLOR_ACENTO);

            tarjetaContador.setBackground(TemaClaro.PANEL_DESTACADO);
            tarjetaContador.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TemaClaro.BORDE, 1),
                    BorderFactory.createEmptyBorder(7, 12, 7, 12)
            ));

            tituloContador.setForeground(TemaClaro.TEXTO_SECUNDARIO);
            etiquetaContador.setForeground(TemaClaro.COLOR_ACENTO);

            botonModo.setText("☀  Modo claro");
            botonModo.setBackground(TemaClaro.PANEL_DESTACADO);
            botonModo.setForeground(TemaClaro.TEXTO_PRINCIPAL);
            botonModo.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        }

        repaint();
        revalidate();
    }
}