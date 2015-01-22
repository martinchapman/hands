package Utility.output;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;

public class HiderRecord extends TraverserRecord {
	
	/**
	 * 
	 */
	private ArrayList<TraverserRecord> seekersAndAttributes;
	
	/**
	 * 
	 */
	private Path fileRelatingTo;
	
	/**
	 * @return
	 */
	public Path getFileRelatingTo() {
		
		return fileRelatingTo;
	
	}

	/**
	 * @return
	 */
	public ArrayList<TraverserRecord> getSeekersAndAttributes() {
		
		return seekersAndAttributes;
	
	}
	
	/**
	 * @param hider
	 */
	public HiderRecord(String hider) {
		
		super(hider);
		
		seekersAndAttributes = new ArrayList<TraverserRecord>();
	
	}
	
	/**
	 * @param fileRelatingTo
	 * @param hider
	 */
	public HiderRecord(Path fileRelatingTo, String hider) {
		
		super(hider);
		
		this.fileRelatingTo = fileRelatingTo;
		
		seekersAndAttributes = new ArrayList<TraverserRecord>();
	
	}
	
	/**
	 * @param seekerRecord
	 */
	public void addSeeker(TraverserRecord seekerRecord) {
		
		setCategory(getCategory() + " " + seekerRecord.getTraverser());
		
		seekerRecord.setCategory(getTraverser());
		
		seekersAndAttributes.add(seekerRecord);
		
	}
	
	/**
	 * @param seeker
	 * @return
	 */
	public boolean containsSeeker(TraverserRecord seeker) {
		
		return seekersAndAttributes.contains(seeker);
		
	}
	
	/**
	 * @param seeker
	 * @return
	 */
	public TraverserRecord getSeeker(String seeker) {
	
		return seekersAndAttributes.get(seekersAndAttributes.indexOf(new TraverserRecord(seeker)));
		
	}
	
	/**
	 * @return
	 */
	public HashSet<String> getSeekerAttributes() {
		
		return seekersAndAttributes.get(0).getAttributes();
		
	}
	
	/* (non-Javadoc)
	 * @see Utility.output.TraverserRecord#getRelativisedAverageGameAttributeValue(java.lang.String)
	 */
	public double getAverageGameAttributeValue(String attribute) {
		
		if (attribute.contains("Score")) {
			
			System.out.println(this);
			
			System.out.println("--------------------");
			
			double hiderCost = super.getRelativisedAverageGameAttributeValue("Cost");
			
			double cumulativeRelativisedSeekerCost = 0.0;
			
			for ( TraverserRecord hidersSeeker : seekersAndAttributes ) {
				
				System.out.println(hidersSeeker);
				
				System.out.println("--------------------");
				
				cumulativeRelativisedSeekerCost += hidersSeeker.getRelativisedAverageGameAttributeValue("Cost");
				
			}
			
			double seekerCost = cumulativeRelativisedSeekerCost / seekersAndAttributes.size();
			
			System.out.println("------------->:" + seekerCost + " " + hiderCost);
			
			return seekerCost - hiderCost;
			
		} else {
		
			return super.getAverageGameAttributeValue(attribute);
		
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		return traverser + " Seekers: " + seekersAndAttributes.toString().replace("[", "").replace("]", "");
		
	}
	
	/**
	 * @return
	 */
	public String printStats() {
		
		String returner = "\n-------------------\n";
		returner += "\nAverages:\n";
		returner += "\n-------------------\n";
		
		returner += "\n" + traverser + " " + calculateGameAverage(false) + "\n";
		
		for ( TraverserRecord seeker : seekersAndAttributes ) returner += "\n" + seeker.printGameAverage() + "\n";
		
		returner += "\n-------------------\n";
		returner += "\nSeries:\n";
		returner += "\n-------------------\n";
		
		returner += "\n" + traverser + " " + showGameSeries() + "\n";
		
		for ( TraverserRecord seeker : seekersAndAttributes ) returner += "\n" + seeker.printSeries() + "\n";
		
		return returner;
		
	}

}
