package concesionaria.interfaces;

/**
 * Interfaz para comparación de vehículos.
 * Permite comparar vehículos por diferentes criterios de ordenamiento.
 */
public interface Comparable {

    /**
     * Compara dos vehículos por precio.
     *
     * @param otro el vehículo con el que comparar
     * @return negativo si este vehículo es más barato, 0 si tienen el mismo precio,
     *         positivo si este vehículo es más caro
     */
    int compararPorPrecio(Comparable otro);

    /**
     * Compara dos vehículos por antigüedad (año).
     *
     * @param otro el vehículo con el que comparar
     * @return negativo si este vehículo es más nuevo, 0 si tienen el mismo año,
     *         positivo si este vehículo es más antiguo
     */
    int compararPorAntiguedad(Comparable otro);
}
