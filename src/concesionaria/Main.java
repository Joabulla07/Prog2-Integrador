package concesionaria;

import concesionaria.repository.MssqlVehiculoRepository;
import concesionaria.servicio.ConcesionariaService;
import concesionaria.ui.ConsolaApp;
import concesionaria.ui.ValidadorEntrada;

import java.util.Scanner;

/**
 * Punto de entrada principal de la aplicación.
 * Inicializa el servicio de concesionaria, el validador de entrada y lanza la aplicación de consola.
 */
public class Main {
    /**
     * Método principal que inicia la aplicación.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        ConcesionariaService servicio = new ConcesionariaService(new MssqlVehiculoRepository());
        ValidadorEntrada validador = new ValidadorEntrada(new Scanner(System.in));
        new ConsolaApp(servicio, validador).iniciar();
    }
}