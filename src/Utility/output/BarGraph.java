package Utility.output;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
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

	/* (non-Javadoc)
	 * @see Utility.output.TraverserGraph#createChart(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createChart(String title, String xlabel, String ylabel) {
		
		// create the chart...
        chart = ChartFactory.createBarChart(
            title,         // chart title
            xlabel,               // domain axis label
            ylabel,                  // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            false,                     // include legend
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
	public void addBar(Double value, String traverser, String attribute) {
		
		dataset.addValue(value, attribute, traverser);
			
	}

}
