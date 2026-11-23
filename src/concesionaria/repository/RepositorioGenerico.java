package concesionaria.repository;

import concesionaria.interfaces.Identificable;

/**
 * Interfaz genérica que define el contrato CRUD básico para entidades identificables.
 * <p>
 * Esta interfaz parametrizada permite crear repositorios reutilizables para cualquier
 * entidad que implemente la interfaz Identificable, proporcionando operaciones estándar
 * de creación, lectura, actualización y eliminación.
 * </p>
 *
 * @param <T> el tipo de entidad, debe implementar Identificable
 * @param <ID> el tipo del identificador de la entidad
 */
public interface RepositorioGenerico<T extends Identificable, ID> {

    /**
     * Guarda una entidad en el repositorio.
     * <p>
     * Si la entidad no tiene identificador asignado, el repositorio debe generar uno
     * y asignarlo a la entidad antes de devolverla.
     * </p>
     *
     * @param entidad la entidad a guardar
     * @return la entidad guardada con su identificador asignado
     * @throws IllegalArgumentException si la entidad es null
     */
    T guardar(T entidad);

    /**
     * Busca una entidad por su identificador.
     *
     * @param codigo el identificador de la entidad a buscar
     * @return la entidad encontrada, o null si no existe
     * @throws IllegalArgumentException si el código es null
     */
    T buscarPorCodigo(ID codigo);

    /**
     * Lista todas las entidades persistidas en el repositorio.
     *
     * @return una colección con todas las entidades
     */
    java.util.List<T> listarTodos();

    /**
     * Elimina una entidad del repositorio por su identificador.
     *
     * @param codigo el identificador de la entidad a eliminar
     * @return true si la entidad fue eliminada, false si no existía
     * @throws IllegalArgumentException si el código es null
     */
    boolean eliminarPorCodigo(ID codigo);
}
