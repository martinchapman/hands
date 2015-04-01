package HideAndSeek.hider;

import java.util.ArrayList;

import HideAndSeek.GraphTraverser;
import HideAndSeek.graph.StringVertex;
import HideAndSeek.seeker.Seeker;

public interface Hider extends GraphTraverser {

	/**
	 * @param location
	 */
	public abstract void addHideLocation(StringVertex location);

	/**
	 * @param vertex
	 * @return
	 */
	public abstract boolean hideHere(StringVertex vertex);
	
	/**
	 * @return
	 */
	public int numberOfHideLocations();
	
	/**
	 * @param traverser
	 */
	public void mergeOtherTraverser(Hider traverser);
	

}