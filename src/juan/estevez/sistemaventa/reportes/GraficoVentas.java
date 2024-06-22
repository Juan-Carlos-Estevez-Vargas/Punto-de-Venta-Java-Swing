package juan.estevez.sistemaventa.reportes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import juan.estevez.sistemaventa.utils.Conexion;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Clase para generar gráficos de ventas utilizando la librería JFreeChart. Los gráficos se generan a partir de los datos de ventas filtrados por fecha. Requiere la librería JFreeChart en el classpath.
 *
 * @autor Juan Carlos Estevez Vargas
 */
public class GraficoVentas {

    private static final String SELECT_TOTAL_VENTAS_BY_FECHA = "SELECT TOTAL FROM VENTAS WHERE FECHA = ?";

    /**
     * Genera y muestra un gráfico de torta a partir de los datos de ventas filtrados por fecha.
     *
     * @param fecha la fecha utilizada para filtrar los datos de ventas.
     * @throws SQLException si ocurre un error al acceder a la base de datos.
     */
    public static void graficar(String fecha) throws SQLException {
        try (Connection con = Conexion.getInstance().getConnection(); PreparedStatement pst = con.prepareStatement(SELECT_TOTAL_VENTAS_BY_FECHA)) {

            pst.setString(1, fecha);
            try (ResultSet rs = pst.executeQuery()) {
                DefaultPieDataset dataset = new DefaultPieDataset();
                while (rs.next()) {
                    double total = rs.getDouble("TOTAL");
                    dataset.setValue("Ventas", total);
                }
                createAndShowChart(dataset);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al realizar el gráfico de ventas", e);
        }
    }

    /**
     * Crea y muestra el gráfico de torta utilizando los datos del dataset.
     *
     * @param dataset el dataset que contiene los datos del gráfico.
     */
    private static void createAndShowChart(DefaultPieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart("Reporte de Venta", dataset);
        ChartFrame frame = new ChartFrame("Total de ventas por día", chart);
        frame.setSize(1000, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
