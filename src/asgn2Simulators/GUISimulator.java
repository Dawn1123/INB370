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

	private final Integer WIDTH = 800,
						  HEIGHT = 700;
	
	private CarPark carPark;
	private Simulator sim;
	private Log log;
	
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
	
	private JButton startButton;
	
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
	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public GUISimulator(String[] args) throws HeadlessException {
		super("Car Park Simulator");		
		createAndShowGUI();
		
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
			
		} else {
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
	
	private void runSimulation() throws IOException, VehicleException, SimulationException {
		String logOutput = "";
		
		if (checkGUIInputParams()) {
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
			
			this.log.initialEntry(this.carPark,this.sim);
			appendLogOutput(initialEntry());					
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
				//Log progress
				logOutput = carPark.getStatus(time);
				appendLogOutput(logOutput);
				log.writer.write(logOutput);
			}
			this.log.finalise(this.carPark);
			appendLogOutput(finalEntry());
		}
	}
	
	private boolean checkGUIInputParams() {
		boolean parsed = true;
		String logOutput = "";
		
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
		} catch (NumberFormatException e) {
			parsed = false;
			logOutput = "Incorrect Inputs, values must be numbers" +
						"\nCar, small Car, MotorCycle spaces, Queue Size, and Simulation Seed must be Integers" +
						"\nCar, Small Car, MotorCycle probabilities, Intended Stay Mean, and Intended Stay SD must be a Double value";
			appendLogOutput(logOutput);
		}
		
		if (maxCarSpaces < 0 || smallCarSpaces < 0 || motorCycleSpaces < 0 || maxQueueSize < 0 || simSeed < 0 ||
				carProb < 0 || smallCarProb < 0 || motoProb < 0 || intendedStayMean < 0 || intendedStaySD < 0) {
			parsed = false;
			logOutput = "Incorrect Inputs, values cannot be negative. Please resolve and try again";
			appendLogOutput(logOutput);
		}
		
		return parsed;
	}
	
	private static void checkCommandLineParams(String[] args) {
		if ((args.length > 0 && args.length < 10) || args.length > 10) {
			System.err.println("Program terminated: if providing command line arguments there needs to be 10, you provided: " + args.length);
			System.exit(1);
			
		} else if (args.length == 10) {
			for (int i = 0; i < 5; i++) {
				try {
					Integer.parseInt(args[i]);
				} catch (NumberFormatException e) {
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
	
	/**
	 * Final state
	 * @return final entry of the simulation  
	 */
	private String finalEntry() {
		String logOutput = "\n" + dateOutput() + ": End of Simulation\n" + carPark.finalState();

		return logOutput;
	}
	
	/**
	 * Initial state of the simulation and carpark 
	 * @return string output of initial state
	 */
	private String initialEntry() {
		String logOutput = dateOutput() + ": Start of Simulation\n" +
								sim.toString() + "\n" +
								carPark.initialState() + "\n\n";
		return logOutput;
	}
	
	private String dateOutput() {
		String dateOutput = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		
		return dateOutput;
	}
	
	private void appendLogOutput(String output) {
		logOutputTextArea.append(output);
		logOutputTextArea.setCaretPosition(logOutputTextArea.getDocument().getLength());
	}
	
	/**
	 * Helper method to determine if new vehicles are permitted
	 * @param time int holding current simulation time
	 * @return true if new vehicles permitted, false if not allowed due to simulation constraints. 
	 */
	private boolean newVehiclesAllowed(int time) {
		boolean allowed = (time >=1);
		return allowed && (time <= (Constants.CLOSING_TIME - 60));
	}
	
	private void createAndShowGUI() {
		startButton = new JButton("Start Simulation");
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
        
        fieldMaxCarSpaces = new JTextField("", 5);
        fieldSmallCarSpaces = new JTextField("", 5);
        fieldMotoSpaces = new JTextField("", 5);
        fieldMaxQueueSize = new JTextField("", 5);
        JLabel maxCarSpacesLBL = new JLabel("Max Parking Spaces:");
        JLabel smallCarSpacesLBL = new JLabel("Small Car Spaces:");
        JLabel motoSpacesLBL = new JLabel("Motorcycle Spaces:");
        JLabel maxQueueSizeLBL = new JLabel("Max Queue Size:");
        
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
        
        JPanel optionsOuterPanel = new JPanel();

		JPanel optionsPanel = new JPanel();
		optionsPanel.setBorder(BorderFactory.createTitledBorder("User Inputs"));
		optionsPanel.setPreferredSize(new Dimension(390, 250));
		optionsPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		
		JTabbedPane tabbedData = new JTabbedPane();
		
        JPanel logOutput = new JPanel();
		logOutputTextArea = new JTextArea(20, 65);
		JScrollPane logOutputScroll = new JScrollPane(logOutputTextArea);
		logOutputScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
        logOutput.add(logOutputScroll);
        JPanel plotSeries = new JPanel();
        plotSeries.add(new JButton("Tab 2"));
        JPanel summary = new JPanel();
        summary.add(new JButton("Tab 2"));

        tabbedData.add("Log Output", logOutput);
        tabbedData.add("Plot Series", plotSeries);
        tabbedData.add("Summary", summary);
        
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.weighty = 1;
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
		
		optionsOuterPanel.add(optionsPanel);

		Box contentPanel = new Box(BoxLayout.X_AXIS);
		
		contentPanel.add(tabbedData);

		this.add(optionsOuterPanel, BorderLayout.NORTH);
		this.add(contentPanel, BorderLayout.CENTER);
        
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
		checkCommandLineParams(args);
		JFrame.setDefaultLookAndFeelDecorated(true);
		new GUISimulator(args);
	}

}
