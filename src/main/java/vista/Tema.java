package vista;

public class Tema {

    private static boolean oscuro = true;

    public static boolean esOscuro() {
        return oscuro;
    }

    public static void cambiarTema() {
        oscuro = !oscuro;
    }
}