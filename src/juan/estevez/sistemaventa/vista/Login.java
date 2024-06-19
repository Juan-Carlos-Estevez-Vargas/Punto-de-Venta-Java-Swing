package juan.estevez.sistemaventa.vista;

import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import juan.estevez.sistemaventa.daos.LoginDAO;
import juan.estevez.sistemaventa.modelo.Loginn;
import juan.estevez.sistemaventa.utils.Utilitarios;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Login extends JFrame {
    
    private final transient ResourceBundle messages = ResourceBundle.getBundle("juan.estevez.sistemaventa.recursos.messages");
    
    private Loginn loginn = new Loginn();
    private final LoginDAO loginDAO = new LoginDAO();
    
    public Login() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * Se encarga de validar el correo y contraseña del login e iniciar sesión.
     */
    public void validar() {
        String correo = txtCorreo.getText();
        String password = String.valueOf(txtPassword.getPassword());
        
        if (!correo.isEmpty() && !password.isEmpty()) {
            try {
                loginn = loginDAO.login(correo, password);
                
                if (loginn.getCorreo() != null && loginn.getPassword() != null) {
                    iniciarSistema();
                } else {
                    Utilitarios.mostrarMensajeAdvertencia(messages.getString("credencialesErroneas"));
                }
            } catch (SQLException ex) {
                Utilitarios.mostrarMensajeError(messages.getString("errorConexionDB"));
            }
        } else {
            Utilitarios.mostrarMensajeAdvertencia(messages.getString("mensajeRegistroInvalido"));
        }
    }
    
    private void iniciarSistema() throws SQLException {
        Sistema sistema = new Sistema(loginn);
        sistema.setVisible(true);
        dispose();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLogin = new javax.swing.JPanel();
        labelInicio = new javax.swing.JLabel();
        labelCorreoElectronico = new javax.swing.JLabel();
        txtCorreo = new javax.swing.JTextField();
        labelPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnRegistro = new javax.swing.JButton();
        panelFacebook = new javax.swing.JPanel();
        labelFacebook = new javax.swing.JLabel();
        panelInstagram = new javax.swing.JPanel();
        labelInstagram = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        Fondo = new javax.swing.JPanel();
        labelLogo = new javax.swing.JLabel();
        labelTituloLogin = new javax.swing.JLabel();
        labelDesarrolladoPorLogin = new javax.swing.JLabel();
        labelNombreDesarrolladorLogin = new javax.swing.JLabel();
        labelTituloVersionLogin = new javax.swing.JLabel();
        labelNumeroVersionLogin = new javax.swing.JLabel();
        labelContactoLogin = new javax.swing.JLabel();
        labelEmailDesarrolladorLogin = new javax.swing.JLabel();
        imagenFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelLogin.setBackground(new java.awt.Color(255, 255, 255));

        labelInicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/iniciar.png"))); // NOI18N

        labelCorreoElectronico.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelCorreoElectronico.setForeground(new java.awt.Color(0, 51, 255));
        labelCorreoElectronico.setText("Correo Electrónico");

        txtCorreo.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        labelPassword.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelPassword.setForeground(new java.awt.Color(0, 51, 255));
        labelPassword.setText("Password");

        txtPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        btnRegistro.setBackground(new java.awt.Color(0, 102, 255));
        btnRegistro.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnRegistro.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistro.setText("Registrarse");
        btnRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistroActionPerformed(evt);
            }
        });

        panelFacebook.setBackground(new java.awt.Color(51, 51, 51));

        labelFacebook.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelFacebook.setForeground(new java.awt.Color(255, 255, 255));
        labelFacebook.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFacebook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/facebook.png"))); // NOI18N
        labelFacebook.setText("Síguenos en ");
        labelFacebook.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout panelFacebookLayout = new javax.swing.GroupLayout(panelFacebook);
        panelFacebook.setLayout(panelFacebookLayout);
        panelFacebookLayout.setHorizontalGroup(
            panelFacebookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelFacebook, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
        );
        panelFacebookLayout.setVerticalGroup(
            panelFacebookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelFacebook, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
        );

        panelInstagram.setBackground(new java.awt.Color(255, 51, 255));

        labelInstagram.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelInstagram.setForeground(new java.awt.Color(255, 255, 255));
        labelInstagram.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelInstagram.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/instagram.png"))); // NOI18N
        labelInstagram.setText("Síguenos en");
        labelInstagram.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout panelInstagramLayout = new javax.swing.GroupLayout(panelInstagram);
        panelInstagram.setLayout(panelInstagramLayout);
        panelInstagramLayout.setHorizontalGroup(
            panelInstagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelInstagram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelInstagramLayout.setVerticalGroup(
            panelInstagramLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelInstagram, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
        );

        btnIniciar.setBackground(new java.awt.Color(0, 51, 255));
        btnIniciar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnIniciar.setForeground(new java.awt.Color(255, 255, 255));
        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLoginLayout = new javax.swing.GroupLayout(panelLogin);
        panelLogin.setLayout(panelLoginLayout);
        panelLoginLayout.setHorizontalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelPassword)
                    .addComponent(labelCorreoElectronico)
                    .addComponent(txtCorreo)
                    .addComponent(txtPassword)
                    .addComponent(panelFacebook, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelInstagram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                .addContainerGap(54, Short.MAX_VALUE)
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                        .addComponent(labelInicio)
                        .addGap(110, 110, 110))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                        .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRegistro)
                        .addGap(68, 68, 68))))
        );
        panelLoginLayout.setVerticalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(labelInicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelCorreoElectronico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelPassword)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addComponent(panelFacebook, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelInstagram, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        getContentPane().add(panelLogin, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 20, 320, 390));

        Fondo.setBackground(new java.awt.Color(102, 102, 102));

        labelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/logo.png"))); // NOI18N

        labelTituloLogin.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        labelTituloLogin.setForeground(new java.awt.Color(204, 204, 204));
        labelTituloLogin.setText("Punto de Venta");

        labelDesarrolladoPorLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelDesarrolladoPorLogin.setForeground(new java.awt.Color(204, 204, 204));
        labelDesarrolladoPorLogin.setText("Desarrollado por :");

        labelNombreDesarrolladorLogin.setForeground(new java.awt.Color(204, 204, 204));
        labelNombreDesarrolladorLogin.setText("Juan Carlos Estevez Vargas");

        labelTituloVersionLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTituloVersionLogin.setForeground(new java.awt.Color(204, 204, 204));
        labelTituloVersionLogin.setText("Versión :");

        labelNumeroVersionLogin.setForeground(new java.awt.Color(204, 204, 204));
        labelNumeroVersionLogin.setText("1.0 ");

        labelContactoLogin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelContactoLogin.setForeground(new java.awt.Color(204, 204, 204));
        labelContactoLogin.setText("Contacto :");

        labelEmailDesarrolladorLogin.setForeground(new java.awt.Color(204, 204, 204));
        labelEmailDesarrolladorLogin.setText("juank2001estevez@gmail.com");

        javax.swing.GroupLayout FondoLayout = new javax.swing.GroupLayout(Fondo);
        Fondo.setLayout(FondoLayout);
        FondoLayout.setHorizontalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(labelTituloLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(FondoLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelContactoLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(labelDesarrolladoPorLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelLogo)
                                .addComponent(labelNombreDesarrolladorLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(FondoLayout.createSequentialGroup()
                                    .addComponent(labelTituloVersionLogin)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(labelNumeroVersionLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(labelEmailDesarrolladorLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(134, Short.MAX_VALUE))
        );
        FondoLayout.setVerticalGroup(
            FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FondoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(labelLogo)
                .addGap(18, 18, 18)
                .addComponent(labelTituloLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(labelDesarrolladoPorLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelNombreDesarrolladorLogin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(FondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTituloVersionLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelNumeroVersionLogin))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelContactoLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelEmailDesarrolladorLogin)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        getContentPane().add(Fondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 430));

        imagenFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/juan/estevez/sistemaventa/img/login.jpg"))); // NOI18N
        getContentPane().add(imagenFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 0, 210, 430));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Inicia sesión en la aplicación.
     *
     * @param evt
     */
    private void btnRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistroActionPerformed
        new RegistroUsuarios().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegistroActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        this.validar();
    }//GEN-LAST:event_btnIniciarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Fondo;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnRegistro;
    private javax.swing.JLabel imagenFondo;
    private javax.swing.JLabel labelContactoLogin;
    private javax.swing.JLabel labelCorreoElectronico;
    private javax.swing.JLabel labelDesarrolladoPorLogin;
    private javax.swing.JLabel labelEmailDesarrolladorLogin;
    private javax.swing.JLabel labelFacebook;
    private javax.swing.JLabel labelInicio;
    private javax.swing.JLabel labelInstagram;
    private javax.swing.JLabel labelLogo;
    private javax.swing.JLabel labelNombreDesarrolladorLogin;
    private javax.swing.JLabel labelNumeroVersionLogin;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelTituloLogin;
    private javax.swing.JLabel labelTituloVersionLogin;
    private javax.swing.JPanel panelFacebook;
    private javax.swing.JPanel panelInstagram;
    private javax.swing.JPanel panelLogin;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}
