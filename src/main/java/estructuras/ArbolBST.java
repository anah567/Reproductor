package estructuras;

import java.util.ArrayList;
import java.util.List;

public class ArbolBST<M extends Comparable<M>> {

    private NodoArbol<M> raiz;
    private int tamano;

    public void insertar(M valor) {
        raiz = insertarRecursivo(raiz, valor);
    }

    private NodoArbol<M> insertarRecursivo(
            NodoArbol<M> actual,
            M valor
    ) {

        if (actual == null) {
            tamano++;
            return new NodoArbol<>(valor);
        }

        int comparacion = valor.compareTo(actual.valor);

        if (comparacion < 0) {
            actual.izquierdo =
                    insertarRecursivo(actual.izquierdo, valor);
        }

        else if (comparacion > 0) {
            actual.derecho =
                    insertarRecursivo(actual.derecho, valor);
        }

        return actual;
    }

    public boolean buscar(M valor) {
        return buscarRecursivo(raiz, valor);
    }

    private boolean buscarRecursivo(
            NodoArbol<M> actual,
            M valor
    ) {

        if (actual == null) {
            return false;
        }

        int comparacion = valor.compareTo(actual.valor);

        if (comparacion == 0) {
            return true;
        }

        if (comparacion < 0) {
            return buscarRecursivo(actual.izquierdo, valor);
        }

        return buscarRecursivo(actual.derecho, valor);
    }

    public List<M> recorridoInorden() {
        List<M> resultado = new ArrayList<>();
        recorridoInordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void recorridoInordenRecursivo(
            NodoArbol<M> actual,
            List<M> resultado
    ) {

        if (actual != null) {

            recorridoInordenRecursivo(
                    actual.izquierdo,
                    resultado
            );

            resultado.add(actual.valor);

            recorridoInordenRecursivo(
                    actual.derecho,
                    resultado
            );
        }
    }

    public int tamano() {
        return tamano;
    }

    public boolean eliminar(M valor) {
        boolean[] eliminado = {false};
        raiz = eliminarRecursivo(raiz, valor, eliminado);
        return eliminado[0];
    }

    private NodoArbol<M> eliminarRecursivo(
            NodoArbol<M> actual,
            M valor,
            boolean[] eliminado
    ) {

        if (actual == null) {
            return null;
        }

        int comparacion = valor.compareTo(actual.valor);

        if (comparacion < 0) {
            actual.izquierdo =
                    eliminarRecursivo(actual.izquierdo, valor, eliminado);
        }

        else if (comparacion > 0) {
            actual.derecho =
                    eliminarRecursivo(actual.derecho, valor, eliminado);
        }

        else {
            eliminado[0] = true;
            tamano--;

            if (actual.izquierdo == null) {
                return actual.derecho;
            }
            if (actual.derecho == null) {
                return actual.izquierdo;
            }

            NodoArbol<M> sucesor = minimo(actual.derecho);
            actual.valor = sucesor.valor;
            actual.derecho = eliminarMinimo(actual.derecho);
        }

        return actual;
    }

    private NodoArbol<M> minimo(NodoArbol<M> nodo) {
        while (nodo.izquierdo != null) {
            nodo = nodo.izquierdo;
        }
        return nodo;
    }

    private NodoArbol<M> eliminarMinimo(NodoArbol<M> nodo) {
        if (nodo.izquierdo == null) {
            return nodo.derecho;
        }
        nodo.izquierdo = eliminarMinimo(nodo.izquierdo);
        return nodo;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public void limpiar() {
        raiz = null;
        tamano = 0;
    }
}