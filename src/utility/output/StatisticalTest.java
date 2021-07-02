package Utility.output;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.inference.TTest;

import Utility.TraverserDataset;

/**
 * @author Martin
 *
 */
public class StatisticalTest {

	/**
	 * @param datasetA
	 * @param datasetB
	 * @return
	 */
	public static double getPValue(TraverserDataset datasetA, TraverserDataset datasetB) {
		
		TTest ttest = new TTest();
		
		return ttest.tTest(datasetA.getDatasetAsArray(), datasetB.getDatasetAsArray());
		
	}
	
	/**
	 * @param datasetA
	 * @param datasetB
	 * @return
	 */
	public static String getPGroup(TraverserDataset datasetA, TraverserDataset datasetB) {
		
		return getPGroup(StatisticalTest.getPValue(datasetA, datasetB));
		
	}
	
	
	/**
	 * @param result
	 * @return
	 */
	public static String getPGroup(double result) {
		
		if ( result < 0.05 && result >= 0.01 ) { 
			
			//return "P<0.05*";
			return "*";
			
		} else if ( result < 0.01 && result >= 0.001 ) {
			
			//return "P<0.01**";
			return "**";
		
		} else if ( result < 0.001 ) {
			
			//return "P<0.001";
			return "***";
			
		} else {
			
			return "";
			
		}
		
	}
	
}
