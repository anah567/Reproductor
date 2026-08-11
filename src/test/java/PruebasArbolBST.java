import estructuras.ArbolBST;
import modelo.Cancion;
import modos.ModoAlfabetico;

import java.util.List;

public class PruebasArbolBST {

    private static int ok = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        probar("Arbol vacio", PruebasArbolBST::testVacio);
        probar("Insertar y recorrido inorden queda alfabetico", PruebasArbolBST::testInsertarInorden);
        probar("tamano() cuenta correctamente", PruebasArbolBST::testTamano);
        probar("eliminar hoja", PruebasArbolBST::testEliminarHoja);
        probar("eliminar nodo con un hijo", PruebasArbolBST::testEliminarUnHijo);
        probar("eliminar nodo con dos hijos (raiz)", PruebasArbolBST::testEliminarDosHijos);
        probar("eliminar valor que no existe retorna false", PruebasArbolBST::testEliminarNoExiste);
        probar("limpiar deja el arbol vacio", PruebasArbolBST::testLimpiar);
        probar("ModoAlfabetico integra bien con el arbol", PruebasArbolBST::testModoAlfabetico);

        System.out.println();
        System.out.println("Resultado: " + ok + " ok, " + fallidas + " fallidas");
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    private interface Prueba {
        void ejecutar();
    }

    private static void probar(String nombre, Prueba p) {
        try {
            p.ejecutar();
            System.out.println("[OK]    " + nombre);
            ok++;
        } catch (AssertionError | RuntimeException e) {
            System.out.println("[FALLO] " + nombre + " -> " + e);
            fallidas++;
        }
    }

    private static void assertTrue(boolean c, String msg) {
        if (!c) throw new AssertionError(msg);
    }

    private static void assertEquals(Object esperado, Object real, String msg) {
        if (esperado == null ? real != null : !esperado.equals(real)) {
            throw new AssertionError(msg + " (esperado=" + esperado + ", real=" + real + ")");
        }
    }

    private static Cancion c(String nombre) {
        return new Cancion(nombre, "Artista", "Album", 180, "Rock", 2020, 80);
    }

    private static void testVacio() {
        ArbolBST<Cancion> arbol = new ArbolBST<>();
        assertTrue(arbol.estaVacio(), "arbol recien creado debe estar vacio");
        assertEquals(0, arbol.tamano(), "tamano inicial debe ser 0");
        assertTrue(arbol.recorridoInorden().isEmpty(), "recorrido inorden vacio debe ser lista vacia");
        assertTrue(!arbol.eliminar(c("X")), "eliminar en arbol vacio debe retornar false");
    }

    private static void testInsertarInorden() {
        ArbolBST<Cancion> arbol = new ArbolBST<>();
        arbol.insertar(c("Delta"));
        arbol.insertar(c("Alfa"));
        arbol.insertar(c("Charlie"));
        arbol.insertar(c("Bravo"));

        List<Cancion> orden = arbol.recorridoInorden();
        assertEquals(4, orden.size(), "deben quedar 4 canciones");
        assertEquals("Alfa", orden.get(0).getNombre(), "primer elemento alfabetico");
        assertEquals("Bravo", orden.get(1).getNombre(), "segundo elemento alfabetico");
        assertEquals("Charlie", orden.get(2).getNombre(), "tercer elemento alfabetico");
        assertEquals("Delta", orden.get(3).getNombre(), "cuarto elemento alfabetico");
    }

    private static void testTamano() {
        ArbolBST<Cancion> arbol = new ArbolBST<>();
        arbol.insertar(c("Uno"));
        arbol.insertar(c("Dos"));
        arbol.insertar(c("Tres"));
        assertEquals(3, arbol.tamano(), "tamano debe reflejar las inserciones");
    }

