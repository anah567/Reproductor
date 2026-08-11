package vista;

import modelo.Cancion;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class PanelBiblioteca extends JPanel {

    private static final String TODOS = "Todos";

    private static final String[] COLUMNAS = {
            "Nombre", "Artista", "Álbum", "Género", "Año", "Duración", "Calificación"
    };

    private final VentanaPrincipal ventana;

    private final JTextField campoBusqueda = new JTextField();

    private final JComboBox<String> filtroArtista = new JComboBox<>();
    private final JComboBox<String> filtroGenero = new JComboBox<>();
    private final JComboBox<String> filtroAlbum = new JComboBox<>();

    private final JLabel titulo = new JLabel("Mi biblioteca");
    private final JLabel icono = new JLabel("\u266B");

    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JScrollPane scrollTabla;

    private final BotonModerno botonAgregar =
            new BotonModerno("＋ Agregar", TemaOscuro.COLOR_ACENTO, true);

    private final BotonModerno botonEditar =
            new BotonModerno("✎ Editar", Color.decode("#3B82F6"), true);

    private final BotonModerno botonEliminar =
            new BotonModerno("🗑 Eliminar", TemaOscuro.COLOR_PELIGRO, true);

    private final BotonModerno botonCalificar =
            new BotonModerno("★ Calificar", Color.decode("#F59E0B"), true);

    private List<Cancion> filaActual = new ArrayList<>();

    private boolean actualizandoFiltros = false;

    public PanelBiblioteca(VentanaPrincipal ventana) {
        this.ventana = ventana;

        setLayout(new BorderLayout(0, 12));
        setBackground(TemaManager.panelSecundario());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaManager.borde(), 1),
                BorderFactory.createEmptyBorder(16, 16, 14, 16)));

        setPreferredSize(new Dimension(700, 0));

        add(construirTitulo(), BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        estilizarTabla();

        scrollTabla = new JScrollPane(tabla);
        estilizarScroll();

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setOpaque(false);

        panelSuperior.add(construirPanelBusqueda());
        panelSuperior.add(Box.createVerticalStrut(10));
        panelSuperior.add(construirPanelFiltros());

        JPanel panelCentral = new JPanel(new BorderLayout(0, 12));
        panelCentral.setOpaque(false);

        panelCentral.add(panelSuperior, BorderLayout.NORTH);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        panelCentral.add(construirPanelBotonesCrud(), BorderLayout.SOUTH);

        add(panelCentral, BorderLayout.CENTER);

        actualizarTema();
    }

    private JPanel construirTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);

        icono.setFont(TemaOscuro.FUENTE_ICONO);
        icono.setForeground(TemaManager.acento());

        titulo.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        titulo.setForeground(TemaManager.textoPrincipal());

        panel.add(icono);
        panel.add(titulo);

        return panel;
    }

    private JPanel construirPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);

        campoBusqueda.setBackground(TemaManager.fondoPrincipal());
        campoBusqueda.setForeground(TemaManager.textoPrincipal());
        campoBusqueda.setCaretColor(TemaManager.textoPrincipal());
        campoBusqueda.setFont(TemaOscuro.FUENTE_NORMAL);
        campoBusqueda.setToolTipText("Buscar canciones por nombre");

        campoBusqueda.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaManager.borde(), 1),
                BorderFactory.createEmptyBorder(9, 12, 9, 12)));

        campoBusqueda.addActionListener(
                e -> ventana.buscar(campoBusqueda.getText().trim())
        );

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        BotonModerno botonBuscar =
                new BotonModerno("\u2315 Buscar", TemaOscuro.COLOR_ACENTO);

        botonBuscar.addActionListener(
                e -> ventana.buscar(campoBusqueda.getText().trim())
        );

        BotonModerno botonLimpiar =
                new BotonModerno("\u27F2 Limpiar", Color.decode("#64748B"), true);

        botonLimpiar.addActionListener(e -> {
            campoBusqueda.setText("");
            resetearFiltros();
            ventana.refrescarBiblioteca();
        });

        panelBotones.add(botonBuscar);
        panelBotones.add(botonLimpiar);

        panel.add(campoBusqueda, BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.EAST);

        return panel;
    }

    private JPanel construirPanelFiltros() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 12, 0));
        panel.setOpaque(false);

        configurarCombo(filtroArtista);
        configurarCombo(filtroGenero);
        configurarCombo(filtroAlbum);

        panel.add(envolverConEtiqueta("Filtrar por artista", filtroArtista));
        panel.add(envolverConEtiqueta("Filtrar por género", filtroGenero));
        panel.add(envolverConEtiqueta("Filtrar por álbum", filtroAlbum));

        return panel;
    }

    private JPanel envolverConEtiqueta(String texto, JComboBox<String> combo) {
        JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.setOpaque(false);

        JLabel etiqueta = new JLabel(texto);
        etiqueta.setForeground(TemaManager.textoSecundario());
        etiqueta.setFont(new Font("Helvetica Neue", Font.BOLD, 13));

        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(combo, BorderLayout.CENTER);

        return panel;
    }

    private void configurarCombo(JComboBox<String> combo) {
        combo.addItem(TODOS);

        combo.setBackground(TemaManager.fondoPrincipal());
        combo.setForeground(TemaManager.textoPrincipal());
        combo.setFont(TemaOscuro.FUENTE_SUBTITULO);
        combo.setPreferredSize(new Dimension(100, 32));

        combo.addItemListener(e -> {
            if (!actualizandoFiltros
                    && e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {

                ventana.aplicarFiltrosCombinados();
            }
        });
    }

    private JPanel construirPanelBotonesCrud() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 14, 0));
        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createEmptyBorder(2, 8, 0, 8)
        );

        botonAgregar.addActionListener(
                e -> ventana.agregarCancion()
        );

        botonEditar.addActionListener(
                e -> ventana.editarCancion()
        );

        botonEliminar.addActionListener(
                e -> ventana.eliminarCancion()
        );

        botonCalificar.addActionListener(
                e -> ventana.calificarCancion()
        );

        panel.add(botonAgregar);
        panel.add(botonEditar);
        panel.add(botonEliminar);
        panel.add(botonCalificar);

        return panel;
    }

    private void estilizarTabla() {
        tabla.setBackground(TemaManager.panelSecundario());
        tabla.setForeground(TemaManager.textoPrincipal());
        tabla.setGridColor(TemaManager.borde());

        tabla.setSelectionBackground(TemaManager.acento());
        tabla.setSelectionForeground(Color.WHITE);

        tabla.setRowHeight(31);

        tabla.setFont(
                new Font("Helvetica Neue", Font.PLAIN, 13)
        );

        tabla.setAutoCreateRowSorter(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setFillsViewportHeight(true);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        tabla.getTableHeader().setBackground(
                TemaManager.panelDestacado()
        );

        tabla.getTableHeader().setForeground(
                TemaManager.textoSecundario()
        );

        tabla.getTableHeader().setFont(
                new Font("Helvetica Neue", Font.BOLD, 12)
        );

        tabla.getTableHeader().setPreferredSize(
                new Dimension(0, 32)
        );

        tabla.getTableHeader().setReorderingAllowed(false);

        tabla.getColumnModel().getColumn(0).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(115);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(55);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(70);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(115);

        DefaultTableCellRenderer centrado =
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable tabla,
                            Object valor,
                            boolean seleccionado,
                            boolean tieneFoco,
                            int fila,
                            int columna) {

                        JLabel etiqueta =
                                (JLabel) super.getTableCellRendererComponent(
                                        tabla,
                                        valor,
                                        seleccionado,
                                        tieneFoco,
                                        fila,
                                        columna
                                );

                        etiqueta.setHorizontalAlignment(
                                SwingConstants.CENTER
                        );

                        etiqueta.setOpaque(true);

                        if (seleccionado) {
                            etiqueta.setBackground(
                                    TemaManager.acento()
                            );

                            etiqueta.setForeground(
                                    Color.WHITE
                            );

                        } else {
                            etiqueta.setBackground(
                                    TemaManager.panelSecundario()
                            );

                            etiqueta.setForeground(
                                    TemaManager.textoPrincipal()
                            );
                        }

                        return etiqueta;
                    }
                };

        tabla.getColumnModel().getColumn(4).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(5).setCellRenderer(centrado);
        tabla.getColumnModel().getColumn(6)
                .setCellRenderer(new RenderizadorCalificacion());

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ventana.onCancionSeleccionada(
                        getCancionSeleccionada()
                );
            }
        });
    }

    private void estilizarScroll() {
        scrollTabla.setBorder(
                BorderFactory.createLineBorder(
                        TemaManager.borde(),
                        1
                )
        );

        scrollTabla.getViewport().setBackground(
                TemaManager.panelSecundario()
        );

        scrollTabla.setBackground(
                TemaManager.panelSecundario()
        );

        scrollTabla.getVerticalScrollBar().setUnitIncrement(16);

        scrollTabla.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
    }

    public void actualizarTabla(List<Cancion> canciones) {
        filaActual = new ArrayList<>(canciones);

        modeloTabla.setRowCount(0);

        for (Cancion cancion : canciones) {

            modeloTabla.addRow(new Object[]{
                    cancion.getNombre(),
                    cancion.getArtista(),
                    cancion.getAlbum(),
                    cancion.getGenero(),
                    cancion.getAnioLanzamiento(),
                    formatearDuracion(
                            cancion.getDuracionSegundos()
                    ),
                    cancion.getCalificacion()
            });
        }
    }

    public void actualizarFiltros(List<Cancion> todas) {
        actualizandoFiltros = true;

        repoblarCombo(
                filtroArtista,
                extraerValoresUnicos(
                        todas,
                        Cancion::getArtista
                )
        );

        repoblarCombo(
                filtroGenero,
                extraerValoresUnicos(
                        todas,
                        Cancion::getGenero
                )
        );

        repoblarCombo(
                filtroAlbum,
                extraerValoresUnicos(
                        todas,
                        Cancion::getAlbum
                )
        );

        actualizandoFiltros = false;
    }

    private Set<String> extraerValoresUnicos(
            List<Cancion> canciones,
            java.util.function.Function<Cancion, String> extractor) {

        Set<String> valores = new LinkedHashSet<>();

        for (Cancion cancion : canciones) {
            String valor = extractor.apply(cancion);

            if (valor != null && !valor.isBlank()) {
                valores.add(valor);
            }
        }

        return valores;
    }

    private void repoblarCombo(
            JComboBox<String> combo,
            Set<String> valores) {

        Object seleccionActual =
                combo.getSelectedItem();

        combo.removeAllItems();
        combo.addItem(TODOS);

        for (String valor : valores) {
            combo.addItem(valor);
        }

        if (seleccionActual != null
                && contieneItem(
                combo,
                seleccionActual.toString()
        )) {

            combo.setSelectedItem(
                    seleccionActual
            );

        } else {

            combo.setSelectedItem(TODOS);
        }
    }

    private boolean contieneItem(
            JComboBox<String> combo,
            String valor) {

        for (int i = 0;
             i < combo.getItemCount();
             i++) {

            if (combo.getItemAt(i).equals(valor)) {
                return true;
            }
        }

        return false;
    }

    private void resetearFiltros() {
        actualizandoFiltros = true;

        filtroArtista.setSelectedItem(TODOS);
        filtroGenero.setSelectedItem(TODOS);
        filtroAlbum.setSelectedItem(TODOS);

        actualizandoFiltros = false;
    }

    public String getFiltroArtista() {
        return (String) filtroArtista.getSelectedItem();
    }

    public String getFiltroGenero() {
        return (String) filtroGenero.getSelectedItem();
    }

    public String getFiltroAlbum() {
        return (String) filtroAlbum.getSelectedItem();
    }

    public static boolean esFiltroTodos(String valor) {
        return valor == null || TODOS.equals(valor);
    }

    public Cancion getCancionSeleccionada() {
        int filaVista = tabla.getSelectedRow();

        if (filaVista < 0) {
            return null;
        }

        int filaModelo =
                tabla.convertRowIndexToModel(filaVista);

        if (filaModelo < 0
                || filaModelo >= filaActual.size()) {

            return null;
        }

        return filaActual.get(filaModelo);
    }

    private String formatearDuracion(int segundos) {
        int minutos = segundos / 60;
        int resto = segundos % 60;

        return String.format(
                "%d:%02d",
                minutos,
                resto
        );
    }

    private String construirEstrellas(int calificacion) {
        int llenas =
                Math.round(calificacion / 20f);

        StringBuilder resultado =
                new StringBuilder();

        for (int i = 0; i < 5; i++) {
            resultado.append(
                    i < llenas
                            ? "\u2605"
                            : "\u2606"
            );
        }

        return resultado.toString();
    }

    private class RenderizadorCalificacion
            extends DefaultTableCellRenderer {

        public RenderizadorCalificacion() {
            setHorizontalAlignment(
                    SwingConstants.CENTER
            );
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable tabla,
                Object valor,
                boolean seleccionado,
                boolean tieneFoco,
                int fila,
                int columna) {

            JLabel etiqueta =
                    (JLabel) super.getTableCellRendererComponent(
                            tabla,
                            valor,
                            seleccionado,
                            tieneFoco,
                            fila,
                            columna
                    );

            int calificacion =
                    valor instanceof Number
                            ? ((Number) valor).intValue()
                            : 0;

            etiqueta.setText(
                    construirEstrellas(calificacion)
                            + "  "
                            + calificacion
            );

            etiqueta.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            etiqueta.setOpaque(true);

            if (seleccionado) {

                etiqueta.setBackground(
                        TemaManager.acento()
                );

                etiqueta.setForeground(
                        Color.WHITE
                );

            } else {

                etiqueta.setBackground(
                        TemaManager.panelSecundario()
                );

                etiqueta.setForeground(
                        TemaManager.acento()
                );
            }

            return etiqueta;
        }
    }

    public void actualizarTema() {
        setBackground(
                TemaManager.panelSecundario()
        );

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        TemaManager.borde(),
                        1
                ),
                BorderFactory.createEmptyBorder(
                        16,
                        16,
                        14,
                        16
                )
        ));

        titulo.setForeground(
                TemaManager.textoPrincipal()
        );

        icono.setForeground(
                TemaManager.acento()
        );

        campoBusqueda.setBackground(
                TemaManager.fondoPrincipal()
        );

        campoBusqueda.setForeground(
                TemaManager.textoPrincipal()
        );

        campoBusqueda.setCaretColor(
                TemaManager.textoPrincipal()
        );

        campoBusqueda.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                TemaManager.borde(),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                9,
                                12,
                                9,
                                12
                        )
                )
        );

        filtroArtista.setBackground(
                TemaManager.fondoPrincipal()
        );

        filtroArtista.setForeground(
                TemaManager.textoPrincipal()
        );

        filtroGenero.setBackground(
                TemaManager.fondoPrincipal()
        );

        filtroGenero.setForeground(
                TemaManager.textoPrincipal()
        );

        filtroAlbum.setBackground(
                TemaManager.fondoPrincipal()
        );

        filtroAlbum.setForeground(
                TemaManager.textoPrincipal()
        );

        tabla.setBackground(
                TemaManager.panelSecundario()
        );

        tabla.setForeground(
                TemaManager.textoPrincipal()
        );

        tabla.setGridColor(
                TemaManager.borde()
        );

        tabla.setSelectionBackground(
                TemaManager.acento()
        );

        tabla.setSelectionForeground(
                Color.WHITE
        );

        tabla.getTableHeader().setBackground(
                TemaManager.panelDestacado()
        );

        tabla.getTableHeader().setForeground(
                TemaManager.textoSecundario()
        );

        scrollTabla.getViewport().setBackground(
                TemaManager.panelSecundario()
        );

        scrollTabla.setBackground(
                TemaManager.panelSecundario()
        );

        scrollTabla.setBorder(
                BorderFactory.createLineBorder(
                        TemaManager.borde(),
                        1
                )
        );

        botonAgregar.actualizarApariencia();
        botonEditar.actualizarApariencia();
        botonEliminar.actualizarApariencia();
        botonCalificar.actualizarApariencia();

        tabla.repaint();
        tabla.getTableHeader().repaint();

        repaint();
        revalidate();
    }
}