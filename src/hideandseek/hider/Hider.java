package hideandseek.hider;

import java.util.ArrayList;

import hideandseek.GraphTraverser;
import hideandseek.graph.StringVertex;
import hideandseek.seeker.Seeker;

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