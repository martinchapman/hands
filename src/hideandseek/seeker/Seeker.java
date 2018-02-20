package hideandseek.seeker;

import java.util.ArrayList;
import java.util.HashSet;

import hideandseek.GraphTraverser;
import hideandseek.graph.StringVertex;

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