package org.kclhi.hands.utility.adaptive;

/**
* @author Martin
*
*/
public class AdaptiveMeasure {
  
  /**
  * 
  */
  private double adaptiveAssessment;
  
  /**
  * @return
  */
  public double getAdaptiveAssessment() {
    
    return adaptiveAssessment;
    
  }
  
  /**
  * 
  */
  private String adaptTo;
  
  /**
  * @return
  */
  public String getAdaptTo() {
    
    return adaptTo;
    
  }
  
  /**
  * 
  */
  private boolean adaptToSpecified;
  
  /**
  * @return
  */
  public boolean adaptToSpecified() {
    
    return adaptToSpecified;
    
  }
  
  /**
  * @param adaptiveAssessment
  */
  public AdaptiveMeasure(double adaptiveAssessment) {
    
    this.adaptiveAssessment = adaptiveAssessment;
    
    this.adaptTo = "";
    
    this.adaptToSpecified = false;
    
  }
  
  /**
  * @param adaptiveAssessment
  * @param adaptTo
  */
  public AdaptiveMeasure(double adaptiveAssessment, String adaptTo) {
    
    this.adaptiveAssessment = adaptiveAssessment;
    
    this.adaptTo = adaptTo;
    
    this.adaptToSpecified = true;
    
  }
  
  /* (non-Javadoc)
  * @see java.lang.Object#toString()
  */
  public String toString() {
    
    return adaptiveAssessment + " " + adaptTo + " " + adaptToSpecified;
    
  }
  
}
