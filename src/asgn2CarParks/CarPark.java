/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2CarParks 
 * 21/04/2014
 * 
 */
package asgn2CarParks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;
import asgn2Vehicles.Vehicle;

/**
 * The CarPark class provides a range of facilities for working with a car park in support 
 * of the simulator. In particular, it maintains a collection of currently parked vehicles, 
 * a queue of vehicles wishing to enter the car park, and an historical list of vehicles which 
 * have left or were never able to gain entry. 
 * 
 * The class maintains a wide variety of constraints on small cars, normal cars and motorcycles 
 * and their access to the car park. See the method javadoc for details. 
 * 
 * The class relies heavily on the asgn2.Vehicle hierarchy, and provides a series of reports 
 * used by the logger. 
 * 
 * @author hogan
 *
 */
public class CarPark {
	
	//static variables used to make program more readable
	private static String park = "P";
    private static String neww = "N";
    private static String archive = "A";
    private static String queue = "Q";
    private static String carLabel = "C";
    private static String smallCarLabel = "S";
    private static String motorCycleLabel = "MC";
	
    //variables used to set the number of available spaces for each vehicle
    private int carSpacesMax;
    private int smallCarSpacesMax; 
    private int motorCycleSpacesMax;
    private int regularCarSpacesEmpty;
    private int smallCarSpacesEmpty;
    private int motorCycleSpacesEmpty;
    private int totalSpaces;
    private int queueSpacesMax;
    
    //counts used to document the number of each category of vehicle
    private int vehicleCount = 0;
    private int numCars = 0;
    private int numSmallCars = 0;
    private int numMotorCycles = 0;
    private int numDissatisfied = 0;
    
    //variable used to store and print status of the carpark during simulation
    private String status;
    
    //collections used to store vehicles to document their status
    private ArrayList<Vehicle> spaces; 
    private ArrayList<Vehicle> vehicleArchive;
    private ArrayBlockingQueue<Vehicle> vehicleQueue;
    private ArrayList<Vehicle> vehiclesToModify;
    private ArrayList<Vehicle> vehiclesInSecondaryParks;
	
    /**
	 * CarPark constructor sets the basic size parameters. 
	 * Uses default parameters
	 */
	public CarPark() {
		this(Constants.DEFAULT_MAX_CAR_SPACES,Constants.DEFAULT_MAX_SMALL_CAR_SPACES,
				Constants.DEFAULT_MAX_MOTORCYCLE_SPACES,Constants.DEFAULT_MAX_QUEUE_SIZE);
	}
	
	/**
	 * @author Lucas
	 * CarPark constructor sets the basic size parameters. 
	 * @param maxCarSpaces maximum number of spaces allocated to cars in the car park 
	 * @param maxSmallCarSpaces maximum number of spaces (a component of maxCarSpaces) 
	 * 						 restricted to small cars
	 * @param maxMotorCycleSpaces maximum number of spaces allocated to MotorCycles
	 * @param maxQueueSize maximum number of vehicles allowed to queue
	 */
	public CarPark(int maxCarSpaces,int maxSmallCarSpaces, int maxMotorCycleSpaces, int maxQueueSize) {
		//store input values as private variables
		carSpacesMax = maxCarSpaces;
		smallCarSpacesMax = maxSmallCarSpaces;
		motorCycleSpacesMax = maxMotorCycleSpaces;
		queueSpacesMax = maxQueueSize;
		
		//create an array to keep track of vehicles in their secondary park type
		vehiclesInSecondaryParks = new ArrayList<Vehicle>();
		
		//initialise counts for number of spaces available
	    regularCarSpacesEmpty = maxCarSpaces - maxSmallCarSpaces;
	    smallCarSpacesEmpty = maxSmallCarSpaces;
	    motorCycleSpacesEmpty = maxMotorCycleSpaces;
		
		//calculate total number of spaces
		totalSpaces = carSpacesMax + motorCycleSpacesMax; 
		
		//initialise an array to store vehicles that are in the carpark
		spaces = new ArrayList<Vehicle>(totalSpaces);
		
		//initialise an array to store vehicles that have been archived
		vehicleArchive = new ArrayList<Vehicle>();
		
		//initialise an array to store vehicles that are in the queue
		vehicleQueue = new ArrayBlockingQueue<Vehicle>(queueSpacesMax);
	}

