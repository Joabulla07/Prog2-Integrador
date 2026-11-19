package concesionaria.interfaces;

/**
 * Interfaz para generación de reportes de vehículos.
 * Permite generar reportes ejecutivos, resúmenes legibles y detalles completos.
 */
public interface Reportable {

    /**
     * Obtiene la ficha directiva del vehículo.
     * Resumen ejecutivo con información clave para directivos:
     * marca, modelo, año, precio y estado.
     *
     * @return ficha directiva en formato texto
     */
    String obtenerFichaDirectiva();

    /**
     * Obtiene un resumen legible del vehículo.
     * Descripción corta y amigable para usuarios.
     *
     * @return resumen (ej. "Toyota Corolla 2026 - 0 km")
     */
    String obtenerResumen();

    /**
     * Obtiene el detalle completo del vehículo.
     * Todos los campos del vehículo en formato legible.
     *
     * @return detalle completo con todos los atributos
     */
    String obtenerDetalleCompleto();
}
