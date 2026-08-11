package modos;

import estructuras.ArbolBST;
import modelo.Cancion;

import java.util.List;

public class ModoAlfabetico implements ModoReproduccion {

    private final ArbolBST<Cancion> arbol =
            new ArbolBST<>();

    private int indiceActual = -1;

    @Override
    public Cancion siguiente() {

        List<Cancion> cancionesOrdenadas =
                arbol.recorridoInorden();

        if (cancionesOrdenadas.isEmpty()) {
            indiceActual = -1;
            return null;
        }

        if (indiceActual == -1) {
            indiceActual = 0;
        }

        else if (indiceActual < cancionesOrdenadas.size() - 1) {
            indiceActual++;
        }

        return cancionesOrdenadas.get(indiceActual);
    }

    @Override
    public Cancion anterior() {

        List<Cancion> cancionesOrdenadas =
                arbol.recorridoInorden();

        if (cancionesOrdenadas.isEmpty()) {
            indiceActual = -1;
            return null;
        }

        if (indiceActual == -1) {
            indiceActual =
                    cancionesOrdenadas.size() - 1;
        }

        else if (indiceActual > 0) {
            indiceActual--;
        }

        return cancionesOrdenadas.get(indiceActual);
    }

    @Override
    public Cancion actual() {

        List<Cancion> cancionesOrdenadas =
                arbol.recorridoInorden();

        if (cancionesOrdenadas.isEmpty()) {
            indiceActual = -1;
            return null;
        }

        if (indiceActual == -1) {
            indiceActual = 0;
        }

        if (indiceActual >= cancionesOrdenadas.size()) {
            indiceActual =
                    cancionesOrdenadas.size() - 1;
        }

        return cancionesOrdenadas.get(indiceActual);
    }

    @Override
    public void agregarCancion(Cancion cancion) {
        arbol.insertar(cancion);
    }

    @Override
    public boolean eliminarCancion(Cancion cancion) {

        boolean eliminada =
                arbol.eliminar(cancion);

        if (eliminada) {
            List<Cancion> cancionesOrdenadas =
                    arbol.recorridoInorden();

            if (cancionesOrdenadas.isEmpty()) {
                indiceActual = -1;

            } else if (indiceActual >= cancionesOrdenadas.size()) {
                indiceActual =
                        cancionesOrdenadas.size() - 1;
            }
        }

        return eliminada;
    }


    @Override
    public List<Cancion> obtenerTodas() {
        return arbol.recorridoInorden();
    }

    @Override
    public String nombreModo() {
        return "Orden alfabético";
    }

    public boolean estaVacio() {
        return arbol.estaVacio();
    }

    public int tamano() {
        return arbol.tamano();
    }

    @Override
    public void limpiar() {
        arbol.limpiar();
        indiceActual = -1;
    }
}