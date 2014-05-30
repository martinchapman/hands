package Utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.math3.stat.inference.TTest;

public class StatisticalTests {

	final static String FILEPREFIX = "/Users/Martin/Dropbox/University/PhD/2014/1. 2nd January - 3rd January/ACySe/Results Analysis/";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedReader br = null;
        String simID = "";
        List<Double> resultSet1 = new ArrayList<Double>();
        List<Double> resultSet2 = new ArrayList<Double>();
        
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(FILEPREFIX + "results.csv"));
 
			while ((sCurrentLine = br.readLine()) != null) {
				
				resultSet1.add(Double.parseDouble(sCurrentLine.split(",")[0]));
				resultSet2.add(Double.parseDouble(sCurrentLine.split(",")[1]));
				
			}
			
		} catch(Exception e) {
			
			
		}
		
		//TTest ttest = new TTest();
		double[] r1 = new double[resultSet1.size()];
		double[] r2 = new double[resultSet2.size()];
		
		for (int i = 0; i < resultSet1.size(); i++) {
			
			r1[i] = resultSet1.get(i);
			r2[i] = resultSet2.get(i);
		}
		
		//System.out.println(ttest.tTest(r1,r2));

	}

}
