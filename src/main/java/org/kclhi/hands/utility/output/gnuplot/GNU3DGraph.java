package org.kclhi.hands.utility.output.gnuplot;
import java.util.ArrayList;

import org.kclhi.hands.utility.Utils;

import com.panayotis.gnuplot.dataset.DataSet;
import com.panayotis.gnuplot.dataset.Point;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.Style;

/**
 * 
 * @author Martin
 *
 */
public class GNU3DGraph extends GNUGraph {

	String[][] d;
	
    public GNU3DGraph(String title, boolean textBased) {
    	
    	super(title, "9", textBased);
    	
    	p.newGraph3D();
    	
    }
   
    public void setZAxisLabel(String label) {
    	
    	p.getAxis("z").setLabel(label);
    	
    }
    
    /**
     * @param title
     * @param data
     */
    public void addDataset(String title, ArrayList<ArrayList<Double>> data) {

    	if ( data.get(0).size() == 0 || data.get(0).get(0) == null ) {
    	
    		Utils.talk(toString(), "No data to plot.");
    		
    		return;
    	
    	}
    	
    	d = new String[(data.size() * data.get(0).size()) + data.size()][3];
        
        int entryNumber = 0;
        
    	int xNumber = 0;
    	
        for (int i = 0; i < data.size(); i++) {
        	
        	int zNumber = 0;
        	
        	for (int j = 0; j < data.get(i).size(); j++) {
        		
        		d[entryNumber][0] = "" + xNumber;
        		
            	d[entryNumber][1] = "" + zNumber;
            	
            	d[entryNumber][2] = "" + data.get(i).get(j);
            	
            	zNumber++;
            	
            	entryNumber++;
            	
        	}
        	
        	d[entryNumber][0] = "";
        	
        	d[entryNumber][1] = "";
        	
        	d[entryNumber][2] = "";
        	
        	entryNumber++;
        	
        	xNumber++;
        	
        }
        
        AbstractPlot originalPlot = new DataSetPlot( new DataSet() {

			/* (non-Javadoc)
			 * @see com.panayotis.gnuplot.dataset.DataSet#getDimensions()
			 */
			@Override
			public int getDimensions() {
				
				return 3;
				
			}

			/* (non-Javadoc)
			 * @see com.panayotis.gnuplot.dataset.DataSet#getPointValue(int, int)
			 */
			@Override
			public String getPointValue(int arg0, int arg1) {
				
				return d[arg0][arg1];
				
			}

			/* (non-Javadoc)
			 * @see com.panayotis.gnuplot.dataset.DataSet#size()
			 */
			@Override
			public int size() {
				
				return d.length;
				
			}
        	
        });
        
        originalPlot.setTitle("");
	    
	    p.addPlot(originalPlot);

    }

	@Override
	public void styleGraph() {
		
		super.styleGraph();
		
		System.out.println(this + " " + FONT_SIZE);
		
		for ( int i = 0; i < p.getPlots().size(); i++ ) {
			
	    	PlotStyle stl = ((AbstractPlot) p.getPlots().get(i)).getPlotStyle();
	    	
	    	stl.setStyle(Style.PM3D);
	    	
    	}
		
		p.set("xtics", "font \"" + FONT_TYPE + "," + FONT_SIZE + "\" nomirror");
		
		p.set("ztics", "font \"" + FONT_TYPE + "," + FONT_SIZE + "\" nomirror");
		
		p.set("zlabel", "font \"" + FONT_TYPE + "," + FONT_SIZE + "\"");
		
		p.set("ylabel", "font \"" + FONT_TYPE + "," + FONT_SIZE + "\"  offset 4,0");
		
		p.set("xlabel", "font \"" + FONT_TYPE + "," + FONT_SIZE + "\" offset 1,-1");
		
		p.getParameters().getPostInit().add("unset colorbox");
    	
	}

}
