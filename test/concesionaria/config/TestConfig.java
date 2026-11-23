package concesionaria.config;

public class TestConfig {
    public static void main(String[] args) {
        try {
            System.out.println("=== Test US-02: Configuración y Conexión DB ===");
            System.out.println("DB_URL: " + Config.URL);
            System.out.println("DB_USER: " + Config.USER);
            
            if (Config.URL == null || Config.URL.isBlank()) {
                throw new AssertionError("DB_URL no está configurado");
            }
            if (Config.USER == null || Config.USER.isBlank()) {
                throw new AssertionError("DB_USER no está configurado");
            }
            if (ConexionDB.sql2o == null) {
                throw new AssertionError("Instancia de Sql2o en ConexionDB es null");
            }

            System.out.println("Configuración de conexión verificada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error en TestConfig: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
