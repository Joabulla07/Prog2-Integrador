package concesionaria.exception;

public class TestExcepciones {
    public static void main(String[] args) {
        try {
            System.out.println("=== Test US-05: Excepciones Propias ===");

            // 1. DatoVehiculoInvalidoException
            DatoVehiculoInvalidoException ex1 = new DatoVehiculoInvalidoException("Campo requerido vacío");
            if (!"Campo requerido vacío".equals(ex1.getMessage())) {
                throw new AssertionError("Mensaje incorrecto en DatoVehiculoInvalidoException");
            }

            // 2. VehiculoNoEncontradoException
            VehiculoNoEncontradoException ex2 = new VehiculoNoEncontradoException("AUTO-0001");
            if (!ex2.getMessage().contains("AUTO-0001")) {
                throw new AssertionError("Mensaje de VehiculoNoEncontradoException debe contener el código buscado");
            }

            // 3. PersistenciaException
            RuntimeException causaOriginal = new RuntimeException("Error SQL");
            PersistenciaException ex3 = new PersistenciaException("Fallo en base de datos", causaOriginal);
            if (!"Fallo en base de datos".equals(ex3.getMessage()) || ex3.getCause() != causaOriginal) {
                throw new AssertionError("Mensaje o causa incorrectos en PersistenciaException");
            }

            System.out.println("Tests de Excepciones Propias ejecutados exitosamente.");
        } catch (Exception e) {
            System.err.println("Error en TestExcepciones: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
