package estructuras;

import modelo.Cancion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListaCircularDoble {

    private NodoCancion cabeza;
    private NodoCancion actual;
    private int tamano;

    public void agregar(Cancion c) {
        NodoCancion nodo = new NodoCancion(c);
        if (estaVacia()) {
            nodo.setAnterior(nodo);
            nodo.setSiguiente(nodo);
            cabeza = nodo;
            actual = nodo;
        } else {
            NodoCancion cola = cabeza.getAnterior();
            cola.setSiguiente(nodo);
            nodo.setAnterior(cola);
            nodo.setSiguiente(cabeza);
            cabeza.setAnterior(nodo);
        }
        tamano++;
    }

    public boolean eliminar(Cancion c) {
        if (estaVacia()) {
            return false;
        }
        NodoCancion nodo = buscarNodo(c);
        if (nodo == null) {
            return false;
        }
        if (tamano == 1) {
            cabeza = null;
            actual = null;
        } else {
            NodoCancion previo = nodo.getAnterior();
            NodoCancion siguienteNodo = nodo.getSiguiente();
            previo.setSiguiente(siguienteNodo);
            siguienteNodo.setAnterior(previo);
            if (nodo == cabeza) {
                cabeza = siguienteNodo;
            }
            if (nodo == actual) {
                actual = siguienteNodo;
            }
        }
        tamano--;
        return true;
    }

    private NodoCancion buscarNodo(Cancion c) {
        NodoCancion nodo = cabeza;
        for (int i = 0; i < tamano; i++) {
            if (nodo.getCancion().equals(c)) {
                return nodo;
            }
            nodo = nodo.getSiguiente();
        }
        return null;
    }

    public Cancion siguiente() {
        if (estaVacia()) {
            return null;
        }
        actual = actual.getSiguiente();
        return actual.getCancion();
    }

    public Cancion anterior() {
        if (estaVacia()) {
            return null;
        }
        actual = actual.getAnterior();
        return actual.getCancion();
    }

    public Cancion actual() {
        return estaVacia() ? null : actual.getCancion();
    }

    public boolean estaVacia() {
        return tamano == 0;
    }

    public int tamano() {
        return tamano;
    }

    public List<Cancion> toList() {
        List<Cancion> resultado = new ArrayList<>(tamano);
        NodoCancion nodo = cabeza;
        for (int i = 0; i < tamano; i++) {
            resultado.add(nodo.getCancion());
            nodo = nodo.getSiguiente();
        }
        return resultado;
    }

    public void mezclar() {
        if (tamano <= 1) {
            return;
        }
        List<Cancion> canciones = toList();
        Collections.shuffle(canciones);
        limpiar();
        for (Cancion c : canciones) {
            agregar(c);
        }
    }

    public void limpiar() {
        cabeza = null;
        actual = null;
        tamano = 0;
    }
}