	/**
	 * @author Lucas
	 * Archives vehicles exiting the car park after a successful stay. Includes transition via 
	 * Vehicle.exitParkedState(). 
	 * @param time int holding time at which vehicle leaves
	 * @param force boolean forcing departure to clear car park 
	 * @throws VehicleException if vehicle to be archived is not in the correct state 
	 * @throws SimulationException if one or more departing vehicles are not in the car park when operation applied
	 */
	public void archiveDepartingVehicles(int time,boolean force) throws VehicleException, SimulationException {
		int departureTime;
		int currentTime = time;
		boolean carParkEmpty = spaces.isEmpty();
		//create a new array to store vehicles to be archived
		vehiclesToModify = new ArrayList<Vehicle>();
			
		//if carpark is empty throw simulationException, otherwise continue on
		if (carParkEmpty) { 
			throw new SimulationException("no vehicles in carpark"); 
		} else {
				
			//if a force has been set, add all vehicles in carpark to array to be archived
			if (force) {
				for(Vehicle currentVehicle : spaces){ 
					vehiclesToModify.add(currentVehicle);
				}
				
			//if a force has not been set, for every vehicle over time add to array to be archived	
			} else {
				for(Vehicle currentVehicle : spaces){ 
					departureTime = currentVehicle.getDepartureTime();
					if (currentTime >= departureTime) {
						vehiclesToModify.add(currentVehicle);
					}
				}
			}
				
			//remove all vehicles identified as being due to depart
			for(Vehicle overDueVehicle : vehiclesToModify){
				departureTime = overDueVehicle.getDepartureTime();
				unparkVehicle(overDueVehicle, departureTime);
				status += setVehicleMsg(overDueVehicle, park, archive);
			}
		}
	}
		
	/**
	 * @author Lucas
	 * Method to archive new vehicles that don't get parked or queued and are turned 
	 * away
	 * @param v Vehicle to be archived
	 * @throws SimulationException if vehicle is currently queued or parked
	 */
	public void archiveNewVehicle(Vehicle v) throws SimulationException {
		vehicleArchive.add(v);//add vehicle to archive
	}
	
	/**
	 * @author Lucas
	 * Archive vehicles which have stayed in the queue too long 
	 * @param time int holding current simulation time 
	 * @throws VehicleException if one or more vehicles not in the correct state or if timing constraints are violated
	 * @throws SimulationException 
	 */
	public void archiveQueueFailures(int time) throws VehicleException, SimulationException {
		int currentTime = time;
		int maxWaitTime;
		boolean queueNotEmpty = !queueEmpty();
		//create a new array to store vehicles to be archived
		vehiclesToModify = new ArrayList<Vehicle>();
		
		if (queueNotEmpty) {
			//for every vehicle that has stayed too long add to list to be archived
			for(Vehicle queuedVehicle : vehicleQueue){ 
				maxWaitTime = queuedVehicle.getArrivalTime() + Constants.MAXIMUM_QUEUE_TIME;
				if (currentTime > maxWaitTime) {
					vehiclesToModify.add(queuedVehicle); 
				}
			}
			
			//for every unsatisfied vehicle, remove from queue and add to archive
			for(Vehicle unsatisfiedVehicle : vehiclesToModify){ 
				exitQueue(unsatisfiedVehicle, currentTime);
				vehicleArchive.add(unsatisfiedVehicle);
				status += setVehicleMsg(unsatisfiedVehicle, queue, archive);
			}
		}
	} 
	
	/**
	 * @author Lucas
	 * Simple status showing whether carPark is empty
	 * @return true if car park empty, false otherwise
	 */
	public boolean carParkEmpty() {
		//check if carpark is empty
		boolean carparkEmpty = spaces.isEmpty();
		return carparkEmpty;
	}
	
