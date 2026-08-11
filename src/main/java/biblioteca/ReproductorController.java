package biblioteca;

import java.util.List;
import java.util.Optional;

import modelo.Cancion;
import modos.ModoAleatorio;
import modos.ModoReproduccion;

public class ReproductorController {

    private final Biblioteca biblioteca = new Biblioteca();
    private ModoReproduccion modoActivo;

    public ReproductorController(ModoReproduccion modoInicial) {
        this.modoActivo = modoInicial;
        biblioteca.registrarModo(modoInicial);
    }

    public void agregarCancion(Cancion c) {
        biblioteca.agregar(c);
    }

    public boolean eliminarCancion(Cancion c) {
        return biblioteca.eliminar(c);
    }

    public boolean editarCancion(Cancion existente, Cancion nuevosDatos) {
        return biblioteca.editar(existente, nuevosDatos);
    }

    public List<Cancion> buscarPorNombre(String texto) {
        return biblioteca.buscarPorNombre(texto);
    }

    public Optional<Cancion> buscarPorId(String id) {
        return biblioteca.buscarPorId(id);
    }

    public List<Cancion> obtenerBiblioteca() {
        return biblioteca.obtenerTodos();
    }

    public void calificarCancion(String id, int calificacion) {
        biblioteca.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Canción no encontrada: " + id))
                .setCalificacion(calificacion);
    }

    public Cancion siguiente() {
        return modoActivo.siguiente();
    }

    public Cancion anterior() {
        return modoActivo.anterior();
    }

    public Cancion cancionActual() {
        return modoActivo.actual();
    }

    public String nombreModoActivo() {
        return modoActivo.nombreModo();
    }

    public void cambiarModo(ModoReproduccion nuevoModo) {
        this.modoActivo = biblioteca.cambiarModoActivo(this.modoActivo, nuevoModo);
    }

    public void mezclarModoActivo() {
        if (modoActivo instanceof ModoAleatorio) {
            ((ModoAleatorio) modoActivo).mezclar();
        }
    }
}
