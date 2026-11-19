package concesionaria.interfaces;

/**
 * Interfaz para valoración financiera de vehículos.
 * Permite calcular valor de mercado, depreciación y margen de ganancia.
 */
public interface Valuable {

    /**
     * Obtiene el valor de mercado del vehículo.
     * - Vehículos 0 km: devuelve el precio lista sin ajustes
     * - Vehículos usados: devuelve el precio lista ajustado por depreciación
     *
     * @return valor de mercado del vehículo
     */
    double obtenerValorMercado();

    /**
     * Calcula el porcentaje de depreciación del vehículo.
     * Fórmula: 10% por año de antigüedad + 5% por cada 10.000 km
     * Máximo 70% de depreciación (valor mínimo 30% del precio lista).
     *
     * @return porcentaje de depreciación (0.0 a 0.70)
     */
    double calcularDepreciacion();

    /**
     * Obtiene el margen de ganancia del vehículo.
     * - Vehículos 0 km: 0% (no hay precio de compra)
     * - Vehículos usados: 20% sobre precio de compra (fijo por simplicidad)
     *
     * @return margen de ganancia como porcentaje (0.0 a 1.0)
     */
    double obtenerMargenGanancia();
}
