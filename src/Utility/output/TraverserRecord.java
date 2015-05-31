package Utility.output;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import Utility.ComparatorResult;
import Utility.Metric;
import Utility.TraverserDataset;
import Utility.TraverserDatasetMeasure;
import Utility.Utils;

/**
 * @author Martin
 *
 */
public class TraverserRecord implements Comparable<TraverserRecord> {
	
	/**
	 * 
	 */
	protected String traverser;
	
	/**
	 * @param traverser
	 */
	public void setTraverser(String traverser) {
		
		this.traverser = Utils.shortenOutputName(traverser);
		
	}
	
	/**
	 * 
	 */
	protected String opponents;
	
	/**
	 * @return
	 */
	public String getOpponents() {
	
		return opponents;
	
	}

	/**
	 * @param opponents
	 */
	public void setOpponents(String opponents) {
		
		this.opponents = opponents;
		
	}
	
	/**
	 * 
	 */
	public void clearOpponents() {
		
		this.opponents = "";
		
	}

	/**
	 * 
	 */
	private String parameters;
	
	/**
	 * @return
	 */
	public String getParameters() {
		
		return parameters;
	
	}

	/**
	 * @param parameters
	 */
	public void setParameters(String parameters) {
	
		this.parameters = parameters;
	
	}
	
	/**
	 * 
	 */
	private String category;

	/**
	 * 
	 */
	private String topology;
	
	/**
	 * @param parameters
	 */
	public void setTopology(String topology) {
		
		this.topology = topology;
		
	}
	
	/**
	 * @return
	 */
	public String getTopology() {
		
		return topology;
		
	}
	
	/**
	 * 
	 */
	private Path datafile;
	
	/**
	 * @param datafile
	 */
	public void setDatafile(Path datafile) {
		
		this.datafile = datafile;
		
	}
	
	/**
	 * @param datafile
	 */
	public Path getDatafile() {
		
		return datafile;
		
	}
	
	/**
	 * @param traverser
	 */
	public TraverserRecord(String traverser) {
	
		setTraverser(traverser);
		
		attributeToValue = new LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>>();
		
		//gameNumber = 1;
		
		//attributeToValue.put(gameNumber, new Hashtable<String, Double>());
		
		attributes = new HashSet<String>();
		
		category = "";
		
		opponents = "";
		
	}
	
	/**
	 * @param traverser
	 * @param attributeToValue
	 * @param attributes
	 */
	public TraverserRecord(String traverser, LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>> attributeToValue, HashSet<String> attributes) {
		
		this.traverser = traverser;
		
		this.attributeToValue = attributeToValue;
		
		this.attributes = attributes;
		
		opponents = "";
		
	}
	
	/**
	 * @param record
	 */
	public void integrateRecord(TraverserRecord record) {
		
		for ( Entry<AttributeSetIdentifier, Hashtable<String, Double>> gameToAttributes : record.getAttributeToValue().entrySet() ) {
			
			for ( Entry<String,Double> attributeToValue : gameToAttributes.getValue().entrySet() ) {
				
				this.attributeToValue.get(gameToAttributes.getKey()).put(attributeToValue.getKey(), this.attributeToValue.get(gameToAttributes.getKey()).get(attributeToValue.getKey()) + attributeToValue.getValue());
				
			}
			
		}
		
	}
	
