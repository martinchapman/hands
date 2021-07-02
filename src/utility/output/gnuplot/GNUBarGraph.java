package Utility.output.gnuplot;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;

import Utility.output.TraverserRecord;

import com.panayotis.gnuplot.dataset.DataSet;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.gnuplot.plot.Plot;
import com.panayotis.gnuplot.style.FillStyle;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.RgbPlotColor;
import com.panayotis.gnuplot.style.Style;

/**
 * 
 * @author Martin
 *
 */
public class GNUBarGraph extends GNUGraph {
	
	/**
	 * Whether to plot as a histogram, or as 
	 * series of boxes
	 */
	private final static boolean IS_HISTOGRAM = false;
	
	/**
	 * Whether to add P significance stars to bars 
	 * (only works in boxes model)
	 */
	private final static boolean ADD_LABELS = true;
	
	/**
	 * Whether bars with an empty pattern should 
	 * be used
	 */
	private final static boolean NO_EMPTY_BARS = true;
	
	/**
	 * @param title
	 */
	public GNUBarGraph(String title, boolean textBased) {
	
		super(title, textBased);
		
		barDatasets = new ArrayList<ArrayList<Entry<TraverserRecord, Double>>>();
	
	}

	/**
	 * 
	 */
	ArrayList<ArrayList<Entry<TraverserRecord, Double>>> barDatasets;
	
	/**
	 * @param traverserAndData
	 * @param category
	 */
	public void addBars(ArrayList<Entry<TraverserRecord, Double>> traverserAndData, String category) {
		
		addBars(traverserAndData, category, new Hashtable<TraverserRecord, String>());
		
	}
	
	/**
	 * @param value
	 * @param traverser
	 * @param attribute
	 */
	public void addBars(ArrayList<Entry<TraverserRecord, Double>> traverserAndData, String category, final Hashtable<TraverserRecord, String> traverserToSignificanceClass) {
		
		final ArrayList<Entry<TraverserRecord, Double>> barData = new ArrayList<Entry<TraverserRecord, Double>>(traverserAndData);
		
		final int existingBars = barDatasets.size();
		
		barDatasets.add(barData);
		
		AbstractPlot barPlot = new DataSetPlot( new DataSet() {

			/* (non-Javadoc)
			 * @see com.panayotis.gnuplot.dataset.DataSet#getDimensions()
			 */
			@Override
			public int getDimensions() {
				
				if ( IS_HISTOGRAM ) {
					
					return 1;
					
				} else {
					
					return 2;
					
				}
				
			}

			/* (non-Javadoc)
			 * @see com.panayotis.gnuplot.dataset.DataSet#getPointValue(int, int)
			 */
			@Override
			public String getPointValue(int arg0, int arg1) {
				
				if ( arg1 == 0 && !IS_HISTOGRAM ) return ( existingBars + ( barDatasets.size() * ( arg0 ) ) ) + "";
				
				return barData.get(arg0).getValue() + "";
				
			}

			/* (non-Javadoc)
			 * @see com.panayotis.gnuplot.dataset.DataSet#size()
			 */
			@Override
			public int size() {
		
				return barData.size();
				
			}
	        	
		});
		
		AbstractPlot labelPlot = new DataSetPlot( new DataSet() {

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
				
				if ( arg1 == 0 ) return ( existingBars + ( barDatasets.size() * ( arg0 ) ) ) + "";
				
				if ( arg1 == 2) {
					
					if ( traverserToSignificanceClass.containsKey(barData.get(arg0).getKey()) ) {
						
						return traverserToSignificanceClass.get(barData.get(arg0).getKey());
						
					}
					
				}
				
				// ~MDC: TODO move labels when negative to below bar 
				if ( barData.get(arg0).getValue() < 0 ) {
					
					return barData.get(arg0).getValue() + "";
					
				} else {
					
					return barData.get(arg0).getValue() + "";
					
				}
				
			}

			/* (non-Javadoc)
			 * @see com.panayotis.gnuplot.dataset.DataSet#size()
			 */
			@Override
			public int size() {
		
				return barData.size();
				
			}
	        	
		});
		 
	    barPlot.setTitle(category);
	    
	    labelPlot.setTitle("");
	    
	    p.addPlot(barPlot);
	    
	    if ( !IS_HISTOGRAM && ADD_LABELS ) p.addPlot(labelPlot);
			
	}

	/**
	 * 
	 */
	@Override
	public void styleGraph() {
		
		super.styleGraph();
		
		if ( IS_HISTOGRAM ) {
			
			p.set("boxwidth", "0.5");
			
			p.set("style", "fill solid border -1");
			
		} else {
		
			p.set("boxwidth", "0.4");
			
			p.set("style", "fill pattern border -1");
		}
		
		ArrayList<Integer> patternOrder = new ArrayList<Integer>();
		
		for ( int i = 0; i < 10; i++ ) {
			
			patternOrder.add(i);
			
		}
		
		if ( NO_EMPTY_BARS ) { 
			
			patternOrder.remove(0);
			
			patternOrder.remove(7);
			
		}
		
		int i = 0;
		
		for ( Plot plot : p.getPlots() ) {
			
			StringBuilder buf = new StringBuilder();
			
			plot.retrieveData(buf);
			
			if ( buf.toString().contains("*") ) continue;
			
	    	PlotStyle stl = ((AbstractPlot) p.getPlots().get(p.getPlots().indexOf(plot))).getPlotStyle();
	    	
    		FillStyle fill = new FillStyle();
	    	
	    	if ( IS_HISTOGRAM ) {
	    		
	    		stl.setStyle(Style.HISTOGRAMS);
	    		
		    	fill.setStyle(FillStyle.Fill.SOLID);
		    	
		    	stl.setFill(fill);
	    
	    	} else {
	    		
	    		stl.setStyle(Style.BOXES);
	    		
	    		fill.setPattern(patternOrder.get(i % patternOrder.size()));
	    		
	    		stl.setFill(fill);
	    		
	    	}
	    	
			i++;
			
    	}
		
		if ( !IS_HISTOGRAM && ADD_LABELS ) {
				
			for ( int j = 1; j < p.getPlots().size(); j+= 2 ) {
				
		    	PlotStyle stl = ((AbstractPlot) p.getPlots().get(j)).getPlotStyle();
		    	
		    	stl.setStyle(Style.LABELS);
		    	
	    	}
		
		}
		
		String labelString = "(";
		
		for ( Entry<TraverserRecord, Double> traverserValues : barDatasets.get(0) ) {
			
			String traverserName = traverserValues.getKey().toString();
			
			if ( IS_HISTOGRAM ) {
				
				labelString += "\"" + traverserName + "\" " + barDatasets.get(0).indexOf(traverserValues) + ",";
				
			} else { 
				
				labelString += "\"" + traverserName + "\" " + ( ( ( barDatasets.size() - 1 ) / (double)2 ) + ( ( barDatasets.get(0).indexOf(traverserValues) ) * barDatasets.size()) ) + ",";
				
			}
			
		}
		
		labelString = labelString.substring(0, labelString.length() - 1) + ")";
		
		p.set("xtics", labelString + " font \"" + FONT_TYPE + "," + FONT_SIZE + "\" nomirror rotate by -45");
		
		p.set("xtic", "scale 0");
		
	}

}
