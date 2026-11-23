package concesionaria.repository;

import concesionaria.interfaces.Identificable;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase genérica que proporciona un índice en memoria respaldado por un Map con tipado seguro.
 * <p>
 * Esta clase es una utilidad de indexación que usa internamente un HashMap<K, V> para almacenar
 * entidades identificables. No mantiene sincronización con la base de datos; es una estructura
 * auxiliar para consultas eficientes en memoria.
 * </p>
 *
 * @param <K> el tipo de clave para indexar las entidades
 * @param <V> el tipo de valor, debe implementar Identificable
 */
public class IndiceGenerico<K, V extends Identificable> {

    private final Map<K, V> indice;

    /**
     * Crea un nuevo índice genérico vacío.
     */
    public IndiceGenerico() {
        this.indice = new HashMap<>();
    }

    /**
     * Agrega una entidad al índice con la clave especificada.
     * <p>
     * Si ya existe una entidad con la misma clave, esta será reemplazada.
     * </p>
     *
     * @param clave la clave bajo la cual se almacenará la entidad
     * @param valor la entidad a almacenar
     * @throws IllegalArgumentException si la clave o el valor son null
     */
    public void agregar(K clave, V valor) {
        if (clave == null) {
            throw new IllegalArgumentException("La clave no puede ser null");
        }
        if (valor == null) {
            throw new IllegalArgumentException("El valor no puede ser null");
        }
        indice.put(clave, valor);
    }

    /**
     * Busca una entidad en el índice por su clave.
     *
     * @param clave la clave de la entidad a buscar
     * @return la entidad asociada a la clave, o null si no existe
     */
    public V buscar(K clave) {
        return indice.get(clave);
    }

    /**
     * Verifica si existe una clave en el índice.
     *
     * @param clave la clave a verificar
     * @return true si la clave existe, false en caso contrario
     */
    public boolean contiene(K clave) {
        return indice.containsKey(clave);
    }

    /**
     * Elimina una entidad del índice por su clave.
     *
     * @param clave la clave de la entidad a eliminar
     * @return la entidad eliminada, o null si no existía
     */
    public V eliminar(K clave) {
        return indice.remove(clave);
    }

    /**
     * Obtiene la cantidad de entidades almacenadas en el índice.
     *
     * @return el número de entidades en el índice
     */
    public int tamaño() {
        return indice.size();
    }

    /**
     * Verifica si el índice está vacío.
     *
     * @return true si el índice no contiene entidades, false en caso contrario
     */
    public boolean estaVacio() {
        return indice.isEmpty();
    }
}
