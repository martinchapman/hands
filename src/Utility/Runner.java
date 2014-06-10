package Utility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

/**
 * @author Martin
 *
 */
public class Runner extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	final static String FILEPREFIX = "Output/";
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) {
		
		new Runner();

	}
	
	private JComboBox<String> topologies;
	
	private JTextField numberOfNodes;
	
	private JTextField numberOfHiddenItems;
	
	private JTextField costOfEdgeTraversal;
	
	private JComboBox<String> fixedOrRandom;
	
	private JTextField edgeTraversalDecrement;
	
	private JTextField numberOfRounds;
	
	private JTextField numberOfGames;
	
	private DefaultListModel<String> simulationHidersModel;
	
	private DefaultListModel<String> simulationSeekersModel;
	
	private DefaultListModel<String> queueListModel;
	
	private JButton start;
	
	private JButton queue;
	
	private JButton startQueue;
	
	public interface PostDelete 
    {
		
		public void postDelete(String deleted);
		
    }

	private void deleteOnClick(final JList<String> list, final DefaultListModel<String> model, final PostDelete deleted) {
		
		list.addMouseListener(new MouseAdapter() {
		    
			public void mouseClicked(MouseEvent evt) {
		        
		        if (evt.getClickCount() == 2) {
		       
		        	/* After something has been deleted, pass what has been deleted to the 'post delete' method
		        	 * for processing
		        	 */
		        	deleted.postDelete(model.getElementAt(list.locationToIndex(evt.getPoint())));
		        	
		        	model.remove(list.locationToIndex(evt.getPoint()));
		        	
		        } 
		        
		    }
			
		});
		
	}
	
	/**
	 * 
	 */
	private void generateGUI() {
		
		setSize(new Dimension(500, 800));
		
		setLayout(new BorderLayout());
		
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		////
		
		JPanel northPane = new JPanel();
		
		northPane.setLayout(new GridLayout(2, 2));
		
		add(northPane, BorderLayout.NORTH);
		
		////
		
		JPanel parameters = new JPanel();
		
		parameters.setLayout(new GridLayout(6, 2));
		
		parameters.setBorder(new TitledBorder("Parameters"));
		
		//
		
		parameters.add(new JLabel("Topology:"));
		
		topologies = new JComboBox<String>();
		
		topologies.addItem("random");
		topologies.addItem("ring");
		topologies.addItem("scalefree");
		
		parameters.add(topologies);
		
		//
		
		parameters.add(new JLabel("Number of nodes:"));
		
		numberOfNodes = new JTextField("100");
		
		parameters.add(numberOfNodes);
		
		//
		
		parameters.add(new JLabel("Number of hide locations:"));
		
		numberOfHiddenItems = new JTextField("5");
		
		parameters.add(numberOfHiddenItems);
		
		// 
		
		parameters.add(new JLabel("Cost of traversing an edge:"));
		
		costOfEdgeTraversal = new JTextField("100.0");
		
		parameters.add(costOfEdgeTraversal);
		
		//
		
		parameters.add(new JLabel("Fixed or random cost:"));
		
		fixedOrRandom = new JComboBox<String>();
		
		fixedOrRandom.addItem("fixed");
		fixedOrRandom.addItem("random");
		
		parameters.add(fixedOrRandom);
		
		//
		
		parameters.add(new JLabel("Edge traversal decrement:"));
		
		edgeTraversalDecrement = new JTextField("10");
		
		parameters.add(edgeTraversalDecrement);
		
		//
		
		northPane.add(parameters);
		
		////
		
		JPanel hiders = new JPanel();
		
		hiders.setLayout(new GridLayout(3, 1));
		
		hiders.setBorder(new TitledBorder("Hiders"));
		
		//
		
		hiders.add(new JLabel("Hider types:"));
		
		//
		
		JPanel hiderListAndButton = new JPanel();
		
		hiderListAndButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		final JComboBox<String> hiderList = new JComboBox<String>();
		
		hiderList.addItem("Random");
		hiderList.addItem("MinimumConnectivity");
		
		hiderListAndButton.add(hiderList);
		
		JButton addHider = new JButton("Add hider");
		
		hiderListAndButton.add(addHider);
		
		hiders.add(hiderListAndButton);
		
		//

		simulationHidersModel = new DefaultListModel<String>();
		
		simulationHidersModel.addElement("Random");
		
		JList<String> simulationHiders = new JList<String>(simulationHidersModel);
		
		deleteOnClick(simulationHiders, simulationHidersModel, new PostDelete() {

			@Override
			public void postDelete(String deleted) {
				
				if (simulationHidersModel.toArray().length == 1) start.setEnabled(false);
				
			}
			
		});
		
		hiders.add(simulationHiders);
		
		addHider.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!simulationHidersModel.contains(hiderList.getSelectedItem().toString())) simulationHidersModel.addElement(hiderList.getSelectedItem().toString());
				
				start.setEnabled(true);
				
			}
			
		});
		
		//
		
		northPane.add(hiders);
		
		////
		
		JPanel simulationParameters = new JPanel();
		
		simulationParameters.setLayout(new GridLayout(2, 2));
		
		simulationParameters.setBorder(new TitledBorder("Simulation Parameters"));
		
		//
		
		simulationParameters.add(new JLabel("Number of rounds:"));
		
		numberOfRounds = new JTextField("1");
		
		simulationParameters.add(numberOfRounds);
		
		//
		
		simulationParameters.add(new JLabel("Number of games:"));
		
		numberOfGames = new JTextField("1");
		
		simulationParameters.add(numberOfGames);
		
		//
		
		northPane.add(simulationParameters);
		
		////
		
		JPanel seekers = new JPanel();
		
		seekers.setLayout(new GridLayout(3, 1));
		
		seekers.setBorder(new TitledBorder("Seekers"));
		
		//
		
		seekers.add(new JLabel("Seeker types:"));
		
		//
		
		JPanel seekerListAndButton = new JPanel();
		
		seekerListAndButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		final JComboBox<String> seekerList = new JComboBox<String>();
		
		seekerList.addItem("ConstrainedRandomWalk");
		seekerList.addItem("LeastConnectedFirst");
		
		seekerListAndButton.add(seekerList);
		
		JButton addSeeker = new JButton("Add seeker");
		
		seekerListAndButton.add(addSeeker);
		
		seekers.add(seekerListAndButton);
		
		//

		simulationSeekersModel = new DefaultListModel<String>();
		
		simulationSeekersModel.addElement("RandomWalk");
		
		JList<String> simulationSeekers = new JList<String>(simulationSeekersModel);
		
		deleteOnClick(simulationSeekers, simulationSeekersModel, new PostDelete() {

			@Override
			public void postDelete(String deleted) {
				
				if (simulationSeekersModel.toArray().length == 1) start.setEnabled(false);
				
			}
			
		});
		
		seekers.add(simulationSeekers);
		
		addSeeker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!simulationSeekersModel.contains(seekerList.getSelectedItem().toString())) simulationSeekersModel.addElement(seekerList.getSelectedItem().toString());
				
				start.setEnabled(true);
				
			}
			
		});
		
		//
		
		northPane.add(seekers);
		
		////////
		
		queueListModel = new DefaultListModel<String>();
		
		JList<String> queueList = new JList<String>(queueListModel);
		
		deleteOnClick(queueList, queueListModel, new PostDelete() {

			@Override
			public void postDelete(String deleted) {
				
				if (queueListModel.toArray().length == 1) startQueue.setEnabled(false);
				
				simulations.remove(deleted);
				
				// Empty the file
				FileWriter writer;
				
				try {
					
					writer = new FileWriter(FILEPREFIX + "simulationSchedule.txt");
					
					writer.write("");
					
					for (String simulation : simulations) { 
						
						writer.append(simulation);
						
					}
					
					writer.close();
					
				} catch (IOException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		});
		
		add(queueList, BorderLayout.CENTER);
		
		////////
		
		JPanel controls = new JPanel();
		
		controls.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		start = new JButton("Start simulation");
		
		controls.add(start);
		
		queue = new JButton("Queue simulation");
		
		controls.add(queue);
		
		startQueue = new JButton("Start queue");
		
		startQueue.setEnabled(false);
		
		controls.add(startQueue);
		
		//
		
		add(controls, BorderLayout.SOUTH);
		
		////

		pack();
		
		setVisible(true);
		
	}
	
	private ArrayList<String> simulations;
	
	/**
	 * 
	 */
	public Runner() {
		
		super("HANDS");
		
		generateGUI();
		
		// Collect list of simulations
        simulations = Utils.readFromFile(FILEPREFIX + "simulationSchedule.txt");
        
        for (String simulation : simulations) { 
        	
        	queueListModel.addElement(simulation);
        	
        	startQueue.setEnabled(true);
        
        }
        
        //
        
        start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println(Arrays.toString(getUISettings()));
				
				runSimulation(Integer.parseInt(numberOfGames.getText()), getUISettings());
				
			}
        	
        });
        
        //
        
        queue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!queueListModel.contains(Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1))) {
				
					queueListModel.addElement(Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1));
				
				}
				
				Utils.writeToFile(FILEPREFIX + "simulationSchedule.txt", Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1) + "\n");
				
				startQueue.setEnabled(true);
				
			}
        	
        });
        
        //
        
        startQueue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				runSimulationList(simulations);
				
			}
        	
        });
       
	}
	
	private String[] getUISettings() {
		
		String hiderParameter = "";
		
		for (Object hider : simulationHidersModel.toArray()) {
			
			hiderParameter += "[" + hider + ",1]";
			
		}
		
		String seekerParameter = "";
		
		for (Object seeker : simulationSeekersModel.toArray()) {
			
			seekerParameter += "[" + seeker + ",1]";
			
		}
		
		return new String[]{ 
					 numberOfGames.getText(),
				     hiderParameter,
					 seekerParameter,
					 "{Topology," + topologies.getSelectedItem().toString() + "}", // Topology
					 "{NumberOfNodes," + numberOfNodes.getText() + "}", // Number of nodes in graph
					 "{NumberOfHideLocations," + numberOfHiddenItems.getText() + "}", // Number of hide locations
					 "{Rounds," + numberOfRounds.getText() + "}", // rounds
					 "{EdgeWeight," + costOfEdgeTraversal.getText() + "}", // cost of traversing an edge
					 "{FixedOrUpperWeight," + fixedOrRandom.getSelectedItem().toString() + "}", // whether cost supplied is static value or the upper bound of a distribution
					 "{EdgeTraversalDecrement," + edgeTraversalDecrement.getText() + "}" // % discount gained by an agent for having traversed an edge before (1.0 = no discount; < 1.0 = discount)
			  		 };  
		
	}
	
	/**
	 * 
	 */
	private void runSimulationList(ArrayList<String> simulations) {
		
		for (String currentSimulation : simulations) {
			
			String[] simulationParameters = currentSimulation.split(",\\s");
			
			// If the simulation is commented out, do not run it.
			if(		simulationParameters[0].equals("//")	) { continue; }
			
			runSimulation(Integer.parseInt(simulationParameters[0]), simulationParameters);
			
		}
		
	}
		
	private void runSimulation(int GAMES, String[] simulationParameters) {
		
		/***********/
		
		// Generate ID For this simulation
		
		String currentSimulationIdentifier = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		Utils.writeToFile(FILEPREFIX + "simRecordID.txt", currentSimulationIdentifier);
		
		Utils.writeToFile(FILEPREFIX + "/Data/" + currentSimulationIdentifier + ".csv", "");
		
		Utils.writeToFile(FILEPREFIX + "/Data/" + currentSimulationIdentifier + ".csv", Arrays.toString(simulationParameters) + "\n");
		
		/***********/
		
		String[] parameters = { "Hiders", 
				  "Seekers",
  				  "Topology", // Topology
  				  "NumberOfNodes", // Number of nodes in graph
  				  "NumberOfHideLocations", // Number of hide locations
  				  "Rounds", // rounds 
  				  "EdgeWeight", // cost of traversing an edge
  				  "FixedOrUpperWeight", // whether cost supplied is static value or the upper bound of a distribution
  				  "EdgeTraversalDecrement" // % discount gained by an agent for having traversed an edge before (100 = no discount; < 100 = discount)
  				  };
  				  
		String[] defaultParameters = { simulationParameters[1],
				 simulationParameters[2],
				 "random", // Topology
				 "100", // Number of nodes in graph
				 "5", // Number of hide locations
				 "120", // rounds
				 "100.0", // cost of traversing an edge
				 "upper", // whether cost supplied is static value or the upper bound of a distribution
				 "0"// % discount gained by an agent for having traversed an edge before (1.0 = no discount; < 1.0 = discount)
		  		  };
			
		/***********/
		
		// Run 'games' of simulation by repeat running program
		
		System.out.println("-----------------------------------------------------------------");
		
	    for(int i = 0; i < GAMES; i++) {
		    	
			System.out.println("Run: " + (i + 1));
			System.out.println("-----------------------------------------------------------------");
		    
			// Remove wildcards from param string and replace with values
			
			for (int j = 0; j < simulationParameters.length; j++) {
	    		  
	    	    if (simulationParameters[j].contains("i*")) { 
	    	    	
	    	    	simulationParameters[j] = 
	    	    	
	    	        simulationParameters[j].replaceAll("(i\\*([0-9]+))", 
	    											   "" + (i * Integer.parseInt(
														               simulationParameters[j].substring( 
														            		   Utils.startIndexOf(simulationParameters[j], "(i\\*([0-9]+))") + 2, 
														            		   Utils.endIndexOf(simulationParameters[j], "(i\\*([0-9]+))")
														            		   )
	    													            		  
	    													      		)
	    													)
	    	    									   );
	    	    
	    	    } else if (simulationParameters[j].contains("i+")) { 
	    	    	
	    	    	simulationParameters[j] = 
	    	    	
	    	        simulationParameters[j].replaceAll("(i\\+([0-9]+))", 
	    	        								   "" + (i + Integer.parseInt(
	    	        										   			simulationParameters[j].substring(
	    	        										   					Utils.startIndexOf(simulationParameters[j], "(i\\+([0-9]+))") + 2, 
	    	        										   					Utils.endIndexOf(simulationParameters[j], "(i\\+([0-9]+))")
	    	        										   			)
	    	        										   	  )
	    	        										)
	    	        								   ); 
	    	    
	    	    } else if (simulationParameters[j].contains(",i")) { 
	    	  		
	    	  		simulationParameters[j] = 
	    	  				
	    	  		simulationParameters[j].replaceAll("(\\,i)", "," + i); 
	    	  		
	    	  	}
    	   
    	    }
		    
			// Alter the default parameters to reflect those that have been input
			
            for (String param : simulationParameters) {
        	  
				  if (param.indexOf('{') != -1) {
				  
					  Pair<String, String> paramPair = Utils.stringToArray(param, "(\\{([0-9a-zA-Z]+),([0-9a-zA-Z.]+)\\})").get(0);
					  
					  defaultParameters[Arrays.asList(parameters).indexOf(paramPair.getElement0())] = paramPair.getElement1(); 
					  
				  }
        	  
            }
		    
            // Construct the param string to supply to the program
            
			String paramString = "";
			  
			for (int k = 0; k < defaultParameters.length; k++) {
				  
				paramString += defaultParameters[k] + " ";
				  
			}
		    
			/***********/
			
			Process proc = null;
			
			try {
				
				proc = Runtime.getRuntime().exec("java -classpath bin:/Users/Martin/Downloads/jgrapht-0.9.0/lib/jgrapht-core-0.9.0.jar HideAndSeek.Main " + i + " " + paramString);
			
			} catch (IOException e1) {
				
				// TODO Auto-generated catch block
				e1.printStackTrace();
			
			}
			
			BufferedReader outputs = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
			BufferedReader errors = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			  
			String line = null;  
			 
			try {
				
				while ((line = outputs.readLine()) != null) {  
				
					System.out.println(line);  
				
				}
				
			} catch (IOException e1) {
				
				// TODO Auto-generated catch block
				e1.printStackTrace();
			
			}
			
			try {
			
				while ((line = errors.readLine()) != null) {  
					
					System.out.println(line);  
				
				}
				
			} catch (IOException e1) {
				
				// TODO Auto-generated catch block
				e1.printStackTrace();
			
			} 
			  
			try {
			
				proc.waitFor();
			
			} catch (InterruptedException e) { System.out.println(e); }
			 
			System.out.println("-----------------------------------------------------------------");
			
	    } // End of game run loop
		
	}
	
}
