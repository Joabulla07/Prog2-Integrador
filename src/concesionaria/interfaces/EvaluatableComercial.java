package concesionaria.interfaces;

/**
 * Interfaz para evaluación comercial de vehículos.
 * Permite clasificar vehículos comercialmente y determinar su aptitud para entrega inmediata.
 */
public interface EvaluatableComercial {

    /**
     * Obtiene la categoría comercial del vehículo.
     * La categoría combina el tipo de vehículo con un rango de precio:
     * - Económico: precio &lt; 15.000.000
     * - Medio: precio entre 15.000.000 y 40.000.000
     * - Premium: precio > 40.000.000
     *
     * @return categoría comercial (ej. "Automóvil Premium 0 km", "Camioneta Media Usada")
     */
    String obtenerCategoriaComercial();

    /**
     * Determina si el vehículo es apto para entrega inmediata.
     * - Vehículos 0 km: siempre aptos
     * - Vehículos usados: aptos si kilometraje &lt; 50.000 km Y antigüedad &lt;= 5 años
     *
     * @return true si es apto para entrega inmediata, false en caso contrario
     */
    boolean esAptoEntregaInmediata();
}
