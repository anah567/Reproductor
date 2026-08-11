import biblioteca.Biblioteca;
import modelo.Cancion;
import modos.ModoAleatorio;
import modos.ModoOrdenLlegada;

public class PruebasBiblioteca {

    public static void main(String[] args) {
        probarAgregarPropagaAModos();
        probarEliminarPropagaAModos();
        probarEditarConservaIdentidad();
        probarBusquedaPorNombre();
        probarCambioDeModoRecargaCanciones();
        probarEditarConDatosNulosNoFalla();
        probarCambioDeModoReutilizandoElMismoObjetoNoDuplica();
        System.out.println("Todas las pruebas de Biblioteca pasaron.");
    }

    private static void probarAgregarPropagaAModos() {
        Biblioteca biblioteca = new Biblioteca();
        ModoAleatorio modo = new ModoAleatorio();
        biblioteca.registrarModo(modo);

        Cancion c1 = new Cancion("Song A", "Artista 1", "Album 1", 200, "Pop", 2021, 70);
        biblioteca.agregar(c1);

        assertTrue(biblioteca.obtenerTodos().contains(c1), "La biblioteca debe contener c1");
        assertTrue(modo.obtenerTodas().contains(c1), "El modo debe recibir c1 al agregarla");
    }

    private static void probarEliminarPropagaAModos() {
        Biblioteca biblioteca = new Biblioteca();
        ModoAleatorio modo = new ModoAleatorio();
        biblioteca.registrarModo(modo);

        Cancion c1 = new Cancion("Song A", "Artista 1", "Album 1", 200, "Pop", 2021, 70);
        biblioteca.agregar(c1);
        boolean eliminada = biblioteca.eliminar(c1);

        assertTrue(eliminada, "eliminar debe retornar true si la canción existía");
        assertTrue(!modo.obtenerTodas().contains(c1), "El modo ya no debe tener c1");
    }

    private static void probarEditarConservaIdentidad() {
        Biblioteca biblioteca = new Biblioteca();
        Cancion original = new Cancion("Song A", "Artista 1", "Album 1", 200, "Pop", 2021, 70);
        biblioteca.agregar(original);

        Cancion nuevosDatos = new Cancion("Song A (Remix)", "Artista 1", "Album 1", 210, "Pop", 2021, 90);
        biblioteca.editar(original, nuevosDatos);

        assertTrue(original.getNombre().equals("Song A (Remix)"), "El nombre debe actualizarse");
        assertTrue(biblioteca.obtenerTodos().size() == 1, "Editar no debe duplicar la canción");
    }

    private static void probarBusquedaPorNombre() {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.agregar(new Cancion("Bohemian Rhapsody", "Queen", "A Night at the Opera", 355, "Rock", 1975, 95));
        biblioteca.agregar(new Cancion("Somebody to Love", "Queen", "A Day at the Races", 296, "Rock", 1976, 88));

        assertTrue(biblioteca.buscarPorNombre("bohemian").size() == 1, "Búsqueda parcial e insensible a mayúsculas");
    }

    private static void probarCambioDeModoRecargaCanciones() {
        Biblioteca biblioteca = new Biblioteca();
        ModoAleatorio modoInicial = new ModoAleatorio();
        biblioteca.registrarModo(modoInicial);

        biblioteca.agregar(new Cancion("Song A", "Artista 1", "Album 1", 200, "Pop", 2021, 70));
        biblioteca.agregar(new Cancion("Song B", "Artista 2", "Album 2", 180, "Rock", 2019, 85));

        ModoOrdenLlegada modoNuevo = new ModoOrdenLlegada();
        biblioteca.cambiarModoActivo(modoInicial, modoNuevo);

        assertTrue(modoNuevo.obtenerTodas().size() == 2, "El modo nuevo debe tener las 2 canciones de la biblioteca");
    }

    private static void probarEditarConDatosNulosNoFalla() {
        Biblioteca biblioteca = new Biblioteca();
        Cancion original = new Cancion("Song A", "Artista 1", "Album 1", 200, "Pop", 2021, 70);
        biblioteca.agregar(original);

        assertTrue(!biblioteca.editar(null, original), "editar con existente nulo debe retornar false");
        assertTrue(!biblioteca.editar(original, null), "editar con nuevosDatos nulo debe retornar false");
        assertTrue(!biblioteca.editar(null, null), "editar con ambos nulos debe retornar false");
    }

    private static void probarCambioDeModoReutilizandoElMismoObjetoNoDuplica() {
        Biblioteca biblioteca = new Biblioteca();
        ModoAleatorio modoAleatorio = new ModoAleatorio();
        biblioteca.registrarModo(modoAleatorio);

        biblioteca.agregar(new Cancion("Song A", "Artista 1", "Album 1", 200, "Pop", 2021, 70));
        biblioteca.agregar(new Cancion("Song B", "Artista 2", "Album 2", 180, "Rock", 2019, 85));

        ModoOrdenLlegada modoCola = new ModoOrdenLlegada();
        biblioteca.cambiarModoActivo(modoAleatorio, modoCola);
        assertTrue(modoCola.obtenerTodas().size() == 2, "El modo cola debe tener las 2 canciones tras el primer cambio");

        biblioteca.cambiarModoActivo(modoCola, modoAleatorio);
        assertTrue(modoAleatorio.obtenerTodas().size() == 2,
                "Reutilizar el mismo objeto de modo no debe duplicar canciones");
    }

    private static void assertTrue(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
