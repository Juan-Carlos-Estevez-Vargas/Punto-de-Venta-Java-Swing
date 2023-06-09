package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.utils.Conexion;
import java.sql.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 * DAO para operaciones relacionadas con los datos de la Empresa.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ConfiguracionDatosEmpresaDAO {

    /**
     * Busca los datos de la empresa en la base de datos.
     *
     * @return un objeto ConfiguracionDatosEmpresa que contiene los datos de la
     * empresa almacenados en la base de datos, o null si no se encontraron
     * datos.
     * @throws SQLException si ocurre un error al buscar los datos de la empresa
     * en la base de datos.
     */
    public ConfiguracionDatosEmpresa buscarDatosEmpresa() throws SQLException {
        String sql = "SELECT * FROM CONFIGURACION";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                ConfiguracionDatosEmpresa configuracionDatosEmpresa = new ConfiguracionDatosEmpresa();
                configuracionDatosEmpresa.setId(rs.getInt("ID"));
                configuracionDatosEmpresa.setRut(rs.getLong("RUT"));
                configuracionDatosEmpresa.setNombre(rs.getString("NOMBRE"));
                configuracionDatosEmpresa.setTelefono(rs.getLong("TELEFONO"));
                configuracionDatosEmpresa.setDireccion(rs.getString("DIRECCION"));
                configuracionDatosEmpresa.setRazonSocial(rs.getString("RAZON_SOCIAL"));
                return configuracionDatosEmpresa;
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar los datos de la empresa en ConfiguracionDatosEmpresaDAO", e);
        }
        return null;
    }

    /**
     * Modifica los datos de la empresa en la base de datos.
     *
     * @param configuracionDatosEmpresa el objeto ConfiguracionDatosEmpresa con
     * los nuevos datos de la empresa a modificar.
     * @return true si los datos de la empresa se modificaron correctamente,
     * false en caso contrario.
     * @throws SQLException si ocurre un error al modificar los datos de la
     * empresa en la base de datos.
     */
    public boolean modificarDatosEmpresa(ConfiguracionDatosEmpresa configuracionDatosEmpresa) throws SQLException {
        String sql = "UPDATE CONFIGURACION SET RUT = ?, NOMBRE = ?, TELEFONO = ?, DIRECCION = ?, RAZON_SOCIAL = ? WHERE ID = ?";
        try (Connection cn = Conexion.conectar(); PreparedStatement pst = cn.prepareStatement(sql)) {
            pst.setLong(1, configuracionDatosEmpresa.getRut());
            pst.setString(2, configuracionDatosEmpresa.getNombre());
            pst.setLong(3, configuracionDatosEmpresa.getTelefono());
            pst.setString(4, configuracionDatosEmpresa.getDireccion());
            pst.setString(5, configuracionDatosEmpresa.getRazonSocial());
            pst.setInt(6, configuracionDatosEmpresa.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            throw new SQLException("Error al modificar los datos de la empresa en ConfiguracionDatosEmpresaDAO", e);
        }
    }
}
