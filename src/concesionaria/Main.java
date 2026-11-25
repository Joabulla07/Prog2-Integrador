package concesionaria;

import concesionaria.repository.MssqlVehiculoRepository;
import concesionaria.servicio.ConcesionariaService;
import concesionaria.ui.ConsolaApp;

import java.util.Scanner;

/**
 * Punto de entrada principal de la aplicación.
 * Inicializa el servicio de concesionaria, el scanner de entrada y lanza la aplicación de consola.
 */
public class Main {
    /**
     * Método principal que inicia la aplicación.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        ConcesionariaService servicio = new ConcesionariaService(new MssqlVehiculoRepository());
        new ConsolaApp(servicio, new Scanner(System.in)).iniciar();
    }
}