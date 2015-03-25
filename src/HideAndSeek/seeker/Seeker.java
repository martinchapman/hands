package HideAndSeek.seeker;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.StringVertex;

public interface Seeker extends GraphTraverser {

	/**
	 * @param location
	 */
	public void addHideLocation(StringVertex location);
	
	/**
	 * @return
	 */
	public void search();

}