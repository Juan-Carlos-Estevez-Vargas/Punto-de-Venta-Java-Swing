package juan.estevez.sistemaventa.modelo;

/**
 *
 * @author Juan Carlos Estevez Vargas.
 */
public class Usuario {

    private int id;
    private String correo, password, nombre, rol;

    public Usuario() {
    }

    public Usuario(int id, String correo, String password, String nombre, String rol) {
        this.id = id;
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getCorreo() {
        return correo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
