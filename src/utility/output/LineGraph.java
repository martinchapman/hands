package utility.output;
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
 * Based on: http://www.java2s.com/Code/Java/Chart/JFreeChartLineChartDemo6.htm
 * 
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
    
    /* (non-Javadoc)
     * @see Utility.output.TraverserGraph#createChartPanel(java.lang.String, java.lang.String, java.lang.String)
     */
    public void createChart(String title, String xlabel, String ylabel) {
    	
    	setContentPane(createChartPanel(title, xlabel, ylabel));
    	
    }
    
    /**
     * @param title
     * @param xlabel
     * @param ylabel
     */
    public ChartPanel createChartPanel(String title, String xlabel, String ylabel) {

    	 // create the chart...
        chart = ChartFactory.createXYLineChart(
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
        
        for ( int i = 0; i < dataset.getSeriesCount(); i++ ) {
            
        	//renderer.setSeriesPaint(i, colours.get(i%colours.size()));
        	
        }
        
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        
        return chartPanel;
        
    }
   
    /**
     * @param title
     * @param data
     */
    public void addDataset(String title, ArrayList<Double> data) {

        XYSeries s = new XYSeries(title);
        
        int xNumber = 0;
        
        for (Double value : data) {
        	
        	s.add(xNumber, value);
        	
        	xNumber++;
        	
        }
        
        dataset.addSeries(s);

    }

}
