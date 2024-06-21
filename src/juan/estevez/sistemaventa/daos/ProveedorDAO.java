package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.utils.Conexion;
import juan.estevez.sistemaventa.modelo.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones relacionadas con Proveedores.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ProveedorDAO {
    
    private static ProveedorDAO instance;
    private final Connection connection;

    private static final String INSERT_PROVEEDOR_SQL = "INSERT INTO PROVEEDOR (RUT, NOMBRE, TELEFONO, DIRECCION, RAZON_SOCIAL) VALUES (?,?,?,?,?)";
    private static final String SELECT_ALL_PROVEEDORES_SQL = "SELECT * FROM PROVEEDOR";
    private static final String DELETE_PROVEEDOR_SQL = "DELETE FROM PROVEEDOR WHERE ID = ?";
    private static final String UPDATE_PROVEEDOR_SQL = "UPDATE PROVEEDOR SET RUT = ?, NOMBRE = ?, TELEFONO = ?, DIRECCION = ?, RAZON_SOCIAL = ? WHERE ID = ?";

    public static ProveedorDAO getInstance() {
        return instance == null ?  new ProveedorDAO() : instance;
    }
    
    private ProveedorDAO() {
        this.connection = Conexion.getInstance().getConnection();
    }
    
    /**
     * Registra un proveedor en la base de datos.
     *
     * @param proveedor el proveedor a registrar.
     * @throws SQLException si ocurre un error al registrar el proveedor.
     */
    public void registrarProveedor(Proveedor proveedor) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(INSERT_PROVEEDOR_SQL)) {
            crearPreparedStatementDesdeProveedor(pst, proveedor);
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al registrar proveedor", e);
        }
    }

    /**
     * Obtiene una lista de todos los proveedores registrados en la base de datos.
     *
     * @return una lista de objetos Proveedor que representan a los proveedores registrados.
     * @throws SQLException si ocurre un error al obtener la lista de proveedores.
     */
    public List<Proveedor> listarProveedores() throws SQLException {
        List<Proveedor> listaProveedores = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(SELECT_ALL_PROVEEDORES_SQL); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                listaProveedores.add(crearProveedorDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al listar proveedores", e);
        }
        return listaProveedores;
    }

    /**
     * Elimina un proveedor de la base de datos dado su ID.
     *
     * @param id el ID del proveedor a eliminar.
     * @throws SQLException si ocurre un error al eliminar el proveedor.
     */
    public void eliminarProveedor(int id) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(DELETE_PROVEEDOR_SQL)) {
            pst.setInt(1, id);
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar proveedor", e);
        }
    }

    /**
     * Modifica los datos de un proveedor en la base de datos.
     *
     * @param proveedor el proveedor con los datos actualizados.
     * @throws SQLException si ocurre un error al modificar el proveedor.
     */
    public void modificarProveedor(Proveedor proveedor) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(UPDATE_PROVEEDOR_SQL)) {
            crearPreparedStatementDesdeProveedor(pst, proveedor);
            pst.setInt(6, proveedor.getId());
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al modificar proveedor", e);
        }
    }

    /**
     * Crea un objeto de tipo Proveedor con los datos provenientes de la base de datos.
     *
     * @param rs ResulSet con la información de la base de datos.
     * @return objeto de tipo Proveedor con los datos de inicio de sesión.
     * @throws SQLException en caso de error con la base de datos.
     */
    private Proveedor crearProveedorDesdeResultSet(ResultSet rs) throws SQLException {
        return Proveedor.builder()
                .id(rs.getInt("ID"))
                .rut(rs.getLong("RUT"))
                .nombre(rs.getString("NOMBRE"))
                .telefono(rs.getLong("TELEFONO"))
                .direccion(rs.getString("DIRECCION"))
                .razonSocial(rs.getString("RAZON_SOCIAL"))
                .build();
    }

    /**
     * Setea los datos del proveedor al objeto encargado de persistirlos en la base de datos.
     *
     * @param pst objeto encargado de persistir la data en la base de datos.
     * @param proveedor objeto con los datos a persistir.
     * @throws SQLException en caso de error con la base de datos.
     */
    private void crearPreparedStatementDesdeProveedor(PreparedStatement pst, Proveedor proveedor) throws SQLException {
        pst.setLong(1, proveedor.getRut());
        pst.setString(2, proveedor.getNombre());
        pst.setLong(3, proveedor.getTelefono());
        pst.setString(4, proveedor.getDireccion());
        pst.setString(5, proveedor.getRazonSocial());
    }

}
