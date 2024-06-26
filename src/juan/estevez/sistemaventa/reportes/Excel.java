package juan.estevez.sistemaventa.reportes;

import java.awt.Desktop;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import juan.estevez.sistemaventa.reportes.builders.ExcelBuilder;
import juan.estevez.sistemaventa.utils.Conexion;
import juan.estevez.sistemaventa.utils.Utilitarios;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Excel {

    private static final String SELECT_PRODUCTOS_SQL = "SELECT CODIGO, DESCRIPCION, PRECIO, STOCK FROM PRODUCTO";

    public static void generarReporte() {
        try (Connection con = Conexion.getInstance().getConnection(); 
                Workbook workbook = new XSSFWorkbook(); 
                PreparedStatement ps = con.prepareStatement(SELECT_PRODUCTOS_SQL); 
                ResultSet rs = ps.executeQuery()) {

            Sheet sheet = workbook.createSheet("Productos");
            ExcelBuilder builder = new ExcelBuilder(workbook, sheet);

            builder.createTitleRow("Reporte de Productos", 1, 2, 1, 3)
                    .createHeaderRow(new String[]{"Código", "Nombre", "Precio", "Existencia"}, 4)
                    .populateData(rs, 5)
                    .autosizeColumns(4);

            Path filePath = Paths.get(System.getProperty("user.home"), "Documents", "productos.xlsx");

            try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
                workbook.write(fileOut);
            }

            if (Files.exists(filePath)) {
                try {
                    Desktop.getDesktop().open(filePath.toFile());
                    Utilitarios.mostrarMensajeExito("Reporte generado correctamente");
                } catch (IOException ex) {
                    Utilitarios.mostrarMensajeError("No se pudo abrir el archivo: " + ex.getMessage());
                }
            } else {
                Utilitarios.mostrarMensajeError("El archivo no se encontró después de escribirlo.");
            }
        } catch (IOException | SQLException ex) {
            Utilitarios.mostrarErrorGenerico(ex);
        }
    }

}
