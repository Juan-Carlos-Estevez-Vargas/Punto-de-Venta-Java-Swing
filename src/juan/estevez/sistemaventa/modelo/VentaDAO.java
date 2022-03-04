package juan.estevez.sistemaventa.modelo;

import java.sql.*;
/**
 *
 * @author User
 */
public class VentaDAO {
    
    Connection cn;
    PreparedStatement pst;
    int response;
    
    public int registrarVenta(Venta venta) {
        String sql = "INSERT INTO VENTAS (CLIENTE, VENDEDOR, TOTAL) VALUES (?,?,?)";
        try {
            cn = Conexion.conectar();
            pst = cn.prepareStatement(sql);
            pst.setString(1, venta.getCliente());
            pst.setString(2, venta.getVendedor());
            pst.setDouble(3, venta.getTotal());
            pst.execute();
        } catch (SQLException e) {
        }
        return response;
    }
}
