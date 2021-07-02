package Utility.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import Utility.Utils;

/**
 * @author Martin
 *
 */
public class SignificanceTable {
	
	/**
	 * @author Martin
	 *
	 */
	private class PValue {
		
		/**
		 * 
		 */
		private String traverserA;
		
		/**
		 * 
		 */
		private String traverserB;
		
		/**
		 * 
		 */
		private double traverserAValue;
		
		/**
		 * 
		 */
		private double traverserBValue;
		
		/**
		 * 
		 */
		private double pValue;
		
		/**
		 * @param traverserA
		 * @param traverserB
		 * @param pValue
		 */
		public PValue(String traverserA, String traverserB, double traverserAValue, double traverserBValue, double pValue) {
			
			this.traverserA = traverserA;
			
			this.traverserB = traverserB;
			
			this.traverserAValue = traverserAValue;
			
			this.traverserBValue = traverserBValue;
			
			this.pValue = pValue;
			
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			
			return traverserA + " & " + traverserB + " & " + Utils.round(traverserAValue, 4) + " & " + Utils.round(traverserBValue, 4) + " & " + (pValue < 0.001 ? "$<$ 0.001" : Utils.round(pValue, 4)) + " \\\\";
			
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			long temp;
			temp = Double.doubleToLongBits(pValue);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PValue other = (PValue) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (Double.doubleToLongBits(pValue) != Double
					.doubleToLongBits(other.pValue))
				return false;
			return true;
		}

		private SignificanceTable getOuterType() {
			return SignificanceTable.this;
		}

	}
	
	/**
	 * 
	 */
	private ArrayList<PValue> pValues = new ArrayList<PValue>();
	
	/**
	 * @param traverserA
	 * @param traverserB
	 * @param traverserAValue
	 * @param traverserBValue
	 * @param pValue
	 */
	public void addPValue(String traverserA, String traverserB, double traverserAValue, double traverserBValue, double pValue) {
		
		Utils.talk(toString(), "Inserting PValue: " + traverserA + " " + traverserB + " " + traverserAValue + " " + traverserBValue + " " + pValue);
		
		if ( !pValues.contains(new PValue(traverserA, traverserB, traverserAValue, traverserBValue, pValue))) {
			
			pValues.add(new PValue(Utils.shortenOutputName(traverserA), Utils.shortenOutputName(traverserB), traverserAValue, traverserBValue, pValue));
		
		}
		
	}
	
	/**
	 * @param associatedFigure
	 * @param description
	 */
	public void outputPValueTable(String associatedFigure, String description) {
		
		if ( pValues.size() == 0 ) return;
		
		String outputPath = "figure" + new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
		
		FileWriter significanceTableWriter = null;
		
		FileWriter significanceTableListWriter = null;
		
		try {
			
			significanceTableWriter = new FileWriter(Utils.FILEPREFIX + "charts/tables/" + associatedFigure + "_significancetable.tex", true);
			
			significanceTableListWriter = new FileWriter(Utils.FILEPREFIX + "charts/tables/significancetables.tex", true);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		
		}
		
		PrintWriter writer = null;
		
		try {
		
			writer = new PrintWriter(Utils.FILEPREFIX + "charts/tables/" + associatedFigure + "_significancetable.tex");
		
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		
		}
		
		writer.close();
		
		boolean containsTable = false;
		
		for ( String inputLine : Utils.readFromFile(Utils.FILEPREFIX + "charts/tables/significancetables.tex")) {
			
			if ( inputLine.contains(Utils.FILEPREFIX + "charts/tables/" + associatedFigure + "_significancetable.tex") ) containsTable = true;
			
		}
		
		if ( !containsTable ) { 
			
			Utils.writeToFile(significanceTableListWriter, "\\input{" + Utils.MARTINPREFIX + Utils.FILEPREFIX + "charts/tables/" + associatedFigure + "_significancetable.tex" + "} \n");
			
		}
		
		boolean longTable = false;
		
		if ( pValues.size() > 11 ) {
			
			longTable = true;
			
			//Utils.writeToFile(significanceTableListWriter, "\\clearpage \n");
			
		}
		
		//
		
		if ( longTable ) {
		
			Utils.writeToFile(significanceTableWriter, "\\begin{longtable}{p{3cm}p{3cm}p{2.5cm}p{2.5cm}p{1.5cm}}");
		
		} else {
			
			Utils.writeToFile(significanceTableWriter, "\\begin{table}[h!]");
			
		}
		
		Utils.writeToFile(significanceTableWriter, "\n");
		
		if ( !longTable ) Utils.writeToFile(significanceTableWriter, "\\begin{center}");
		
		Utils.writeToFile(significanceTableWriter, "\n");
		
		if ( !longTable ) Utils.writeToFile(significanceTableWriter, "\\begin{tabular}{p{4cm}p{4cm}p{1.5cm}p{1.5cm}p{1.5cm}}");
		
		Utils.writeToFile(significanceTableWriter, "\n");
		
		Utils.writeToFile(significanceTableWriter, "\\hline");
		
		Utils.writeToFile(significanceTableWriter, "\n");
		
		Utils.writeToFile(significanceTableWriter, "Strategy A (SA) & Strategy B (SB) & SA Value & SB Value & pValue \\\\");
		
		Utils.writeToFile(significanceTableWriter, "\n");
		
		Utils.writeToFile(significanceTableWriter, "\\hline");
		
		Utils.writeToFile(significanceTableWriter, "\n");
		
		for ( PValue line : pValues ) {
			
			Utils.writeToFile(significanceTableWriter, line.toString());
			
			Utils.writeToFile(significanceTableWriter, "\n");
			
			Utils.writeToFile(significanceTableWriter, "\\hline");
			
			Utils.writeToFile(significanceTableWriter, "\n");
			
		}
		
		if ( !longTable ) Utils.writeToFile(significanceTableWriter, "\\end{tabular}");
		
		Utils.writeToFile(significanceTableWriter, "\n");
		
		if ( longTable ) {
			
			Utils.writeToFile(significanceTableWriter, "\\caption*{" + description + "}");
		
		} else {
			
			Utils.writeToFile(significanceTableWriter, "\\caption{" + description + "}");
			
		}
		
		Utils.writeToFile(significanceTableWriter, "\n");
		
		Utils.writeToFile(significanceTableWriter, "\\label{" + ( associatedFigure + "_significancetable" ) + "}");
		
		Utils.writeToFile(significanceTableWriter, "\n");
		
		if ( !longTable ) Utils.writeToFile(significanceTableWriter, "\\end{center}");
		
		Utils.writeToFile(significanceTableWriter, "\n");
		
		if ( longTable ) {
			
			Utils.writeToFile(significanceTableWriter, "\\end{longtable}");
			
		} else {
		
			Utils.writeToFile(significanceTableWriter, "\\end{table}");
		
		}
		
		try {
			
			significanceTableWriter.close();
			
			significanceTableListWriter.close();
		
		} catch (IOException e) {
		
			e.printStackTrace();
		
		}
		
	}

}
