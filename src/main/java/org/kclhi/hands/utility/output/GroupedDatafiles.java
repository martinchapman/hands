package org.kclhi.hands.utility.output;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

/**
* @author Martin
*
*/
public class GroupedDatafiles extends Datafile {
  
  /**
  * @param properties
  * @param path
  */
  public GroupedDatafiles(String properties, Path path) {
    
    super(properties, path);
    
    allDatafiles = new ArrayList<Datafile>();
    
  }
  
  /**
  * @param identifier
  */
  public GroupedDatafiles(String identifier) {
    
    super(identifier);
    
  }
  
  /**
  * 
  */
  private ArrayList<Datafile> allDatafiles;
  
  /**
  * @return
  */
  public ArrayList<Datafile> getAllDatafiles() {
    
    ArrayList<Datafile> allDatafiles = new ArrayList<Datafile>(this.allDatafiles);
    
    Datafile thisCopy = new Datafile(properties, path);
    
    thisCopy.setIdentifier(identifier);
    
    allDatafiles.add(0, thisCopy);
    
    Collections.sort(allDatafiles);
    
    return allDatafiles;
    
  }
  
  /**
  * @param otherDatafile
  */
  public void addDatafile(Datafile otherDatafile) {
    
    allDatafiles.add(otherDatafile);
    
  }
  
  /* (non-Javadoc)
  * @see Utility.output.Datafile#toString()
  */
  public String toString() {
    
    return "(Multi) " + super.toString();
    
  }
  
  
  
}
