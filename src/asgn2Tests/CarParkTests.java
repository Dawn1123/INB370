/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 29/04/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2CarParks.CarPark;
import asgn2Exceptions.SimulationException;
import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Simulators.Simulator;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;
import asgn2Vehicles.Vehicle;

/**
 * @author hogan
 *
 */
public class CarParkTests {

	private static boolean smallCarCondition = true;
	private static boolean normalCarCondition = false;
	private static String testVehicleID = "1";
	private static int overTimeValue = 300;

	
	/* Declare an object for use in each test */
	private CarPark testCarPark;
	private Car testCar;
	private MotorCycle testMotorCycle;
	private Simulator testSimulator;
	int currentTime = 100;
	boolean currentTestCondition;
	private int testValue;
	private int time = 1;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testCarPark = new CarPark();
		testSimulator = new Simulator();
		currentTestCondition = false;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
	////////////////////////////////////////////////////
	//			archiveDepartingVehicles
	////////////////////////////////////////////////////
	
	/**
	 * @author Lucas
	 * Check that SimulationeException is thrown when no vehicles in CarPark
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test  (expected = SimulationException.class)
	public void testArchiveDepartingEmpty() throws VehicleException, SimulationException {
		testCarPark.archiveDepartingVehicles(0, false);	
		assertTrue("SimulationException not thrown when ArchiveDepartingVehicles"
				+ "is called and CarPark is empty",(true)); 
	}
	
	/**
	 * @author Lucas
	 * Check that vehicle is removed from carpark when greater than time, and force is false
	 */
	@Test 
	public void testArchiveVehicleOverTime() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.parkVehicle(testCar, currentTime, 100); 
		//call archiveDepartingVehicles when single vehicle is over time
		testCarPark.archiveDepartingVehicles(201, false); 
		currentTestCondition = (testCarPark.carParkEmpty()); 
		assertTrue("Vehicle is not removed from carpark when over time,",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that vehicle changes state when greater than time, and force is false
	 */
	@Test 
	public void testArchiveVehicleOverTimeState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.parkVehicle(testCar, currentTime, 100); 
		//call archiveDepartingVehicles when single vehicle is over time
		testCarPark.archiveDepartingVehicles(201, false);	
		currentTestCondition = !testCar.isParked(); 
		assertTrue("archiveDepartingVehicle does not correctly change state"
				+ " of vehicle when over time",(currentTestCondition));
	}

	
	/**
	 * @author Lucas
	 * Check that vehicle doesn't change state when less than time, and force is false
	 */
	@Test 
	public void testArchiveVehicleUnderTime() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.parkVehicle(testCar, currentTime, 100); 
		//call archiveDepartingVehicles when single vehicle is under time
		testCarPark.archiveDepartingVehicles(199, false);	
		currentTestCondition = testCar.isParked(); 
		assertTrue("archiveDepartingVehicle incorrectly changes state"
				+ " of vehicle when under time",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that vehicle isn't removed from carpark when less than time, and force is false
	 */
	@Test 
	public void testArchiveVehicleUnderTimeState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.parkVehicle(testCar, currentTime, 100); 
		//call archiveDepartingVehicles when single vehicle is under time
		testCarPark.archiveDepartingVehicles(199, false);	
		currentTestCondition = (testCarPark.getNumCars() == 1);
		assertTrue("archiveDepartingVehicle incorrectly removes vehicle from carpark"
				+ " when under time",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that vehicle is removed from carpark when equal to time, and force is false
	 */
	@Test 
	public void testArchiveVehicleEqualTime() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.parkVehicle(testCar, currentTime, 100); 
		//call archiveDepartingVehicles when single vehicle is equal to time
		testCarPark.archiveDepartingVehicles(200, false);	
		currentTestCondition = testCarPark.carParkEmpty();
		assertTrue("archiveDepartingVehicle fails to remove vehicle from carpark"
				+ " when equal to time",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that vehicle changes state when equal to time, and force is false
	 */
	@Test 
	public void testArchiveVehicleEqualTimeState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.parkVehicle(testCar, currentTime, 100); 
		//call archiveDepartingVehicles when single vehicle is equal to time
		testCarPark.archiveDepartingVehicles(200, false);	
		currentTestCondition = !testCar.isParked(); 
		assertTrue("archiveDepartingVehicle does not correctly change state"
				+ " of vehicle when equal to time",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that vehicle changes state when less than time, but force is true
	 */
	@Test 
	public void testArchiveVehicleForceOnState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.parkVehicle(testCar, currentTime, 100); 
		//call archiveDepartingVehicles when single vehicle is less than time, and force true
		testCarPark.archiveDepartingVehicles(199, true);	
		currentTestCondition = !testCar.isParked(); 
		assertTrue("archiveDepartingVehicle does not correctly change state"
				+ " of vehicle when force is true",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that vehicle is removed from carpark when less than time, but force is true
	 */
	@Test 
	public void testArchiveVehicleForceOn() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.parkVehicle(testCar, currentTime, 100); 
		//call archiveDepartingVehicles when single vehicle is less than time, and force true
		testCarPark.archiveDepartingVehicles(199, true);	
		currentTestCondition = testCarPark.carParkEmpty();
		assertTrue("archiveDepartingVehicle fails to remove vehicle from carpark "
				+ "when less than time but force is true",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that no vehicles are removed from carpark when multiple less than time
	 */
	@Test 
	public void testArchiveVehicleMultipleUnderTime() throws VehicleException, SimulationException {
		//park 30 cars in the carpark
		for (int i = 0; i < 30; i++) {
			testCar = new Car(testVehicleID, currentTime, false); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//call archiveDepartingVehicles when multiple vehicles are less than time
		testCarPark.archiveDepartingVehicles(199, false);	
		currentTestCondition = (testCarPark.getNumCars() == 30);
		assertTrue("archiveDepartingVehicle incorrectly removes vehicles from carpark when"
				+ " multiple under time",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that multiple vehicles removed from carpark when multiple greater than time
	 */
	@Test 
	public void testArchiveVehicleMultipleOverTime() throws VehicleException, SimulationException {
		//park 30 cars in the carpark
		for (int i = 0; i < 30; i++) {
			testCar = new Car(testVehicleID, currentTime, true); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//call archiveDepartingVehicles when multiple vehicles are over time
		testCarPark.archiveDepartingVehicles(201, false);	
		currentTestCondition = testCarPark.carParkEmpty();
		assertTrue("archiveDepartingVehicle fails to remove vehicles from carpark when"
				+ " multiple over time",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that correct number of vehicles removed from carpark when multiple over and under time
	 */
	@Test 
	public void testArchiveVehicleMultipleConditions() throws VehicleException, SimulationException {
		//park 30 cars in the carpark
		for (int i = 0; i < 30; i++) {
			testCar = new Car(testVehicleID, currentTime, true); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//park 20 cars in the carpark
				for (int i = 0; i < 20; i++) {
					testCar = new Car(testVehicleID, currentTime, true); 
					testCarPark.parkVehicle(testCar, currentTime, 150); 
				}
		//call archiveDepartingVehicles when 30 vehicles are over time
		testCarPark.archiveDepartingVehicles(201, false);	
		currentTestCondition = (testCarPark.getNumCars() == 20);
		assertTrue("archiveDepartingVehicle fails to remove vehicles from carpark when"
				+ " multiple over time",(currentTestCondition));
	}

	
	////////////////////////////////////////////////////
	//			archiveNewVehicle
	////////////////////////////////////////////////////
	
	/**
	 * @author Lucas
	 * Check that vehicle was never parked
	 * @throws VehicleException 
	 */
	@Test 
	public void testArchiveNewVehicleParked() throws SimulationException, VehicleException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.archiveNewVehicle(testCar); 
		currentTestCondition = (!testCar.wasParked()); 
		assertTrue("archiveNewVehicle fails to set wasParked to false",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that vehicle was never queued
	 */
	@Test 
	public void testArchiveNewVehicleQueued() throws SimulationException, VehicleException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.archiveNewVehicle(testCar); 
		currentTestCondition = (!testCar.wasQueued()); 
		assertTrue("archiveNewVehicle fails to set wasQueued to false",(currentTestCondition));
	}
	
	/**
	 * Check that vehicle is unsatisfied
	 */
	@Test 
	public void testArchiveNewVehicleSatisfied() throws SimulationException, VehicleException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.archiveNewVehicle(testCar); 
		currentTestCondition = ((!testCar.wasQueued()) && (!testCar.wasParked())); 
		assertTrue("archiveNewVehicle fails to set vehicle to unsatisfied",(currentTestCondition));
	}
	
	
	////////////////////////////////////////////////////
	//			archiveQueueFailures
	////////////////////////////////////////////////////
	
	/**
	 * Check that vehicle is removed from queue
	 */
	@Test 
	public void testArchiveQueueFailuresOverTime() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.enterQueue(testCar);
		//call archiveDepartingVehicles when single vehicle is over time
		testCarPark.archiveQueueFailures(overTimeValue); 
		currentTestCondition = (testCarPark.queueEmpty()); 
		assertTrue("Vehicle is not removed from queue when over time",(currentTestCondition));
	}
	
	/**
	 * Check that vehicle changes state when greater than time
	 */
	@Test 
	public void testArchiveQueueFailuresOverTimeState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.enterQueue(testCar);
		//call archiveDepartingVehicles when single vehicle is over time
		testCarPark.archiveQueueFailures(overTimeValue); 
		currentTestCondition = (!testCar.isQueued()); 
		assertTrue("Vehicle does not change state when over time and removed from queue",(currentTestCondition));
	}
	
	/**
	 * Check that vehicle doesn't change state when less than time
	 */
	@Test 
	public void testArchiveQueueFailuresUnderTimeState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.enterQueue(testCar);
		//call archiveDepartingVehicles when single vehicle is under time
		testCarPark.archiveQueueFailures(currentTime+1); 
		currentTestCondition = (testCar.isQueued()); 
		assertTrue("Vehicle incorrectly changes state when under time",(currentTestCondition));
	}
	
	/**
	 * Check that vehicle isn't removed from queue when less than time
	 */
	@Test 
	public void testArchiveQueueFailuresUnderTime() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.enterQueue(testCar);
		//call archiveDepartingVehicles when single vehicle is under time
		testCarPark.archiveQueueFailures(currentTime+1); 
		currentTestCondition = (!testCarPark.queueEmpty()); 
		assertTrue("Vehicle is incorrectly removed from queue when under time",(currentTestCondition));
	}
	
	/**
	 * Check that vehicle doesn't change state when equal to time
	 */
	@Test 
	public void testArchiveQueueFailuresEqualTimeState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.enterQueue(testCar);
		//call archiveDepartingVehicles when single vehicle is equal to time
		testCarPark.archiveQueueFailures(currentTime+Constants.MAXIMUM_QUEUE_TIME); 
		currentTestCondition = (testCar.isQueued()); 
		assertTrue("Vehicle incorrectly changes state when equal to time",(currentTestCondition));
	}
	
	/**
	 * Check that vehicle isn't removed from queue when equal to time
	 */
	@Test 
	public void testArchiveQueueFailuresEqualTime() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.enterQueue(testCar);
		//call archiveDepartingVehicles when single vehicle is under time
		testCarPark.archiveQueueFailures(currentTime+Constants.MAXIMUM_QUEUE_TIME); 
		currentTestCondition = (!testCarPark.queueEmpty()); 
		assertTrue("Vehicle is incorrectly removed from queue when equal to time",(currentTestCondition));
	}
	
	/**
	 * Check that no vehicles removed from queue when multiple less than time
	 */
	@Test 
	public void testArchiveQueueFailuresMultipleUnderTime() throws VehicleException, SimulationException {
		for (int i = 0; i < 5; i++) {
			testCar = new Car(testVehicleID, currentTime, false); 
			testCarPark.enterQueue(testCar);
		}
		//call archiveDepartingVehicles when multiple vehicles are under time
		testCarPark.archiveQueueFailures(currentTime+1); 
		currentTestCondition = (!testCarPark.queueEmpty()); 
		assertTrue("Vehicles are incorrectly removed from queue when multiple under time",(currentTestCondition));
	}
	
	/**
	 * Check that multiple vehicles removed from queue when multiple greater than time
	 */
	@Test 
	public void testArchiveQueueFailuresMultipleOverTime() throws VehicleException, SimulationException {
		for (int i = 0; i < 5; i++) {
			testCar = new Car(testVehicleID, currentTime, false); 
			testCarPark.enterQueue(testCar);
		}
		//call archiveDepartingVehicles when multiple vehicles are over time
		testCarPark.archiveQueueFailures(overTimeValue); 
		currentTestCondition = (testCarPark.queueEmpty()); 
		assertTrue("Vehicles aren't removed from queue when multiple over time",(currentTestCondition));
	}

	
	////////////////////////////////////////////////////
	//			carParkEmpty
	////////////////////////////////////////////////////
	
	/**
	 * @author Lucas
	 * Check that carParkEmpty returns true when no vehicles in carpark
	 */
	@Test 
	public void testCarParkEmptyNoVehicles() throws VehicleException, SimulationException {
		currentTestCondition = testCarPark.carParkEmpty(); //check if carparkEmpty returns true when no vehicles present
		assertTrue("carParkEmpty fails to return true when no vehicles present",(currentTestCondition));
	}
	
	
	/**
	 * @author Lucas
	 * Check that carParkEmpty returns false when one car in carpark
	 */
	@Test 
	public void testCarParkEmptyOneVehicle() throws VehicleException, SimulationException {
		testCar = new Car("1", 10, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		currentTestCondition = !testCarPark.carParkEmpty(); //check if carparkEmpty returns false when one vehicle present
		assertTrue("carParkEmpty fails to return false when one vehicle present",(currentTestCondition));
	}
	
	/**
	 * Check that carParkEmpty returns false when multiple cars in carpark
	 */
	public void testCarParkEmptyMultipleVehicle() throws VehicleException, SimulationException {
		testCar = new Car("1", 10, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		testCar = new Car("1", 10, normalCarCondition); //create a second car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		currentTestCondition = !testCarPark.carParkEmpty(); //check if carparkEmpty returns false when multiple vehicles present
		assertTrue("carParkEmpty fails to return false when multiple vehicles present",(currentTestCondition));
	}
	
	
	////////////////////////////////////////////////////
	//			carParkFull
	////////////////////////////////////////////////////

	/**
	 * @author Lucas
	 * Check that carParkFull returns true when carpark is at capacity
	 */
	@Test 
	public void testCarParkFullAtCapacity() throws VehicleException, SimulationException {
		//park 100 cars in the carpark
		for (int i = 0; i < 70; i++) {
			testCar = new Car("1", 10, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//park 30 small cars in the carpark
		for (int i = 0; i < 30; i++) {
			testCar = new Car("1", 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//park 20 motorcycles in the carpark
		for (int i = 0; i < 20; i++) {
			testMotorCycle = new MotorCycle("1", 10); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, 100); 
		}
		
		currentTestCondition = testCarPark.carParkFull();
		
		assertTrue("carParkFull fails to return true, when carpark at capacty" ,currentTestCondition);
	}

	/**
	 * @author Lucas
	 * Check that carParkFull returns false carSpaces is one below capacity
	 */
	@Test 
	public void testCarParkCarOneBelow() throws VehicleException, SimulationException {
		//park 99 cars in the carpark
		for (int i = 0; i < 69; i++) {
			testCar = new Car("1", 10, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//park 20 small cars in the carpark
		for (int i = 0; i < 30; i++) {
			testCar = new Car("1", 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//park 20 motorcycles in the carpark
		for (int i = 0; i < 20; i++) {
			testMotorCycle = new MotorCycle("1", 10); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, 100); 
		}
		
		currentTestCondition = !testCarPark.carParkFull();
		assertTrue("carParkFull fails to return false, when carSpaces one below capacty" ,currentTestCondition);
	}
	
	/**
	 * @author Lucas
	 * Check that carParkFull returns false smallcarSpaces is one below capacity
	 */
	@Test 
	public void testCarParkSmallCarOneBelow() throws VehicleException, SimulationException {
		//park 19 small cars in the carpark
		for (int i = 0; i < 99; i++) {
			testCar = new Car("1", 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//park 20 motorcycles in the carpark
		for (int i = 0; i < 20; i++) {
			testMotorCycle = new MotorCycle("1", 10); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, 100); 
		}
		
		currentTestCondition = !testCarPark.carParkFull();
		assertTrue("carParkFull fails to return false, when smallCarSpaces one below capacty" ,currentTestCondition);
	}
	
	/**
	 * @author Lucas
	 * Check that carParkFull returns false motorCycleSpaces is one below capacity
	 */
	@Test 
	public void testCarParkMotorcycleOneBelow() throws VehicleException, SimulationException {
		//park 100 cars in the carpark
		for (int i = 0; i < 100; i++) {
			testCar = new Car("1", 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//park 19 motorcycles in the carpark
		for (int i = 0; i < 19; i++) {
			testMotorCycle = new MotorCycle("1", 10); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, 100); 
		}
		
		currentTestCondition = !testCarPark.carParkFull();
		
		assertTrue("carParkFull fails to return false, when motorCycleSpaces one below capacty" ,currentTestCondition);
	}

	/**
	 * @author Lucas
	 * Check that carParkFull returns false when no cars in carpark
	 */
	@Test 
	public void testCarParkFullWhenEmpty() throws VehicleException, SimulationException {
		currentTestCondition = !testCarPark.carParkFull();
		assertTrue("carParkFull fails to return false, when carPark is empty" ,currentTestCondition);
	}
	
	
	////////////////////////////////////////////////////
	//			enterQueue
	////////////////////////////////////////////////////
	
	/**
	 * @author Lucas
	 * Check that enterQueue throws SimulationException if queue is full when called
	 */  
	@Test  (expected = SimulationException.class)
	public void testEnterQueueFull() throws VehicleException, SimulationException {
		//try to place 11 vehicles in queue
		for (int i = 0; i < 11; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		assertTrue("SimulationException not thrown when enterQueue is called, and queue is already full",(true)); 
	}
	
	/**
	 * @author Lucas
	 * Check that enterQueue successfully adds one vehicle to the queue
	 */  
	@Test 
	public void testEnterQueueOne() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition);
		testCarPark.enterQueue(testCar);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 1);
		assertTrue("enterQueue does not successfully add a single vehicle to queue",(currentTestCondition)); 
	}
	
	/**
	 * Check that enterQueue successfully changes state of vehicle when adding to queue
	 */  
	@Test  
	public void testEnterQueueOneState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition);
		testCarPark.enterQueue(testCar);
		currentTestCondition = (testCar.isQueued());
		assertTrue("enterQueue does not successfully change state of vehicle when added to queue",(currentTestCondition)); 
	}
	
	/**
	 * Check that enterQueue successfully adds multiple vehicles to the queue
	 */  
	@Test  
	public void testEnterQueueMultiple() throws VehicleException, SimulationException {
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 3);
		assertTrue("enterQueue does not correctly add multiple vehicles to queue",(currentTestCondition)); 
	}
	
	
	////////////////////////////////////////////////////
	//			exitQueue
	////////////////////////////////////////////////////
	
	/**
	 * @author Lucas
	 * Check that exitQueue throws SimulationException if queue is empty when called
	 */  
	@Test  (expected = SimulationException.class)
	public void testExitQueueSimulationexception() throws VehicleException, SimulationException {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.exitQueue(testCar, currentTime);
		assertTrue("SimulationException not thrown when exitQueue is called, and queue is already empty",(true)); 
	}
	
	/**
	 * Check that exitQueue successfully removes one vehicle from the queue
	 */ 
	@Test  
	public void testExitQueueOne() throws VehicleException, SimulationException {
		//place 3 vehicles in queue
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		//remove one vehicle from queue
		testCarPark.exitQueue(testCar, currentTime);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 2);
		assertTrue("exitQueue does not correctly remove a single vehicle from queue",(currentTestCondition)); 
	}
	
	/**
	 * Check that exitQueue successfully changes state of vehicle when removing from queue
	 */
	@Test  
	public void testExitQueueOneState() throws VehicleException, SimulationException {
		//place 3 vehicles in queue
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		//remove one vehicle from queue
		testCarPark.exitQueue(testCar, currentTime);
		currentTestCondition = !testCar.isQueued();
		assertTrue("exitQueue does not correctly change state of vehicle when removed from queue",(currentTestCondition)); 
	}
	
	/**
	 * Check that exitQueue successfully removes multiple vehicles from the queue
	 */  
	/*
	@Test  
	public void testExitQueueMultiple() throws VehicleException, SimulationException {
		//place 8 vehicles in queue
		for (int i = 0; i < 8; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		//remove one vehicle from queue
		
		testCarPark.exitQueue(testCar, currentTime);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 2);
		assertTrue("exitQueue does not correctly remove a single vehicle from queue",(currentTestCondition)); 
	}
	*/

	/**
	 * Check that exitQueue successfully removes vehicle from the queue when only one present
	 */ 
	@Test  
	public void testExitQueueOnePresent() throws VehicleException, SimulationException {
		//place 1 vehicles in queue
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		//remove one vehicle from queue
		testCarPark.exitQueue(testCar, currentTime);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 0);
		assertTrue("exitQueue does not correctly remove a single vehicle from queue when one present",(currentTestCondition)); 
	}


	////////////////////////////////////////////////////
	//			getNumCars
	////////////////////////////////////////////////////
	
	/**
	 * @author Lucas
	 * Check that getNumCars returns correct value when no cars in carpark
	 */ 
	@Test
	public void testGetNumCarsZero() throws VehicleException, SimulationException {
		currentTestCondition = (testCarPark.getNumCars() == 0); //check if 0 cars in carpark
		assertTrue("getNumCars returns incorrect value when no cars in carpark",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that getNumCars returns correct value when one car in carpark
	 */ 
	@Test
	public void testGetNumCarsOne() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		currentTestCondition = (testCarPark.getNumCars() == 1); //check if 1 car in carpark
		assertTrue("getNumCars returns incorrect value when one car in carpark",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that getNumCars returns correct value when one small car in carpark
	 */ 
	@Test
	public void testGetNumCarsOneSmall() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, smallCarCondition); //create a small car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		currentTestCondition = (testCarPark.getNumCars() == 1); //check if 1 car in carpark
		assertTrue("getNumCars returns incorrect value when one small car in carpark",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that getNumCars returns correct value when multiple cars/small cars in carpark
	 */ 
	@Test
	public void testGetNumCarsMultiple() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		testCar = new Car(testVehicleID, 10, smallCarCondition); //create a small car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		testCar = new Car(testVehicleID, 10, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		currentTestCondition = (testCarPark.getNumCars() == 3); //check if 3 cars in carpark
		assertTrue("getNumCars returns incorrect value when one small car in carpark",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that getNumCars returns correct value when carpark full
	 */ 
	@Test
	public void testGetNumCarsFull() throws VehicleException, SimulationException {
		//park 100 cars in the carpark
		for (int i = 0; i < 70; i++) {
			testCar = new Car("1", 10, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//park 20 small cars in the carpark
		for (int i = 0; i < 30; i++) {
			testCar = new Car("1", 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		currentTestCondition = (testCarPark.getNumCars() == 100); //check if 120 cars in carpark
		assertTrue("getNumCars returns incorrect value when carpark full",(currentTestCondition));
	}
	
	
	////////////////////////////////////////////////////
	//			getNumMotorCycles
	////////////////////////////////////////////////////
	
	/**
	 * @author Lucas
	 * Check that getNumMotorCycles returns correct value when no motorcycles in carpark
	 */ 
	@Test
	public void testGetNumMotorCyclesZero() throws VehicleException, SimulationException {
		currentTestCondition = (testCarPark.getNumMotorCycles() == 0); //check if 0 motorcycles in carpark
		assertTrue("getNumMotorCycles returns incorrect value when no motorcycles in carpark",(currentTestCondition));
	}
	
	/**
	 * Check that getNumMotorCycles returns correct value when one motorcycle in carpark
	 */ 
	@Test
	public void testGetNumMotorCyclesOne() throws VehicleException, SimulationException {
		testMotorCycle = new MotorCycle(testVehicleID, 10); //create a motorcycle
		testCarPark.parkVehicle(testMotorCycle, currentTime, 100); //call park vehicle
		currentTestCondition = (testCarPark.getNumMotorCycles() == 1); //check if 1 motorcycle in carpark
		assertTrue("getNumMotorCycles returns incorrect value when one motorcycle in carpark",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that getNumMotorCycles returns correct value when more motorcycles than motorcycle parks
	 */ 
	@Test
	public void testGetNumMotorCyclesOneExtra() throws VehicleException, SimulationException {
		//park 21 motorcycles in the carpark
		for (int i = 0; i < 21; i++) {
			testMotorCycle = new MotorCycle("1", 10); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, 100); 
		}
		currentTestCondition = (testCarPark.getNumMotorCycles() == 21); //check if 21 motorCycles in carpark
		assertTrue("getNumMotorCycles returns incorrect value when more motorCycles "
				+ "than motorCycle spaces",(currentTestCondition));
	}

	/**
	 * @author Lucas
	 * Check that getNumMotorCycles returns correct value when multiple motorcycles in both parks
	 */ 
	@Test
	public void testGetNumMotorCyclesMultipleTypes() throws VehicleException, SimulationException {
		//park 10 small cars in the carpark
				for (int i = 0; i < 10; i++) {
					testCar = new Car("1", 10, smallCarCondition); 
					testCarPark.parkVehicle(testCar, currentTime, 100); 
				}
		//park 25 motorcycles in the carpark
		for (int i = 0; i < 25; i++) {
			testMotorCycle = new MotorCycle("1", 10); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, 100); 
		}
		currentTestCondition = (testCarPark.getNumMotorCycles() == 25); //check if 25 motorCycles in carpark
		assertTrue("getNumMotorCycles returns incorrect value when more motorCycles "
				+ "than motorCycle spaces and other vehicles present",(currentTestCondition));
	}
	
	/**
	 * Check that getNumMotorCycles returns correct value when motorcycle parks full
	 */ 
	@Test
	public void testGetNumMotorCyclesFull() throws VehicleException, SimulationException {
		//park 20 motorcycles in the carpark
		for (int i = 0; i < 20; i++) {
			testMotorCycle = new MotorCycle("1", 10); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, 100); 
		}
		currentTestCondition = (testCarPark.getNumMotorCycles() == 20); //check if 21 motorCycles in carpark
		assertTrue("getNumMotorCycles returns incorrect value when motorcycle "
				+ "parks are full",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that getNumMotorCycles returns correct value when motorcycle and small car parks full
	 */ 
	@Test
	public void testGetNumMotorCyclesBothFull() throws VehicleException, SimulationException {
		//park 40 motorcycles in the carpark
		for (int i = 0; i < 40; i++) {
			testMotorCycle = new MotorCycle("1", 10); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, 100); 
		}
		currentTestCondition = (testCarPark.getNumMotorCycles() == 40); //check if 40 motorCycles in carpark
		assertTrue("getNumMotorCycles returns incorrect value when both motorcycle "
				+ "and smallCar parks full",(currentTestCondition));
	}
	
	
	////////////////////////////////////////////////////
	//			getNumSmallCars
	////////////////////////////////////////////////////

	/**
	 * @author Lucas
	 * Check that getNumSmallCars returns correct value when no small cars in carpark
	 */ 
	@Test
	public void testGetNumSmallCarsZero() throws VehicleException, SimulationException {
		currentTestCondition = (testCarPark.getNumSmallCars() == 0); //check if 0 small cars in carpark
		assertTrue("getNumSmallCars returns incorrect value when no cars in carpark",(currentTestCondition));
	}

	/**
	 * @author Lucas
	 * Check that getNumSmallCars returns correct value when one small car in car park
	 */ 
	@Test
	public void testGetNumSmallCarsOne() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, smallCarCondition); //create a small car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		currentTestCondition = (testCarPark.getNumSmallCars() == 1); //check if 1 car in carpark
		assertTrue("getNumSmallCars returns incorrect value when one small car in carpark",(currentTestCondition));
	}

	/**
	 * @author Lucas
	 * Check that getNumSmallCars returns correct value when more small cars than spaces
	 */ 
	@Test
	public void testGetNumSmallCarsOneExtra() throws VehicleException, SimulationException {
		//park 21 small cars in the carpark
		for (int i = 0; i < 21; i++) {
			testCar = new Car("1", 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		currentTestCondition = (testCarPark.getNumSmallCars() == 21); //check if 21 small cars in carpark
		assertTrue("getNumSmallCars returns incorrect value when more small cars than small spaces",(currentTestCondition));
	}

	/**
	 * @author Lucas
	 * Check that getNumSmallCars returns correct value when multiple small cars in carpark
	 */ 
	@Test
	public void testGetNumSmallCarsMultiple() throws VehicleException, SimulationException {
		//park 21 small cars in the carpark
		for (int i = 0; i < 5; i++) {
			testCar = new Car("1", 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		currentTestCondition = (testCarPark.getNumSmallCars() == 5); //check if 5 small cars in carpark
		assertTrue("getNumSmallCars returns incorrect value when multiple small cars in carpark",(currentTestCondition));
	}

	/**
	 * Check that getNumSmallCars returns correct value when carpark full of small cars
	 */ 
	@Test
	public void testGetNumSmallCarsFull() throws VehicleException, SimulationException {
		//park 100 small cars in the carpark
		for (int i = 0; i < 100; i++) {
			testCar = new Car("1", 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		currentTestCondition = (testCarPark.getNumSmallCars() == 100); //check if 100 small cars in carpark
		assertTrue("getNumSmallCars returns incorrect value when carpark full of small cars",(currentTestCondition));
	}


	////////////////////////////////////////////////////
	//			numVehiclesInQueue
	////////////////////////////////////////////////////	

	/**
	 * @author Lucas
	 * Check that numVehiclesInQueue returns correct value when empty
	 */ 
	@Test  
	public void testNumVehiclesInQueueEmpty() throws VehicleException, SimulationException {
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 0);
		assertTrue("numVehiclesInQueue does not return currect value when empty",(currentTestCondition)); 
	}
	
	/**
	 * @author Lucas
	 * Check that numVehiclesInQueue returns correct value when one vehicle present
	 */ 
	@Test  
	public void testNumVehiclesInQueueOne() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition);
		testCarPark.enterQueue(testCar);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 1);
		assertTrue("numVehiclesInQueue does not return currect value when one vehicle present",(currentTestCondition)); 
	}
	
	/**
	 * Check that numVehiclesInQueue returns correct value when multiple vehicles present
	 */ 
	@Test  
	public void testNumVehiclesInQueueMultiple() throws VehicleException, SimulationException {
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 3);
		assertTrue("numVehiclesInQueue does not return currect value when multiple vehicles present",(currentTestCondition)); 
	}
	
	/**
	 * Check that numVehiclesInQueue returns correct value when full
	 */ 
	@Test  
	public void testNumVehiclesInQueueFull() throws VehicleException, SimulationException {
		for (int i = 0; i < 10; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 10);
		assertTrue("numVehiclesInQueue does not return currect value when queue full",(currentTestCondition)); 
	}
	

	////////////////////////////////////////////////////
	//			parkVehicle
	////////////////////////////////////////////////////
	
	/**
	 * Check that parkVehicle throws SimulationException when no suitable places are available for parking
	 */  
	@Test (expected = SimulationException.class)
	public void testParkVehicleNoSpaces() throws VehicleException, SimulationException {
		for (int i = 0; i < 101; i++) {
			testCar = new Car("1", 10, normalCarCondition); //create a car
			testCarPark.parkVehicle(testCar, currentTime, 100); //try to add 101 cars to carpark of max capacity 100
		}
		assertTrue("parkVehicle fails to throw simulation exception when carpark is full,"
				+ "and an attempt is made to add another vehicle",true);
	}
	
	/**
	 * Check that parkVehicle successfully adds car to carpark
	 */  
	@Test 
	public void testParkVehicleSuccessfully() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		currentTestCondition = (testCarPark.getNumCars() == 1); //check if 1 vehicle in carpark
		assertTrue("parkVehicle fails to park type Car",(currentTestCondition));
	}
	
	/**
	 * Check that parkVehicle successfully changes state of car when parking
	 */ 
	@Test 
	public void testParkVehicleChangeState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		currentTestCondition = (testCar.isParked()); //check if vehicle has changed state
		assertTrue("parkVehicle fails change state of vehicle to parked",(currentTestCondition));
	}
	
	/**
	 * Check that parkVehicle successfully adds small car to carpark
	 */  
	@Test
	public void testParkSmallCarSuccessfully() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, smallCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		currentTestCondition = (testCarPark.getNumSmallCars() == 1); //check if 1 vehicle in carpark
		assertTrue("parkVehicle fails to park small car",(currentTestCondition));
	}
	
	/**
	 * Check that parkVehicle successfully changes state of small car when parking
	 */ 
	@Test 
	public void testParkSmallCarChangeState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, smallCarCondition); //create a small car
		testCarPark.parkVehicle(testCar, currentTime, 100); //call park vehicle
		currentTestCondition = (testCar.isParked()); //check if small car has changed state
		assertTrue("parkVehicle fails change state of vehicle to parked",(currentTestCondition));
	}
	
	/**
	 * Check that parkVehicle successfully adds motorcycle to carpark
	 */  
	@Test
	public void testParkMotorcycleSuccessfully() throws VehicleException, SimulationException {
		testMotorCycle = new MotorCycle(testVehicleID, 10); //create a MotorCycle
		testCarPark.parkVehicle(testMotorCycle, currentTime, 100); //call park vehicle
		currentTestCondition = (testCarPark.getNumMotorCycles() == 1); //check if 1 MotorCycle in carpark
		assertTrue("parkVehicle fails to park MotorCycle",(currentTestCondition));
	}
	
	/**
	 * Check that parkVehicle successfully changes state of motorcycle when parking
	 */ 
	//@Test 
	public void testParkMotorCyceChangeState() throws VehicleException, SimulationException {
		testMotorCycle = new MotorCycle(testVehicleID, 10); //create a small car
		testCarPark.parkVehicle(testMotorCycle, currentTime, 100); //call park vehicle
		currentTestCondition = (testMotorCycle.isParked()); //check if MotorCycle has changed state
		assertTrue("parkVehicle fails change state of MotorCycle to parked",(currentTestCondition));
	}
	
	
	////////////////////////////////////////////////////
	//			processQueue
	////////////////////////////////////////////////////

	/**
	 * Check that processQueue runs when queue is empty, making no changes to parking spaces
	 */  
	@Test 
	public void testProcessQueueEmpty() throws VehicleException, SimulationException {
		testSimulator = new Simulator();
		//add 20 cars to carpark
		for (int i = 0; i < 20; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		testCarPark.processQueue(currentTime, testSimulator);
		currentTestCondition = ((testCarPark.getNumCars() == 20) && (testCarPark.queueEmpty()));
		assertTrue("processQueue fails to run correctly when queue is empty, altering"
				+ " contents of carpark",currentTestCondition);
	}
	
	/**
	 * @author Lucas
	 * Check that processQueue adds vehicle to carpark when a suitable space is available 
	 */  
	@Test 
	public void testProcessQueueOne() throws VehicleException, SimulationException {
		testSimulator = new Simulator();
		testCar = new Car(testVehicleID, 10, normalCarCondition); 
		testCarPark.enterQueue(testCar);
		testCarPark.processQueue(currentTime, testSimulator);
		currentTestCondition = ((testCarPark.getNumCars() == 1) && (testCarPark.queueEmpty()));
		assertTrue("processQueue fails to run correctly add vehicle to carpark and remove from queue "
				+ "when relevant spaces available",currentTestCondition);
	}
	
	/**
	 * @author Lucas
	 * Check that processQueue adds multiple vehicles to carpark when suitable spaces are available 
	 */  
	@Test 
	public void testProcessQueueMultiple() throws VehicleException, SimulationException {
		testSimulator = new Simulator();
		//add 3 cars to queue
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition); 
			testCarPark.enterQueue(testCar);
		}
		//add 3 small cars to queue
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, 10, smallCarCondition); 
			testCarPark.enterQueue(testCar);
		}
		//add 3 motorCycles to queue
		for (int i = 0; i < 3; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, 10); 
			testCarPark.enterQueue(testMotorCycle);
		}
		testCarPark.processQueue(currentTime, testSimulator);
		currentTestCondition = ((testCarPark.getNumCars() == 6) && (testCarPark.queueEmpty()));
		currentTestCondition &= ((testCarPark.getNumSmallCars() == 3) && (testCarPark.getNumMotorCycles() == 3));
		
		assertTrue("processQueue fails to correctly add multiple vehicles to carpark "
				+ "when relevant spaces available",currentTestCondition);
	}
	
	/**
	 * @author Lucas
	 * Check that processQueue blocks when no spaces are available for the first element in queue
	 */ 
	@Test 
	public void testProcessQueueBlocksMultiple() throws VehicleException, SimulationException {
		testSimulator = new Simulator();
		//add 70 cars to carpark
		for (int i = 0; i < 70; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime,100);;
		}
		//add car to queue
		testCar = new Car(testVehicleID, 10, normalCarCondition); 
		testCarPark.enterQueue(testCar);
		//add 3 small cars to queue
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, 10, smallCarCondition); 
			testCarPark.enterQueue(testCar);
		}
		//add 3 motorCycles to queue
		for (int i = 0; i < 3; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, 10); 
			testCarPark.enterQueue(testMotorCycle);
		}
		testCarPark.processQueue(currentTime, testSimulator);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 7);
		
		assertTrue("processQueue fails to block when first element in queue "
				+ "unable to park",currentTestCondition);
	}
	

	////////////////////////////////////////////////////
	//			queueEmpty
	////////////////////////////////////////////////////

	/**
	 * @author Lucas
	 * Check that queueEmpty returns true when no vehicles in queue
	 */
	@Test  
	public void testQueueEmptyNoVehicles() throws VehicleException, SimulationException {
		currentTestCondition = testCarPark.queueEmpty();
		assertTrue("queueEmpty does not return true when queue is empty",(currentTestCondition)); 
	}
	
	/**
	 * @author Lucas
	 * Check that queueEmpty returns false when one vehicle in queue
	 */
	@Test  
	public void testQueueEmptyOne() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition);
		testCarPark.enterQueue(testCar);
		currentTestCondition = !testCarPark.queueEmpty();
		assertTrue("queueEmpty does not return false when one vehicle in queue",(currentTestCondition)); 
	}
	
