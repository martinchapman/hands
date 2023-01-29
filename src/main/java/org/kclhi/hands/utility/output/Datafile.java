package org.kclhi.hands.utility.output;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.kclhi.hands.utility.ComparatorResult;

/**
 * @author Martin
 *
 */
public class Datafile implements Comparable<Datafile> {

	/**
	 * 
	 */
	protected String properties;
	
	/**
	 * 
	 */
	protected Path path;
	
	/**
	 * 
	 */
	protected String identifier;
	
	/**
	 * @param identifier
	 */
	public void setIdentifier(String identifier) {
		
		this.identifier = identifier;
		
	}
	
	/**
	 * @return
	 */
	public Path getPath() {
		
		return path;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((identifier == null) ? 0 : identifier.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		//System.out.println("Comparing: " + identifier + " " + ((Datafile)obj).identifier);
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Datafile other = (Datafile) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		return true;
	}

	/**
	 * @param properties
	 * @param filepath
	 */
	public Datafile(String properties, Path path) {
		
		this.properties = properties;
				
		this.path = path;
		
		this.identifier = path.toString();
		
	}
	
	/**
	 * @param identifier
	 */
	public Datafile(String identifier) {
		
		this.identifier = identifier;
		
	}
	
	/**
	 * 
	 */
	boolean showEnclosingFolder = true;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		String filename = path.getFileName().toString();
		
		String enclosingFolder = path.getParent().getFileName().toString();
		
		int first = filename.indexOf("_");
		int second = filename.indexOf("_", first + 1);
		
		if ( second != -1 ) {
			
			filename = filename.substring(0, second);
			
		}
		
		if ( showEnclosingFolder ) {
			
			return enclosingFolder + "/" + filename.replace(".csv", "") + " (" + ( ( path.toFile().length() / 1024 ) / 1024 ) + "MB): " + properties;
		
		} else {
			
			return filename.replace(".csv", "") + " (" + ( ( path.toFile().length() / 1024 ) / 1024 ) + "MB): " + properties;
			
		}
		
	}

	private LocalDateTime extractDate(Path path) {
		
		String stringPath = path.toFile().getName();
		
		stringPath = stringPath.replace(".csv", "");
		
		if ( stringPath.contains("GRAPHED") ) stringPath = stringPath.substring(0, stringPath.indexOf("G") - 1);
	
		if ( stringPath.contains("-") ) stringPath = stringPath.substring(stringPath.indexOf("-") + 1);
		
		if ( stringPath.contains("-") ) throw new UnsupportedOperationException("Filename not correct format for datafile comparison");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		
		return LocalDateTime.from(formatter.parse(stringPath));
		
		
	}
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Datafile o) {
		
		return extractDate(path).compareTo(extractDate(o.path));
		
	}
	
}
