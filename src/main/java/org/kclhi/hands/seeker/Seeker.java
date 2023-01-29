package org.kclhi.hands.seeker;

import java.util.ArrayList;
import java.util.HashSet;

import org.kclhi.hands.GraphTraverser;
import org.kclhi.hands.graph.StringVertex;

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
