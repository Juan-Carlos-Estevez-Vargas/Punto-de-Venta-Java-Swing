package juan.estevez.sistemaventa.modelo;

/**
 * Mapea el inicio de sesión (Usuario).
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Loginn {

    int id;
    String nombre;
    String correo;
    String password;

    public Loginn() {
    }

    public Loginn(int id, String nombre, String correo, String password) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

}