	/**
	 * @author Lucas
	 * Simple status showing whether carPark is full
	 * @return true if car park full, false otherwise
	 */
	public boolean carParkFull() {
		boolean motorCycleSpacesFull = false;
		boolean regularCarSpacesFull = false;
		boolean smallCarSpacesFull = false;
		boolean carParkFull = false;
		
		//check if each category of carpark is full
		if (regularCarSpacesEmpty == 0) {
			regularCarSpacesFull = true;
		}
		if (smallCarSpacesEmpty == 0) {
			smallCarSpacesFull = true;
		}
		if (motorCycleSpacesEmpty == 0) {
			motorCycleSpacesFull = true;
		}
		if (regularCarSpacesFull && smallCarSpacesFull && motorCycleSpacesFull) {
			carParkFull = true;
		}
		
		return carParkFull;
	}
	
	/**
	 * @author Lucas
	 * Method to add vehicle successfully to the queue
	 * Precondition is a test that spaces are available
	 * Includes transition through Vehicle.enterQueuedState 
	 * @param v Vehicle to be added 
	 * @throws SimulationException if queue is full  
	 * @throws VehicleException if vehicle not in the correct state 
	 */
	public void enterQueue(Vehicle v) throws SimulationException, VehicleException {
		Vehicle currentVehicle = v;
		boolean vehicleIsArchived = vehicleArchive.contains(currentVehicle);
		boolean queueFull = queueFull();
		
		//Add the given vehicle to the queue, provided it's not full
		if (queueFull) { 
			throw new SimulationException("queue was full when enterQueue was called"); 
		} else if (vehicleIsArchived) { 
			throw new VehicleException("Vehicle cannot enter a queued state when already archived"); 
		} else {
			vehicleQueue.add(currentVehicle); //add vehicle to queue
			currentVehicle.enterQueuedState();//change status to queued
		}
	}
	
	
	/**
	 * @author Lucas
	 * Method to remove vehicle from the queue after which it will be parked or 
	 * removed altogether. Includes transition through Vehicle.exitQueuedState.  
	 * @param v Vehicle to be removed from the queue 
	 * @param exitTime int time at which vehicle exits queue
	 * @throws SimulationException if vehicle is not in queue 
	 * @throws VehicleException if the vehicle is in an incorrect state or timing 
	 * constraints are violated
	 */
	public void exitQueue(Vehicle v,int exitTime) throws SimulationException, VehicleException {
		Vehicle currentVehicle = v;
		boolean vehicleNotInQueue = !vehicleQueue.contains(currentVehicle);
		
		//if queue does not contain vehicle throw exception
		if (vehicleNotInQueue) { 
			throw new SimulationException("queue was empty when exitQueue was called"); 
		} else {
			vehicleQueue.remove(currentVehicle); //remove vehicle from queue
			currentVehicle.exitQueuedState(exitTime);//change status to not queued
		}
	}
	
	/**
	 * State dump intended for use in logging the final state of the carpark
	 * All spaces and queue positions should be empty and so we dump the archive
	 * @return String containing dump of final carpark state 
	 */
	public String finalState() {
		String str = "Vehicles Processed: count: " + 
				this.vehicleCount + ", logged: " + this.vehicleArchive.size() 
				+ "\nVehicle Record: \n";
		for (Vehicle v : this.vehicleArchive) {
			str += v.toString() + "\n\n";
		}
		return str + "\n";
	}
	
	/**
	 * @author Lucas
	 * Simple getter for number of cars in the car park 
	 * @return number of cars in car park, including small cars
	 */
	public int getNumCars() {
		int carCount = 0; 
		//for every car in the carpark, increase the count of cars by one
		for(Vehicle currentVehicle : spaces){
		    if (currentVehicle instanceof Car) {
		    	carCount += 1;
		    } 		
		}
		return carCount;
	}
	
