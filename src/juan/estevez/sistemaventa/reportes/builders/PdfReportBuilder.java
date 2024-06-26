package juan.estevez.sistemaventa.reportes.builders;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javax.swing.JTable;
import juan.estevez.sistemaventa.servicio.VentaServicio;

/**
 *
 * @author Juan Carlos Estevez Vargas
 */
public class PdfReportBuilder {

    private final Document document;
    private final FileOutputStream outputStream;
    private final VentaServicio ventaServicio = VentaServicio.getInstance();

    public PdfReportBuilder(FileOutputStream outputStream) throws DocumentException {
        this.outputStream = outputStream;
        this.document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
    }

    public PdfReportBuilder addEncabezado(String txtRutEmpresa, String txtNombreEmpresa, String txtTelefonoEmpresa,
            String txtDireccionEmpresa, String txtRazonSocialEmpresa) throws DocumentException, IOException, SQLException {
        Image img = Image.getInstance("src/juan/estevez/sistemaventa/img/logo.png");

        Paragraph fecha = new Paragraph();
        fecha.add(Chunk.NEWLINE);
        LocalDate date = LocalDate.now();
        fecha.add("Factura N° " + ventaServicio.obtenerIdVenta() + "\n" + "Fecha: " + date + "\n\n");

        PdfPTable encabezado = new PdfPTable(4);
        encabezado.setWidthPercentage(100);
        encabezado.getDefaultCell().setBorder(0);
        float[] columnaEncabezado = new float[]{20f, 40f, 70f, 40f};
        encabezado.setWidths(columnaEncabezado);
        encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);
        encabezado.addCell(img);
        encabezado.addCell("");
        encabezado.addCell(
                "Rut: " + txtRutEmpresa + "\nNombre: " + txtNombreEmpresa + "\nTeléfono: " + txtTelefonoEmpresa
                + "\nDirección: " + txtDireccionEmpresa + "\nRazón Social: " + txtRazonSocialEmpresa);
        encabezado.addCell(fecha);
        document.add(encabezado);
        return this;
    }

    public PdfReportBuilder addDatosCliente(String txtDniRutVenta, String txtNombreClienteVenta,
            String txtTelefonoClienteVenta, String txtDireccionClienteVenta) throws DocumentException {
        Paragraph paragraphCliente = new Paragraph();
        paragraphCliente.add(Chunk.NEWLINE);
        paragraphCliente.add("Datos del cliente \n\n");
        document.add(paragraphCliente);
        Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);

        PdfPTable tablaClientes = new PdfPTable(4);
        tablaClientes.setWidthPercentage(100);
        tablaClientes.getDefaultCell().setBorder(0);
        float[] columnaCliente = new float[]{20f, 50f, 30f, 40f};
        tablaClientes.setWidths(columnaCliente);
        tablaClientes.setHorizontalAlignment(Element.ALIGN_LEFT);

        tablaClientes.addCell(createCell("DNI/RUT", negrita));
        tablaClientes.addCell(createCell("Nombre", negrita));
        tablaClientes.addCell(createCell("Teléfono", negrita));
        tablaClientes.addCell(createCell("Dirección", negrita));

        tablaClientes.addCell(createCell(txtDniRutVenta));
        tablaClientes.addCell(createCell(txtNombreClienteVenta));
        tablaClientes.addCell(createCell(txtTelefonoClienteVenta));
        tablaClientes.addCell(createCell(txtDireccionClienteVenta));
        document.add(tablaClientes);
        return this;
    }

    public PdfReportBuilder addProductos(JTable tableVenta) throws DocumentException {
        PdfPTable tablaProductos = new PdfPTable(4);
        tablaProductos.setWidthPercentage(100);
        tablaProductos.getDefaultCell().setBorder(0);
        float[] columnaProducto = new float[]{20f, 50f, 25f, 30f};
        tablaProductos.setWidths(columnaProducto);
        tablaProductos.setHorizontalAlignment(Element.ALIGN_LEFT);

        Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);

        tablaProductos.addCell(createCell("Cantidad", negrita, BaseColor.GRAY));
        tablaProductos.addCell(createCell("Descripción", negrita, BaseColor.GRAY));
        tablaProductos.addCell(createCell("Precio U.", negrita, BaseColor.GRAY));
        tablaProductos.addCell(createCell("Precio T.", negrita, BaseColor.GRAY));

        for (int i = 0; i < tableVenta.getRowCount(); i++) {
            tablaProductos.addCell(createCell(tableVenta.getValueAt(i, 2).toString()));
            tablaProductos.addCell(createCell(tableVenta.getValueAt(i, 1).toString()));
            tablaProductos.addCell(createCell(tableVenta.getValueAt(i, 3).toString()));
            tablaProductos.addCell(createCell(tableVenta.getValueAt(i, 4).toString()));
        }

        document.add(tablaProductos);
        return this;
    }

    public PdfReportBuilder addTotalPagar(double totalPagar) throws DocumentException {
        Paragraph info = new Paragraph();
        info.add(Chunk.NEWLINE);
        info.add("Total a Pagar $" + totalPagar);
        info.setAlignment(Element.ALIGN_RIGHT);
        document.add(info);
        return this;
    }

    public PdfReportBuilder addFirma() throws DocumentException {
        Paragraph firma = new Paragraph();
        firma.add(Chunk.NEWLINE);
        firma.add("Cancelación y Firma\n\n");
        firma.add("_______________________");
        firma.setAlignment(Element.ALIGN_CENTER);
        document.add(firma);
        return this;
    }

    public PdfReportBuilder addMensaje() throws DocumentException {
        Paragraph mensaje = new Paragraph();
        mensaje.add(Chunk.NEWLINE);
        mensaje.add("Gracias por su compra");
        mensaje.setAlignment(Element.ALIGN_CENTER);
        document.add(mensaje);
        return this;
    }

    public void build() throws DocumentException, IOException {
        document.close();
        outputStream.close();
    }

    private PdfPCell createCell(String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content));
        cell.setBorder(0);
        return cell;
    }

    private PdfPCell createCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorder(0);
        return cell;
    }

    private PdfPCell createCell(String content, Font font, BaseColor backgroundColor) {
        PdfPCell cell = createCell(content, font);
        cell.setBackgroundColor(backgroundColor);
        return cell;
    }

}
