package vista;

import modelo.Cancion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class PersistenciaCanciones {

    private static final String SEPARADOR = "\\|";
    private static final String NOMBRE_ARCHIVO = "biblioteca_datos.csv";

    private PersistenciaCanciones() {
    }

    public static void guardar(List<Cancion> canciones) {
        try (PrintWriter escritor = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(NOMBRE_ARCHIVO), StandardCharsets.UTF_8))) {
            for (Cancion c : canciones) {
                escritor.println(construirLinea(c));
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar la biblioteca: " + e.getMessage());
        }
    }

    private static String construirLinea(Cancion c) {
        String portada = c.getRutaPortada() == null ? "" : c.getRutaPortada();
        return c.getId() + "|"
                + c.getNombre() + "|"
                + c.getArtista() + "|"
                + c.getAlbum() + "|"
                + c.getDuracionSegundos() + "|"
                + c.getGenero() + "|"
                + c.getAnioLanzamiento() + "|"
                + c.getCalificacion() + "|"
                + portada;
    }

    public static List<Cancion> cargar() {
        List<Cancion> resultado = new ArrayList<>();
        File archivo = new File(NOMBRE_ARCHIVO);
        if (!archivo.exists()) {
            return resultado;
        }
        try (BufferedReader lector = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                Cancion c = interpretarLinea(linea);
                if (c != null) {
                    resultado.add(c);
                }
            }
        } catch (IOException e) {
            System.err.println("No se pudo cargar la biblioteca: " + e.getMessage());
        }
        return resultado;
    }

    private static Cancion interpretarLinea(String linea) {
        if (linea == null || linea.isBlank()) {
            return null;
        }
        String[] partes = linea.split(SEPARADOR, -1);
        if (partes.length < 8) {
            return null;
        }
        try {
            String id = partes[0];
            String nombre = partes[1];
            String artista = partes[2];
            String album = partes[3];
            int duracion = Integer.parseInt(partes[4]);
            String genero = partes[5];
            int anio = Integer.parseInt(partes[6]);
            int calificacion = Integer.parseInt(partes[7]);
            String portada = (partes.length > 8 && !partes[8].isEmpty()) ? partes[8] : null;

            Cancion c = new Cancion(id, nombre, artista, album, duracion, genero, anio, calificacion);
            c.setRutaPortada(portada);
            return c;
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static boolean existeArchivo() {
        return new File(NOMBRE_ARCHIVO).exists();
    }
}
