/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Simulators 
 * 20/04/2014
 * 
 */
package asgn2Simulators;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.*;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;

/**
 * @author hogan
 *
 */
@SuppressWarnings("serial")
public class GUISimulator extends JFrame implements Runnable {
 
	//GUI sizing
	private final Integer WIDTH = 800,
						  HEIGHT = 700;
	
	//new object instances to run simulation with
	private CarPark carPark;
	private Simulator sim;
	private Log log;
	
	//simulation variables
	private int maxCarSpaces,
				smallCarSpaces,
				motorCycleSpaces,
				maxQueueSize,
				simSeed;
	
	private double carProb,
				   smallCarProb,
				   motoProb,
				   intendedStayMean,
				   intendedStaySD;
	
	private JTextArea logOutputTextArea;
	
	//button to start sim
	private JButton startButton;
	
	//textfields for user inputs
	private JTextField fieldMaxCarSpaces,
     		   		   fieldSmallCarSpaces,
     		   		   fieldMotoSpaces,
     		   		   fieldMaxQueueSize,
     		   		   fieldSimSeed,
     		   		   fieldCarProb,
     		   		   fieldSmallCarProb,
     		   		   fieldMotoProb,
     		   		   fieldIntendedStayMean,
     		   		   fieldIntendedStaySD; 
	
	//tabbed pane for different data views after simulation is run
	private JTabbedPane tabbedData;
	
