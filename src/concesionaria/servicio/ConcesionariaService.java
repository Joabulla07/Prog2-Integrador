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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

/**
 * Coordina las operaciones de negocio de la concesionaria.
 */
public class ConcesionariaService {

    private static final int MAX_MARCA = 60;
    private static final int MAX_MODELO = 60;
    private static final int MAX_COLOR = 40;
    private static final int MAX_DETALLE = 60;
    private static final BigDecimal PRECIO_MAXIMO = new BigDecimal("999999999999.99");

    private static final Set<String> SIGLAS = Set.of(
            "SUV", "GTI", "GR-S", "MT-03", "HRC", "BMW", "GTR", "4X4", "CBR", "AMG");

    private static final List<Columna> COLUMNAS_TABLA = List.of(
            new Columna("Código", 11, v -> valorOGuion(v.getCodigoInventario()), 0),
            new Columna("Tipo", 11, v -> v.obtenerTipoVehiculo() != null
                    ? v.obtenerTipoVehiculo().getDescripcion() : "-", 3),
            new Columna("Marca", 12, v -> valorOGuion(v.getMarca()), 0),
            new Columna("Modelo", 14, v -> valorOGuion(v.getModelo()), 0),
            new Columna("Año", 4, v -> v.getAnio() != null ? v.getAnio().toString() : "-", 0),
            new Columna("Color", 10, v -> valorOGuion(v.getColor()), 1),
            new Columna("Estado", 7, v -> v.getEstado() != null ? v.getEstado().getDescripcion() : "-", 4),
            new Columna("Km", 8, v -> v.getKilometraje() != null ? v.getKilometraje().toString() : "-", 5),
            new Columna("Precio", 14, Vehiculo::getPrecioFormateado, 0),
            new Columna("Detalle", 16, v -> valorOGuion(obtenerDetalle(v)), 7),
            new Columna("Categoría", 18, Vehiculo::obtenerCategoriaComercial, 2),
            new Columna("Entrega", 7, v -> v.esAptoEntregaInmediata() ? "Sí" : "No", 6)
    );

    private final VehiculoRepository repositorio;

