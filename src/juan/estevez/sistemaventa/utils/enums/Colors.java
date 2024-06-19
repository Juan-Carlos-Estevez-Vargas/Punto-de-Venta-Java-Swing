package juan.estevez.sistemaventa.utils.enums;

import java.awt.Color;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public enum Colors {
    
    CADET_BLUE(122, 163, 177),
    LIGHT_GRAY(187, 187, 187);

    private final int red;
    private final int green;
    private final int blue;

    Colors(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color getColor() {
        return new Color(red, green, blue);
    }
    
}
