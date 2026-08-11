package biblioteca;

import java.util.List;
import java.util.Optional;

public interface RepositorioCRUD<T> {

    void agregar(T item);

    boolean eliminar(T item);

    boolean editar(T existente, T nuevosDatos);

    Optional<T> buscarPorId(String id);

    List<T> obtenerTodos();
}
