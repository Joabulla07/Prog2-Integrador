package concesionaria.servicio;

import concesionaria.exception.DatoVehiculoInvalidoException;
import concesionaria.exception.VehiculoNoEncontradoException;
import concesionaria.model.Automovil;
import concesionaria.model.Camioneta;
import concesionaria.model.Motocicleta;
import concesionaria.model.Vehiculo;
import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;
import concesionaria.interfaces.VehiculoRepository;

import java.util.ArrayList;
import java.util.List;

public class TestConcesionariaService {

    private static class RepositorioPrueba implements VehiculoRepository {
        private final List<Vehiculo> vehiculos = new ArrayList<>();
        private int guardarLlamadas;

        @Override
        public Vehiculo guardar(Vehiculo entidad) {
            guardarLlamadas++;
            entidad.asignarCodigoInventario("AUTO-TEST");
            vehiculos.add(entidad);
            return entidad;
        }

        @Override
        public Vehiculo buscarPorCodigo(String codigo) {
            for (Vehiculo vehiculo : vehiculos) {
                if (codigo.equals(vehiculo.getCodigoInventario())) return vehiculo;
            }
            return null;
        }

        @Override
        public java.util.List<Vehiculo> listarTodos() {
            return new ArrayList<>(vehiculos);
        }

        @Override
        public boolean eliminarPorCodigo(String codigo) {
            Vehiculo encontrado = buscarPorCodigo(codigo);
            return encontrado != null && vehiculos.remove(encontrado);
        }

        @Override
        public java.util.List<Vehiculo> listarPorEstado(EstadoUnidad estado) {
            return new ArrayList<>();
        }

