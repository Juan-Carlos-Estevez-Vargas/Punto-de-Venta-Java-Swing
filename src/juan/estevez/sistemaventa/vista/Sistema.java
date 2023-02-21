package juan.estevez.sistemaventa.vista;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.*;
import juan.estevez.sistemaventa.daos.ClienteDAO;
import juan.estevez.sistemaventa.daos.ConfiguracionDatosEmpresaDAO;
import juan.estevez.sistemaventa.daos.ProductoDAO;
import juan.estevez.sistemaventa.daos.ProveedorDAO;
import juan.estevez.sistemaventa.daos.UsuarioDAO;
import juan.estevez.sistemaventa.daos.VentaDAO;
import juan.estevez.sistemaventa.modelo.Cliente;
import juan.estevez.sistemaventa.modelo.ConfiguracionDatosEmpresa;
import juan.estevez.sistemaventa.modelo.Detalle;
import juan.estevez.sistemaventa.modelo.Eventos;
import juan.estevez.sistemaventa.modelo.Loginn;
import juan.estevez.sistemaventa.modelo.Producto;
import juan.estevez.sistemaventa.modelo.Proveedor;
import juan.estevez.sistemaventa.modelo.Usuario;
import juan.estevez.sistemaventa.modelo.Venta;
import juan.estevez.sistemaventa.reportes.Excel;
import juan.estevez.sistemaventa.reportes.GraficoVentas;
import juan.estevez.sistemaventa.reportes.ReporteVentaPDF;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public final class Sistema extends javax.swing.JFrame {

    Date fechaVenta = new Date();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    Cliente cliente = new Cliente();
    ClienteDAO clienteDAO = new ClienteDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    Proveedor proveedor = new Proveedor();
    ProveedorDAO proveedorDAO = new ProveedorDAO();
    Producto producto = new Producto();
    ProductoDAO productoDAO = new ProductoDAO();
    Usuario usuario = new Usuario();
    UsuarioDAO usuarioDAO = new UsuarioDAO();
    Venta venta = new Venta();
    VentaDAO ventaDao = new VentaDAO();
    Detalle detalleVenta = new Detalle();
    Eventos evento = new Eventos();
    ConfiguracionDatosEmpresa configuracionDatosEmpresa = new ConfiguracionDatosEmpresa();
    ConfiguracionDatosEmpresaDAO configuracionDatosEmpresaDAO = new ConfiguracionDatosEmpresaDAO();
    DefaultTableModel modeloTemporal = new DefaultTableModel();
    ReporteVentaPDF reporteVentaPDF = new ReporteVentaPDF();
    int item;
    double totalPagar = 0.00;

    /**
     * Creates new form Sistema
     */
    public Sistema() {
        this.iniciarAplicacion();
        this.listarDatosEmpresa();
    }

    /**
     * Creates new form Sistema
     *
     * @param login a validar sus permisos.
     */
    public Sistema(Loginn login) {
        this.iniciarAplicacion();
        this.listarDatosEmpresa();
        jTabbedPane1.setSelectedIndex(6);

        if (login.getRol().equals("Asistente")) {
            btnUsuarios.setEnabled(false);
            this.btnEliminarCliente.setEnabled(false);
            this.btnEliminarProveedor.setEnabled(false);
            this.txtCodigoProducto.setEnabled(false);
            this.txtDescripcionProducto.setEnabled(false);
            this.cbxProveedorProducto.setEnabled(false);
            this.btnGuardarProducto.setEnabled(false);
            this.btnEliminarProducto.setEnabled(false);
            this.btnConfiguracion.setEnabled(false);
            this.txtIdUsuario.setEnabled(false);
            labelVendedor.setText(login.getNombre());
        } else {
            labelVendedor.setText(login.getNombre());
        }
    }

    /**
     * Lista los clientes de la base de datos en la tabla clientes.
     */
    public void listarClientes() {
        List<Cliente> listarClientes = clienteDAO.listarClientes();
        modelo = (DefaultTableModel) tableClientes.getModel();
        Object[] objeto = new Object[6];

        for (int i = 0; i < listarClientes.size(); i++) {
            objeto[0] = listarClientes.get(i).getId();
            objeto[1] = listarClientes.get(i).getDni();
            objeto[2] = listarClientes.get(i).getNombre();
            objeto[3] = listarClientes.get(i).getTelefono();
            objeto[4] = listarClientes.get(i).getDireccion();
            objeto[5] = listarClientes.get(i).getRazonSocial();

            modelo.addRow(objeto);
        }
        tableClientes.setModel(modelo);
    }

    /**
     * Lista los clientes de la base de datos en la tabla clientes.
     */
    public void listarUsuarios() {
        List<Usuario> listarUsuarios = usuarioDAO.listarUsuarios();
        modelo = (DefaultTableModel) tableUsuarios.getModel();
        Object[] objeto = new Object[4];

        for (int i = 0; i < listarUsuarios.size(); i++) {
            objeto[0] = listarUsuarios.get(i).getId();
            objeto[1] = listarUsuarios.get(i).getCorreo();
            objeto[2] = listarUsuarios.get(i).getNombre();
            objeto[3] = listarUsuarios.get(i).getRol();

            modelo.addRow(objeto);
        }
        tableUsuarios.setModel(modelo);
    }

    /**
     * Lista los proveedores de la base de datos en la tabla proveedor.
     */
    public void listarProveedor() {
        List<Proveedor> listarProveedores = proveedorDAO.listarProveedores();
        modelo = (DefaultTableModel) tableProveedores.getModel();
        Object[] objeto = new Object[6];

        for (int i = 0; i < listarProveedores.size(); i++) {
            objeto[0] = listarProveedores.get(i).getId();
            objeto[1] = listarProveedores.get(i).getRut();
            objeto[2] = listarProveedores.get(i).getNombre();
            objeto[3] = listarProveedores.get(i).getTelefono();
            objeto[4] = listarProveedores.get(i).getDireccion();
            objeto[5] = listarProveedores.get(i).getRazonSocial();

            modelo.addRow(objeto);
        }
        tableProveedores.setModel(modelo);
    }

    /**
     * Lista los productos de la base de datos en la tabla producto.
     */
    public void listarProductos() {
        List<Producto> listarProductos = productoDAO.listarProductos();
        modelo = (DefaultTableModel) tableProductos.getModel();
        Object[] objeto = new Object[6];

        for (int i = 0; i < listarProductos.size(); i++) {
            objeto[0] = listarProductos.get(i).getId();
            objeto[1] = listarProductos.get(i).getCodigo();
            objeto[2] = listarProductos.get(i).getNombre();
            objeto[3] = listarProductos.get(i).getProveedor();
            objeto[4] = listarProductos.get(i).getStock();
            objeto[5] = listarProductos.get(i).getPrecio();

            modelo.addRow(objeto);
        }
        tableProductos.setModel(modelo);
    }

    /**
     * Lista los datos de la empresa en el formulario de configuración.
     */
    public void listarDatosEmpresa() {
        configuracionDatosEmpresa = configuracionDatosEmpresaDAO.buscarDatosEmpresa();
        txtIdEmpresa.setText(String.valueOf(configuracionDatosEmpresa.getId()));
        txtRutEmpresa.setText(String.valueOf(configuracionDatosEmpresa.getRut()));
        txtNombreEmpresa.setText(configuracionDatosEmpresa.getNombre());
        txtTelefonoEmpresa.setText(String.valueOf(configuracionDatosEmpresa.getTelefono()));
        txtDireccionEmpresa.setText(configuracionDatosEmpresa.getDireccion());
        txtRazonSocialEmpresa.setText(configuracionDatosEmpresa.getRazonSocial());
    }

    /**
     * Lista las ventas existentes en el sistema.
     */
    public void listarVentas() {
        List<Venta> listaVentas = ventaDao.listarVentas();
        modelo = (DefaultTableModel) tableVentas.getModel();
        Object[] objeto = new Object[4];

        for (int i = 0; i < listaVentas.size(); i++) {
            objeto[0] = listaVentas.get(i).getId();
            objeto[1] = listaVentas.get(i).getCliente();
            objeto[2] = listaVentas.get(i).getVendedor();
            objeto[3] = listaVentas.get(i).getTotal();

            modelo.addRow(objeto);
        }
        tableVentas.setModel(modelo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnUsuarios = new javax.swing.JButton();
        btnProveedor = new javax.swing.JButton();
        btnProductos = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        btnConfiguracion = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        labelVendedor = new javax.swing.JLabel();
        btnClientes1 = new javax.swing.JButton();
        btnNuevaVenta = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        txtIdEmpresa = new javax.swing.JTextField();
        btnCerrarSesión = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtDniRutCliente = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        txtRazonSocialCliente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableClientes = new javax.swing.JTable();
        btnGuardarCliente = new javax.swing.JButton();
        btnEditarCliente = new javax.swing.JButton();
        btnEliminarCliente = new javax.swing.JButton();
        btnNuevoCliente = new javax.swing.JButton();
        txtIdCliente = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        btnGuardarProveedor = new javax.swing.JButton();
        btnEditarProveedor = new javax.swing.JButton();
        btnEliminarProveedor = new javax.swing.JButton();
        btnNuevoProveedor = new javax.swing.JButton();
        txtRazonSocialProveedor = new javax.swing.JTextField();
        txtDireccionProveedor = new javax.swing.JTextField();
        txtTelefonoProveedor = new javax.swing.JTextField();
        txtNombreProveedor = new javax.swing.JTextField();
        txtDniRutProveedor = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableProveedores = new javax.swing.JTable();
        txtIdProveedor = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        btnGuardarProducto = new javax.swing.JButton();
        btnEditarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnNuevoProducto = new javax.swing.JButton();
        txtPrecioProducto = new javax.swing.JTextField();
        txtCantidadProducto = new javax.swing.JTextField();
        txtDescripcionProducto = new javax.swing.JTextField();
        txtCodigoProducto = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableProductos = new javax.swing.JTable();
        cbxProveedorProducto = new javax.swing.JComboBox<>();
        btnExcelProducto = new javax.swing.JButton();
        txtIdProducto = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableVentas = new javax.swing.JTable();
        btnPdfVentas = new javax.swing.JButton();
        txtIdVenta = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txtNombreEmpresa = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        txtTelefonoEmpresa = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtRazonSocialEmpresa = new javax.swing.JTextField();
        txtRutEmpresa = new javax.swing.JTextField();
        txtDireccionEmpresa = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        btnActualizarDatosEmpresa = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        txtNombreUsuarioActualizarPerfil = new javax.swing.JTextField();
        txtCorreoUsuarioActualizarPerfil = new javax.swing.JTextField();
        btnActualizarDatosEmpresa1 = new javax.swing.JButton();
        txtPasswordUsuarioActualizarPerfil = new javax.swing.JPasswordField();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableUsuarios = new javax.swing.JTable();
        btnEliminarVenta1 = new javax.swing.JButton();
        txtIdUsuario = new javax.swing.JTextField();
        btnRegistrarUsuario1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnEliminarVenta = new javax.swing.JButton();
        txtCodigoVenta = new javax.swing.JTextField();
        txtDescripcionVenta = new javax.swing.JTextField();
        txtCantidadVenta = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStockDisponibleVenta = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableVenta = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtDniRutVenta = new javax.swing.JTextField();
        txtNombreClienteVenta = new javax.swing.JTextField();
        btnGenerarVenta = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        labelTotalVenta = new javax.swing.JLabel();
        txtTelefonoClienteVenta = new javax.swing.JTextField();
        txtDireccionClienteVenta = new javax.swing.JTextField();
        txtRazonSocialClienteVenta = new javax.swing.JTextField();
        txtIdProductoVenta = new javax.swing.JTextField();
        btnGraficaVentas = new javax.swing.JButton();
        jDateChooserVenta = new com.toedter.calendar.JDateChooser();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(50, 69, 109));

        btnUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/lupa.png"))); // NOI18N
        btnUsuarios.setText("Usuarios");
        btnUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsuariosActionPerformed(evt);
            }
        });

        btnProveedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/proveedor.png"))); // NOI18N
        btnProveedor.setText("Proveedor");
        btnProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedorActionPerformed(evt);
            }
        });

        btnProductos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/producto.png"))); // NOI18N
        btnProductos.setText("Productos");
        btnProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductosActionPerformed(evt);
            }
        });

        btnVentas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/compras.png"))); // NOI18N
        btnVentas.setText("Ventas");
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });

        btnConfiguracion.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnConfiguracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/config.png"))); // NOI18N
        btnConfiguracion.setText("Configuración");
        btnConfiguracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfiguracionActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(51, 51, 255));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/logo.png"))); // NOI18N

        labelVendedor.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelVendedor.setForeground(new java.awt.Color(255, 255, 255));
        labelVendedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelVendedor.setText("Juan Estevez");

        btnClientes1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClientes1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Clientes.png"))); // NOI18N
        btnClientes1.setText("Clientes");
        btnClientes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientes1ActionPerformed(evt);
            }
        });

        btnNuevaVenta.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Nventa.png"))); // NOI18N
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
                .addComponent(txtIdEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(441, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(txtIdEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(344, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnNuevaVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClientes1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(labelVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnNuevaVenta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClientes1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 170, 600));

        btnCerrarSesión.setBackground(new java.awt.Color(0, 51, 255));
        btnCerrarSesión.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCerrarSesión.setForeground(new java.awt.Color(255, 255, 255));
        btnCerrarSesión.setText("Cerrar Sesión");
        btnCerrarSesión.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesiónActionPerformed(evt);
            }
        });
        getContentPane().add(btnCerrarSesión, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 120, 140, 37));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/encabezado.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 0, 1070, 150));

        jPanel3.setBackground(new java.awt.Color(115, 135, 181));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("DNI/RUT :");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("NOMBRE :");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("TELÉFONO :");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("DIRECCIÓN :");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("RAZÓN SOCIAL :");

        txtDniRutCliente.setBackground(new java.awt.Color(153, 204, 255));
        txtDniRutCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDniRutCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDniRutCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniRutClienteKeyTyped(evt);
            }
        });

        txtNombreCliente.setBackground(new java.awt.Color(153, 204, 255));
        txtNombreCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtNombreCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreClienteKeyTyped(evt);
            }
        });

        txtTelefonoCliente.setBackground(new java.awt.Color(153, 204, 255));
        txtTelefonoCliente.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        txtTelefonoCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTelefonoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoClienteActionPerformed(evt);
            }
        });
        txtTelefonoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoClienteKeyTyped(evt);
            }
        });

        txtDireccionCliente.setBackground(new java.awt.Color(153, 204, 255));
        txtDireccionCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDireccionCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDireccionCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDireccionClienteKeyTyped(evt);
            }
        });

        txtRazonSocialCliente.setBackground(new java.awt.Color(153, 204, 255));
        txtRazonSocialCliente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtRazonSocialCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRazonSocialCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRazonSocialClienteActionPerformed(evt);
            }
        });

        tableClientes.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "DNI/RUT", "NOMBRE", "TELÉFONO", "DIRECCIÓN", "RAZÓN SOCIAL"
            }
        ));
        tableClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableClientesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableClientes);
        if (tableClientes.getColumnModel().getColumnCount() > 0) {
            tableClientes.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableClientes.getColumnModel().getColumn(1).setPreferredWidth(70);
            tableClientes.getColumnModel().getColumn(2).setPreferredWidth(150);
            tableClientes.getColumnModel().getColumn(3).setPreferredWidth(80);
            tableClientes.getColumnModel().getColumn(4).setPreferredWidth(80);
        }

        btnGuardarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/GuardarTodo.png"))); // NOI18N
        btnGuardarCliente.setBorder(null);
        btnGuardarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarClienteActionPerformed(evt);
            }
        });

        btnEditarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Actualizar (2).png"))); // NOI18N
        btnEditarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarClienteActionPerformed(evt);
            }
        });

        btnEliminarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/eliminar.png"))); // NOI18N
        btnEliminarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
            }
        });

        btnNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/nuevo.png"))); // NOI18N
        btnNuevoCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoClienteActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 48)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("Gestión Clientes");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDniRutCliente)
                                    .addComponent(txtNombreCliente)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtRazonSocialCliente)
                                    .addComponent(txtTelefonoCliente)
                                    .addComponent(txtDireccionCliente))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnGuardarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnNuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(66, 66, 66)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 682, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDniRutCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(txtRazonSocialCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEditarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGuardarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab2", jPanel3);

        jPanel4.setBackground(new java.awt.Color(115, 135, 181));

        jLabel17.setBackground(new java.awt.Color(204, 204, 204));
        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("DNI/RUT :");

        jLabel18.setBackground(new java.awt.Color(204, 204, 204));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("NOMBRE :");

        jLabel19.setBackground(new java.awt.Color(204, 204, 204));
        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("TELÉFONO :");

        jLabel20.setBackground(new java.awt.Color(204, 204, 204));
        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("DIRECCIÓN :");

        jLabel21.setBackground(new java.awt.Color(204, 204, 204));
        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("RAZÓN SOCIAL :");

        btnGuardarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/GuardarTodo.png"))); // NOI18N
        btnGuardarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProveedorActionPerformed(evt);
            }
        });

        btnEditarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Actualizar (2).png"))); // NOI18N
        btnEditarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProveedorActionPerformed(evt);
            }
        });

        btnEliminarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/eliminar.png"))); // NOI18N
        btnEliminarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProveedorActionPerformed(evt);
            }
        });

        btnNuevoProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/nuevo.png"))); // NOI18N
        btnNuevoProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProveedorActionPerformed(evt);
            }
        });

        txtRazonSocialProveedor.setBackground(new java.awt.Color(153, 204, 255));
        txtRazonSocialProveedor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtRazonSocialProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtDireccionProveedor.setBackground(new java.awt.Color(153, 204, 255));
        txtDireccionProveedor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDireccionProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtTelefonoProveedor.setBackground(new java.awt.Color(153, 204, 255));
        txtTelefonoProveedor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTelefonoProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTelefonoProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoProveedorActionPerformed(evt);
            }
        });
        txtTelefonoProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoProveedorKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoProveedorKeyTyped(evt);
            }
        });

        txtNombreProveedor.setBackground(new java.awt.Color(153, 204, 255));
        txtNombreProveedor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtNombreProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNombreProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreProveedorActionPerformed(evt);
            }
        });
        txtNombreProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreProveedorKeyTyped(evt);
            }
        });

        txtDniRutProveedor.setBackground(new java.awt.Color(153, 204, 255));
        txtDniRutProveedor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDniRutProveedor.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDniRutProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtDniRutProveedorKeyTyped(evt);
            }
        });

        tableProveedores.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableProveedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "DNI/RUT", "NOMBRE", "TELÉFONO", "DIRECCIÓN", "RAZÓN SOCIAL"
            }
        ));
        tableProveedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProveedoresMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableProveedores);
        if (tableProveedores.getColumnModel().getColumnCount() > 0) {
            tableProveedores.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableProveedores.getColumnModel().getColumn(1).setPreferredWidth(70);
            tableProveedores.getColumnModel().getColumn(2).setPreferredWidth(150);
            tableProveedores.getColumnModel().getColumn(3).setPreferredWidth(80);
            tableProveedores.getColumnModel().getColumn(4).setPreferredWidth(80);
            tableProveedores.getColumnModel().getColumn(5).setPreferredWidth(80);
        }

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setText("Gestión Proveedores");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGap(53, 53, 53)
                                    .addComponent(btnGuardarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnEditarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(btnNuevoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(47, 47, 47))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtDniRutProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtNombreProveedor)
                                        .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRazonSocialProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(0, 7, Short.MAX_VALUE)
                                .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel40)
                        .addGap(26, 26, 26)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDniRutProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelefonoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel19))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(txtDireccionProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(txtRazonSocialProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEditarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", jPanel4);

        jPanel5.setBackground(new java.awt.Color(115, 135, 181));

        jLabel22.setBackground(new java.awt.Color(204, 204, 204));
        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("CÓDIGO :");

        jLabel23.setBackground(new java.awt.Color(204, 204, 204));
        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("DESCRIPCIÓN :");

        jLabel24.setBackground(new java.awt.Color(204, 204, 204));
        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("CANTIDAD :");

        jLabel25.setBackground(new java.awt.Color(204, 204, 204));
        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("PRECIO :");

        jLabel26.setBackground(new java.awt.Color(204, 204, 204));
        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("PROVEEDOR :");

        btnGuardarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/GuardarTodo.png"))); // NOI18N
        btnGuardarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductoActionPerformed(evt);
            }
        });

        btnEditarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Actualizar (2).png"))); // NOI18N
        btnEditarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEditarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarProductoActionPerformed(evt);
            }
        });

        btnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/eliminar.png"))); // NOI18N
        btnEliminarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        btnNuevoProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/nuevo.png"))); // NOI18N
        btnNuevoProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProductoActionPerformed(evt);
            }
        });

        txtPrecioProducto.setBackground(new java.awt.Color(153, 204, 255));
        txtPrecioProducto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtPrecioProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrecioProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioProductoKeyTyped(evt);
            }
        });

        txtCantidadProducto.setBackground(new java.awt.Color(153, 204, 255));
        txtCantidadProducto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtCantidadProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadProductoKeyTyped(evt);
            }
        });

        txtDescripcionProducto.setBackground(new java.awt.Color(153, 204, 255));
        txtDescripcionProducto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDescripcionProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtCodigoProducto.setBackground(new java.awt.Color(153, 204, 255));
        txtCodigoProducto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtCodigoProducto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCodigoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoProductoActionPerformed(evt);
            }
        });
        txtCodigoProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoProductoKeyTyped(evt);
            }
        });

        tableProductos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CÓDIGO", "DESCRIPCIÓN", "PROVEEDOR", "CANTIDAD", "PRECIO"
            }
        ));
        tableProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableProductosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tableProductos);
        if (tableProductos.getColumnModel().getColumnCount() > 0) {
            tableProductos.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableProductos.getColumnModel().getColumn(1).setPreferredWidth(50);
            tableProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tableProductos.getColumnModel().getColumn(3).setPreferredWidth(80);
            tableProductos.getColumnModel().getColumn(4).setPreferredWidth(30);
            tableProductos.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        cbxProveedorProducto.setBackground(new java.awt.Color(153, 204, 255));
        cbxProveedorProducto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cbxProveedorProducto.setForeground(new java.awt.Color(204, 204, 255));

        btnExcelProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/excel.png"))); // NOI18N
        btnExcelProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExcelProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcelProductoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrecioProducto)
                                    .addComponent(cbxProveedorProducto, 0, 213, Short.MAX_VALUE)
                                    .addComponent(txtCantidadProducto)
                                    .addComponent(txtDescripcionProducto)
                                    .addComponent(txtCodigoProducto)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(btnGuardarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEditarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnNuevoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnExcelProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(261, 261, 261)
                        .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel22)
                            .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPrecioProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(cbxProveedorProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEditarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEliminarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevoProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnExcelProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab4", jPanel5);

        jPanel6.setBackground(new java.awt.Color(115, 135, 181));

        tableVentas.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CLIENTE", "VENDEDOR", "TOTAL"
            }
        ));
        tableVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableVentasMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tableVentas);
        if (tableVentas.getColumnModel().getColumnCount() > 0) {
            tableVentas.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableVentas.getColumnModel().getColumn(1).setPreferredWidth(200);
            tableVentas.getColumnModel().getColumn(2).setPreferredWidth(180);
            tableVentas.getColumnModel().getColumn(3).setPreferredWidth(80);
        }

        btnPdfVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/pdf.png"))); // NOI18N
        btnPdfVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPdfVentasActionPerformed(evt);
            }
        });

        jLabel33.setBackground(new java.awt.Color(204, 204, 204));
        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Registro de las ventas realizadas");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnPdfVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(51, 51, 51)
                        .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85)
                        .addComponent(jLabel33)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(btnPdfVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab5", jPanel6);

        jPanel7.setBackground(new java.awt.Color(115, 135, 181));

        jPanel10.setBackground(new java.awt.Color(50, 69, 109));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("DATOS DE LA EMPRESA");

        jLabel28.setBackground(new java.awt.Color(204, 204, 204));
        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("NOMBRE");

        txtNombreEmpresa.setEditable(false);
        txtNombreEmpresa.setBackground(new java.awt.Color(153, 204, 255));
        txtNombreEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtNombreEmpresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel29.setBackground(new java.awt.Color(204, 204, 204));
        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("TELÉFONO");

        txtTelefonoEmpresa.setEditable(false);
        txtTelefonoEmpresa.setBackground(new java.awt.Color(153, 204, 255));
        txtTelefonoEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtTelefonoEmpresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTelefonoEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoEmpresaKeyTyped(evt);
            }
        });

        jLabel31.setBackground(new java.awt.Color(204, 204, 204));
        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("RAZÓN SOCIAL");

        txtRazonSocialEmpresa.setEditable(false);
        txtRazonSocialEmpresa.setBackground(new java.awt.Color(153, 204, 255));
        txtRazonSocialEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtRazonSocialEmpresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtRutEmpresa.setEditable(false);
        txtRutEmpresa.setBackground(new java.awt.Color(153, 204, 255));
        txtRutEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtRutEmpresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtRutEmpresa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRutEmpresaKeyTyped(evt);
            }
        });

        txtDireccionEmpresa.setEditable(false);
        txtDireccionEmpresa.setBackground(new java.awt.Color(153, 204, 255));
        txtDireccionEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDireccionEmpresa.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel27.setBackground(new java.awt.Color(204, 204, 204));
        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("RUC");

        jLabel30.setBackground(new java.awt.Color(204, 204, 204));
        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("DIRECCIÓN");

        btnActualizarDatosEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnActualizarDatosEmpresa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Actualizar (2).png"))); // NOI18N
        btnActualizarDatosEmpresa.setText("Actualizar");
        btnActualizarDatosEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDatosEmpresaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(jLabel32))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(149, 149, 149)
                        .addComponent(btnActualizarDatosEmpresa))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))
                                    .addGroup(jPanel10Layout.createSequentialGroup()
                                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(19, 19, 19)))
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTelefonoEmpresa)
                                    .addComponent(txtNombreEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDireccionEmpresa)
                                    .addComponent(txtRazonSocialEmpresa)
                                    .addComponent(txtRutEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel32)
                .addGap(27, 27, 27)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtNombreEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(txtTelefonoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(txtRazonSocialEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(txtDireccionEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(txtRutEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(btnActualizarDatosEmpresa)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        jPanel11.setBackground(new java.awt.Color(50, 69, 109));

        jLabel34.setText("jLabel34");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("Actualizar perfil");

        jLabel36.setBackground(new java.awt.Color(204, 204, 204));
        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("NOMBRE");

        jLabel37.setBackground(new java.awt.Color(204, 204, 204));
        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("EMAIL");

        jLabel38.setBackground(new java.awt.Color(204, 204, 204));
        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("PASSWORD");

        txtNombreUsuarioActualizarPerfil.setEditable(false);
        txtNombreUsuarioActualizarPerfil.setBackground(new java.awt.Color(153, 204, 255));
        txtNombreUsuarioActualizarPerfil.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtNombreUsuarioActualizarPerfil.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        txtCorreoUsuarioActualizarPerfil.setEditable(false);
        txtCorreoUsuarioActualizarPerfil.setBackground(new java.awt.Color(153, 204, 255));
        txtCorreoUsuarioActualizarPerfil.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtCorreoUsuarioActualizarPerfil.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCorreoUsuarioActualizarPerfil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCorreoUsuarioActualizarPerfilKeyTyped(evt);
            }
        });

        btnActualizarDatosEmpresa1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnActualizarDatosEmpresa1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/Actualizar (2).png"))); // NOI18N
        btnActualizarDatosEmpresa1.setText("Actualizar");
        btnActualizarDatosEmpresa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarDatosEmpresa1ActionPerformed(evt);
            }
        });

        txtPasswordUsuarioActualizarPerfil.setBackground(new java.awt.Color(153, 204, 255));
        txtPasswordUsuarioActualizarPerfil.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtPasswordUsuarioActualizarPerfil.setText("jPasswordField1");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtCorreoUsuarioActualizarPerfil)
                            .addComponent(txtNombreUsuarioActualizarPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(62, 62, 62))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(btnActualizarDatosEmpresa1)
                        .addGap(207, 207, 207))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(txtPasswordUsuarioActualizarPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(93, 93, 93))))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(txtNombreUsuarioActualizarPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(txtCorreoUsuarioActualizarPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(txtPasswordUsuarioActualizarPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addComponent(btnActualizarDatosEmpresa1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab6", jPanel7);

        jPanel8.setBackground(new java.awt.Color(115, 135, 181));

        tableUsuarios.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CORREO", "NOMBRE", "ROL"
            }
        ));
        tableUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableUsuariosMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(tableUsuarios);
        if (tableUsuarios.getColumnModel().getColumnCount() > 0) {
            tableUsuarios.getColumnModel().getColumn(0).setPreferredWidth(20);
            tableUsuarios.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableUsuarios.getColumnModel().getColumn(2).setPreferredWidth(100);
            tableUsuarios.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        btnEliminarVenta1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/eliminar.png"))); // NOI18N
        btnEliminarVenta1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVenta1ActionPerformed(evt);
            }
        });

        txtIdUsuario.setEditable(false);
        txtIdUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdUsuarioActionPerformed(evt);
            }
        });

        btnRegistrarUsuario1.setBackground(new java.awt.Color(0, 51, 255));
        btnRegistrarUsuario1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnRegistrarUsuario1.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarUsuario1.setText("Registrar Usuario");
        btnRegistrarUsuario1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarUsuario1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(txtIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(315, 315, 315)
                        .addComponent(btnRegistrarUsuario1, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminarVenta1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1034, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminarVenta1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrarUsuario1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIdUsuario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jTabbedPane1.addTab("tab7", jPanel8);

        jPanel2.setBackground(new java.awt.Color(115, 135, 181));
        jPanel2.setPreferredSize(new java.awt.Dimension(1081, 427));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("CÓDIGO");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("DESCRIPCIÓN");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("CANTIDAD");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("PRECIO");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("STOCK");

        btnEliminarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/eliminar.png"))); // NOI18N
        btnEliminarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVentaActionPerformed(evt);
            }
        });

        txtCodigoVenta.setBackground(new java.awt.Color(153, 204, 255));
        txtCodigoVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtCodigoVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoVentaActionPerformed(evt);
            }
        });
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
        txtPrecioVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioVentaActionPerformed(evt);
            }
        });
        txtPrecioVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioVentaKeyTyped(evt);
            }
        });

        txtStockDisponibleVenta.setBackground(new java.awt.Color(153, 204, 255));
        txtStockDisponibleVenta.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtStockDisponibleVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockDisponibleVentaActionPerformed(evt);
            }
        });
        txtStockDisponibleVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockDisponibleVentaKeyTyped(evt);
            }
        });

        tableVenta.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tableVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "DESCRIPCIÓN", "CANTIDAD", "PRECIO", "TOTAL"
            }
        ));
        jScrollPane1.setViewportView(tableVenta);
        if (tableVenta.getColumnModel().getColumnCount() > 0) {
            tableVenta.getColumnModel().getColumn(0).setPreferredWidth(30);
            tableVenta.getColumnModel().getColumn(1).setPreferredWidth(100);
            tableVenta.getColumnModel().getColumn(2).setPreferredWidth(30);
            tableVenta.getColumnModel().getColumn(3).setPreferredWidth(30);
            tableVenta.getColumnModel().getColumn(4).setPreferredWidth(40);
        }

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("DNI/RUT");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("NOMBRE");

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

        btnGenerarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/print.png"))); // NOI18N
        btnGenerarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarVentaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/money.png"))); // NOI18N
        jLabel10.setText("TOTAL A PAGAR");

        labelTotalVenta.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        labelTotalVenta.setForeground(new java.awt.Color(153, 204, 255));
        labelTotalVenta.setText("---");

        txtDireccionClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionClienteVentaActionPerformed(evt);
            }
        });

        btnGraficaVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/torta.png"))); // NOI18N
        btnGraficaVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraficaVentasActionPerformed(evt);
            }
        });

        jDateChooserVenta.setBackground(new java.awt.Color(153, 204, 255));
        jDateChooserVenta.setForeground(new java.awt.Color(204, 204, 255));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(204, 204, 204));
        jLabel11.setText("Seleccionar");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1036, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtCodigoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(35, 35, 35)
                                .addComponent(txtIdProductoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantidadVenta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStockDisponibleVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtDniRutVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtNombreClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTelefonoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDireccionClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRazonSocialClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnGraficaVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jDateChooserVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTotalVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel4)
                                    .addComponent(txtIdProductoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(8, 8, 8))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCodigoVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStockDisponibleVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(labelTotalVenta))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jDateChooserVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDniRutVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefonoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDireccionClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtRazonSocialClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnGraficaVentas, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab1", jPanel2);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 150, 1070, 450));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Redirige al panel de productos, limpia y lista los mismos.
     *
     * @param evt
     */
    private void btnProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductosActionPerformed
        jTabbedPane1.setSelectedIndex(2);
        this.limpiarTabla();
        this.listarProductos();
        this.limpiarProducto();
        this.btnProductos.setBackground(new Color(122, 163, 177));
        this.limpiarEstilosBotones(this.btnNuevaVenta, this.btnClientes1, this.btnConfiguracion, this.btnProveedor, this.btnUsuarios, this.btnVentas);
    }//GEN-LAST:event_btnProductosActionPerformed

    private void txtCodigoVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoVentaActionPerformed
    }//GEN-LAST:event_txtCodigoVentaActionPerformed

    /**
     * Guarda un cliente en la base de datos
     *
     * @param evt
     */
    private void btnGuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarClienteActionPerformed
        if ("".equals(txtDireccionCliente.getText())
                || "".equals(txtNombreCliente.getText())
                || "".equals(txtDniRutCliente.getText())
                || "".equals(txtTelefonoCliente.getText())) {
            JOptionPane.showMessageDialog(null, "Algunos campos están vacíos");
        } else {

            cliente.setDni(Long.parseLong(txtDniRutCliente.getText()));
            cliente.setNombre(txtNombreCliente.getText());
            cliente.setTelefono(Long.parseLong(txtTelefonoCliente.getText()));
            cliente.setDireccion(txtDireccionCliente.getText());
            cliente.setRazonSocial(txtRazonSocialCliente.getText());

            clienteDAO.registrarCliente(cliente);
            JOptionPane.showMessageDialog(null, "Cliente Registrado");

            this.limpiarTabla();
            this.listarClientes();
            this.limpiarCliente();
        }
    }//GEN-LAST:event_btnGuardarClienteActionPerformed

    /**
     * Muestra el panel de los clientes y llama al método para listar los
     * clientes.
     *
     * @param evt
     */
    private void btnUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuariosActionPerformed
        this.limpiarTabla();
        this.listarUsuarios();
        jTabbedPane1.setSelectedIndex(5);
        this.btnUsuarios.setBackground(new Color(122, 163, 177));
        this.limpiarEstilosBotones(this.btnNuevaVenta, this.btnClientes1, this.btnProductos, this.btnProveedor, this.btnConfiguracion, this.btnVentas);
    }//GEN-LAST:event_btnUsuariosActionPerformed

    /**
     * Selecciona un registro de la tabla clientes clickeado por el usuario.
     *
     * @param evt
     */
    private void tableClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableClientesMouseClicked
        int fila = tableClientes.rowAtPoint(evt.getPoint());
        txtIdCliente.setText(tableClientes.getValueAt(fila, 0).toString());
        txtDniRutCliente.setText(tableClientes.getValueAt(fila, 1).toString());
        txtNombreCliente.setText(tableClientes.getValueAt(fila, 2).toString());
        txtTelefonoCliente.setText(tableClientes.getValueAt(fila, 3).toString());
        txtDireccionCliente.setText(tableClientes.getValueAt(fila, 4).toString());
        txtRazonSocialCliente.setText(tableClientes.getValueAt(fila, 5).toString());
    }//GEN-LAST:event_tableClientesMouseClicked

    /**
     * Elimina un cliente seleccionado de la tabla clientes.
     *
     * @param evt
     */
    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        if (!"".equals(txtIdCliente.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?");

            if (pregunta == 0) {
                clienteDAO.eliminarCliente(Integer.parseInt(txtIdCliente.getText()));

                this.limpiarTabla();
                this.listarClientes();
                this.limpiarCliente();
            }
        }
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    /**
     * Actualiza un cliente en específico.
     *
     * @param evt
     */
    private void btnEditarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarClienteActionPerformed
        if ("".equals(txtIdCliente.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        } else {
            if (!"".equals(txtDniRutCliente.getText())
                    || !"".equals(txtNombreCliente.getText())
                    || !"".equals(txtTelefonoCliente.getText())
                    || !"".equals(txtDireccionCliente.getText())) {

                cliente.setId(Integer.parseInt(txtIdCliente.getText()));
                cliente.setDni(Long.parseLong(txtDniRutCliente.getText()));
                cliente.setNombre(txtNombreCliente.getText());
                cliente.setTelefono(Long.parseLong(txtTelefonoCliente.getText()));
                cliente.setDireccion(txtDireccionCliente.getText());
                cliente.setRazonSocial(txtRazonSocialCliente.getText());

                clienteDAO.modificarCliente(cliente);
                JOptionPane.showMessageDialog(null, "Cliente actualizado");
                this.limpiarTabla();
                this.limpiarCliente();
                this.listarClientes();
            } else {
                JOptionPane.showMessageDialog(null, "Algunos campos están vacíos.");
            }
        }
    }//GEN-LAST:event_btnEditarClienteActionPerformed

    /**
     * Limpiar el formulario del cliente.
     *
     * @param evt
     */
    private void btnNuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoClienteActionPerformed
        this.limpiarCliente();
    }//GEN-LAST:event_btnNuevoClienteActionPerformed

    /**
     * Almacena un proveedor en la base de datos.
     *
     * @param evt
     */
    private void btnGuardarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProveedorActionPerformed
        if (!"".equals(txtDniRutProveedor.getText())
                || !"".equals(txtNombreProveedor.getText())
                || !"".equals(txtTelefonoProveedor.getText())
                || !"".equals(txtDireccionProveedor.getText())) {

            proveedor.setRut(Long.parseLong(txtDniRutProveedor.getText()));
            proveedor.setNombre(txtNombreProveedor.getText());
            proveedor.setTelefono(Long.parseLong(txtTelefonoProveedor.getText()));
            proveedor.setDireccion(txtDireccionProveedor.getText());
            proveedor.setRazonSocial(txtRazonSocialProveedor.getText());

            proveedorDAO.registrarProveedor(proveedor);
            JOptionPane.showMessageDialog(null, "Proveedor Registrado");

            this.limpiarTabla();
            this.listarProveedor();
            this.limpiarProveedor();
        } else {
            JOptionPane.showMessageDialog(null, "Los campos están vacíos");
        }
    }//GEN-LAST:event_btnGuardarProveedorActionPerformed

    /**
     * Muestra el panel de los proveedores y llama al método para listar los
     * proveedores.
     *
     * @param evt
     */
    private void btnProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProveedorActionPerformed
        this.limpiarTabla();
        this.listarProveedor();
        jTabbedPane1.setSelectedIndex(1);
        this.btnProveedor.setBackground(new Color(122, 163, 177));
        this.limpiarEstilosBotones(this.btnNuevaVenta, this.btnClientes1, this.btnProductos, this.btnConfiguracion, this.btnUsuarios, this.btnVentas);
    }//GEN-LAST:event_btnProveedorActionPerformed

    /**
     * Selecciona un registro de la tabla proveedores clickeado por el usuario.
     *
     * @param evt
     */
    private void tableProveedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProveedoresMouseClicked
        int fila = tableProveedores.rowAtPoint(evt.getPoint());
        txtIdProveedor.setText(tableProveedores.getValueAt(fila, 0).toString());
        txtDniRutProveedor.setText(tableProveedores.getValueAt(fila, 1).toString());
        txtNombreProveedor.setText(tableProveedores.getValueAt(fila, 2).toString());
        txtTelefonoProveedor.setText(tableProveedores.getValueAt(fila, 3).toString());
        txtDireccionProveedor.setText(tableProveedores.getValueAt(fila, 4).toString());
        txtRazonSocialProveedor.setText(tableProveedores.getValueAt(fila, 5).toString());
    }//GEN-LAST:event_tableProveedoresMouseClicked

    /**
     * Elimina un proveedor en específico.
     *
     * @param evt
     */
    private void btnEliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProveedorActionPerformed
        if (!"".equals(txtIdProveedor.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?");

            if (pregunta == 0) {
                proveedorDAO.eliminarProveedor(Integer.parseInt(txtIdProveedor.getText()));

                this.limpiarTabla();
                this.listarProveedor();
                this.limpiarProveedor();
            }
        }
    }//GEN-LAST:event_btnEliminarProveedorActionPerformed

    /**
     * Actualiza un proveedor en específico.
     *
     * @param evt
     */
    private void btnEditarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProveedorActionPerformed
        if ("".equals(txtIdProveedor.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        } else {
            if (!"".equals(txtDniRutProveedor.getText())
                    || !"".equals(txtNombreProveedor.getText())
                    || !"".equals(txtTelefonoProveedor.getText())
                    || !"".equals(txtDireccionProveedor.getText())) {

                proveedor.setId(Integer.parseInt(txtIdProveedor.getText()));
                proveedor.setRut(Long.parseLong(txtDniRutProveedor.getText()));
                proveedor.setNombre(txtNombreProveedor.getText());
                proveedor.setTelefono(Long.parseLong(txtTelefonoProveedor.getText()));
                proveedor.setDireccion(txtDireccionProveedor.getText());
                proveedor.setRazonSocial(txtRazonSocialProveedor.getText());

                proveedorDAO.modificarProveedor(proveedor);
                this.limpiarTabla();
                this.limpiarProveedor();
                this.listarProveedor();
            } else {
                JOptionPane.showMessageDialog(null, "Algunos campos están vacíos.");
            }
        }
    }//GEN-LAST:event_btnEditarProveedorActionPerformed

    /**
     * Limpia el formulario de proveedores.
     *
     * @param evt
     */
    private void btnNuevoProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProveedorActionPerformed
        this.limpiarProveedor();
    }//GEN-LAST:event_btnNuevoProveedorActionPerformed

    /**
     * Guarda un producto en la base de datos
     *
     * @param evt
     */
    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductoActionPerformed
        if ("".equals(txtCodigoProducto.getText().trim())
                || "".equals(txtDescripcionProducto.getText().trim())
                || "".equals(txtPrecioProducto.getText().trim())
                || "".equals(cbxProveedorProducto.getSelectedItem())
                || "".equals(txtCantidadProducto.getText().trim())) {
            JOptionPane.showMessageDialog(null, "Algunos campos están vacíos");
        } else {

            producto.setCodigo(txtCodigoProducto.getText());
            producto.setNombre(txtDescripcionProducto.getText());
            producto.setProveedor(cbxProveedorProducto.getSelectedItem().toString());
            producto.setStock(Integer.parseInt(txtCantidadProducto.getText()));
            producto.setPrecio(Double.parseDouble(txtPrecioProducto.getText()));

            productoDAO.registrarProducto(producto);
            JOptionPane.showMessageDialog(null, "Producto Registrado");

            this.limpiarTabla();
            this.listarProductos();
            this.limpiarProducto();
        }
    }//GEN-LAST:event_btnGuardarProductoActionPerformed

    /**
     * Selecciona un registro de la tabla productos clickeado por el usuario.
     *
     * @param evt
     */
    private void tableProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableProductosMouseClicked
        int fila = tableProductos.rowAtPoint(evt.getPoint());
        txtIdProducto.setText(tableProductos.getValueAt(fila, 0).toString());
        txtCodigoProducto.setText(tableProductos.getValueAt(fila, 1).toString());
        txtDescripcionProducto.setText(tableProductos.getValueAt(fila, 2).toString());
        cbxProveedorProducto.setSelectedItem(tableProductos.getValueAt(fila, 3).toString());
        txtCantidadProducto.setText(tableProductos.getValueAt(fila, 4).toString());
        txtPrecioProducto.setText(tableProductos.getValueAt(fila, 5).toString());
    }//GEN-LAST:event_tableProductosMouseClicked

    /**
     * Elimina un producto del sistema.
     *
     * @param evt
     */
    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        if (!"".equals(txtIdProducto.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?");

            if (pregunta == 0) {
                productoDAO.eliminarProducto(Integer.parseInt(txtIdProducto.getText()));

                this.limpiarTabla();
                this.listarProductos();
                this.limpiarProducto();
            }
        }
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    /**
     * Edita un producto existente en el sistema.
     *
     * @param evt
     */
    private void btnEditarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarProductoActionPerformed
        if ("".equals(txtIdProducto.getText())) {
            JOptionPane.showMessageDialog(null, "Seleccione una fila");
        } else {
            if (!"".equals(txtCodigoProducto.getText())
                    || !"".equals(txtDescripcionProducto.getText())
                    || !"".equals(cbxProveedorProducto.getSelectedItem().toString())
                    || !"".equals(txtPrecioProducto.getText())
                    || !"".equals(txtCantidadProducto.getText())) {

                producto.setId(Integer.parseInt(txtIdProducto.getText()));
                producto.setCodigo(txtCodigoProducto.getText());
                producto.setNombre(txtDescripcionProducto.getText());
                producto.setProveedor(cbxProveedorProducto.getSelectedItem().toString());
                producto.setStock(Integer.parseInt(txtCantidadProducto.getText()));
                producto.setPrecio(Double.parseDouble(txtPrecioProducto.getText()));

                productoDAO.modificarProducto(producto);
                this.limpiarTabla();
                this.limpiarProducto();
                this.listarProductos();
            } else {
                JOptionPane.showMessageDialog(null, "Algunos campos están vacíos.");
            }
        }
    }//GEN-LAST:event_btnEditarProductoActionPerformed

    /**
     * Genera reporte en excel de los productos existentes en el sistema.
     *
     * @param evt
     */
    private void btnExcelProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcelProductoActionPerformed
        Excel.reporte();
    }//GEN-LAST:event_btnExcelProductoActionPerformed

    /**
     * Realiza un seteo de campos según un código ingresado, cuando el usuario
     * escribe un código de algún producto y presiona la tecla ENTER,
     * automáticamente setea sus valores para que el usuario después ingrese la
     * cantidad.
     *
     * @param evt
     */
    private void txtCodigoVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoVentaKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            if (!"".equals(txtCodigoVenta.getText())) {
                producto = productoDAO.buscarProducto(txtCodigoVenta.getText());
                if (producto.getNombre() != null) {
                    txtDescripcionVenta.setText(producto.getNombre());
                    txtPrecioVenta.setText(String.valueOf(producto.getPrecio()));
                    txtStockDisponibleVenta.setText(String.valueOf(producto.getStock()));
                    txtCantidadVenta.requestFocus();
                } else {
                    this.limpiarVenta();
                    txtCodigoVenta.requestFocus();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese el código del producto");
                txtCodigoVenta.requestFocus();
            }
        }
    }//GEN-LAST:event_txtCodigoVentaKeyPressed

    /**
     * Cuando el usuario escribe la cantidad y presiona la tecla ENTER, el
     * producto se registra en la venta y se posiciona en la tabla venta.
     *
     * @param evt
     */
    private void txtCantidadVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            if (!"".equals(txtCantidadVenta.getText())) {
                String codigoProducto = txtCodigoVenta.getText();
                System.out.println(codigoProducto);
                String descripcion = txtDescripcionVenta.getText();
                int cantidad = Integer.parseInt(txtCantidadVenta.getText());
                double precio = Double.parseDouble(txtPrecioVenta.getText());
                double totalVenta = cantidad * precio;
                int stockDisponible = Integer.parseInt(txtStockDisponibleVenta.getText());

                if (stockDisponible >= cantidad) {
                    item = item + 1;
                    modeloTemporal = (DefaultTableModel) tableVenta.getModel();
                    for (int i = 0; i < tableVenta.getRowCount(); i++) {
                        if (tableVenta.getValueAt(i, 1).equals(txtDescripcionVenta.getText())) {
                            JOptionPane.showMessageDialog(null, "El producto ya está registrado.");
                            return;
                        }
                    }

                    ArrayList lista = new ArrayList();
                    lista.add(item);
                    lista.add(codigoProducto);
                    lista.add(descripcion);
                    lista.add(cantidad);
                    lista.add(precio);
                    lista.add(totalVenta);

                    Object[] objeto = new Object[5];
                    objeto[0] = lista.get(1);
                    objeto[1] = lista.get(2);
                    objeto[2] = lista.get(3);
                    objeto[3] = lista.get(4);
                    objeto[4] = lista.get(5);

                    modeloTemporal.addRow(objeto);
                    tableVenta.setModel(modeloTemporal);
                    this.totalPagar();
                    this.limpiarVenta();
                    txtCodigoVenta.requestFocus();
                } else {
                    JOptionPane.showMessageDialog(null, "Stock no disponible.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese la cantidad.");
            }
        }
    }//GEN-LAST:event_txtCantidadVentaKeyPressed

    /**
     * Elimina un producto seleccionado de la table de la venta.
     *
     * @param evt
     */
    private void btnEliminarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVentaActionPerformed
        modelo = (DefaultTableModel) tableVenta.getModel();
        modelo.removeRow(tableVenta.getSelectedRow());
        this.totalPagar();
        txtCodigoVenta.requestFocus();
    }//GEN-LAST:event_btnEliminarVentaActionPerformed

    /**
     * Cuando el usuario ingrese un dni de algún cliente y presione la tecla
     * ENTER, se setean sus valores para generarle la venta a dicho cliente.
     *
     * @param evt
     */
    private void txtDniRutVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniRutVentaKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            if (!"".equals(txtDniRutVenta.getText())) {
                cliente = clienteDAO.buscarCliente(Integer.parseInt(txtDniRutVenta.getText()));
                if (cliente.getNombre() != null) {
                    txtNombreClienteVenta.setText(cliente.getNombre());
                    txtTelefonoClienteVenta.setText(String.valueOf(cliente.getTelefono()));
                    txtDireccionClienteVenta.setText(cliente.getDireccion());
                    txtRazonSocialClienteVenta.setText(cliente.getRazonSocial());
                } else {
                    this.limpiarClienteVenta();
                    JOptionPane.showMessageDialog(null, "El cliente no está registrado");
                }
            }
        }
    }//GEN-LAST:event_txtDniRutVentaKeyPressed

    /**
     * Genera la venta y su detalle de venta junto con los productos que el
     * cliente vaya a comprar.
     *
     * @param evt
     */
    private void btnGenerarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarVentaActionPerformed
        if (tableVenta.getRowCount() > 0) {
            if (!"".equals(txtNombreClienteVenta.getText())) {
                this.registrarVenta();
                this.registrarDetalleVenta();
                this.actualizarStock();
                this.reporteVentaPDF.pdf(txtRutEmpresa.getText(), txtNombreEmpresa.getText(),
                        txtTelefonoEmpresa.getText(), txtDireccionEmpresa.getText(),
                        txtRazonSocialEmpresa.getText(), txtDniRutVenta.getText(),
                        txtNombreClienteVenta.getText(), txtTelefonoClienteVenta.getText(),
                        txtDireccionClienteVenta.getText(), tableVenta, totalPagar);
                this.limpiarTableVenta();
                this.limpiarClienteVenta();
            } else {
                JOptionPane.showMessageDialog(null, "Debe ingresar un cliente");
                this.txtNombreClienteVenta.requestFocus();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Debe ingresar al menos un producto");
            this.txtCodigoVenta.requestFocus();
        }
    }//GEN-LAST:event_btnGenerarVentaActionPerformed

    /**
     * Al presionar sobre el botón, se redirige al panel Nueva Venta y se
     * limpian sus campos.
     *
     * @param evt
     */
    private void btnNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaVentaActionPerformed
        this.jTabbedPane1.setSelectedIndex(6);
        this.btnNuevaVenta.setBackground(new Color(122, 163, 177));
        this.limpiarEstilosBotones(this.btnConfiguracion, this.btnClientes1, this.btnProductos, this.btnProveedor, this.btnUsuarios, this.btnVentas);
        this.limpiarVenta();
    }//GEN-LAST:event_btnNuevaVentaActionPerformed

    /**
     * Al presionar sobre el botón, se redirige al panel de Configuración y se
     * listan los datos de la empresa.
     *
     * @param evt
     */
    private void btnConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfiguracionActionPerformed
        this.txtIdEmpresa.setVisible(false);
        this.jTabbedPane1.setSelectedIndex(4);
        this.listarDatosEmpresa();
        this.btnConfiguracion.setBackground(new Color(122, 163, 177));
        this.limpiarEstilosBotones(this.btnNuevaVenta, this.btnClientes1, this.btnProductos, this.btnProveedor, this.btnUsuarios, this.btnVentas);
    }//GEN-LAST:event_btnConfiguracionActionPerformed

    private void txtCodigoVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoVentaKeyTyped

    }//GEN-LAST:event_txtCodigoVentaKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números enteros.
     *
     * @param evt
     */
    private void txtCantidadVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyTyped
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtCantidadVentaKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números con punto
     * decimal.
     *
     * @param evt
     */
    private void txtPrecioVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioVentaKeyTyped
        this.evento.numberDecimalKeyPress(evt, txtPrecioVenta);
    }//GEN-LAST:event_txtPrecioVentaKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números enteros.
     *
     * @param evt
     */
    private void txtStockDisponibleVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockDisponibleVentaKeyTyped
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtStockDisponibleVentaKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números enteros.
     *
     * @param evt
     */
    private void txtDniRutVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniRutVentaKeyTyped
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtDniRutVentaKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir cadenas de caracteres.
     *
     * @param evt
     */
    private void txtNombreClienteVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreClienteVentaKeyTyped
        this.evento.textKeyPress(evt);
    }//GEN-LAST:event_txtNombreClienteVentaKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números enteros.
     *
     * @param evt
     */
    private void txtDniRutClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniRutClienteKeyTyped
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtDniRutClienteKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir cadenas de caracteres.
     *
     * @param evt
     */
    private void txtNombreClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreClienteKeyTyped
        this.evento.textKeyPress(evt);
    }//GEN-LAST:event_txtNombreClienteKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números enteros.
     *
     * @param evt
     */
    private void txtTelefonoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoClienteKeyTyped
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtTelefonoClienteKeyTyped

    private void txtDireccionClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionClienteKeyTyped

    }//GEN-LAST:event_txtDireccionClienteKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números enteros.
     *
     * @param evt
     */
    private void txtDniRutProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDniRutProveedorKeyTyped
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtDniRutProveedorKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir cadenas de caracteres.
     *
     * @param evt
     */
    private void txtNombreProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreProveedorKeyTyped
        this.evento.textKeyPress(evt);
    }//GEN-LAST:event_txtNombreProveedorKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números enteros.
     *
     * @param evt
     */
    private void txtTelefonoProveedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoProveedorKeyPressed
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtTelefonoProveedorKeyPressed

    private void txtCodigoProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoProductoKeyTyped

    }//GEN-LAST:event_txtCodigoProductoKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números enteros.
     *
     * @param evt
     */
    private void txtCantidadProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadProductoKeyTyped
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtCantidadProductoKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números con punto
     * decimal.
     *
     * @param evt
     */
    private void txtPrecioProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioProductoKeyTyped
        this.evento.numberDecimalKeyPress(evt, txtPrecioProducto);
    }//GEN-LAST:event_txtPrecioProductoKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números enteros.
     *
     * @param evt
     */
    private void txtRutEmpresaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRutEmpresaKeyTyped
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtRutEmpresaKeyTyped

    /**
     * Se valida que en este txt solo se puedan escribir números enteros.
     *
     * @param evt
     */
    private void txtTelefonoEmpresaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoEmpresaKeyTyped
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtTelefonoEmpresaKeyTyped

    /**
     * Se encarga de pasarle los datos de la empresa al método que modifica los
     * datos de la empresa para luego actualizar dichos datos.
     *
     * @param evt
     */
    private void btnActualizarDatosEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarDatosEmpresaActionPerformed
        if (!"".equals(txtRutEmpresa.getText())
                || !"".equals(txtNombreEmpresa.getText())
                || !"".equals(txtTelefonoEmpresa.getText())
                || !"".equals(txtDireccionEmpresa.getText())) {

            configuracionDatosEmpresa.setId(Integer.parseInt(txtIdEmpresa.getText()));
            configuracionDatosEmpresa.setRut(Long.parseLong(txtRutEmpresa.getText()));
            configuracionDatosEmpresa.setNombre(txtNombreEmpresa.getText());
            configuracionDatosEmpresa.setTelefono(Long.parseLong(txtTelefonoEmpresa.getText()));
            configuracionDatosEmpresa.setDireccion(txtDireccionEmpresa.getText());
            configuracionDatosEmpresa.setRazonSocial(txtRazonSocialEmpresa.getText());

            configuracionDatosEmpresaDAO.modificarDatosEmpresa(configuracionDatosEmpresa);
            JOptionPane.showMessageDialog(null, "Datos Actualizados.");

            this.listarDatosEmpresa();
        } else {
            JOptionPane.showMessageDialog(null, "Algunos campos están vacíos.");
        }
    }//GEN-LAST:event_btnActualizarDatosEmpresaActionPerformed

    /**
     * Limita al usuario a que solo pueda escribir números enteros.
     *
     * @param evt
     */
    private void txtTelefonoProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoProveedorKeyTyped
        this.evento.numberKeyPress(evt);
    }//GEN-LAST:event_txtTelefonoProveedorKeyTyped

    /**
     * Redirige al panel de ventas, lista las ventas y limpia el formulario.
     *
     * @param evt
     */
    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActionPerformed
        this.jTabbedPane1.setSelectedIndex(3);
        this.limpiarTabla();
        this.listarVentas();
        this.btnVentas.setBackground(new Color(122, 163, 177));
        this.limpiarEstilosBotones(this.btnNuevaVenta, this.btnClientes1, this.btnProductos, this.btnProveedor, this.btnUsuarios, this.btnConfiguracion);
    }//GEN-LAST:event_btnVentasActionPerformed

    /**
     * Recupera el id de la venta seleccionada por el usuario en la tabla.
     *
     * @param evt
     */
    private void tableVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableVentasMouseClicked
        int filaSeleccionada = tableVentas.rowAtPoint(evt.getPoint());
        txtIdVenta.setText(tableVentas.getValueAt(filaSeleccionada, 0).toString());
    }//GEN-LAST:event_tableVentasMouseClicked

    /**
     * Abre el reporte de la venta seleccionada en la tabla.
     *
     * @param evt
     */
    private void btnPdfVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPdfVentasActionPerformed
        try {
            int idVenta = Integer.parseInt(txtIdVenta.getText());
            File file = new File("src/juan/estevez/sistemaventa/reportes/reporteVenta" + idVenta + ".pdf");
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPdfVentasActionPerformed

    /**
     * Grafica las ventas parametrizadas por la fecha que el usuario suministre.
     *
     * @param evt
     */
    private void btnGraficaVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraficaVentasActionPerformed
        String fechaReporte = new SimpleDateFormat("dd/MM/yyyy").format(jDateChooserVenta.getDate());
        GraficoVentas.graficar(fechaReporte);
    }//GEN-LAST:event_btnGraficaVentasActionPerformed

    /**
     * Redirige al panel de clientes, los lista y limpia el formulario.
     *
     * @param evt
     */
    private void btnClientes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientes1ActionPerformed
        this.limpiarTabla();
        this.listarClientes();
        this.btnClientes1.setBackground(new Color(122, 163, 177));
        this.limpiarEstilosBotones(this.btnNuevaVenta, this.btnConfiguracion, this.btnProductos, this.btnProveedor, this.btnUsuarios, this.btnVentas);
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_btnClientes1ActionPerformed

    /**
     * Limpia los txt del formulario producto.
     *
     * @param evt
     */
    private void btnNuevoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProductoActionPerformed
        this.limpiarProducto();
    }//GEN-LAST:event_btnNuevoProductoActionPerformed

    /**
     * Recupera el id del usuario seleccionado en la tabala.
     *
     * @param evt
     */
    private void tableUsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableUsuariosMouseClicked
        int fila = tableUsuarios.rowAtPoint(evt.getPoint());
        txtIdUsuario.setText(tableUsuarios.getValueAt(fila, 0).toString());
    }//GEN-LAST:event_tableUsuariosMouseClicked

    /**
     * Cierra la sesión del usuario.
     *
     * @param evt
     */
    private void btnCerrarSesiónActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesiónActionPerformed
        int pregunta = JOptionPane.showConfirmDialog(null, "¿Seguro que quiere salir?");
        if (pregunta == 0) {
            Login login = new Login();
            login.setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnCerrarSesiónActionPerformed

    /**
     * Elimina un usuario del sistema.
     *
     * @param evt
     */
    private void btnEliminarVenta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVenta1ActionPerformed
        if (!"".equals(txtIdUsuario.getText())) {
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?");
            if (pregunta == 0) {
                usuarioDAO.eliminarUsuario(Integer.parseInt(txtIdUsuario.getText()));

                this.limpiarTabla();
                this.listarUsuarios();
            }
        } else {
            JOptionPane.showMessageDialog(null, "El usuario no está registrado.");
        }

    }//GEN-LAST:event_btnEliminarVenta1ActionPerformed

    /**
     * Redirige al formulario para registrar un nuevo usuario.
     *
     * @param evt
     */
    private void btnRegistrarUsuario1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarUsuario1ActionPerformed
        RegistroUsuarios registrarUsuario = new RegistroUsuarios();
        registrarUsuario.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegistrarUsuario1ActionPerformed

    private void txtRazonSocialClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRazonSocialClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRazonSocialClienteActionPerformed

    private void txtTelefonoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoClienteActionPerformed

    private void txtTelefonoProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoProveedorActionPerformed

    private void txtNombreProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreProveedorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreProveedorActionPerformed

    private void txtPrecioVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioVentaActionPerformed

    private void txtStockDisponibleVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockDisponibleVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockDisponibleVentaActionPerformed

    private void txtDireccionClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionClienteVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteVentaActionPerformed

    private void txtIdUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdUsuarioActionPerformed

    private void txtCodigoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoProductoActionPerformed

    private void txtCorreoUsuarioActualizarPerfilKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCorreoUsuarioActualizarPerfilKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreoUsuarioActualizarPerfilKeyTyped

    private void btnActualizarDatosEmpresa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarDatosEmpresa1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnActualizarDatosEmpresa1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Sistema().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarDatosEmpresa;
    private javax.swing.JButton btnActualizarDatosEmpresa1;
    private javax.swing.JButton btnCerrarSesión;
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel labelTotalVenta;
    private javax.swing.JLabel labelVendedor;
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

    /**
     * Limpia la tabla de los clientes.
     */
    public void limpiarTabla() {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            modelo.removeRow(i);
            i -= 1;
        }
    }

    /**
     * Limpia los txt del formulario clientes.
     */
    private void limpiarCliente() {
        this.txtIdCliente.setText("");
        this.txtDniRutCliente.setText("");
        this.txtNombreCliente.setText("");
        this.txtTelefonoCliente.setText("");
        this.txtDireccionCliente.setText("");
        this.txtRazonSocialCliente.setText("");
    }

    /**
     * Limpia los txt del formulario proveedor.
     */
    private void limpiarProveedor() {
        this.txtIdProveedor.setText("");
        this.txtDniRutProveedor.setText("");
        this.txtNombreProveedor.setText("");
        this.txtTelefonoProveedor.setText("");
        this.txtDireccionProveedor.setText("");
        this.txtRazonSocialProveedor.setText("");
    }

    /**
     * Limpia los txt del formulario productos.
     */
    private void limpiarProducto() {
        this.txtIdProducto.setText("");
        this.txtCodigoProducto.setText("");
        this.txtDescripcionProducto.setText("");
        this.txtCantidadProducto.setText("");
        this.txtPrecioProducto.setText("");
        this.cbxProveedorProducto.setSelectedItem("");
    }

    /**
     * Calcula el total a pagar de los productos registrados por el cliente.
     */
    private void totalPagar() {
        totalPagar = 0.00;
        int numeroFilas = tableVenta.getRowCount();
        for (int i = 0; i < numeroFilas; i++) {
            double calcular = Double.parseDouble(String.valueOf(tableVenta.getModel().getValueAt(i, 4)));
            totalPagar += calcular;
        }
        labelTotalVenta.setText(String.format("%.2f", totalPagar));
    }

    /**
     * Limpia el formulario de la venta.
     */
    private void limpiarVenta() {
        txtCodigoVenta.setText("");
        txtDescripcionVenta.setText("");
        txtPrecioVenta.setText("");
        txtCantidadVenta.setText("");
        txtStockDisponibleVenta.setText("");
        txtIdVenta.setText("");
    }

    /**
     * Limpia los txt del cliente ingresado en el panel de ventas.
     */
    private void limpiarClienteVenta() {
        txtDniRutVenta.setText("");
        txtNombreClienteVenta.setText("");
        txtTelefonoClienteVenta.setText("");
        txtDireccionClienteVenta.setText("");
        txtRazonSocialClienteVenta.setText("");
    }

    /**
     * Registra la venta en la base de datos.
     */
    private void registrarVenta() {
        String cliente = txtNombreClienteVenta.getText();
        String vendedor = labelVendedor.getText();
        double totalVenta = totalPagar;
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        venta.setTotal(totalVenta);
        venta.setFecha(fechaActual);
        ventaDao.registrarVenta(venta);
    }

    /**
     * Registra el detalle de venta en el sistema.
     */
    private void registrarDetalleVenta() {
        int id = ventaDao.idVenta();

        for (int i = 0; i < tableVenta.getRowCount(); i++) {
            String codigoProducto = tableVenta.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(tableVenta.getValueAt(i, 2).toString());
            double precio = Double.parseDouble(tableVenta.getValueAt(i, 3).toString());

            this.detalleVenta.setId(id);
            this.detalleVenta.setCodigoProducto(codigoProducto);
            this.detalleVenta.setCantidad(cantidad);
            this.detalleVenta.setPrecio(precio);

            ventaDao.registrarDetalleVenta(detalleVenta);
        }
    }

    /**
     * Actualiza el stock disponible de cada producto después de realizar una
     * venta.
     */
    private void actualizarStock() {
        for (int i = 0; i < tableVenta.getRowCount(); i++) {
            String codigoProducto = tableVenta.getValueAt(i, 0).toString();
            System.out.println("c" + codigoProducto);
            int cantidadProducto = Integer.parseInt(tableVenta.getValueAt(i, 2).toString());
            producto = productoDAO.buscarProducto(codigoProducto);
            int stockActual = producto.getStock() - cantidadProducto;

            this.productoDAO.actualizarStock(stockActual, codigoProducto);
        }
    }

    /**
     * Limpia la tabla venta donde se encuentran los productos de la venta.
     */
    private void limpiarTableVenta() {
        this.modeloTemporal = (DefaultTableModel) tableVenta.getModel();
        int filas = tableVenta.getRowCount();

        for (int i = 0; i < filas; i++) {
            modeloTemporal.removeRow(0);
        }
    }

    /**
     * Oculta los campos no requeridos e inicia los componentes de la
     * aplicación.
     */
    public void iniciarAplicacion() {
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
        this.productoDAO.consultarProveedor(cbxProveedorProducto);
    }

    private void limpiarEstilosBotones(JButton btn1, JButton btn2, JButton btn3, JButton btn4, JButton btn5, JButton btn6) {
        btn1.setBackground(new Color(187, 187, 187));
        btn2.setBackground(new Color(187, 187, 187));
        btn3.setBackground(new Color(187, 187, 187));
        btn4.setBackground(new Color(187, 187, 187));
        btn5.setBackground(new Color(187, 187, 187));
        btn6.setBackground(new Color(187, 187, 187));
    }

}
