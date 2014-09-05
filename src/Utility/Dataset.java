package Utility;

import java.util.ArrayList;
import java.util.Arrays;

public class Dataset {

    /**
     * 
     */
    ArrayList<Double> data;
    
    /**
     * 
     */
    double size;    

    /**
     * 
     */
    public Dataset()  {
    
    	data = new ArrayList<Double>();
    
    	size = 0;
    
    }   
    
    /**
     * @param item
     */
    public void addItemToDataset(double item) {
    	
    	data.add(item);
    	
    	size++;
    	
    }

    /**
     * @return
     */
    public double getMean() {
    	
        double sum = 0.0;
        
        for ( double a : data ) {
        
        	sum += a;
        
        }
        
        return sum/size;
        
    }

    /**
     * @return
     */
    public double getVariance() {
    	
        double mean = getMean();
        
        double temp = 0;
        
        for ( double a :data ) {
        	
            temp += (mean-a)*(mean-a);
        
        }
        
        return temp/size;
    
    }

    /**
     * @return
     */
    public double getStdDev() {
    	
        return Math.sqrt(getVariance());
        
    }

    /**
     * @return
     */
    public double median() {
    	
       double[] b = new double[data.size()];
       
       System.arraycopy(data, 0, b, 0, b.length);
       
       Arrays.sort(b);

       if (data.size() % 2 == 0) {
    	   
          return (b[(b.length / 2) - 1] + b[b.length / 2]) / 2.0;
       
       } else {
          
    	   return b[b.length / 2];
       
       }
       
    }
    
}
