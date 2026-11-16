package concesionaria.model.enums;

/**
 * Enumeración que representa el tipo de vehículo en el sistema de concesionaria.
 * Clasifica los vehículos en tres familias: automóviles, camionetas y motocicletas.
 */
public enum TipoVehiculo {

    /**
     * Vehículo tipo automóvil.
     */
    AUTOMOVIL("Automóvil"),

    /**
     * Vehículo tipo camioneta.
     */
    CAMIONETA("Camioneta"),

    /**
     * Vehículo tipo motocicleta.
     */
    MOTOCICLETA("Motocicleta");

    private final String descripcion;

    /**
     * Constructor privado del enum.
     *
     * @param descripcion descripción legible en español del tipo de vehículo
     */
    TipoVehiculo(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción legible en español del tipo de vehículo.
     *
     * @return descripción del tipo de vehículo
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Convierte una cadena de texto en el valor correspondiente de TipoVehiculo.
     * Acepta el nombre del enum (mayúsculas/minúsculas) o la descripción en español.
     *
     * @param valor texto a convertir (ej. "AUTOMOVIL", "automovil", "Automóvil")
     * @return valor de TipoVehiculo correspondiente
     * @throws IllegalArgumentException si el texto no coincide con ningún valor válido
     */
    public static TipoVehiculo fromString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El valor no puede ser nulo o vacío");
        }

        String normalized = valor.trim().toUpperCase();

        for (TipoVehiculo tipo : values()) {
            if (tipo.name().equals(normalized) ||
                tipo.descripcion.equalsIgnoreCase(valor.trim()) ||
                tipo.descripcion.toUpperCase().equals(normalized)) {
                return tipo;
            }
        }

        throw new IllegalArgumentException("Tipo de vehículo inválido: " + valor);
    }
}
