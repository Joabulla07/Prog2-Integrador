package concesionaria.interfaces;

import java.util.List;

/**
 * Interfaz para validación de vehículos.
 * Permite verificar reglas del dominio y obtener errores específicos.
 */
public interface Validable {

    /**
     * Verifica si el vehículo cumple todas las reglas del dominio.
     * Reglas validadas: año, kilometraje, estado, campos obligatorios.
     *
     * @return true si el vehículo es válido, false en caso contrario
     */
    boolean esVehiculoValido();

    /**
     * Obtiene la lista de errores de validación del vehículo.
     * Devuelve mensajes descriptivos para cada regla que no cumple.
     *
     * @return lista de errores de validación (vacía si es válido)
     */
    List<String> obtenerErroresValidacion();
}
