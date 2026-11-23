package concesionaria.model;

import concesionaria.model.enums.EstadoUnidad;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestOrdenamientoVehiculos {
    public static void main(String[] args) {
        try {
            System.out.println("=== Test US-09: Ordenamiento Comparable y Comparator ===");

            Automovil a1 = new Automovil("Toyota", "Corolla", 2026, "Blanco", EstadoUnidad.CERO_KM, 0, 25000000.0, "Sedán");
            Automovil a2 = new Automovil("Ford", "Focus", 2020, "Negro", EstadoUnidad.USADO, 50000, 18000000.0, "Hatchback");
            Automovil a3 = new Automovil("Chevrolet", "Cruze", 2024, "Gris", EstadoUnidad.CERO_KM, 0, 22000000.0, "Sedán");

            // 1. Asignar código para permitir comparaciones por orden natural
            Method assignMethod = Vehiculo.class.getDeclaredMethod("asignarCodigoInventario", String.class);
            assignMethod.setAccessible(true);
            assignMethod.invoke(a1, "AUTO-0003");
            assignMethod.invoke(a2, "AUTO-0001");
            assignMethod.invoke(a3, "AUTO-0002");

            List<Vehiculo> lista = new ArrayList<>();
            lista.add(a1);
            lista.add(a2);
            lista.add(a3);

            // Orden Natural (por codigoInventario)
            Collections.sort(lista);
            if (!"AUTO-0001".equals(lista.get(0).getCodigoInventario()) ||
                !"AUTO-0002".equals(lista.get(1).getCodigoInventario()) ||
                !"AUTO-0003".equals(lista.get(2).getCodigoInventario())) {
                throw new AssertionError("Fallo en el ordenamiento natural (por código)");
            }

            // Comparator por Año
            lista.sort(ComparadoresVehiculo.POR_ANIO_ASC);
            if (lista.get(0).getAnio() != 2020 || lista.get(2).getAnio() != 2026) {
                throw new AssertionError("Fallo en el ordenamiento por Año");
            }

            // Comparator por Precio
            lista.sort(ComparadoresVehiculo.POR_PRECIO_ASC);
            if (lista.get(0).getPrecioLista() != 18000000.0 || lista.get(2).getPrecioLista() != 25000000.0) {
                throw new AssertionError("Fallo en el ordenamiento por Precio");
            }

            // Comparator por Marca y Modelo
            lista.sort(ComparadoresVehiculo.POR_MARCA_MODELO);
            if (!"Chevrolet".equals(lista.get(0).getMarca()) || !"Toyota".equals(lista.get(2).getMarca())) {
                throw new AssertionError("Fallo en el ordenamiento por Marca/Modelo");
            }

            // Excepción con código nulo
            Automovil sinCodigo = new Automovil("Fiat", "Cronos", 2025, "Rojo", EstadoUnidad.CERO_KM, 0, 15000000.0, "Sedán");
            boolean exceptionLanzada = false;
            try {
                a1.compareTo(sinCodigo);
            } catch (IllegalStateException e) {
                exceptionLanzada = true;
            }
            if (!exceptionLanzada) {
                throw new AssertionError("compareTo debió lanzar IllegalStateException al comparar vehículos sin código");
            }

            System.out.println("Tests de ordenamiento ejecuatdos exitosamente.");
        } catch (Exception e) {
            System.err.println("Error en TestOrdenamientoVehiculos: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
