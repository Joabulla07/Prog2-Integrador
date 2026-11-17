package concesionaria.exception;

/**
 * Excepción lanzada cuando falla una operación de base de datos con Sql2o/MSSQL.
 * Preserva la causa original y la operación que falló para facilitar debugging.
 * Es una RuntimeException porque los fallos de persistencia suelen ser no recuperables.
 */
public class PersistenciaException extends RuntimeException {

    private final Throwable causa;
    private final String operacion;

    /**
     * Constructor con mensaje y causa.
     *
     * @param mensaje descripción del error
     * @param causa excepción original que provocó este error (ej. Sql2oException)
     */
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.causa = causa;
        this.operacion = null;
    }

    /**
     * Constructor con mensaje, operación y causa.
     *
     * @param mensaje descripción del error
     * @param operacion operación de base de datos que falló (ej. "guardar", "buscarPorCodigo")
     * @param causa excepción original que provocó este error (ej. Sql2oException)
     */
    public PersistenciaException(String mensaje, String operacion, Throwable causa) {
        super(mensaje, causa);
        this.causa = causa;
        this.operacion = operacion;
    }

    /**
     * Obtiene la excepción original que provocó este error.
     *
     * @return causa original del error
     */
    public Throwable getCausa() {
        return causa;
    }

    /**
     * Obtiene la operación de base de datos que falló.
     *
     * @return operación que falló, o null si no se especificó
     */
    public String getOperacion() {
        return operacion;
    }
}
