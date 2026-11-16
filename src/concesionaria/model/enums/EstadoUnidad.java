package concesionaria.model.enums;

/**
 * Enumeración que representa el estado de una unidad vehicular.
 * Clasifica las unidades en dos categorías: 0 km (nuevas) y usadas.
 */
public enum EstadoUnidad {

    /**
     * Unidad 0 km (nueva, sin kilometraje).
     */
    CERO_KM("0 km"),

    /**
     * Unidad usada (con kilometraje recorrido).
     */
    USADO("Usado");

    private final String descripcion;

    /**
     * Constructor privado del enum.
     *
     * @param descripcion descripción legible en español del estado de la unidad
     */
    EstadoUnidad(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción legible en español del estado de la unidad.
     *
     * @return descripción del estado de la unidad
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Convierte una cadena de texto en el valor correspondiente de EstadoUnidad.
     * Acepta el nombre del enum (mayúsculas/minúsculas), la descripción en español o variantes comunes.
     *
     * @param valor texto a convertir (ej. "CERO_KM", "0 km", "USADO", "Usado")
     * @return valor de EstadoUnidad correspondiente
     * @throws IllegalArgumentException si el texto no coincide con ningún valor válido
     */
    public static EstadoUnidad fromString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El valor no puede ser nulo o vacío");
        }

        String normalized = valor.trim().toUpperCase();

        for (EstadoUnidad estado : values()) {
            if (estado.name().equals(normalized) ||
                estado.descripcion.equalsIgnoreCase(valor.trim()) ||
                estado.descripcion.toUpperCase().equals(normalized)) {
                return estado;
            }
        }

        throw new IllegalArgumentException("Estado de unidad inválido: " + valor);
    }
}
