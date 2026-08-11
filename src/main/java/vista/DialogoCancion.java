package vista;

import modelo.Cancion;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.time.Year;
import java.util.function.Consumer;

public class DialogoCancion extends JDialog {

    private final JTextField campoNombre = new JTextField();
    private final JTextField campoArtista = new JTextField();
    private final JTextField campoAlbum = new JTextField();
    private final JTextField campoGenero = new JTextField();
    private final JSpinner campoDuracion = new JSpinner(new SpinnerNumberModel(180, 1, 36000, 1));
    private final JSpinner campoAnio = new JSpinner(new SpinnerNumberModel(
            Year.now().getValue(), 1, Year.now().getValue() + 1, 1));
    private final JSpinner campoCalificacion = new JSpinner(new SpinnerNumberModel(50, 0, 100, 1));
    private final JTextField campoPortada = new JTextField();

    private final Cancion cancionExistente;
    private final Consumer<Cancion> alGuardar;

    public DialogoCancion(JFrame propietario, Cancion cancionExistente, Consumer<Cancion> alGuardar) {
        super(propietario, cancionExistente == null ? "Agregar cancion" : "Editar cancion", true);
        this.cancionExistente = cancionExistente;
        this.alGuardar = alGuardar;

        construirInterfaz();
        if (cancionExistente != null) {
            cargarDatos(cancionExistente);
        }

        pack();
        setMinimumSize(new java.awt.Dimension(420, 420));
        setLocationRelativeTo(propietario);
    }

