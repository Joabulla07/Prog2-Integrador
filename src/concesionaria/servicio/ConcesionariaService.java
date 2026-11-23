package concesionaria.servicio;

import concesionaria.exception.DatoVehiculoInvalidoException;
import concesionaria.exception.VehiculoNoEncontradoException;
import concesionaria.model.Automovil;
import concesionaria.model.Camioneta;
import concesionaria.model.Motocicleta;
import concesionaria.model.Vehiculo;
import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;
import concesionaria.repository.VehiculoRepository;

import java.time.Year;

/**
 * Coordina las operaciones de negocio de la concesionaria.
 */
public class ConcesionariaService {

    private final VehiculoRepository repositorio;

    public ConcesionariaService(VehiculoRepository repositorio) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio no puede ser null");
        }
        this.repositorio = repositorio;
    }

    public Vehiculo registrarVehiculo(Vehiculo vehiculo) throws DatoVehiculoInvalidoException {
        validarVehiculo(vehiculo);
        return repositorio.guardar(vehiculo);
    }

    public String generarReporteDirectivos() {
        java.util.List<Vehiculo> vehiculos = repositorio.listarTodos();
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
        reporte.append("Reporte directivo\n");
        reporte.append("AUTOMOVIL: ").append(automoviles).append("\n");
        reporte.append("CAMIONETA: ").append(camionetas).append("\n");
        reporte.append("MOTOCICLETA: ").append(motocicletas).append("\n");
        reporte.append("CERO_KM: ").append(ceroKm).append("\n");
        reporte.append("USADO: ").append(usados).append("\n");
        reporte.append("Fichas:\n");
        for (Vehiculo vehiculo : vehiculos) {
            reporte.append("- ").append(vehiculo.obtenerFichaDirectiva()).append("\n");
        }
        return reporte.toString();
    }

    public Vehiculo obtenerVehiculoPorCodigo(String codigo) throws VehiculoNoEncontradoException {
        Vehiculo vehiculo = repositorio.buscarPorCodigo(codigo);
        if (vehiculo == null) {
            throw new VehiculoNoEncontradoException("No existe el vehículo con el código indicado", codigo);
        }
        return vehiculo;
    }

    public boolean eliminarVehiculo(String codigo) throws VehiculoNoEncontradoException {
        boolean eliminado = repositorio.eliminarPorCodigo(codigo);
        if (!eliminado) {
            throw new VehiculoNoEncontradoException("No existe el vehículo con el código indicado", codigo);
        }
        return true;
    }

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

    private void validarObligatorio(String campo, String valor) throws DatoVehiculoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoVehiculoInvalidoException("El campo " + campo + " no puede estar vacío", campo, String.valueOf(valor));
        }
    }
}
