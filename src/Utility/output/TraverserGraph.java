package Utility.output;

import java.awt.event.WindowEvent;

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
	
	public void windowClosing(final WindowEvent evt){
		
		if ( evt.getWindow() == this ) {
		
			dispose();

		}
	
	}
	
	public abstract void createChart(String title, String xlabel, String ylabel);

	
}
