package utility;

import java.util.ArrayList;
import java.util.Arrays;

import org.jfree.util.StringUtils;

public class FixScale {

	public FixScale() {
		
		ArrayList<Pair<String, String>> tupleList = new ArrayList<Pair<String, String>>();
		ArrayList<String> thisSectionLines = new ArrayList<String>();
		
		for (String line : Utils.readFromFile(Utils.FILEPREFIX + "ShiftRight.txt")) {
			
			thisSectionLines.add(line);
			
			ArrayList<String> tuplesInLine = new ArrayList<String>();
			
			if ( line.contains("--") ) {
				
				tuplesInLine.addAll(new ArrayList<String>(Arrays.asList(line.split("--"))));
				
			} else {
				
				tuplesInLine.add(line);
				
			}
			
			for ( String tuple : tuplesInLine ) {
				
				if (!tuple.contains(",")) continue;
				
				tupleList.add(new Pair<String, String>(
						
						tuple.substring(tuple.indexOf("(") + 1, tuple.indexOf(",")),
						tuple.substring(tuple.indexOf(",") + 1, tuple.indexOf(")"))
						
						)
				
				);
				
			}
			
			if ( line.contains("ENDSHIFT") ) { 
				
				//System.out.println(tupleList);
				
				//System.exit(0);
				
				ArrayList<String> editedLines = new ArrayList<String>();
				
				for ( String storedLine : thisSectionLines ) {
					
					if ( storedLine.length() - storedLine.replace(",", "").length() == 1 ) {
						
						for ( Pair<String, String> storedTuple : tupleList ) {
						
							if ( storedLine.contains(storedTuple.getElement0() + ",") ) {
								
								if ( tupleList.indexOf(storedTuple) + 1 < tupleList.size() ) {
									
									storedLine = replaceLast(storedLine, storedTuple.getElement0(), tupleList.get(tupleList.indexOf(storedTuple) + 1).getElement0());
									
								} else {
									
									storedLine = replaceLast(storedLine, storedTuple.getElement0(), "" + (Double.parseDouble(storedTuple.getElement0()) + 0.103));
									
								}
								
								//System.out.println("breaking");
								break;
								
							}
						
						}
						
					} else {
						
						//System.out.println("len : " + (storedLine.length() - storedLine.replace(",", "").length()));
						
						int replacements = 0;
						
						//for (int i = 0; i < storedLine.length() - storedLine.replace(",", "").length(); i++) { // ( Pair<String, String> storedTuple : tupleList ) {
						for ( Pair<String, String> storedTuple : tupleList ) {
							
							//Pair<String, String> storedTuple = tupleList.get(i);
							
							if ( storedLine.contains(storedTuple.getElement0() + ",") ) {
								
								if ( tupleList.indexOf(storedTuple) + 1 < tupleList.size() ) {
									
									/*if ( instancesOf(storedLine, storedTuple.getElement0()) > 2 ) {
										
			
									}*/
									
									storedLine = replaceLast(storedLine, storedTuple.getElement0(), tupleList.get(tupleList.indexOf(storedTuple) + 1).getElement0());
									
								} else {
									
									storedLine = replaceLast(storedLine, storedTuple.getElement0(), "" + (Double.parseDouble(storedTuple.getElement0()) + 0.103));
									
								}
								
								replacements++;
								
							}
							
							if (replacements >= storedLine.length() - storedLine.replace(",", "").length()) break;
							
						}
					
					}
					
					editedLines.add(storedLine);
					
					
					
				}
				
				for ( String editedLine : editedLines ) {
					
					System.out.println(editedLine);
					
				}
				
				//System.exit(0);
				
				tupleList.clear();
				thisSectionLines.clear();
				
			}
		}
		
	}
	
	private int instancesOf(String str, String phrase) {
		
		int lastIndex = 0;
		int count = 0;

		while(lastIndex != -1){

		    lastIndex = str.indexOf(phrase,lastIndex);

		    if(lastIndex != -1){
		        count ++;
		        lastIndex += phrase.length();
		    }
		}
		
		return count;
		
	}
	
	public static String replaceLast(String text, String regex, String replacement) {
    
		int endIndex = text.length() - 1;
			
		for ( int startIndex = text.length() - 2; startIndex >= 0; startIndex-- ) {
			
			if ( text.substring(startIndex, endIndex).contains(regex) ) {
				
				if ( !text.substring(startIndex - 1, endIndex).contains("," + regex) ) {
					
					return text.substring(0, startIndex) + text.substring(startIndex, endIndex).replace(regex, replacement) + text.substring(endIndex, text.length());
					
				} else {
					
					endIndex = startIndex;
					
				}
				
			}
			
		}
		
		return text;
		
		//return text.replaceFirst("(?s)"+regex+"(?!.*?"+regex+")", replacement);
    
	}
	
	public static void main(String[] args) {
		
		new FixScale();
		
	}

}
