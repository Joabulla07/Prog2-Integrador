package concesionaria.exception;

/**
 * Excepción lanzada cuando la entrada estándar se cierra (EOF) mientras se esperan datos.
 * Permite cortar los bucles de lectura de consola sin dejar el proceso girando indefinidamente.
 */
public class EntradaFinalizadaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del contexto en el que se terminó la entrada
     */
    public EntradaFinalizadaException(String mensaje) {
        super(mensaje);
    }
}
