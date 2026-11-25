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
import java.util.Scanner;

import concesionaria.model.ComparadoresVehiculo;

/**
 * Aplicación interactiva de consola para operar la concesionaria.
 */
public class ConsolaApp {

    private final ConcesionariaService servicio;
    private final ValidadorEntrada entrada;
    private final TerminalUtil terminalUtil;

    /**
     * Constructor que inicializa la aplicación de consola.
     *
     * @param servicio el servicio de concesionaria a utilizar
     * @param scanner el Scanner desde el cual se leerán los datos del usuario
     * @throws IllegalArgumentException si servicio o scanner son null
     */
    public ConsolaApp(ConcesionariaService servicio, Scanner scanner) {
        if (servicio == null) throw new IllegalArgumentException("El servicio no puede ser null");
        if (scanner == null) throw new IllegalArgumentException("El Scanner no puede ser null");
        this.servicio = servicio;

        TerminalUtil tempTerminalUtil;

        try {
            Terminal terminal = TerminalBuilder.builder().build();
            tempTerminalUtil = new TerminalUtil(terminal);
        } catch (Exception e) {
            tempTerminalUtil = new TerminalUtil();
        }

        this.terminalUtil = tempTerminalUtil;
        this.entrada = new ValidadorEntrada(scanner, this.terminalUtil);
    }


    /**
     * Inicia el bucle principal de la aplicación de consola.
     * Muestra el menú y procesa las opciones del usuario hasta que se selecciona salir.
     */
    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            TerminalUtil.limpiarPantalla();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            mostrarMenu();
            int opcion = entrada.leerEnteroWrapper("Opción", 0, 13);
            try {
                if (opcion != 0) {
                    TerminalUtil.limpiarPantalla();
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
                    case 10: mostrarTitulo("REPORTE PARA DIRECTIVOS"); System.out.println(terminalUtil.centrarTexto(servicio.generarReporteDirectivos())); break;
                    case 11: ordenarPorAnio(); break;
                    case 12: ordenarPorPrecio(); break;
                    case 13: ordenarPorMarcaModelo(); break;
                    case 0:
                        salir = true;
                        TerminalUtil.limpiarPantalla();
                        System.out.println(terminalUtil.centrarTexto("Saliendo de la aplicación."));
                        break;
                    default:
                        System.out.println(terminalUtil.centrarTexto("Opción fuera del rango disponible."));
                }
            } catch (DatoVehiculoInvalidoException e) {
                System.out.println(terminalUtil.centrarTexto("Datos inválidos: " + e.getMessage()));
            } catch (VehiculoNoEncontradoException e) {
                System.out.println(terminalUtil.centrarTexto("No encontrado: " + e.getMessage()));
            } catch (PersistenciaException e) {
                System.out.println(terminalUtil.centrarTexto("Error de persistencia: " + e.getMessage()));
            } catch (IllegalArgumentException e) {
                System.out.println(terminalUtil.centrarTexto("Error: " + e.getMessage()));
            }