	/**
	 * @param record
	 */
	public void duplicateRecord(TraverserRecord record) {
		
		this.attributeToValue = record.getAttributeToValue();
		
		this.traverser = record.getTraverser();
		
		this.opponents = record.getOpponents();
		
		this.parameters = record.getParameters();
		
		this.topology = record.getTopology();
		
		this.datafile = record.getDatafile();
		
		this.attributes = record.getAttributes();
		
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
	protected LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>> attributeToValue;
	
	/**
	 * @return
	 */
	public LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>> getAttributeToValue() {
	
		return attributeToValue;
	
	}

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
	 * 
	 */
	private int currentGameNumber = -1;
	
	/**
	 * @return
	 */
	public int getCurrentGameNumber() {
	
		return currentGameNumber;
	
	}

	/**
	 * 
	 */
	private int currentRoundNumber = -1;
	
	/**
	 * @return
	 */
	public int getCurrentRoundNumber() {
		
		return currentRoundNumber;
	
	}
	
	/**
	 * @param attribute
	 * @param value
	 */
	public void attribute(String gameOrRound, String attribute, double value) {
	   
		// To maintain list of individual attributes
		attributes.add(attribute);
		
		// ~MDC Take the logarithm of the value of effectively scale results. Same achieved with normalisation. 
		//value = Math.log(value)/Math.log(2);
		
		// If we already have an entry for this game / round, and no entry for the given attribute, add it to this entry. 
		if (attributeToValue.containsKey(new AttributeSetIdentifier(currentGameNumber, currentRoundNumber)) && 
			!attributeToValue.get(new AttributeSetIdentifier(currentGameNumber, currentRoundNumber)).containsKey(attribute) ) {
			
			attributeToValue.get(new AttributeSetIdentifier(currentGameNumber, currentRoundNumber)).put(attribute, value);
		
		// If there is no entry for this game / round then add it.
		} else {
			
			if ( gameOrRound.equals("Game") ) {
				
				currentGameNumber++;
				
				currentRoundNumber = -1;
				
			} else if ( gameOrRound.equals("Round") ) {
				
				currentRoundNumber++;
				
			}
			
			Hashtable<String, Double> newGameRoundTable = new Hashtable<String, Double>();
			
			newGameRoundTable.put(attribute, value);
			
			attributeToValue.put(new AttributeSetIdentifier(currentGameNumber, currentRoundNumber), newGameRoundTable);
			
		}
		
	}

	/**
	 * @deprecated
	 * @return
	 */
	public LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> getNormalisedGameSeries() {
		
		return getGameSeries(true);
		
	}
	
	/**
	 * @return
	 */
	public LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> getGameSeries() {
		
		return getGameSeries(false);
		
	}
	
	/**
	 * @return
	 */
	private LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> getGameSeries(boolean normalise) {
		
		LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> gameEntries = new LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>>();
		
		for (Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet()) {
			
			if ( attributeEntry.getKey().getGameOrRound().equals("Game") ) {
				
				gameEntries.put(attributeEntry.getKey(), attributeEntry.getValue());
				
			}
			
		}
		
		if (normalise) {
			
			Hashtable<String, Double> minAttributeInSeries = new Hashtable<String, Double>();
			
			Hashtable<String, Double> maxAttributeInSeries = new Hashtable<String, Double>();
		
			for (Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : gameEntries.entrySet() ) {
				
				for (Entry<String, Double> attributeToValueEntry : attributeEntry.getValue().entrySet()) {
					
					if ( !minAttributeInSeries.containsKey(attributeToValueEntry.getKey()) && !maxAttributeInSeries.containsKey(attributeToValueEntry.getKey()) ) {
						
						minAttributeInSeries.put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
						
						maxAttributeInSeries.put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
						
					} else {
						
						if ( attributeToValueEntry.getValue() < minAttributeInSeries.get(attributeToValueEntry.getKey())) {
							
							minAttributeInSeries.put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
							
						}
						
						if ( attributeToValueEntry.getValue() > maxAttributeInSeries.get(attributeToValueEntry.getKey())) {
							
							maxAttributeInSeries.put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
							
						}
						
					}
					
				}
			
			}
			
			LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> newGameEntries = new LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>>();
			
			for ( Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : gameEntries.entrySet() ) {
				
				Hashtable<String, Double> normalisedAttributeEntry = new Hashtable<String, Double>();
				
				for (Entry<String, Double> attributeToValueEntry : attributeEntry.getValue().entrySet()) {
					
					normalisedAttributeEntry.put(attributeToValueEntry.getKey(), ( (attributeToValueEntry.getValue() - minAttributeInSeries.get(attributeToValueEntry.getKey())) / (maxAttributeInSeries.get(attributeToValueEntry.getKey()) - minAttributeInSeries.get(attributeToValueEntry.getKey())) ));
					
				}
				
				newGameEntries.put(attributeEntry.getKey(), normalisedAttributeEntry);
				
			}
			
			gameEntries = newGameEntries;
		
		}
		
		return gameEntries;
		
	}
	
	/**
	 * @return
	 */
	protected Hashtable<String, Double> attributeToGameMeasure(TraverserDatasetMeasure traverserDatasetMeasure) {
		
		return attributeToGameMeasure(null, null, traverserDatasetMeasure);
		
	}
	
	/**
	 * @return
	 */
	private Hashtable<String, Double> attributeToGameMeasure(Hashtable<String, Double> minAttributeToValueAllSeries, Hashtable<String, Double> maxAttributeToValueAllSeries, TraverserDatasetMeasure traverserDatasetMeasure) {
		
		Hashtable<String, TraverserDataset> attributeToDataset = getAttributeToDataset(getGameSeries(), minAttributeToValueAllSeries, maxAttributeToValueAllSeries);
		
		Hashtable<String, Double> attributeToMeasure = new Hashtable<String, Double>();
		
		for ( Entry<String, TraverserDataset> attributeToDatasetEntry : attributeToDataset.entrySet() ) {
			
			if ( traverserDatasetMeasure == TraverserDatasetMeasure.MEAN ) {
				
				attributeToMeasure.put(attributeToDatasetEntry.getKey(), attributeToDatasetEntry.getValue().getMean());
				
			} else if ( traverserDatasetMeasure == TraverserDatasetMeasure.STANDARD_ERROR ) {
				
				attributeToMeasure.put(attributeToDatasetEntry.getKey(), attributeToDatasetEntry.getValue().getStandardError());
				
			} else if ( traverserDatasetMeasure == TraverserDatasetMeasure.STANDARD_DEVIATION ) {
				
				attributeToMeasure.put(attributeToDatasetEntry.getKey(), attributeToDatasetEntry.getValue().getStandardDeviation());
				
			}
			
		}
		
		return attributeToMeasure;
		
	}
	
	/**
	 * @param minAttributeToValueAllSeries
	 * @param maxAttributeToValueAllSeries
	 * @return
	 */
	public TraverserDataset gameDatasetForPayoff(Hashtable<String, Double> minAttributeToValueAllSeries, Hashtable<String, Double> maxAttributeToValueAllSeries) {
		
		TraverserDataset payoffData = new TraverserDataset();
		
		TraverserDataset seekerData = getAttributeToDataset(getGameSeries(), minAttributeToValueAllSeries, maxAttributeToValueAllSeries).get("Cost");
		
		for ( int i = 0; i < seekerData.getDataset().size(); i++ ) {
			
			Utils.printSystemStats();
			
			payoffData.addItemToDataset(-1 * seekerData.getDataset().get(i));
			
		}
		
		return payoffData;
		
	}

	/**
	 * @param attribute
	 * @return
	 */
	public TraverserDataset gameDatasetForAttribute(String attribute) {
		
		return getAttributeToDataset(getGameSeries(), null, null).get(attribute);
		
	}

	/**
	 * 
	 * Match each attribute to a dataset containing values for all games (effectively switches attributeToValue).
	 * 
	 * @param attributeToValue - a subset of interaction records that we want to take an average of
	 * @return
	 */
	public Hashtable<String, TraverserDataset> getAttributeToDataset(LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> attributeToValue, Hashtable<String, Double> minAttributeToValueAllSeries, Hashtable<String, Double> maxAttributeToValueAllSeries) {
		
		Hashtable<String, TraverserDataset> attributeToDataset = new Hashtable<String, TraverserDataset>();
		
		for (Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet()) {
		
			for (Entry<String, Double> attributeToValueEntry : attributeEntry.getValue().entrySet()) {
				
				if ( attributeToDataset.containsKey(attributeToValueEntry.getKey()) ) {
					  
					if ( attributeToValue.size() > 1 && minAttributeToValueAllSeries != null && maxAttributeToValueAllSeries != null ) {
						
						if ( minAttributeToValueAllSeries.containsKey(attributeToValueEntry.getKey()) && maxAttributeToValueAllSeries.containsKey(attributeToValueEntry.getKey())) {
							
							attributeToDataset.get(attributeToValueEntry.getKey()).addItemToDataset( (attributeToValueEntry.getValue() - minAttributeToValueAllSeries.get(attributeToValueEntry.getKey())) / (maxAttributeToValueAllSeries.get(attributeToValueEntry.getKey()) - minAttributeToValueAllSeries.get(attributeToValueEntry.getKey())) );
						
						}
						
					} else {
						
						attributeToDataset.get(attributeToValueEntry.getKey()).addItemToDataset(attributeToValueEntry.getValue());
						
					}
					
				} else {
					
					if ( attributeToValue.size() > 1 && minAttributeToValueAllSeries != null && maxAttributeToValueAllSeries != null ) {
						
						if ( minAttributeToValueAllSeries.containsKey(attributeToValueEntry.getKey()) && maxAttributeToValueAllSeries.containsKey(attributeToValueEntry.getKey())) {
						
							TraverserDataset gameEntriesForAttribute = new TraverserDataset( (attributeToValueEntry.getValue() - minAttributeToValueAllSeries.get(attributeToValueEntry.getKey()) ) / ( maxAttributeToValueAllSeries.get(attributeToValueEntry.getKey()) - minAttributeToValueAllSeries.get(attributeToValueEntry.getKey()) ) );
						
							attributeToDataset.put(attributeToValueEntry.getKey(), gameEntriesForAttribute);
						
						}
						
					} else {
						
						TraverserDataset gameEntriesForAttribute = new TraverserDataset(attributeToValueEntry.getValue());
						
						attributeToDataset.put(attributeToValueEntry.getKey(), gameEntriesForAttribute);
						
					}
					
				}
				
			}
			
		}
		
		return attributeToDataset;
		
	}
	
	
	/**
	 * Using attributeToGameMeasure(), select a single passed measure (and get 
	 * the average for this measure)
	 * 
	 * @param attribute
	 * @return
	 */
	public double getAttributeToGameAverage(String attribute) {
		
		return attributeToGameMeasure(TraverserDatasetMeasure.MEAN).get(attribute);
		
	}
	
	/**
	 * @param maxAttributeToValueAllSeries
	 * @param minAttributeToValueAllSeries
	 * @param attribute
	 * @return
	 */
	public double getAttributeToGameAverage(String attribute, Hashtable<String, Double> minAttributeToValueAllSeries, Hashtable<String, Double> maxAttributeToValueAllSeries) {
		
		return attributeToGameMeasure(minAttributeToValueAllSeries, maxAttributeToValueAllSeries, TraverserDatasetMeasure.MEAN).get(attribute);
		
	}
	
	/**
	 * @param attribute
	 * @return
	 */
	public double getStandardErrorGameAttributeValue(String attribute) {
		
		return attributeToGameMeasure(TraverserDatasetMeasure.STANDARD_ERROR).get(attribute);
		
	}
	
	/**
	 * @param attribute
	 * @param minAttributeToValueAllSeries
	 * @param maxAttributeToValueAllSeries
	 * @return
	 */
	public double getStandardErrorGameAttributeValue(String attribute, Hashtable<String, Double> minAttributeToValueAllSeries, Hashtable<String, Double> maxAttributeToValueAllSeries) {
		
		return attributeToGameMeasure(minAttributeToValueAllSeries, maxAttributeToValueAllSeries, TraverserDatasetMeasure.STANDARD_ERROR).get(attribute);
		
	}
	
	/**
	 * @param traverserRecord
	 * @return
	 */
	public double pValue(TraverserRecord traverserRecord, Metric metric) {
		
		return StatisticalTest.getPValue(gameDatasetForAttribute(metric.getText()), traverserRecord.gameDatasetForAttribute(metric.getText()));
		
	}
	
	/**
	 * @param traverserRecord
	 * @param minAttributeToValueAllSeries
	 * @param maxAttributeToValueAllSeries
	 * @return
	 */
	public double pValue(TraverserRecord traverserRecord, Hashtable<String, Double> minAttributeToValueAllSeries, Hashtable<String, Double> maxAttributeToValueAllSeries) {
		
		return StatisticalTest.getPValue(gameDatasetForPayoff(minAttributeToValueAllSeries, maxAttributeToValueAllSeries), traverserRecord.gameDatasetForPayoff(minAttributeToValueAllSeries, maxAttributeToValueAllSeries));
		
	}
	
	/**
	 * @param traverserRecord
	 * @param metric
	 * @return
	 */
	public String pGroup(TraverserRecord traverserRecord, Metric metric) {
	
		return StatisticalTest.getPGroup(gameDatasetForAttribute(metric.getText()), traverserRecord.gameDatasetForAttribute(metric.getText()));
		
	}
	
	/**
	 * @param traverserRecord
	 * @param minAttributeToValueAllSeries
	 * @param maxAttributeToValueAllSeries
	 * @return
	 */
	public String pGroup(TraverserRecord traverserRecord, Hashtable<String, Double> minAttributeToValueAllSeries, Hashtable<String, Double> maxAttributeToValueAllSeries) {
		
		return StatisticalTest.getPGroup(gameDatasetForPayoff(minAttributeToValueAllSeries, maxAttributeToValueAllSeries), traverserRecord.gameDatasetForPayoff(minAttributeToValueAllSeries, maxAttributeToValueAllSeries));
		
	}
	
	/**
	 * @return
	 */
	protected String showGameSeries() {
		
		String returner = "";
		
		LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> series = getGameSeries();
			
		series = Utils.manualReverse(series);
	
		for ( Entry<AttributeSetIdentifier, Hashtable<String,Double>> entry : series.entrySet() ) {
			
			returner += "\n" + entry; 
			
		}
		
		return returner;
		
	}
	
	/**
	 * Allows to see performance in an 'average round'.
	 * 
	 * That is Round 0, Game 0 + Round 0, Game 1 + ... / Total games. For each round.
	 * 
	 * ~MDC 31/7/14 This could be much neater.
	 * 
	 * @return
	 */
	public LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> getRoundSeries() {
	
		// Create map to hold round numbers to average values for all attributes within that round
		LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>> cumulativeRoundsAttributeToValue = new LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>>();
		
		int attributeAdditions = 0; 
		
		// For each record 
		for ( Entry<AttributeSetIdentifier, Hashtable<String, Double>> attributeEntry : attributeToValue.entrySet() ) {
			
			// If this is a round entry
			if ( attributeEntry.getKey().getGameOrRound().equals("Round") ) {
	
				// For each attribute entry within that round
				for ( Entry<String, Double> attributeToValueEntry : attributeEntry.getValue().entrySet() ) {
					
					// If an entry for the round doesn't exist within our cumulative map (should only happen for first game processed)...
					if ( !cumulativeRoundsAttributeToValue.containsKey( new AttributeSetIdentifier(attributeEntry.getKey().getRoundNumber()) ) ) {
					
						// Create it
						cumulativeRoundsAttributeToValue.put(new AttributeSetIdentifier(attributeEntry.getKey().getRoundNumber()), new Hashtable<String, Double>());
					
					}
				
					// See if an entry for this attribute already exists at this round number, if it does, add it on 
					if ( cumulativeRoundsAttributeToValue.get(new AttributeSetIdentifier(attributeEntry.getKey().getRoundNumber())).containsKey(attributeToValueEntry.getKey()) ) {
						
						cumulativeRoundsAttributeToValue.get(new AttributeSetIdentifier(attributeEntry.getKey().getRoundNumber())).put(attributeToValueEntry.getKey(), cumulativeRoundsAttributeToValue.get(new AttributeSetIdentifier(attributeEntry.getKey().getRoundNumber())).get(attributeToValueEntry.getKey()) + attributeToValueEntry.getValue() );
					
						attributeAdditions++;
						
					// Otherwise create this entry, at that round number
					} else {
						
						cumulativeRoundsAttributeToValue.get(new AttributeSetIdentifier(attributeEntry.getKey().getRoundNumber())).put(attributeToValueEntry.getKey(), attributeToValueEntry.getValue());
						
						attributeAdditions++;
						
					}
					
				}
			
			}
		
		}
		
		/* We now have a map from each round number to a map from each attribute to their cumulative values.
		Complete by creating a new map to the average value: */
		
		LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>> averageRoundAttributeToValue = new LinkedHashMap<AttributeSetIdentifier, Hashtable<String, Double>>();
		
		for (Entry<AttributeSetIdentifier, Hashtable<String, Double>> attribute : cumulativeRoundsAttributeToValue.entrySet()) {
			
			for ( Entry<String, Double> attributeCumulative: attribute.getValue().entrySet()) {
				
				// No entry in the average mapping from round to attribute:value exists
				if ( !averageRoundAttributeToValue.containsKey( attribute.getKey() ) ) {
				
					// Create it
					averageRoundAttributeToValue.put(attribute.getKey(), new Hashtable<String, Double>());
				
				}
				
				/* Get the existing key, be it previously created, and put the key of the current attribute, plus the value
				   of the current attribute divided by the number of rounds, as shown in the cumulative mapping. 
				   
				   To work out `how many round 1s' etc (how many games), we priorly calculate the number of round attribute
				   additions for this traverser. Taking this number, divided by the number of attributes, we work out the number
				   of attributes within one column. Dividing this number by the number of rounds in a game gives the number of games.
				   */
				averageRoundAttributeToValue.get(attribute.getKey()).put(attributeCumulative.getKey(), cumulativeRoundsAttributeToValue.get(attribute.getKey()).get(attributeCumulative.getKey()) / ( ( attributeAdditions / attribute.getValue().entrySet().size() ) / cumulativeRoundsAttributeToValue.entrySet().size()) ); 
			
			}
			
		}
		
		LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>> series = new LinkedHashMap<AttributeSetIdentifier, Hashtable<String,Double>>(averageRoundAttributeToValue);
		
		//series = Utils.manualReverse(series);
		
		return series;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String printGameAverage() {
	
		return traverser + " " + attributeToGameMeasure(TraverserDatasetMeasure.MEAN);
		
	}
	
	/**
	 * @return
	 */
	public String printSeries() {
		
		return traverser + " " + showGameSeries();
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		String traverserA = traverser;
		String traverserB = ((TraverserRecord) obj).traverser;
		
		String opponentsA = opponents;
		String opponentsB = ((TraverserRecord) obj).opponents;
		
		//return traverserA.equals(traverserB) && opponentsA.equals(opponentsB);
		
		return traverserA.equals(traverserB);
		
	}
	
	/**
	 * Actually used in a subclass, but put here for ease.
	 */
	protected boolean showOpponents = true;
	
	/**
	 * 
	 */
	public void switchShowOpponents() {
		
		showOpponents = showOpponents == true ? false : true;
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		if ( showOpponents && opponents != null ) {
			
			return traverser + " vs " + opponents;
			
		} else {
			
			return traverser;
			
		}
		
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TraverserRecord o) {
		
		if ( getTraverser().substring(1).compareTo(o.getTraverser().substring(1)) >= ComparatorResult.AFTER ) {
			
			return ComparatorResult.AFTER;
			
		} else if ( getTraverser().substring(1).compareTo(o.getTraverser().substring(1)) <= ComparatorResult.BEFORE ) {
			
			return ComparatorResult.BEFORE;
			
		} else {
			
			return ComparatorResult.EQUAL;
			
		}
		
	}
		
}