	/**
	 * @author Lucas
	 * Simple getter for number of motorcycles in the car park 
	 * @return number of MotorCycles in car park, including those occupying 
	 * 			a small car space
	 */
	public int getNumMotorCycles() {
		int MotorCycleCount = 0; 
		//for every motorcycle in the carpark, increase the count of motorcycles by one
		for(Vehicle currentVehicle : spaces){
			if (currentVehicle instanceof MotorCycle) {
				MotorCycleCount += 1;
			} 		
		}
		return MotorCycleCount;
	}
	
	
	/**
	 * @author Lucas
	 * Simple getter for number of small cars in the car park 
	 * @return number of small cars in car park, including those 
	 * 		   not occupying a small car space. 
	 */
	public int getNumSmallCars() {
		int SmallCarCount = 0; 
		//for every small car in the carpark, increase the small car count by one
		for(Vehicle currentVehicle : spaces){
			if (currentVehicle instanceof Car) {
				if(((Car) currentVehicle).isSmall()){
					SmallCarCount += 1;
				}
			} 		
		}
		return SmallCarCount;
	}
	
	/**
	 * @author Lucas
	 * Method used to provide the current status of the car park. 
	 * Uses private status String set whenever a transition occurs. 
	 * Example follows (using high probability for car creation). At time 262, 
	 * we have 276 vehicles existing, 91 in car park (P), 84 cars in car park (C), 
	 * of which 14 are small (S), 7 MotorCycles in car park (M), 48 dissatisfied (D),
	 * 176 archived (A), queue of size 9 (CCCCCCCCC), and on this iteration we have 
	 * seen: car C go from Parked (P) to Archived (A), C go from queued (Q) to Parked (P),
	 * and small car S arrive (new N) and go straight into the car park<br>
	 * 262::276::P:91::C:84::S:14::M:7::D:48::A:176::Q:9CCCCCCCCC|C:P>A||C:Q>P||S:N>P|
	 * @return String containing current state 
	 */
	public String getStatus(int time) {
		boolean vehicleIsDissatisfied;
		
		//update vehicle counts
		numCars = getNumCars();
		numSmallCars = getNumSmallCars();
		numMotorCycles = getNumMotorCycles();
		
		//reset and update number of dissatisfied count
		numDissatisfied = 0;
		for (Vehicle archivedVehicle : vehicleArchive) {
			vehicleIsDissatisfied = !archivedVehicle.isSatisfied();
			if (vehicleIsDissatisfied){
				numDissatisfied++;
			}
		}

		String str = time +"::"
		+ this.vehicleCount + "::" 
		+ "P:" + this.spaces.size() + "::"
		+ "C:" + this.numCars + "::S:" + this.numSmallCars 
		+ "::M:" + this.numMotorCycles 
		+ "::D:" + this.numDissatisfied 
		+ "::A:" + this.vehicleArchive.size()  
		+ "::Q:" + this.vehicleQueue.size(); 
		for (Vehicle v : this.vehicleQueue) {
			if (v instanceof Car) {
				if (((Car)v).isSmall()) {
					str += "S";
				} else {
					str += "C";
				}
			} else {
				str += "M";
			}
		}
		if (this.status != null){
		str += this.status;
		}
		this.status="";
		return str+"\n";
	}
	

	/**
	 * State dump intended for use in logging the initial state of the carpark.
	 * Mainly concerned with parameters. 
	 * @return String containing dump of initial carpark state 
	 */
	public String initialState() {
		return "CarPark [maxCarSpaces: " + this.carSpacesMax
				+ " maxSmallCarSpaces: " + this.smallCarSpacesMax 
				+ " maxMotorCycleSpaces: " + this.motorCycleSpacesMax 
				+ " maxQueueSize: " + this.queueSpacesMax + "]";
	}

	/**
	 * @author Lucas
	 * Simple status showing number of vehicles in the queue 
	 * @return number of vehicles in the queue
	 */
	public int numVehiclesInQueue() {
		//determine the number of vehicles in the queue
		int queueCount = vehicleQueue.size();
		return queueCount;
	}
	
