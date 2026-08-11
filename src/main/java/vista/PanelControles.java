package vista;

import modelo.Cancion;
import vista.TemaManager;
import vista.TemaOscuro;
import vista.VentanaPrincipal;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;

public class PanelControles extends JPanel {

    private final JLabel portadaMini = new JLabel("♫", SwingConstants.CENTER);
    private final JLabel etiquetaNombre = new JLabel("Ninguna canción");
    private final JLabel etiquetaArtista = new JLabel("");
    private final JLabel etiquetaTranscurrido = new JLabel("0:00");
    private final JLabel etiquetaDuracion = new JLabel("0:00");
    private final JLabel etiquetaVolumen = new JLabel("Volumen");

    private final JProgressBar barraProgreso = new JProgressBar();
    private final JSlider sliderVolumen = new JSlider(0, 100, 70);

    private final JButton botonAnterior = crearBotonTexto("Anterior");
    private final JButton botonSiguiente = crearBotonTexto("Siguiente");
    private final JButton botonRepetir = crearBotonTexto("Reiniciar");
    private final JButton botonPausar = crearBotonTexto("Pausar");
    private final BotonCircular botonReproducir = new BotonCircular();

    private final String[] modos = {
            "Aleatorio",
            "Orden de llegada",
            "Orden alfabetico"
    };

    private final JComboBox<String> selectorModo = new JComboBox<>(modos);

    private boolean actualizandoModo = false;
    private boolean reproduciendo = false;

