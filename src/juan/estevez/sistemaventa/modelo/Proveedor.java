package juan.estevez.sistemaventa.modelo;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Proveedor {

    private int id, rut, telefono;
    private String nombre, direccion, razonSocial;

    public Proveedor() {
    }

    public Proveedor(int id, int rut, int telefono, String nombre, String direccion, String razonSocial) {
        this.id = id;
        this.rut = rut;
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

    public int getRut() {
        return rut;
    }

    public void setRut(int rut) {
        this.rut = rut;
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
