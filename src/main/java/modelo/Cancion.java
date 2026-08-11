package modelo;

import java.time.Year;
import java.util.Objects;
import java.util.UUID;

public class Cancion implements Comparable<Cancion> {

    private final String id;
    private String nombre;
    private String artista;
    private String album;
    private int duracionSegundos;
    private String genero;
    private int anioLanzamiento;
    private int calificacion;
    private String rutaPortada;

    public Cancion(String id, String nombre, String artista, String album,
                   int duracionSegundos, String genero, int anioLanzamiento,
                   int calificacion) {
        this.id = Objects.requireNonNull(id, "id no puede ser nulo");
        setNombre(nombre);
        setArtista(artista);
        setAlbum(album);
        setDuracionSegundos(duracionSegundos);
        setGenero(genero);
        setAnioLanzamiento(anioLanzamiento);
        setCalificacion(calificacion);
    }

    public Cancion(String nombre, String artista, String album,
                   int duracionSegundos, String genero, int anioLanzamiento,
                   int calificacion) {
        this(UUID.randomUUID().toString(), nombre, artista, album,
                duracionSegundos, genero, anioLanzamiento, calificacion);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la cancion no puede estar vacio");
        }
        this.nombre = nombre;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        if (artista == null || artista.isBlank()) {
            throw new IllegalArgumentException("El artista no puede estar vacio");
        }
        this.artista = artista;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = Objects.requireNonNull(album, "el album no puede ser nulo");
    }

    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(int duracionSegundos) {
        if (duracionSegundos <= 0) {
            throw new IllegalArgumentException(
                    "La duracion debe ser mayor que 0, se recibio: " + duracionSegundos);
        }
        this.duracionSegundos = duracionSegundos;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = Objects.requireNonNull(genero, "el genero no puede ser nulo");
    }

    public int getAnioLanzamiento() {
        return anioLanzamiento;
    }

    public void setAnioLanzamiento(int anioLanzamiento) {
        int anioMaximo = Year.now().getValue() + 1;
        if (anioLanzamiento <= 0 || anioLanzamiento > anioMaximo) {
            throw new IllegalArgumentException(
                    "El anio de lanzamiento debe estar entre 1 y " + anioMaximo
                            + ", se recibio: " + anioLanzamiento);
        }
        this.anioLanzamiento = anioLanzamiento;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        if (calificacion < 0 || calificacion > 100) {
            throw new IllegalArgumentException(
                    "La calificacion debe estar entre 0 y 100, se recibio: " + calificacion);
        }
        this.calificacion = calificacion;
    }

    public String getRutaPortada() {
        return rutaPortada;
    }

    public void setRutaPortada(String rutaPortada) {
        this.rutaPortada = rutaPortada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cancion)) return false;
        Cancion cancion = (Cancion) o;
        return id.equals(cancion.id);
    }

    @Override
    public int compareTo(Cancion otraCancion) {

        int comparacionNombre =
                this.nombre.compareToIgnoreCase(otraCancion.nombre);

        if (comparacionNombre != 0) {
            return comparacionNombre;
        }

        return this.id.compareTo(otraCancion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cancion{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", artista='" + artista + '\'' +
                ", album='" + album + '\'' +
                ", duracionSegundos=" + duracionSegundos +
                ", genero='" + genero + '\'' +
                ", anioLanzamiento=" + anioLanzamiento +
                ", calificacion=" + calificacion +
                '}';
    }
}
