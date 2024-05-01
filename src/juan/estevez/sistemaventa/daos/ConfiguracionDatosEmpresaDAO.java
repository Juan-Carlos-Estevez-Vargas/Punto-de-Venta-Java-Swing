package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.modelo.ConfiguracionDatosEmpresa;
import juan.estevez.sistemaventa.utils.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO para operaciones relacionadas con los datos de la Empresa.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ConfiguracionDatosEmpresaDAO {

    private static final String UPDATE_DATOS_EMPRESA_SQL = "UPDATE CONFIGURACION SET RUT = ?, NOMBRE = ?, TELEFONO = ?, DIRECCION = ?, RAZON_SOCIAL = ? WHERE ID = ?";
    private static final String SELECT_ALL_DATOS_EMPRESA_SQL = "SELECT * FROM CONFIGURACION";

    /**
     * Busca los datos de la empresa en la base de datos.
     *
     * @return un objeto ConfiguracionDatosEmpresa que contiene los datos de la empresa almacenados en la base de datos, o null si no se encontraron datos.
     * @throws SQLException si ocurre un error al buscar los datos de la empresa en la base de datos.
     */
    public ConfiguracionDatosEmpresa buscarDatosEmpresa() throws SQLException {
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(SELECT_ALL_DATOS_EMPRESA_SQL); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                return crearDatosEmpresaDesdeResulset(rs);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar los datos de la empresa en ConfiguracionDatosEmpresaDAO", e);
        }
        return null;
    }

    /**
     * Modifica los datos de la empresa en la base de datos.
     *
     * @param configuracionDatosEmpresa el objeto ConfiguracionDatosEmpresa con los nuevos datos de la empresa a modificar.
     * @throws SQLException si ocurre un error al modificar los datos de la empresa en la base de datos.
     */
    public void modificarDatosEmpresa(ConfiguracionDatosEmpresa configuracionDatosEmpresa) throws SQLException {
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(UPDATE_DATOS_EMPRESA_SQL)) {
            crearResultSetConfiguracionDatosEmpresa(pst, configuracionDatosEmpresa);
            pst.execute();
        } catch (SQLException e) {
            throw new SQLException("Error al modificar los datos de la empresa en ConfiguracionDatosEmpresaDAO", e);
        }
    }

    /**
     * Crea un objeto de tipo ConfiguracionDatosEmpresa con los datos provenientes de la base de datos.
     *
     * @param rs ResulSet con la información de la base de datos.
     * @return objeto de tipo ConfiguracionDatosEmpresa con los datos de la empresa.
     * @throws SQLException en caso de error con la base de datos.
     */
    private ConfiguracionDatosEmpresa crearDatosEmpresaDesdeResulset(ResultSet rs) throws SQLException {
        ConfiguracionDatosEmpresa configuracionDatosEmpresa = new ConfiguracionDatosEmpresa();
        configuracionDatosEmpresa.setId(rs.getInt("ID"));
        configuracionDatosEmpresa.setRut(rs.getLong("RUT"));
        configuracionDatosEmpresa.setNombre(rs.getString("NOMBRE"));
        configuracionDatosEmpresa.setTelefono(rs.getLong("TELEFONO"));
        configuracionDatosEmpresa.setDireccion(rs.getString("DIRECCION"));
        configuracionDatosEmpresa.setRazonSocial(rs.getString("RAZON_SOCIAL"));
        return configuracionDatosEmpresa;
    }

    /**
     * Setea los datos de la configuración de la empresa al objeto encargado de persistirlos en la base de datos.
     *
     * @param pst objeto encargado de persistir la data en la base de datos.
     * @param configuracionDatosEmpresa objeto con los datos a persistir.
     * @throws SQLException en caso de error con la base de datos.
     */
    private void crearResultSetConfiguracionDatosEmpresa(PreparedStatement pst, ConfiguracionDatosEmpresa configuracionDatosEmpresa) throws SQLException {
        pst.setLong(1, configuracionDatosEmpresa.getRut());
        pst.setString(2, configuracionDatosEmpresa.getNombre());
        pst.setLong(3, configuracionDatosEmpresa.getTelefono());
        pst.setString(4, configuracionDatosEmpresa.getDireccion());
        pst.setString(5, configuracionDatosEmpresa.getRazonSocial());
        pst.setInt(6, configuracionDatosEmpresa.getId());
    }

}