        @Override
        public java.util.List<Vehiculo> listarPorTipo(TipoVehiculo tipo) {
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) throws Exception {
        RepositorioPrueba repositorio = new RepositorioPrueba();
        ConcesionariaService servicio = new ConcesionariaService(repositorio);

        Automovil valido = new Automovil("Toyota", "Corolla", 2026, "Blanco",
                EstadoUnidad.CERO_KM, 0, 20000000.0, "Sedán");
        Vehiculo registrado = servicio.registrarVehiculo(valido);
        if (!"AUTO-TEST".equals(registrado.getCodigoInventario())) throw new AssertionError("No se asignó el código persistido");
        if (repositorio.guardarLlamadas != 1) throw new AssertionError("Guardar debía llamarse una vez");

        Automovil normalizado = new Automovil("tOyOtA", "cOROLLA", 2026, "bLANCO PERLADO",
                EstadoUnidad.CERO_KM, 0, 20000000.0, "sEDÁN");
        Vehiculo guardadoNormalizado = servicio.registrarVehiculo(normalizado);
        if (!"Toyota".equals(guardadoNormalizado.getMarca())
                || !"Corolla".equals(guardadoNormalizado.getModelo())
                || !"Blanco Perlado".equals(guardadoNormalizado.getColor())
                || !"Sedán".equals(((Automovil) guardadoNormalizado).getCarroceria())) {
            throw new AssertionError("Los textos no se normalizaron a formato legible");
        }

        String reporte = servicio.generarReporteDirectivos();
        Camioneta camioneta = new Camioneta("fORD", "rANGER LIMITED", 2026, "aZUL BELICE",
                EstadoUnidad.CERO_KM, 0, 47000000.0, "pICK-UP DOBLE CABINA");
        Vehiculo camionetaNormalizada = servicio.registrarVehiculo(camioneta);
        if (!"Ford".equals(camionetaNormalizada.getMarca())
                || !"Ranger Limited".equals(camionetaNormalizada.getModelo())
                || !"Azul Belice".equals(camionetaNormalizada.getColor())
                || !"Pick-up Doble Cabina".equals(((Camioneta) camionetaNormalizada).getCarroceria())) {
            throw new AssertionError("La camioneta no se normalizó correctamente");
        }

        Motocicleta motocicleta = new Motocicleta("YAMAHA", "mT-03", 2026, "aZUL RACING",
                EstadoUnidad.CERO_KM, 0, 9450000.0, "nAKED");
        Vehiculo motocicletaNormalizada = servicio.registrarVehiculo(motocicleta);
        if (!"Yamaha".equals(motocicletaNormalizada.getMarca())
                || !"MT-03".equals(motocicletaNormalizada.getModelo())
                || !"Azul Racing".equals(motocicletaNormalizada.getColor())
                || !"Naked".equals(((Motocicleta) motocicletaNormalizada).getTipoMotocicleta())) {
            throw new AssertionError("La motocicleta no se normalizó correctamente");
        }

        if (!reporte.contains("AUTOMOVIL        | 2") || !reporte.contains("CERO_KM          | 2") ||
                !reporte.contains("AUTO-TEST") || !reporte.contains("Código") || !reporte.contains("Detalle")) {
            throw new AssertionError("Reporte directivo incompleto");
        }

        RepositorioPrueba repositorioVacio = new RepositorioPrueba();
        String reporteVacio = new ConcesionariaService(repositorioVacio).generarReporteDirectivos();
        if (!reporteVacio.contains("AUTOMOVIL        | 0") || !reporteVacio.contains("USADO            | 0") ||
                !reporteVacio.contains("No hay vehículos registrados.")) {
            throw new AssertionError("Reporte vacío incorrecto");
        }

        RepositorioPrueba rechazado = new RepositorioPrueba();
        ConcesionariaService servicioRechazado = new ConcesionariaService(rechazado);
        Automovil vacio = new Automovil("", "Corolla", 2026, "Blanco",
                EstadoUnidad.CERO_KM, 0, 20000000.0, "Sedán");
        try {
            servicioRechazado.registrarVehiculo(vacio);
            throw new AssertionError("Debía rechazar marca vacía");
        } catch (DatoVehiculoInvalidoException e) {
            if (!"marca".equals(e.getCampoInvalido())) throw new AssertionError("Campo inválido incorrecto");
        }
        if (rechazado.guardarLlamadas != 0) throw new AssertionError("No debía llamar al repositorio");

        Automovil conCodigo = new Automovil("Toyota", "Corolla", 2026, "Blanco",
                EstadoUnidad.CERO_KM, 0, 20000000.0, "Sedán");
        conCodigo.asignarCodigoInventario("AUTO-001");
        try {
            servicioRechazado.registrarVehiculo(conCodigo);
            throw new AssertionError("Debía rechazar código previo");
        } catch (DatoVehiculoInvalidoException e) {
            if (!"codigoInventario".equals(e.getCampoInvalido())) throw new AssertionError("Campo inválido incorrecto");
        }
        if (rechazado.guardarLlamadas != 0) throw new AssertionError("No debía guardar vehículo con código previo");

        assertRechazo(servicioRechazado, rechazado,
                new Automovil("Toyota", "Corolla", 1949, "Blanco", EstadoUnidad.CERO_KM, 0, 20000000.0, "Sedán"), "anio");
        assertRechazo(servicioRechazado, rechazado,
                new Automovil("Toyota", "Corolla", 2026, "Blanco", EstadoUnidad.CERO_KM, 0, 0.0, "Sedán"), "precioLista");
        assertRechazo(servicioRechazado, rechazado,
                new Automovil("Toyota", "Corolla", 2026, "Blanco", EstadoUnidad.CERO_KM, 10, 20000000.0, "Sedán"), "kilometraje");
        assertRechazo(servicioRechazado, rechazado,
                new Automovil("Toyota", "Corolla", 2026, "Blanco", EstadoUnidad.USADO, 0, 20000000.0, "Sedán"), "kilometraje");
        assertRechazo(servicioRechazado, rechazado,
                new Automovil("Toyota", "Corolla", 2026, "Blanco", EstadoUnidad.CERO_KM, 0, 20000000.0, ""), "carroceria");

        Vehiculo encontrado = servicio.obtenerVehiculoPorCodigo("AUTO-TEST");
        if (encontrado == null) throw new AssertionError("Debía encontrar el vehículo registrado");
        try {
            servicio.obtenerVehiculoPorCodigo("NO-EXISTE");
            throw new AssertionError("Debía rechazar código inexistente");
        } catch (VehiculoNoEncontradoException e) {
            if (!"NO-EXISTE".equals(e.getCodigoBuscado())) throw new AssertionError("Código buscado incorrecto");
        }
        if (!servicio.eliminarVehiculo("AUTO-TEST")) throw new AssertionError("Debía confirmar eliminación existente");
        try {
            servicio.eliminarVehiculo("NO-EXISTE");
            throw new AssertionError("Debía rechazar eliminación inexistente");
        } catch (VehiculoNoEncontradoException e) {
            if (!"NO-EXISTE".equals(e.getCodigoBuscado())) throw new AssertionError("Código buscado incorrecto");
        }

        System.out.println("TestConcesionariaService: OK");
    }

    private static void assertRechazo(ConcesionariaService servicio, RepositorioPrueba repositorio,
                                      Vehiculo vehiculo, String campoEsperado) throws Exception {
        int llamadasAntes = repositorio.guardarLlamadas;
        try {
            servicio.registrarVehiculo(vehiculo);
            throw new AssertionError("Debía rechazar el campo " + campoEsperado);
        } catch (DatoVehiculoInvalidoException e) {
            if (!campoEsperado.equals(e.getCampoInvalido())) {
                throw new AssertionError("Campo inválido incorrecto: " + e.getCampoInvalido());
            }
        }
        if (repositorio.guardarLlamadas != llamadasAntes) throw new AssertionError("No debía llamar al repositorio");
    }
}
