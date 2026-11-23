package concesionaria.model;

import concesionaria.interfaces.EvaluatableComercial;
import concesionaria.interfaces.Identificable;
import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;

import java.lang.reflect.Method;

public class TestInterfacesModel {
    public static void main(String[] args) {
        try {
            System.out.println("=== Test US-06 & US-07: Interfaces, Jerarquía, JavaBean y Polimorfismo ===");

            // 1. Instanciación JavaBeans
            Automovil auto = new Automovil("Toyota", "Corolla", 2026, "Blanco", EstadoUnidad.CERO_KM, 0, 25000000.0, "Sedán");
            Camioneta cam = new Camioneta("Ford", "Ranger", 2023, "Gris", EstadoUnidad.USADO, 450000, 38000000.0, "Pick-up");
            Motocicleta moto = new Motocicleta("Honda", "CB500X", 2024, "Rojo", EstadoUnidad.CERO_KM, 0, 12000000.0, "Trail");

            // Verify Interfaces
            if (!(auto instanceof Identificable) || !(auto instanceof EvaluatableComercial)) {
                throw new AssertionError("Automovil debe implementar Identificable y EvaluatableComercial");
            }

            // Verify Polimorfismo obtenerTipoVehiculo()
            if (auto.obtenerTipoVehiculo() != TipoVehiculo.AUTOMOVIL) {
                throw new AssertionError("TipoVehiculo incorrecto en Automovil");
            }
            if (cam.obtenerTipoVehiculo() != TipoVehiculo.CAMIONETA) {
                throw new AssertionError("TipoVehiculo incorrecto en Camioneta");
            }
            if (moto.obtenerTipoVehiculo() != TipoVehiculo.MOTOCICLETA) {
                throw new AssertionError("TipoVehiculo incorrecto en Motocicleta");
            }

            // Verify Polimorfismo obtenerFichaDirectiva() & obtenerTipoEstructura()
            if (!auto.obtenerTipoEstructura().equals("Carrocería: Sedán")) {
                throw new AssertionError("obtenerTipoEstructura() incorrecto para Automovil");
            }
            if (!moto.obtenerFichaDirectiva().contains("MOTOCICLETA")) {
                throw new AssertionError("obtenerFichaDirectiva() incorrecto para Motocicleta");
            }

            // Verify EvaluatableComercial
            if (!auto.esAptoEntregaInmediata()) {
                throw new AssertionError("Vehículo CERO_KM debe ser apto para entrega inmediata");
            }
            if (cam.esAptoEntregaInmediata()) {
                throw new AssertionError("Vehículo USADO no debe ser apto para entrega inmediata inmediata directa");
            }

            // Verify JavaBean getter/setter para carrocería y tipoMotocicleta
            auto.setCarroceria("Hatchback");
            if (!"Hatchback".equals(auto.getCarroceria())) {
                throw new AssertionError("Setter/Getter carroceria falló");
            }

            moto.setTipoMotocicleta("Custom");
            if (!"Custom".equals(moto.getTipoMotocicleta())) {
                throw new AssertionError("Setter/Getter tipoMotocicleta falló");
            }

            // Verify asignación única de código de inventario (reflection o asignacion directa via paquete/repositorio)
            Method assignMethod = Vehiculo.class.getDeclaredMethod("asignarCodigoInventario", String.class);
            assignMethod.setAccessible(true);
            assignMethod.invoke(auto, "AUTO-0001");

            if (!"AUTO-0001".equals(auto.getCodigoInventario())) {
                throw new AssertionError("No se asignó correctamente el código de inventario");
            }

            boolean exceptionLanzada = false;
            try {
                assignMethod.invoke(auto, "AUTO-0002");
            } catch (Exception e) {
                exceptionLanzada = true;
            }
            if (!exceptionLanzada) {
                throw new AssertionError("No se puede reasignar un código de inventario una vez establecido");
            }

            System.out.println("Tests US-06 & US-07 pasaron exitosamente.");
        } catch (Exception e) {
            System.err.println("Error en TestInterfacesModel: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
