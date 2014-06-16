package Utility.output;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;

/**
 * @author Martin
 *
 */
public class BarGraph extends TraverserGraph {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private DefaultCategoryDataset dataset;
	
	/**
	 * @param title
	 */
	public BarGraph(String title) {
		
		super(title);
		
		dataset = new DefaultCategoryDataset();
		
	}

	/* (non-Javadoc)
	 * @see Utility.output.TraverserGraph#createChart(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createChart(String title, String xlabel, String ylabel) {
		
		// create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
            title,         // chart title
            xlabel,               // domain axis label
            ylabel,                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                     // include legend
            true,                     // tooltips?
            false                     // URLs?
        );

        chart.setBackgroundPaint(Color.white);

        final CategoryPlot plot = chart.getCategoryPlot();
        
        plot.setBackgroundPaint(Color.lightGray);
        
        plot.setDomainGridlinePaint(Color.white);
        
        plot.setRangeGridlinePaint(Color.white);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();

        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        final BarRenderer renderer = (BarRenderer) plot.getRenderer();

        renderer.setDrawBarOutline(false);
        
        final GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, Color.lightGray
        );
        
        final GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, Color.lightGray
        );
        
        final GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, Color.lightGray
        );
        
        renderer.setSeriesPaint(0, gp0);
        
        renderer.setSeriesPaint(1, gp1);
        
        renderer.setSeriesPaint(2, gp2);

        final CategoryAxis domainAxis = plot.getDomainAxis();
       
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setPreferredSize(new Dimension(500, 270));
        
        setContentPane(chartPanel);
        
	}

	/**
	 * @param traverser
	 * @param value
	 * @param attribute
	 */
	public void addBar(String traverser, Double value, String attribute) {
		
		dataset.addValue(value, traverser, attribute);
			
	}

}
