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
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import concesionaria.model.ComparadoresVehiculo;

/**
 * Aplicación interactiva de consola para operar la concesionaria.
 */
public class ConsolaApp {

    private final ConcesionariaService servicio;
    private final ValidadorEntrada entrada;
    private final Terminal terminal;

    public ConsolaApp(ConcesionariaService servicio, ValidadorEntrada entrada) {
        if (servicio == null) throw new IllegalArgumentException("El servicio no puede ser null");
        if (entrada == null) throw new IllegalArgumentException("El validador no puede ser null");
        this.servicio = servicio;
        try {
            this.terminal = TerminalBuilder.builder().build();
            this.entrada = new ValidadorEntrada(new java.util.Scanner(System.in), terminal);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar el terminal", e);
        }
    }

    private String centrarTexto(String texto) {
        int ancho = obtenerAnchoTerminal();
        int padding = (ancho - texto.length()) / 2;
        if (padding < 0) padding = 0;
        return " ".repeat(padding) + texto;
    }

    private void centrarVerticalmente(int lineasContenido) {
        int altura = obtenerAlturaTerminal();
        int paddingVertical = (altura - lineasContenido) / 2;
        if (paddingVertical < 0) paddingVertical = 0;
        for (int i = 0; i < paddingVertical; i++) {
            System.out.println();
        }
    }

    private int obtenerAnchoTerminal() {
        try {
            int ancho = terminal.getWidth();
            if (ancho > 0 && ancho < 10000) {
                return ancho;
            }
        } catch (Exception e) {
        }
        return 80;
    }