	/**
	 * constructor for the GUI class, used to create the GUI
	 * and set the textfield params at start
	 * @param args - command line arguments if provided
	 * @throws HeadlessException
	 */
	public GUISimulator(String[] args) throws HeadlessException {
		super("Car Park Simulator");		
		createAndShowGUI();
		
		//if no command line arguments provided set textfields to default
		if (args.length == 0) {
			fieldMaxCarSpaces.setText(Integer.toString(Constants.DEFAULT_MAX_CAR_SPACES));
			fieldSmallCarSpaces.setText(Integer.toString(Constants.DEFAULT_MAX_SMALL_CAR_SPACES));
			fieldMotoSpaces.setText(Integer.toString(Constants.DEFAULT_MAX_MOTORCYCLE_SPACES));
			fieldMaxQueueSize.setText(Integer.toString(Constants.DEFAULT_MAX_QUEUE_SIZE));
			fieldSimSeed.setText(Integer.toString(Constants.DEFAULT_SEED));
			fieldCarProb.setText(Double.toString(Constants.DEFAULT_CAR_PROB));
			fieldSmallCarProb.setText(Double.toString(Constants.DEFAULT_SMALL_CAR_PROB));
			fieldMotoProb.setText(Double.toString(Constants.DEFAULT_MOTORCYCLE_PROB));
			fieldIntendedStayMean.setText(Double.toString(Constants.DEFAULT_INTENDED_STAY_MEAN));
			fieldIntendedStaySD.setText(Double.toString(Constants.DEFAULT_INTENDED_STAY_SD));
			
		} else {//else set to arguments provided
			fieldMaxCarSpaces.setText(args[0]);
			fieldSmallCarSpaces.setText(args[1]);
			fieldMotoSpaces.setText(args[2]);
			fieldMaxQueueSize.setText(args[3]);
			fieldSimSeed.setText(args[4]);
			fieldCarProb.setText(args[5]);
			fieldSmallCarProb.setText(args[6]);
			fieldMotoProb.setText(args[7]);
			fieldIntendedStayMean.setText(args[8]);
			fieldIntendedStaySD.setText(args[9]);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

	}
	
	/*
	 * Method to run the simulation after the startbutton is pressed
	 * appends all log information to the textarea on the GUI and saves log file
	 */
	private void runSimulation() throws IOException, VehicleException, SimulationException {
		//local variable to store log information
		String logOutput = "";
		
		//if user input is valid
		if (checkGUIInputParams()) {
			//initialise class objects with values provided by user
			carPark = new CarPark(maxCarSpaces, smallCarSpaces, motorCycleSpaces, maxQueueSize);
			sim = null;
			log = null; 
			try {
				sim = new Simulator(simSeed, intendedStayMean, intendedStaySD, carProb, smallCarProb, motoProb);
				log = new Log();
			} catch (IOException | SimulationException e1) {
				e1.printStackTrace();
				System.exit(-1);
			}
			
			//create the initial log entry in the log file and append on GUI display
			this.log.initialEntry(this.carPark,this.sim);
			appendLogOutput(initialEntry());
			
			//for each interval of car park being open
			for (int time=0; time<=Constants.CLOSING_TIME; time++) {
				//queue elements exceed max waiting time
				if (!this.carPark.queueEmpty()) {
					this.carPark.archiveQueueFailures(time);
				}
				//vehicles whose time has expired
				if (!this.carPark.carParkEmpty()) {
					//force exit at closing time, otherwise normal
					boolean force = (time == Constants.CLOSING_TIME);
					this.carPark.archiveDepartingVehicles(time, force);
				}
				//attempt to clear the queue 
				if (!this.carPark.carParkFull()) {
					this.carPark.processQueue(time,this.sim);
				}
				// new vehicles from minute 1 until the last hour
				if (newVehiclesAllowed(time)) { 
					this.carPark.tryProcessNewVehicles(time,this.sim);
				}
				//create and store the current times log
				logOutput = carPark.getStatus(time);
				
				//append log to GUI textarea and to the log file
				appendLogOutput(logOutput);
				log.writer.write(logOutput);
			}
			//create final log entry in the log file and append to GUI display
			this.log.finalise(this.carPark);
			appendLogOutput(finalEntry());
		}
	}
	
	/*
	 * method to check the validity of users inputs in the GUI,
	 * if fields are of the wrong type or value the simulation will
	 * not run
	 */
	private boolean checkGUIInputParams() {
		boolean parsed = true;
		String logOutput = "";
		
		//try parse all input fields to their required type
		try {
			maxCarSpaces = Integer.parseInt(fieldMaxCarSpaces.getText());
			smallCarSpaces = Integer.parseInt(fieldSmallCarSpaces.getText());
			motorCycleSpaces = Integer.parseInt(fieldMotoSpaces.getText());
			maxQueueSize = Integer.parseInt(fieldMaxQueueSize.getText());
			simSeed = Integer.parseInt(fieldSimSeed.getText());
		
			carProb = Double.parseDouble(fieldCarProb.getText());
			smallCarProb = Double.parseDouble(fieldSmallCarProb.getText());
			motoProb = Double.parseDouble(fieldMotoProb.getText());
			intendedStayMean = Double.parseDouble(fieldIntendedStayMean.getText());
			intendedStaySD = Double.parseDouble(fieldIntendedStaySD.getText());
		} catch (NumberFormatException e) {//if any fail return false and alert user of correct values
			parsed = false;
			logOutput = "Incorrect Inputs, values must be numbers" +
						"\nCar, small Car, MotorCycle spaces, Queue Size, and Simulation Seed must be Integers" +
						"\nCar, Small Car, MotorCycle probabilities, Intended Stay Mean, and Intended Stay SD must be a Double value";
			appendLogOutput(logOutput);
			return parsed;
		}
		
		//if values are correct type but values are wrong, alert user and return false.
		if (maxCarSpaces < 0 || smallCarSpaces < 0 || motorCycleSpaces < 0 || maxQueueSize < 0 || simSeed < 0 ||
				carProb < 0 || smallCarProb < 0 || motoProb < 0 || intendedStayMean < 0 || intendedStaySD < 0) {
			parsed = false;
			logOutput = "Incorrect Inputs, values cannot be negative. Please resolve and try again";
			appendLogOutput(logOutput);
		}
		
		return parsed;
	}
	
	/*
	 *method to check the supplised command line arguments 
	 */
	private static void checkCommandLineParams(String[] args) {
		//if not exactly 0 or 10 arguments provided terminate the program and alert user
		if ((args.length > 0 && args.length < 10) || args.length > 10) {
			System.err.println("Program terminated: if providing command line arguments there needs to be 10, you provided: " + args.length);
			System.exit(1);
			
		} else if (args.length == 10) {//else if 10 are provided try parsing all values to required type
			for (int i = 0; i < 5; i++) {
				try {
					Integer.parseInt(args[i]);
				} catch (NumberFormatException e) {//if any are incorrect terminate the program and alert user why on console
					System.err.println("Program terminated, First 5 command line arguments must be of type Integer, value that threw this exception: " + args[i]);
					System.exit(1);
				}
			}
			
			for (int i = 5; i < 10; i++) {
				try {
					Double.parseDouble(args[i]);
				} catch (NumberFormatException e) {
					System.err.println("Program terminated, last 5 command line arguments must be of type Double, value that threw this exception: " + args[i]);
					System.exit(1);
				}
			}
		}
	}
	
	/*
	 * Method to get the final state string of the car park
	 */
	private String finalEntry() {
		String logOutput = "\n" + dateOutput() + ": End of Simulation\n" + carPark.finalState();

		return logOutput;
	}
	
	/*
	 * method to get the initial state string of the car park
	 */
	private String initialEntry() {
		String logOutput = dateOutput() + ": Start of Simulation\n" +
								sim.toString() + "\n" +
								carPark.initialState() + "\n\n";
		return logOutput;
	}
	
	/*
	 * method to get the required time output for the simulation
	 */
	private String dateOutput() {
		String dateOutput = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		return dateOutput;
	}
	
	/*
	 * method to append any given string to the textarea 
	 * and position cursor at the bottom so it auto scrolls
	 */
	private void appendLogOutput(String output) {
		logOutputTextArea.append(output);
		logOutputTextArea.setCaretPosition(logOutputTextArea.getDocument().getLength());
	}
	
	/*
	 * method to determine if new vechiles are still allowed into the car park
	 * i.e. if car parks within 60 minutes of closing time
	 */
	private boolean newVehiclesAllowed(int time) {
		boolean allowed = (time >=1);
		return allowed && (time <= (Constants.CLOSING_TIME - 60));
	}
	
	/*
	 * method to produce the GUI display of the program
	 */
	private void createAndShowGUI() {
		//initialising the start button
		startButton = new JButton("Start Simulation");
		
		//adding an action listener to the start button
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					runSimulation();
				} catch (IOException | SimulationException | VehicleException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		});
        