    /**
     * Crea una nueva instancia del servicio con el repositorio especificado.
     *
     * @param repositorio el repositorio de vehículos a utilizar
     * @throws IllegalArgumentException si el repositorio es null
     */
    public ConcesionariaService(VehiculoRepository repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser null");
        }
        this.repositorio = repositorio;
    }

    /**
     * Registra un nuevo vehículo en el sistema.
     * Normaliza los textos, valida los datos y persiste el vehículo.
     *
     * @param vehiculo el vehículo a registrar
     * @return el vehículo registrado con código de inventario asignado
     * @throws DatoVehiculoInvalidoException si los datos del vehículo son inválidos
     */
    public Vehiculo registrarVehiculo(Vehiculo vehiculo) throws DatoVehiculoInvalidoException {
        normalizarTextosVehiculo(vehiculo);
        validarVehiculo(vehiculo);
        return repositorio.guardar(vehiculo);
    }

    /**
     * Genera un reporte directivo con estadísticas y listado de todos los vehículos.
     * Incluye conteos por tipo y estado, y un detalle completo de cada vehículo.
     *
     * @return el reporte formateado como string
     */
    public String generarReporteDirectivos() {
        List<Vehiculo> vehiculos = repositorio.listarTodos();
        int automoviles = 0;
        int camionetas = 0;
        int motocicletas = 0;
        int ceroKm = 0;
        int usados = 0;

        for (Vehiculo vehiculo : vehiculos) {
            TipoVehiculo tipo = vehiculo.obtenerTipoVehiculo();
            if (tipo == TipoVehiculo.AUTOMOVIL) automoviles++;
            if (tipo == TipoVehiculo.CAMIONETA) camionetas++;
            if (tipo == TipoVehiculo.MOTOCICLETA) motocicletas++;

            EstadoUnidad estado = vehiculo.getEstado();
            if (estado == EstadoUnidad.CERO_KM) ceroKm++;
            if (estado == EstadoUnidad.USADO) usados++;
        }

        StringBuilder reporte = new StringBuilder();
        reporte.append("REPORTE DIRECTIVO\n\n");
        reporte.append("Tipo             | Cantidad\n");
        reporte.append("-----------------|---------\n");
        reporte.append("AUTOMOVIL        | ").append(automoviles).append("\n");
        reporte.append("CAMIONETA        | ").append(camionetas).append("\n");
        reporte.append("MOTOCICLETA      | ").append(motocicletas).append("\n\n");
        reporte.append("Estado           | Cantidad\n");
        reporte.append("-----------------|---------\n");
        reporte.append("CERO_KM          | ").append(ceroKm).append("\n");
        reporte.append("USADO            | ").append(usados).append("\n\n");

        reporte.append("Vehículos\n");
        reporte.append(formatearTablaVehiculos(vehiculos, Integer.MAX_VALUE));
        reporte.append("\n");
        return reporte.toString();
    }

    /**
     * Formatea una lista de vehículos como una tabla de ancho fijo.
     * Todas las líneas tienen exactamente el mismo ancho, los valores largos se
     * recortan y, si el ancho máximo no alcanza, se quitan columnas opcionales
     * (primero Color, luego Categoría, Tipo, Estado, Km, Entrega y Detalle).
     *
     * @param vehiculos lista de vehículos a mostrar
     * @param anchoMaximo ancho máximo disponible en caracteres
     * @return tabla formateada sin salto de línea final
     * @throws IllegalArgumentException si la lista es null
     */
    public String formatearTablaVehiculos(List<Vehiculo> vehiculos, int anchoMaximo) {
        if (vehiculos == null) {
            throw new IllegalArgumentException("La lista de vehículos no puede ser null");
        }

        List<Columna> columnas = new ArrayList<>(COLUMNAS_TABLA);
        while (anchoTotal(columnas) > anchoMaximo) {
            Columna aQuitar = null;
            for (Columna columna : columnas) {
                if (columna.prioridadRemocion > 0
                        && (aQuitar == null || columna.prioridadRemocion < aQuitar.prioridadRemocion)) {
                    aQuitar = columna;
                }
            }
            if (aQuitar == null) {
                break;
            }
            columnas.remove(aQuitar);
        }

        StringBuilder tabla = new StringBuilder();
        tabla.append(formatearLinea(columnas, Columna::getEncabezado)).append('\n');
        tabla.append(formatearSeparador(columnas)).append('\n');

        if (vehiculos.isEmpty()) {
            tabla.append("No hay vehículos registrados.");
            return tabla.toString();
        }

        for (int i = 0; i < vehiculos.size(); i++) {
            if (i > 0) {
                tabla.append('\n');
            }
            Vehiculo vehiculo = vehiculos.get(i);
            tabla.append(formatearLinea(columnas, columna -> columna.valor.apply(vehiculo)));
        }
        return tabla.toString();
    }

    /**
     * Lista todos los vehículos registrados en el sistema.
     *
     * @return lista de todos los vehículos
     */
    public List<Vehiculo> listarTodos() {
        return repositorio.listarTodos();
    }

    /**
     * Lista todos los vehículos que tienen un estado específico.
     *
     * @param estado el estado de unidad a filtrar
     * @return lista de vehículos con el estado especificado
     * @throws IllegalArgumentException si el estado es null
     */
    public List<Vehiculo> listarPorEstado(EstadoUnidad estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser null");
        }
        return repositorio.listarPorEstado(estado);
    }

    /**
     * Lista todos los vehículos de un tipo específico.
     *
     * @param tipo el tipo de vehículo a filtrar
     * @return lista de vehículos del tipo especificado
     * @throws IllegalArgumentException si el tipo es null
     */
    public List<Vehiculo> listarPorTipo(TipoVehiculo tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo no puede ser null");
        }
        return repositorio.listarPorTipo(tipo);
    }

    /**
     * Busca un vehículo por su código de inventario.
     *
     * @param codigo el código de inventario del vehículo
     * @return el vehículo encontrado
     * @throws VehiculoNoEncontradoException si no existe un vehículo con ese código
     */
    public Vehiculo obtenerVehiculoPorCodigo(String codigo) throws VehiculoNoEncontradoException {
        Vehiculo vehiculo = repositorio.buscarPorCodigo(codigo);
        if (vehiculo == null) {
            throw new VehiculoNoEncontradoException("No existe el vehículo con el código indicado", codigo);
        }
        return vehiculo;
    }

    /**
     * Elimina un vehículo del sistema por su código de inventario.
     *
     * @param codigo el código de inventario del vehículo a eliminar
     * @return true si se eliminó exitosamente
     * @throws VehiculoNoEncontradoException si no existe un vehículo con ese código
     */
    public boolean eliminarVehiculo(String codigo) throws VehiculoNoEncontradoException {
        boolean eliminado = repositorio.eliminarPorCodigo(codigo);
        if (!eliminado) {
            throw new VehiculoNoEncontradoException("No existe el vehículo con el código indicado", codigo);
        }
        return true;
    }

    /**
     * Normaliza los campos de texto de un vehículo (marca, modelo, color, etc).
     * Aplica reglas de capitalización y elimina espacios extra.
     *
     * @param vehiculo el vehículo a normalizar
     */
    private void normalizarTextosVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return;
        }

        vehiculo.setMarca(normalizarTexto(vehiculo.getMarca()));
        vehiculo.setModelo(normalizarTexto(vehiculo.getModelo()));
        vehiculo.setColor(normalizarTexto(vehiculo.getColor()));

        if (vehiculo instanceof Automovil) {
            Automovil automovil = (Automovil) vehiculo;
            automovil.setCarroceria(normalizarTexto(automovil.getCarroceria()));
        } else if (vehiculo instanceof Camioneta) {
            Camioneta camioneta = (Camioneta) vehiculo;
            camioneta.setCarroceria(normalizarTexto(camioneta.getCarroceria()));
        } else if (vehiculo instanceof Motocicleta) {
            Motocicleta motocicleta = (Motocicleta) vehiculo;
            motocicleta.setTipoMotocicleta(normalizarTexto(motocicleta.getTipoMotocicleta()));
        }
    }

    /**
     * Normaliza un texto aplicando capitalización de título, manteniendo siglas
     * en mayúsculas y preservando tokens con dígitos (por ejemplo "i20" o "CX-5").
     *
     * @param texto el texto a normalizar
     * @return el texto normalizado
     */
    private String normalizarTexto(String texto) {
        if (texto == null) {
            return null;
        }

        String limpio = texto.trim().replaceAll("\\s+", " ");
        if (limpio.isEmpty()) {
            return limpio;
        }

        String[] palabras = limpio.split(" ");
        StringBuilder normalizado = new StringBuilder();

        for (int i = 0; i < palabras.length; i++) {
            if (i > 0) {
                normalizado.append(' ');
            }

            String palabra = palabras[i];
            if (esSigla(palabra)) {
                normalizado.append(palabra.toUpperCase(Locale.ROOT));
            } else if (contieneDigito(palabra)) {
                normalizado.append(palabra);
            } else {
                String minuscula = palabra.toLowerCase(Locale.ROOT);
                normalizado.append(Character.toUpperCase(minuscula.charAt(0)))
                        .append(minuscula.substring(1));
            }
        }
        return normalizado.toString();
    }

    /**
     * Determina si una palabra es una sigla conocida que debe mantenerse en mayúsculas.
     *
     * @param palabra la palabra a evaluar
     * @return true si es una sigla conocida, false en caso contrario
     */
    private static boolean esSigla(String palabra) {
        return SIGLAS.contains(palabra.toUpperCase(Locale.ROOT));
    }

    /**
     * Determina si una palabra contiene al menos un dígito.
     * Los tokens con dígitos (i20, CX-5, MT-03) se preservan tal como los ingresó el usuario.
     *
     * @param palabra la palabra a evaluar
     * @return true si contiene un dígito
     */
    private static boolean contieneDigito(String palabra) {
        for (int i = 0; i < palabra.length(); i++) {
            if (Character.isDigit(palabra.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valida que todos los datos del vehículo sean correctos según las reglas de negocio.
     *
     * @param vehiculo el vehículo a validar
     * @throws DatoVehiculoInvalidoException si algún dato es inválido
     */
    private void validarVehiculo(Vehiculo vehiculo) throws DatoVehiculoInvalidoException {
        if (vehiculo == null) {
            throw new DatoVehiculoInvalidoException("El vehículo no puede ser null", "vehiculo", "null");
        }
        if (vehiculo.getCodigoInventario() != null) {
            throw new DatoVehiculoInvalidoException("El vehículo ya tiene código de inventario", "codigoInventario", vehiculo.getCodigoInventario());
        }

        validarObligatorio("marca", vehiculo.getMarca());
        validarLongitud("marca", vehiculo.getMarca(), MAX_MARCA);
        validarObligatorio("modelo", vehiculo.getModelo());
        validarLongitud("modelo", vehiculo.getModelo(), MAX_MODELO);
        validarObligatorio("color", vehiculo.getColor());
        validarLongitud("color", vehiculo.getColor(), MAX_COLOR);

        TipoVehiculo tipo = vehiculo.obtenerTipoVehiculo();
        if (tipo == null) {
            throw new DatoVehiculoInvalidoException("El tipo de vehículo no puede ser null", "tipoVehiculo", "null");
        }

        if (vehiculo instanceof Automovil) {
            String carroceria = ((Automovil) vehiculo).getCarroceria();
            validarObligatorio("carroceria", carroceria);
            validarLongitud("carroceria", carroceria, MAX_DETALLE);
        } else if (vehiculo instanceof Camioneta) {
            String carroceria = ((Camioneta) vehiculo).getCarroceria();
            validarObligatorio("carroceria", carroceria);
            validarLongitud("carroceria", carroceria, MAX_DETALLE);
        } else if (vehiculo instanceof Motocicleta) {
            String tipoMotocicleta = ((Motocicleta) vehiculo).getTipoMotocicleta();
            validarObligatorio("tipoMotocicleta", tipoMotocicleta);
            validarLongitud("tipoMotocicleta", tipoMotocicleta, MAX_DETALLE);
        }

        EstadoUnidad estado = vehiculo.getEstado();
        if (estado == null) {
            throw new DatoVehiculoInvalidoException("El estado no puede ser null", "estado", "null");
        }

        Integer anio = vehiculo.getAnio();
        int anioMaximo = Year.now().getValue() + 1;
        if (anio == null || anio < 1950 || anio > anioMaximo) {
            throw new DatoVehiculoInvalidoException("El año debe estar entre 1950 y " + anioMaximo,
                    "anio", String.valueOf(anio));
        }

        Double precio = vehiculo.getPrecioLista();
        if (precio == null || !Double.isFinite(precio) || precio <= 0) {
            throw new DatoVehiculoInvalidoException("El precio debe ser un número finito mayor a cero",
                    "precioLista", String.valueOf(precio));
        }
        BigDecimal precioRedondeado = BigDecimal.valueOf(precio).setScale(2, RoundingMode.HALF_UP);
        if (precioRedondeado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatoVehiculoInvalidoException("El precio debe ser mayor a cero",
                    "precioLista", String.valueOf(precio));
        }
        if (precioRedondeado.compareTo(PRECIO_MAXIMO) > 0) {
            throw new DatoVehiculoInvalidoException("El precio no puede superar " + PRECIO_MAXIMO,
                    "precioLista", String.valueOf(precio));
        }

        Integer kilometraje = vehiculo.getKilometraje();
        if (kilometraje == null || kilometraje < 0) {
            throw new DatoVehiculoInvalidoException("El kilometraje no puede ser negativo ni null", "kilometraje", String.valueOf(kilometraje));
        }
        if (estado == EstadoUnidad.CERO_KM && kilometraje != 0) {
            throw new DatoVehiculoInvalidoException("Un vehículo CERO_KM debe tener kilometraje cero", "kilometraje", String.valueOf(kilometraje));
        }
        if (estado == EstadoUnidad.USADO && kilometraje <= 0) {
            throw new DatoVehiculoInvalidoException("Un vehículo USADO debe tener kilometraje mayor a cero", "kilometraje", String.valueOf(kilometraje));
        }

        vehiculo.setPrecioLista(precioRedondeado.doubleValue());
    }

    /**
     * Retorna un guion si el valor es null o vacío, sino retorna el valor.
     * Utilizado para formatear la tabla de vehículos.
     *
     * @param valor el valor a evaluar
     * @return el valor o "-" si es null/vacío
     */
    private static String valorOGuion(String valor) {
        return valor == null || valor.trim().isEmpty() ? "-" : valor;
    }

    /**
     * Obtiene el detalle específico de la subclase (carrocería o tipo de motocicleta).
     *
     * @param vehiculo vehículo a inspeccionar
     * @return detalle específico o cadena vacía
     */
    private static String obtenerDetalle(Vehiculo vehiculo) {
        if (vehiculo instanceof Automovil) {
            return ((Automovil) vehiculo).getCarroceria();
        }
        if (vehiculo instanceof Camioneta) {
            return ((Camioneta) vehiculo).getCarroceria();
        }
        if (vehiculo instanceof Motocicleta) {
            return ((Motocicleta) vehiculo).getTipoMotocicleta();
        }
        return "";
    }

    /**
     * Formatea una línea de tabla con todas las columnas activas.
     *
     * @param columnas columnas a incluir
     * @param valor función que obtiene el valor de cada columna
     * @return línea con ancho fijo
     */
    private static String formatearLinea(List<Columna> columnas, Function<Columna, String> valor) {
        StringBuilder linea = new StringBuilder();
        for (int i = 0; i < columnas.size(); i++) {
            if (i > 0) {
                linea.append(" | ");
            }
            Columna columna = columnas.get(i);
            linea.append(ajustar(valor.apply(columna), columna.ancho));
        }
        return linea.toString();
    }

    /**
     * Genera una línea separadora que respeta el ancho exacto de cada columna.
     *
     * @param columnas columnas activas
     * @return separador de guiones y cruces
     */
    private static String formatearSeparador(List<Columna> columnas) {
        StringBuilder separador = new StringBuilder();
        for (int i = 0; i < columnas.size(); i++) {
            if (i > 0) {
                separador.append("-+-");
            }
            separador.append("-".repeat(columnas.get(i).ancho));
        }
        return separador.toString();
    }

    /**
     * Calcula el ancho total de una tabla, incluyendo los separadores " | ".
     *
     * @param columnas columnas activas
     * @return ancho total en caracteres
     */
    private static int anchoTotal(List<Columna> columnas) {
        int total = 0;
        for (Columna columna : columnas) {
            total += columna.ancho;
        }
        return total + Math.max(0, columnas.size() - 1) * 3;
    }

    /**
     * Ajusta un valor al ancho de la columna, recortando con puntos suspensivos
     * si es demasiado largo y rellenando con espacios si es más corto.
     *
     * @param valor valor a ajustar
     * @param ancho ancho de la columna
     * @return texto de exactamente {@code ancho} caracteres
     */
    private static String ajustar(String valor, int ancho) {
        String texto = valor == null || valor.trim().isEmpty() ? "-" : valor.trim();
        if (texto.length() > ancho) {
            texto = ancho > 1 ? texto.substring(0, ancho - 1) + "…" : texto.substring(0, ancho);
        }
        return texto + " ".repeat(Math.max(0, ancho - texto.length()));
    }

    /**
     * Valida que un campo de texto no sea null ni esté vacío.
     *
     * @param campo el nombre del campo para el mensaje de error
     * @param valor el valor a validar
     * @throws DatoVehiculoInvalidoException si el valor es null o vacío
     */
    private void validarObligatorio(String campo, String valor) throws DatoVehiculoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoVehiculoInvalidoException("El campo " + campo + " no puede estar vacío", campo, String.valueOf(valor));
        }
    }

    /**
     * Valida que un campo de texto no supere la longitud máxima permitida por el esquema.
     *
     * @param campo el nombre del campo para el mensaje de error
     * @param valor el valor a validar
     * @param maximo longitud máxima permitida
     * @throws DatoVehiculoInvalidoException si el valor supera la longitud máxima
     */
    private void validarLongitud(String campo, String valor, int maximo) throws DatoVehiculoInvalidoException {
        if (valor != null && valor.length() > maximo) {
            throw new DatoVehiculoInvalidoException(
                    "El campo " + campo + " no puede superar " + maximo + " caracteres",
                    campo, valor);
        }
    }

    /**
     * Columna de la tabla de vehículos.
     */
    private static final class Columna {

        private final String encabezado;
        private final int ancho;
        private final Function<Vehiculo, String> valor;
        private final int prioridadRemocion;

        private Columna(String encabezado, int ancho, Function<Vehiculo, String> valor, int prioridadRemocion) {
            this.encabezado = encabezado;
            this.ancho = ancho;
            this.valor = valor;
            this.prioridadRemocion = prioridadRemocion;
        }

        private String getEncabezado() {
            return encabezado;
        }
    }
}
