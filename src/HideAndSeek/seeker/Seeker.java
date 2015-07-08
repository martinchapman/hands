package HideAndSeek.seeker;

import java.util.ArrayList;
import java.util.HashSet;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.StringVertex;

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