package Utility.output;

import java.awt.event.WindowEvent;

import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;

import Utility.Utils;

/**
 * @author Martin
 *
 */
public abstract class TraverserGraph extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TraverserGraph(String title) {
		
		super(title);
		
		
		
	}
	
	protected JFreeChart chart;
	
	public void exportChartAsEPS(String filePath) {
		
		Utils.exportAsEPS(filePath, chart, 1300, 1200);
		
	}
	
	public void windowClosing(final WindowEvent evt){
		
		if ( evt.getWindow() == this ) {
		
			dispose();

		}
	
	}
	
	public abstract void createChart(String title, String xlabel, String ylabel);

	
}
