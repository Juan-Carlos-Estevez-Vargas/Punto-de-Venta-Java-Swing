package juan.estevez.sistemaventa.daos;

import java.sql.*;
import juan.estevez.sistemaventa.modelo.*;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ConfiguracionDatosEmpresaDAO {

    Connection cn;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Captura los datos de la empresa.
     *
     * @return Objeto con los datos de la empresa.
     */
    public ConfiguracionDatosEmpresa buscarDatosEmpresa() {
        ConfiguracionDatosEmpresa configuracionDatosEmpresa = new ConfiguracionDatosEmpresa();
        String sql = "SELECT * FROM CONFIGURACION";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                configuracionDatosEmpresa.setId(rs.getInt("ID"));
                configuracionDatosEmpresa.setRut(rs.getLong("RUT"));
                configuracionDatosEmpresa.setNombre(rs.getString("NOMBRE"));
                configuracionDatosEmpresa.setTelefono(rs.getLong("TELEFONO"));
                configuracionDatosEmpresa.setDireccion(rs.getString("DIRECCION"));
                configuracionDatosEmpresa.setRazonSocial(rs.getString("RAZON_SOCIAL"));
            }
        } catch (SQLException e) {
            System.err.println("Error el listar los datos de la empresa en configuracionDatosEmpresaDAO " + e.toString());
        } finally {
            try {
                rs.close();
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en configuracionDatosEmpresaDAO " + e.toString());
            }
        }
        return configuracionDatosEmpresa;
    }

    /**
     * Modifica los datos de la empresa en la base de datos.
     *
     * @param configuracionDatosEmpresa a actualizar.
     * @return true si se modificó, false si no se modificó.
     */
    public boolean modificarDatosEmpresa(ConfiguracionDatosEmpresa configuracionDatosEmpresa) {
        String sql = "UPDATE CONFIGURACION SET RUT = ?, NOMBRE = ?, TELEFONO = ?, DIRECCION = ?, RAZON_SOCIAL = ? WHERE ID = ?";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setLong(1, configuracionDatosEmpresa.getRut());
            pst.setString(2, configuracionDatosEmpresa.getNombre());
            pst.setLong(3, configuracionDatosEmpresa.getTelefono());
            pst.setString(4, configuracionDatosEmpresa.getDireccion());
            pst.setString(5, configuracionDatosEmpresa.getRazonSocial());
            pst.setInt(6, configuracionDatosEmpresa.getId());
            pst.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error el modificar datos de la empresa en configuracionDatosEmpresaDAO " + e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar los objetos en configuracionDatosEmpresaDAO " + e.toString());
            }
        }
    }

}
