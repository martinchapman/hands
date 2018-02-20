package utility.output;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Based on: http://www.java2s.com/Code/Java/Chart/JFreeChartBarChartDemo.htm
 * 
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

	public void createChart(String title, String xlabel, String ylabel) {
		
	      setContentPane(createChartPanel(title, xlabel, ylabel));
		
	}
	/* (non-Javadoc)
	 * @see Utility.output.TraverserGraph#createChart(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ChartPanel createChartPanel(String title, String xlabel, String ylabel) {
		
		// create the chart...
        chart = ChartFactory.createBarChart(
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
        
    	((BarRenderer)renderer).setBarPainter(new StandardBarPainter());
    	
        for ( int i = 0; i < dataset.getRowCount(); i++ ) {
        
        	//renderer.setSeriesPaint(i, colours.get(i));
        	
        }
        
        final CategoryAxis domainAxis = plot.getDomainAxis();
       
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        
        chartPanel.setPreferredSize(new Dimension(500, 270));
        
        return chartPanel;
        
	}

	/**
	 * @param value
	 * @param traverser
	 * @param attribute
	 */
	public void addBar(Double value, String traverser, String category) {
		
		dataset.addValue(value, category, traverser);
			
	}

}
