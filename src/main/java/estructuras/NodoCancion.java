package estructuras;

import modelo.Cancion;

class NodoCancion {

    private Cancion cancion;
    private NodoCancion anterior;
    private NodoCancion siguiente;

    NodoCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    Cancion getCancion() {
        return cancion;
    }

    void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    NodoCancion getAnterior() {
        return anterior;
    }

    void setAnterior(NodoCancion anterior) {
        this.anterior = anterior;
    }

    NodoCancion getSiguiente() {
        return siguiente;
    }

    void setSiguiente(NodoCancion siguiente) {
        this.siguiente = siguiente;
    }
}