            if (opcion != 0) {
                esperarVolverMenu();
            }
        }
    }


    /**
     * Espera a que el usuario presione 0 para volver al menú principal.
     */
    private void esperarVolverMenu() {
        System.out.println();
        entrada.esperarOpcionCero();
        TerminalUtil.limpiarPantalla();
    }

    /**
     * Muestra el menú principal de la aplicación centrado en el terminal.
     */
    private void mostrarMenu() {
        String cyan = "\u001B[36m";
        String reset = "\u001B[0m";

        terminalUtil.centrarVerticalmente(17);

        System.out.println(cyan + terminalUtil.centrarTexto("════════════════════════════════════════════════════════════════════════════════") + reset);
        System.out.println(cyan + terminalUtil.centrarTexto("                              CONCESIONARIA MULTIMARCAS                                  ") + reset);
        System.out.println(cyan + terminalUtil.centrarTexto("                                         TFI                                              ") + reset);
        System.out.println(cyan + terminalUtil.centrarTexto("════════════════════════════════════════════════════════════════════════════════") + reset);
        System.out.println(terminalUtil.centrarTexto("=== MENÚ PRINCIPAL ==="));
        System.out.println(terminalUtil.centrarTexto("1. Registrar Automóvil"));
        System.out.println(terminalUtil.centrarTexto("2. Registrar Camioneta"));
        System.out.println(terminalUtil.centrarTexto("3. Registrar Motocicleta"));
        System.out.println(terminalUtil.centrarTexto("4. Listar todos los vehículos"));
        System.out.println(terminalUtil.centrarTexto("5. Listar unidades 0 km"));
        System.out.println(terminalUtil.centrarTexto("6. Listar unidades usadas"));
        System.out.println(terminalUtil.centrarTexto("7. Filtrar por tipo de vehículo"));
        System.out.println(terminalUtil.centrarTexto("8. Buscar vehículo por código"));
        System.out.println(terminalUtil.centrarTexto("9. Eliminar vehículo por código"));
        System.out.println(terminalUtil.centrarTexto("10. Ver reporte para directivos"));
        System.out.println(terminalUtil.centrarTexto("11. Listar ordenados por año"));
        System.out.println(terminalUtil.centrarTexto("12. Listar ordenados por precio"));
        System.out.println(terminalUtil.centrarTexto("13. Listar ordenados por marca y modelo"));
        System.out.println(terminalUtil.centrarTexto("0. Salir"));
    }

    /**
     * Muestra un título centrado con bordes decorativos.
     *
     * @param titulo el título a mostrar
     */
    private void mostrarTitulo(String titulo) {
        String cyan = "\u001B[36m";
        String reset = "\u001B[0m";
        System.out.println();
        System.out.println(cyan + terminalUtil.centrarTexto("════════════════════════════════════════════════════════════════════════════════") + reset);
        System.out.println(cyan + terminalUtil.centrarTexto("  " + titulo + "  ") + reset);
        System.out.println(cyan + terminalUtil.centrarTexto("════════════════════════════════════════════════════════════════════════════════") + reset);
    }

    /**
     * Procesa el registro de un nuevo automóvil.
     * Solicita los datos al usuario y registra el vehículo en el sistema.
     *
     * @throws DatoVehiculoInvalidoException si los datos ingresados son inválidos
     */
    private void registrarAutomovil() throws DatoVehiculoInvalidoException {
        terminalUtil.centrarVerticalmente(30);
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
        System.out.println(terminalUtil.centrarTexto("Automóvil registrado exitosamente. Código: " + registrado.getCodigoInventario()));
    }

    /**
     * Procesa el registro de una nueva camioneta.
     * Solicita los datos al usuario y registra el vehículo en el sistema.
     *
     * @throws DatoVehiculoInvalidoException si los datos ingresados son inválidos
     */
    private void registrarCamioneta() throws DatoVehiculoInvalidoException {
        terminalUtil.centrarVerticalmente(30);
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
        System.out.println(terminalUtil.centrarTexto("Camioneta registrada exitosamente. Código: " + registrado.getCodigoInventario()));
    }

    /**
     * Procesa el registro de una nueva motocicleta.
     * Solicita los datos al usuario y registra el vehículo en el sistema.
     *
     * @throws DatoVehiculoInvalidoException si los datos ingresados son inválidos
     */
    private void registrarMotocicleta() throws DatoVehiculoInvalidoException {
        terminalUtil.centrarVerticalmente(30);
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
        System.out.println(terminalUtil.centrarTexto("Motocicleta registrada exitosamente. Código: " + registrado.getCodigoInventario()));
    }

    /**
     * Filtra y lista vehículos por su tipo.
     */
    private void filtrarPorTipo() {
        terminalUtil.centrarVerticalmente(30);
        mostrarTitulo("FILTRAR POR TIPO DE VEHÍCULO");
        TipoVehiculo tipo = entrada.leerEnum("Tipo de vehículo", TipoVehiculo.class);
        listar(servicio.listarPorTipo(tipo), "vehículos de tipo " + tipo.getDescripcion());
    }

    /**
     * Busca un vehículo por su código de inventario.
     *
     * @throws VehiculoNoEncontradoException si no existe el vehículo
     */
    private void buscarPorCodigo() throws VehiculoNoEncontradoException {
        terminalUtil.centrarVerticalmente(30);
        mostrarTitulo("BUSCAR VEHÍCULO POR CÓDIGO");
        String codigo = entrada.leerTextoObligatorio("Código de inventario");
        Vehiculo vehiculo = servicio.obtenerVehiculoPorCodigo(codigo);
        listar(Collections.singletonList(vehiculo), "vehículo solicitado");
    }

    /**
     * Lista todos los vehículos ordenados por año ascendente.
     */
    private void ordenarPorAnio() {
        terminalUtil.centrarVerticalmente(30);
        mostrarTitulo("LISTAR VEHÍCULOS ORDENADOS POR AÑO");
        List<Vehiculo> vehiculos = new ArrayList<>(servicio.listarTodos());
        vehiculos.sort(ComparadoresVehiculo.POR_ANIO_ASC);
        listar(vehiculos, "vehículos ordenados por año");
    }

    /**
     * Lista todos los vehículos ordenados por precio ascendente.
     */
    private void ordenarPorPrecio() {
        terminalUtil.centrarVerticalmente(30);
        mostrarTitulo("LISTAR VEHÍCULOS ORDENADOS POR PRECIO");
        List<Vehiculo> vehiculos = new ArrayList<>(servicio.listarTodos());
        vehiculos.sort(ComparadoresVehiculo.POR_PRECIO_ASC);
        listar(vehiculos, "vehículos ordenados por precio");
    }

    /**
     * Lista todos los vehículos ordenados por marca y modelo.
     */
    private void ordenarPorMarcaModelo() {
        terminalUtil.centrarVerticalmente(30);
        mostrarTitulo("LISTAR VEHÍCULOS ORDENADOS POR MARCA Y MODELO");
        List<Vehiculo> vehiculos = new ArrayList<>(servicio.listarTodos());
        vehiculos.sort(ComparadoresVehiculo.POR_MARCA_MODELO);
        listar(vehiculos, "vehículos ordenados por marca y modelo");
    }

    /**
     * Elimina un vehículo por su código de inventario.
     *
     * @throws VehiculoNoEncontradoException si no existe el vehículo
     */
    private void eliminarPorCodigo() throws VehiculoNoEncontradoException {
        terminalUtil.centrarVerticalmente(30);
        mostrarTitulo("ELIMINAR VEHÍCULO POR CÓDIGO");
        String codigo = entrada.leerTextoObligatorio("Código de inventario");
        servicio.eliminarVehiculo(codigo);
        System.out.println(terminalUtil.centrarTexto("Vehículo eliminado: " + codigo));
    }

    /**
     * Muestra una lista de vehículos en formato tabular.
     *
     * @param vehiculos la lista de vehículos a mostrar
     * @param descripcion descripción del tipo de vehículos para mensajes
     */
    private void listar(List<Vehiculo> vehiculos, String descripcion) {
        //terminalUtil.centrarVerticalmente(5);
        if (vehiculos == null || vehiculos.isEmpty()) {
            System.out.println(terminalUtil.centrarTexto("No hay " + descripcion + "."));
            return;
        }

        System.out.println(terminalUtil.centrarTexto("\n%-12s | %-12s | %-16s | %-20s | %-4s | %-16s | %-8s | %-10s | %-14s | %-22s | %-20s | %-8s".formatted(
                "Código", "Tipo", "Marca", "Modelo", "Año", "Color", "Estado", "Km", "Precio", "Detalle", "Categoría", "Entrega")));
        System.out.println(terminalUtil.centrarTexto("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------"));
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

    /**
     * Retorna un guion si el valor es null o vacío, sino retorna el valor.
     *
     * @param valor el valor a evaluar
     * @return el valor o "-" si es null/vacío
     */
    private String valorOGuion(String valor) {
        return valor == null || valor.trim().isEmpty() ? "-" : valor;
    }
}
