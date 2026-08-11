package modos;

import estructuras.ListaCircularDoble;
import modelo.Cancion;

import java.util.List;

public class ModoAleatorio implements ModoReproduccion {

    private final ListaCircularDoble lista = new ListaCircularDoble();

    @Override
    public Cancion siguiente() {
        return lista.siguiente();
    }

    @Override
    public Cancion anterior() {
        return lista.anterior();
    }

    @Override
    public Cancion actual() {
        return lista.actual();
    }

    @Override
    public void agregarCancion(Cancion c) {
        lista.agregar(c);
    }

    @Override
    public boolean eliminarCancion(Cancion c) {
        return lista.eliminar(c);
    }

    @Override
    public List<Cancion> obtenerTodas() {
        return lista.toList();
    }

    @Override
    public String nombreModo() {
        return "Aleatorio";
    }

    @Override
    public void limpiar() {
        lista.limpiar();
    }

    public void mezclar() {
        lista.mezclar();
    }

    public boolean estaVacia() {
        return lista.estaVacia();
    }

    public int tamano() {
        return lista.tamano();
    }
}
