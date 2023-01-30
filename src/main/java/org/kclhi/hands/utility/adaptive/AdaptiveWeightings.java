package org.kclhi.hands.utility.adaptive;

/**
* @author Martin
*
*/
public class AdaptiveWeightings {
  
  /**
  * 
  */
  private double environmentalWeighting;
  
  /**
  * @return
  */
  public double getEnvironmentalWeighting() {
    
    return environmentalWeighting;
    
  }
  
  /**
  * 
  */
  private double socialWeighting;
  
  /**
  * @return
  */
  public double getSocialWeighting() {
    
    return socialWeighting;
    
  }
  
  /**
  * 
  */
  private double internalWeighting;
  
  /**
  * @return
  */
  public double getInternalWeighting() {
    
    return internalWeighting;
    
  }
  
  /**
  * @param environmentalWeighting
  * @param socialWeighting
  * @param internalWeighting
  */
  public AdaptiveWeightings(double environmentalWeighting, double socialWeighting, double internalWeighting) {
    
    setEnvironmentalWeighting(environmentalWeighting);
    
    setSocialWeighting(socialWeighting);
    
    setInternalWeighting(internalWeighting);
    
  }
  
  /**
  * @return
  */
  private double totalWeightings() {
    
    return environmentalWeighting + socialWeighting + internalWeighting;
    
  }
  
  /**
  * 
  */
  private void weightingError() {
    
    System.out.println("Weighting exceeds 1.0");
    
  }
  
  /**
  * @param weighting
  * @return
  */
  private boolean weightingCheck(double weighting) {
    
    if ( ( totalWeightings() + weighting ) > 1.0 ) {
      
      weightingError();
      
      return false;
      
    }
    
    return true;
    
  }
  
  /**
  * @param environmentalWeighting
  */
  public void setEnvironmentalWeighting(double environmentalWeighting) {
    
    if ( weightingCheck(environmentalWeighting) ) this.environmentalWeighting = environmentalWeighting;
    
  }
  
  /**
  * @param socialWeighting
  */
  public void setSocialWeighting(double socialWeighting) {
    
    if ( weightingCheck(socialWeighting) ) this.socialWeighting = socialWeighting;
    
  }
  
  /**
  * @param internalWeighting
  */
  public void setInternalWeighting(double internalWeighting) {
    
    if ( weightingCheck(internalWeighting) ) this.internalWeighting = internalWeighting;
    
  }
  
}
