package modos;

import modelo.Cancion;

import java.util.List;

public interface ModoReproduccion {

    Cancion siguiente();

    Cancion anterior();

    Cancion actual();

    void agregarCancion(Cancion c);

    boolean eliminarCancion(Cancion c);

    List<Cancion> obtenerTodas();

    String nombreModo();

    void limpiar();
}
