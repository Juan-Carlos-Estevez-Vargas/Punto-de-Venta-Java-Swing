package juan.estevez.sistemaventa.modelo;

import lombok.Data;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
@Data
public class Producto {

    private int id;
    private int stock;
    private String codigo;
    private String nombre;
    private String proveedor;
    private double precio;
    
}