		//creating the textfields and their labels for the car park class variables
        fieldMaxCarSpaces = new JTextField("", 5);
        fieldSmallCarSpaces = new JTextField("", 5);
        fieldMotoSpaces = new JTextField("", 5);
        fieldMaxQueueSize = new JTextField("", 5);
        JLabel maxCarSpacesLBL = new JLabel("Max Parking Spaces:");
        JLabel smallCarSpacesLBL = new JLabel("Small Car Spaces:");
        JLabel motoSpacesLBL = new JLabel("Motorcycle Spaces:");
        JLabel maxQueueSizeLBL = new JLabel("Max Queue Size:");
        
        //creating the textfields and their labels for the simulation class variables
        fieldSimSeed = new JTextField("", 5);
        fieldCarProb = new JTextField("", 5);
        fieldSmallCarProb = new JTextField("", 5);
        fieldMotoProb = new JTextField("", 5);
        fieldIntendedStayMean = new JTextField("", 5);
        fieldIntendedStaySD = new JTextField("", 5);        
        JLabel simSeedLBL = new JLabel("Simulation Seed:");
        JLabel carProbLBL = new JLabel("Car Prob:");
        JLabel smallCarProbLBL = new JLabel("Small Car Prob:");
        JLabel motoProbLBL = new JLabel("Motorcycle Prob:");
        JLabel intendedStayMeanLBL = new JLabel("Intended Stay Mean:");
        JLabel intendedStaySDLBL = new JLabel("Intended Stay SD:");
        