	/**
	 * @author Lucas
	 * Method to add vehicle successfully to the car park store. 
	 * Precondition is a test that spaces are available. 
	 * Includes transition via Vehicle.enterParkedState.
	 * @param v Vehicle to be added 
	 * @param time int holding current simulation time
	 * @param intendedDuration int holding intended duration of stay 
	 * @throws SimulationException if no suitable spaces are available for parking 
	 * @throws VehicleException if vehicle not in the correct state or timing constraints are violated
	 */
	public void parkVehicle(Vehicle v, int time, int intendedDuration) throws SimulationException, VehicleException {
		Vehicle vehicleToPark = v;
		int currentTime = time;
		boolean noSuitableSpaces = !spacesAvailable(vehicleToPark);
		
		//park the given vehicle if there are relevant spaces available
		if (noSuitableSpaces) { 
			throw new SimulationException("parkvehicle called when carpark full"); 
		} else { 	
			spaces.add(vehicleToPark); //add to spaces
			vehicleToPark.enterParkedState(currentTime, intendedDuration); //update status to is parked
			parkedVehicleUpdateCount(vehicleToPark); //update parked vehicle count
		}
	}  

	/**
	 * @author Lucas
	 * Silently process elements in the queue, whether empty or not. If possible, add them to the car park. 
	 * Includes transition via exitQueuedState where appropriate
	 * Block when we reach the first element that can't be parked. 
	 * @param time int holding current simulation time 
	 * @throws SimulationException if no suitable spaces available when parking attempted
	 * @throws VehicleException if state is incorrect, or timing constraints are violated
	 */
	public void processQueue(int time, Simulator sim) throws VehicleException, SimulationException {
		int parkingDuration;
		int currentTime = time;
		Vehicle firstVehicleInQueue;
		boolean firstVehicleCanPark = spacesAvailable(vehicleQueue.peek());

		//If there is a space available for the first vehicle in the queue, park in carpark
		//Continue moving through the queue until a vehicle cannot park
		while (firstVehicleCanPark) {
			firstVehicleInQueue = vehicleQueue.element();						//get a copy of vehicle
			exitQueue(firstVehicleInQueue, currentTime); 						//exit carpark
			parkingDuration = sim.setDuration();								//check duration of park
			parkVehicle(firstVehicleInQueue, currentTime, parkingDuration);		//park vehicle
			status += setVehicleMsg(firstVehicleInQueue, queue, park);
			firstVehicleCanPark = spacesAvailable(vehicleQueue.peek());			//check if the next vehicle can park
		}
		
	}

	/**
	 * @author Lucas
	 * Simple status showing whether queue is empty
	 * @return true if queue empty, false otherwise
	 */
	public boolean queueEmpty() {
		//check if the queue is empty
		boolean queueIsEmpty = vehicleQueue.isEmpty();
		return queueIsEmpty;
	}

	/**
	 * @author Lucas
	 * Simple status showing whether queue is full
	 * @return true if queue full, false otherwise
	 */
	public boolean queueFull() {
		//check if the queue is full
		boolean queueIsFull = (queueSpacesMax == numVehiclesInQueue());
		return queueIsFull;
	}
	
