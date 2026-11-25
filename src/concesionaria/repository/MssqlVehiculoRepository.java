package concesionaria.repository;

import concesionaria.config.ConexionDB;
import concesionaria.exception.PersistenciaException;
import concesionaria.interfaces.VehiculoRepository;
import concesionaria.model.Automovil;
import concesionaria.model.Camioneta;
import concesionaria.model.Motocicleta;
import concesionaria.model.Vehiculo;
import concesionaria.model.enums.EstadoUnidad;
import concesionaria.model.enums.TipoVehiculo;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementación del repositorio de vehículos usando Sql2o para persistencia en MSSQL.
 * <p>
 * Esta clase implementa la interfaz VehiculoRepository y utiliza Sql2o para ejecutar
 * consultas parametrizadas contra la base de datos MSSQL. Genera el código de inventario
 * mediante la columna calculada de MSSQL y lo asigna al objeto vehículo una única vez.
 * </p>
 */
public class MssqlVehiculoRepository implements VehiculoRepository {

    private final Sql2o sql2o;

    /**
     * Crea una nueva instancia del repositorio con la conexión Sql2o especificada.
     *
     * @param sql2o la instancia de Sql2o para conectar a la base de datos
     */
    public MssqlVehiculoRepository(Sql2o sql2o) {
        if (sql2o == null) {
            throw new IllegalArgumentException("La instancia de Sql2o no puede ser null");
        }
        this.sql2o = sql2o;
    }

    /**
     * Crea una nueva instancia usando la conexión por defecto de ConexionDB.
     */
    public MssqlVehiculoRepository() {
        this(ConexionDB.sql2o);
    }

    /**
     * Guarda un nuevo vehículo en la base de datos y le asigna el código de inventario generado.
     *
     * @param vehiculo el vehículo a guardar
     * @return el vehículo guardado con el código de inventario asignado
     * @throws PersistenciaException si ocurre un error al guardar en la base de datos
     */
    @Override
    public Vehiculo guardar(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser null");
        }
        if (vehiculo.getCodigoInventario() != null) {
            throw new IllegalArgumentException("El vehículo ya tiene código de inventario");
        }

