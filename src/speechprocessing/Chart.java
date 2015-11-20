package speechprocessing;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Chart extends ApplicationFrame {

    public Chart(String applicationTitle, String chartTitle, int start, int stop, int type) {
        super(applicationTitle);
        //type 1 :error, type 2 : wave file type 3: coefficient
        DefaultCategoryDataset dataset
                = new DefaultCategoryDataset();
        if (type == 1) {
            dataset = (DefaultCategoryDataset) createErrorDataset(start, stop);
        } else if (type == 2) {
            dataset = (DefaultCategoryDataset) createWavFileDataset(start, stop);
        } else if (type == 3) {
            dataset = (DefaultCategoryDataset) createCoefficientDataset(start, stop);

        }
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Time",
                "Value",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }

    private CategoryDataset createCoefficientDataset(int start, int stop) {
        final DefaultCategoryDataset dataset
                = new DefaultCategoryDataset();

        int col = SpeechProcessing.P;
        String mytitle = "coefficient value" + "[" + String.valueOf(start) + "," + String.valueOf(stop) + "]";

        for (int i = start; i <= stop; i++) {
            for (int j = 0; j < col; j++) {
                dataset.addValue(SpeechProcessing.Coefficient[i][j], mytitle, String.valueOf(i * col + j));
            }
        }
        return dataset;
    }

    private CategoryDataset createErrorDataset(int start, int stop) {
        final DefaultCategoryDataset dataset
                = new DefaultCategoryDataset();

        int col = SpeechProcessing.Error[0].length;
        String mytitle = "error value" + "[" + String.valueOf(start) + "," + String.valueOf(stop) + "]";
        for (int i = start; i <= stop; i++) {
            for (int j = 0; j < col; j++) {
                dataset.addValue(SpeechProcessing.Error[i][j], mytitle, String.valueOf(i * col + j));
            }
        }
        return dataset;
    }

    private CategoryDataset createWavFileDataset(int start, int stop) {
        final DefaultCategoryDataset dataset
                = new DefaultCategoryDataset();
        String mytitle = "wav value" + "[" + String.valueOf(start) + "," + String.valueOf(stop) + "]";

        int col = SpeechProcessing.Error[0].length;
        for (int i = start; i <= stop; i++) {
            for (int j = 0; j < col; j++) {
                dataset.addValue((double) SpeechProcessing.WavValue[i * col + j], mytitle, String.valueOf(i * col + j));
            }
        }
        return dataset;
    }

    public static void main(String[] args) {
        SpeechProcessing spe = new SpeechProcessing();
        spe.extractWavFile(SpeechProcessing.fName);
        spe.readWavFile();
        Chart chart = new Chart("Car Usage Statistics", "Which car do you like?", 100, 101, 2);
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}
