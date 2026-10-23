package concesionaria.config;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    public static final String URL = require("DB_URL");
    public static final String USER = require("DB_USER");
    public static final String PASS = require("DB_PASS");

    /**
     * Obtiene el valor de una variable de entorno requerida.
     *
     * @param key el nombre de la variable de entorno a obtener
     * @return el valor de la variable de entorno
     * @throws IllegalStateException si la variable de entorno no está definida o está vacía
     */
    private static String require(String key) throws IllegalStateException {
        String value = dotenv.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Falta la variable de entorno: " + key);
        }
        return value;
    }
}