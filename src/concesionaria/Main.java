package concesionaria;

import concesionaria.repository.MssqlVehiculoRepository;
import concesionaria.servicio.ConcesionariaService;
import concesionaria.ui.ConsolaApp;
import concesionaria.ui.ValidadorEntrada;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConcesionariaService servicio = new ConcesionariaService(new MssqlVehiculoRepository());
        ValidadorEntrada validador = new ValidadorEntrada(new Scanner(System.in));
        new ConsolaApp(servicio, validador).iniciar();
    }
}