	/**
	 * @author Lucas
	 * Method determines, given a vehicle of a particular type, whether there are spaces available for that 
	 * type in the car park under the parking policy in the class header.  
	 * @param v Vehicle to be stored. 
	 * @return true if space available for v, false otherwise 
	 */
	public boolean spacesAvailable(Vehicle v) {
		int spacesCount = 0;
		Vehicle currentVehicle = v;
		boolean spacesRemaining = false;
		boolean vehicleIsCar = (currentVehicle instanceof Car);
		boolean vehicleIsSmallCar; 
		boolean vehicleIsMotorCycle = currentVehicle instanceof MotorCycle;
		
		//determine type of vehicle and get number of relevant spaces available 
		if (vehicleIsCar) {
			vehicleIsSmallCar = ((Car) currentVehicle).isSmall();
			if(vehicleIsSmallCar){
				spacesCount = regularCarSpacesEmpty + smallCarSpacesEmpty;
			} else {
				spacesCount = regularCarSpacesEmpty;	
			}
		} else if (vehicleIsMotorCycle) {
				spacesCount = motorCycleSpacesEmpty + smallCarSpacesEmpty;
		}
		
		//check if the number of spaces available is 1 or more
		spacesRemaining = spacesCount > 0;
		return spacesRemaining;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CarPark [count: " + vehicleCount + " numCars: " + numCars + " numSmallCars: " + numSmallCars + " numMotorCycles: " +
				numMotorCycles + " queue: " + (vehicleQueue.size()) + " numDissatisfied: " + numDissatisfied + " past: " + 
				vehicleArchive.size() + "]";
	}

	/**
	 * @author Lucas
	 * Method to try to create new vehicles (one trial per vehicle type per time point) 
	 * and to then try to park or queue (or archive) any vehicles that are created 
	 * @param sim Simulation object controlling vehicle creation 
	 * @throws SimulationException if no suitable spaces available when operation attempted 
	 * @throws VehicleException if vehicle creation violates constraints 
	 */
	public void tryProcessNewVehicles(int time,Simulator sim) throws VehicleException, SimulationException {
		boolean createCar = sim.newCarTrial();
		boolean createSmallCar;
		boolean createMotorCycle;
		int currentTime = time;
		Vehicle currentVehicle;
		String vehicleID;

		//check if a car is to be created
		if (createCar) {
			createSmallCar = sim.smallCarTrial();
			if (createSmallCar) {
				//create a small car
				vehicleCount++;
				vehicleID = smallCarLabel + Integer.toString(vehicleCount);
				currentVehicle = new Car(vehicleID, currentTime, true); 
				//process the new vehicle
				processVehicle(time, sim, currentVehicle);
			
			} else {
				//create a normal car
				vehicleCount++;
				vehicleID = carLabel + Integer.toString(vehicleCount);
				currentVehicle = new Car(vehicleID, time, false);
				//process the new vehicle
				processVehicle(time, sim, currentVehicle);
			}
		}
		
		//check if a motorcycle is to be created
		createMotorCycle = sim.motorCycleTrial();
		if (createMotorCycle) {
			//create a motorcycle
			vehicleCount++;
			vehicleID = motorCycleLabel + Integer.toString(vehicleCount);
			currentVehicle = new MotorCycle(vehicleID, time); 
			//process the new vehicle
			processVehicle(time, sim, currentVehicle);
		}
	}

	/**
	 * @author Lucas
	 * Method to remove vehicle from the carpark. 
	 * For symmetry with parkVehicle, include transition via Vehicle.exitParkedState.  
	 * So vehicle should be in parked state prior to entry to this method. 
	 * @param v Vehicle to be removed from the car park 
	 * @throws VehicleException if Vehicle is not parked, is in a queue, or violates timing constraints 
	 * @throws SimulationException if vehicle is not in car park
	 */
	public void unparkVehicle(Vehicle v,int departureTime) throws VehicleException, SimulationException {
		boolean vehicleNotInCarpark = !spaces.contains(v);
		Vehicle vehicleToUnpark = v;
		
		//unpark the given vehicle if it is present in carpark
		if (vehicleNotInCarpark) { 
			throw new SimulationException("unparkVehicle called when vehicle is not present in carpark"); 
		} else { 
			spaces.remove(vehicleToUnpark); 					//remove from spaces
			vehicleArchive.add(vehicleToUnpark); 				//add to vehicleArchive
			vehicleToUnpark.exitParkedState(departureTime); 	//update status to unparked
			UnparkedVehicleUpdateCount(vehicleToUnpark); 		//update parked vehicle count
		}
	}
	
	/**
	 * Helper to set vehicle message for transitions 
	 * @param v Vehicle making a transition (uses S,C,M)
	 * @param source String holding starting state of vehicle (N,Q,P,A) 
	 * @param target String holding finishing state of vehicle (Q,P,A) 
	 * @return String containing transition in the form: |(S|C|M):(N|Q|P|A)>(Q|P|A)| 
	 */
	private String setVehicleMsg(Vehicle v,String source, String target) {
		String str="";
		if (v instanceof Car) {
			if (((Car)v).isSmall()) {
				str+="S";
			} else {
				str+="C";
			}
		} else {
			str += "M";
		}
		return "|"+str+":"+source+">"+target+"|";
	}
	
	/**
	 * @author Lucas
	 * Method used to place new vehicle in either the carpark, queue or archive
	 * @param currentTime int holding the current simulation time
	 * @param sim Simulation object controlling duration of stay
	 * @param vehicleToProcess holding the vehicle to place 
	 */
	private void processVehicle(int currentTime, Simulator sim, Vehicle vehicleToProcess) throws VehicleException, SimulationException {
		int durationOfStay;
		boolean spaceInQueue = !queueFull();
		
		//if spaces are available, park vehicle in carpark
		if (spacesAvailable(vehicleToProcess)) {
			durationOfStay = sim.setDuration();
			parkVehicle(vehicleToProcess, currentTime, durationOfStay);
			status += setVehicleMsg(vehicleToProcess, neww, park);
		//otherwise if there are spaces in the queue, park vehicle in queue
		} else if (spaceInQueue) {
			enterQueue(vehicleToProcess);
			status += setVehicleMsg(vehicleToProcess, neww, queue);
		//otherwise, vehicle cannot be parked and is placed in the archive
		} else {
			archiveNewVehicle(vehicleToProcess);
			status += setVehicleMsg(vehicleToProcess, neww, archive);
		}
	}
	
	/**
	 * @author Lucas
	 * Method used to update number of parks available when a vehicle is parked
	 */
	private void parkedVehicleUpdateCount(Vehicle v) {
		Vehicle parkedVehicle = v;
		boolean vehicleIsCar = (parkedVehicle instanceof Car);
		boolean vehicleIsSmallCar; 
		boolean vehicleIsMotorCycle = parkedVehicle instanceof MotorCycle;
		boolean smallCarSpacesFull = (smallCarSpacesEmpty == 0);
		boolean motorCycleSpacesFull = (motorCycleSpacesEmpty == 0);
		
		if (vehicleIsCar) {
			vehicleIsSmallCar = ((Car) parkedVehicle).isSmall();
			//if vehicle is small car and all small car spaces are full
			//add to list of vehicles in secondary parks
			//subtract from regular car spaces available
			if (vehicleIsSmallCar) {
				if (smallCarSpacesFull) {
					vehiclesInSecondaryParks.add(parkedVehicle);
					regularCarSpacesEmpty--;
				} else {
				smallCarSpacesEmpty--;
				}
			//if vehicle is a regular car, subtract from list of regular car spaces available
			} else {
				regularCarSpacesEmpty--;
			}
		} else if (vehicleIsMotorCycle) {
			//if vehicle is a motorcycle and all motorcycle spaces are full
			//add to list of vehicles in secondary parks
			//subtract from small car spaces available
			if (motorCycleSpacesFull) {
				vehiclesInSecondaryParks.add(parkedVehicle);
				smallCarSpacesEmpty--;
			} else {
			motorCycleSpacesEmpty--;
			}
		}
	}
	
	/**
	 * @author Lucas
	 * Method used to update number of parks available when a vehicle is unparked
	 */
	private void UnparkedVehicleUpdateCount(Vehicle v) {
		Vehicle currentVehicle = v;
		boolean vehicleIsCar = (currentVehicle instanceof Car);
		boolean vehicleIsSmallCar; 
		boolean vehicleIsMotorCycle = currentVehicle instanceof MotorCycle;
		
		//find where the vehicle is parked, and update the count of empty spaces to indicate the vehicle has left
		if (vehicleIsCar) {
			
			//if vehicle is small car and was parked in a secondary park, update count for regular spaces empty
			//otherwise update count for small car spaces empty
			vehicleIsSmallCar = ((Car) currentVehicle).isSmall();
			if (vehicleIsSmallCar) {
				if (vehiclesInSecondaryParks.contains(currentVehicle)) {
					regularCarSpacesEmpty++;
					vehiclesInSecondaryParks.remove(currentVehicle);
				} else {
				smallCarSpacesEmpty++;
				}
			//if vehicle is a regular car update count for regular spaces empty
			} else {
				regularCarSpacesEmpty++;
			}
		//if vehicle is a motorcycle and was parked in a secondary park, update count for small car spaces empty
		//otherwise update count for motorcycle spaces empty
		} else if (vehicleIsMotorCycle) {
			if (vehiclesInSecondaryParks.contains(currentVehicle)) {
				smallCarSpacesEmpty++;
				vehiclesInSecondaryParks.remove(currentVehicle);
			} else {
			motorCycleSpacesEmpty++;
			}
		}
	}
}