    private static void testEliminarHoja() {
        ArbolBST<Cancion> arbol = new ArbolBST<>();
        Cancion bravo = c("Bravo");
        Cancion alfa = c("Alfa");
        Cancion charlie = c("Charlie");
        arbol.insertar(bravo);
        arbol.insertar(alfa);
        arbol.insertar(charlie);

        boolean eliminado = arbol.eliminar(alfa);
        assertTrue(eliminado, "eliminar una hoja debe retornar true");
        assertEquals(2, arbol.tamano(), "tamano debe bajar a 2");
        List<Cancion> orden = arbol.recorridoInorden();
        assertEquals(2, orden.size(), "solo deben quedar 2 canciones");
        assertEquals("Bravo", orden.get(0).getNombre(), "");
        assertEquals("Charlie", orden.get(1).getNombre(), "");
    }

    private static void testEliminarUnHijo() {
        ArbolBST<Cancion> arbol = new ArbolBST<>();
        Cancion bravo = c("Bravo");
        Cancion alfa = c("Alfa");
        arbol.insertar(bravo);
        arbol.insertar(alfa);

        boolean eliminado = arbol.eliminar(bravo);
        assertTrue(eliminado, "eliminar nodo con un hijo debe retornar true");
        assertEquals(1, arbol.tamano(), "tamano debe bajar a 1");
        assertEquals("Alfa", arbol.recorridoInorden().get(0).getNombre(), "debe quedar Alfa");
    }

    private static void testEliminarDosHijos() {
        ArbolBST<Cancion> arbol = new ArbolBST<>();
        Cancion delta = c("Delta");
        arbol.insertar(delta);
        arbol.insertar(c("Bravo"));
        arbol.insertar(c("Foxtrot"));
        arbol.insertar(c("Alfa"));
        arbol.insertar(c("Charlie"));
        arbol.insertar(c("Echo"));
        arbol.insertar(c("Golf"));

        boolean eliminado = arbol.eliminar(delta);
        assertTrue(eliminado, "eliminar la raiz con dos hijos debe retornar true");
        assertEquals(6, arbol.tamano(), "tamano debe bajar a 6");

        List<Cancion> orden = arbol.recorridoInorden();
        assertEquals(6, orden.size(), "deben quedar 6 canciones");
        assertEquals("Alfa", orden.get(0).getNombre(), "");
        assertEquals("Bravo", orden.get(1).getNombre(), "");
        assertEquals("Charlie", orden.get(2).getNombre(), "");
        assertEquals("Echo", orden.get(3).getNombre(), "sucesor inorden debe subir a la raiz");
        assertEquals("Foxtrot", orden.get(4).getNombre(), "");
        assertEquals("Golf", orden.get(5).getNombre(), "");
    }

    private static void testEliminarNoExiste() {
        ArbolBST<Cancion> arbol = new ArbolBST<>();
        arbol.insertar(c("Alfa"));
        assertTrue(!arbol.eliminar(c("Zulu")), "eliminar un valor que no existe debe retornar false");
        assertEquals(1, arbol.tamano(), "tamano no debe cambiar si no se elimino nada");
    }

    private static void testLimpiar() {
        ArbolBST<Cancion> arbol = new ArbolBST<>();
        arbol.insertar(c("Alfa"));
        arbol.insertar(c("Bravo"));
        arbol.limpiar();
        assertTrue(arbol.estaVacio(), "limpiar debe dejar el arbol vacio");
        assertEquals(0, arbol.tamano(), "limpiar debe reiniciar tamano a 0");
    }

    private static void testModoAlfabetico() {
        ModoAlfabetico modo = new ModoAlfabetico();
        assertEquals("Orden alfabético", modo.nombreModo(), "");

        Cancion a = c("Bravo");
        Cancion b = c("Alfa");
        Cancion d = c("Charlie");
        modo.agregarCancion(a);
        modo.agregarCancion(b);
        modo.agregarCancion(d);

        assertEquals("Alfa", modo.actual().getNombre(), "actual() debe iniciar en la primera alfabeticamente");
        assertEquals("Bravo", modo.siguiente().getNombre(), "siguiente() debe respetar el inorden");
        assertEquals("Charlie", modo.siguiente().getNombre(), "");

        assertTrue(modo.eliminarCancion(a), "eliminarCancion debe delegar en el arbol");
        assertEquals(2, modo.tamano(), "tamano debe reflejar la eliminacion");
    }
}
