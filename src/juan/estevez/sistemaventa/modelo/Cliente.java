package juan.estevez.sistemaventa.modelo;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Cliente {

    private int id, dni, telefono;
    private String nombre, direccion, razonSocial;

    public Cliente() {
    }

    public Cliente(int id, int dni, int telefono, String nombre, String direccion, String razonSocial) {
        this.id = id;
        this.dni = dni;
        this.telefono = telefono;
        this.nombre = nombre;
        this.direccion = direccion;
        this.razonSocial = razonSocial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

}
