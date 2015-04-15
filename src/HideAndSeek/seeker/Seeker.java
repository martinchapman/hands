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
	public HashSet<StringVertex> uniqueHideLocations();
	
	/**
	 * @return
	 */
	public ArrayList<StringVertex> allHideLocations();
	
	/**
	 * @param traverser
	 */
	public void mergeOtherTraverser(Seeker traverser);

	/**
	 * @return
	 */
	public boolean searchCriteria();
	
}