package vista;

import modelo.Cancion;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;

public class PanelCancionActual extends JPanel {

    private static final int TAMANO_PORTADA = 260;
    private static final String TARJETA_CANCION = "cancion";
    private static final String TARJETA_VACIA = "vacia";

    private final JLabel portada = new JLabel();
    private final JLabel etiquetaNombre = new JLabel();
    private final JLabel etiquetaArtista = new JLabel();
    private final JLabel etiquetaAlbum = new JLabel();
    private final JLabel etiquetaGenero = new JLabel();
    private final JLabel etiquetaAnio = new JLabel();
    private final JLabel etiquetaDuracion = new JLabel();
    private final JLabel etiquetaCalificacion = new JLabel();
    private final JLabel etiquetaModo = new JLabel("Modo Aleatorio");

    private final JLabel tituloPanel = new JLabel("Canción actual");
    private final JLabel iconoTitulo = new JLabel("\u266B");

    private final JLabel tituloModo = new JLabel("Modo de reproducción actual");

    private final JLabel tituloVacio = new JLabel("No hay ninguna canción reproduciéndose");
    private final JLabel subtituloVacio = new JLabel("Selecciona una canción de tu biblioteca para comenzar");
    private final JLabel iconoVacio = new JLabel("\u266B", SwingConstants.CENTER);

