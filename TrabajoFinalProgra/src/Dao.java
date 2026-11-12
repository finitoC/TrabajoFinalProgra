

import java.util.List;

/// INTERFAZ GENÉRICA: operaciones CRUD que implementa cada DAO para su entidad.



public interface Dao<T> {

    /** Inserta la entidad, le asigna su id y lo devuelve. */
    int guardar(T entidad) throws GimnasioException;

    T buscarPorId(int id) throws GimnasioException;

    List<T> listar() throws GimnasioException;

}
