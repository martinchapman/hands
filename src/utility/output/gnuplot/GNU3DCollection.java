package Utility.output.gnuplot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import Utility.Utils;

/**
 * @author Martin
 *
 */
public class GNU3DCollection extends GNUGraph {

	/**
	 * 
	 */
	private ArrayList<GNU3DGraph> GNU3DGraphs;
	
	 /**
	 * @param title
	 * @param textBased
	 */
	public GNU3DCollection(String title, boolean textBased) {
	    	
    	super(title, "10", textBased);
    	
    	GNU3DGraphs = new ArrayList<GNU3DGraph>();
	    	
    }
	
	/**
	 * @param label
	 */
	public void setZAxisLabel(String label) {
    	
		for ( GNU3DGraph graph : GNU3DGraphs ) graph.getP().getAxis("z").setLabel(label);
    	
    }

	/**
	 * @param title
	 * @param data
	 */
	public void addDataset(String title, ArrayList<ArrayList<Double>> data) {
		 
		GNU3DGraph newGraph = new GNU3DGraph(this.title, this.textBased);
		 
		newGraph.addDataset(title, data);
		
		GNU3DGraphs.add(newGraph);
		
	}

	/* (non-Javadoc)
	 * @see Utility.output.gnuplot.GNUGraph#styleGraph()
	 */
	@Override
	public void styleGraph() {
		
		for ( GNU3DGraph graph : GNU3DGraphs ) graph.styleGraph();
		
	}

	/* (non-Javadoc)
	 * @see Utility.output.gnuplot.GNUGraph#createChart(java.lang.String, java.lang.String)
	 */
	@Override
	public void createChart(String xlabel, String ylabel) {
		
		for ( GNU3DGraph graph : GNU3DGraphs ) graph.createChart(xlabel, ylabel);
		
	}

	/* (non-Javadoc)
	 * @see Utility.output.gnuplot.GNUGraph#createChart(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createChart(String title, String xlabel, String ylabel) {
		
		for ( GNU3DGraph graph : GNU3DGraphs ) graph.createChart(title, xlabel, ylabel);
		
	}

	/* (non-Javadoc)
	 * @see Utility.output.gnuplot.GNUGraph#exportChartAsEPS(java.lang.String)
	 */
	@Override
	public void exportChartAsEPS(String outputPath) {
		
		//if ( GNU3DGraphs.size() == 1 ) { 
			
			GNU3DGraphs.get(0).exportChartAsEPS(outputPath);
		
		//}
		
	}

	/* (non-Javadoc)
	 * @see Utility.output.gnuplot.GNUGraph#exportChartAsTikz(java.lang.String)
	 */
	@Override
	public void exportChartAsTikz(String outputPath) {
		
		if ( GNU3DGraphs.size() == 1 ) {
			
			GNU3DGraphs.get(0).exportChartAsTikz(outputPath);
			
		} else {
			
			for ( GNU3DGraph graph : GNU3DGraphs ) graph.exportChartAsTikz(outputPath + "_TEMP_" + GNU3DGraphs.indexOf(graph));
			
			ArrayList<Path> files = Utils.listFilesForFolder(new File(outputPath.substring(0, outputPath.lastIndexOf("/") + 1)));
	
			ArrayList<String> completeGraphLines = new ArrayList<String>();
			
			completeGraphLines.add("\\centering");
			
			// ~MDC 31/8 Affects ordering
			for ( Path file : files ) {
				
				if ( file.toString().contains("TEMP") ) {
				
					completeGraphLines.add("\\begin{minipage}{.5\\textwidth}");
					
					completeGraphLines.add("\\centering");
					
					for ( String line : Utils.readFromFile(file.toAbsolutePath().toString()) ) {
						
						completeGraphLines.add(line);
						
					}
					
					completeGraphLines.add("\\end{minipage}%");
					
					Utils.deleteFile(file);
					
				}
				
			}
			
			FileWriter graphWriter = null;
			
			try {
			
				graphWriter = new FileWriter(outputPath, true);

			} catch (IOException e) {

				e.printStackTrace();
			
			}
			
			for ( String line : completeGraphLines ) {
				
				Utils.writeToFile(graphWriter, line + "\n");
				
			}
			
		}
	
	}

}
