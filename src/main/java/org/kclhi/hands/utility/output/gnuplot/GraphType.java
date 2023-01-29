package org.kclhi.hands.utility.output.gnuplot;

/**
 * @author Martin
 *
 */
public enum GraphType {

	/**
	 * 
	 */
	BAR("Bar"), 
	
	/**
	 * 
	 */
	LINE("Line"),
	
	/**
	 * 
	 */
	THREE_DIMENSIONAL("3D"); 
	
	
	/**
	 * 
	 */
	private String text;

	/**
	 * @param text
	 */
	private GraphType(String text) {
		
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
	public static GraphType fromString(String text) {
    
		if (text != null) {
			
			for (GraphType t : GraphType.values()) {
				
				if (text.equalsIgnoreCase(t.text)) {
          
					return t;
        
				}
      
			}
    
		}
    
		throw new IllegalArgumentException("No constant with text " + text + " found");
  
	}
	
}
