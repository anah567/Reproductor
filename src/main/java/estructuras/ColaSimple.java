package estructuras;

import modelo.Cancion;

import java.util.ArrayList;
import java.util.List;

public class ColaSimple {

    private NodoCola frente;
    private NodoCola fondo;
    private Cancion ultimaReproducida;
    private int tamano;

    public void encolar(Cancion c) {
        NodoCola nodo = new NodoCola(c);
        if (estaVacia()) {
            frente = nodo;
            fondo = nodo;
        } else {
            fondo.setSiguiente(nodo);
            fondo = nodo;
        }
        tamano++;
    }

    public Cancion siguiente() {
        if (estaVacia()) {
            return null;
        }
        NodoCola nodo = frente;
        frente = frente.getSiguiente();
        if (frente == null) {
            fondo = null;
        }
        tamano--;
        ultimaReproducida = nodo.getCancion();
        return ultimaReproducida;
    }

    public Cancion anterior() {
        throw new UnsupportedOperationException("No se puede retroceder en modo cola");
    }

    public Cancion actual() {
        return ultimaReproducida;
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    public int tamano() {
        return tamano;
    }

    public boolean eliminar(Cancion c) {
        if (estaVacia()) {
            return false;
        }
        NodoCola previo = null;
        NodoCola nodo = frente;
        while (nodo != null) {
            if (nodo.getCancion().equals(c)) {
                if (previo == null) {
                    frente = nodo.getSiguiente();
                } else {
                    previo.setSiguiente(nodo.getSiguiente());
                }
                if (nodo == fondo) {
                    fondo = previo;
                }
                tamano--;
                return true;
            }
            previo = nodo;
            nodo = nodo.getSiguiente();
        }
        return false;
    }

    public List<Cancion> toList() {
        List<Cancion> resultado = new ArrayList<>(tamano);
        NodoCola nodo = frente;
        while (nodo != null) {
            resultado.add(nodo.getCancion());
            nodo = nodo.getSiguiente();
        }
        return resultado;
    }

    public void limpiar() {
        frente = null;
        fondo = null;
        ultimaReproducida = null;
        tamano = 0;
    }
}
