package Utility.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;

public class TraverserRecord {
	
	/**
	 * 
	 */
	protected String traverser;
	
	/**
	 * 
	 */
	protected int gameNumber;
	
	/**
	 * @param traverser
	 */
	public TraverserRecord(String traverser) {
	
		this.traverser = traverser;
		
		attributeToValue = new Hashtable<Integer, Hashtable<String, Double>>();
		
		gameNumber = 1;
		
		attributeToValue.put(gameNumber, new Hashtable<String, Double>());
		
		attributes = new HashSet<String>();
		
	}
		
	/**
	 * @return
	 */
	public String getTraverser() {
		
		return traverser;
	
	}
		
	/**
	 * Game Number to a list of the attributes and their values,
	 * for this traverser, in that game.
	 */
	protected Hashtable<Integer, Hashtable<String, Double>> attributeToValue;
	
	/**
	 * 
	 */
	protected HashSet<String> attributes;
	
	/**
	 * @return
	 */
	public HashSet<String> getAttributes() {
		
		return attributes;
	
	}

	/**
	 * @param attribute
	 * @param value
	 */
	public void attribute(String attribute, double value) {
	    
		// To maintain list of individual attributes
		attributes.add(attribute);
		
		// If we already have an attribute entry for the most recent game, create new game
		if (attributeToValue.get(gameNumber).containsKey(attribute)) {
		
			gameNumber++;
			
			Hashtable<String, Double> newGameTable = new Hashtable<String, Double>();
			
			newGameTable.put(attribute, value);
			
			attributeToValue.put(gameNumber, newGameTable);
		
		// Otherwise, attribute should go to this game
		} else {
		
			Hashtable<String, Double> currentGameTable = attributeToValue.get(gameNumber);
			
			currentGameTable.put(attribute, value);
			
			attributeToValue.put(gameNumber, currentGameTable);
		
		}
		
	}
	
	/**
	 * 
	 */
	protected Hashtable<String, Double> calculateAverage() {
		
		Hashtable<String, Double> cumulativeAttributeToValue = new Hashtable<String, Double>();
		
		for (Entry<Integer, Hashtable<String, Double>> gameAttributeEntry : attributeToValue.entrySet()) {
		
			for (Entry<String, Double> attribute : gameAttributeEntry.getValue().entrySet()) {
				
				if (cumulativeAttributeToValue.containsKey(attribute.getKey())) {
					
					cumulativeAttributeToValue.put(attribute.getKey(), cumulativeAttributeToValue.get(attribute.getKey()) + attribute.getValue());
					
				} else {
					
					cumulativeAttributeToValue.put(attribute.getKey(), attribute.getValue());
					
				}
				
			}
		
		}
		
		Hashtable<String, Double> averageAttributeToValue = new Hashtable<String, Double>();
		
		for (Entry<String, Double> attribute : cumulativeAttributeToValue.entrySet()) {
			
			averageAttributeToValue.put(attribute.getKey(), attribute.getValue() / gameNumber);
			
		}
		
		return averageAttributeToValue;
		
	}
	
	/**
	 * @return
	 */
	protected String showSeries() {
		
		String returner = "";
		
		ArrayList<Entry<Integer, Hashtable<String,Double>>> series = 
				new ArrayList<Entry<Integer, Hashtable<String,Double>>>(attributeToValue.entrySet());
		
		Collections.reverse(series);
		
		for ( Entry<Integer, Hashtable<String,Double>> entry : series ) {
			
			returner += "\n" + entry; 
			
		}
		
		return returner;
		
	}
	
	/**
	 * 
	 * Return the series for each attribute
	 * 
	 * @param attribute
	 * @return
	 */
	public ArrayList<Entry<Integer, Hashtable<String,Double>>> getSeries() {
		
		ArrayList<Entry<Integer, Hashtable<String,Double>>> series = 
				new ArrayList<Entry<Integer, Hashtable<String,Double>>>(attributeToValue.entrySet());
		
		Collections.reverse(series);
		
		return series;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String printAverage() {
	
		return traverser + " " + calculateAverage();
		
	}
	
	/**
	 * @return
	 */
	public String printSeries() {
		
		return traverser + " " + showSeries();
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		return getTraverser().equals(((TraverserRecord) obj).getTraverser());
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return traverser;
		
	}
		
}

