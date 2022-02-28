package juan.estevez.sistemaventa.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ProveedorDAO {
    
    Connection cn;
    PreparedStatement pst;
    ResultSet rs;

    /**
     * Registra un proveedor en la base de datos.
     *
     * @param proveedor a registrar
     * @return true si se registró, false si no se registró
     */
    public boolean registrarProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO PROVEEDOR (RUT, NOMBRE, TELEFONO, DIRECCION, RAZON_SOCIAL) VALUES (?,?,?,?,?)";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setInt(1, proveedor.getRut());
            pst.setString(2, proveedor.getNombre());
            pst.setInt(3, proveedor.getTelefono());
            pst.setString(4, proveedor.getDireccion());
            pst.setString(5, proveedor.getRazonSocial());
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally {
            try {
                pst.close();
                cn.close();
            } catch (SQLException e) {
                System.err.println(e.toString());
            }
        }
    }
}
