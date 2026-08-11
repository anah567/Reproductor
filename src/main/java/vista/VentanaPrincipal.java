package vista;

import biblioteca.ReproductorController;
import modelo.Cancion;
import modos.ModoAleatorio;
import modos.ModoAlfabetico;
import modos.ModoOrdenLlegada;
import modos.ModoReproduccion;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final ReproductorController controller = new ReproductorController(new ModoAleatorio());

    private final PanelEncabezado panelEncabezado = new PanelEncabezado(this);
    private final PanelBiblioteca panelBiblioteca = new PanelBiblioteca(this);
    private final PanelCancionActual panelCancionActual = new PanelCancionActual();
    private final PanelControles panelControles = new PanelControles(this);
    private final JLabel barraEstado = new JLabel("Listo");
    private final JPanel panelBarraEstado = new JPanel(new BorderLayout());

    private Cancion cancionSeleccionadaEnTabla;
    private Cancion cancionEnReproduccion;
    private int segundosTranscurridos;
    private final Timer temporizador;

    public VentanaPrincipal() {
        super("Reproductor Musical - Camellos vs Enanos");

        setSize(1450, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(TemaOscuro.FONDO_PRINCIPAL);
        setLayout(new BorderLayout());

        temporizador = new Timer(1000, e -> avanzarProgreso());

        construirDistribucion();
        cargarDatosIniciales();
        refrescarBiblioteca();

        panelCancionActual.actualizarModo(controller.nombreModoActivo());
        panelControles.actualizarModoActual(controller.nombreModoActivo());
        panelControles.mostrarVacio();
    }

    private void construirDistribucion() {
        JPanel contenedorPrincipal = new JPanel(new BorderLayout(0, 12));
        contenedorPrincipal.setBackground(TemaOscuro.FONDO_PRINCIPAL);
        contenedorPrincipal.setBorder(BorderFactory.createEmptyBorder(0, 20, 14, 20));
        contenedorPrincipal.add(panelEncabezado, BorderLayout.NORTH);

        JPanel panelDivision = new JPanel(new java.awt.GridBagLayout());
        panelDivision.setOpaque(false);

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0;
        gbc.weightx = 0.56;
        gbc.insets = new java.awt.Insets(0, 0, 0, 8);
        panelDivision.add(panelBiblioteca, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.44;
        gbc.insets = new java.awt.Insets(0, 8, 0, 0);
        panelDivision.add(panelCancionActual, gbc);

        contenedorPrincipal.add(panelDivision, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(0, 6));
        panelInferior.setOpaque(false);
        panelInferior.add(panelControles, BorderLayout.CENTER);
        panelInferior.add(construirBarraEstado(), BorderLayout.SOUTH);

        contenedorPrincipal.add(panelInferior, BorderLayout.SOUTH);
        setContentPane(contenedorPrincipal);
    }

    private JPanel construirBarraEstado() {
        panelBarraEstado.setBackground(TemaManager.panelDestacado());
        panelBarraEstado.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

        barraEstado.setForeground(TemaManager.textoSecundario());
        barraEstado.setFont(TemaOscuro.FUENTE_SUBTITULO);

        panelBarraEstado.add(barraEstado, BorderLayout.WEST);

        return panelBarraEstado;
    }

    private void cargarDatosIniciales() {
        if (PersistenciaCanciones.existeArchivo()) {
            List<Cancion> guardadas = PersistenciaCanciones.cargar();

            for (Cancion cancion : guardadas) {
                controller.agregarCancion(cancion);
            }

            return;
        }

        for (Cancion demo : crearCancionesDemo()) {
            controller.agregarCancion(demo);
        }

        PersistenciaCanciones.guardar(controller.obtenerBiblioteca());
    }

    private List<Cancion> crearCancionesDemo() {
        List<Cancion> demo = new ArrayList<>();

        demo.add(new Cancion("Believer", "Imagine Dragons", "Evolve", 204, "Rock", 2017, 90));
        demo.add(new Cancion("Halo", "Beyonce", "I Am... Sasha Fierce", 261, "Pop", 2008, 88));
        demo.add(new Cancion("Zombie", "The Cranberries", "No Need to Argue", 307, "Rock", 1994, 85));
        demo.add(new Cancion("Bohemian Rhapsody", "Queen", "A Night at the Opera", 355, "Rock", 1975, 98));
        demo.add(new Cancion("Counting Stars", "OneRepublic", "Native", 257, "Pop", 2013, 87));

        return demo;
    }

    public void onCancionSeleccionada(Cancion cancion) {
        cancionSeleccionadaEnTabla = cancion;
    }

    public void agregarCancion() {
        DialogoCancion dialogo = new DialogoCancion(this, null, nuevaCancion -> {
            controller.agregarCancion(nuevaCancion);
            persistir();
            refrescarBiblioteca();
            mostrarMensaje("Canción agregada correctamente");
        });

        dialogo.setVisible(true);
    }

    public void editarCancion() {
        if (cancionSeleccionadaEnTabla == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una canción de la biblioteca para editar.",
                    "Ninguna canción seleccionada",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Cancion existente = cancionSeleccionadaEnTabla;

        DialogoCancion dialogo = new DialogoCancion(this, existente, nuevosDatos -> {
            controller.editarCancion(existente, nuevosDatos);
            persistir();
            refrescarBiblioteca();

            if (cancionEnReproduccion != null && cancionEnReproduccion.getId().equals(existente.getId())) {
                panelCancionActual.mostrarCancion(existente);
                panelControles.mostrarCancion(existente);
            }

            mostrarMensaje("Canción editada correctamente");
        });

        dialogo.setVisible(true);
    }

    public void eliminarCancion() {
        if (cancionSeleccionadaEnTabla == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una canción de la biblioteca para eliminar.",
                    "Ninguna canción seleccionada",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar esta canción?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        Cancion aEliminar = cancionSeleccionadaEnTabla;
        controller.eliminarCancion(aEliminar);

        if (cancionEnReproduccion != null && cancionEnReproduccion.getId().equals(aEliminar.getId())) {
            temporizador.stop();
            segundosTranscurridos = 0;
            cancionEnReproduccion = null;

            panelCancionActual.mostrarVacio();
            panelControles.mostrarVacio();
        }

        cancionSeleccionadaEnTabla = null;

        persistir();
        refrescarBiblioteca();
        mostrarMensaje("Canción eliminada");
    }

    public void calificarCancion() {
        if (cancionSeleccionadaEnTabla == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una canción de la biblioteca para calificar.",
                    "Ninguna canción seleccionada",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Cancion seleccionada = cancionSeleccionadaEnTabla;

        DialogoCalificar dialogo = new DialogoCalificar(this, seleccionada, valor -> {
            controller.calificarCancion(seleccionada.getId(), valor);
            persistir();
            refrescarBiblioteca();

            if (cancionEnReproduccion != null && cancionEnReproduccion.getId().equals(seleccionada.getId())) {
                panelCancionActual.mostrarCancion(cancionEnReproduccion);
                panelControles.mostrarCancion(cancionEnReproduccion);
            }

            mostrarMensaje("Calificación actualizada");
        });

        dialogo.setVisible(true);
    }

    public void buscar(String texto) {
        if (texto == null || texto.isBlank()) {
            refrescarBiblioteca();
            return;
        }

        List<Cancion> resultado = controller.buscarPorNombre(texto);
        panelBiblioteca.actualizarTabla(resultado);

        if (resultado.isEmpty()) {
            mostrarMensaje("No se encontraron resultados");
        } else {
            mostrarMensaje(resultado.size() + " resultado(s) encontrados");
        }
    }

    public void aplicarFiltrosCombinados() {
        List<Cancion> resultado = controller.obtenerBiblioteca();

        String artista = panelBiblioteca.getFiltroArtista();
        String genero = panelBiblioteca.getFiltroGenero();
        String album = panelBiblioteca.getFiltroAlbum();

        List<Cancion> filtrado = new ArrayList<>();

        for (Cancion cancion : resultado) {
            boolean coincideArtista = PanelBiblioteca.esFiltroTodos(artista)
                    || cancion.getArtista().equals(artista);

            boolean coincideGenero = PanelBiblioteca.esFiltroTodos(genero)
                    || cancion.getGenero().equals(genero);

            boolean coincideAlbum = PanelBiblioteca.esFiltroTodos(album)
                    || cancion.getAlbum().equals(album);

            if (coincideArtista && coincideGenero && coincideAlbum) {
                filtrado.add(cancion);
            }
        }

        panelBiblioteca.actualizarTabla(filtrado);

        if (filtrado.isEmpty()) {
            mostrarMensaje("No se encontraron resultados con esos filtros");
        }
    }

    public void refrescarBiblioteca() {
        List<Cancion> todas = controller.obtenerBiblioteca();

        panelBiblioteca.actualizarTabla(todas);
        panelBiblioteca.actualizarFiltros(todas);
        panelEncabezado.actualizarContador(todas.size());
    }

    public void reproducir() {
        if (cancionEnReproduccion == null) {
            Cancion siguiente = controller.cancionActual();

            if (siguiente == null) {
                siguiente = controller.siguiente();
            }

            if (siguiente == null) {
                mostrarMensaje("Biblioteca vacía");
                panelControles.mostrarVacio();
                return;
            }

            cancionEnReproduccion = siguiente;
            segundosTranscurridos = 0;

            panelCancionActual.mostrarCancion(cancionEnReproduccion);
            panelControles.mostrarCancion(cancionEnReproduccion);
        }

        temporizador.start();
        mostrarMensaje("Reproduciendo");
    }

    public void pausar() {
        temporizador.stop();
        mostrarMensaje("Pausado");
    }

    public void detener() {
        temporizador.stop();
        segundosTranscurridos = 0;

        panelControles.reiniciarProgreso();
        mostrarMensaje("Detenido");
    }

    public void reiniciarCancion() {
        segundosTranscurridos = 0;
        panelControles.reiniciarProgreso();
        mostrarMensaje("Canción reiniciada");
    }

    public void siguiente() {
        Cancion siguiente = controller.siguiente();

        if (siguiente == null) {
            temporizador.stop();
            mostrarMensaje("No hay más canciones");
            return;
        }

        cancionEnReproduccion = siguiente;
        segundosTranscurridos = 0;

        panelCancionActual.mostrarCancion(siguiente);
        panelControles.mostrarCancion(siguiente);
        panelControles.reiniciarProgreso();
    }

    public void anterior() {
        try {
            Cancion anterior = controller.anterior();

            if (anterior == null) {
                mostrarMensaje("No hay canciones");
                return;
            }

            cancionEnReproduccion = anterior;
            segundosTranscurridos = 0;

            panelCancionActual.mostrarCancion(anterior);
            panelControles.mostrarCancion(anterior);
            panelControles.reiniciarProgreso();

        } catch (UnsupportedOperationException ex) {
            mostrarMensaje("No se puede retroceder en el modo actual");
        }
    }

    private void avanzarProgreso() {
        if (cancionEnReproduccion == null) {
            temporizador.stop();
            return;
        }

        segundosTranscurridos++;

        panelControles.actualizarProgreso(
                segundosTranscurridos,
                cancionEnReproduccion.getDuracionSegundos()
        );

        if (segundosTranscurridos < cancionEnReproduccion.getDuracionSegundos()) {
            return;
        }

        temporizador.stop();

        Cancion siguiente = controller.siguiente();

        if (siguiente == null) {
            mostrarMensaje("Fin de la reproducción");

            cancionEnReproduccion = null;
            segundosTranscurridos = 0;

            panelCancionActual.mostrarVacio();
            panelControles.mostrarVacio();
            return;
        }

        cancionEnReproduccion = siguiente;
        segundosTranscurridos = 0;

        panelCancionActual.mostrarCancion(siguiente);
        panelControles.mostrarCancion(siguiente);
        panelControles.reiniciarProgreso();

        temporizador.start();
    }

    public void cambiarModoDesdeSelector(String seleccion) {
        if (seleccion == null) {
            return;
        }

        ModoReproduccion nuevoModo;

        switch (seleccion) {
            case "Orden de llegada":
                nuevoModo = new ModoOrdenLlegada();
                break;

            case "Orden alfabetico":
                nuevoModo = new ModoAlfabetico();
                break;

            default:
                nuevoModo = new ModoAleatorio();
                break;
        }

        controller.cambiarModo(nuevoModo);

        temporizador.stop();
        cancionEnReproduccion = null;
        segundosTranscurridos = 0;

        panelControles.reiniciarProgreso();
        panelControles.mostrarVacio();

        panelCancionActual.mostrarVacio();
        panelCancionActual.actualizarModo(controller.nombreModoActivo());

        panelControles.actualizarModoActual(controller.nombreModoActivo());
        panelControles.habilitarBotonAnterior(!"Orden de llegada".equals(seleccion));

        mostrarMensaje("Modo cambiado a " + controller.nombreModoActivo());
    }

    public void mostrarMensaje(String mensaje) {
        barraEstado.setText(mensaje);
    }

    public void actualizarTema() {
        getContentPane().setBackground(TemaManager.fondoPrincipal());

        panelEncabezado.actualizarTema();
        panelBiblioteca.actualizarTema();
        panelCancionActual.actualizarTema();
        panelControles.actualizarTema();

        panelBarraEstado.setBackground(TemaManager.panelDestacado());
        barraEstado.setForeground(TemaManager.textoSecundario());

        repaint();
        revalidate();
    }

    private void persistir() {
        PersistenciaCanciones.guardar(controller.obtenerBiblioteca());
    }
}