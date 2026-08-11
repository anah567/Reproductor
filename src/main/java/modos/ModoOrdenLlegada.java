package modos;

import estructuras.ColaSimple;
import modelo.Cancion;

import java.util.List;

public class ModoOrdenLlegada implements ModoReproduccion {

    private final ColaSimple cola = new ColaSimple();

    @Override
    public Cancion siguiente() {
        return cola.siguiente();
    }

    @Override
    public Cancion anterior() {
        return cola.anterior();
    }

    @Override
    public Cancion actual() {
        return cola.actual();
    }

    @Override
    public void agregarCancion(Cancion c) {
        cola.encolar(c);
    }

    @Override
    public boolean eliminarCancion(Cancion c) {
        return cola.eliminar(c);
    }

    @Override
    public List<Cancion> obtenerTodas() {
        return cola.toList();
    }

    @Override
    public String nombreModo() {
        return "Orden de llegada";
    }

    @Override
    public void limpiar() {
        cola.limpiar();
    }

    public boolean estaVacia() {
        return cola.estaVacia();
    }

    public int tamano() {
        return cola.tamano();
    }
}
