package concesionaria.repository;

import concesionaria.model.Vehiculo;
import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;

import java.util.List;

/**
 * Interfaz específica del repositorio de vehículos.
 * <p>
 * Esta interfaz extiende el repositorio genérico y agrega métodos específicos
 * del dominio para filtrar vehículos por estado y tipo.
 * </p>
 */
public interface VehiculoRepository extends RepositorioGenerico<Vehiculo, String> {

    /**
     * Lista todos los vehículos filtrados por su estado de unidad.
     *
     * @param estado el estado de unidad a filtrar (CERO_KM o USADO)
     * @return lista de vehículos que coinciden con el estado especificado
     * @throws IllegalArgumentException si el estado es null
     */
    List<Vehiculo> listarPorEstado(EstadoUnidad estado);

    /**
     * Lista todos los vehículos filtrados por su tipo.
     *
     * @param tipo el tipo de vehículo a filtrar (AUTOMOVIL, CAMIONETA o MOTOCICLETA)
     * @return lista de vehículos que coinciden con el tipo especificado
     * @throws IllegalArgumentException si el tipo es null
     */
    List<Vehiculo> listarPorTipo(TipoVehiculo tipo);
}