    private void construirInterfaz() {
        getContentPane().setBackground(TemaManager.panelSecundario());
        setLayout(new BorderLayout());

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBackground(TemaManager.panelSecundario());
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;
        agregarCampo(panelCampos, gbc, fila++, "Nombre", campoNombre);
        agregarCampo(panelCampos, gbc, fila++, "Artista", campoArtista);
        agregarCampo(panelCampos, gbc, fila++, "Album", campoAlbum);
        agregarCampo(panelCampos, gbc, fila++, "Genero", campoGenero);
        agregarCampo(panelCampos, gbc, fila++, "Duracion (segundos)", campoDuracion);
        agregarCampo(panelCampos, gbc, fila++, "Anio de lanzamiento", campoAnio);
        agregarCampo(panelCampos, gbc, fila++, "Calificacion (0-100)", campoCalificacion);

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        JLabel etiquetaPortada = new JLabel("Portada (opcional)");
        etiquetaPortada.setForeground(TemaManager.textoSecundario());
        etiquetaPortada.setFont(TemaOscuro.FUENTE_NORMAL);
        panelCampos.add(etiquetaPortada, gbc);

        JPanel panelPortada = new JPanel(new BorderLayout(6, 0));
        panelPortada.setOpaque(false);
        campoPortada.setEditable(false);
        estilizarCampo(campoPortada);
        BotonModerno botonExaminar = new BotonModerno("Examinar...", TemaManager.panelDestacado(), true);
        botonExaminar.addActionListener(e -> elegirImagen());
        panelPortada.add(campoPortada, BorderLayout.CENTER);
        panelPortada.add(botonExaminar, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panelCampos.add(panelPortada, gbc);

        add(panelCampos, BorderLayout.CENTER);
        add(construirPanelBotones(), BorderLayout.SOUTH);
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, javax.swing.JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.weightx = 0;
        JLabel label = new JLabel(etiqueta);
        label.setForeground(TemaManager.textoSecundario());
        label.setFont(TemaOscuro.FUENTE_NORMAL);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        if (campo instanceof JTextField) {
            estilizarCampo((JTextField) campo);
        } else {
            campo.setBackground(TemaManager.panelDestacado());
            campo.setForeground(TemaManager.textoPrincipal());
        }
        panel.add(campo, gbc);
    }

    private void estilizarCampo(JTextField campo) {

        campo.setBackground(TemaManager.panelDestacado());
        campo.setForeground(TemaManager.textoPrincipal());
        campo.setCaretColor(TemaManager.textoPrincipal());

        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaManager.borde(), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    private JPanel construirPanelBotones() {
        JPanel panel = new JPanel();
        panel.setBackground(TemaManager.panelSecundario());
        panel.setBorder(BorderFactory.createEmptyBorder(6, 24, 18, 24));

        BotonModerno botonCancelar = new BotonModerno("Cancelar",
                        Tema.esOscuro()
                                ? Color.decode("#3F3F46")
                                : Color.decode("#CBD5E1"),
                        true);

        BotonModerno botonGuardar = new BotonModerno("Guardar", TemaManager.acento());

        panel.add(botonCancelar);
        panel.add(botonGuardar);
        return panel;
    }

    private void elegirImagen() {
        JFileChooser selector = new JFileChooser();
        selector.setFileFilter(new FileNameExtensionFilter(
                "Imagenes", "jpg", "jpeg", "png", "gif"));
        int resultado = selector.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = selector.getSelectedFile();
            campoPortada.setText(archivo.getAbsolutePath());
        }
    }

    private void cargarDatos(Cancion c) {
        campoNombre.setText(c.getNombre());
        campoArtista.setText(c.getArtista());
        campoAlbum.setText(c.getAlbum());
        campoGenero.setText(c.getGenero());
        campoDuracion.setValue(c.getDuracionSegundos());
        campoAnio.setValue(c.getAnioLanzamiento());
        campoCalificacion.setValue(c.getCalificacion());
        campoPortada.setText(c.getRutaPortada() == null ? "" : c.getRutaPortada());
    }

    private void guardar() {
        String nombre = campoNombre.getText().trim();
        String artista = campoArtista.getText().trim();
        String album = campoAlbum.getText().trim();
        String genero = campoGenero.getText().trim();

        if (nombre.isEmpty()) {
            mostrarError("El nombre de la cancion no puede estar vacio.");
            return;
        }
        if (artista.isEmpty()) {
            mostrarError("El artista no puede estar vacio.");
            return;
        }
        if (album.isEmpty()) {
            mostrarError("El album no puede estar vacio.");
            return;
        }
        if (genero.isEmpty()) {
            mostrarError("El genero no puede estar vacio.");
            return;
        }

        int duracion = (Integer) campoDuracion.getValue();
        int anio = (Integer) campoAnio.getValue();
        int calificacion = (Integer) campoCalificacion.getValue();
        String portada = campoPortada.getText().trim();

        try {
            Cancion resultado = new Cancion(nombre, artista, album, duracion, genero, anio, calificacion);
            resultado.setRutaPortada(portada.isEmpty() ? null : portada);
            alGuardar.accept(resultado);
            dispose();
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos invalidos", JOptionPane.ERROR_MESSAGE);
    }

    public void actualizarTema() {
        getContentPane().setBackground(TemaManager.panelSecundario());

        for (Component componente : getContentPane().getComponents()) {
            actualizarComponente(componente);
        }

        repaint();
        revalidate();
    }

    private void actualizarComponente(Component componente) {

        if (componente instanceof JPanel panel) {
            panel.setBackground(TemaManager.panelSecundario());

            for (Component hijo : panel.getComponents()) {
                actualizarComponente(hijo);
            }
        }

        if (componente instanceof JLabel label) {
            label.setForeground(TemaManager.textoSecundario());
        }

        if (componente instanceof JTextField campo) {
            campo.setBackground(TemaManager.panelDestacado());
            campo.setForeground(TemaManager.textoPrincipal());
            campo.setCaretColor(TemaManager.textoPrincipal());
        }

        if (componente instanceof JSpinner spinner) {
            spinner.getEditor().setBackground(TemaManager.panelDestacado());
            spinner.setBackground(TemaManager.panelDestacado());
            spinner.setForeground(TemaManager.textoPrincipal());
        }
    }
}
