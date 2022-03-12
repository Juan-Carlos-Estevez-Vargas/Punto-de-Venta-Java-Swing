package juan.estevez.sistemaventa.modelo;

/**
 *
 * @author User
 */
public class Usuario {

    private String correo, password, nombre, rol;

    public Usuario() {
    }

    public Usuario(String correo, String password, String nombre, String rol) {
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

}
