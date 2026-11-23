package concesionaria.repository;

import concesionaria.config.ConexionDB;
import concesionaria.exception.PersistenciaException;
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        this.sql2o = sql2o;
    }

    /**
     * Crea una nueva instancia usando la conexión por defecto de ConexionDB.
     */
    public MssqlVehiculoRepository() {
        this(ConexionDB.sql2o);
    }

    @Override
    public Vehiculo guardar(Vehiculo vehiculo) {
        String insertSql = "INSERT INTO Vehiculos (tipo_vehiculo, marca, modelo, anio, color, estado_unidad, kilometraje, precio_lista, carroceria, tipo_motocicleta) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (java.sql.Connection con = sql2o.open().getJdbcConnection();
             PreparedStatement ps = con.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

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

            ps.setString(1, tipoVehiculo);
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getAnio());
            ps.setString(5, vehiculo.getColor());
            ps.setString(6, vehiculo.getEstado().name());
            ps.setInt(7, vehiculo.getKilometraje());
            ps.setDouble(8, vehiculo.getPrecioLista());
            ps.setString(9, carroceria);
            ps.setString(10, tipoMotocicleta);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        int idVehiculo = rsKeys.getInt(1);
                        String selectSql = "SELECT codigo_inventario FROM Vehiculos WHERE id_vehiculo = ?";
                        try (PreparedStatement psSelect = con.prepareStatement(selectSql)) {
                            psSelect.setInt(1, idVehiculo);
                            try (ResultSet rs = psSelect.executeQuery()) {
                                if (rs.next()) {
                                    String codigoGenerado = rs.getString("codigo_inventario");
                                    vehiculo.asignarCodigoInventario(codigoGenerado);
                                }
                            }
                        }
                    }
                }
            }

            return vehiculo;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar vehículo en base de datos", e);
        }
    }

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
    private Vehiculo mapearVehiculo(java.sql.ResultSet resultado) throws SQLException {
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
