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

import java.time.Year;
import java.util.List;
import java.util.Locale;

/**
 * Coordina las operaciones de negocio de la concesionaria.
 */
public class ConcesionariaService {

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
        reporte.append("%-12s | %-12s | %-16s | %-20s | %-4s | %-16s | %-8s | %-10s | %-14s | %-22s | %-20s | %-8s%n".formatted(
                "Código", "Tipo", "Marca", "Modelo", "Año", "Color", "Estado", "Km", "Precio", "Detalle", "Categoría", "Entrega"));
        reporte.append("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        if (vehiculos.isEmpty()) {
            reporte.append("No hay vehículos registrados.\n");
            return reporte.toString();
        }

        for (Vehiculo vehiculo : vehiculos) {
            String detalle = "";
            if (vehiculo instanceof Automovil) {
                detalle = ((Automovil) vehiculo).getCarroceria();
            } else if (vehiculo instanceof Camioneta) {
                detalle = ((Camioneta) vehiculo).getCarroceria();
            } else if (vehiculo instanceof Motocicleta) {
                detalle = ((Motocicleta) vehiculo).getTipoMotocicleta();
            }

            reporte.append("%-12s | %-12s | %-16s | %-20s | %-4s | %-16s | %-8s | %-10s | %-14s | %-22s | %-20s | %-8s%n".formatted(
                    valorOGuion(vehiculo.getCodigoInventario()),
                    vehiculo.obtenerTipoVehiculo() != null ? vehiculo.obtenerTipoVehiculo().getDescripcion() : "-",
                    valorOGuion(vehiculo.getMarca()),
                    valorOGuion(vehiculo.getModelo()),
                    vehiculo.getAnio() != null ? vehiculo.getAnio().toString() : "-",
                    valorOGuion(vehiculo.getColor()),
                    vehiculo.getEstado() != null ? vehiculo.getEstado().getDescripcion() : "-",
                    vehiculo.getKilometraje() != null ? vehiculo.getKilometraje().toString() : "-",
                    vehiculo.getPrecioLista() != null ? "$" + vehiculo.getPrecioLista() : "-",
                    valorOGuion(detalle),
                    vehiculo.obtenerCategoriaComercial(),
                    vehiculo.esAptoEntregaInmediata() ? "Sí" : "No"));
        }
        return reporte.toString();
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
     * Normaliza un texto aplicando capitalización de título y manteniendo siglas en mayúsculas.
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
    private boolean esSigla(String palabra) {
        String limpia = palabra.toUpperCase(Locale.ROOT);
        return limpia.equals("SUV")
                || limpia.equals("GTI")
                || limpia.equals("GR-S")
                || limpia.equals("MT-03")
                || limpia.equals("HRC")
                || limpia.equals("BMW")
                || limpia.equals("GTR")
                || limpia.equals("4X4");
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
        validarObligatorio("modelo", vehiculo.getModelo());
        validarObligatorio("color", vehiculo.getColor());

        TipoVehiculo tipo = vehiculo.obtenerTipoVehiculo();
        if (tipo == null) {
            throw new DatoVehiculoInvalidoException("El tipo de vehículo no puede ser null", "tipoVehiculo", "null");
        }

        if (vehiculo instanceof Automovil) {
            validarObligatorio("carroceria", ((Automovil) vehiculo).getCarroceria());
        } else if (vehiculo instanceof Camioneta) {
            validarObligatorio("carroceria", ((Camioneta) vehiculo).getCarroceria());
        } else if (vehiculo instanceof Motocicleta) {
            validarObligatorio("tipoMotocicleta", ((Motocicleta) vehiculo).getTipoMotocicleta());
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
        if (precio == null || precio <= 0) {
            throw new DatoVehiculoInvalidoException("El precio debe ser mayor a cero", "precioLista", String.valueOf(precio));
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
    }

    /**
     * Retorna un guion si el valor es null o vacío, sino retorna el valor.
     * Utilizado para formatear el reporte directivo.
     *
     * @param valor el valor a evaluar
     * @return el valor o "-" si es null/vacío
     */
    private String valorOGuion(String valor) {
        return valor == null || valor.trim().isEmpty() ? "-" : valor;
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
}
