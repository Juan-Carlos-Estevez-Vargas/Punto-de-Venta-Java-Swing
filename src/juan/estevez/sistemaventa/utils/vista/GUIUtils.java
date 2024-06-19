package juan.estevez.sistemaventa.utils.vista;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import juan.estevez.sistemaventa.modelo.Cliente;
import juan.estevez.sistemaventa.modelo.Producto;
import juan.estevez.sistemaventa.modelo.Proveedor;
import juan.estevez.sistemaventa.modelo.Usuario;
import juan.estevez.sistemaventa.modelo.Venta;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class GUIUtils {

    public static DefaultTableModel listarClientes(List<Cliente> clientes, DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);

        clientes.forEach(cliente -> {
            Object[] objeto = {
                cliente.getId(),
                cliente.getDni(),
                cliente.getNombre(),
                cliente.getTelefono(),
                cliente.getDireccion(),
                cliente.getRazonSocial()
            };
            modeloTabla.addRow(objeto);
        });

        return modeloTabla;
    }

    public static DefaultTableModel listarUsuarios(List<Usuario> usuarios, DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);

        usuarios.forEach(usuario -> {
            Object[] objeto = {
                usuario.getId(),
                usuario.getCorreo(),
                usuario.getNombre(),
                usuario.getRol()
            };
            modeloTabla.addRow(objeto);
        });

        return modeloTabla;
    }

    public static DefaultTableModel listarProveedores(List<Proveedor> proveedores, DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);

        proveedores.forEach(proveedor -> {
            Object[] objeto = {
                proveedor.getId(),
                proveedor.getRut(),
                proveedor.getNombre(),
                proveedor.getTelefono(),
                proveedor.getDireccion(),
                proveedor.getRazonSocial()
            };
            modeloTabla.addRow(objeto);
        });

        return modeloTabla;
    }

    public static DefaultTableModel listarProductos(List<Producto> productos, DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);

        productos.forEach(producto -> {
            Object[] objeto = {
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getProveedor(),
                producto.getStock(),
                producto.getPrecio()
            };
            modeloTabla.addRow(objeto);
        });

        return modeloTabla;
    }

    public static DefaultTableModel listarVentas(List<Venta> ventas, DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);
        
        ventas.forEach(venta -> {
            Object[] objeto = {
                venta.getId(),
                venta.getCliente(),
                venta.getVendedor(),
                venta.getTotal()
            };
            modeloTabla.addRow(objeto);
        });
        
        return modeloTabla;
    }

}