        String insertSql = "INSERT INTO Vehiculos (tipo_vehiculo, marca, modelo, anio, color, estado_unidad, kilometraje, precio_lista, carroceria, tipo_motocicleta) " +
                           "OUTPUT INSERTED.codigo_inventario " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = sql2o.beginTransaction()) {
            String tipoVehiculo = vehiculo.obtenerTipoVehiculo().name();
            String carroceria = null;
            String tipoMotocicleta = null;

            if (vehiculo instanceof Automovil) {
                carroceria = ((Automovil) vehiculo).getCarroceria();
            } else if (vehiculo instanceof Camioneta) {
                carroceria = ((Camioneta) vehiculo).getCarroceria();
            } else if (vehiculo instanceof Motocicleta) {
                tipoMotocicleta = ((Motocicleta) vehiculo).getTipoMotocicleta();
            }

            try (PreparedStatement ps = con.getJdbcConnection().prepareStatement(insertSql)) {
                ps.setString(1, tipoVehiculo);
                ps.setString(2, vehiculo.getMarca());
                ps.setString(3, vehiculo.getModelo());
                ps.setInt(4, vehiculo.getAnio());
                ps.setString(5, vehiculo.getColor());
                ps.setString(6, vehiculo.getEstado().name());
                ps.setInt(7, vehiculo.getKilometraje());
                ps.setBigDecimal(8, BigDecimal.valueOf(vehiculo.getPrecioLista())
                        .setScale(2, RoundingMode.HALF_UP));
                ps.setString(9, carroceria);
                ps.setString(10, tipoMotocicleta);

                String codigoGenerado;
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new PersistenciaException(
                                "No se pudo recuperar el código de inventario generado",
                                "guardar", null);
                    }
                    codigoGenerado = rs.getString(1);
                }

                if (codigoGenerado == null || codigoGenerado.trim().isEmpty()) {
                    throw new PersistenciaException(
                            "La base de datos devolvió un código de inventario vacío",
                            "guardar", null);
                }

                con.commit();
                vehiculo.asignarCodigoInventario(codigoGenerado);
                return vehiculo;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar vehículo en base de datos", "guardar", e);
        }
    }

    /**
     * Busca un vehículo por su código de inventario.
     *
     * @param codigo el código de inventario del vehículo
     * @return el vehículo encontrado o null si no existe
     * @throws PersistenciaException si ocurre un error al buscar en la base de datos
     */
    @Override
    public Vehiculo buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM Vehiculos WHERE codigo_inventario = :codigo";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("codigo", codigo)
                    .executeAndFetchFirst(new ResultSetHandler<Vehiculo>() {
                        @Override
                        public Vehiculo handle(ResultSet rs) throws SQLException {
                            return mapearVehiculo(rs);
                        }
                    });
        } catch (Sql2oException e) {
            throw new PersistenciaException("Error al buscar vehículo por código", e);
        }
    }

    /**
     * Lista todos los vehículos almacenados en la base de datos.
     *
     * @return lista de todos los vehículos
     * @throws PersistenciaException si ocurre un error al listar los vehículos
     */
    @Override
    public List<Vehiculo> listarTodos() {
        String sql = "SELECT * FROM Vehiculos";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .executeAndFetch(new ResultSetHandler<Vehiculo>() {
                        @Override
                        public Vehiculo handle(ResultSet rs) throws SQLException {
                            return mapearVehiculo(rs);
                        }
                    });
        } catch (Sql2oException e) {
            throw new PersistenciaException("Error al listar todos los vehículos", e);
        }
    }

    /**
     * Elimina un vehículo de la base de datos por su código de inventario.
     *
     * @param codigo el código de inventario del vehículo a eliminar
     * @return true si se eliminó el vehículo, false si no existía
     * @throws PersistenciaException si ocurre un error al eliminar el vehículo
     */
    @Override
    public boolean eliminarPorCodigo(String codigo) {
        String sql = "DELETE FROM Vehiculos WHERE codigo_inventario = :codigo";

        try (Connection con = sql2o.open()) {
            int filasAfectadas = con.createQuery(sql)
                    .addParameter("codigo", codigo)
                    .executeUpdate()
                    .getResult();
            return filasAfectadas > 0;
        } catch (Sql2oException e) {
            throw new PersistenciaException("Error al eliminar vehículo", e);
        }
    }

    /**
     * Lista todos los vehículos que tienen un estado específico.
     *
     * @param estado el estado de unidad a filtrar
     * @return lista de vehículos con el estado especificado
     * @throws PersistenciaException si ocurre un error al listar los vehículos
     */
    @Override
    public List<Vehiculo> listarPorEstado(EstadoUnidad estado) {
        String sql = "SELECT * FROM Vehiculos WHERE estado_unidad = :estado";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("estado", estado.name())
                    .executeAndFetch(new ResultSetHandler<Vehiculo>() {
                        @Override
                        public Vehiculo handle(ResultSet rs) throws SQLException {
                            return mapearVehiculo(rs);
                        }
                    });
        } catch (Sql2oException e) {
            throw new PersistenciaException("Error al listar vehículos por estado", e);
        }
    }

    /**
     * Lista todos los vehículos de un tipo específico.
     *
     * @param tipo el tipo de vehículo a filtrar
     * @return lista de vehículos del tipo especificado
     * @throws PersistenciaException si ocurre un error al listar los vehículos
     */
    @Override
    public List<Vehiculo> listarPorTipo(TipoVehiculo tipo) {
        String sql = "SELECT * FROM Vehiculos WHERE tipo_vehiculo = :tipo";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("tipo", tipo.name())
                    .executeAndFetch(new ResultSetHandler<Vehiculo>() {
                        @Override
                        public Vehiculo handle(ResultSet rs) throws SQLException {
                            return mapearVehiculo(rs);
                        }
                    });
        } catch (Sql2oException e) {
            throw new PersistenciaException("Error al listar vehículos por tipo", e);
        }
    }

    /**
     * Método factory privado que mapea un registro de base de datos a la subclase
     * de Vehiculo correspondiente según el tipo_vehiculo.
     *
     * @param resultado el ResultSet JDBC con los datos del vehículo
     * @return la instancia de la subclase correspondiente
     * @throws SQLException si hay error al leer del ResultSet
     */
    private Vehiculo mapearVehiculo(ResultSet resultado) throws SQLException {
        String tipoVehiculoStr = resultado.getString("tipo_vehiculo");
        TipoVehiculo tipoVehiculo = TipoVehiculo.fromString(tipoVehiculoStr);
        String estadoUnidadStr = resultado.getString("estado_unidad");
        EstadoUnidad estado = EstadoUnidad.fromString(estadoUnidadStr);

        String codigoInventario = resultado.getString("codigo_inventario");
        String marca = resultado.getString("marca");
        String modelo = resultado.getString("modelo");
        Integer anio = resultado.getInt("anio");
        String color = resultado.getString("color");
        Integer kilometraje = resultado.getInt("kilometraje");
        Double precioLista = resultado.getBigDecimal("precio_lista").doubleValue();
        String carroceria = resultado.getString("carroceria");
        String tipoMotocicleta = resultado.getString("tipo_motocicleta");

        Vehiculo vehiculo;

        switch (tipoVehiculo) {
            case AUTOMOVIL:
                vehiculo = new Automovil(marca, modelo, anio, color, estado, kilometraje, precioLista, carroceria);
                break;
            case CAMIONETA:
                vehiculo = new Camioneta(marca, modelo, anio, color, estado, kilometraje, precioLista, carroceria);
                break;
            case MOTOCICLETA:
                vehiculo = new Motocicleta(marca, modelo, anio, color, estado, kilometraje, precioLista, tipoMotocicleta);
                break;
            default:
                throw new PersistenciaException("Tipo de vehículo desconocido: " + tipoVehiculoStr, null);
        }

        vehiculo.asignarCodigoInventario(codigoInventario);
        return vehiculo;
    }
}
