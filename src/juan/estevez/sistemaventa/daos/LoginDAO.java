package juan.estevez.sistemaventa.daos;

import juan.estevez.sistemaventa.modelo.Loginn;
import juan.estevez.sistemaventa.utils.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO para operaciones relacionadas con el inicio de sesión.
 *
 * @author Juan Carlos Estevez Vargas
 */
public class LoginDAO {
    
    private static LoginDAO instance;
    private final Connection connection;

    private static final String GET_USUARIO_BY_CORREO_AND_PASSWORD_SQL = "SELECT * FROM USUARIO WHERE CORREO = ? AND PASSWORD = ?";
    private static final String INSERT_USUARIO_SQL = "INSERT INTO USUARIO (NOMBRE, CORREO, PASSWORD, ROL) VALUES (?,?,?,?)";

    public static LoginDAO getInstance() {
        return instance == null ?  new LoginDAO() : instance;
    }
    
    private LoginDAO() {
        this.connection = Conexion.getInstance().getConnection();
    }
    
    /**
     * Realiza el inicio de sesi�n de un usuario en la aplicaci�n.
     *
     * @param correo el correo electr�nico del usuario.
     * @param password la contrase�a del usuario.
     * @return un objeto Loginn que representa al usuario que inici� sesi�n, o null si las credenciales son inv�lidas.
     * @throws SQLException si ocurre un error al realizar el inicio de sesi�n.
     */
    public Loginn login(String correo, String password) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(GET_USUARIO_BY_CORREO_AND_PASSWORD_SQL)) {
            pst.setString(1, correo);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return crearLoginDesdeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error en el inicio de sesi�n de la aplicaci�n", e);
        }
        return null;
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param login el objeto Loginn que contiene la informaci�n del usuario a registrar.
     * @throws SQLException si ocurre un error al registrar el usuario.
     */
    public void registrarUsuario(Loginn login) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(INSERT_USUARIO_SQL)) {
            crearPreparedStatementDesdeLogin(pst, login);
            pst.execute();
        }
    }

    /**
     * Crea un objeto de tipo Loginn con los datos provenientes de la base de datos.
     *
     * @param rs ResulSet con la información de la base de datos.
     * @return objeto de tipo Loginn con los datos de inicio de sesión.
     * @throws SQLException en caso de error con la base de datos.
     */
    private Loginn crearLoginDesdeResultSet(ResultSet rs) throws SQLException {
        return Loginn.builder()
                .id(rs.getInt("ID"))
                .nombre(rs.getString("NOMBRE"))
                .correo(rs.getString("CORREO"))
                .password(rs.getString("PASSWORD"))
                .rol(rs.getString("ROL"))
                .build();
    }

    /**
     * Setea los datos del login al objeto encargado de persistirlos en la base de datos.
     *
     * @param pst objeto encargado de persistir la data en la base de datos.
     * @param login objeto con los datos a persistir.
     * @throws SQLException en caso de error con la base de datos.
     */
    private void crearPreparedStatementDesdeLogin(PreparedStatement pst, Loginn login) throws SQLException {
        pst.setString(1, login.getNombre());
        pst.setString(2, login.getCorreo());
        pst.setString(3, login.getPassword());
        pst.setString(4, login.getRol());
    }
}
