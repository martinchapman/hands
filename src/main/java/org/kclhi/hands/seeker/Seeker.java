package org.kclhi.hands.seeker;

import org.kclhi.hands.GraphTraverser;

/**
* @author Martin
*
*/
public interface Seeker extends GraphTraverser {
  
  /**
  * @return
  */
  public void search();
  
  /**
  * @return
  */
  public boolean searchCriteria();
  
}
