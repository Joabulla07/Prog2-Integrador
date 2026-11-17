package concesionaria.exception;

/**
 * Excepción lanzada cuando no se encuentra un vehículo por su código de inventario.
 * Incluye el código buscado para facilitar debugging y mensajes de error.
 */
public class VehiculoNoEncontradoException extends Exception {

    private final String codigoBuscado;

    /**
     * Constructor con mensaje y código buscado.
     *
     * @param mensaje descripción del error
     * @param codigoBuscado código de inventario que no se encontró
     */
    public VehiculoNoEncontradoException(String mensaje, String codigoBuscado) {
        super(mensaje);
        this.codigoBuscado = codigoBuscado;
    }

    /**
     * Constructor con mensaje, código buscado y causa.
     *
     * @param mensaje descripción del error
     * @param codigoBuscado código de inventario que no se encontró
     * @param causa excepción original que provocó este error
     */
    public VehiculoNoEncontradoException(String mensaje, String codigoBuscado, Throwable causa) {
        super(mensaje, causa);
        this.codigoBuscado = codigoBuscado;
    }

    /**
     * Obtiene el código de inventario que se buscó sin éxito.
     *
     * @return código de inventario buscado
     */
    public String getCodigoBuscado() {
        return codigoBuscado;
    }
}