	/**
	 * @author Lucas
	 * Check that queueEmpty returns false when queue is full
	 */
	@Test  
	public void testQueueEmptyCapacity() throws VehicleException, SimulationException {
		for (int i = 0; i < 10; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		currentTestCondition = !testCarPark.queueEmpty();
		assertTrue("queueEmpty does not return false when queue is full",(currentTestCondition)); 
	}
	
	////////////////////////////////////////////////////
	//			queueFull
	////////////////////////////////////////////////////

	/**
	 * @author Lucas
	 * Check that queueFull returns true when queue is at capacity
	 */
	@Test  
	public void testQueueFullCapacity() throws VehicleException, SimulationException {
		for (int i = 0; i < 10; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		currentTestCondition = testCarPark.queueFull();
		assertTrue("queueFull does not return true when at capacity",(currentTestCondition)); 
	}

	/**
	 * @author Lucas
	 * Check that queueFull returns false queue is one below capacity
	 */
	@Test  
	public void testQueueFullOneBelow() throws VehicleException, SimulationException {
		for (int i = 0; i < 9; i++) {
			testCar = new Car(testVehicleID, 10, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		currentTestCondition = !testCarPark.queueFull();
		assertTrue("queueFull does not return false when one below capacity",(currentTestCondition)); 
	}

	/**
	 * @author Lucas
	 * Check that queueFull returns false when no vehicles in queue
	 */
	@Test  
	public void testQueueFullNoVehicles() throws VehicleException, SimulationException {
		currentTestCondition = !testCarPark.queueFull();
		assertTrue("queueFull does not return false when queue is empty",(currentTestCondition)); 
	}
	
	
	////////////////////////////////////////////////////
	//			spacesAvailable
	////////////////////////////////////////////////////

	/**
	 * @author Lucas
	 * Check that spacesAvailable returns true when vehicle is car and all relevant spaces available
	 */
	@Test 
	public void testSpacesAvailableCarEmpty() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition); //create a car
		currentTestCondition = testCarPark.spacesAvailable(testCar); //check if spaces available
		assertTrue("spacesAvailable fails to return true when vehicle is car"
				+ "and all relevant spaces available",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that spacesAvailable returns true vehicle is car and one relevant space available
	 */
	@Test 
	public void testSpacesAvailableCarOneBelow() throws VehicleException, SimulationException {
		//park 69 cars
		for (int i = 0; i < 69; i++) {
			testCar = new Car("1", 10, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//check if spaces available
		currentTestCondition = testCarPark.spacesAvailable(testCar); 
		assertTrue("spacesAvailable fails to return true when vehicle is car"
				+ "and relevant spaces are one below full",(currentTestCondition));
	}

	/**
	 * Check that spacesAvailable returns false when vehicle is car and no relevant spaces available
	 */
	@Test 
	public void testSpacesAvailableCarFull() throws VehicleException, SimulationException {
		//park 70 cars
		for (int i = 0; i < 70; i++) {
			testCar = new Car("1", 10, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//check if spaces available
		currentTestCondition = !testCarPark.spacesAvailable(testCar); 
		assertTrue("spacesAvailable fails to return false when vehicle is car"
				+ "and all relevant spaces are full",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that spacesAvailable returns true when vehicle is small car and all relevant spaces available
	 */
	@Test 
	public void testSpacesAvailableSmallCarEmpty() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, smallCarCondition); //create a small car
		currentTestCondition = testCarPark.spacesAvailable(testCar); //check if spaces available
		assertTrue("spacesAvailable fails to return true when vehicle is small car"
				+ "and all relevant spaces available",(currentTestCondition));
	}

	/**
	 * @author Lucas
	 * Check that spacesAvailable returns true vehicle is small car and one relevant space available
	 */
	@Test 
	public void testSpacesAvailableSmallCarOneBelow() throws VehicleException, SimulationException {
		//park 119 small cars
		for (int i = 0; i < 99; i++) {
			testCar = new Car("1", 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//check if spaces available
		currentTestCondition = testCarPark.spacesAvailable(testCar); 
		assertTrue("spacesAvailable fails to return true when vehicle is small car"
				+ "and relevant spaces are one below full",(currentTestCondition));
	}

	/**
	 * @author Lucas
	 * Check that spacesAvailable returns false when vehicle is small car and no relevant spaces available
	 */
	@Test 
	public void testSpacesAvailableSmallCarFull() throws VehicleException, SimulationException {
		//park 100 small cars
		for (int i = 0; i < 100; i++) {
			testCar = new Car("1", 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		//check if spaces available
		currentTestCondition = !testCarPark.spacesAvailable(testCar); 
		assertTrue("spacesAvailable fails to return false when vehicle is small car"
				+ "and relevant spaces are full",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that spacesAvailable returns true when vehicle is motorcycle and all relevant spaces available
	 */
	@Test 
	public void testSpacesAvailableMotorCycleEmpty() throws VehicleException, SimulationException {
		testMotorCycle = new MotorCycle(testVehicleID, 10); //create a motorCycle
		currentTestCondition = testCarPark.spacesAvailable(testMotorCycle); //check if spaces available
		assertTrue("spacesAvailable fails to return true when vehicle is motorCycle"
				+ "and all relevant spaces available",(currentTestCondition));
	}

	/**
	 * @author Lucas
	 * Check that spacesAvailable returns true vehicle is motorcycle and one relevant space available
	 */
	@Test 
	public void testSpacesAvailableMotorCycleOneBelow() throws VehicleException, SimulationException {
		//park 39 motorCycles
		for (int i = 0; i < 39; i++) {
			testMotorCycle = new MotorCycle("1", 10); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, 100); 
		}
		//check if spaces available
		currentTestCondition = testCarPark.spacesAvailable(testMotorCycle); 
		assertTrue("spacesAvailable fails to return true when vehicle is motorCycle"
				+ "and relevant spaces are one below full",(currentTestCondition));
	}

	/**
	 * @author Lucas
	 * Check that spacesAvailable returns false when vehicle is motorcycle and no relevant spaces available
	 */
	@Test 
	public void testSpacesAvailableMotorCycleFull() throws VehicleException, SimulationException {
		//park 50 motorCycles
		for (int i = 0; i < 50; i++) {
			testMotorCycle = new MotorCycle("1", 10); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, 100); 
		}
		//check if spaces available
		currentTestCondition = !testCarPark.spacesAvailable(testMotorCycle); 
		assertTrue("spacesAvailable fails to return false when vehicle is motorCycle"
				+ "and relevant spaces are full",(currentTestCondition));
	}


	////////////////////////////////////////////////////
	//			tryProcessNewVehicles
	////////////////////////////////////////////////////

	/**
	 * @author Lucas
	 * Check that tryProcessNewVehicle adds car to queue when car spaces empty and it is called
	 */
	@Test 
	public void testTryProcessNewVehicleCarFull() throws VehicleException, SimulationException {
		//park 100 small cars
		for (int i = 0; i < 100; i++) {
			testCar = new Car(testVehicleID, 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		
		testCarPark.tryProcessNewVehicles(currentTime, testSimulator);
		testValue = testCarPark.numVehiclesInQueue();
		currentTestCondition = (testValue == 1);
		assertTrue("tryProcessNewVehicle fails to add car to queue when car spaces are full"
				+ ", vehicles in queue =" + testValue,(currentTestCondition));
	}
	
	
	
	
	//need to complete
	
	
	////////////////////////////////////////////////////
	//			unparkVehicle
	////////////////////////////////////////////////////

	/**
	 * @author Lucas
	 * Check that unparkVehicle throws SimulationException if carpark does not contain vehicle to be removed
	 */ 
	@Test (expected = SimulationException.class)
	public void testUnparkVehicleNotInCarpark() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition); //create a car
		testCarPark.unparkVehicle(testCar, currentTime);
		assertTrue("unparkVehicle fails to throw SimulationException when carpark does"
				+ "not contain vehicle to be removed",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that unparkVehicle removes vehicle from carpark
	 */ 
	@Test 
	public void testUnparkVehicleOneCar() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime,100);
		testCarPark.unparkVehicle(testCar, 300);
		currentTestCondition = testCarPark.carParkEmpty();
		assertTrue("unparkVehicle fails to remove single car from carpark",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that unparkVehicle removes vehicle from carpark and changes its state to unparked
	 */ 
	@Test 
	public void testUnparkVehicleChangesState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, 10, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime,100);
		testCarPark.unparkVehicle(testCar, 300);
		currentTestCondition = !testCar.isParked();
		assertTrue("unparkVehicle fails to change state of vehicle to unparked",(currentTestCondition));
	}
	
	
	////////////////////////////////////////////////////
	//			miscelaneousTests
	////////////////////////////////////////////////////
	
	/**
	 * @author Lucas
	 * Check that if vehicle is added to queue by tryProcessNewVehicle and one is removed from carpark, 
	 * then process queue is run that the vehicle is then added to carpark
	 */
	@Test 
	public void testMovedToParkAfterQueue() throws VehicleException, SimulationException {
		//park 100 small cars
		for (int i = 0; i < 100; i++) {
			testCar = new Car(testVehicleID, 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		
		testCarPark.tryProcessNewVehicles(currentTime, testSimulator);
		testCarPark.archiveDepartingVehicles(5000, false);
		testCarPark.processQueue(currentTime + 1, testSimulator);
		testValue = testCarPark.getNumSmallCars();
		currentTestCondition = ((testValue == 1) && (testCarPark.numVehiclesInQueue() == 0));
		assertTrue("after added to queue vehicle is not correctly added to carpark when spaces are available"
				+ ", vehicles in carpark =" + testValue,(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that if vehicle is added to queue by tryProcessNewVehicle and one is removed from carpark, 
	 * then process queue is run that the vehicle is then added to carpark
	 */
	@Test 
	public void testMovedToArchiveAfterPark() throws VehicleException, SimulationException {
		//park 100 small cars
		for (int i = 0; i < 100; i++) {
			testCar = new Car(testVehicleID, 10, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, 100); 
		}
		
		testCarPark.tryProcessNewVehicles(currentTime, testSimulator);
		testCarPark.archiveDepartingVehicles(5000, false);
		testCarPark.processQueue(currentTime + 1, testSimulator);
		testCarPark.archiveDepartingVehicles(5000, false);
		testValue = testCarPark.getNumSmallCars();
		currentTestCondition = (testValue == 0);
		assertTrue("after added to carpark vehicle is not correctly added to archive when spaces are available"
				+ ", vehicles in carpark =" + testValue,(currentTestCondition));
	}
	
	
	//just a test method i have created to see where it is throwing the exception
	@Test 
	public void testRunCode() throws VehicleException, SimulationException {
		for (int i = 0; i < 135; i++) {
			time++;
			//queue elements exceed max waiting time
			if (!testCarPark.queueEmpty()) {
				testCarPark.archiveQueueFailures(time);
			}
			//vehicles whose time has expired
			if (!testCarPark.carParkEmpty()) {
				//force exit at closing time, otherwise normal
				boolean force = (time == Constants.CLOSING_TIME);
				testCarPark.archiveDepartingVehicles(time, force);
			}
			//attempt to clear the queue 
			if (!testCarPark.carParkFull()) {
				testCarPark.processQueue(time, testSimulator);
			}

			testCarPark.tryProcessNewVehicles(time, testSimulator);
		}

	}

}
