package HideAndSeek.hider;

public interface AdaptiveHiderStrategy {
	
	/**
	 * Adaptive Hiding strategies must provide 
	 * a mechanism that reports on their ability to 
	 * hide effectively under their given strategy,
	 * based upon game conditions, and the performance/attributes
	 * of their opponent.
	 * 
	 * @return
	 */
	public double abilityToPerform();

}
