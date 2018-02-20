package utility;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Martin
 *
 */
public class TraverserDataset {

    /**
     * 
     */
    private ArrayList<Double> data;
    
    /**
     * 
     */
    private double size;    

    /**
     * 
     */
    public TraverserDataset()  {
    
    	data = new ArrayList<Double>();
    
    	size = 0;
    
    }  
    
    /**
     * @param firstEntry
     */
    public TraverserDataset(double firstEntry) {
    	
    	this();
    	
    	addItemToDataset(firstEntry);
    	
    }
    
    /**
     * @return
     */
    public ArrayList<Double> getDataset() {
    	
    	return data;
    	
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	
    	return getDataset().toString();
    	
    }
    
    /**
     * @return
     */
    public double[] getDatasetAsArray() {
    	
    	double[] data = new double[this.data.size()];
		
		for (int i = 0; i < this.data.size(); i++) {
			
			data[i] = this.data.get(i);

		}
		
		return data;
		
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
    public double getStandardDeviation() {
    	
        return Math.sqrt(getVariance());
        
    }
    
    /**
     * @return
     */
    public double getStandardError() {
    	
       return getStandardDeviation() / Math.sqrt(size);
        
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
