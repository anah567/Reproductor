package estructuras;

import modelo.Cancion;

class NodoCola {

    private Cancion cancion;
    private NodoCola siguiente;

    NodoCola(Cancion cancion) {
        this.cancion = cancion;
    }

    Cancion getCancion() {
        return cancion;
    }

    void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    NodoCola getSiguiente() {
        return siguiente;
    }

    void setSiguiente(NodoCola siguiente) {
        this.siguiente = siguiente;
    }
}
