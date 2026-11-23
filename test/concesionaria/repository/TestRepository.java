package concesionaria.repository;

import concesionaria.model.Automovil;
import concesionaria.model.Vehiculo;
import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;
import concesionaria.repository.MssqlVehiculoRepository;
import concesionaria.repository.VehiculoRepository;

public class TestRepository {
    public static void main(String[] args) {
        try {
            VehiculoRepository repo = new MssqlVehiculoRepository();
            
            System.out.println("=== Test 1: Crear y guardar vehículo ===");
            Automovil auto = new Automovil(
                "Toyota", "Corolla", 2026, "Blanco",
                EstadoUnidad.CERO_KM, 0, 25000000.0, "Sedán"
            );
            System.out.println("Vehículo creado: " + auto);
            System.out.println("Código antes de guardar: " + auto.getCodigoInventario());
            
            Vehiculo guardado = repo.guardar(auto);
            System.out.println("Vehículo guardado: " + guardado);
            System.out.println("Código después de guardar: " + guardado.getCodigoInventario());
            
            String codigo = guardado.getCodigoInventario();
            System.out.println("\n=== Test 2: Buscar por código ===");
            Vehiculo encontrado = repo.buscarPorCodigo(codigo);
            System.out.println("Vehículo encontrado: " + encontrado);

            if (codigo == null || encontrado == null) {
                throw new AssertionError("No se pudo completar el flujo de alta y búsqueda");
            }

            System.out.println("\n=== Test 3: Listar todos ===");
            System.out.println("Total vehículos: " + repo.listarTodos().size());
            
            System.out.println("\n=== Test 4: Listar por estado (CERO_KM) ===");
            System.out.println("Vehículos 0 km: " + repo.listarPorEstado(EstadoUnidad.CERO_KM).size());
            
            System.out.println("\n=== Test 5: Listar por tipo (AUTOMOVIL) ===");
            System.out.println("Automóviles: " + repo.listarPorTipo(TipoVehiculo.AUTOMOVIL).size());

            System.out.println("\n=== Test 6: Inyección SQL en parámetro ===");
            String valorMalicioso = "' OR 1=1 --";
            Vehiculo resultadoMalicioso = repo.buscarPorCodigo(valorMalicioso);
            if (resultadoMalicioso != null) {
                throw new AssertionError("La consulta parametrizada devolvió datos ante una entrada maliciosa");
            }
            System.out.println("Entrada maliciosa no produjo coincidencias");

            System.out.println("\n=== Test 7: Eliminar vehículo creado ===");
            if (!repo.eliminarPorCodigo(codigo)) {
                throw new AssertionError("No se pudo eliminar el vehículo creado por el test");
            }
            if (repo.buscarPorCodigo(codigo) != null) {
                throw new AssertionError("El vehículo eliminado sigue siendo encontrado");
            }
            System.out.println("Vehículo eliminado y búsqueda posterior devolvió null");
            
            System.out.println("\n=== TODOS LOS TESTS PASARON ===");
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