    private int obtenerAlturaTerminal() {
        try {
            int altura = terminal.getHeight();
            if (altura > 0 && altura < 10000) {
                return altura;
            }
        } catch (Exception e) {
        }
        return 24;
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            limpiarPantalla();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            mostrarMenu();
            int opcion = entrada.leerEnteroWrapper("Opción", 0, 13);
            try {
                if (opcion != 0) {
                    limpiarPantalla();
                }
                switch (opcion) {
                    case 1: registrarAutomovil(); break;
                    case 2: registrarCamioneta(); break;
                    case 3: registrarMotocicleta(); break;
                    case 4: mostrarTitulo("LISTAR TODOS LOS VEHÍCULOS"); listar(servicio.listarTodos(), "vehículos registrados"); break;
                    case 5: mostrarTitulo("LISTAR UNIDADES 0 KM"); listar(servicio.listarPorEstado(EstadoUnidad.CERO_KM), "vehículos 0 km"); break;
                    case 6: mostrarTitulo("LISTAR UNIDADES USADAS"); listar(servicio.listarPorEstado(EstadoUnidad.USADO), "vehículos usados"); break;
                    case 7: filtrarPorTipo(); break;
                    case 8: buscarPorCodigo(); break;
                    case 9: eliminarPorCodigo(); break;
                    case 10: mostrarTitulo("REPORTE PARA DIRECTIVOS"); System.out.println(centrarTexto(servicio.generarReporteDirectivos())); break;
                    case 11: ordenarPorAnio(); break;
                    case 12: ordenarPorPrecio(); break;
                    case 13: ordenarPorMarcaModelo(); break;
                    case 0:
                        salir = true;
                        limpiarPantalla();
                        System.out.println(centrarTexto("Saliendo de la aplicación."));
                        System.exit(0);
                        break;
                    default:
                        System.out.println(centrarTexto("Opción fuera del rango disponible."));
                }
            } catch (DatoVehiculoInvalidoException e) {
                System.out.println(centrarTexto("Datos inválidos: " + e.getMessage()));
            } catch (VehiculoNoEncontradoException e) {
                System.out.println(centrarTexto("No encontrado: " + e.getMessage()));
            } catch (PersistenciaException e) {
                System.out.println(centrarTexto("Error de persistencia: " + e.getMessage()));
            } catch (IllegalArgumentException e) {
                System.out.println(centrarTexto("Error: " + e.getMessage()));
            }

            if (opcion != 0) {
                esperarVolverMenu();
            }
        }
    }

    public static void limpiarPantalla() {
        try {
            String os = System.getProperty("os.name", "").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    private void esperarVolverMenu() {
        System.out.println();
        entrada.esperarOpcionCero();
        limpiarPantalla();
    }

    private void mostrarMenu() {
        String cyan = "\u001B[36m";
        String reset = "\u001B[0m";

        centrarVerticalmente(17);

        System.out.println(cyan + centrarTexto("════════════════════════════════════════════════════════════════════════════════") + reset);
        System.out.println(cyan + centrarTexto("                              CONCESIONARIA MULTIMARCAS                                  ") + reset);
        System.out.println(cyan + centrarTexto("                                         TFI                                              ") + reset);
        System.out.println(cyan + centrarTexto("════════════════════════════════════════════════════════════════════════════════") + reset);
        System.out.println(centrarTexto("=== MENÚ PRINCIPAL ==="));
        System.out.println(centrarTexto("1. Registrar Automóvil"));
        System.out.println(centrarTexto("2. Registrar Camioneta"));
        System.out.println(centrarTexto("3. Registrar Motocicleta"));
        System.out.println(centrarTexto("4. Listar todos los vehículos"));
        System.out.println(centrarTexto("5. Listar unidades 0 km"));
        System.out.println(centrarTexto("6. Listar unidades usadas"));
        System.out.println(centrarTexto("7. Filtrar por tipo de vehículo"));
        System.out.println(centrarTexto("8. Buscar vehículo por código"));
        System.out.println(centrarTexto("9. Eliminar vehículo por código"));
        System.out.println(centrarTexto("10. Ver reporte para directivos"));
        System.out.println(centrarTexto("11. Listar ordenados por año"));
        System.out.println(centrarTexto("12. Listar ordenados por precio"));
        System.out.println(centrarTexto("13. Listar ordenados por marca y modelo"));
        System.out.println(centrarTexto("0. Salir"));
    }

    private void mostrarTitulo(String titulo) {
        String cyan = "\u001B[36m";
        String reset = "\u001B[0m";
        System.out.println();
        System.out.println(cyan + centrarTexto("════════════════════════════════════════════════════════════════════════════════") + reset);
        System.out.println(cyan + centrarTexto("  " + titulo + "  ") + reset);
        System.out.println(cyan + centrarTexto("════════════════════════════════════════════════════════════════════════════════") + reset);
    }

    private void registrarAutomovil() throws DatoVehiculoInvalidoException {
        centrarVerticalmente(30);
        mostrarTitulo("REGISTRAR AUTOMÓVIL");
        String marca = entrada.leerTextoObligatorio("Marca");
        String modelo = entrada.leerTextoObligatorio("Modelo");
        Integer anio = entrada.leerEnteroWrapper("Año", 1950, java.time.Year.now().getValue() + 1);
        String color = entrada.leerTextoObligatorio("Color");
        Integer kilometraje = entrada.leerEnteroWrapper("Kilometraje (0 = nuevo, mayor a 0 = usado)", 0, Integer.MAX_VALUE);
        EstadoUnidad estado = kilometraje == 0 ? EstadoUnidad.CERO_KM : EstadoUnidad.USADO;
        Double precio = entrada.leerDecimalWrapper("Precio de lista", 0.01);
        String carroceria = entrada.leerTextoObligatorio("Carrocería");
        Vehiculo registrado = servicio.registrarVehiculo(new Automovil(marca, modelo, anio, color, estado, kilometraje, precio, carroceria));
        System.out.println(centrarTexto("Automóvil registrado exitosamente. Código: " + registrado.getCodigoInventario()));
    }

    private void registrarCamioneta() throws DatoVehiculoInvalidoException {
        centrarVerticalmente(30);
        mostrarTitulo("REGISTRAR CAMIONETA");
        String marca = entrada.leerTextoObligatorio("Marca");
        String modelo = entrada.leerTextoObligatorio("Modelo");
        Integer anio = entrada.leerEnteroWrapper("Año", 1950, java.time.Year.now().getValue() + 1);
        String color = entrada.leerTextoObligatorio("Color");
        Integer kilometraje = entrada.leerEnteroWrapper("Kilometraje (0 = nuevo, mayor a 0 = usado)", 0, Integer.MAX_VALUE);
        EstadoUnidad estado = kilometraje == 0 ? EstadoUnidad.CERO_KM : EstadoUnidad.USADO;
        Double precio = entrada.leerDecimalWrapper("Precio de lista", 0.01);
        String carroceria = entrada.leerTextoObligatorio("Carrocería");
        Vehiculo registrado = servicio.registrarVehiculo(new Camioneta(marca, modelo, anio, color, estado, kilometraje, precio, carroceria));
        System.out.println(centrarTexto("Camioneta registrada exitosamente. Código: " + registrado.getCodigoInventario()));
    }

    private void registrarMotocicleta() throws DatoVehiculoInvalidoException {
        centrarVerticalmente(30);
        mostrarTitulo("REGISTRAR MOTOCICLETA");
        String marca = entrada.leerTextoObligatorio("Marca");
        String modelo = entrada.leerTextoObligatorio("Modelo");
        Integer anio = entrada.leerEnteroWrapper("Año", 1950, java.time.Year.now().getValue() + 1);
        String color = entrada.leerTextoObligatorio("Color");
        Integer kilometraje = entrada.leerEnteroWrapper("Kilometraje (0 = nuevo, mayor a 0 = usado)", 0, Integer.MAX_VALUE);
        EstadoUnidad estado = kilometraje == 0 ? EstadoUnidad.CERO_KM : EstadoUnidad.USADO;
        Double precio = entrada.leerDecimalWrapper("Precio de lista", 0.01);
        String tipoMotocicleta = entrada.leerTextoObligatorio("Tipo de motocicleta");
        Vehiculo registrado = servicio.registrarVehiculo(new Motocicleta(marca, modelo, anio, color, estado, kilometraje, precio, tipoMotocicleta));
        System.out.println(centrarTexto("Motocicleta registrada exitosamente. Código: " + registrado.getCodigoInventario()));
    }

    private void filtrarPorTipo() {
        centrarVerticalmente(30);
        mostrarTitulo("FILTRAR POR TIPO DE VEHÍCULO");
        TipoVehiculo tipo = entrada.leerEnum("Tipo de vehículo", TipoVehiculo.class);
        listar(servicio.listarPorTipo(tipo), "vehículos de tipo " + tipo.getDescripcion());
    }

    private void buscarPorCodigo() throws VehiculoNoEncontradoException {
        centrarVerticalmente(30);
        mostrarTitulo("BUSCAR VEHÍCULO POR CÓDIGO");
        String codigo = entrada.leerTextoObligatorio("Código de inventario");
        Vehiculo vehiculo = servicio.obtenerVehiculoPorCodigo(codigo);
        listar(Collections.singletonList(vehiculo), "vehículo solicitado");
    }

    private void ordenarPorAnio() {
        centrarVerticalmente(30);
        mostrarTitulo("LISTAR VEHÍCULOS ORDENADOS POR AÑO");
        List<Vehiculo> vehiculos = new ArrayList<>(servicio.listarTodos());
        vehiculos.sort(ComparadoresVehiculo.POR_ANIO_ASC);
        listar(vehiculos, "vehículos ordenados por año");
    }

    private void ordenarPorPrecio() {
        centrarVerticalmente(30);
        mostrarTitulo("LISTAR VEHÍCULOS ORDENADOS POR PRECIO");
        List<Vehiculo> vehiculos = new ArrayList<>(servicio.listarTodos());
        vehiculos.sort(ComparadoresVehiculo.POR_PRECIO_ASC);
        listar(vehiculos, "vehículos ordenados por precio");
    }

    private void ordenarPorMarcaModelo() {
        centrarVerticalmente(30);
        mostrarTitulo("LISTAR VEHÍCULOS ORDENADOS POR MARCA Y MODELO");
        List<Vehiculo> vehiculos = new ArrayList<>(servicio.listarTodos());
        vehiculos.sort(ComparadoresVehiculo.POR_MARCA_MODELO);
        listar(vehiculos, "vehículos ordenados por marca y modelo");
    }

    private void eliminarPorCodigo() throws VehiculoNoEncontradoException {
        centrarVerticalmente(30);
        mostrarTitulo("ELIMINAR VEHÍCULO POR CÓDIGO");
        String codigo = entrada.leerTextoObligatorio("Código de inventario");
        servicio.eliminarVehiculo(codigo);
        System.out.println(centrarTexto("Vehículo eliminado: " + codigo));
    }

    private void listar(List<Vehiculo> vehiculos, String descripcion) {
        //centrarVerticalmente(5);
        if (vehiculos == null || vehiculos.isEmpty()) {
            System.out.println(centrarTexto("No hay " + descripcion + "."));
            return;
        }

        System.out.println(centrarTexto("\n%-12s | %-12s | %-16s | %-20s | %-4s | %-16s | %-8s | %-10s | %-14s | %-22s | %-20s | %-8s".formatted(
                "Código", "Tipo", "Marca", "Modelo", "Año", "Color", "Estado", "Km", "Precio", "Detalle", "Categoría", "Entrega")));
        System.out.println(centrarTexto("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"));
        for (Vehiculo vehiculo : vehiculos) {
            String detalle = "";
            if (vehiculo instanceof concesionaria.model.Automovil) {
                detalle = ((concesionaria.model.Automovil) vehiculo).getCarroceria();
            } else if (vehiculo instanceof concesionaria.model.Camioneta) {
                detalle = ((concesionaria.model.Camioneta) vehiculo).getCarroceria();
            } else if (vehiculo instanceof concesionaria.model.Motocicleta) {
                detalle = ((concesionaria.model.Motocicleta) vehiculo).getTipoMotocicleta();
            }

            System.out.printf("%-12s | %-12s | %-16s | %-20s | %-4s | %-16s | %-8s | %-10s | %-14s | %-22s | %-20s | %-8s%n",
                    valorOGuion(vehiculo.getCodigoInventario()),
                    vehiculo.obtenerTipoVehiculo() != null ? vehiculo.obtenerTipoVehiculo().getDescripcion() : "-",
                    valorOGuion(vehiculo.getMarca()),
                    valorOGuion(vehiculo.getModelo()),
                    vehiculo.getAnio() != null ? vehiculo.getAnio() : "-",
                    valorOGuion(vehiculo.getColor()),
                    vehiculo.getEstado() != null ? vehiculo.getEstado().getDescripcion() : "-",
                    vehiculo.getKilometraje() != null ? vehiculo.getKilometraje() : "-",
                    vehiculo.getPrecioLista() != null ? "$" + vehiculo.getPrecioLista() : "-",
                    valorOGuion(detalle),
                    vehiculo.obtenerCategoriaComercial(),
                    vehiculo.esAptoEntregaInmediata() ? "Sí" : "No");
        }
    }

    private String valorOGuion(String valor) {
        return valor == null || valor.trim().isEmpty() ? "-" : valor;
    }
}
