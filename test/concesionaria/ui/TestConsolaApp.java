package concesionaria.ui;

import concesionaria.model.Automovil;
import concesionaria.model.Vehiculo;
import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;
import concesionaria.interfaces.VehiculoRepository;
import concesionaria.servicio.ConcesionariaService;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestConsolaApp {

    private static class RepositorioPrueba implements VehiculoRepository {
        private final List<Vehiculo> vehiculos = new ArrayList<>();
        private boolean listadoTodos;

        @Override public Vehiculo guardar(Vehiculo entidad) { vehiculos.add(entidad); return entidad; }
        @Override public Vehiculo buscarPorCodigo(String codigo) {
            for (Vehiculo vehiculo : vehiculos) {
                if (codigo.equals(vehiculo.getCodigoInventario())) return vehiculo;
            }
            return null;
        }
        @Override public List<Vehiculo> listarTodos() {
            listadoTodos = true;
            return new ArrayList<>(vehiculos);
        }
        @Override public boolean eliminarPorCodigo(String codigo) { return false; }
        @Override public List<Vehiculo> listarPorEstado(EstadoUnidad estado) { return new ArrayList<>(vehiculos); }
        @Override public List<Vehiculo> listarPorTipo(TipoVehiculo tipo) { return new ArrayList<>(vehiculos); }

        boolean fueListadoTodos() { return listadoTodos; }
    }

    public static void main(String[] args) {
        TerminalUtil.limpiarPantalla();

        RepositorioPrueba repositorio = new RepositorioPrueba();
        repositorio.guardar(new Automovil("Toyota", "Corolla", 2026, "Blanco",
                EstadoUnidad.CERO_KM, 0, 20000000.0, "Sedán"));

        ByteArrayInputStream input = new ByteArrayInputStream(
                "4\n0\n10\n0\n11\n0\n12\n0\n13\n0\n0\n".getBytes(StandardCharsets.UTF_8));
        Scanner scanner = new Scanner(input, StandardCharsets.UTF_8);

        ConsolaApp app = new ConsolaApp(new ConcesionariaService(repositorio), scanner);
        app.iniciar();

        if (!repositorio.fueListadoTodos()) {
            throw new AssertionError("El menú no llegó a listar los vehículos");
        }

        System.out.println("TestConsolaApp: OK");
    }
}
