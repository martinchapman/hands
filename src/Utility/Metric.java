package Utility;

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
	SCORE("Score"),
	
	/**
	 * 
	 */
	PATH("Path"), 
	
	/**
	 * 
	 */
	COST_CHANGE("Cost change"), 
	
	/**
	 * 
	 */
	COST_CHANGE_SCORE("Cost change score"), 
	
	/**
	 * 
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
