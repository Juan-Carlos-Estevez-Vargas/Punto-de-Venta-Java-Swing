package juan.estevez.sistemaventa.reportes;

import java.awt.Desktop;
import java.io.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import juan.estevez.sistemaventa.utils.Conexion;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase que genera reportes en formato Excel y PDF. Se genera un reporte de
 * productos con informaci�n de la base de datos. Los reportes se guardan en el
 * directorio de documentos del usuario y se abren autom�ticamente. Utiliza las
 * bibliotecas Apache POI y iTextPDF. Requiere la clase de conexi�n a la base de
 * datos "Conexion".
 *
 * @author Juan Carlos Estevez Vargas
 */
public class Excel {

    /**
     * Genera un reporte en formato Excel con informaci�n de productos y lo
     * guarda en un archivo.El reporte incluye el c�digo, descripci�n, precio y
     * stock de los productos.El archivo generado se guarda en la carpeta
     * "Documents" del directorio de inicio del usuario. Se utiliza la
     * biblioteca Apache POI para manipular el archivo Excel. Se muestra una
     * ventana de di�logo con un mensaje de confirmaci�n despu�s de generar el
     * reporte. Si ocurren errores durante la generaci�n del reporte, se
     * registran en el registro de eventos.
     *
     */
    public static void generarReporte() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Productos");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            createTitleRow(sheet, headerStyle);
            createHeaderRow(sheet, headerStyle);

            Connection con = Conexion.conectar();
            PreparedStatement ps = con.prepareStatement("SELECT CODIGO, DESCRIPCION, PRECIO, STOCK FROM PRODUCTO");
            ResultSet rs = ps.executeQuery();

            int rowNum = 5;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                fillDataRow(row, rs, dataStyle);
            }

            autosizeColumns(sheet);

            String fileName = "productos.xlsx";
            String homeDir = System.getProperty("user.home");
            File file = new File(homeDir, "Documents/" + fileName);

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

            Desktop.getDesktop().open(file);
            JOptionPane.showMessageDialog(null, "Reporte generado");

        } catch (IOException | SQLException ex) {
            Logger.getLogger(Excel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        return style;
    }

    /**
     * Crea y devuelve un estilo de celda para las celdas de encabezado en el
     * archivo Excel. El estilo incluye un fondo de color azul claro, bordes
     * delgados y texto en negrita de color blanco.
     *
     * @param workbook el libro de trabajo de Excel al que se aplicar� el
     * estilo.
     * @return el estilo de celda creado para las celdas de encabezado.
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);

        return style;
    }

    /**
     * Crea una fila de t�tulo en la hoja de c�lculo especificada y establece el
     * estilo de celda proporcionado. El t�tulo se fusiona en un rango de celdas
     * desde la columna 1 hasta la columna 3.
     *
     * @param sheet la hoja de c�lculo de Excel en la que se crear� la fila de
     * t�tulo.
     * @param style el estilo de celda a aplicar a la celda de t�tulo.
     */
    private static void createTitleRow(Sheet sheet, CellStyle style) {
        Row titleRow = sheet.createRow(1);
        Cell titleCell = titleRow.createCell(1);
        titleCell.setCellStyle(style);
        titleCell.setCellValue("Reporte de Productos");

        sheet.addMergedRegion(new CellRangeAddress(1, 2, 1, 3));
    }

    /**
     * Crea una fila de encabezado en la hoja de c�lculo especificada y
     * establece el estilo de celda proporcionado. Los encabezados de columna se
     * obtienen del arreglo de cadenas proporcionado.
     *
     * @param sheet la hoja de c�lculo de Excel en la que se crear� la fila de
     * encabezado.
     * @param style el estilo de celda a aplicar a las celdas de encabezado.
     */
    private static void createHeaderRow(Sheet sheet, CellStyle style) {
        String[] headers = new String[]{"C�digo", "Nombre", "Precio", "Existencia"};
        Row headerRow = sheet.createRow(4);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }
    }

    /**
     * Rellena una fila de datos en la hoja de c�lculo especificada con los
     * valores obtenidos de un ResultSet. Cada columna del ResultSet se asigna a
     * una celda en la fila de datos, utilizando el estilo de celda
     * proporcionado.
     *
     * @param row la fila de la hoja de c�lculo donde se agregar�n los datos.
     * @param rs el ResultSet que contiene los valores de las columnas.
     * @param style el estilo de celda a aplicar a las celdas de datos.
     * @throws SQLException si ocurre alg�n error al acceder a los datos del
     * ResultSet.
     */
    private static void fillDataRow(Row row, ResultSet rs, CellStyle style) throws SQLException {
        int numCols = rs.getMetaData().getColumnCount();

        for (int i = 0; i < numCols; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(rs.getString(i + 1));
        }
    }

    /**
     * Ajusta autom�ticamente el ancho de las columnas en la hoja de c�lculo
     * para que se ajusten al contenido. El m�todo recorre todas las columnas de
     * la fila de encabezados (fila 4) y ajusta el ancho de cada columna para
     * que se ajuste al contenido m�s ancho presente en esa columna.
     *
     * @param sheet la hoja de c�lculo en la que se ajustar�n las columnas.
     */
    private static void autosizeColumns(Sheet sheet) {
        for (int i = 0; i < sheet.getRow(4).getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

}
