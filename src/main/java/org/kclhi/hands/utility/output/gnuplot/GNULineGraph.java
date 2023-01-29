package org.kclhi.hands.utility.output.gnuplot;
import java.util.ArrayList;

import org.kclhi.hands.utility.Utils;

import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

/**
 * 
 * @author Martin
 *
 */
public class GNULineGraph extends GNUGraph {

	/**
	 * 
	 */
	private double[][] d;
	
	/**
	 * 
	 */
	private boolean startsAtOne;
	
	/**
	 * 
	 */
	public void startsAtOne() {
		
		startsAtOne = true;
		
	}
	
	/**
	 * @return
	 */
	public void startsAtZero() {
		
		startsAtOne= false;
		
	}
	
	
    /**
     * @param title
     */
    public GNULineGraph(String title, boolean textBased) {
    	
    	super(title, textBased);
    	
    	startsAtOne = false;
    	
    }
   
    /**
     * @param title
     * @param data
     */
    public void addDataset(String title, ArrayList<Double> data) {

    	if ( data.size() == 0 || data.get(0) == null ) {
    		
    		Utils.talk(toString(), "No data to plot.");
    		
    		return;
    	
    	}
    	
    	d = new double[data.size()][2];
        
    	int xValue = 0;
    	
        for (Double value : data) {
        	
        	if ( startsAtOne ) {
        	
        		d[xValue][0] = xValue + 1;
        	
        	} else {
        		
        		d[xValue][0] = xValue;
        		
        	}
        	
        	d[xValue][1] = value;
        	
        	xValue++;
        	
        }
        
        AbstractPlot originalPlot = new DataSetPlot(d);
        
        originalPlot.setTitle(title);
        
        p.addPlot(originalPlot);

    }

    /**
     * @param pointTypeOrder
     * @param point
     * @param newPosition
     */
    private void swapPoints(ArrayList<Integer> pointTypeOrder, int point, int newPosition) {
    	
    	int retrievedPoint = pointTypeOrder.remove(pointTypeOrder.indexOf(point));
    	
    	pointTypeOrder.add(newPosition, retrievedPoint);
    	
    }
    
	/* (non-Javadoc)
	 * @see Utility.output.gnuplot.GNUGraph#styleGraph()
	 */
	@Override
	public void styleGraph() {
		
		super.styleGraph();
		
		//if (startsAtOne) p.getParameters().add("set xrange [1:100]");
		
		ArrayList<Integer> pointTypeOrder = new ArrayList<Integer>();
		
		for ( int i = 1; i < 20; i++ ) {
			
			pointTypeOrder.add(i);
			
		}
		
		swapPoints(pointTypeOrder, 13, 0);
		
		swapPoints(pointTypeOrder, 9, 0);
		
		swapPoints(pointTypeOrder, 7, 0);
		
		swapPoints(pointTypeOrder, 5, 0);
		
		swapPoints(pointTypeOrder, 12, 0);
		
		swapPoints(pointTypeOrder, 6, 0);
		
		swapPoints(pointTypeOrder, 8, 0);
		
		swapPoints(pointTypeOrder, 4, 0);
		
		swapPoints(pointTypeOrder, 2, 0);
		
		
		for ( int i = 0; i < p.getPlots().size(); i++ ) {
			
	    	PlotStyle stl = ((AbstractPlot) p.getPlots().get(i)).getPlotStyle();
	    	
	    	stl.setStyle(Style.LINESPOINTS);
	    	
	    	stl.setPointType(pointTypeOrder.get(i));
	    	
		}
    	
	}

}
