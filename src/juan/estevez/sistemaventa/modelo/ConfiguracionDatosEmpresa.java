package juan.estevez.sistemaventa.modelo;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ConfiguracionDatosEmpresa {

    private int id;
    private long rut;
    private long telefono;
    private String nombre;
    private String direccion;
    private String razonSocial;

    public ConfiguracionDatosEmpresa() {
    }

    public ConfiguracionDatosEmpresa(int id, long rut, long telefono, String nombre, String direccion, String razonSocial) {
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

    public long getRut() {
        return rut;
    }

    public void setRut(long rut) {
        this.rut = rut;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
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
