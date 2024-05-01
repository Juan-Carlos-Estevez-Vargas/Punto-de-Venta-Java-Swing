package juan.estevez.sistemaventa.modelo;

import lombok.Data;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
@Data
public class Proveedor {

    private int id;
    private long rut;
    private long telefono;
    private String nombre;
    private String direccion;
    private String razonSocial;

}
