package estructuras;

public class NodoArbol<M> {

    M valor;
    NodoArbol<M> izquierdo;
    NodoArbol<M> derecho;

    public NodoArbol(M valor) {
        this.valor = valor;
    }
}