    private final JPanel tarjetaVacia = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 18));

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelTarjetas = new JPanel(cardLayout);

    public PanelCancionActual() {
        setLayout(new BorderLayout(0, 14));
        setBackground(TemaOscuro.PANEL_SECUNDARIO);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaOscuro.BORDE, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        add(construirTitulo(), BorderLayout.NORTH);

        panelTarjetas.setOpaque(false);
        panelTarjetas.add(construirPanelCancion(), TARJETA_CANCION);
        panelTarjetas.add(construirPanelVacio(), TARJETA_VACIA);

        add(panelTarjetas, BorderLayout.CENTER);

        mostrarVacio();
    }

    private JPanel construirTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);

        iconoTitulo.setFont(TemaOscuro.FUENTE_ICONO);
        iconoTitulo.setForeground(TemaOscuro.COLOR_ACENTO);

        tituloPanel.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        tituloPanel.setForeground(TemaOscuro.TEXTO_PRINCIPAL);

        panel.add(iconoTitulo);
        panel.add(tituloPanel);

        return panel;
    }

    private JPanel construirPanelCancion() {
        JPanel panel = new JPanel(new BorderLayout(24, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));

        configurarPortada();

        JPanel panelPortada = new JPanel(new GridBagLayout());
        panelPortada.setOpaque(false);
        panelPortada.add(portada);

        JPanel panelInformacion = construirPanelInformacion();

        panel.add(panelPortada, BorderLayout.WEST);
        panel.add(panelInformacion, BorderLayout.CENTER);

        return panel;
    }

    private void configurarPortada() {
        portada.setPreferredSize(new Dimension(TAMANO_PORTADA, TAMANO_PORTADA));
        portada.setMinimumSize(new Dimension(TAMANO_PORTADA, TAMANO_PORTADA));
        portada.setMaximumSize(new Dimension(TAMANO_PORTADA, TAMANO_PORTADA));
        portada.setHorizontalAlignment(SwingConstants.CENTER);
        portada.setVerticalAlignment(SwingConstants.CENTER);
        portada.setOpaque(true);
        portada.setBackground(TemaOscuro.PANEL_DESTACADO);
        portada.setBorder(BorderFactory.createLineBorder(TemaOscuro.BORDE, 1));
    }

    private JPanel construirPanelInformacion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        configurarEtiquetas();

        panel.add(Box.createVerticalGlue());
        panel.add(etiquetaNombre);
        panel.add(Box.createVerticalStrut(4));
        panel.add(etiquetaArtista);
        panel.add(Box.createVerticalStrut(22));
        panel.add(etiquetaAlbum);
        panel.add(Box.createVerticalStrut(12));
        panel.add(etiquetaGenero);
        panel.add(Box.createVerticalStrut(12));
        panel.add(etiquetaAnio);
        panel.add(Box.createVerticalStrut(12));
        panel.add(etiquetaDuracion);
        panel.add(Box.createVerticalStrut(12));
        panel.add(etiquetaCalificacion);
        panel.add(Box.createVerticalStrut(22));
        panel.add(construirPanelModo());
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void configurarEtiquetas() {
        etiquetaNombre.setFont(new Font("Helvetica Neue", Font.BOLD, 30));
        etiquetaNombre.setForeground(TemaOscuro.TEXTO_PRINCIPAL);
        etiquetaNombre.setAlignmentX(LEFT_ALIGNMENT);

        etiquetaArtista.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        etiquetaArtista.setForeground(TemaOscuro.COLOR_ACENTO);
        etiquetaArtista.setAlignmentX(LEFT_ALIGNMENT);

        configurarEtiquetaDetalle(etiquetaAlbum);
        configurarEtiquetaDetalle(etiquetaGenero);
        configurarEtiquetaDetalle(etiquetaAnio);
        configurarEtiquetaDetalle(etiquetaDuracion);

        etiquetaCalificacion.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        etiquetaCalificacion.setForeground(TemaOscuro.COLOR_ACENTO);
        etiquetaCalificacion.setAlignmentX(LEFT_ALIGNMENT);
    }

    private void configurarEtiquetaDetalle(JLabel etiqueta) {
        etiqueta.setFont(new Font("Helvetica Neue", Font.PLAIN, 15));
        etiqueta.setForeground(TemaOscuro.TEXTO_SECUNDARIO);
        etiqueta.setAlignmentX(LEFT_ALIGNMENT);
    }

    private JPanel construirPanelModo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        tituloModo.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        tituloModo.setForeground(TemaOscuro.TEXTO_SECUNDARIO);
        tituloModo.setAlignmentX(LEFT_ALIGNMENT);

        etiquetaModo.setFont(new Font("Helvetica Neue", Font.BOLD, 14));
        etiquetaModo.setForeground(Color.WHITE);
        etiquetaModo.setOpaque(true);
        etiquetaModo.setBackground(TemaOscuro.COLOR_ACENTO);
        etiquetaModo.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        etiquetaModo.setAlignmentX(LEFT_ALIGNMENT);

        panel.add(tituloModo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(etiquetaModo);

        return panel;
    }

    private JPanel construirPanelVacio() {
        JPanel contenedor = new JPanel(new GridBagLayout());
        contenedor.setOpaque(false);

        tarjetaVacia.setBackground(Color.decode("#10131B"));
        tarjetaVacia.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createDashedBorder(Color.decode("#303645"), 1, 5, 4, true),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)));

        iconoVacio.setFont(new Font("Helvetica Neue", Font.PLAIN, 42));
        iconoVacio.setForeground(TemaOscuro.TEXTO_PRINCIPAL);
        iconoVacio.setPreferredSize(new Dimension(80, 80));
        iconoVacio.setOpaque(true);
        iconoVacio.setBackground(TemaOscuro.PANEL_DESTACADO);
        iconoVacio.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);

        tituloVacio.setFont(new Font("Helvetica Neue", Font.BOLD, 17));
        tituloVacio.setForeground(TemaOscuro.TEXTO_PRINCIPAL);

        subtituloVacio.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
        subtituloVacio.setForeground(TemaOscuro.TEXTO_SECUNDARIO);

        textos.add(tituloVacio);
        textos.add(Box.createVerticalStrut(7));
        textos.add(subtituloVacio);

        tarjetaVacia.add(iconoVacio);
        tarjetaVacia.add(textos);
        contenedor.add(tarjetaVacia);

        return contenedor;
    }

    public void mostrarCancion(Cancion cancion) {
        if (cancion == null) {
            mostrarVacio();
            return;
        }

        etiquetaNombre.setText(cancion.getNombre());
        etiquetaArtista.setText(cancion.getArtista());
        etiquetaAlbum.setText("\u25A3  Álbum: " + cancion.getAlbum());
        etiquetaGenero.setText("\u266B  Género: " + cancion.getGenero());
        etiquetaAnio.setText("\u25A6  Año: " + cancion.getAnioLanzamiento());
        etiquetaDuracion.setText("\u25F7  Duración: " + formatearDuracion(cancion.getDuracionSegundos()));
        etiquetaCalificacion.setText("\u2606  Calificación: " + construirEstrellas(cancion.getCalificacion())
                + "  (" + cancion.getCalificacion() + "/100)");

        actualizarPortada(cancion.getRutaPortada());
        cardLayout.show(panelTarjetas, TARJETA_CANCION);
    }

    public void mostrarVacio() {
        portada.setIcon(null);
        portada.setText("\u266B");
        portada.setFont(new Font("Helvetica Neue", Font.PLAIN, 90));
        portada.setForeground(Tema.esOscuro() ? TemaOscuro.TEXTO_SECUNDARIO : TemaClaro.TEXTO_SECUNDARIO);
        cardLayout.show(panelTarjetas, TARJETA_VACIA);
    }

    public void actualizarModo(String nombreModo) {
        etiquetaModo.setText(nombreModo);
    }

    private void actualizarPortada(String ruta) {
        if (ruta != null && !ruta.isBlank() && new File(ruta).exists()) {
            ImageIcon original = new ImageIcon(ruta);
            Image escalada = original.getImage().getScaledInstance(
                    TAMANO_PORTADA - 8, TAMANO_PORTADA - 8, Image.SCALE_SMOOTH);

            portada.setIcon(new ImageIcon(escalada));
            portada.setText(null);
        } else {
            portada.setIcon(null);
            portada.setText("\u266B");
            portada.setFont(new Font("Helvetica Neue", Font.PLAIN, 90));
            portada.setForeground(Tema.esOscuro() ? TemaOscuro.TEXTO_SECUNDARIO : TemaClaro.TEXTO_SECUNDARIO);
        }
    }

    private String construirEstrellas(int calificacion) {
        int llenas = Math.round(calificacion / 20f);
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            resultado.append(i < llenas ? "\u2605" : "\u2606");
        }

        return resultado.toString();
    }

    private String formatearDuracion(int segundos) {
        int minutos = segundos / 60;
        int resto = segundos % 60;
        return String.format("%d:%02d", minutos, resto);
    }

    public void actualizarTema() {
        if (Tema.esOscuro()) {
            setBackground(TemaOscuro.PANEL_SECUNDARIO);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TemaOscuro.BORDE, 1),
                    BorderFactory.createEmptyBorder(16, 16, 16, 16)));

            tituloPanel.setForeground(TemaOscuro.TEXTO_PRINCIPAL);
            iconoTitulo.setForeground(TemaOscuro.COLOR_ACENTO);

            etiquetaNombre.setForeground(TemaOscuro.TEXTO_PRINCIPAL);
            etiquetaArtista.setForeground(TemaOscuro.COLOR_ACENTO);

            etiquetaAlbum.setForeground(TemaOscuro.TEXTO_SECUNDARIO);
            etiquetaGenero.setForeground(TemaOscuro.TEXTO_SECUNDARIO);
            etiquetaAnio.setForeground(TemaOscuro.TEXTO_SECUNDARIO);
            etiquetaDuracion.setForeground(TemaOscuro.TEXTO_SECUNDARIO);

            etiquetaCalificacion.setForeground(TemaOscuro.COLOR_ACENTO);

            tituloModo.setForeground(TemaOscuro.TEXTO_SECUNDARIO);
            etiquetaModo.setBackground(TemaOscuro.COLOR_ACENTO);
            etiquetaModo.setForeground(Color.WHITE);

            portada.setBackground(TemaOscuro.PANEL_DESTACADO);
            portada.setForeground(TemaOscuro.TEXTO_SECUNDARIO);
            portada.setBorder(BorderFactory.createLineBorder(TemaOscuro.BORDE, 1));

            tarjetaVacia.setBackground(Color.decode("#10131B"));
            tarjetaVacia.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createDashedBorder(Color.decode("#303645"), 1, 5, 4, true),
                    BorderFactory.createEmptyBorder(24, 28, 24, 28)));

            iconoVacio.setBackground(TemaOscuro.PANEL_DESTACADO);
            iconoVacio.setForeground(TemaOscuro.TEXTO_PRINCIPAL);

            tituloVacio.setForeground(TemaOscuro.TEXTO_PRINCIPAL);
            subtituloVacio.setForeground(TemaOscuro.TEXTO_SECUNDARIO);

        } else {
            setBackground(TemaClaro.PANEL_SECUNDARIO);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TemaClaro.BORDE, 1),
                    BorderFactory.createEmptyBorder(16, 16, 16, 16)));

            tituloPanel.setForeground(TemaClaro.TEXTO_PRINCIPAL);
            iconoTitulo.setForeground(TemaClaro.COLOR_ACENTO);

            etiquetaNombre.setForeground(TemaClaro.TEXTO_PRINCIPAL);
            etiquetaArtista.setForeground(TemaClaro.COLOR_ACENTO);

            etiquetaAlbum.setForeground(TemaClaro.TEXTO_SECUNDARIO);
            etiquetaGenero.setForeground(TemaClaro.TEXTO_SECUNDARIO);
            etiquetaAnio.setForeground(TemaClaro.TEXTO_SECUNDARIO);
            etiquetaDuracion.setForeground(TemaClaro.TEXTO_SECUNDARIO);

            etiquetaCalificacion.setForeground(TemaClaro.COLOR_ACENTO);

            tituloModo.setForeground(TemaClaro.TEXTO_SECUNDARIO);
            etiquetaModo.setBackground(TemaClaro.COLOR_ACENTO);
            etiquetaModo.setForeground(Color.WHITE);

            portada.setBackground(TemaClaro.PANEL_DESTACADO);
            portada.setForeground(TemaClaro.TEXTO_SECUNDARIO);
            portada.setBorder(BorderFactory.createLineBorder(TemaClaro.BORDE, 1));

            tarjetaVacia.setBackground(TemaClaro.PANEL_DESTACADO);
            tarjetaVacia.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createDashedBorder(TemaClaro.BORDE, 1, 5, 4, true),
                    BorderFactory.createEmptyBorder(24, 28, 24, 28)));

            iconoVacio.setBackground(TemaClaro.FONDO_PRINCIPAL);
            iconoVacio.setForeground(TemaClaro.COLOR_ACENTO);

            tituloVacio.setForeground(TemaClaro.TEXTO_PRINCIPAL);
            subtituloVacio.setForeground(TemaClaro.TEXTO_SECUNDARIO);
        }

        repaint();
        revalidate();
    }
}