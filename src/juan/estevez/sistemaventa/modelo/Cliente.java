package juan.estevez.sistemaventa.modelo;

import lombok.Data;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
@Data
public class Cliente {

    private int id;
    private long telefono;
    private long dni;
    private String nombre;
    private String razonSocial;
    private String direccion;

}
