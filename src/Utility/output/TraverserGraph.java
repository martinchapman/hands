package Utility.output;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

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

	/**
	 * 
	 */
	protected ArrayList<Color> colours = new ArrayList<Color>();
    
	public TraverserGraph(String title) {
		
		super(title);
		
	    colours.add(new Color(0, 0, 0));
	    
	    colours.add(new Color(105, 105, 105));
	    
	    colours.add(new Color(128, 128, 128));
	    
	    //colours.add(new Color(169, 169, 169));
	    
	    //colours.add(new Color(192, 192, 192));
	    
	    //colours.add(new Color(211, 211, 211));
	    
	    //colours.add(new Color(220, 220, 220));
	    
	    //colours.add(new Color(112, 128, 144));
	    
	    //colours.add(new Color(47, 79, 79));
	    
	}
	
	protected JFreeChart chart;
	
	public void exportChartAsEPS(String filePath) {
		
		Utils.exportAsEPS(filePath, chart, 800, 500);
		
	}
	
	public void windowClosing(final WindowEvent evt){
		
		if ( evt.getWindow() == this ) {
		
			dispose();

		}
	
	}
	
	public abstract void createChart(String title, String xlabel, String ylabel);

	
}
