package juan.estevez.sistemaventa.reportes.builders;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class ExcelBuilder {

    private final Workbook workbook;
    private final Sheet sheet;
    private final CellStyle headerStyle;
    private final CellStyle dataStyle;

    public ExcelBuilder(Workbook workbook, Sheet sheet) {
        this.workbook = workbook;
        this.sheet = sheet;
        this.headerStyle = createHeaderStyle(workbook);
        this.dataStyle = createDataStyle(workbook);
    }

    public ExcelBuilder createTitleRow(String title, int firstRow, int lastRow, int firstCol, int lastCol) {
        Row titleRow = sheet.createRow(firstRow);
        Cell titleCell = titleRow.createCell(firstCol);
        titleCell.setCellStyle(headerStyle);
        titleCell.setCellValue(title);
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
        return this;
    }

    public ExcelBuilder createHeaderRow(String[] headers, int rowNumber) {
        Row headerRow = sheet.createRow(rowNumber);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(headers[i]);
        }
        return this;
    }

    public ExcelBuilder populateData(ResultSet rs, int startRow) throws SQLException {
        int rowNum = startRow;
        while (rs.next()) {
            Row row = sheet.createRow(rowNum++);
            fillDataRow(row, rs, dataStyle);
        }
        return this;
    }

    public ExcelBuilder autosizeColumns(int headerRowNumber) {
        for (int i = 0; i < sheet.getRow(headerRowNumber).getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
        }
        return this;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
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

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        return style;
    }

    private void fillDataRow(Row row, ResultSet rs, CellStyle style) throws SQLException {
        int numCols = rs.getMetaData().getColumnCount();
        for (int i = 0; i < numCols; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(rs.getString(i + 1));
        }
    }
}