        //creating an outer panel for the options for aesthetics
        JPanel optionsOuterPanel = new JPanel();
        
        //creating the input options panel and setting layout + size
		JPanel optionsPanel = new JPanel();
		optionsPanel.setBorder(BorderFactory.createTitledBorder("User Inputs"));
		optionsPanel.setPreferredSize(new Dimension(390, 250));
		optionsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//creating the new tabbedpane for swapping between log and graphs
		tabbedData = new JTabbedPane();
		
		//first panel of tabbedpane for logOutput
        JPanel logOutput = new JPanel();
		logOutputTextArea = new JTextArea(20, 65);
		JScrollPane logOutputScroll = new JScrollPane(logOutputTextArea);
		logOutputScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		logOutput.add(logOutputScroll);
		
		//second panel
        JPanel plotSeries = new JPanel();
        
        //third panel
        JPanel summary = new JPanel();

        //adding each panel to the tabbedpane
        tabbedData.add("Log Output", logOutput);
        tabbedData.add("Plot Series", plotSeries);
        tabbedData.add("Summary", summary);
        
        //creating the gridbag constraints for each textfield
        //and label group to display them correctly and in line
        c.anchor = GridBagConstraints.WEST;
        //weight so objects don't overlap
        c.weightx = 1;
        c.weighty = 1;
        //position in the bag
        c.gridx = 0;
        c.gridy = 0;
		optionsPanel.add(maxCarSpacesLBL, c);
		c.gridy = 1;
		optionsPanel.add(fieldMaxCarSpaces, c);
		
		c.gridx = 1;
		c.gridy = 0;
		optionsPanel.add(simSeedLBL, c);
		c.gridy = 1;
		optionsPanel.add(fieldSimSeed, c);
		
		c.gridx = 2;
		c.gridy = 0;
		optionsPanel.add(carProbLBL, c);
		c.gridy = 1;
		optionsPanel.add(fieldCarProb, c);
		
		//padding between fields for display purposes
        c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 2;
		optionsPanel.add(smallCarSpacesLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 3;
		optionsPanel.add(fieldSmallCarSpaces, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 2;
		optionsPanel.add(smallCarProbLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 3;
		optionsPanel.add(fieldSmallCarProb, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 2;
		c.gridy = 2;
		optionsPanel.add(motoProbLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 3;
		optionsPanel.add(fieldMotoProb, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 4;
		optionsPanel.add(motoSpacesLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 5;
		optionsPanel.add(fieldMotoSpaces, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 4;
		optionsPanel.add(intendedStayMeanLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 5;
		optionsPanel.add(fieldIntendedStayMean, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 2;
		c.gridy = 4;
		optionsPanel.add(intendedStaySDLBL, c);
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 5;
		optionsPanel.add(fieldIntendedStaySD, c);
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 0;
		c.gridy = 6;
		optionsPanel.add(maxQueueSizeLBL, c);		
		c.insets = new Insets(0, 0, 0, 0);
		c.gridy = 7;
		optionsPanel.add(fieldMaxQueueSize, c);		
		
		c.insets = new Insets(5, 0, 0, 0);
		c.gridx = 1;
		c.gridy = 8;
		optionsPanel.add(startButton, c);
		
		//add the options panel to the outer panel
		optionsOuterPanel.add(optionsPanel);
		
		//create the content panel displaying items left to right
		Box contentPanel = new Box(BoxLayout.X_AXIS);
		
		//add the tabbedpane to the main content panel
		contentPanel.add(tabbedData);

		//add the two panels to the jframe in specific positions
		this.add(optionsOuterPanel, BorderLayout.NORTH);
		this.add(contentPanel, BorderLayout.CENTER);
        
		//adjusting final jframe options and setting visibility to true
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLocation(new Point(125, 20));
		pack();
		setVisible(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//check command line arguments
		checkCommandLineParams(args);
		JFrame.setDefaultLookAndFeelDecorated(true);
		//create the GUI and new simulation
		new GUISimulator(args);
	}

}
