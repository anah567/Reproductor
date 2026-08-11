import estructuras.ColaSimple;
import estructuras.ListaCircularDoble;
import modelo.Cancion;
import modos.ModoAleatorio;
import modos.ModoOrdenLlegada;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PruebasModulo2 {

    private static int pruebasOk = 0;
    private static int pruebasFallidas = 0;

    public static void main(String[] args) {
        ejecutar("Cancion: calificacion invalida lanza excepcion", PruebasModulo2::testCalificacionInvalida);
        ejecutar("Cancion: equals/hashCode se basan en id", PruebasModulo2::testEqualsHashCode);
        ejecutar("Cancion: nombre vacio lanza excepcion", PruebasModulo2::testNombreVacio);
        ejecutar("Cancion: artista vacio lanza excepcion", PruebasModulo2::testArtistaVacio);
        ejecutar("Cancion: duracion invalida lanza excepcion", PruebasModulo2::testDuracionInvalida);
        ejecutar("Cancion: anio invalido lanza excepcion", PruebasModulo2::testAnioInvalido);
        ejecutar("Cancion: album nulo lanza excepcion", PruebasModulo2::testAlbumNulo);
        ejecutar("Cancion: genero nulo lanza excepcion", PruebasModulo2::testGeneroNulo);

        ejecutar("ListaCircularDoble: vacia", PruebasModulo2::testListaVacia);
        ejecutar("ListaCircularDoble: un solo elemento", PruebasModulo2::testListaUnElemento);
        ejecutar("ListaCircularDoble: navegacion circular hacia adelante", PruebasModulo2::testListaSiguienteCircular);
        ejecutar("ListaCircularDoble: navegacion circular hacia atras", PruebasModulo2::testListaAnteriorCircular);
        ejecutar("ListaCircularDoble: eliminar la cancion actual", PruebasModulo2::testListaEliminarActual);
        ejecutar("ListaCircularDoble: eliminar cabeza y cola", PruebasModulo2::testListaEliminarCabezaYCola);
        ejecutar("ListaCircularDoble: toList preserva orden", PruebasModulo2::testListaToList);
        ejecutar("ListaCircularDoble: mezclar conserva todas las canciones", PruebasModulo2::testListaMezclarConservaCanciones);
        ejecutar("ListaCircularDoble: limpiar deja la lista vacia", PruebasModulo2::testListaLimpiar);

        ejecutar("ColaSimple: vacia", PruebasModulo2::testColaVacia);
        ejecutar("ColaSimple: orden FIFO exacto", PruebasModulo2::testColaOrdenFifo);
        ejecutar("ColaSimple: anterior lanza excepcion", PruebasModulo2::testColaAnteriorLanzaExcepcion);
        ejecutar("ColaSimple: eliminar cancion pendiente", PruebasModulo2::testColaEliminarPendiente);
        ejecutar("ColaSimple: un solo elemento", PruebasModulo2::testColaUnElemento);
        ejecutar("ColaSimple: limpiar deja la cola vacia", PruebasModulo2::testColaLimpiar);

        ejecutar("ModoAleatorio: delega correctamente en ListaCircularDoble", PruebasModulo2::testModoAleatorio);
        ejecutar("ModoAleatorio: mezclar cambia el orden pero conserva las canciones", PruebasModulo2::testModoAleatorioMezclar);
        ejecutar("ModoOrdenLlegada: delega correctamente en ColaSimple", PruebasModulo2::testModoOrdenLlegada);

        System.out.println();
        System.out.println("Resultado: " + pruebasOk + " ok, " + pruebasFallidas + " fallidas");
        if (pruebasFallidas > 0) {
            System.exit(1);
        }
    }

    private interface Prueba {
        void ejecutar();
    }

    private static void ejecutar(String nombre, Prueba prueba) {
        try {
            prueba.ejecutar();
            System.out.println("[OK]     " + nombre);
            pruebasOk++;
        } catch (AssertionError | RuntimeException e) {
            System.out.println("[FALLO]  " + nombre + " -> " + e);
            pruebasFallidas++;
        }
    }

    private static Cancion cancion(String nombre) {
        return new Cancion(nombre, "Artista " + nombre, "Album " + nombre, 180, "Rock", 2020, 80);
    }

    private static void assertTrue(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static void assertEquals(Object esperado, Object real, String mensaje) {
        if (esperado == null ? real != null : !esperado.equals(real)) {
            throw new AssertionError(mensaje + " (esperado=" + esperado + ", real=" + real + ")");
        }
    }

    private static void testCalificacionInvalida() {
        boolean lanzo = false;
        try {
            new Cancion("X", "A", "B", 100, "Pop", 2020, 150);
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        assertTrue(lanzo, "Se esperaba IllegalArgumentException con calificacion fuera de rango");
    }

    private static void testEqualsHashCode() {
        Cancion a = cancion("Igual");
        Cancion b = new Cancion(a.getId(), "Otro nombre", "Otro artista", "Otro album", 999, "Jazz", 1999, 10);
        assertTrue(a.equals(b), "Canciones con el mismo id deben ser iguales aunque difieran en otros atributos");
        assertEquals(a.hashCode(), b.hashCode(), "hashCode debe coincidir para el mismo id");

        Cancion c = cancion("Distinta");
        assertTrue(!a.equals(c), "Canciones con id distinto no deben ser iguales");
    }

    private static void testNombreVacio() {
        boolean lanzo = false;
        try {
            new Cancion("  ", "Artista", "Album", 180, "Rock", 2020, 80);
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        assertTrue(lanzo, "Se esperaba IllegalArgumentException con nombre vacio o en blanco");
    }

    private static void testArtistaVacio() {
        boolean lanzo = false;
        try {
            new Cancion("Nombre", "", "Album", 180, "Rock", 2020, 80);
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        assertTrue(lanzo, "Se esperaba IllegalArgumentException con artista vacio");
    }

    private static void testDuracionInvalida() {
        boolean lanzo = false;
        try {
            new Cancion("Nombre", "Artista", "Album", 0, "Rock", 2020, 80);
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        assertTrue(lanzo, "Se esperaba IllegalArgumentException con duracion menor o igual a 0");
    }

    private static void testAnioInvalido() {
        boolean lanzo = false;
        try {
            new Cancion("Nombre", "Artista", "Album", 180, "Rock", 3000, 80);
        } catch (IllegalArgumentException e) {
            lanzo = true;
        }
        assertTrue(lanzo, "Se esperaba IllegalArgumentException con anio de lanzamiento fuera de rango");
    }

    private static void testAlbumNulo() {
        boolean lanzo = false;
        try {
            new Cancion("Nombre", "Artista", null, 180, "Rock", 2020, 80);
        } catch (NullPointerException e) {
            lanzo = true;
        }
        assertTrue(lanzo, "Se esperaba NullPointerException con album nulo");
    }

    private static void testGeneroNulo() {
        boolean lanzo = false;
        try {
            new Cancion("Nombre", "Artista", "Album", 180, null, 2020, 80);
        } catch (NullPointerException e) {
            lanzo = true;
        }
        assertTrue(lanzo, "Se esperaba NullPointerException con genero nulo");
    }

    private static void testListaVacia() {
        ListaCircularDoble lista = new ListaCircularDoble();
        assertTrue(lista.estaVacia(), "Lista recien creada debe estar vacia");
        assertEquals(0, lista.tamano(), "tamano de lista vacia debe ser 0");
        assertEquals(null, lista.actual(), "actual() en lista vacia debe ser null");
        assertEquals(null, lista.siguiente(), "siguiente() en lista vacia debe ser null");
        assertEquals(null, lista.anterior(), "anterior() en lista vacia debe ser null");
        assertTrue(lista.toList().isEmpty(), "toList() en lista vacia debe ser lista vacia");
        assertTrue(!lista.eliminar(cancion("Fantasma")), "eliminar() en lista vacia debe retornar false");
    }

    private static void testListaUnElemento() {
        ListaCircularDoble lista = new ListaCircularDoble();
        Cancion unica = cancion("Unica");
        lista.agregar(unica);

        assertEquals(unica, lista.actual(), "actual() debe ser la unica cancion agregada");
        assertEquals(unica, lista.siguiente(), "siguiente() con un elemento debe devolver el mismo elemento");
        assertEquals(unica, lista.siguiente(), "siguiente() repetido con un elemento sigue devolviendo el mismo");
        assertEquals(unica, lista.anterior(), "anterior() con un elemento debe devolver el mismo elemento");
    }

    private static void testListaSiguienteCircular() {
        ListaCircularDoble lista = new ListaCircularDoble();
        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        Cancion c3 = cancion("C3");
        lista.agregar(c1);
        lista.agregar(c2);
        lista.agregar(c3);

        assertEquals(c1, lista.actual(), "actual() inicial debe ser la primera agregada (c1)");
        assertEquals(c2, lista.siguiente(), "primer siguiente() debe ir a c2");
        assertEquals(c3, lista.siguiente(), "segundo siguiente() debe ir a c3");
        assertEquals(c1, lista.siguiente(), "tercer siguiente() debe volver circularmente a c1");

        assertEquals(c2, lista.siguiente(), "cuarto siguiente() debe ir a c2 de nuevo");
        assertEquals(c3, lista.siguiente(), "quinto siguiente() debe ir a c3 de nuevo");
    }

    private static void testListaAnteriorCircular() {
        ListaCircularDoble lista = new ListaCircularDoble();
        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        Cancion c3 = cancion("C3");
        lista.agregar(c1);
        lista.agregar(c2);
        lista.agregar(c3);

        assertEquals(c1, lista.actual(), "actual() inicial debe ser c1");
        assertEquals(c3, lista.anterior(), "anterior() desde c1 debe dar la vuelta circularmente a c3");
        assertEquals(c2, lista.anterior(), "anterior() desde c3 debe ir a c2");
        assertEquals(c1, lista.anterior(), "anterior() desde c2 debe ir a c1");

        assertEquals(c3, lista.anterior(), "retroceder de nuevo desde c1 debe volver a c3");
    }

    private static void testListaEliminarActual() {
        ListaCircularDoble lista = new ListaCircularDoble();
        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        Cancion c3 = cancion("C3");
        lista.agregar(c1);
        lista.agregar(c2);
        lista.agregar(c3);

        lista.siguiente();
        assertEquals(c2, lista.actual(), "actual() debe ser c2 antes de eliminar");

        boolean eliminado = lista.eliminar(c2);
        assertTrue(eliminado, "eliminar(c2) debe retornar true");
        assertEquals(2, lista.tamano(), "tamano debe reducirse a 2 tras eliminar");
        assertEquals(c3, lista.actual(), "al eliminar la cancion actual, el puntero debe reubicarse en la siguiente (c3)");

        assertEquals(c1, lista.siguiente(), "tras eliminar c2, la navegacion circular debe ser c3 -> c1");
        assertEquals(c3, lista.siguiente(), "y de c1 debe volver circularmente a c3");

        lista.eliminar(c3);
        lista.eliminar(c1);
        assertTrue(lista.estaVacia(), "la lista debe quedar vacia tras eliminar todos los elementos");
        assertEquals(null, lista.actual(), "actual() debe ser null cuando la lista queda vacia");
    }

    private static void testListaEliminarCabezaYCola() {
        ListaCircularDoble lista = new ListaCircularDoble();
        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        Cancion c3 = cancion("C3");
        lista.agregar(c1);
        lista.agregar(c2);
        lista.agregar(c3);

        assertTrue(lista.eliminar(c1), "eliminar la cabeza (c1) debe funcionar");
        assertEquals(2, lista.tamano(), "tamano debe ser 2 tras eliminar la cabeza");
        List<Cancion> restantes = lista.toList();
        assertEquals(2, restantes.size(), "toList debe reflejar el tamano tras eliminar la cabeza");

        assertTrue(lista.eliminar(c3), "eliminar la cola (c3) debe funcionar");
        assertEquals(1, lista.tamano(), "tamano debe ser 1 tras eliminar la cola");
        assertEquals(c2, lista.actual(), "solo debe quedar c2");
    }

    private static void testListaToList() {
        ListaCircularDoble lista = new ListaCircularDoble();
        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        Cancion c3 = cancion("C3");
        lista.agregar(c1);
        lista.agregar(c2);
        lista.agregar(c3);

        List<Cancion> resultado = lista.toList();
        assertEquals(3, resultado.size(), "toList debe tener 3 elementos");
        assertEquals(c1, resultado.get(0), "orden de toList debe iniciar en c1");
        assertEquals(c2, resultado.get(1), "orden de toList debe seguir con c2");
        assertEquals(c3, resultado.get(2), "orden de toList debe terminar con c3");
    }

    private static void testListaMezclarConservaCanciones() {
        ListaCircularDoble lista = new ListaCircularDoble();
        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        Cancion c3 = cancion("C3");
        Cancion c4 = cancion("C4");
        Cancion c5 = cancion("C5");
        lista.agregar(c1);
        lista.agregar(c2);
        lista.agregar(c3);
        lista.agregar(c4);
        lista.agregar(c5);

        lista.mezclar();

        assertEquals(5, lista.tamano(), "mezclar no debe cambiar el numero de canciones");
        List<Cancion> resultado = lista.toList();
        Set<String> idsEsperados = new HashSet<>();
        idsEsperados.add(c1.getId());
        idsEsperados.add(c2.getId());
        idsEsperados.add(c3.getId());
        idsEsperados.add(c4.getId());
        idsEsperados.add(c5.getId());
        Set<String> idsReales = new HashSet<>();
        for (Cancion c : resultado) {
            idsReales.add(c.getId());
        }
        assertEquals(idsEsperados, idsReales, "mezclar debe conservar exactamente las mismas canciones");

        ListaCircularDoble listaUnElemento = new ListaCircularDoble();
        listaUnElemento.agregar(c1);
        listaUnElemento.mezclar();
        assertEquals(c1, listaUnElemento.actual(), "mezclar con un solo elemento no debe romper la lista");
    }

    private static void testListaLimpiar() {
        ListaCircularDoble lista = new ListaCircularDoble();
        lista.agregar(cancion("C1"));
        lista.agregar(cancion("C2"));

        lista.limpiar();

        assertTrue(lista.estaVacia(), "limpiar debe dejar la lista vacia");
        assertEquals(0, lista.tamano(), "limpiar debe reiniciar el tamano a 0");
        assertEquals(null, lista.actual(), "actual() tras limpiar debe ser null");
    }

    private static void testColaVacia() {
        ColaSimple cola = new ColaSimple();
        assertTrue(cola.estaVacia(), "cola recien creada debe estar vacia");
        assertEquals(0, cola.tamano(), "tamano de cola vacia debe ser 0");
        assertEquals(null, cola.actual(), "actual() en cola vacia (sin reproducir nada) debe ser null");
        assertEquals(null, cola.siguiente(), "siguiente() en cola vacia debe ser null");
        assertTrue(cola.toList().isEmpty(), "toList() en cola vacia debe ser lista vacia");
    }

    private static void testColaOrdenFifo() {
        ColaSimple cola = new ColaSimple();
        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        Cancion c3 = cancion("C3");
        cola.encolar(c1);
        cola.encolar(c2);
        cola.encolar(c3);

        assertEquals(3, cola.tamano(), "tamano debe ser 3 tras encolar 3 canciones");
        assertEquals(c1, cola.siguiente(), "primer siguiente() debe desencolar c1 (orden exacto de llegada)");
        assertEquals(c1, cola.actual(), "actual() debe ser la ultima reproducida (c1)");
        assertEquals(c2, cola.siguiente(), "segundo siguiente() debe desencolar c2");
        assertEquals(c3, cola.siguiente(), "tercer siguiente() debe desencolar c3");
        assertTrue(cola.estaVacia(), "cola debe quedar vacia tras desencolar todo");
        assertEquals(null, cola.siguiente(), "siguiente() en cola ya vacia debe ser null");
    }

    private static void testColaAnteriorLanzaExcepcion() {
        ColaSimple cola = new ColaSimple();
        cola.encolar(cancion("C1"));
        boolean lanzo = false;
        try {
            cola.anterior();
        } catch (UnsupportedOperationException e) {
            lanzo = true;
            assertEquals("No se puede retroceder en modo cola", e.getMessage(),
                    "mensaje de excepcion debe coincidir con el especificado");
        }
        assertTrue(lanzo, "anterior() en ColaSimple siempre debe lanzar UnsupportedOperationException");
    }

    private static void testColaEliminarPendiente() {
        ColaSimple cola = new ColaSimple();
        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        Cancion c3 = cancion("C3");
        cola.encolar(c1);
        cola.encolar(c2);
        cola.encolar(c3);

        assertTrue(cola.eliminar(c2), "eliminar cancion pendiente del medio debe funcionar");
        assertEquals(2, cola.tamano(), "tamano debe reducirse tras eliminar");
        List<Cancion> pendientes = cola.toList();
        assertEquals(2, pendientes.size(), "toList debe reflejar la eliminacion");
        assertEquals(c1, pendientes.get(0), "orden FIFO debe mantenerse tras eliminar del medio");
        assertEquals(c3, pendientes.get(1), "c3 debe seguir siendo la ultima pendiente");

        Cancion reproducida = cola.siguiente();
        assertEquals(c1, reproducida, "siguiente cancion en sonar debe seguir siendo c1");

        assertTrue(!cola.eliminar(c1), "no se puede eliminar una cancion que ya fue reproducida (no esta pendiente)");
    }

    private static void testColaUnElemento() {
        ColaSimple cola = new ColaSimple();
        Cancion unica = cancion("Unica");
        cola.encolar(unica);

        assertEquals(1, cola.tamano(), "tamano debe ser 1 con un elemento");
        assertEquals(unica, cola.siguiente(), "siguiente() debe devolver la unica cancion");
        assertTrue(cola.estaVacia(), "cola debe quedar vacia tras reproducir la unica cancion");
        assertEquals(unica, cola.actual(), "actual() debe seguir mostrando la ultima reproducida");
    }

    private static void testColaLimpiar() {
        ColaSimple cola = new ColaSimple();
        cola.encolar(cancion("C1"));
        cola.encolar(cancion("C2"));
        cola.siguiente();

        cola.limpiar();

        assertTrue(cola.estaVacia(), "limpiar debe dejar la cola vacia");
        assertEquals(0, cola.tamano(), "limpiar debe reiniciar el tamano a 0");
        assertEquals(null, cola.actual(), "actual() tras limpiar debe ser null");
    }

    private static void testModoAleatorio() {
        ModoAleatorio modo = new ModoAleatorio();
        assertEquals("Aleatorio", modo.nombreModo(), "nombreModo() debe ser 'Aleatorio'");

        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        modo.agregarCancion(c1);
        modo.agregarCancion(c2);

        assertEquals(c1, modo.actual(), "actual() inicial del modo debe ser c1");
        assertEquals(c2, modo.siguiente(), "siguiente() debe delegar en la lista circular");
        assertEquals(c1, modo.siguiente(), "navegacion circular debe funcionar a traves del modo");
        assertEquals(2, modo.obtenerTodas().size(), "obtenerTodas() debe reflejar las canciones agregadas");

        assertTrue(modo.eliminarCancion(c1), "eliminarCancion() debe delegar en la lista circular");
        assertEquals(1, modo.obtenerTodas().size(), "obtenerTodas() debe reflejar la eliminacion");
    }

    private static void testModoAleatorioMezclar() {
        ModoAleatorio modo = new ModoAleatorio();
        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        Cancion c3 = cancion("C3");
        Cancion c4 = cancion("C4");
        modo.agregarCancion(c1);
        modo.agregarCancion(c2);
        modo.agregarCancion(c3);
        modo.agregarCancion(c4);

        modo.mezclar();

        assertEquals(4, modo.obtenerTodas().size(), "mezclar no debe perder canciones del modo");
        Set<String> idsEsperados = new HashSet<>();
        idsEsperados.add(c1.getId());
        idsEsperados.add(c2.getId());
        idsEsperados.add(c3.getId());
        idsEsperados.add(c4.getId());
        Set<String> idsReales = new HashSet<>();
        for (Cancion c : modo.obtenerTodas()) {
            idsReales.add(c.getId());
        }
        assertEquals(idsEsperados, idsReales, "mezclar debe conservar exactamente las mismas canciones del modo");
    }

    private static void testModoOrdenLlegada() {
        ModoOrdenLlegada modo = new ModoOrdenLlegada();
        assertEquals("Orden de llegada", modo.nombreModo(), "nombreModo() debe ser 'Orden de llegada'");

        Cancion c1 = cancion("C1");
        Cancion c2 = cancion("C2");
        modo.agregarCancion(c1);
        modo.agregarCancion(c2);

        assertEquals(c1, modo.siguiente(), "siguiente() debe delegar en la cola FIFO");
        assertEquals(c1, modo.actual(), "actual() debe reflejar la ultima reproducida");

        boolean lanzo = false;
        try {
            modo.anterior();
        } catch (UnsupportedOperationException e) {
            lanzo = true;
        }
        assertTrue(lanzo, "anterior() debe propagar la excepcion de ColaSimple");

        assertEquals(1, modo.obtenerTodas().size(), "obtenerTodas() solo debe mostrar canciones pendientes (c2)");
    }
}
