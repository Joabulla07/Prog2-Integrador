package concesionaria.model;

import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;

public class TestEnums {
    public static void main(String[] args) {
        try {
            System.out.println("=== Test US-04: Tipos Enumerativos del Dominio ===");

            // Test TipoVehiculo
            if (TipoVehiculo.AUTOMOVIL.getDescripcion() == null || !TipoVehiculo.AUTOMOVIL.getDescripcion().contains("Automóvil")) {
                throw new AssertionError("Descripción incorrecta para AUTOMOVIL");
            }
            if (TipoVehiculo.fromString("automovil") != TipoVehiculo.AUTOMOVIL) {
                throw new AssertionError("fromString falló para 'automovil'");
            }
            if (TipoVehiculo.fromString("CAMIONETA") != TipoVehiculo.CAMIONETA) {
                throw new AssertionError("fromString falló para 'CAMIONETA'");
            }

            // Test EstadoUnidad
            if (EstadoUnidad.CERO_KM.getDescripcion() == null || !EstadoUnidad.CERO_KM.getDescripcion().contains("0 km")) {
                throw new AssertionError("Descripción incorrecta para CERO_KM");
            }
            if (EstadoUnidad.fromString("0 km") != EstadoUnidad.CERO_KM) {
                throw new AssertionError("fromString falló para '0 km'");
            }
            if (EstadoUnidad.fromString("usado") != EstadoUnidad.USADO) {
                throw new AssertionError("fromString falló para 'usado'");
            }

            // Test excepcion entrada invalida
            boolean lanzoExcepcion = false;
            try {
                TipoVehiculo.fromString("AVION");
            } catch (IllegalArgumentException e) {
                lanzoExcepcion = true;
            }
            if (!lanzoExcepcion) {
                throw new AssertionError("TipoVehiculo.fromString debió lanzar IllegalArgumentException para 'AVION'");
            }

            System.out.println("Tests de enums ejecutados exitosamente.");
        } catch (Exception e) {
            System.err.println("Error en TestEnums: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
