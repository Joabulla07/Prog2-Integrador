package concesionaria.interfaces;

/**
 * Interfaz para identificación de entidades mediante un código único.
 * Permite obtener el código de inventario que identifica un vehículo en el sistema.
 */
public interface Identificable {

    /**
     * Obtiene el código de inventario único del vehículo.
     *
     * @return código de inventario (ej. "AUTO-001", "CAM-001", "MOTO-001")
     */
    String getCodigoInventario();
}
