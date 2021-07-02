package Utility.output.gnuplot;

import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.AbstractPlot;
import com.panayotis.gnuplot.style.FillStyle;
import com.panayotis.gnuplot.style.PlotStyle;
import com.panayotis.gnuplot.style.RgbPlotColor;
import com.panayotis.gnuplot.style.Style;
import com.panayotis.gnuplot.terminal.CustomTerminal;
import com.panayotis.gnuplot.terminal.PostscriptTerminal;
import com.panayotis.gnuplot.utils.Debug;

/**
 * @author Martin
 */
public abstract class GNUGraph {
	
	/**
	 * 
	 */
	protected JavaPlot p;
	
	/**
	 * @return
	 */
	public JavaPlot getP() {
		
		return p;
		
	}
	
	/**
	 * 
	 */
	protected String title;
	
	/**
	 * 
	 */
	protected boolean textBased = false;
	
	/**
	 * @param title
	 * @param FONT_SIZE
	 */
	public GNUGraph(String title, String FONT_SIZE, boolean textBased) {
		
		this.title = title;
		
		this.FONT_SIZE = FONT_SIZE;
		
		this.textBased = textBased;
		
		p = new JavaPlot();

    	p.setTerminal(new CustomTerminal("X11", ""));
    	
		JavaPlot.getDebugger().setLevel(Debug.VERBOSE);
		
		p.setTitle(title);
		
	}
	
	/**
	 * @param title
	 */
	public GNUGraph(String title) {
		
		this(title, "12", false);
		
	}
	
	/**
	 * @param title
	 * @param FONT_SIZE
	 */
	public GNUGraph(String title, String FONT_SIZE) {
		
		this(title, FONT_SIZE, false);
		
	}
	
	/**
	 * @param title
	 * @param textBased
	 */
	public GNUGraph(String title, boolean textBased) {
		
		this(title, "12", textBased);
		
	}
	
	/**
	 * 
	 */
	
	/**
	 * 
	 */
	protected String FONT_TYPE = "verdana";
	
	/**
	 * 
	 */
	protected String FONT_SIZE = "12";
	
	public void styleGraph() {
	
		p.set("ytics", "font \"" + FONT_TYPE + "," + FONT_SIZE + "\" nomirror");
		
		p.set("ylabel", "font \"" + FONT_TYPE + "," + FONT_SIZE + "\"");
		
		p.set("xtics", "font \"" + FONT_TYPE + "," + FONT_SIZE + "\" nomirror");
		
		p.set("xlabel", "font \"" + FONT_TYPE + "," + FONT_SIZE + "\"");
		
		p.set("key", "font \"" + FONT_TYPE + "," + FONT_SIZE + "\" below");
		
		p.set("xzeroaxis", "");
		
		// Library Version: p.setKey(JavaPlot.Key.BELOW);
		
	}
	
    /**
     * 
     */
    private void setGreyscale() {
		
    	for ( int i = 0; i < p.getPlots().size(); i++ ) {
			
	    	PlotStyle stl = ((AbstractPlot) p.getPlots().get(i)).getPlotStyle();
	    	
	    	stl.set("linecolor", "rgb \"#696969\"");
	    	
		}
    	
    }
	
	/**
	 * @param xlabel
	 * @param ylabel
	 */
	public void createChart(String xlabel, String ylabel) {
	    
        p.getAxis("x").setLabel(xlabel);
        
        p.getAxis("y").setLabel(ylabel);
        
        if (!textBased) p.plot();
    
    }
	
    /**
     * @param title
     * @param xlabel
     * @param ylabel
     */
    public void createChart(String title, String xlabel, String ylabel) {
    	
    	p.setTitle(title);
    	
        p.getAxis("x").setLabel(xlabel);
        
        p.getAxis("y").setLabel(ylabel);
        
        setGreyscale();
        
        if (!textBased) p.plot();
    
    }
    
    /**2
     * @param outputPath
     */
    public void exportChartAsEPS(String outputPath) {
    	
    	PostscriptTerminal epsf = new PostscriptTerminal(outputPath);
    	
        epsf.setColor(true);
        
        p.setTerminal(epsf);
        
        p.plot();
        
    }
    
    /**
     * @param outputPath
     */
    public void exportChartAsTikz(String outputPath) {
    	
    	TikzTerminal tikz = new TikzTerminal(outputPath);
    	
    	p.setTerminal(tikz);
    	
    	FONT_TYPE = "";
    	
    	try {
    	
    		FONT_SIZE = (Integer.parseInt(FONT_SIZE) - 3) + "";
    	
    	} catch ( NumberFormatException e ) { }
    	
    	styleGraph();
    	
    	setGreyscale();
    	
    	p.plot();
    	
    }
	
}
