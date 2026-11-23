package concesionaria.ui;

import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TestValidadorEntrada {

    private static ValidadorEntrada validador(String entrada) {
        ByteArrayInputStream input = new ByteArrayInputStream(entrada.getBytes(StandardCharsets.UTF_8));
        return new ValidadorEntrada(new Scanner(input, StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        ValidadorEntrada texto = validador("   \n  Toyota  \n");
        if (!"Toyota".equals(texto.leerTextoObligatorio("Marca"))) {
            throw new AssertionError("leerTextoObligatorio no normalizó ni reintentó");
        }

        ValidadorEntrada entero = validador("abc\n999\n2026\n");
        if (!Integer.valueOf(2026).equals(entero.leerEnteroWrapper("Año", 1950, 2027))) {
            throw new AssertionError("leerEnteroWrapper no reintentó hasta un valor válido");
        }

        ValidadorEntrada decimal = validador("texto\n-1\n25000000.50\n");
        Double precio = decimal.leerDecimalWrapper("Precio", 1.0);
        if (precio == null || precio != 25000000.50) {
            throw new AssertionError("leerDecimalWrapper no reintentó hasta un valor válido");
        }

        ValidadorEntrada tipo = validador("moto\nAUTOMOVIL\n");
        if (tipo.leerEnum("Tipo", TipoVehiculo.class) != TipoVehiculo.AUTOMOVIL) {
            throw new AssertionError("leerEnum no aceptó nombre válido después de un error");
        }

        ValidadorEntrada estado = validador("0 km\n");
        if (estado.leerEnum("Estado", EstadoUnidad.class) != EstadoUnidad.CERO_KM) {
            throw new AssertionError("leerEnum no aceptó descripción válida");
        }

        System.out.println("TestValidadorEntrada: OK");
    }
}
