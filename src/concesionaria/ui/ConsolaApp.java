package concesionaria.ui;

import concesionaria.exception.DatoVehiculoInvalidoException;
import concesionaria.exception.PersistenciaException;
import concesionaria.exception.VehiculoNoEncontradoException;
import concesionaria.model.Automovil;
import concesionaria.model.Camioneta;
import concesionaria.model.Motocicleta;
import concesionaria.model.Vehiculo;
import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;
import concesionaria.servicio.ConcesionariaService;

import java.util.List;

/**
 * Aplicación interactiva de consola para operar la concesionaria.
 */
public class ConsolaApp {

    private final ConcesionariaService servicio;
    private final ValidadorEntrada entrada;

    public ConsolaApp(ConcesionariaService servicio, ValidadorEntrada entrada) {
        if (servicio == null) throw new IllegalArgumentException("El servicio no puede ser null");
        if (entrada == null) throw new IllegalArgumentException("El validador no puede ser null");
        this.servicio = servicio;
        this.entrada = entrada;
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = entrada.leerEnteroWrapper("Opción", 0, 10);
            try {
                switch (opcion) {
                    case 1: registrarAutomovil(); break;
                    case 2: registrarCamioneta(); break;
                    case 3: registrarMotocicleta(); break;
                    case 4: listar(servicio.listarTodos(), "vehículos registrados"); break;
                    case 5: listar(servicio.listarPorEstado(EstadoUnidad.CERO_KM), "vehículos 0 km"); break;
                    case 6: listar(servicio.listarPorEstado(EstadoUnidad.USADO), "vehículos usados"); break;
                    case 7: filtrarPorTipo(); break;
                    case 8: buscarPorCodigo(); break;
                    case 9: eliminarPorCodigo(); break;
                    case 10: System.out.println(servicio.generarReporteDirectivos()); break;
                    case 0:
                        salir = true;
                        System.out.println("Saliendo de la aplicación.");
                        break;
                    default:
                        System.out.println("Opción fuera del rango disponible.");
                }
            } catch (DatoVehiculoInvalidoException e) {
                System.out.println("Datos inválidos: " + e.getMessage());
            } catch (VehiculoNoEncontradoException e) {
                System.out.println("No encontrado: " + e.getMessage());
            } catch (PersistenciaException e) {
                System.out.println("Error de persistencia: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void mostrarMenu() {
        System.out.println("\n=== CONCESIONARIA ===");
        System.out.println("1. Registrar Automóvil");
        System.out.println("2. Registrar Camioneta");
        System.out.println("3. Registrar Motocicleta");
        System.out.println("4. Listar todos los vehículos");
        System.out.println("5. Listar unidades 0 km");
        System.out.println("6. Listar unidades usadas");
        System.out.println("7. Filtrar por tipo de vehículo");
        System.out.println("8. Buscar vehículo por código");
        System.out.println("9. Eliminar vehículo por código");
        System.out.println("10. Ver reporte para directivos");
        System.out.println("0. Salir");
    }

    private void registrarAutomovil() throws DatoVehiculoInvalidoException {
        String marca = entrada.leerTextoObligatorio("Marca");
        String modelo = entrada.leerTextoObligatorio("Modelo");
        Integer anio = entrada.leerEnteroWrapper("Año", 1950, java.time.Year.now().getValue() + 1);
        String color = entrada.leerTextoObligatorio("Color");
        EstadoUnidad estado = entrada.leerEnum("Estado", EstadoUnidad.class);
        Integer kilometraje = leerKilometrajeSegunEstado(estado);
        Double precio = entrada.leerDecimalWrapper("Precio de lista", 0.01);
        String carroceria = entrada.leerTextoObligatorio("Carrocería");
        Vehiculo registrado = servicio.registrarVehiculo(new Automovil(marca, modelo, anio, color, estado, kilometraje, precio, carroceria));
        System.out.println("Automóvil registrado: " + registrado);
    }

    private void registrarCamioneta() throws DatoVehiculoInvalidoException {
        String marca = entrada.leerTextoObligatorio("Marca");
        String modelo = entrada.leerTextoObligatorio("Modelo");
        Integer anio = entrada.leerEnteroWrapper("Año", 1950, java.time.Year.now().getValue() + 1);
        String color = entrada.leerTextoObligatorio("Color");
        EstadoUnidad estado = entrada.leerEnum("Estado", EstadoUnidad.class);
        Integer kilometraje = leerKilometrajeSegunEstado(estado);
        Double precio = entrada.leerDecimalWrapper("Precio de lista", 0.01);
        String carroceria = entrada.leerTextoObligatorio("Carrocería");
        Vehiculo registrado = servicio.registrarVehiculo(new Camioneta(marca, modelo, anio, color, estado, kilometraje, precio, carroceria));
        System.out.println("Camioneta registrada: " + registrado);
    }

    private void registrarMotocicleta() throws DatoVehiculoInvalidoException {
        String marca = entrada.leerTextoObligatorio("Marca");
        String modelo = entrada.leerTextoObligatorio("Modelo");
        Integer anio = entrada.leerEnteroWrapper("Año", 1950, java.time.Year.now().getValue() + 1);
        String color = entrada.leerTextoObligatorio("Color");
        EstadoUnidad estado = entrada.leerEnum("Estado", EstadoUnidad.class);
        Integer kilometraje = leerKilometrajeSegunEstado(estado);
        Double precio = entrada.leerDecimalWrapper("Precio de lista", 0.01);
        String tipoMotocicleta = entrada.leerTextoObligatorio("Tipo de motocicleta");
        Vehiculo registrado = servicio.registrarVehiculo(new Motocicleta(marca, modelo, anio, color, estado, kilometraje, precio, tipoMotocicleta));
        System.out.println("Motocicleta registrada: " + registrado);
    }

    private Integer leerKilometrajeSegunEstado(EstadoUnidad estado) {
        if (estado == EstadoUnidad.CERO_KM) {
            return 0;
        }
        return entrada.leerEnteroWrapper("Kilometraje", 1, Integer.MAX_VALUE);
    }

    private void filtrarPorTipo() {
        TipoVehiculo tipo = entrada.leerEnum("Tipo de vehículo", TipoVehiculo.class);
        listar(servicio.listarPorTipo(tipo), "vehículos de tipo " + tipo.getDescripcion());
    }

    private void buscarPorCodigo() throws VehiculoNoEncontradoException {
        String codigo = entrada.leerTextoObligatorio("Código de inventario");
        System.out.println(servicio.obtenerVehiculoPorCodigo(codigo));
    }

    private void eliminarPorCodigo() throws VehiculoNoEncontradoException {
        String codigo = entrada.leerTextoObligatorio("Código de inventario");
        servicio.eliminarVehiculo(codigo);
        System.out.println("Vehículo eliminado: " + codigo);
    }

    private void listar(List<Vehiculo> vehiculos, String descripcion) {
        if (vehiculos == null || vehiculos.isEmpty()) {
            System.out.println("No hay " + descripcion + ".");
            return;
        }

        System.out.println("\n%-12s | %-12s | %-16s | %-20s | %-4s | %-16s | %-8s | %-10s | %-14s | %-22s".formatted(
                "Código", "Tipo", "Marca", "Modelo", "Año", "Color", "Estado", "Km", "Precio", "Detalle"));
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Vehiculo vehiculo : vehiculos) {
            String detalle = "";
            if (vehiculo instanceof concesionaria.model.Automovil) {
                detalle = ((concesionaria.model.Automovil) vehiculo).getCarroceria();
            } else if (vehiculo instanceof concesionaria.model.Camioneta) {
                detalle = ((concesionaria.model.Camioneta) vehiculo).getCarroceria();
            } else if (vehiculo instanceof concesionaria.model.Motocicleta) {
                detalle = ((concesionaria.model.Motocicleta) vehiculo).getTipoMotocicleta();
            }

            System.out.printf("%-12s | %-12s | %-16s | %-20s | %-4s | %-16s | %-8s | %-10s | %-14s | %-22s%n",
                    valorOGuion(vehiculo.getCodigoInventario()),
                    vehiculo.obtenerTipoVehiculo() != null ? vehiculo.obtenerTipoVehiculo().getDescripcion() : "-",
                    valorOGuion(vehiculo.getMarca()),
                    valorOGuion(vehiculo.getModelo()),
                    vehiculo.getAnio() != null ? vehiculo.getAnio() : "-",
                    valorOGuion(vehiculo.getColor()),
                    vehiculo.getEstado() != null ? vehiculo.getEstado().getDescripcion() : "-",
                    vehiculo.getKilometraje() != null ? vehiculo.getKilometraje() : "-",
                    vehiculo.getPrecioLista() != null ? "$" + vehiculo.getPrecioLista() : "-",
                    valorOGuion(detalle));
        }
    }

    private String valorOGuion(String valor) {
        return valor == null || valor.trim().isEmpty() ? "-" : valor;
    }
}