    public PanelControles(VentanaPrincipal ventana) {
        setLayout(new BorderLayout(24, 0));
        setBackground(TemaManager.panelSecundario());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaManager.borde(), 1),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));
        setPreferredSize(new Dimension(0, 112));

        add(construirInformacionCancion(ventana), BorderLayout.WEST);
        add(construirCentro(), BorderLayout.CENTER);
        add(construirVolumen(), BorderLayout.EAST);

        botonPausar.addActionListener(e -> {
            ventana.pausar();
            reproduciendo = false;
            botonReproducir.setTexto("▶");
        });

        botonAnterior.addActionListener(e -> ventana.anterior());
        botonSiguiente.addActionListener(e -> ventana.siguiente());
        botonRepetir.addActionListener(e -> ventana.reiniciarCancion());

        botonReproducir.addActionListener(e -> {
            if (reproduciendo) {
                ventana.pausar();
                botonReproducir.setTexto("▶");
            } else {
                ventana.reproducir();
                botonReproducir.setTexto("Ⅱ");
            }

            reproduciendo = !reproduciendo;
        });

        mostrarVacio();
        actualizarTema();
    }

    private JPanel construirInformacionCancion(VentanaPrincipal ventana) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(280, 82));

        portadaMini.setPreferredSize(new Dimension(72, 72));
        portadaMini.setMinimumSize(new Dimension(72, 72));
        portadaMini.setMaximumSize(new Dimension(72, 72));
        portadaMini.setOpaque(true);
        portadaMini.setFont(new Font("Helvetica Neue", Font.PLAIN, 34));
        portadaMini.setBorder(BorderFactory.createLineBorder(TemaManager.borde(), 1));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        etiquetaNombre.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        etiquetaNombre.setAlignmentX(LEFT_ALIGNMENT);

        etiquetaArtista.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
        etiquetaArtista.setAlignmentX(LEFT_ALIGNMENT);

        configurarSelectorModo(ventana);

        textos.add(Box.createVerticalGlue());
        textos.add(etiquetaNombre);
        textos.add(Box.createVerticalStrut(4));
        textos.add(etiquetaArtista);
        textos.add(Box.createVerticalStrut(7));
        textos.add(selectorModo);
        textos.add(Box.createVerticalGlue());

        panel.add(portadaMini, BorderLayout.WEST);
        panel.add(textos, BorderLayout.CENTER);

        return panel;
    }

    private JPanel construirCentro() {
        JPanel panel = new JPanel(new BorderLayout(0, 7));
        panel.setOpaque(false);

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        controles.setOpaque(false);

        controles.add(botonPausar);
        controles.add(botonAnterior);
        controles.add(botonReproducir);
        controles.add(botonSiguiente);
        controles.add(botonRepetir);

        JPanel progreso = new JPanel(new BorderLayout(12, 0));
        progreso.setOpaque(false);

        configurarTiempo(etiquetaTranscurrido);
        configurarTiempo(etiquetaDuracion);

        barraProgreso.setMinimum(0);
        barraProgreso.setMaximum(1);
        barraProgreso.setValue(0);
        barraProgreso.setBorderPainted(false);
        barraProgreso.setStringPainted(false);
        barraProgreso.setPreferredSize(new Dimension(0, 7));

        progreso.add(etiquetaTranscurrido, BorderLayout.WEST);
        progreso.add(barraProgreso, BorderLayout.CENTER);
        progreso.add(etiquetaDuracion, BorderLayout.EAST);

        panel.add(controles, BorderLayout.CENTER);
        panel.add(progreso, BorderLayout.SOUTH);

        return panel;
    }

    private void configurarSelectorModo(VentanaPrincipal ventana) {
        selectorModo.setFont(new Font("Helvetica Neue", Font.BOLD, 11));
        selectorModo.setPreferredSize(new Dimension(150, 28));
        selectorModo.setMaximumSize(new Dimension(150, 28));
        selectorModo.setFocusable(false);
        selectorModo.setAlignmentX(LEFT_ALIGNMENT);

        selectorModo.addActionListener(e -> {
            if (actualizandoModo) {
                return;
            }

            String modoSeleccionado = (String) selectorModo.getSelectedItem();
            ventana.cambiarModoDesdeSelector(modoSeleccionado);
        });
    }

    private JPanel construirVolumen() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 28));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(230, 82));

        etiquetaVolumen.setFont(TemaOscuro.FUENTE_SUBTITULO);

        sliderVolumen.setOpaque(false);
        sliderVolumen.setPreferredSize(new Dimension(125, 22));

        panel.add(etiquetaVolumen);
        panel.add(sliderVolumen);

        return panel;
    }

    private JButton crearBotonTexto(String texto) {
        JButton boton = new JButton(texto);

        boton.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(true);
        boton.setOpaque(true);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(105, 36));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        return boton;
    }

    private void configurarTiempo(JLabel etiqueta) {
        etiqueta.setFont(new Font("Helvetica Neue", Font.PLAIN, 12));
    }

    public void mostrarCancion(Cancion cancion) {
        if (cancion == null) {
            mostrarVacio();
            return;
        }

        etiquetaNombre.setText(cancion.getNombre());
        etiquetaArtista.setText(cancion.getArtista());
        etiquetaDuracion.setText(formatear(cancion.getDuracionSegundos()));

        actualizarPortada(cancion.getRutaPortada());
    }

    public void mostrarVacio() {
        etiquetaNombre.setText("Ninguna canción");
        etiquetaArtista.setText("");
        etiquetaTranscurrido.setText("0:00");
        etiquetaDuracion.setText("0:00");
        barraProgreso.setValue(0);

        portadaMini.setIcon(null);
        portadaMini.setText("♫");

        reproduciendo = false;
        botonReproducir.setTexto("▶");
    }

    public void actualizarProgreso(int segundosTranscurridos, int duracionTotal) {
        barraProgreso.setMaximum(Math.max(duracionTotal, 1));
        barraProgreso.setValue(Math.min(segundosTranscurridos, duracionTotal));

        etiquetaTranscurrido.setText(formatear(segundosTranscurridos));
        etiquetaDuracion.setText(formatear(duracionTotal));
    }

    public void reiniciarProgreso() {
        barraProgreso.setValue(0);
        etiquetaTranscurrido.setText("0:00");
    }

    public void actualizarModoActual(String nombreModo) {
        actualizandoModo = true;

        if ("Orden alfabético".equalsIgnoreCase(nombreModo)) {
            selectorModo.setSelectedItem("Orden alfabetico");
        } else {
            selectorModo.setSelectedItem(nombreModo);
        }

        actualizandoModo = false;
    }

    public void establecerModoSeleccionado(String nombreModo) {
        actualizarModoActual(nombreModo);
    }

    public void habilitarBotonAnterior(boolean habilitado) {
        botonAnterior.setEnabled(habilitado);
    }

    private void actualizarPortada(String ruta) {
        if (ruta != null && !ruta.isBlank() && new File(ruta).exists()) {
            ImageIcon original = new ImageIcon(ruta);
            Image escalada = original.getImage().getScaledInstance(68, 68, Image.SCALE_SMOOTH);

            portadaMini.setIcon(new ImageIcon(escalada));
            portadaMini.setText("");
        } else {
            portadaMini.setIcon(null);
            portadaMini.setText("♫");
        }
    }

    private String formatear(int segundos) {
        int minutos = segundos / 60;
        int resto = segundos % 60;

        return String.format("%d:%02d", minutos, resto);
    }

    public void actualizarTema() {
        setBackground(TemaManager.panelSecundario());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaManager.borde(), 1),
                BorderFactory.createEmptyBorder(10, 16, 10, 16)));

        portadaMini.setBackground(TemaManager.panelDestacado());
        portadaMini.setForeground(TemaManager.textoSecundario());
        portadaMini.setBorder(BorderFactory.createLineBorder(TemaManager.borde(), 1));

        etiquetaNombre.setForeground(TemaManager.textoPrincipal());
        etiquetaArtista.setForeground(TemaManager.acento());

        etiquetaTranscurrido.setForeground(TemaManager.textoSecundario());
        etiquetaDuracion.setForeground(TemaManager.textoSecundario());
        etiquetaVolumen.setForeground(TemaManager.textoSecundario());

        selectorModo.setBackground(TemaManager.panelDestacado());
        selectorModo.setForeground(TemaManager.textoPrincipal());

        barraProgreso.setBackground(TemaManager.panelDestacado());
        barraProgreso.setForeground(TemaManager.acento());

        sliderVolumen.setBackground(TemaManager.panelSecundario());

        TemaManager.aplicarBoton(botonPausar);
        TemaManager.aplicarBoton(botonAnterior);
        TemaManager.aplicarBoton(botonSiguiente);
        TemaManager.aplicarBoton(botonRepetir);

        repaint();
        revalidate();
    }

    private static class BotonCircular extends JButton {

        private String texto = "▶";

        public BotonCircular() {
            setPreferredSize(new Dimension(54, 54));
            setMinimumSize(new Dimension(54, 54));
            setMaximumSize(new Dimension(54, 54));
            setForeground(Color.WHITE);
            setFont(new Font("Helvetica Neue", Font.BOLD, 22));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public void setTexto(String texto) {
            this.texto = texto;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(TemaManager.acento());
            g2.fillOval(0, 0, getWidth(), getHeight());

            g2.setColor(Color.WHITE);
            g2.setFont(getFont());

            int anchoTexto = g2.getFontMetrics().stringWidth(texto);
            int altoTexto = g2.getFontMetrics().getAscent();

            int x = (getWidth() - anchoTexto) / 2;
            int y = (getHeight() + altoTexto) / 2 - 3;

            g2.drawString(texto, x, y);
            g2.dispose();
        }
    }
}