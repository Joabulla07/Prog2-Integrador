package concesionaria.exception;

/**
 * Excepción lanzada cuando los datos de un vehículo no cumplen las reglas del dominio.
 * Incluye información contextual sobre el campo inválido y su valor para facilitar debugging.
 */
public class DatoVehiculoInvalidoException extends Exception {

    private final String campoInvalido;
    private final String valorInvalido;

    /**
     * Constructor con mensaje, campo inválido y valor.
     *
     * @param mensaje descripción del error
     * @param campoInvalido nombre del campo que contiene el valor inválido
     * @param valorInvalido valor que causó el error
     */
    public DatoVehiculoInvalidoException(String mensaje, String campoInvalido, String valorInvalido) {
        super(mensaje);
        this.campoInvalido = campoInvalido;
        this.valorInvalido = valorInvalido;
    }

    /**
     * Constructor con mensaje, campo inválido, valor y causa.
     *
     * @param mensaje descripción del error
     * @param campoInvalido nombre del campo que contiene el valor inválido
     * @param valorInvalido valor que causó el error
     * @param causa excepción original que provocó este error
     */
    public DatoVehiculoInvalidoException(String mensaje, String campoInvalido, String valorInvalido, Throwable causa) {
        super(mensaje, causa);
        this.campoInvalido = campoInvalido;
        this.valorInvalido = valorInvalido;
    }

    /**
     * Obtiene el nombre del campo que contiene el valor inválido.
     *
     * @return nombre del campo inválido
     */
    public String getCampoInvalido() {
        return campoInvalido;
    }

    /**
     * Obtiene el valor que causó el error.
     *
     * @return valor inválido
     */
    public String getValorInvalido() {
        return valorInvalido;
    }
}
