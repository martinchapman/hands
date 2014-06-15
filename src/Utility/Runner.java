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

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Utility.output.HiderRecord;
import Utility.output.OutputManager;
import Utility.output.TraverserRecord;

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
	
	private JButton startSelected;
	private JList<String> queueList;
	
	/**
	 * @author Martin
	 *
	 */
	public interface PostDelete 
    {
		
		public void postDelete(String deleted);
		
    }

	/**
	 * @param list
	 * @param model
	 * @param deleted
	 */
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
		
		JTabbedPane tabbedPane = new JTabbedPane();
		
		add(tabbedPane);
		
		//
		
		simulationsTab(tabbedPane);
		
		outputTab(tabbedPane);
		
		//
		
		pack();
		
		setVisible(true);
		
	}
	
	/**
	 * @param tabbedPane
	 */
	private void outputTab(JTabbedPane tabbedPane) {
		
		final OutputManager outputManager = new OutputManager();
		
		//////
		
		JPanel outputTab = new JPanel();
		
		outputTab.setLayout(new BorderLayout());
		
		tabbedPane.addTab("Output", outputTab);
		
		////
		
		JPanel northPane = new JPanel();
		
		northPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		outputTab.add(northPane, BorderLayout.NORTH);
		
		//
		
		JButton collateOutput = new JButton("Process Output");
		
		northPane.add(collateOutput);
		
		final DefaultListModel<HiderRecord> outputFeedback = new DefaultListModel<HiderRecord>();

		collateOutput.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				outputManager.processOutput();
				
				//System.out.println(outputManager.printAllStats());
				
				for (ArrayList<HiderRecord> fileHiderRecord : outputManager.getFileHiderRecords()) {
					
					for (HiderRecord hiderRecord : fileHiderRecord) {
						
						outputFeedback.addElement(hiderRecord);
						
					}
					
					// Mark end of Hiders in that game
					
					outputFeedback.addElement(new HiderRecord("") {
						
						public String toString() {
							
							return "-----";
							
						}
						
					});
					
				}
				
			}
			
		});
		
		//
		
		JButton deleteOutputFiles = new JButton("Delete output files");
		
		deleteOutputFiles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				outputManager.removeAllOutputFiles();
				
			}
			
		});
		
		northPane.add(deleteOutputFiles);
		
		////
		
		JPanel centerPane = new JPanel();
		
		centerPane.setLayout(new BorderLayout());
		
		outputTab.add(centerPane, BorderLayout.CENTER);
		
		//
		
		final JList<HiderRecord> outputFeedbackList = new JList<HiderRecord>(outputFeedback);
		
		final JComboBox<String> measure = new JComboBox<String>();
		
		outputFeedbackList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				if (e.getValueIsAdjusting()) return;
				
				if (outputFeedbackList.getSelectedIndex() == -1 || outputFeedbackList.getSelectedValue().toString().equals("-----")) return;
				
				for ( String attribute : outputFeedbackList.getSelectedValue().getSeekerAttributes() ) {
					
					measure.addItem(attribute);
					
				}
				
			}
			
		});
		
		centerPane.add(outputFeedbackList, BorderLayout.WEST);
		
		////
		
		JPanel centerPaneRight = new JPanel();
		
		centerPaneRight.setLayout(new BorderLayout());
		
		centerPane.add(centerPaneRight, BorderLayout.EAST);
		
		//
		
		JPanel centerPaneRightCenter = new JPanel(new GridLayout());
		
		centerPaneRight.add(centerPaneRightCenter, BorderLayout.CENTER);
		
		//
		
		final JRadioButton seekers = new JRadioButton("Seekers");
		
		centerPaneRightCenter.add(seekers);
		
		seekers.setSelected(true);
		
		final JRadioButton hiders = new JRadioButton("Hiders");
		
		centerPaneRightCenter.add(hiders);
		
		ButtonGroup hidersSeekers = new ButtonGroup();
		
		hidersSeekers.add(hiders);
		
		hidersSeekers.add(seekers);
	    
		//
		
		seekers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (outputFeedbackList.getSelectedIndex() == -1 || outputFeedbackList.getSelectedValue().toString().equals("-----")) return;
				
				measure.removeAllItems();
				
				for ( String attribute : outputFeedbackList.getSelectedValue().getSeekerAttributes() ) {
					
					measure.addItem(attribute);
					
				}
				
			}
			
		});
		
		hiders.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (outputFeedbackList.getSelectedIndex() == -1 || outputFeedbackList.getSelectedValue().toString().equals("-----")) return;
				
				measure.removeAllItems();
				
				for ( String attribute : outputFeedbackList.getSelectedValue().getAttributes() ) {
					
					measure.addItem(attribute);
					
				}
				
			}
			
		});
		
		//
		
		centerPaneRightCenter.add(measure);
		
		//
		
		JButton generateGraph = new JButton("Generate graph");
		
		generateGraph.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (outputFeedbackList.getSelectedIndex() == -1 || outputFeedbackList.getSelectedValue().toString().equals("-----")) return;
				
				if (seekers.isSelected()) {
				
					// Only works for a single selected HiderRecord with a set of seekers
					if (outputFeedbackList.getSelectedValuesList().size() > 1) return;
					
					outputManager.showSeekersLineGraphForAttribute(outputFeedbackList.getSelectedValue(), (String)measure.getSelectedItem());
				
				} else if (hiders.isSelected()) {
					
					// Hiders are selected in the GUI by selecting them as multiple items from the list
					ArrayList<TraverserRecord> hiders = new ArrayList<TraverserRecord>();
					
					for ( TraverserRecord hider : outputFeedbackList.getSelectedValuesList()) {
						
						hiders.add(hider);
						
					}
					
					outputManager.showHidersLineGraphForAttribute(hiders, hiders.toString(), (String)measure.getSelectedItem());
					
				}
				
			}
		
		});
		
		centerPaneRight.add(generateGraph, BorderLayout.SOUTH);
		
	}
	
	private void simulationsTab(JTabbedPane tabbedPane) {
		
		//////
		
		JPanel simulationsTab = new JPanel();
		
		simulationsTab.setLayout(new BorderLayout());
		
		tabbedPane.addTab("Simulations", simulationsTab);
		
		////
		
		JPanel northPane = new JPanel();
		
		northPane.setLayout(new GridLayout(2, 2));
		
		simulationsTab.add(northPane, BorderLayout.NORTH);
		
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
		
		fixedOrRandom.addItem("random");
		fixedOrRandom.addItem("fixed");
		
		parameters.add(fixedOrRandom);
		
		//
		
		parameters.add(new JLabel("Edge traversal decrement:"));
		
		edgeTraversalDecrement = new JTextField("0");
		
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
		hiderList.addItem("RandomDirection");
		hiderList.addItem("RandomFixedDistance");
		hiderList.addItem("LowEdgeCostFixedDistance");
		hiderList.addItem("MinimumConnectivity");
		hiderList.addItem("MaxDistance");
		
		//
		
		hiderList.addItem("FullyBiasHider");
		hiderList.addItem("LooselyBiasHider");
		hiderList.addItem("VariableBiasHider");
		
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
				
				if (simulationHidersModel.toArray().length == 1) { 
					
					start.setEnabled(false);
					
					queue.setEnabled(false);
					
				}
				
			}
			
		});
		
		hiders.add(simulationHiders);
		
		addHider.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!simulationHidersModel.contains(hiderList.getSelectedItem().toString())) simulationHidersModel.addElement(hiderList.getSelectedItem().toString());
				
				start.setEnabled(true);
				
				queue.setEnabled(true);
				
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
		seekerList.addItem("BacktrackPath");
		seekerList.addItem("BreadthFirstSearch");
		seekerList.addItem("BreadthFirstSearchLowCost");
		seekerList.addItem("FixedStartRandomWalk");
		seekerList.addItem("LowEdgeCost");
		seekerList.addItem("RandomWalk");
		seekerList.addItem("DepthFirstSearch");
		seekerList.addItem("DepthFirstSearchLowCost");
		
		// 
		
		seekerList.addItem("HighProbabilitySeeker");
		
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
				
				if (simulationSeekersModel.toArray().length == 1) { 
					
					start.setEnabled(false);
					
					queue.setEnabled(false);
					
				}
				
			}
			
		});
		
		seekers.add(simulationSeekers);
		
		addSeeker.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!simulationSeekersModel.contains(seekerList.getSelectedItem().toString())) simulationSeekersModel.addElement(seekerList.getSelectedItem().toString());
				
				start.setEnabled(true);
				
				queue.setEnabled(true);
				
			}
			
		});
		
		//
		
		northPane.add(seekers);
		
		////////
		
		queueListModel = new DefaultListModel<String>();
		
		queueList = new JList<String>(queueListModel);
		
		queueList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
			
				updateUISettings(queueList.getSelectedValue());
				
				startSelected.setEnabled(true);
				
			}
			
		});
		
		deleteOnClick(queueList, queueListModel, new PostDelete() {

			@Override
			public void postDelete(String deleted) {
				
				if (queueListModel.toArray().length == 1) { 
					
					startQueue.setEnabled(false);
					
					startSelected.setEnabled(false);
					
				}
				
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
		
		simulationsTab.add(queueList, BorderLayout.CENTER);
		
		////////
		
		JPanel controls = new JPanel();
		
		controls.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		startSelected = new JButton("Start selected");
		
		startSelected.setEnabled(false);
		
		controls.add(startSelected);
		
		start = new JButton("Start setup");
		
		controls.add(start);
		
		queue = new JButton("Queue simulation");
		
		controls.add(queue);
		
		startQueue = new JButton("Start queue");
		
		startQueue.setEnabled(false);
		
		controls.add(startQueue);
		
		//
		
		simulationsTab.add(controls, BorderLayout.SOUTH);
		
		////

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
        
        startSelected.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String[] simulation = queueList.getSelectedValue().split(",\\s");
				
	        	runSimulation(Integer.parseInt(simulation[0]), simulation);
				
			}
        	
        });
        
        //
        
        queue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!queueListModel.contains(Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1))) {
				
					queueListModel.addElement(Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1));
				
				}
				
				try {
					Utils.writeToFile(new FileWriter(FILEPREFIX + "simulationSchedule.txt", true), Arrays.toString(getUISettings()).substring(1, Arrays.toString(getUISettings()).length() - 1) + "\n");
				
				} catch (IOException e1) {
				
					// TODO Auto-generated catch block
					e1.printStackTrace();
				
				}
				
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
	
	private void updateUISettings(String settings) {
		
		if (settings == null) return;
		
		String[] simulationParameters = settings.split(",\\s");
		
		int arrayPosition = 0;
		
		simulationHidersModel.clear();
		
		simulationSeekersModel.clear();
		
		for (String param : simulationParameters) {
      	  
			if (param.indexOf('{') != -1) {
				
				Pair<String, String> paramPair = Utils.stringToArray(param, "(\\{([0-9a-zA-Z]+),([0-9a-zA-Z.]+)\\})").get(0);
  
				if (paramPair.getElement0().equals("Topology")) {
		  
					topologies.setSelectedItem(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("NumberOfNodes")) { 
		  
					numberOfNodes.setText(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("NumberOfHideLocations")) { 
		  
					numberOfHiddenItems.setText(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("Rounds")) { 
		  
					numberOfRounds.setText(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("EdgeWeight")) { 
		  
					costOfEdgeTraversal.setText(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("FixedOrUpperWeight")) { 
		  
					fixedOrRandom.setSelectedItem(paramPair.getElement1());
		  
				} else if (paramPair.getElement0().equals("EdgeTraversalDecrement")) { 
			  
					edgeTraversalDecrement.setText(paramPair.getElement1());
			  
				} 
	  
			} else if (param.indexOf('[') != -1) {
  
				ArrayList<Pair<String, String>> types = Utils.stringToArray(param, "(\\[([0-9a-zA-Z]+),([0-9]+)\\])");
  
				for (Pair<String, String> traverser : types) {
	  
					// Is Hider 
					if (arrayPosition == 1)  {
	  
						simulationHidersModel.addElement(traverser.getElement0());
	  
					// Is Seeker  
					} else {
			  
						simulationSeekersModel.addElement(traverser.getElement0());
			  
					}
		  
				}
	  
			} else {
	  
				numberOfGames.setText(param);
	  
			}
  
			arrayPosition++;
			  
		}
		
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
			
			"{EdgeTraversalDecrement," + edgeTraversalDecrement.getText() + "}" // % discount gained by an agent for having traversed an edge before (100 = no discount; < 100 = discount)
		  		 
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
