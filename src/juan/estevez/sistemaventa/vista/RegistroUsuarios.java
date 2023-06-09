package juan.estevez.sistemaventa.vista;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import juan.estevez.sistemaventa.daos.LoginDAO;
import juan.estevez.sistemaventa.modelo.Loginn;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class RegistroUsuarios extends javax.swing.JFrame {

    Loginn login = new Loginn();
    LoginDAO loginDAO = new LoginDAO();

    /**
     * Creates new form Loginn
     */
    public RegistroUsuarios() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * Se encarga de validar y registrar el usuario y redirigirlo al login
     * principal.
     *
     * @throws java.sql.SQLException
     */
    public void validar() throws SQLException {
        String correo = txtCorreoUsuario.getText();
        String password = String.valueOf(txtPasswordUsuario.getPassword());
        String nombre = txtNombreUsuario.getText();
        String rol = cmbRolUsuario.getSelectedItem().toString();

        if (!"".equals(correo) || !"".equals(password) || !"".equals(nombre)) {
            login.setNombre(nombre);
            login.setCorreo(correo);
            login.setRol(rol);
            login.setPassword(password);

            loginDAO.registrarUsuario(login);

            Login loginApp = new Login();
            loginApp.setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "¡Debes ingresar datos!", "Registro inválido", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLogin = new javax.swing.JPanel();
        labelInicio = new javax.swing.JLabel();
        labelCorreoElectronicoUsuario = new javax.swing.JLabel();
        txtNombreUsuario = new javax.swing.JTextField();
        labelPasswordUsuario = new javax.swing.JLabel();
        txtPasswordUsuario = new javax.swing.JPasswordField();
        btnRegistrarse = new javax.swing.JButton();
        labelNombreUsuario = new javax.swing.JLabel();
        txtCorreoUsuario = new javax.swing.JTextField();
        labelRlUsuario = new javax.swing.JLabel();
        cmbRolUsuario = new javax.swing.JComboBox<>();
        Fondo = new javax.swing.JPanel();
        labelLogo = new javax.swing.JLabel();
        labelTituloRegistrarUsuario = new javax.swing.JLabel();
        labelDesarrolladoPorEnRegistrarUsuario = new javax.swing.JLabel();
        labelNombreDesarrolladorEnRegistrarUsuario = new javax.swing.JLabel();
        labelTituloVersionEnRegistrarUsuario = new javax.swing.JLabel();
        labelNumeroVersionEnRegistrarUsuario = new javax.swing.JLabel();
        labelContactoEnRegistrarUsuario = new javax.swing.JLabel();
        labelEmailDesarrolladorRegistrarUsuario = new javax.swing.JLabel();
        imagenFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelLogin.setBackground(new java.awt.Color(255, 255, 255));

        labelInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/iniciar.png"))); // NOI18N

        labelCorreoElectronicoUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCorreoElectronicoUsuario.setForeground(new java.awt.Color(0, 51, 255));
        labelCorreoElectronicoUsuario.setText("Correo Electrónico");

        txtNombreUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        labelPasswordUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelPasswordUsuario.setForeground(new java.awt.Color(0, 51, 255));
        labelPasswordUsuario.setText("Password");

        txtPasswordUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnRegistrarse.setBackground(new java.awt.Color(0, 51, 255));
        btnRegistrarse.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRegistrarse.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarse.setText("Registrarse");
        btnRegistrarse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarseActionPerformed(evt);
            }
        });

        labelNombreUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelNombreUsuario.setForeground(new java.awt.Color(0, 51, 255));
        labelNombreUsuario.setText("Nombre");

        txtCorreoUsuario.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        labelRlUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelRlUsuario.setForeground(new java.awt.Color(0, 51, 255));
        labelRlUsuario.setText("Rol");

        cmbRolUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Asistente" }));
        cmbRolUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbRolUsuarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLoginLayout = new javax.swing.GroupLayout(panelLogin);
        panelLogin.setLayout(panelLoginLayout);
        panelLoginLayout.setHorizontalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelRlUsuario)
                    .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(labelNombreUsuario)
                        .addComponent(labelPasswordUsuario)
                        .addComponent(labelCorreoElectronicoUsuario)
                        .addComponent(txtNombreUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                        .addComponent(txtPasswordUsuario)
                        .addComponent(txtCorreoUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                    .addComponent(cmbRolUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                .addContainerGap(105, Short.MAX_VALUE)
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                        .addComponent(labelInicio)
                        .addGap(110, 110, 110))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                        .addComponent(btnRegistrarse, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(97, 97, 97))))
        );
        panelLoginLayout.setVerticalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(labelInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelCorreoElectronicoUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCorreoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(labelPasswordUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPasswordUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelNombreUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelRlUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbRolUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRegistrarse, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        getContentPane().add(panelLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, 320, 410));

        Fondo.setBackground(new java.awt.Color(102, 102, 102));

        labelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/logo.png"))); // NOI18N

        labelTituloRegistrarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        labelTituloRegistrarUsuario.setForeground(new java.awt.Color(204, 204, 204));
        labelTituloRegistrarUsuario.setText("Punto de Venta");

        labelDesarrolladoPorEnRegistrarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDesarrolladoPorEnRegistrarUsuario.setForeground(new java.awt.Color(204, 204, 204));
        labelDesarrolladoPorEnRegistrarUsuario.setText("Desarrollado por :");

        labelNombreDesarrolladorEnRegistrarUsuario.setForeground(new java.awt.Color(204, 204, 204));
        labelNombreDesarrolladorEnRegistrarUsuario.setText("Juan Carlos Estevez Vargas");

        labelTituloVersionEnRegistrarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTituloVersionEnRegistrarUsuario.setForeground(new java.awt.Color(204, 204, 204));
        labelTituloVersionEnRegistrarUsuario.setText("Versión :");

        labelNumeroVersionEnRegistrarUsuario.setForeground(new java.awt.Color(204, 204, 204));
        labelNumeroVersionEnRegistrarUsuario.setText("1.0 ");

        labelContactoEnRegistrarUsuario.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelContactoEnRegistrarUsuario.setForeground(new java.awt.Color(204, 204, 204));
        labelContactoEnRegistrarUsuario.setText("Contacto :");

        labelEmailDesarrolladorRegistrarUsuario.setForeground(new java.awt.Color(204, 204, 204));
        labelEmailDesarrolladorRegistrarUsuario.setText("juank2001estevez@gmail.com");

        javax.swing.GroupLayout FondoLayout = new javax.swing.GroupLayout(Fondo);
        Fondo.setLayout(FondoLayout);
        FondoLayout.setHorizontalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(labelTituloRegistrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelContactoEnRegistrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelDesarrolladoPorEnRegistrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNombreDesarrolladorEnRegistrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addComponent(labelTituloVersionEnRegistrarUsuario)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelNumeroVersionEnRegistrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelLogo)
                    .addComponent(labelEmailDesarrolladorRegistrarUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(165, Short.MAX_VALUE))
        );
        FondoLayout.setVerticalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(labelLogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(labelTituloRegistrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(labelDesarrolladoPorEnRegistrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelNombreDesarrolladorEnRegistrarUsuario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTituloVersionEnRegistrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNumeroVersionEnRegistrarUsuario))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelContactoEnRegistrarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelEmailDesarrolladorRegistrarUsuario)
                .addGap(26, 26, 26))
        );

        getContentPane().add(Fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 430));

        imagenFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/login.jpg"))); // NOI18N
        getContentPane().add(imagenFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 210, 430));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarseActionPerformed
        try {
            this.validar();
        } catch (SQLException ex) {
            Logger.getLogger(RegistroUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnRegistrarseActionPerformed

    private void cmbRolUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbRolUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbRolUsuarioActionPerformed

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
            java.util.logging.Logger.getLogger(RegistroUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new RegistroUsuarios().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Fondo;
    private javax.swing.JButton btnRegistrarse;
    private javax.swing.JComboBox<String> cmbRolUsuario;
    private javax.swing.JLabel imagenFondo;
    private javax.swing.JLabel labelContactoEnRegistrarUsuario;
    private javax.swing.JLabel labelCorreoElectronicoUsuario;
    private javax.swing.JLabel labelDesarrolladoPorEnRegistrarUsuario;
    private javax.swing.JLabel labelEmailDesarrolladorRegistrarUsuario;
    private javax.swing.JLabel labelInicio;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelNombreDesarrolladorEnRegistrarUsuario;
    private javax.swing.JLabel labelNombreUsuario;
    private javax.swing.JLabel labelNumeroVersionEnRegistrarUsuario;
    private javax.swing.JLabel labelPasswordUsuario;
    private javax.swing.JLabel labelRlUsuario;
    private javax.swing.JLabel labelTituloRegistrarUsuario;
    private javax.swing.JLabel labelTituloVersionEnRegistrarUsuario;
    private javax.swing.JPanel panelLogin;
    private javax.swing.JTextField txtCorreoUsuario;
    private javax.swing.JTextField txtNombreUsuario;
    private javax.swing.JPasswordField txtPasswordUsuario;
    // End of variables declaration//GEN-END:variables
}
