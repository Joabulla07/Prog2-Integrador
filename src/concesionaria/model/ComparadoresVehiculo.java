package concesionaria.model;

import java.util.Comparator;
import java.util.Locale;

/**
 * Comparadores externos reutilizables para listas de vehículos.
 */
public final class ComparadoresVehiculo {

    private ComparadoresVehiculo() {
    }

    /** Compara vehículos por precio ascendente; los valores nulos quedan al final. */
    public static final Comparator<Vehiculo> POR_PRECIO_ASC = (uno, otro) -> {
        if (uno == otro) {
            return 0;
        }
        if (uno == null) {
            return 1;
        }
        if (otro == null) {
            return -1;
        }
        if (uno.getPrecioLista() == null && otro.getPrecioLista() == null) {
            return 0;
        }
        if (uno.getPrecioLista() == null) {
            return 1;
        }
        if (otro.getPrecioLista() == null) {
            return -1;
        }
        return uno.getPrecioLista().compareTo(otro.getPrecioLista());
    };

    /** Compara vehículos por año ascendente; los valores nulos quedan al final. */
    public static final Comparator<Vehiculo> POR_ANIO_ASC = (uno, otro) -> {
        if (uno == otro) {
            return 0;
        }
        if (uno == null) {
            return 1;
        }
        if (otro == null) {
            return -1;
        }
        if (uno.getAnio() == null && otro.getAnio() == null) {
            return 0;
        }
        if (uno.getAnio() == null) {
            return 1;
        }
        if (otro.getAnio() == null) {
            return -1;
        }
        return uno.getAnio().compareTo(otro.getAnio());
    };

    /** Compara vehículos por marca y luego por modelo, sin distinguir mayúsculas. */
    public static final Comparator<Vehiculo> POR_MARCA_MODELO =
            Comparator.comparing(
                    (Vehiculo vehiculo) -> vehiculo == null || vehiculo.getMarca() == null
                            ? null
                            : vehiculo.getMarca().trim().toUpperCase(Locale.ROOT),
                    Comparator.nullsLast(String::compareTo))
            .thenComparing(
                    vehiculo -> vehiculo == null || vehiculo.getModelo() == null
                            ? null
                            : vehiculo.getModelo().trim().toUpperCase(Locale.ROOT),
                    Comparator.nullsLast(String::compareTo));
}
