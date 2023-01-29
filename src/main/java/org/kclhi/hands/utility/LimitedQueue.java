package org.kclhi.hands.utility;

import java.util.LinkedList;

/**
 * @author Martin
 *
 * @param <E>
 */
public class LimitedQueue<E> extends LinkedList<E> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private int limit;

    /**
     * @param limit
     */
    public LimitedQueue(int limit) {
        this.limit = limit;
    }

    /* (non-Javadoc)
     * @see java.util.LinkedList#add(java.lang.Object)
     */
    @Override
    public boolean add(E o) {
        boolean added = super.add(o);
        while (added && size() > limit) {
           super.removeLast();
        }
        return added;
    }
    
}
