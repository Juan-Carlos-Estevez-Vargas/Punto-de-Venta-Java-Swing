package juan.estevez.sistemaventa.vista;

import com.itextpdf.text.DocumentException;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import juan.estevez.sistemaventa.controladores.*;
import juan.estevez.sistemaventa.modelo.*;
import juan.estevez.sistemaventa.reportes.*;
import juan.estevez.sistemaventa.utils.*;
import juan.estevez.sistemaventa.utils.enums.Colors;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Sistema extends javax.swing.JFrame {

    private final ProveedorControlador proveedorControlador = ProveedorControlador.getInstance();
    private final VentaControlador ventaControlador = VentaControlador.getInstance();
    private final ProductoControlador productoControlador = ProductoControlador.getInstance();
    private final UsuarioControlador usuarioControlador = UsuarioControlador.getInstance();
    private final ClienteControlador clienteControlador = ClienteControlador.getInstance();
    private final ConfiguracionDatosEmpresaControlador configuracionDatosEmpresaControlador = ConfiguracionDatosEmpresaControlador.getInstance();

    private final transient ResourceBundle messages = ResourceBundle.getBundle("juan.estevez.sistemaventa.recursos.messages");

    private DefaultTableModel modelo = new DefaultTableModel();
    private Eventos evento = new Eventos();
    private DefaultTableModel modeloTemporal = new DefaultTableModel();
    private int idUsuarioLogueado;

    public Sistema() {
        this.iniciarAplicacion();
        this.listarDatosEmpresa();
        this.productoControlador.consultarProveedor(cbxProveedorProducto);
    }

    public Sistema(Loginn login) {
        this.iniciarAplicacion();
        this.listarDatosEmpresa();
        TabbedPane.setSelectedIndex(6);

        if (login.getRol().equals("Asistente")) {
            deshabilitarOpcionesAdministrador();
            labelVendedor.setText(login.getNombre());
        } else if (login.getRol().equals("Administrador")) {
            labelVendedor.setText(login.getNombre());
            this.txtNombreUsuarioActualizarPerfil.setText(login.getNombre());
            this.txtCorreoUsuarioActualizarPerfil.setText(login.getCorreo());
            this.txtPasswordUsuarioActualizarPerfil.setText(login.getPassword());
            this.idUsuarioLogueado = login.getId();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Productos">
    private void btnProductosActionPerformed(java.awt.event.ActionEvent evt) {
        TabbedPane.setSelectedIndex(2);
        actualizarVistaProductos();
        setActiveButton(this.btnProductos);
    }

    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        if (camposProductoVacios()) {
            Utilitarios.mostrarMensajeError(messages.getString("error.campos.vacios"));
            return;
        }

        Producto nuevoProducto = crearProductoDesdeFormulario();
        if (this.productoControlador.guardarProducto(nuevoProducto)) {
            actualizarVistaProductos();
        }
    }

    private void tableProductosMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tableProductos.rowAtPoint(evt.getPoint());

        if (fila >= 0) {
            cargarProductoDesdeFila(fila);
        }
    }

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        String idProductoStr = Utilitarios.eliminarEspaciosEnBlanco(txtIdProducto.getText());
        if (idProductoStr.isEmpty()) {
            Utilitarios.mostrarMensajeError(messages.getString("seleccione.fila"));
            return;
        }

        int idProducto = Integer.parseInt(idProductoStr);
        if (this.productoControlador.eliminarProducto(idProducto)) {
            actualizarVistaProductos();
        }
    }

    private void btnEditarProductoActionPerformed(java.awt.event.ActionEvent evt) {
        if (Utilitarios.eliminarEspaciosEnBlanco(txtIdProducto.getText()).isEmpty()) {
            Utilitarios.mostrarMensajeError(messages.getString("seleccione.fila"));
            return;
        }

        int idProducto = Integer.parseInt(Utilitarios.eliminarEspaciosEnBlanco(txtIdProducto.getText()));
        if (camposProductoVacios()) {
            Utilitarios.mostrarMensajeError(messages.getString("error.campos.vacios"));
            return;
        }

        Producto productoModificar = crearProductoDesdeFormulario();
        productoModificar.setId(idProducto);
        this.productoControlador.editarProducto(productoModificar);
    }

    private void cargarProductoDesdeFila(int fila) {
        txtIdProducto.setText(tableProductos.getValueAt(fila, 0).toString());
        txtCodigoProducto.setText(tableProductos.getValueAt(fila, 1).toString());
        txtDescripcionProducto.setText(tableProductos.getValueAt(fila, 2).toString());
        cbxProveedorProducto.setSelectedItem(tableProductos.getValueAt(fila, 3).toString());
        txtCantidadProducto.setText(tableProductos.getValueAt(fila, 4).toString());
        txtPrecioProducto.setText(tableProductos.getValueAt(fila, 5).toString());
    }

    private void agregarProductoAVenta(int cantidad) {
        String codigoProducto = Utilitarios.eliminarEspaciosEnBlanco(txtCodigoVenta.getText());
        String descripcion = Utilitarios.eliminarEspaciosEnBlanco(txtDescripcionVenta.getText());
        double precio = Double.parseDouble(Utilitarios.eliminarEspaciosEnBlanco(txtPrecioVenta.getText()));
        double totalVenta = cantidad * precio;
        int stockDisponible = Integer.parseInt(Utilitarios.eliminarEspaciosEnBlanco(txtStockDisponibleVenta.getText()));

        if (stockDisponible >= cantidad) {
            if (productoYaRegistrado(descripcion)) {
                Utilitarios.mostrarMensajeAdvertencia(messages.getString("producto.existente"));
                return;
            }

            Object[] fila = {codigoProducto, descripcion, cantidad, precio, totalVenta};
            agregarFilaATablaVenta(fila);
            totalPagar();
            limpiarVenta();
            txtCodigoVenta.requestFocus();
        } else {
            Utilitarios.mostrarMensajeAdvertencia(messages.getString("stock.no.disponible"));
        }
    }

    private boolean productoYaRegistrado(String descripcion) {
        for (int i = 0; i < tableVenta.getRowCount(); i++) {
            if (tableVenta.getValueAt(i, 1).equals(descripcion)) {
                return true;
            }
        }
        return false;
    }

    private Producto crearProductoDesdeFormulario() {
        return Producto.builder()
                .codigo(Utilitarios.eliminarEspaciosEnBlanco(txtCodigoProducto.getText()))
                .nombre(Utilitarios.eliminarEspaciosEnBlanco(txtDescripcionProducto.getText()))
                .proveedor(cbxProveedorProducto.getSelectedItem().toString())
                .stock(Integer.parseInt(Utilitarios.eliminarEspaciosEnBlanco(txtCantidadProducto.getText())))
                .precio(Double.parseDouble(Utilitarios.eliminarEspaciosEnBlanco(txtPrecioProducto.getText())))
                .build();
    }

    private void cargarProductoEnFormulario(Producto producto) {
        txtDescripcionVenta.setText(producto.getNombre());
        txtPrecioVenta.setText(String.valueOf(producto.getPrecio()));
        txtStockDisponibleVenta.setText(String.valueOf(producto.getStock()));
        txtCantidadVenta.requestFocus();
    }

    private void btnNuevoProductoActionPerformed(java.awt.event.ActionEvent evt) {
        this.limpiarProducto();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Clientes">
    private void btnClientes1ActionPerformed(java.awt.event.ActionEvent evt) {
        TabbedPane.setSelectedIndex(0);
        actualizarVistaClientes();
        setActiveButton(this.btnClientes1);
    }

    private void btnGuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {
        if (camposClienteVacios()) {
            Utilitarios.mostrarMensajeError(messages.getString("error.campos.vacios"));
            return;
        }

        Cliente nuevoCliente = crearClienteDesdeFormulario();
        if (this.clienteControlador.guardarCliente(nuevoCliente)) {
            actualizarVistaClientes();
        }
    }

    private void tableClientesMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tableClientes.rowAtPoint(evt.getPoint());

        if (fila >= 0) {
            cargarClienteDesdeFila(fila);
        }
    }

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {
        String idClienteStr = Utilitarios.eliminarEspaciosEnBlanco(txtIdCliente.getText());
        if (idClienteStr.isEmpty()) {
            Utilitarios.mostrarMensajeError(messages.getString("seleccione.fila"));
            return;
        }

        int idCliente = Integer.parseInt(idClienteStr);
        if (this.clienteControlador.eliminarCliente(idCliente)) {
            actualizarVistaClientes();
        }
    }

    private void btnEditarClienteActionPerformed(java.awt.event.ActionEvent evt) {
        if (Utilitarios.eliminarEspaciosEnBlanco(txtIdCliente.getText()).isEmpty()) {
            Utilitarios.mostrarMensajeError(messages.getString("seleccione.fila"));
            return;
        }

        int idCliente = Integer.parseInt(Utilitarios.eliminarEspaciosEnBlanco(txtIdCliente.getText()));
        if (camposClienteVacios()) {
            Utilitarios.mostrarMensajeError(messages.getString("error.campos.vacios"));
            return;
        }

        Cliente clienteModificar = crearClienteDesdeFormulario();
        clienteModificar.setId(idCliente);
        if (this.clienteControlador.editarCliente(clienteModificar)) {
            actualizarVistaClientes();
        }
    }

    private void btnNuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarCliente();
    }

    private void cargarClienteEnFormulario(Cliente cliente) {
        txtNombreClienteVenta.setText(cliente.getNombre());
        txtTelefonoClienteVenta.setText(String.valueOf(cliente.getTelefono()));
        txtDireccionClienteVenta.setText(cliente.getDireccion());
        txtRazonSocialClienteVenta.setText(cliente.getRazonSocial());
    }

    private Cliente crearClienteDesdeFormulario() {
        return Cliente.builder()
                .dni(Long.parseLong(Utilitarios.eliminarEspaciosEnBlanco(txtDniRutCliente.getText())))
                .nombre(Utilitarios.eliminarEspaciosEnBlanco(txtNombreCliente.getText()))
                .telefono(Long.parseLong(Utilitarios.eliminarEspaciosEnBlanco(txtTelefonoCliente.getText())))
                .direccion(Utilitarios.eliminarEspaciosEnBlanco(txtDireccionCliente.getText()))
                .razonSocial(Utilitarios.eliminarEspaciosEnBlanco(txtRazonSocialCliente.getText()))
                .build();
    }

    private void cargarClienteDesdeFila(int fila) {
        txtIdCliente.setText(tableClientes.getValueAt(fila, 0).toString());
        txtDniRutCliente.setText(tableClientes.getValueAt(fila, 1).toString());
        txtNombreCliente.setText(tableClientes.getValueAt(fila, 2).toString());
        txtTelefonoCliente.setText(tableClientes.getValueAt(fila, 3).toString());
        txtDireccionCliente.setText(tableClientes.getValueAt(fila, 4).toString());
        txtRazonSocialCliente.setText(tableClientes.getValueAt(fila, 5).toString());
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Usuarios">
    private void btnUsuariosActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarVistaUsuarios();
        TabbedPane.setSelectedIndex(5);
        setActiveButton(this.btnUsuarios);
    }

    private void tableUsuariosMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tableUsuarios.rowAtPoint(evt.getPoint());
        txtIdUsuario.setText(tableUsuarios.getValueAt(fila, 0).toString());
    }

    private void btnRegistrarUsuario1ActionPerformed(java.awt.event.ActionEvent evt) {
        new RegistroUsuarios().setVisible(true);
        this.dispose();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Proveedores">
    private void btnGuardarProveedorActionPerformed(java.awt.event.ActionEvent evt) {
        if (camposProveedorVacios()) {
            Utilitarios.mostrarMensajeError(messages.getString("error.campos.vacios"));
            return;
        }

        Proveedor nuevoProveedor = crearProveedorDesdeFormulario();
        if (this.proveedorControlador.guardarProveedor(nuevoProveedor)) {
            actualizarVistaProveedores();
        }
    }

    private void btnProveedorActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarVistaProveedores();
        TabbedPane.setSelectedIndex(1);
        setActiveButton(this.btnProveedor);
    }

    private void tableProveedoresMouseClicked(java.awt.event.MouseEvent evt) {
        int fila = tableProveedores.rowAtPoint(evt.getPoint());

        if (fila >= 0) {
            cargarProveedorDesdeFila(fila);
        }
    }

    private void btnEliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {
        String idProveedorStr = Utilitarios.eliminarEspaciosEnBlanco(txtIdProveedor.getText());
        if (idProveedorStr.isEmpty()) {
            Utilitarios.mostrarMensajeError(messages.getString("seleccione.fila"));
            return;
        }

        int idProveedor = Integer.parseInt(idProveedorStr);
        if (proveedorControlador.eliminarProveedor(idProveedor)) {
            actualizarVistaProveedores();
        }
    }

    private void btnEditarProveedorActionPerformed(java.awt.event.ActionEvent evt) {
        if (Utilitarios.eliminarEspaciosEnBlanco(txtIdProveedor.getText()).isEmpty()) {
            Utilitarios.mostrarMensajeError(messages.getString("seleccione.fila"));
            return;
        }

        int idProveedor = Integer.parseInt(Utilitarios.eliminarEspaciosEnBlanco(txtIdProveedor.getText()));
        if (camposProveedorVacios()) {
            Utilitarios.mostrarMensajeError(messages.getString("error.campos.vacios"));
            return;
        }

        Proveedor proveedorModificar = crearProveedorDesdeFormulario();
        proveedorModificar.setId(idProveedor);
        if (this.proveedorControlador.editarProveedor(proveedorModificar)) {
            actualizarVistaProveedores();
        }
    }

    private void btnNuevoProveedorActionPerformed(java.awt.event.ActionEvent evt) {
        limpiarProveedor();
    }

    private void cargarProveedorDesdeFila(int fila) {
        txtIdProveedor.setText(tableProveedores.getValueAt(fila, 0).toString());
        txtDniRutProveedor.setText(tableProveedores.getValueAt(fila, 1).toString());
        txtNombreProveedor.setText(tableProveedores.getValueAt(fila, 2).toString());
        txtTelefonoProveedor.setText(tableProveedores.getValueAt(fila, 3).toString());
        txtDireccionProveedor.setText(tableProveedores.getValueAt(fila, 4).toString());
        txtRazonSocialProveedor.setText(tableProveedores.getValueAt(fila, 5).toString());
    }

    private Proveedor crearProveedorDesdeFormulario() {
        return Proveedor.builder()
                .rut(Long.parseLong(Utilitarios.eliminarEspaciosEnBlanco(txtDniRutProveedor.getText())))
                .nombre(Utilitarios.eliminarEspaciosEnBlanco(txtNombreProveedor.getText()))
                .telefono(Long.parseLong(Utilitarios.eliminarEspaciosEnBlanco(txtTelefonoProveedor.getText())))
                .direccion(Utilitarios.eliminarEspaciosEnBlanco(txtDireccionProveedor.getText()))
                .razonSocial(Utilitarios.eliminarEspaciosEnBlanco(txtRazonSocialProveedor.getText()))
                .build();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Ventas">
    private void btnNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarVistaVentas();
        TabbedPane.setSelectedIndex(6);
        setActiveButton(this.btnNuevaVenta);
    }

    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarVistaVentas();
        TabbedPane.setSelectedIndex(3);
        setActiveButton(this.btnVentas);
    }

    private void agregarFilaATablaVenta(Object[] fila) {
        DefaultTableModel model = (DefaultTableModel) tableVenta.getModel();
        model.addRow(fila);
        tableVenta.setModel(model);
    }

    private void btnEliminarVentaActionPerformed(java.awt.event.ActionEvent evt) {
        this.ventaControlador.eliminarVenta(tableVenta);
        this.totalPagar();
        txtCodigoVenta.requestFocus();
    }

    private void btnGenerarVentaActionPerformed(java.awt.event.ActionEvent evt) {
        if (tableVenta.getRowCount() > 0) {
            if (Utilitarios.eliminarEspaciosEnBlanco(txtNombreClienteVenta.getText()).isEmpty()) {
                Utilitarios.mostrarMensajeAdvertencia(messages.getString("error.ingrese.cliente"));
                this.txtNombreClienteVenta.requestFocus();
                return;
            }

            try {
                this.registrarVenta();
                this.ventaControlador.registrarDetalleVenta(tableVenta);
                this.productoControlador.actualizarStock(tableVenta);
                new ReporteVentaPDF().generarReporteVenta(txtRutEmpresa.getText(), txtNombreEmpresa.getText(),
                        txtTelefonoEmpresa.getText(), txtDireccionEmpresa.getText(),
                        txtRazonSocialEmpresa.getText(), txtDniRutVenta.getText(),
                        txtNombreClienteVenta.getText(), txtTelefonoClienteVenta.getText(),
                        txtDireccionClienteVenta.getText(), tableVenta, 0.0);
                Utilitarios.limpiarTableVenta(modeloTemporal, tableVenta);
                this.limpiarClienteVenta();
            } catch (HeadlessException | SQLException | DocumentException | IOException e) {
                Utilitarios.mostrarErrorGenerico(e);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar al menos un producto");
            this.txtCodigoVenta.requestFocus();
        }
    }

    private void tableVentasMouseClicked(java.awt.event.MouseEvent evt) {
        int filaSeleccionada = tableVentas.rowAtPoint(evt.getPoint());
        txtIdVenta.setText(tableVentas.getValueAt(filaSeleccionada, 0).toString());
    }

    private void registrarVenta() throws SQLException {
        String clienteTxt = Utilitarios.eliminarEspaciosEnBlanco(txtNombreClienteVenta.getText());
        String vendedor = Utilitarios.eliminarEspaciosEnBlanco(labelVendedor.getText());
        Date fechaVenta = new Date();
        String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
        this.ventaControlador.registrarVenta(Venta.builder().cliente(clienteTxt).vendedor(vendedor).total(0.0).fecha(fechaActual).build());
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Configuracion Datos Empresa">
    private void listarDatosEmpresa() {
        ConfiguracionDatosEmpresa configuracionDatosEmpresa = this.configuracionDatosEmpresaControlador.obtenerDatosEmpresa();
        txtIdEmpresa.setText(String.valueOf(configuracionDatosEmpresa.getId()));
        txtRutEmpresa.setText(String.valueOf(configuracionDatosEmpresa.getRut()));
        txtNombreEmpresa.setText(configuracionDatosEmpresa.getNombre());
        txtTelefonoEmpresa.setText(String.valueOf(configuracionDatosEmpresa.getTelefono()));
        txtDireccionEmpresa.setText(configuracionDatosEmpresa.getDireccion());
        txtRazonSocialEmpresa.setText(configuracionDatosEmpresa.getRazonSocial());
    }

    private void btnConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {
        this.txtIdEmpresa.setVisible(false);
        this.TabbedPane.setSelectedIndex(4);
        this.listarDatosEmpresa();
        this.setActiveButton(btnConfiguracion);
    }

    private void btnActualizarDatosEmpresaActionPerformed(java.awt.event.ActionEvent evt) {
        if (camposConfiguracionDatosEmpresaVacios()) {
            Utilitarios.mostrarMensajeError(messages.getString("error.campos.vacios"));
            return;
        }

        ConfiguracionDatosEmpresa configuracionDatosEmpresa = crearConfiguracionDatosEmpresaDesdeFormulario();
        this.configuracionDatosEmpresaControlador.modificarDatosEmpresa(configuracionDatosEmpresa);
        Utilitarios.mostrarMensajeExito("Datos Actualizados");
        listarDatosEmpresa();
    }

    private ConfiguracionDatosEmpresa crearConfiguracionDatosEmpresaDesdeFormulario() {
        return ConfiguracionDatosEmpresa.builder()
                .id(Integer.parseInt(Utilitarios.eliminarEspaciosEnBlanco(txtIdEmpresa.getText())))
                .rut(Long.parseLong(Utilitarios.eliminarEspaciosEnBlanco(txtRutEmpresa.getText())))
                .nombre(Utilitarios.eliminarEspaciosEnBlanco(txtNombreEmpresa.getText()))
                .telefono(Long.parseLong(Utilitarios.eliminarEspaciosEnBlanco(txtTelefonoEmpresa.getText())))
                .direccion(Utilitarios.eliminarEspaciosEnBlanco(txtDireccionEmpresa.getText()))
                .razonSocial(Utilitarios.eliminarEspaciosEnBlanco(txtRazonSocialEmpresa.getText()))
                .build();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="KeyEvents">
    private void txtCodigoVentaKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            handleCodigoVentaEnter();
        }
    }

    private void txtCantidadVentaKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            handleCantidadVentaEnter();
        }
    }

    private void txtDniRutVentaKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            handleDniRutVentaEnter();
        }
    }

    private void handleCodigoVentaEnter() {
        String codigo = txtCodigoVenta.getText().trim();
        if (codigo.isEmpty()) {
            Utilitarios.mostrarMensajeExito(messages.getString("ingrese.codigo.producto"));
            txtCodigoVenta.requestFocus();
            return;
        }

        Producto producto = this.productoControlador.buscarProducto(codigo);
        if (producto != null && producto.getNombre() != null) {
            cargarProductoEnFormulario(producto);
        } else {
            limpiarVenta();
            txtCodigoVenta.requestFocus();
        }
    }

    private void handleCantidadVentaEnter() {
        String cantidadStr = txtCantidadVenta.getText().trim();
        if (cantidadStr.isEmpty()) {
            Utilitarios.mostrarMensajeAdvertencia(messages.getString("ingrese.cantidad"));
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            agregarProductoAVenta(cantidad);
        } catch (NumberFormatException ex) {
            Utilitarios.mostrarMensajeError(messages.getString("cantidad.invalida"));
        }
    }

    private void handleDniRutVentaEnter() {
        String dniRut = txtDniRutVenta.getText().trim();
        if (dniRut.isEmpty()) {
            return;
        }

        try {
            Cliente cliente = clienteControlador.buscarCliente(Integer.parseInt(dniRut));
            if (cliente != null && cliente.getNombre() != null) {
                cargarClienteEnFormulario(cliente);
            } else {
                limpiarClienteVenta();
                Utilitarios.mostrarMensajeError(messages.getString("cliente.no.registrado"));
            }
        } catch (NumberFormatException ex) {
            Utilitarios.mostrarMensajeError(messages.getString("error.dni.invalido"));
        }
    }

    private void txtCantidadVentaKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtPrecioVentaKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberDecimalKeyPress(evt, txtPrecioVenta);
    }

    private void txtCodigoVentaKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtStockDisponibleVentaKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtDniRutVentaKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtNombreClienteVentaKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.textKeyPress(evt);
    }

    private void txtDniRutClienteKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtNombreClienteKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.textKeyPress(evt);
    }

    private void txtDireccionClienteKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.textKeyPress(evt);
    }

    private void txtCodigoProductoKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtTelefonoClienteKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtDniRutProveedorKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtNombreProveedorKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.textKeyPress(evt);
    }

    private void txtTelefonoProveedorKeyPressed(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtCantidadProductoKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtPrecioProductoKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberDecimalKeyPress(evt, txtPrecioProducto);
    }

    private void txtRutEmpresaKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtTelefonoEmpresaKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    private void txtTelefonoProveedorKeyTyped(java.awt.event.KeyEvent evt) {
        this.evento.numberKeyPress(evt);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Actualización Vistas">
    private void actualizarVistaClientes() {
        this.clienteControlador.limpiarTablaClientes(tableClientes);
        this.clienteControlador.listarClientes(tableClientes);
        limpiarCamposCliente();
    }

    private void actualizarVistaUsuarios() {
        limpiarTabla();
        this.usuarioControlador.listarUsuarios(tableUsuarios);
    }

    private void actualizarVistaProveedores() {
        this.proveedorControlador.limpiarTablaProveedores(tableProveedores);
        this.proveedorControlador.listarProveedores(tableProveedores);
        limpiarCamposProveedor();
    }

    private void actualizarVistaProductos() {
        this.productoControlador.limpiarTablaProductos(tableProductos);
        this.productoControlador.listarProductos(tableProductos);
        limpiarCamposProducto();
    }

    private void actualizarVistaVentas() {
        limpiarTabla();
        this.ventaControlador.listarVentas(tableVenta);
        limpiarVenta();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Limpieza de Datos">
    public void limpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i -= 1;
        }
    }

    private void limpiarCliente() {
        this.txtIdCliente.setText("");
        this.txtDniRutCliente.setText("");
        this.txtNombreCliente.setText("");
        this.txtTelefonoCliente.setText("");
        this.txtDireccionCliente.setText("");
        this.txtRazonSocialCliente.setText("");
    }

    private void limpiarProveedor() {
        this.txtIdProveedor.setText("");
        this.txtDniRutProveedor.setText("");
        this.txtNombreProveedor.setText("");
        this.txtTelefonoProveedor.setText("");
        this.txtDireccionProveedor.setText("");
        this.txtRazonSocialProveedor.setText("");
    }

    private void limpiarProducto() {
        this.txtIdProducto.setText("");
        this.txtCodigoProducto.setText("");
        this.txtDescripcionProducto.setText("");
        this.txtCantidadProducto.setText("");
        this.txtPrecioProducto.setText("");
        this.cbxProveedorProducto.setSelectedItem("");
    }

    private void limpiarVenta() {
        txtCodigoVenta.setText("");
        txtDescripcionVenta.setText("");
        txtPrecioVenta.setText("");
        txtCantidadVenta.setText("");
        txtStockDisponibleVenta.setText("");
        txtIdVenta.setText("");
    }

    private void limpiarClienteVenta() {
        txtDniRutVenta.setText("");
        txtNombreClienteVenta.setText("");
        txtTelefonoClienteVenta.setText("");
        txtDireccionClienteVenta.setText("");
        txtRazonSocialClienteVenta.setText("");
    }

    private void limpiarCamposCliente() {
        txtDniRutCliente.setText("");
        txtNombreCliente.setText("");
        txtTelefonoCliente.setText("");
        txtDireccionCliente.setText("");
        txtRazonSocialCliente.setText("");
    }

    private void limpiarCamposProveedor() {
        txtDniRutProveedor.setText("");
        txtNombreProveedor.setText("");
        txtTelefonoProveedor.setText("");
        txtDireccionProveedor.setText("");
        txtRazonSocialProveedor.setText("");
    }

    private void limpiarCamposProducto() {
        txtCodigoProducto.setText("");
        txtDescripcionProducto.setText("");
        txtPrecioProducto.setText("");
        txtCantidadProducto.setText("");
        cbxProveedorProducto.setSelectedIndex(0);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Utilitarios">
    private void setActiveButton(JButton activeButton) {
        activeButton.setBackground(Colors.CADET_BLUE.getColor());
        Utilitarios.limpiarEstilosBotones(this.btnNuevaVenta, this.btnClientes1, this.btnConfiguracion, this.btnProveedor, this.btnUsuarios, this.btnVentas);
    }

    private boolean camposClienteVacios() {
        return Utilitarios.eliminarEspaciosEnBlanco(txtDireccionCliente.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtNombreCliente.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtDniRutCliente.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtTelefonoCliente.getText()).isEmpty();
    }

    private boolean camposConfiguracionDatosEmpresaVacios() {
        return Utilitarios.eliminarEspaciosEnBlanco(txtRutEmpresa.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtNombreEmpresa.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtTelefonoEmpresa.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtDireccionEmpresa.getText()).isEmpty();
    }

    private boolean camposProveedorVacios() {
        return Utilitarios.eliminarEspaciosEnBlanco(txtDniRutProveedor.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtNombreProveedor.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtTelefonoProveedor.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtDireccionProveedor.getText()).isEmpty();
    }

    private boolean camposProductoVacios() {
        return Utilitarios.eliminarEspaciosEnBlanco(txtCodigoProducto.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtDescripcionProducto.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtPrecioProducto.getText()).isEmpty()
                || cbxProveedorProducto.getSelectedItem().equals("")
                || Utilitarios.eliminarEspaciosEnBlanco(txtCantidadProducto.getText()).isEmpty();
    }

    private boolean camposPerfilVacios() {
        return Arrays.toString(txtPasswordUsuarioActualizarPerfil.getPassword()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtNombreUsuarioActualizarPerfil.getText()).isEmpty()
                || Utilitarios.eliminarEspaciosEnBlanco(txtCorreoUsuarioActualizarPerfil.getText()).isEmpty();
    }

    // </editor-fold>
    private void btnExcelProductoActionPerformed(java.awt.event.ActionEvent evt) {
        Excel.generarReporte();
    }

    private void btnPdfVentasActionPerformed(java.awt.event.ActionEvent evt) {
        String textIdVenta = Utilitarios.eliminarEspaciosEnBlanco((txtIdVenta.getText()));
        if (textIdVenta.isEmpty()) {
            Utilitarios.mostrarMensajeAdvertencia(messages.getString("error.campos.vacios"));
            return;
        }

        int idVenta = Integer.parseInt(textIdVenta);
        File file = new File("src/juan/estevez/sistemaventa/reportes/reporteVenta" + idVenta + ".pdf");

        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                Utilitarios.mostrarErrorGenerico(ex);
            }
        } else {
            Utilitarios.mostrarMensajeError(messages.getString("error.obtencion.factura.compra"));
        }
    }

    private void btnGraficaVentasActionPerformed(java.awt.event.ActionEvent evt) {
        String fechaReporte = new SimpleDateFormat("dd/MM/yyyy").format(jDateChooserVenta.getDate());
        try {
            GraficoVentas.graficar(fechaReporte);
        } catch (SQLException ex) {
            Utilitarios.mostrarErrorGenerico(ex);
        }
    }

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {
        int pregunta = JOptionPane.showConfirmDialog(null, messages.getString("mensaje.cerrar.sesion"));
        if (pregunta == 0) {
            new Login().setVisible(true);
            this.dispose();
        }
    }

    private void btnEliminarVenta1ActionPerformed(java.awt.event.ActionEvent evt) {
        String idUsuario = Utilitarios.eliminarEspaciosEnBlanco(txtIdUsuario.getText());
        if (idUsuario.isEmpty()) {
            Utilitarios.mostrarMensajeError(messages.getString("error.usuario.no.registrado"));
            return;
        }

        if (Utilitarios.confirmarEliminacion()) {
            this.usuarioControlador.eliminarUsuario(Integer.parseInt(idUsuario));
            this.limpiarTabla();
            this.usuarioControlador.listarUsuarios(tableProductos);
        }
    }

    private void btnActualizarDatosPerfilActionPerformed(java.awt.event.ActionEvent evt) {
        if (camposPerfilVacios()) {
            Utilitarios.mostrarMensajeAdvertencia(messages.getString("error.campos.vacios"));
            return;
        }

        Usuario usuario = Usuario.builder()
                .id(idUsuarioLogueado)
                .nombre(Utilitarios.eliminarEspaciosEnBlanco(txtNombreUsuarioActualizarPerfil.getText()))
                .correo(Utilitarios.eliminarEspaciosEnBlanco(txtCorreoUsuarioActualizarPerfil.getText()))
                .password(Utilitarios.eliminarEspaciosEnBlanco(Arrays.toString(txtPasswordUsuarioActualizarPerfil.getPassword())))
                .build();

        this.usuarioControlador.editarUsuario(usuario);
        Utilitarios.mostrarMensajeExito(messages.getString("datos.actualizados"));
        this.labelVendedor.setText(usuario.getNombre());
    }

    private void totalPagar() {
        Double totalPagar = 0.00;
        int numeroFilas = tableVenta.getRowCount();
        for (int i = 0; i < numeroFilas; i++) {
            double calcular = Double.parseDouble(String.valueOf(tableVenta.getModel().getValueAt(i, 4)));
            totalPagar += calcular;
        }
        labelTotalVenta.setText(String.format("%.2f", totalPagar));
    }

    private void iniciarAplicacion() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.txtIdCliente.setVisible(false);
        this.txtIdProductoVenta.setVisible(false);
        this.txtTelefonoClienteVenta.setVisible(false);
        this.txtDireccionClienteVenta.setVisible(false);
        this.txtRazonSocialClienteVenta.setVisible(false);
        this.txtIdProveedor.setVisible(false);
        this.txtIdProducto.setVisible(false);
        this.txtIdUsuario.setVisible(false);
        this.txtIdVenta.setVisible(false);
        AutoCompleteDecorator.decorate(cbxProveedorProducto);
    }

    private void deshabilitarOpcionesAdministrador() {
        this.btnUsuarios.setEnabled(false);
        this.btnEliminarCliente.setEnabled(false);
        this.btnEliminarProveedor.setEnabled(false);
        this.txtCodigoProducto.setEnabled(false);
        this.txtDescripcionProducto.setEnabled(false);
        this.cbxProveedorProducto.setEnabled(false);
        this.btnGuardarProducto.setEnabled(false);
        this.btnEliminarProducto.setEnabled(false);
        this.btnConfiguracion.setEnabled(false);
        this.txtIdUsuario.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelNavBar = new javax.swing.JPanel();
        btnUsuarios = new javax.swing.JButton();
        btnProveedor = new javax.swing.JButton();
        btnProductos = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        btnConfiguracion = new javax.swing.JButton();
        labelIconoPrincipal = new javax.swing.JLabel();
        labelVendedor = new javax.swing.JLabel();
        btnClientes1 = new javax.swing.JButton();
        btnNuevaVenta = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        txtIdEmpresa = new javax.swing.JTextField();
        btnCerrarSesion = new javax.swing.JButton();
        labelHeaderApp = new javax.swing.JLabel();
        TabbedPane = new javax.swing.JTabbedPane();
        panelClientes = new javax.swing.JPanel();
        labelDNICliente = new javax.swing.JLabel();
        labelNombreCliente = new javax.swing.JLabel();
        labelTelefonoCliente = new javax.swing.JLabel();
        LabelDireccionCliente = new javax.swing.JLabel();
        labelRazonSocialCliente = new javax.swing.JLabel();
        txtDniRutCliente = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        txtRazonSocialCliente = new javax.swing.JTextField();
        ScrollPaneTableClientes = new javax.swing.JScrollPane();
        tableClientes = new javax.swing.JTable();
        btnGuardarCliente = new javax.swing.JButton();
        btnEditarCliente = new javax.swing.JButton();
        btnEliminarCliente = new javax.swing.JButton();
        btnNuevoCliente = new javax.swing.JButton();
        txtIdCliente = new javax.swing.JTextField();
        labelTituloClientes = new javax.swing.JLabel();
        panelProveedores = new javax.swing.JPanel();
        labelDNIProveedor = new javax.swing.JLabel();
        labelNombreProveedor = new javax.swing.JLabel();
        labelTelefonoProveedor = new javax.swing.JLabel();
        labelDireccionProveedor = new javax.swing.JLabel();
        labelRazonSocialProveedor = new javax.swing.JLabel();
        btnGuardarProveedor = new javax.swing.JButton();
        btnEditarProveedor = new javax.swing.JButton();
        btnEliminarProveedor = new javax.swing.JButton();
        btnNuevoProveedor = new javax.swing.JButton();
        txtRazonSocialProveedor = new javax.swing.JTextField();
        txtDireccionProveedor = new javax.swing.JTextField();
        txtTelefonoProveedor = new javax.swing.JTextField();
        txtNombreProveedor = new javax.swing.JTextField();
        txtDniRutProveedor = new javax.swing.JTextField();
        ScrollPaneTableProveedores = new javax.swing.JScrollPane();
        tableProveedores = new javax.swing.JTable();
        txtIdProveedor = new javax.swing.JTextField();
        labelTituloProveedores = new javax.swing.JLabel();
        panelProductos = new javax.swing.JPanel();
        labelCodigoProducto = new javax.swing.JLabel();
        labelDescripcionProducto = new javax.swing.JLabel();
        labelCantidadProducto = new javax.swing.JLabel();
        labelPrecioProducto = new javax.swing.JLabel();
        labelProveedorProducto = new javax.swing.JLabel();
        btnGuardarProducto = new javax.swing.JButton();
        btnEditarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnNuevoProducto = new javax.swing.JButton();
        txtPrecioProducto = new javax.swing.JTextField();
        txtCantidadProducto = new javax.swing.JTextField();
        txtDescripcionProducto = new javax.swing.JTextField();
        txtCodigoProducto = new javax.swing.JTextField();
        ScrollPaneTableProductos = new javax.swing.JScrollPane();
        tableProductos = new javax.swing.JTable();
        cbxProveedorProducto = new javax.swing.JComboBox<>();
        btnExcelProducto = new javax.swing.JButton();
        txtIdProducto = new javax.swing.JTextField();
        labelTituloProductos = new javax.swing.JLabel();
        panelVentas = new javax.swing.JPanel();
        ScrollPaneTableVentas = new javax.swing.JScrollPane();
        tableVentas = new javax.swing.JTable();
        btnPdfVentas = new javax.swing.JButton();
        txtIdVenta = new javax.swing.JTextField();
        labelTituloPanelVentas = new javax.swing.JLabel();
        panelConfiguracion = new javax.swing.JPanel();
        panelDatosEmpresa = new javax.swing.JPanel();
        labelTitlePanelDatosEmpresa = new javax.swing.JLabel();
        labelNombreEmpresa = new javax.swing.JLabel();
        txtNombreEmpresa = new javax.swing.JTextField();
        labelTelefonoEmpresa = new javax.swing.JLabel();
        txtTelefonoEmpresa = new javax.swing.JTextField();
        labelRazonSocialEmpresa = new javax.swing.JLabel();
        txtRazonSocialEmpresa = new javax.swing.JTextField();
        txtRutEmpresa = new javax.swing.JTextField();
        txtDireccionEmpresa = new javax.swing.JTextField();
        labelRUCEmpresa = new javax.swing.JLabel();
        labelDireccionEmpresa = new javax.swing.JLabel();
        btnActualizarDatosEmpresa = new javax.swing.JButton();
        panelPerfil = new javax.swing.JPanel();
        labelIconoEmpresa = new javax.swing.JLabel();
        labelTituloPanelPerfil = new javax.swing.JLabel();
        labelNombrePerfil = new javax.swing.JLabel();
        labelEmailPerfil = new javax.swing.JLabel();
        labelPasswordPerfil = new javax.swing.JLabel();
        txtNombreUsuarioActualizarPerfil = new javax.swing.JTextField();
        txtCorreoUsuarioActualizarPerfil = new javax.swing.JTextField();
        txtPasswordUsuarioActualizarPerfil = new javax.swing.JPasswordField();
        btnActualizarDatosPerfil = new javax.swing.JButton();
        panelUsuario = new javax.swing.JPanel();
        ScrollPaneTableUsuarios = new javax.swing.JScrollPane();
        tableUsuarios = new javax.swing.JTable();
        btnEliminarVenta1 = new javax.swing.JButton();
        txtIdUsuario = new javax.swing.JTextField();
        btnRegistrarUsuario1 = new javax.swing.JButton();
        panelNuevaVenta = new javax.swing.JPanel();
        labelCodigoProductoVenta = new javax.swing.JLabel();
        labelDescripcionProductoVenta = new javax.swing.JLabel();
        labelCantidadProductoVenta = new javax.swing.JLabel();
        labelPrecioProductoVenta = new javax.swing.JLabel();
        labelStockProductoVenta = new javax.swing.JLabel();
        btnEliminarVenta = new javax.swing.JButton();
        txtCodigoVenta = new javax.swing.JTextField();
        txtDescripcionVenta = new javax.swing.JTextField();
        txtCantidadVenta = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStockDisponibleVenta = new javax.swing.JTextField();
        ScrollPaneTableVenta = new javax.swing.JScrollPane();
        tableVenta = new javax.swing.JTable();
        labelDNIClienteVenta = new javax.swing.JLabel();
        labelNombreClienteVenta = new javax.swing.JLabel();
        txtDniRutVenta = new javax.swing.JTextField();
        txtNombreClienteVenta = new javax.swing.JTextField();
        btnGenerarVenta = new javax.swing.JButton();
        labelTotalPagar = new javax.swing.JLabel();
        labelTotalVenta = new javax.swing.JLabel();
        txtTelefonoClienteVenta = new javax.swing.JTextField();
        txtDireccionClienteVenta = new javax.swing.JTextField();
        txtRazonSocialClienteVenta = new javax.swing.JTextField();
        txtIdProductoVenta = new javax.swing.JTextField();
        btnGraficaVentas = new javax.swing.JButton();
        jDateChooserVenta = new com.toedter.calendar.JDateChooser();
        labelSeleccionarFecha = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelNavBar.setBackground(new java.awt.Color(50, 69, 109));

        btnUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUsuarios
                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/lupa.png"))); // NOI18N
        btnUsuarios.setText("Usuarios");
        btnUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuariosActionPerformed(evt);
            }
        });

        btnProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnProveedor.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/proveedor.png"))); // NOI18N
        btnProveedor.setText("Proveedor");
        btnProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedorActionPerformed(evt);
            }
        });

        btnProductos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnProductos.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/producto.png"))); // NOI18N
        btnProductos.setText("Productos");
        btnProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductosActionPerformed(evt);
            }
        });

        btnVentas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnVentas.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/compras.png"))); // NOI18N
        btnVentas.setText("Ventas");
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });

        btnConfiguracion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnConfiguracion.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/config.png"))); // NOI18N
        btnConfiguracion.setText("Configuración");
        btnConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfiguracionActionPerformed(evt);
            }
        });

        labelIconoPrincipal.setBackground(new java.awt.Color(51, 51, 255));
        labelIconoPrincipal
                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/logo.png"))); // NOI18N

        labelVendedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelVendedor.setForeground(new java.awt.Color(255, 255, 255));
        labelVendedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelVendedor.setText("Juan Estevez");

        btnClientes1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClientes1.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Clientes.png"))); // NOI18N
        btnClientes1.setText("Clientes");
        btnClientes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientes1ActionPerformed(evt);
            }
        });

        btnNuevaVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNuevaVenta.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Nventa.png"))); // NOI18N
        btnNuevaVenta.setText("Nueva Venta");
        btnNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaVentaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(txtIdEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 16,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(441, Short.MAX_VALUE)));
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(txtIdEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(344, Short.MAX_VALUE)));

        javax.swing.GroupLayout panelNavBarLayout = new javax.swing.GroupLayout(panelNavBar);
        panelNavBar.setLayout(panelNavBarLayout);
        panelNavBarLayout.setHorizontalGroup(
                panelNavBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelNavBarLayout.createSequentialGroup()
                                .addGroup(panelNavBarLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnProveedor, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnProductos, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnVentas, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(panelNavBarLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(labelVendedor, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(btnNuevaVenta, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnClientes1, javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(panelNavBarLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelIconoPrincipal)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        panelNavBarLayout.setVerticalGroup(
                panelNavBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelNavBarLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(labelIconoPrincipal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelNavBarLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panelNavBarLayout.createSequentialGroup()
                                                .addComponent(labelVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 24,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnNuevaVenta)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnClientes1, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(16, Short.MAX_VALUE)));

        getContentPane().add(panelNavBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 600));

        btnCerrarSesion.setBackground(new java.awt.Color(0, 51, 255));
        btnCerrarSesion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCerrarSesion.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesion.setText("Cerrar Sesión");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
        getContentPane().add(btnCerrarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 120, 140, 37));

        labelHeaderApp.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/encabezado.png"))); // NOI18N
        getContentPane().add(labelHeaderApp, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 1070, 180));

        panelClientes.setBackground(new java.awt.Color(115, 135, 181));

        labelDNICliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDNICliente.setForeground(new java.awt.Color(255, 255, 255));
        labelDNICliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDNICliente.setText("DNI/RUT :");

        labelNombreCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelNombreCliente.setForeground(new java.awt.Color(255, 255, 255));
        labelNombreCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelNombreCliente.setText("NOMBRE :");

        labelTelefonoCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTelefonoCliente.setForeground(new java.awt.Color(255, 255, 255));
        labelTelefonoCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTelefonoCliente.setText("TELÉFONO :");

        LabelDireccionCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        LabelDireccionCliente.setForeground(new java.awt.Color(255, 255, 255));
        LabelDireccionCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelDireccionCliente.setText("DIRECCIÓN :");

        labelRazonSocialCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelRazonSocialCliente.setForeground(new java.awt.Color(255, 255, 255));
        labelRazonSocialCliente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRazonSocialCliente.setText("RAZÓN SOCIAL :");

        txtDniRutCliente.setBackground(new java.awt.Color(153, 204, 255));
        txtDniRutCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtDniRutCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDniRutCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniRutClienteKeyTyped(evt);
            }
        });

        txtNombreCliente.setBackground(new java.awt.Color(153, 204, 255));
        txtNombreCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtNombreCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreClienteKeyTyped(evt);
            }
        });

        txtTelefonoCliente.setBackground(new java.awt.Color(153, 204, 255));
        txtTelefonoCliente.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        txtTelefonoCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTelefonoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoClienteKeyTyped(evt);
            }
        });

        txtDireccionCliente.setBackground(new java.awt.Color(153, 204, 255));
        txtDireccionCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtDireccionCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDireccionCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionClienteKeyTyped(evt);
            }
        });

        txtRazonSocialCliente.setBackground(new java.awt.Color(153, 204, 255));
        txtRazonSocialCliente.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtRazonSocialCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        tableClientes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableClientes.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "ID", "DNI/RUT", "NOMBRE", "TELÉFONO", "DIRECCIÓN", "RAZÓN SOCIAL"
                }));
        tableClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableClientesMouseClicked(evt);
            }
        });
        ScrollPaneTableClientes.setViewportView(tableClientes);
        if (tableClientes.getColumnModel().getColumnCount() > 0) {
            tableClientes.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableClientes.getColumnModel().getColumn(1).setPreferredWidth(70);
            tableClientes.getColumnModel().getColumn(2).setPreferredWidth(150);
            tableClientes.getColumnModel().getColumn(3).setPreferredWidth(80);
            tableClientes.getColumnModel().getColumn(4).setPreferredWidth(80);
        }

        btnGuardarCliente.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/GuardarTodo.png"))); // NOI18N
        btnGuardarCliente.setBorder(null);
        btnGuardarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnGuardarClienteActionPerformed(evt);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnEditarCliente.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Actualizar (2).png"))); // NOI18N
        btnEditarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarClienteActionPerformed(evt);
            }
        });

        btnEliminarCliente.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/eliminar.png"))); // NOI18N
        btnEliminarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
            }
        });

        btnNuevoCliente
                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/nuevo.png"))); // NOI18N
        btnNuevoCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoClienteActionPerformed(evt);
            }
        });

        labelTituloClientes.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        labelTituloClientes.setForeground(new java.awt.Color(255, 255, 255));
        labelTituloClientes.setText("Gestión Clientes");

        javax.swing.GroupLayout panelClientesLayout = new javax.swing.GroupLayout(panelClientes);
        panelClientes.setLayout(panelClientesLayout);
        panelClientesLayout.setHorizontalGroup(
                panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelClientesLayout.createSequentialGroup()
                                .addGroup(panelClientesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelClientesLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(panelClientesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(panelClientesLayout.createSequentialGroup()
                                                                .addComponent(txtIdCliente,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 9,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(labelTituloClientes,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE))
                                                        .addGroup(panelClientesLayout.createSequentialGroup()
                                                                .addGroup(panelClientesLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        false)
                                                                        .addGroup(panelClientesLayout
                                                                                .createSequentialGroup()
                                                                                .addGap(2, 2, 2)
                                                                                .addComponent(labelNombreCliente,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE))
                                                                        .addComponent(labelDNICliente,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                128,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(panelClientesLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtDniRutCliente)
                                                                        .addComponent(txtNombreCliente)))
                                                        .addGroup(panelClientesLayout.createSequentialGroup()
                                                                .addGroup(panelClientesLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        false)
                                                                        .addComponent(LabelDireccionCliente,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(labelTelefonoCliente,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(labelRazonSocialCliente,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                128, Short.MAX_VALUE))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(panelClientesLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtRazonSocialCliente)
                                                                        .addComponent(txtTelefonoCliente)
                                                                        .addComponent(txtDireccionCliente))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelClientesLayout
                                                .createSequentialGroup()
                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnGuardarCliente, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnEditarCliente, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnEliminarCliente,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnNuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(67, 67, 67)))
                                .addComponent(ScrollPaneTableClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 682,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)));
        panelClientesLayout.setVerticalGroup(
                panelClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelClientesLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(panelClientesLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelClientesLayout.createSequentialGroup()
                                                .addGroup(panelClientesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtIdCliente,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelTituloClientes))
                                                .addGap(18, 18, 18)
                                                .addGroup(panelClientesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtDniRutCliente,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelDNICliente))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelClientesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtNombreCliente,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelNombreCliente))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelClientesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelTelefonoCliente)
                                                        .addComponent(txtTelefonoCliente,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelClientesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(LabelDireccionCliente)
                                                        .addComponent(txtDireccionCliente,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelClientesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelRazonSocialCliente)
                                                        .addComponent(txtRazonSocialCliente,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(31, 31, 31)
                                                .addGroup(panelClientesLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnEditarCliente,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnGuardarCliente,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnEliminarCliente,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnNuevoCliente,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(ScrollPaneTableClientes, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                370, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(25, Short.MAX_VALUE)));

        TabbedPane.addTab("tab2", panelClientes);

        panelProveedores.setBackground(new java.awt.Color(115, 135, 181));

        labelDNIProveedor.setBackground(new java.awt.Color(204, 204, 204));
        labelDNIProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDNIProveedor.setForeground(new java.awt.Color(255, 255, 255));
        labelDNIProveedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDNIProveedor.setText("DNI/RUT :");

        labelNombreProveedor.setBackground(new java.awt.Color(204, 204, 204));
        labelNombreProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelNombreProveedor.setForeground(new java.awt.Color(255, 255, 255));
        labelNombreProveedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelNombreProveedor.setText("NOMBRE :");

        labelTelefonoProveedor.setBackground(new java.awt.Color(204, 204, 204));
        labelTelefonoProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTelefonoProveedor.setForeground(new java.awt.Color(255, 255, 255));
        labelTelefonoProveedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTelefonoProveedor.setText("TELÉFONO :");

        labelDireccionProveedor.setBackground(new java.awt.Color(204, 204, 204));
        labelDireccionProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDireccionProveedor.setForeground(new java.awt.Color(255, 255, 255));
        labelDireccionProveedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDireccionProveedor.setText("DIRECCIÓN :");

        labelRazonSocialProveedor.setBackground(new java.awt.Color(204, 204, 204));
        labelRazonSocialProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelRazonSocialProveedor.setForeground(new java.awt.Color(255, 255, 255));
        labelRazonSocialProveedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRazonSocialProveedor.setText("RAZÓN SOCIAL :");

        btnGuardarProveedor.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/GuardarTodo.png"))); // NOI18N
        btnGuardarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProveedorActionPerformed(evt);
            }
        });

        btnEditarProveedor.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Actualizar (2).png"))); // NOI18N
        btnEditarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProveedorActionPerformed(evt);
            }
        });

        btnEliminarProveedor.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/eliminar.png"))); // NOI18N
        btnEliminarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProveedorActionPerformed(evt);
            }
        });

        btnNuevoProveedor
                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/nuevo.png"))); // NOI18N
        btnNuevoProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProveedorActionPerformed(evt);
            }
        });

        txtRazonSocialProveedor.setBackground(new java.awt.Color(153, 204, 255));
        txtRazonSocialProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtRazonSocialProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtDireccionProveedor.setBackground(new java.awt.Color(153, 204, 255));
        txtDireccionProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtDireccionProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtTelefonoProveedor.setBackground(new java.awt.Color(153, 204, 255));
        txtTelefonoProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTelefonoProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTelefonoProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoProveedorKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoProveedorKeyTyped(evt);
            }
        });

        txtNombreProveedor.setBackground(new java.awt.Color(153, 204, 255));
        txtNombreProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtNombreProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreProveedorKeyTyped(evt);
            }
        });

        txtDniRutProveedor.setBackground(new java.awt.Color(153, 204, 255));
        txtDniRutProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtDniRutProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDniRutProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniRutProveedorKeyTyped(evt);
            }
        });

        tableProveedores.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableProveedores.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "ID", "DNI/RUT", "NOMBRE", "TELÉFONO", "DIRECCIÓN", "RAZÓN SOCIAL"
                }));
        tableProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProveedoresMouseClicked(evt);
            }
        });
        ScrollPaneTableProveedores.setViewportView(tableProveedores);
        if (tableProveedores.getColumnModel().getColumnCount() > 0) {
            tableProveedores.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableProveedores.getColumnModel().getColumn(1).setPreferredWidth(70);
            tableProveedores.getColumnModel().getColumn(2).setPreferredWidth(150);
            tableProveedores.getColumnModel().getColumn(3).setPreferredWidth(80);
            tableProveedores.getColumnModel().getColumn(4).setPreferredWidth(80);
            tableProveedores.getColumnModel().getColumn(5).setPreferredWidth(80);
        }

        labelTituloProveedores.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        labelTituloProveedores.setForeground(new java.awt.Color(255, 255, 255));
        labelTituloProveedores.setText("Gestión Proveedores");

        javax.swing.GroupLayout panelProveedoresLayout = new javax.swing.GroupLayout(panelProveedores);
        panelProveedores.setLayout(panelProveedoresLayout);
        panelProveedoresLayout.setHorizontalGroup(
                panelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelProveedoresLayout.createSequentialGroup()
                                .addGroup(panelProveedoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelProveedoresLayout
                                                .createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(panelProveedoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                panelProveedoresLayout.createSequentialGroup()
                                                                        .addComponent(labelDNIProveedor,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addComponent(txtDniRutProveedor,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                214,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(panelProveedoresLayout.createSequentialGroup()
                                                                .addGroup(panelProveedoresLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(labelTelefonoProveedor,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(labelNombreProveedor,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(panelProveedoresLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(txtNombreProveedor)
                                                                        .addComponent(txtTelefonoProveedor,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                214,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(panelProveedoresLayout.createSequentialGroup()
                                                                .addComponent(labelRazonSocialProveedor,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE, 143,
                                                                        Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtRazonSocialProveedor,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 215,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(panelProveedoresLayout.createSequentialGroup()
                                                                .addComponent(labelDireccionProveedor,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtDireccionProveedor,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 214,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(panelProveedoresLayout.createSequentialGroup()
                                                                .addGap(0, 7, Short.MAX_VALUE)
                                                                .addComponent(labelTituloProveedores,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 357,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(panelProveedoresLayout.createSequentialGroup()
                                                .addGroup(panelProveedoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(panelProveedoresLayout.createSequentialGroup()
                                                                .addGap(166, 166, 166)
                                                                .addComponent(txtIdProveedor,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 16,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(panelProveedoresLayout.createSequentialGroup()
                                                                .addGap(59, 59, 59)
                                                                .addComponent(btnGuardarProveedor,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 55,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnEditarProveedor,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnEliminarProveedor,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnNuevoProveedor,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 48,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ScrollPaneTableProveedores, javax.swing.GroupLayout.DEFAULT_SIZE, 667,
                                        Short.MAX_VALUE)
                                .addGap(21, 21, 21)));
        panelProveedoresLayout.setVerticalGroup(
                panelProveedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelProveedoresLayout.createSequentialGroup()
                                .addGroup(panelProveedoresLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelProveedoresLayout
                                                .createSequentialGroup()
                                                .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(31, 31, 31)
                                                .addComponent(labelTituloProveedores)
                                                .addGap(26, 26, 26)
                                                .addGroup(panelProveedoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtDniRutProveedor,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelDNIProveedor))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelProveedoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtNombreProveedor,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelNombreProveedor))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelProveedoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtTelefonoProveedor,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelTelefonoProveedor))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelProveedoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtDireccionProveedor,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelDireccionProveedor))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelProveedoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtRazonSocialProveedor,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelRazonSocialProveedor))
                                                .addGap(31, 31, 31)
                                                .addGroup(panelProveedoresLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnGuardarProveedor,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnEditarProveedor,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnEliminarProveedor,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnNuevoProveedor,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(panelProveedoresLayout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addComponent(ScrollPaneTableProveedores,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 378,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(21, Short.MAX_VALUE)));

        TabbedPane.addTab("tab3", panelProveedores);

        panelProductos.setBackground(new java.awt.Color(115, 135, 181));

        labelCodigoProducto.setBackground(new java.awt.Color(204, 204, 204));
        labelCodigoProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCodigoProducto.setForeground(new java.awt.Color(255, 255, 255));
        labelCodigoProducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCodigoProducto.setText("CÓDIGO :");

        labelDescripcionProducto.setBackground(new java.awt.Color(204, 204, 204));
        labelDescripcionProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDescripcionProducto.setForeground(new java.awt.Color(255, 255, 255));
        labelDescripcionProducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDescripcionProducto.setText("DESCRIPCIÓN :");

        labelCantidadProducto.setBackground(new java.awt.Color(204, 204, 204));
        labelCantidadProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCantidadProducto.setForeground(new java.awt.Color(255, 255, 255));
        labelCantidadProducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCantidadProducto.setText("CANTIDAD :");

        labelPrecioProducto.setBackground(new java.awt.Color(204, 204, 204));
        labelPrecioProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelPrecioProducto.setForeground(new java.awt.Color(255, 255, 255));
        labelPrecioProducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPrecioProducto.setText("PRECIO :");

        labelProveedorProducto.setBackground(new java.awt.Color(204, 204, 204));
        labelProveedorProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelProveedorProducto.setForeground(new java.awt.Color(255, 255, 255));
        labelProveedorProducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelProveedorProducto.setText("PROVEEDOR :");

        btnGuardarProducto.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/GuardarTodo.png"))); // NOI18N
        btnGuardarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductoActionPerformed(evt);
            }
        });

        btnEditarProducto.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Actualizar (2).png"))); // NOI18N
        btnEditarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProductoActionPerformed(evt);
            }
        });

        btnEliminarProducto.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/eliminar.png"))); // NOI18N
        btnEliminarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        btnNuevoProducto
                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/nuevo.png"))); // NOI18N
        btnNuevoProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProductoActionPerformed(evt);
            }
        });

        txtPrecioProducto.setBackground(new java.awt.Color(153, 204, 255));
        txtPrecioProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtPrecioProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrecioProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioProductoKeyTyped(evt);
            }
        });

        txtCantidadProducto.setBackground(new java.awt.Color(153, 204, 255));
        txtCantidadProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtCantidadProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyTyped(evt);
            }
        });

        txtDescripcionProducto.setBackground(new java.awt.Color(153, 204, 255));
        txtDescripcionProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtDescripcionProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtCodigoProducto.setBackground(new java.awt.Color(153, 204, 255));
        txtCodigoProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtCodigoProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyTyped(evt);
            }
        });

        tableProductos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableProductos.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "ID", "CÓDIGO", "DESCRIPCIÓN", "PROVEEDOR", "CANTIDAD", "PRECIO"
                }));
        tableProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProductosMouseClicked(evt);
            }
        });
        ScrollPaneTableProductos.setViewportView(tableProductos);
        if (tableProductos.getColumnModel().getColumnCount() > 0) {
            tableProductos.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableProductos.getColumnModel().getColumn(1).setPreferredWidth(50);
            tableProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tableProductos.getColumnModel().getColumn(3).setPreferredWidth(80);
            tableProductos.getColumnModel().getColumn(4).setPreferredWidth(30);
            tableProductos.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        cbxProveedorProducto.setBackground(new java.awt.Color(153, 204, 255));
        cbxProveedorProducto.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cbxProveedorProducto.setForeground(new java.awt.Color(204, 204, 255));

        btnExcelProducto
                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/excel.png"))); // NOI18N
        btnExcelProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelProductoActionPerformed(evt);
            }
        });

        labelTituloProductos.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        labelTituloProductos.setForeground(new java.awt.Color(255, 255, 255));
        labelTituloProductos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTituloProductos.setText("Gestión Productos");

        javax.swing.GroupLayout panelProductosLayout = new javax.swing.GroupLayout(panelProductos);
        panelProductos.setLayout(panelProductosLayout);
        panelProductosLayout.setHorizontalGroup(
                panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelProductosLayout.createSequentialGroup()
                                .addGroup(panelProductosLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelProductosLayout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addGroup(panelProductosLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(panelProductosLayout.createSequentialGroup()
                                                                .addGroup(panelProductosLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(labelDescripcionProducto,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                112, Short.MAX_VALUE)
                                                                        .addComponent(labelCodigoProducto,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(labelCantidadProducto,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(labelPrecioProducto,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(labelProveedorProducto,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(panelProductosLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtPrecioProducto)
                                                                        .addComponent(cbxProveedorProducto, 0,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(txtCantidadProducto)
                                                                        .addComponent(txtDescripcionProducto)
                                                                        .addComponent(txtCodigoProducto)))
                                                        .addGroup(panelProductosLayout.createSequentialGroup()
                                                                .addComponent(btnGuardarProducto,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 55,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnEditarProducto,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnEliminarProducto,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 50,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnNuevoProducto,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 48,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnExcelProducto,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 48,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))))
                                        .addGroup(panelProductosLayout.createSequentialGroup()
                                                .addGap(261, 261, 261)
                                                .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 22,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                panelProductosLayout.createSequentialGroup()
                                                        .addContainerGap(16, Short.MAX_VALUE)
                                                        .addComponent(labelTituloProductos,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 356,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(ScrollPaneTableProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 663,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)));
        panelProductosLayout.setVerticalGroup(
                panelProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelProductosLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(panelProductosLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ScrollPaneTableProductos, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                388, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panelProductosLayout.createSequentialGroup()
                                                .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(labelTituloProductos)
                                                .addGap(33, 33, 33)
                                                .addGroup(panelProductosLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(labelCodigoProducto)
                                                        .addComponent(txtCodigoProducto,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelProductosLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelDescripcionProducto)
                                                        .addComponent(txtDescripcionProducto,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelProductosLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(labelCantidadProducto)
                                                        .addComponent(txtCantidadProducto,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelProductosLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelPrecioProducto)
                                                        .addComponent(txtPrecioProducto,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelProductosLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(labelProveedorProducto)
                                                        .addComponent(cbxProveedorProducto,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(31, 31, 31)
                                                .addGroup(panelProductosLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnGuardarProducto,
                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnEditarProducto,
                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnEliminarProducto,
                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnNuevoProducto,
                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnExcelProducto,
                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 49,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(21, Short.MAX_VALUE)));

        TabbedPane.addTab("tab4", panelProductos);

        panelVentas.setBackground(new java.awt.Color(115, 135, 181));

        tableVentas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableVentas.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "ID", "CLIENTE", "VENDEDOR", "TOTAL"
                }));
        tableVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableVentasMouseClicked(evt);
            }
        });
        ScrollPaneTableVentas.setViewportView(tableVentas);
        if (tableVentas.getColumnModel().getColumnCount() > 0) {
            tableVentas.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableVentas.getColumnModel().getColumn(1).setPreferredWidth(200);
            tableVentas.getColumnModel().getColumn(2).setPreferredWidth(180);
            tableVentas.getColumnModel().getColumn(3).setPreferredWidth(80);
        }

        btnPdfVentas
                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/pdf.png"))); // NOI18N
        btnPdfVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfVentasActionPerformed(evt);
            }
        });

        labelTituloPanelVentas.setBackground(new java.awt.Color(204, 204, 204));
        labelTituloPanelVentas.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        labelTituloPanelVentas.setForeground(new java.awt.Color(255, 255, 255));
        labelTituloPanelVentas.setText("Registro de las ventas realizadas");

        javax.swing.GroupLayout panelVentasLayout = new javax.swing.GroupLayout(panelVentas);
        panelVentas.setLayout(panelVentasLayout);
        panelVentasLayout.setHorizontalGroup(
                panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelVentasLayout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(panelVentasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(ScrollPaneTableVentas, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                1030, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panelVentasLayout.createSequentialGroup()
                                                .addComponent(btnPdfVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 55,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(51, 51, 51)
                                                .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(85, 85, 85)
                                                .addComponent(labelTituloPanelVentas)))
                                .addContainerGap(17, Short.MAX_VALUE)));
        panelVentasLayout.setVerticalGroup(
                panelVentasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelVentasLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(panelVentasLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelVentasLayout.createSequentialGroup()
                                                .addGroup(panelVentasLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(txtIdVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelTituloPanelVentas))
                                                .addGap(25, 25, 25))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelVentasLayout
                                                .createSequentialGroup()
                                                .addComponent(btnPdfVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 45,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                .addComponent(ScrollPaneTableVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 325,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(13, Short.MAX_VALUE)));

        TabbedPane.addTab("tab5", panelVentas);

        panelConfiguracion.setBackground(new java.awt.Color(115, 135, 181));

        panelDatosEmpresa.setBackground(new java.awt.Color(50, 69, 109));

        labelTitlePanelDatosEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        labelTitlePanelDatosEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        labelTitlePanelDatosEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitlePanelDatosEmpresa.setText("DATOS DE LA EMPRESA");

        labelNombreEmpresa.setBackground(new java.awt.Color(204, 204, 204));
        labelNombreEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelNombreEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        labelNombreEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelNombreEmpresa.setText("NOMBRE");

        txtNombreEmpresa.setBackground(new java.awt.Color(153, 204, 255));
        txtNombreEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtNombreEmpresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        labelTelefonoEmpresa.setBackground(new java.awt.Color(204, 204, 204));
        labelTelefonoEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTelefonoEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        labelTelefonoEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTelefonoEmpresa.setText("TELÉFONO");

        txtTelefonoEmpresa.setBackground(new java.awt.Color(153, 204, 255));
        txtTelefonoEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTelefonoEmpresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTelefonoEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoEmpresaKeyTyped(evt);
            }
        });

        labelRazonSocialEmpresa.setBackground(new java.awt.Color(204, 204, 204));
        labelRazonSocialEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelRazonSocialEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        labelRazonSocialEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRazonSocialEmpresa.setText("RAZÓN SOCIAL");

        txtRazonSocialEmpresa.setBackground(new java.awt.Color(153, 204, 255));
        txtRazonSocialEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtRazonSocialEmpresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtRutEmpresa.setBackground(new java.awt.Color(153, 204, 255));
        txtRutEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtRutEmpresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRutEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRutEmpresaKeyTyped(evt);
            }
        });

        txtDireccionEmpresa.setBackground(new java.awt.Color(153, 204, 255));
        txtDireccionEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtDireccionEmpresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        labelRUCEmpresa.setBackground(new java.awt.Color(204, 204, 204));
        labelRUCEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelRUCEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        labelRUCEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRUCEmpresa.setText("RUC");

        labelDireccionEmpresa.setBackground(new java.awt.Color(204, 204, 204));
        labelDireccionEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDireccionEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        labelDireccionEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDireccionEmpresa.setText("DIRECCIÓN");

        btnActualizarDatosEmpresa.setBackground(new java.awt.Color(0, 204, 51));
        btnActualizarDatosEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnActualizarDatosEmpresa.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarDatosEmpresa.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Actualizar (2).png"))); // NOI18N
        btnActualizarDatosEmpresa.setText("Actualizar");
        btnActualizarDatosEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDatosEmpresaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDatosEmpresaLayout = new javax.swing.GroupLayout(panelDatosEmpresa);
        panelDatosEmpresa.setLayout(panelDatosEmpresaLayout);
        panelDatosEmpresaLayout.setHorizontalGroup(
                panelDatosEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelDatosEmpresaLayout.createSequentialGroup()
                                .addGroup(panelDatosEmpresaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelDatosEmpresaLayout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addGroup(panelDatosEmpresaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                false)
                                                        .addGroup(panelDatosEmpresaLayout.createSequentialGroup()
                                                                .addGroup(panelDatosEmpresaLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(labelNombreEmpresa,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                104,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(labelTelefonoEmpresa,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                103,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(panelDatosEmpresaLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(txtTelefonoEmpresa)
                                                                        .addComponent(txtNombreEmpresa,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                288,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(panelDatosEmpresaLayout.createSequentialGroup()
                                                                .addGroup(panelDatosEmpresaLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                        .addComponent(labelRazonSocialEmpresa,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(labelDireccionEmpresa,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                        .addComponent(labelRUCEmpresa,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                103,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(panelDatosEmpresaLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtDireccionEmpresa)
                                                                        .addComponent(txtRazonSocialEmpresa)
                                                                        .addComponent(txtRutEmpresa,
                                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                289,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                        .addComponent(btnActualizarDatosEmpresa,
                                                                javax.swing.GroupLayout.Alignment.TRAILING)))
                                        .addGroup(panelDatosEmpresaLayout.createSequentialGroup()
                                                .addGap(37, 37, 37)
                                                .addComponent(labelTitlePanelDatosEmpresa,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 373,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(22, Short.MAX_VALUE)));
        panelDatosEmpresaLayout.setVerticalGroup(
                panelDatosEmpresaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelDatosEmpresaLayout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(labelTitlePanelDatosEmpresa)
                                .addGap(27, 27, 27)
                                .addGroup(panelDatosEmpresaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(labelNombreEmpresa)
                                        .addComponent(txtNombreEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelDatosEmpresaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelTelefonoEmpresa)
                                        .addComponent(txtTelefonoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelDatosEmpresaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelRazonSocialEmpresa)
                                        .addComponent(txtRazonSocialEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelDatosEmpresaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelDireccionEmpresa)
                                        .addComponent(txtDireccionEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelDatosEmpresaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labelRUCEmpresa)
                                        .addComponent(txtRutEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(12, 12, 12)
                                .addComponent(btnActualizarDatosEmpresa)
                                .addContainerGap(20, Short.MAX_VALUE)));

        panelPerfil.setBackground(new java.awt.Color(50, 69, 109));

        labelIconoEmpresa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelIconoEmpresa.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/proveedor.png"))); // NOI18N

        labelTituloPanelPerfil.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        labelTituloPanelPerfil.setForeground(new java.awt.Color(255, 255, 255));
        labelTituloPanelPerfil.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTituloPanelPerfil.setText("Actualizar perfil");

        labelNombrePerfil.setBackground(new java.awt.Color(204, 204, 204));
        labelNombrePerfil.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelNombrePerfil.setForeground(new java.awt.Color(255, 255, 255));
        labelNombrePerfil.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelNombrePerfil.setText("NOMBRE");

        labelEmailPerfil.setBackground(new java.awt.Color(204, 204, 204));
        labelEmailPerfil.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelEmailPerfil.setForeground(new java.awt.Color(255, 255, 255));
        labelEmailPerfil.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelEmailPerfil.setText("EMAIL");

        labelPasswordPerfil.setBackground(new java.awt.Color(204, 204, 204));
        labelPasswordPerfil.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelPasswordPerfil.setForeground(new java.awt.Color(255, 255, 255));
        labelPasswordPerfil.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPasswordPerfil.setText("PASSWORD");

        txtNombreUsuarioActualizarPerfil.setBackground(new java.awt.Color(153, 204, 255));
        txtNombreUsuarioActualizarPerfil.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtNombreUsuarioActualizarPerfil.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtCorreoUsuarioActualizarPerfil.setBackground(new java.awt.Color(153, 204, 255));
        txtCorreoUsuarioActualizarPerfil.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtCorreoUsuarioActualizarPerfil.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtPasswordUsuarioActualizarPerfil.setBackground(new java.awt.Color(153, 204, 255));
        txtPasswordUsuarioActualizarPerfil.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtPasswordUsuarioActualizarPerfil.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnActualizarDatosPerfil.setBackground(new java.awt.Color(0, 204, 51));
        btnActualizarDatosPerfil.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnActualizarDatosPerfil.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarDatosPerfil.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Actualizar (2).png"))); // NOI18N
        btnActualizarDatosPerfil.setText("Actualizar");
        btnActualizarDatosPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDatosPerfilActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPerfilLayout = new javax.swing.GroupLayout(panelPerfil);
        panelPerfil.setLayout(panelPerfilLayout);
        panelPerfilLayout.setHorizontalGroup(
                panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelPerfilLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(panelPerfilLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnActualizarDatosPerfil)
                                        .addGroup(panelPerfilLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(panelPerfilLayout.createSequentialGroup()
                                                        .addGap(11, 11, 11)
                                                        .addGroup(panelPerfilLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(labelNombrePerfil,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 104,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(labelEmailPerfil,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 103,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(panelPerfilLayout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING,
                                                                        false)
                                                                .addComponent(txtCorreoUsuarioActualizarPerfil)
                                                                .addComponent(txtNombreUsuarioActualizarPerfil,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 288,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(panelPerfilLayout.createSequentialGroup()
                                                        .addComponent(labelIconoEmpresa,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 111,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(labelTituloPanelPerfil,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 290,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(panelPerfilLayout.createSequentialGroup()
                                                        .addComponent(labelPasswordPerfil,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 126,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(txtPasswordUsuarioActualizarPerfil))))
                                .addContainerGap(23, Short.MAX_VALUE)));
        panelPerfilLayout.setVerticalGroup(
                panelPerfilLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelPerfilLayout.createSequentialGroup()
                                .addGroup(panelPerfilLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelPerfilLayout.createSequentialGroup()
                                                .addGap(48, 48, 48)
                                                .addComponent(labelTituloPanelPerfil,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelPerfilLayout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addComponent(labelIconoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        106, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelPerfilLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelNombrePerfil)
                                        .addComponent(txtNombreUsuarioActualizarPerfil,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(panelPerfilLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelEmailPerfil)
                                        .addComponent(txtCorreoUsuarioActualizarPerfil,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13)
                                .addGroup(panelPerfilLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelPasswordPerfil)
                                        .addComponent(txtPasswordUsuarioActualizarPerfil,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(btnActualizarDatosPerfil)
                                .addContainerGap(24, Short.MAX_VALUE)));

        javax.swing.GroupLayout panelConfiguracionLayout = new javax.swing.GroupLayout(panelConfiguracion);
        panelConfiguracion.setLayout(panelConfiguracionLayout);
        panelConfiguracionLayout.setHorizontalGroup(
                panelConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelConfiguracionLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(panelDatosEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79,
                                        Short.MAX_VALUE)
                                .addComponent(panelPerfil, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(66, 66, 66)));
        panelConfiguracionLayout.setVerticalGroup(
                panelConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelConfiguracionLayout.createSequentialGroup()
                                .addGroup(panelConfiguracionLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelConfiguracionLayout.createSequentialGroup()
                                                .addGap(41, 41, 41)
                                                .addComponent(panelPerfil, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelConfiguracionLayout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addComponent(panelDatosEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(50, Short.MAX_VALUE)));

        TabbedPane.addTab("tab6", panelConfiguracion);

        panelUsuario.setBackground(new java.awt.Color(115, 135, 181));

        tableUsuarios.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableUsuarios.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "ID", "CORREO", "NOMBRE", "ROL"
                }));
        tableUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableUsuariosMouseClicked(evt);
            }
        });
        ScrollPaneTableUsuarios.setViewportView(tableUsuarios);
        if (tableUsuarios.getColumnModel().getColumnCount() > 0) {
            tableUsuarios.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableUsuarios.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableUsuarios.getColumnModel().getColumn(2).setPreferredWidth(100);
            tableUsuarios.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        btnEliminarVenta1.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/eliminar.png"))); // NOI18N
        btnEliminarVenta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVenta1ActionPerformed(evt);
            }
        });

        txtIdUsuario.setEditable(false);

        btnRegistrarUsuario1.setBackground(new java.awt.Color(0, 51, 255));
        btnRegistrarUsuario1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnRegistrarUsuario1.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarUsuario1.setText("Registrar Usuario");
        btnRegistrarUsuario1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarUsuario1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelUsuarioLayout = new javax.swing.GroupLayout(panelUsuario);
        panelUsuario.setLayout(panelUsuarioLayout);
        panelUsuarioLayout.setHorizontalGroup(
                panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelUsuarioLayout.createSequentialGroup()
                                .addGroup(panelUsuarioLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelUsuarioLayout.createSequentialGroup()
                                                .addGap(74, 74, 74)
                                                .addComponent(txtIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 5,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(315, 315, 315)
                                                .addComponent(btnRegistrarUsuario1,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 192,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnEliminarVenta1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(panelUsuarioLayout.createSequentialGroup()
                                                .addGap(19, 19, 19)
                                                .addComponent(ScrollPaneTableUsuarios,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 1034,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(17, Short.MAX_VALUE)));
        panelUsuarioLayout.setVerticalGroup(
                panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelUsuarioLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(ScrollPaneTableUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, 327,
                                        Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addGroup(panelUsuarioLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnEliminarVenta1, javax.swing.GroupLayout.Alignment.TRAILING,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnRegistrarUsuario1, javax.swing.GroupLayout.Alignment.TRAILING,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 37,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtIdUsuario, javax.swing.GroupLayout.Alignment.TRAILING,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)));

        TabbedPane.addTab("tab7", panelUsuario);

        panelNuevaVenta.setBackground(new java.awt.Color(115, 135, 181));
        panelNuevaVenta.setPreferredSize(new java.awt.Dimension(1081, 427));

        labelCodigoProductoVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCodigoProductoVenta.setForeground(new java.awt.Color(255, 255, 255));
        labelCodigoProductoVenta.setText("CÓDIGO");

        labelDescripcionProductoVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDescripcionProductoVenta.setForeground(new java.awt.Color(255, 255, 255));
        labelDescripcionProductoVenta.setText("DESCRIPCIÓN");

        labelCantidadProductoVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCantidadProductoVenta.setForeground(new java.awt.Color(255, 255, 255));
        labelCantidadProductoVenta.setText("CANTIDAD");

        labelPrecioProductoVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelPrecioProductoVenta.setForeground(new java.awt.Color(255, 255, 255));
        labelPrecioProductoVenta.setText("PRECIO");

        labelStockProductoVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelStockProductoVenta.setForeground(new java.awt.Color(255, 255, 255));
        labelStockProductoVenta.setText("STOCK");

        btnEliminarVenta.setIcon(
                new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/eliminar.png"))); // NOI18N
        btnEliminarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVentaActionPerformed(evt);
            }
        });

        txtCodigoVenta.setBackground(new java.awt.Color(153, 204, 255));
        txtCodigoVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtCodigoVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodigoVentaKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoVentaKeyTyped(evt);
            }
        });

        txtDescripcionVenta.setBackground(new java.awt.Color(153, 204, 255));
        txtDescripcionVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        txtCantidadVenta.setBackground(new java.awt.Color(153, 204, 255));
        txtCantidadVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtCantidadVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyTyped(evt);
            }
        });

        txtPrecioVenta.setEditable(false);
        txtPrecioVenta.setBackground(new java.awt.Color(153, 204, 255));
        txtPrecioVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtPrecioVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioVentaKeyTyped(evt);
            }
        });

        txtStockDisponibleVenta.setBackground(new java.awt.Color(153, 204, 255));
        txtStockDisponibleVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtStockDisponibleVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockDisponibleVentaKeyTyped(evt);
            }
        });

        tableVenta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableVenta.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "Código", "DESCRIPCIÓN", "CANTIDAD", "PRECIO", "TOTAL"
                }));
        ScrollPaneTableVenta.setViewportView(tableVenta);
        if (tableVenta.getColumnModel().getColumnCount() > 0) {
            tableVenta.getColumnModel().getColumn(0).setPreferredWidth(30);
            tableVenta.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableVenta.getColumnModel().getColumn(2).setPreferredWidth(30);
            tableVenta.getColumnModel().getColumn(3).setPreferredWidth(30);
            tableVenta.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        labelDNIClienteVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDNIClienteVenta.setForeground(new java.awt.Color(255, 255, 255));
        labelDNIClienteVenta.setText("DNI/RUT");

        labelNombreClienteVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelNombreClienteVenta.setForeground(new java.awt.Color(255, 255, 255));
        labelNombreClienteVenta.setText("NOMBRE");

        txtDniRutVenta.setBackground(new java.awt.Color(153, 204, 255));
        txtDniRutVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDniRutVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDniRutVentaKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniRutVentaKeyTyped(evt);
            }
        });

        txtNombreClienteVenta.setEditable(false);
        txtNombreClienteVenta.setBackground(new java.awt.Color(153, 204, 255));
        txtNombreClienteVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtNombreClienteVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreClienteVentaKeyTyped(evt);
            }
        });

        btnGenerarVenta
                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/print.png"))); // NOI18N
        btnGenerarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarVentaActionPerformed(evt);
            }
        });

        labelTotalPagar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTotalPagar.setForeground(new java.awt.Color(255, 255, 255));
        labelTotalPagar
                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/money.png"))); // NOI18N
        labelTotalPagar.setText("TOTAL A PAGAR");

        labelTotalVenta.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        labelTotalVenta.setForeground(new java.awt.Color(153, 204, 255));
        labelTotalVenta.setText("---");

        btnGraficaVentas
                .setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/torta.png"))); // NOI18N
        btnGraficaVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraficaVentasActionPerformed(evt);
            }
        });

        jDateChooserVenta.setBackground(new java.awt.Color(153, 204, 255));
        jDateChooserVenta.setForeground(new java.awt.Color(204, 204, 255));

        labelSeleccionarFecha.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelSeleccionarFecha.setForeground(new java.awt.Color(204, 204, 204));
        labelSeleccionarFecha.setText("Seleccionar");

        javax.swing.GroupLayout panelNuevaVentaLayout = new javax.swing.GroupLayout(panelNuevaVenta);
        panelNuevaVenta.setLayout(panelNuevaVentaLayout);
        panelNuevaVentaLayout.setHorizontalGroup(
                panelNuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(panelNuevaVentaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                .addComponent(ScrollPaneTableVenta,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 1036,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelCodigoProductoVenta)
                                                        .addComponent(txtCodigoVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 86,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                                .addComponent(labelDescripcionProductoVenta)
                                                                .addGap(35, 35, 35)
                                                                .addComponent(txtIdProductoVenta,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(txtDescripcionVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 471,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9,
                                                        Short.MAX_VALUE)
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelCantidadProductoVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 89,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtCantidadVenta,
                                                                javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 95,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelPrecioProductoVenta)
                                                        .addComponent(txtPrecioVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 147,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelStockProductoVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 64,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtStockDisponibleVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 100,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(70, 70, 70))
                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelDNIClienteVenta)
                                                        .addComponent(txtDniRutVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE, 141,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelNombreClienteVenta)
                                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                                .addComponent(txtNombreClienteVenta,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 221,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtTelefonoClienteVenta,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 5,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtDireccionClienteVenta,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 7,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(txtRazonSocialClienteVenta,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 3,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnGraficaVentas, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                                .addComponent(jDateChooserVenta,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 168,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE))
                                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                                .addComponent(labelSeleccionarFecha,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 153,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE)))
                                                .addComponent(labelTotalPagar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(labelTotalVenta, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap()))));
        panelNuevaVentaLayout.setVerticalGroup(
                panelNuevaVentaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(panelNuevaVentaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                                .addGroup(panelNuevaVentaLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(labelCodigoProductoVenta)
                                                                        .addComponent(labelDescripcionProductoVenta)
                                                                        .addComponent(txtIdProductoVenta,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(8, 8, 8))
                                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                                .addGroup(panelNuevaVentaLayout.createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(labelCantidadProductoVenta)
                                                                        .addComponent(labelPrecioProductoVenta)
                                                                        .addComponent(labelStockProductoVenta))
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtCodigoVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtDescripcionVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtCantidadVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtPrecioVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtStockDisponibleVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 43,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(ScrollPaneTableVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 223,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25,
                                        Short.MAX_VALUE)
                                .addGroup(panelNuevaVentaLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(panelNuevaVentaLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(labelTotalPagar)
                                                        .addComponent(labelTotalVenta))
                                                .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                        .addComponent(labelSeleccionarFecha)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jDateChooserVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(btnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 48,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(panelNuevaVentaLayout.createSequentialGroup()
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(labelDNIClienteVenta)
                                                        .addComponent(labelNombreClienteVenta))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(panelNuevaVentaLayout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtDniRutVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtNombreClienteVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtTelefonoClienteVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtDireccionClienteVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtRazonSocialClienteVenta,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(btnGraficaVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 48,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(25, Short.MAX_VALUE)));

        TabbedPane.addTab("tab1", panelNuevaVenta);

        getContentPane().add(TabbedPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 150, 1070, 450));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelDireccionCliente;
    private javax.swing.JScrollPane ScrollPaneTableClientes;
    private javax.swing.JScrollPane ScrollPaneTableProductos;
    private javax.swing.JScrollPane ScrollPaneTableProveedores;
    private javax.swing.JScrollPane ScrollPaneTableUsuarios;
    private javax.swing.JScrollPane ScrollPaneTableVenta;
    private javax.swing.JScrollPane ScrollPaneTableVentas;
    private javax.swing.JTabbedPane TabbedPane;
    private javax.swing.JButton btnActualizarDatosEmpresa;
    private javax.swing.JButton btnActualizarDatosPerfil;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnClientes1;
    private javax.swing.JButton btnConfiguracion;
    private javax.swing.JButton btnEditarCliente;
    private javax.swing.JButton btnEditarProducto;
    private javax.swing.JButton btnEditarProveedor;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnEliminarProveedor;
    private javax.swing.JButton btnEliminarVenta;
    private javax.swing.JButton btnEliminarVenta1;
    private javax.swing.JButton btnExcelProducto;
    private javax.swing.JButton btnGenerarVenta;
    private javax.swing.JButton btnGraficaVentas;
    private javax.swing.JButton btnGuardarCliente;
    private javax.swing.JButton btnGuardarProducto;
    private javax.swing.JButton btnGuardarProveedor;
    private javax.swing.JButton btnNuevaVenta;
    private javax.swing.JButton btnNuevoCliente;
    private javax.swing.JButton btnNuevoProducto;
    private javax.swing.JButton btnNuevoProveedor;
    private javax.swing.JButton btnPdfVentas;
    private javax.swing.JButton btnProductos;
    private javax.swing.JButton btnProveedor;
    private javax.swing.JButton btnRegistrarUsuario1;
    private javax.swing.JButton btnUsuarios;
    private javax.swing.JButton btnVentas;
    private javax.swing.JComboBox<String> cbxProveedorProducto;
    private com.toedter.calendar.JDateChooser jDateChooserVenta;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel labelCantidadProducto;
    private javax.swing.JLabel labelCantidadProductoVenta;
    private javax.swing.JLabel labelCodigoProducto;
    private javax.swing.JLabel labelCodigoProductoVenta;
    private javax.swing.JLabel labelDNICliente;
    private javax.swing.JLabel labelDNIClienteVenta;
    private javax.swing.JLabel labelDNIProveedor;
    private javax.swing.JLabel labelDescripcionProducto;
    private javax.swing.JLabel labelDescripcionProductoVenta;
    private javax.swing.JLabel labelDireccionEmpresa;
    private javax.swing.JLabel labelDireccionProveedor;
    private javax.swing.JLabel labelEmailPerfil;
    private javax.swing.JLabel labelHeaderApp;
    private javax.swing.JLabel labelIconoEmpresa;
    private javax.swing.JLabel labelIconoPrincipal;
    private javax.swing.JLabel labelNombreCliente;
    private javax.swing.JLabel labelNombreClienteVenta;
    private javax.swing.JLabel labelNombreEmpresa;
    private javax.swing.JLabel labelNombrePerfil;
    private javax.swing.JLabel labelNombreProveedor;
    private javax.swing.JLabel labelPasswordPerfil;
    private javax.swing.JLabel labelPrecioProducto;
    private javax.swing.JLabel labelPrecioProductoVenta;
    private javax.swing.JLabel labelProveedorProducto;
    private javax.swing.JLabel labelRUCEmpresa;
    private javax.swing.JLabel labelRazonSocialCliente;
    private javax.swing.JLabel labelRazonSocialEmpresa;
    private javax.swing.JLabel labelRazonSocialProveedor;
    private javax.swing.JLabel labelSeleccionarFecha;
    private javax.swing.JLabel labelStockProductoVenta;
    private javax.swing.JLabel labelTelefonoCliente;
    private javax.swing.JLabel labelTelefonoEmpresa;
    private javax.swing.JLabel labelTelefonoProveedor;
    private javax.swing.JLabel labelTitlePanelDatosEmpresa;
    private javax.swing.JLabel labelTituloClientes;
    private javax.swing.JLabel labelTituloPanelPerfil;
    private javax.swing.JLabel labelTituloPanelVentas;
    private javax.swing.JLabel labelTituloProductos;
    private javax.swing.JLabel labelTituloProveedores;
    private javax.swing.JLabel labelTotalPagar;
    private javax.swing.JLabel labelTotalVenta;
    private javax.swing.JLabel labelVendedor;
    private javax.swing.JPanel panelClientes;
    private javax.swing.JPanel panelConfiguracion;
    private javax.swing.JPanel panelDatosEmpresa;
    private javax.swing.JPanel panelNavBar;
    private javax.swing.JPanel panelNuevaVenta;
    private javax.swing.JPanel panelPerfil;
    private javax.swing.JPanel panelProductos;
    private javax.swing.JPanel panelProveedores;
    private javax.swing.JPanel panelUsuario;
    private javax.swing.JPanel panelVentas;
    private javax.swing.JTable tableClientes;
    private javax.swing.JTable tableProductos;
    private javax.swing.JTable tableProveedores;
    private javax.swing.JTable tableUsuarios;
    private javax.swing.JTable tableVenta;
    private javax.swing.JTable tableVentas;
    private javax.swing.JTextField txtCantidadProducto;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtCodigoVenta;
    private javax.swing.JTextField txtCorreoUsuarioActualizarPerfil;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtDescripcionVenta;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtDireccionClienteVenta;
    private javax.swing.JTextField txtDireccionEmpresa;
    private javax.swing.JTextField txtDireccionProveedor;
    private javax.swing.JTextField txtDniRutCliente;
    private javax.swing.JTextField txtDniRutProveedor;
    private javax.swing.JTextField txtDniRutVenta;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdEmpresa;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIdProductoVenta;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtIdVenta;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreClienteVenta;
    private javax.swing.JTextField txtNombreEmpresa;
    private javax.swing.JTextField txtNombreProveedor;
    private javax.swing.JTextField txtNombreUsuarioActualizarPerfil;
    private javax.swing.JPasswordField txtPasswordUsuarioActualizarPerfil;
    private javax.swing.JTextField txtPrecioProducto;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtRazonSocialCliente;
    private javax.swing.JTextField txtRazonSocialClienteVenta;
    private javax.swing.JTextField txtRazonSocialEmpresa;
    private javax.swing.JTextField txtRazonSocialProveedor;
    private javax.swing.JTextField txtRutEmpresa;
    private javax.swing.JTextField txtStockDisponibleVenta;
    private javax.swing.JTextField txtTelefonoCliente;
    private javax.swing.JTextField txtTelefonoClienteVenta;
    private javax.swing.JTextField txtTelefonoEmpresa;
    private javax.swing.JTextField txtTelefonoProveedor;
    // End of variables declaration//GEN-END:variables
}
