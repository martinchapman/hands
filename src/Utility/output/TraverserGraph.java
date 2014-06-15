package Utility.output;

import java.util.ArrayList;

import org.jfree.ui.ApplicationFrame;

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
	
	public abstract void createChart(String title, String xlabel, String ylabel);

	public abstract void addDataset(String title, ArrayList<Double> data);
	
}
