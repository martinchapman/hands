package Utility.output;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


/**
 * @author Martin
 *
 */
public class LineGraph extends TraverserGraph {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private XYSeriesCollection dataset;
	
    /**
     * @param title
     */
    public LineGraph(String title) {
    	
        super(title);
        
        dataset = new XYSeriesCollection();
        
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

    }
    
    /**
     * @param title
     * @param xlabel
     * @param ylabel
     */
    public void createChart(String title, String xlabel, String ylabel) {

    	 // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            title,      			  // chart title
            xlabel,                   // x axis label
            ylabel,                   // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        chart.setBackgroundPaint(Color.white);

        final XYPlot plot = chart.getXYPlot();
        
        plot.setBackgroundPaint(Color.lightGray);
  
        plot.setDomainGridlinePaint(Color.white);
        
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        
        renderer.setSeriesLinesVisible(0, true);
        
        renderer.setSeriesShapesVisible(1, true);
        
        plot.setRenderer(renderer);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        
        setContentPane(chartPanel);

    }
   
    /**
     * @param title
     * @param data
     */
    public void addDataset(String title, ArrayList<Double> data) {

        XYSeries s = new XYSeries(title);
        
        int gameNumber = 0;
        
        for (Double value : data) {
        	
        	s.add(gameNumber, value);
        	
        	gameNumber++;
        	
        }
        
        dataset.addSeries(s);

    }

}
