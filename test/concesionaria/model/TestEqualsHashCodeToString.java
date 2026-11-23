package concesionaria.model;

import concesionaria.model.enums.EstadoUnidad;

import java.lang.reflect.Method;

public class TestEqualsHashCodeToString {
    public static void main(String[] args) {
        try {
            System.out.println("=== Test US-08: Sobreescritura de equals, hashCode y toString ===");

            Automovil a1 = new Automovil("Toyota", "Corolla", 2026, "Blanco", EstadoUnidad.CERO_KM, 0, 25000000.0, "Sedán");
            Automovil a2 = new Automovil("Toyota", "Corolla", 2026, "Blanco", EstadoUnidad.CERO_KM, 0, 25000000.0, "Sedán");

            // Dos objetos sin código de inventario (no persistidos) NO deben ser iguales
            if (a1.equals(a2)) {
                throw new AssertionError("Dos objetos no persistidos sin código de inventario no deben ser iguales");
            }

            // Asignar código de inventario a ambos
            Method assignMethod = Vehiculo.class.getDeclaredMethod("asignarCodigoInventario", String.class);
            assignMethod.setAccessible(true);
            assignMethod.invoke(a1, "AUTO-0001");
            assignMethod.invoke(a2, "auto-0001"); // Mismo código en minúscula para probar normalización

            // Deben ser iguales y tener el mismo hashCode
            if (!a1.equals(a2)) {
                throw new AssertionError("Dos objetos con el mismo código de inventario deben ser iguales");
            }
            if (a1.hashCode() != a2.hashCode()) {
                throw new AssertionError("Dos objetos iguales deben tener el mismo hashCode");
            }

            // Test toString
            String strAuto = a1.toString();
            if (!strAuto.contains("AUTO-0001") || !strAuto.contains("Toyota") || !strAuto.contains("Sedán")) {
                throw new AssertionError("toString debe contener detalles de la subclase y atributos: " + strAuto);
            }

            System.out.println("Tests de equals, hashCode y toString ejecutados exitosamente.");
        } catch (Exception e) {
            System.err.println("Error en TestEqualsHashCodeToString: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
