package org.kclhi.hands.utility.output;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @author Martin
 *
 */
public class GroupedHiderRecords extends HiderRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param fileRelatingTo
	 * @param hider
	 */
	public GroupedHiderRecords(Path fileRelatingTo, String hider) {
	
		super(fileRelatingTo, hider);
		
		allHiders = new ArrayList<HiderRecord>();

	}

	/**
	 * @param hider
	 */
	public GroupedHiderRecords(String hider) {
		
		super(hider);
		
		allHiders = new ArrayList<HiderRecord>();
		
	}
	
	public GroupedHiderRecords() {
		
		this("");
		
	}

	/**
	 * 
	 */
	private ArrayList<HiderRecord> allHiders;
	
	/**
	 * 
	 */
	public void clearAllHiders() {
		
		allHiders.clear();
		
	}
	
	/**
	 * 
	 */
	private boolean showMulti = true;
	
	/**
	 * @return
	 */
	public ArrayList<HiderRecord> allHiders() {
		
		ArrayList<HiderRecord> allHiders = new ArrayList<HiderRecord>();
		
		showMulti = false;
		
		allHiders.add(this);
		
		allHiders.addAll(allHidersNoOuter());
		
		return allHiders;
		
	}
	
	/**
	 * @return
	 */
	public ArrayList<HiderRecord> allHidersNoOuter() {
		
		return allHiders;
		
	}
	
	/**
	 * @param otherHider
	 */
	public void addHider(HiderRecord otherHider) {
		
		if ( traverser.equals("") ) {
			
			duplicateRecord(otherHider);
			
		} else {
			
			allHiders.add(otherHider);
			
		}
		
		
	}
	
	/**
	 * @param record
	 */
	public void duplicateRecord(TraverserRecord record) {
		
		super.duplicateRecord(record);
		
		try {
		
			this.allHiders = new ArrayList<HiderRecord>(((GroupedHiderRecords)record).allHidersNoOuter());
		
		} catch (ClassCastException e) {
			
			// ~MDC 19/8 Messy
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see Utility.output.HiderRecord#toString()
	 */
	public String toString() {
		
		if ( showMulti ) {
		
			return "(Multi) " + super.toString();
		
		} else {
			
			return super.toString();
			
		}
		
	}
	
}
