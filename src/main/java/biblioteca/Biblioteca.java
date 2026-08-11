package biblioteca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import modelo.Cancion;
import modos.ModoAleatorio;
import modos.ModoReproduccion;

public class Biblioteca implements RepositorioCRUD<Cancion> {

    private final List<Cancion> canciones = new ArrayList<>();

    private final List<ModoReproduccion> modosRegistrados = new ArrayList<>();

    public void registrarModo(ModoReproduccion modo) {
        if (modo != null && !modosRegistrados.contains(modo)) {
            modosRegistrados.add(modo);
        }
    }

    public void desregistrarModo(ModoReproduccion modo) {
        modosRegistrados.remove(modo);
    }

    public ModoReproduccion cambiarModoActivo(ModoReproduccion modoAnterior, ModoReproduccion modoNuevo) {
        if (modoAnterior != null) {
            desregistrarModo(modoAnterior);
        }
        modoNuevo.limpiar();
        for (Cancion c : canciones) {
            modoNuevo.agregarCancion(c);
        }
        if (modoNuevo instanceof ModoAleatorio) {
            ((ModoAleatorio) modoNuevo).mezclar();
        }
        registrarModo(modoNuevo);
        return modoNuevo;
    }

    @Override
    public void agregar(Cancion c) {
        if (c == null) {
            throw new IllegalArgumentException("La canción no puede ser null");
        }
        canciones.add(c);
        for (ModoReproduccion modo : modosRegistrados) {
            modo.agregarCancion(c);
            if (modo instanceof ModoAleatorio) {
                ((ModoAleatorio) modo).mezclar();
            }
        }
    }

    @Override
    public boolean eliminar(Cancion c) {
        boolean eliminada = canciones.removeIf(existente -> existente.equals(c));
        if (eliminada) {
            for (ModoReproduccion modo : modosRegistrados) {
                modo.eliminarCancion(c);
            }
        }
        return eliminada;
    }

    @Override
    public boolean editar(Cancion existente, Cancion nuevosDatos) {
        if (existente == null || nuevosDatos == null) {
            return false;
        }
        Optional<Cancion> encontrada = buscarPorId(existente.getId());
        if (encontrada.isEmpty()) {
            return false;
        }
        Cancion c = encontrada.get();
        c.setNombre(nuevosDatos.getNombre());
        c.setArtista(nuevosDatos.getArtista());
        c.setAlbum(nuevosDatos.getAlbum());
        c.setDuracionSegundos(nuevosDatos.getDuracionSegundos());
        c.setGenero(nuevosDatos.getGenero());
        c.setAnioLanzamiento(nuevosDatos.getAnioLanzamiento());
        c.setCalificacion(nuevosDatos.getCalificacion());
        c.setRutaPortada(nuevosDatos.getRutaPortada());
        return true;
    }

    @Override
    public Optional<Cancion> buscarPorId(String id) {
        return canciones.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public List<Cancion> buscarPorNombre(String textoBusqueda) {
        String q = textoBusqueda == null ? "" : textoBusqueda.trim().toLowerCase();
        List<Cancion> resultado = new ArrayList<>();
        for (Cancion c : canciones) {
            if (c.getNombre().toLowerCase().contains(q)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Cancion> filtrarPorArtista(String artista) {
        return filtrarPor(c -> c.getArtista().equalsIgnoreCase(artista));
    }

    public List<Cancion> filtrarPorGenero(String genero) {
        return filtrarPor(c -> c.getGenero().equalsIgnoreCase(genero));
    }

    public List<Cancion> filtrarPorAlbum(String album) {
        return filtrarPor(c -> c.getAlbum().equalsIgnoreCase(album));
    }

    private List<Cancion> filtrarPor(java.util.function.Predicate<Cancion> criterio) {
        List<Cancion> resultado = new ArrayList<>();
        for (Cancion c : canciones) {
            if (criterio.test(c)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    @Override
    public List<Cancion> obtenerTodos() {
        return Collections.unmodifiableList(new ArrayList<>(canciones));
    }

    public int tamano() {
        return canciones.size();
    }
}
