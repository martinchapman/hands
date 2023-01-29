package org.kclhi.hands.utility;

/**
 * @author Martin
 *
 */
public enum Metric {

	/**
	 * 
	 */
	COST("Cost"), 
	
	/**
	 * 
	 */
	PAYOFF("Payoff"),
	
	/**
	 * 
	 */
	PATH("Path"), 
	
	/**
	 * Costs accrued compared to total edge costs
	 */
	TOTAL_EDGE_COST("Total edge cost"), 
	
	/**
	 * 
	 */
	COST_CHANGE("Cost change"), 
	
	/**
	 * 
	 */
	COST_CHANGE_PAYOFF("Cost change payoff"), 
	
	/**
	 * Difference between current round and first round
	 */
	RELATIVE_COST("Relative cost");
	
	/**
	 * 
	 */
	private String text;

	/**
	 * @param text
	 */
	private Metric(String text) {
		
		this.text = text;
	
	}

	/**
	 * @return
	 */
	public String getText() {
    
		return this.text;
	
	}

	/**
	 * @param text
	 * @return
	 */
	public static Metric fromString(String text) {
    
		if (text != null) {
			
			for (Metric m : Metric.values()) {
				
				if (text.equalsIgnoreCase(m.text)) {
          
					return m;
        
				}
      
			}
    
		}
    
		throw new IllegalArgumentException("No constant with text " + text + " found");
  
	}
	
}
