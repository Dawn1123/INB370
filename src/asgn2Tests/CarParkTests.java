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
	//static variables used to make program more readable
	private static boolean smallCarCondition = true;
	private static boolean normalCarCondition = false;
	private static String testVehicleID = "1";
	private static int overTimeValue = 300;
	private static int startTime = 10;
	private static int standardParkingDuration = 100;
	private static int currentTime = 100;
	
	//variables used for calculation/indication of test cases
	boolean currentTestCondition;
	private int testValue;
	private int time = 1;
	
	//objects used in testing
	private CarPark testCarPark;
	private Car testCar;
	private Car testSmallCar;
	private MotorCycle testMotorCycle;
	private Simulator testSimulator;
	
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
	 * Check that VehicleException is thrown when vehicle is not in the correct state
	 * @throws SimulationException 
	 * @throws VehicleException 
	 */
	@Test  (expected = VehicleException.class)
	public void testArchiveDepartingIncorrectState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration);
		testCar.enterQueuedState();
		testCarPark.archiveDepartingVehicles(0, false);	
		assertTrue("VehicleException not thrown when ArchiveDepartingVehicles"
				+ " is called and vehicle is in the incorrect state",(true)); 
	}
	
	/**
	 * @author Lucas
	 * Check that vehicle is removed from carpark when greater than time, and force is false
	 */
	@Test 
	public void testArchiveVehicleOverTime() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		currentTestCondition = (!testCar.isSatisfied()); 
		assertTrue("archiveNewVehicle fails to set vehicle to unsatisfied",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that vehicle is not parked or placed in queue
	 */
	@Test 
	public void testArchiveNewVehicleArchived() throws SimulationException, VehicleException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.archiveNewVehicle(testCar); 
		currentTestCondition = (testCarPark.carParkEmpty() && testCarPark.queueEmpty()); 
		assertTrue("archiveNewVehicle incorrectly adds vehicle to queue or carpark",(currentTestCondition));
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
	 * Check that vehicle is set to dissatisfied when removed from queue
	 */
	@Test 
	public void testArchiveQueueFailuresDissatisfied() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, currentTime, false); 
		testCarPark.enterQueue(testCar);
		//call archiveDepartingVehicles when single vehicle is over time
		testCarPark.archiveQueueFailures(overTimeValue); 
		currentTestCondition = (!testCar.isSatisfied()); 
		assertTrue("Vehicle is not set to dissatisfied when removed from queue when over time",(currentTestCondition));
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
	 * Check that no vehicles removed from queue when multiple cars less than time
	 */
	@Test 
	public void testArchiveQueueFailuresMultipleCarsUnderTime() throws VehicleException, SimulationException {
		//add 5 cars to queue
		for (int i = 0; i < 5; i++) {
			testCar = new Car(testVehicleID, currentTime, false); 
			testCarPark.enterQueue(testCar);
		}
		//call archiveDepartingVehicles when multiple cars are under time
		testCarPark.archiveQueueFailures(currentTime+1); 
		currentTestCondition = (!testCarPark.queueEmpty()); 
		assertTrue("Vehicles are incorrectly removed from queue when multiple cars under time",(currentTestCondition));
	}
	
	/**
	 * Check that no vehicles removed from queue when multiple motorcycles less than time
	 */
	@Test 
	public void testArchiveQueueFailuresMultipleMotorCyclesUnderTime() throws VehicleException, SimulationException {
		//add 5 motorcycles to queue
		for (int i = 0; i < 5; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, currentTime); 
			testCarPark.enterQueue(testMotorCycle);
		}
		//call archiveDepartingVehicles when multiple motorcycles are under time
		testCarPark.archiveQueueFailures(currentTime+1); 
		currentTestCondition = (!testCarPark.queueEmpty()); 
		assertTrue("Vehicles are incorrectly removed from queue when multiple motorcycles under time",(currentTestCondition));
	}
	
	/**
	 * Check that multiple vehicles removed from queue when multiple cars greater than time
	 */
	@Test 
	public void testArchiveQueueFailuresMultipleCarsOverTime() throws VehicleException, SimulationException {
		//add 5 cars to queue
		for (int i = 0; i < 5; i++) {
			testCar = new Car(testVehicleID, currentTime, false); 
			testCarPark.enterQueue(testCar);
		}
		//call archiveDepartingVehicles when multiple vehicles are over time
		testCarPark.archiveQueueFailures(overTimeValue); 
		currentTestCondition = (testCarPark.queueEmpty()); 
		assertTrue("Vehicles aren't removed from queue when multiple cars over time",(currentTestCondition));
	}

	/**
	 * Check that multiple vehicles removed from queue when multiple motorcycles greater than time
	 */
	@Test 
	public void testArchiveQueueFailuresMultipleMotorCyclesOverTime() throws VehicleException, SimulationException {
		//add 5 motorcycles to queue
		for (int i = 0; i < 5; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, currentTime); 
			testCarPark.enterQueue(testMotorCycle);
		}
		//call archiveDepartingVehicles when multiple motorcycles are over time
		testCarPark.archiveQueueFailures(overTimeValue); 
		currentTestCondition = (testCarPark.queueEmpty()); 
		assertTrue("Vehicles aren't removed from queue when multiple motorcycles over time",(currentTestCondition));
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
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		currentTestCondition = !testCarPark.carParkEmpty(); //check if carparkEmpty returns false when one vehicle present
		assertTrue("carParkEmpty fails to return false when one vehicle present",(currentTestCondition));
	}
	
	/**
	 * Check that carParkEmpty returns false when multiple cars in carpark
	 */
	public void testCarParkEmptyMultipleVehicle() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a second car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
		}
		//park 30 small cars in the carpark
		for (int i = 0; i < 30; i++) {
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
		}
		//park 20 motorcycles in the carpark
		for (int i = 0; i < 20; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); 
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
		//park 69 cars in the carpark
		for (int i = 0; i < 69; i++) {
			testCar = new Car(testVehicleID, startTime, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
		}
		//park 30 small cars in the carpark
		for (int i = 0; i < 30; i++) {
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
		}
		//park 20 motorcycles in the carpark
		for (int i = 0; i < 20; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); 
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
		//park 99 small cars in the carpark
		for (int i = 0; i < 99; i++) {
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
		}
		//park 20 motorcycles in the carpark
		for (int i = 0; i < 20; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); 
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
		//park 100 small cars in the carpark
		for (int i = 0; i < 100; i++) {
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
		}
		//park 19 motorcycles in the carpark
		for (int i = 0; i < 19; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); 
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
	 * Check that enterQueue throws SimulationException if queue is full of cars when called
	 */  
	@Test  (expected = SimulationException.class)
	public void testEnterQueueFullCars() throws VehicleException, SimulationException {
		//try to place 11 cars in queue
		for (int i = 0; i < 11; i++) {
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		assertTrue("SimulationException not thrown when enterQueue is called, and queue is already full of cars",(true)); 
	}
	
	/**
	 * @author Lucas
	 * Check that enterQueue throws SimulationException if queue is full of cars when called
	 */  
	@Test  (expected = SimulationException.class)
	public void testEnterQueueFullSmallCars() throws VehicleException, SimulationException {
		//try to place 11 small cars in queue
		for (int i = 0; i < 11; i++) {
			testCar = new Car(testVehicleID, startTime, smallCarCondition);
			testCarPark.enterQueue(testCar);
		}
		assertTrue("SimulationException not thrown when enterQueue is called, and queue is already full of small cars",(true)); 
	}
	
	/**
	 * @author Lucas
	 * Check that enterQueue throws SimulationException if queue is full of motorcycles when called
	 */  
	@Test  (expected = SimulationException.class)
	public void testEnterQueueFullMotorCycles() throws VehicleException, SimulationException {
		//try to place 11 motorcycles in queue
		for (int i = 0; i < 11; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, startTime);
			testCarPark.enterQueue(testMotorCycle);
		}
		assertTrue("SimulationException not thrown when enterQueue is called, and queue is already full of motorcycles",(true)); 
	}
	
	/**
	 * @author Lucas
	 * Check that enterQueue throws VehicleException if vehicle is in a parked state when called
	 */  
	@Test  (expected = VehicleException.class)
	public void testEnterQueueIncorrectStateParked() throws VehicleException, SimulationException {
		//park vehicle and then try to enter queue
		testCar = new Car(testVehicleID, startTime, normalCarCondition);
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration);
		testCarPark.enterQueue(testCar);

		assertTrue("VehicleException not thrown when enterQueue is called, and vehicle is parked",(true)); 
	}
	
	/**
	 * @author Lucas
	 * Check that enterQueue throws VehicleException if vehicle is in a queued state when called
	 */  
	@Test  (expected = VehicleException.class)
	public void testEnterQueueIncorrectStateQueued() throws VehicleException, SimulationException {
		//queue vehicle and then try to enter queue again
		testCar = new Car(testVehicleID, startTime, normalCarCondition);
		testCarPark.enterQueue(testCar);
		testCarPark.enterQueue(testCar);

		assertTrue("VehicleException not thrown when enterQueue is called, and vehicle is already queued",(true)); 
	}
	
	/**
	 * @author Lucas
	 * Check that enterQueue throws VehicleException if vehicle is in an archived state when called
	 */  
	@Test  (expected = VehicleException.class)
	public void testEnterQueueIncorrectStateArchived() throws VehicleException, SimulationException {
		//archive vehicle and then try to enter queue
		testCar = new Car(testVehicleID, startTime, normalCarCondition);
		testCarPark.archiveNewVehicle(testCar);
		testCarPark.enterQueue(testCar);

		assertTrue("VehicleException not thrown when enterQueue is called, and vehicle is archived",(true)); 
	}
	
	/**
	 * @author Lucas
	 * Check that enterQueue successfully adds one car to the queue
	 */  
	@Test 
	public void testEnterQueueOneCar() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, startTime, normalCarCondition);
		testCarPark.enterQueue(testCar);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 1);
		assertTrue("enterQueue does not successfully add a single car to queue",(currentTestCondition)); 
	}
	
	/**
	 * @author Lucas
	 * Check that enterQueue successfully adds one small car to the queue
	 */  
	@Test 
	public void testEnterQueueOneSmallCar() throws VehicleException, SimulationException {
		testSmallCar = new Car(testVehicleID, startTime, smallCarCondition);
		testCarPark.enterQueue(testSmallCar);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 1);
		assertTrue("enterQueue does not successfully add a single small car to queue",(currentTestCondition)); 
	}
	
	/**
	 * @author Lucas
	 * Check that enterQueue successfully adds one motorcycle to the queue
	 */  
	@Test 
	public void testEnterQueueOneMotorCycle() throws VehicleException, SimulationException {
		testMotorCycle = new MotorCycle(testVehicleID, startTime);
		testCarPark.enterQueue(testMotorCycle);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 1);
		assertTrue("enterQueue does not successfully add a single motorcycle to queue",(currentTestCondition)); 
	}
	
	/**
	 * Check that enterQueue successfully changes state of vehicle when adding to queue
	 */  
	@Test  
	public void testEnterQueueOneState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
			testCarPark.exitQueue(testCar, currentTime);
		assertTrue("SimulationException not thrown when exitQueue is called, and queue is already empty",(true)); 
	}
	
	/**
	 * Check that exitQueue successfully removes one car from the queue
	 */ 
	@Test  
	public void testExitQueueOneCar() throws VehicleException, SimulationException {
		//place 3 cars in queue
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
			testCarPark.enterQueue(testCar);
		}
		//remove one car from queue
		testCarPark.exitQueue(testCar, currentTime);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 2);
		assertTrue("exitQueue does not correctly remove a single car from queue",(currentTestCondition)); 
	}
	
	/**
	 * Check that exitQueue successfully removes one small car from the queue
	 */ 
	@Test  
	public void testExitQueueOneSmallCar() throws VehicleException, SimulationException {
		//place 3 small cars in queue
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, startTime, smallCarCondition);
			testCarPark.enterQueue(testCar);
		}
		//remove one small car from queue
		testCarPark.exitQueue(testCar, currentTime);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 2);
		assertTrue("exitQueue does not correctly remove a single small car from queue",(currentTestCondition)); 
	}
	
	/**
	 * Check that exitQueue successfully removes one motorcycle from the queue
	 */ 
	@Test  
	public void testExitQueueOneMotorCycle() throws VehicleException, SimulationException {
		//place 3 motorcycles in queue
		for (int i = 0; i < 3; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, startTime);
			testCarPark.enterQueue(testMotorCycle);
		}
		//remove one motorcycle from queue
		testCarPark.exitQueue(testMotorCycle, currentTime);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 2);
		assertTrue("exitQueue does not correctly remove a single motorcycle from queue",(currentTestCondition)); 
	}
	
	/**
	 * Check that exitQueue successfully changes state of vehicle when removing from queue
	 */
	@Test  
	public void testExitQueueOneState() throws VehicleException, SimulationException {
		//place 3 vehicles in queue
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
	@Test  
	public void testExitQueueMultiple() throws VehicleException, SimulationException {
		//place 3 vehicles in queue
		testCar = new Car(testVehicleID, startTime, normalCarCondition);
		testCarPark.enterQueue(testCar);
		testSmallCar = new Car(testVehicleID, startTime, smallCarCondition);
		testCarPark.enterQueue(testSmallCar);
		testMotorCycle = new MotorCycle(testVehicleID, startTime);
		testCarPark.enterQueue(testMotorCycle);
		
		//remove the 3 vehicles from the queue
		testCarPark.exitQueue(testCar, currentTime);
		testCarPark.exitQueue(testSmallCar, currentTime);
		testCarPark.exitQueue(testMotorCycle, currentTime);
		currentTestCondition = (testCarPark.numVehiclesInQueue() == 0);
		assertTrue("exitQueue does not correctly remove multiple vehicles from queue",(currentTestCondition)); 
	}

	/**
	 * Check that exitQueue successfully removes vehicle from the queue when only one present
	 */ 
	@Test  
	public void testExitQueueOnePresent() throws VehicleException, SimulationException {
		//place 1 vehicles in queue
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		currentTestCondition = (testCarPark.getNumCars() == 1); //check if 1 car in carpark
		assertTrue("getNumCars returns incorrect value when one car in carpark",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that getNumCars returns correct value when one small car in carpark
	 */ 
	@Test
	public void testGetNumCarsOneSmall() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, startTime, smallCarCondition); //create a small car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		currentTestCondition = (testCarPark.getNumCars() == 1); //check if 1 car in carpark
		assertTrue("getNumCars returns incorrect value when one small car in carpark",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that getNumCars returns correct value when multiple cars/small cars in carpark
	 */ 
	@Test
	public void testGetNumCarsMultiple() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		testCar = new Car(testVehicleID, startTime, smallCarCondition); //create a small car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		currentTestCondition = (testCarPark.getNumCars() == 3); //check if 3 cars in carpark
		assertTrue("getNumCars returns incorrect value when one small car in carpark",(currentTestCondition));
	}
	
	/**
	 * @author Lucas
	 * Check that getNumCars returns correct value when carpark full
	 */ 
	@Test
	public void testGetNumCarsFull() throws VehicleException, SimulationException {
		//park 70 cars in the carpark
		for (int i = 0; i < 70; i++) {
			testCar = new Car(testVehicleID, startTime, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
		}
		//park 20 small cars in the carpark
		for (int i = 0; i < 30; i++) {
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testMotorCycle = new MotorCycle(testVehicleID, startTime); //create a motorcycle
		testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); //call park vehicle
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
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); 
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
					testCar = new Car(testVehicleID, startTime, smallCarCondition); 
					testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
				}
		//park 25 motorcycles in the carpark
		for (int i = 0; i < 25; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); 
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
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); 
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
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); 
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
		testCar = new Car(testVehicleID, startTime, smallCarCondition); //create a small car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
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
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //try to add 101 cars to carpark of max capacity 100
		}
		assertTrue("parkVehicle fails to throw simulation exception when carpark is full,"
				+ "and an attempt is made to add another vehicle",true);
	}
	
	/**
	 * Check that parkVehicle successfully adds car to carpark
	 */  
	@Test 
	public void testParkVehicleSuccessfully() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		currentTestCondition = (testCarPark.getNumCars() == 1); //check if 1 vehicle in carpark
		assertTrue("parkVehicle fails to park type Car",(currentTestCondition));
	}
	
	/**
	 * Check that parkVehicle successfully changes state of car when parking
	 */ 
	@Test 
	public void testParkVehicleChangeState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		currentTestCondition = (testCar.isParked()); //check if vehicle has changed state
		assertTrue("parkVehicle fails change state of vehicle to parked",(currentTestCondition));
	}
	
	/**
	 * Check that parkVehicle successfully adds small car to carpark
	 */  
	@Test
	public void testParkSmallCarSuccessfully() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, startTime, smallCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		currentTestCondition = (testCarPark.getNumSmallCars() == 1); //check if 1 vehicle in carpark
		assertTrue("parkVehicle fails to park small car",(currentTestCondition));
	}
	
	/**
	 * Check that parkVehicle successfully changes state of small car when parking
	 */ 
	@Test 
	public void testParkSmallCarChangeState() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, startTime, smallCarCondition); //create a small car
		testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); //call park vehicle
		currentTestCondition = (testCar.isParked()); //check if small car has changed state
		assertTrue("parkVehicle fails change state of vehicle to parked",(currentTestCondition));
	}
	
	/**
	 * Check that parkVehicle successfully adds motorcycle to carpark
	 */  
	@Test
	public void testParkMotorcycleSuccessfully() throws VehicleException, SimulationException {
		testMotorCycle = new MotorCycle(testVehicleID, startTime); //create a MotorCycle
		testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); //call park vehicle
		currentTestCondition = (testCarPark.getNumMotorCycles() == 1); //check if 1 MotorCycle in carpark
		assertTrue("parkVehicle fails to park MotorCycle",(currentTestCondition));
	}
	
	/**
	 * Check that parkVehicle successfully changes state of motorcycle when parking
	 */ 
	//@Test 
	public void testParkMotorCyceChangeState() throws VehicleException, SimulationException {
		testMotorCycle = new MotorCycle(testVehicleID, startTime); //create a small car
		testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); //call park vehicle
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testCar = new Car(testVehicleID, startTime, normalCarCondition); 
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition); 
			testCarPark.enterQueue(testCar);
		}
		//add 3 small cars to queue
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.enterQueue(testCar);
		}
		//add 3 motorCycles to queue
		for (int i = 0; i < 3; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime,standardParkingDuration);;
		}
		//add car to queue
		testCar = new Car(testVehicleID, startTime, normalCarCondition); 
		testCarPark.enterQueue(testCar);
		//add 3 small cars to queue
		for (int i = 0; i < 3; i++) {
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.enterQueue(testCar);
		}
		//add 3 motorCycles to queue
		for (int i = 0; i < 3; i++) {
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
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
		testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition);
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
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
			testCar = new Car(testVehicleID, startTime, normalCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testCar = new Car(testVehicleID, startTime, smallCarCondition); //create a small car
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
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
		testMotorCycle = new MotorCycle(testVehicleID, startTime); //create a motorCycle
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
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); 
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
			testMotorCycle = new MotorCycle(testVehicleID, startTime); 
			testCarPark.parkVehicle(testMotorCycle, currentTime, standardParkingDuration); 
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
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
		}
		
		testCarPark.tryProcessNewVehicles(currentTime, testSimulator);
		testValue = testCarPark.numVehiclesInQueue();
		currentTestCondition = (testValue == 1);
		assertTrue("tryProcessNewVehicle fails to add car to queue when car spaces are full"
				+ ", vehicles in queue =" + testValue,(currentTestCondition));
	}
	
	
	////////////////////////////////////////////////////
	//			unparkVehicle
	////////////////////////////////////////////////////

	/**
	 * @author Lucas
	 * Check that unparkVehicle throws SimulationException if carpark does not contain vehicle to be removed
	 */ 
	@Test (expected = SimulationException.class)
	public void testUnparkVehicleNotInCarpark() throws VehicleException, SimulationException {
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
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
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime,standardParkingDuration);
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
		testCar = new Car(testVehicleID, startTime, normalCarCondition); //create a car
		testCarPark.parkVehicle(testCar, currentTime,standardParkingDuration);
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
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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
			testCar = new Car(testVehicleID, startTime, smallCarCondition); 
			testCarPark.parkVehicle(testCar, currentTime, standardParkingDuration); 
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

}
