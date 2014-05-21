/**
 * 
 * This file is part of the CarParkSimulator Project, written as 
 * part of the assessment for INB370, semester 1, 2014. 
 *
 * CarParkSimulator
 * asgn2Tests 
 * 22/04/2014
 * 
 */
package asgn2Tests;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Vehicles.Car;

/**
 * @author hogan
 *
 */
public class CarTests {

	private Car testVehicle;
	
	private final String FIRST_CAR = "C1";		 
	
	private final int NORMAL_ARRIVAL = 1,
					  TOO_LOW_ARRIVAL = 0,
					  PARK_ON_ARRIVAL_TIME = NORMAL_ARRIVAL,
					  PARK_LATER = PARK_ON_ARRIVAL_TIME + 1,
					  PARK_TIME_BELOW_ZERO = -1,
					  NORMAL_DURATION = 100,
					  TOO_LOW_DURATION = Constants.MINIMUM_STAY - 5,
					  DEPART_NORMAL_DURATION = NORMAL_ARRIVAL + NORMAL_DURATION,
					  DEPART_EARLY = NORMAL_DURATION / 2,
					  DEPART_BAD_DEPARTURE_TIME = -1,
					  DEPART_QUEUE = NORMAL_ARRIVAL + 25,
					  DEPART_QUEUE_BAD_EXIT_TIME = NORMAL_ARRIVAL - 1,
					  DEPART_LONG_QUEUE = NORMAL_ARRIVAL + 26;
	
	private final boolean IS_NOT_SMALL = false,
						  IS_SMALL = true;
	
	/*
	 * Before method constructing a new normal car
	 * @throws asgn2Exceptions.VehicleException
	 */
	@Before
	public void constructCar() throws VehicleException {
		testVehicle = new Car(FIRST_CAR, NORMAL_ARRIVAL, IS_NOT_SMALL);
	}
	
	//---------------------Car constructor tests----------------------\\
	
	@Test(expected = VehicleException.class)
	/*
	 * throws exception when arrival time is lower than 0
	 */
	public void tooLowArrival() throws VehicleException {
		testVehicle = new Car(FIRST_CAR, TOO_LOW_ARRIVAL, IS_NOT_SMALL);
	}
	
	@Test
	/*
	 * check vehID after constructing car
	 */
	public void getVehIDAfterConstruct() {
		assertEquals(testVehicle.getVehID(), FIRST_CAR);
	}
	
	@Test
	/*
	 * check arrival time after constructing car
	 */
	public void getArrivalTimeAfterConstruct() {
		assertEquals(testVehicle.getArrivalTime(), NORMAL_ARRIVAL);
	}
	
	@Test
	/*
	 * check that the car is not small after false given in construct
	 */
	public void checkNotSmall() {
		assertEquals(testVehicle.isSmall(), false);
	}
	
	@Test
	/*
	 * check that the car is small after true given in construct
	 */
	public void checkIsSmall() throws VehicleException {
		testVehicle = new Car(FIRST_CAR, NORMAL_ARRIVAL, IS_SMALL);
		assertEquals(testVehicle.isSmall(), true);
	}
	
	//---------------------Parked State tests----------------------\\
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to park if car
	 * is already parked
	 */
	public void alreadyParkedParkedTest() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to park if car
	 * has not left the queue yet
	 */
	public void stillInQueue() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.enterParkedState(PARK_LATER, NORMAL_DURATION);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to park if intended duration
	 * is lower than minimum
	 */
	public void parkDurationTooLow() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, TOO_LOW_DURATION);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to park if the parking time
	 * is below 0
	 */
	public void parkArrivalBelowZero() throws VehicleException {
		testVehicle.enterParkedState(PARK_TIME_BELOW_ZERO, NORMAL_DURATION);
	}
	
	@Test
	/*
	 * check parking time after vehicle has parked
	 */
	public void vehicleParkTime() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		assertEquals(testVehicle.getParkingTime(), PARK_ON_ARRIVAL_TIME);
	}
	
	@Test
	/*
	 * Test if vehicle is in parked state after enterParkedState() func is called
	 */
	public void vehicleParked() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		assertEquals(testVehicle.isParked(), true);
	}
	
	@Test
	/*
	 * Test if vehicle was parked after enterParkedState() func is called
	 */
	public void vehicleWasParked() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		assertEquals(testVehicle.wasParked(), true);
	}
	
	@Test
	/*
	 * Test if intended departure time is returned after parking
	 */
	public void vehicleIntendedDepartureTime() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		assertEquals(testVehicle.getDepartureTime(), PARK_ON_ARRIVAL_TIME + NORMAL_DURATION);
	}
	
	@Test
	/*
	 * Test that customer is satisfied after parking
	 */
	public void vehicleParkedCustomerSatisfied() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		assertEquals(testVehicle.isSatisfied(), true);
	}
	
	//---------------------Queued State tests----------------------\\

	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to queue if car
	 * is already queued
	 */
	public void alreadyQueued() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.enterQueuedState();
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to queue if car
	 * is already parked
	 */
	public void alreadyParkedQueueTest() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.enterQueuedState();
	}
	
	@Test
	/*
	 * check that vehicle is in queue after enterQueuedState() func is called
	 */
	public void vehicleQueued() throws VehicleException {
		testVehicle.enterQueuedState();
		assertEquals(testVehicle.isQueued(), true);
	}
	
	@Test
	/*
	 * check that vehicle was in queue after enterQueuedState() func is called
	 */
	public void vehicleWasQueued() throws VehicleException {
		testVehicle.enterQueuedState();
		assertEquals(testVehicle.wasQueued(), true);
	}
	
	@Test
	/*
	 * check that customer is satisfied during the queue
	 */
	public void customerSatisfiedInQueue() throws VehicleException {
		testVehicle.enterQueuedState();
		assertEquals(testVehicle.isSatisfied(), true);
	}
	
	//---------------------exit parked state tests----------------------\\
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when vehicle is not parked
	 */
	public void notParkedExitParkTest() throws VehicleException {
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when vehicle is in a queued state
	 */
	public void departParkStateInQueue() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when departure time is less than parking time
	 */
	public void departParkStateBadDeparture() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_BAD_DEPARTURE_TIME);
	}
	
	@Test
	/*
	 * check departure time after leaving the park
	 */
	public void vehicleDepartureTime() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, DEPART_NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
		assertEquals(testVehicle.getDepartureTime(), DEPART_NORMAL_DURATION);
	}
	
	@Test
	/*
	 * check that car is no longer parked after exiting
	 */
	public void departedParkTrue() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
		assertEquals(testVehicle.isParked(), false);
	}
	
	@Test
	/*
	 * check that car was parked after exiting parked state
	 */
	public void departedParkWasParkTrue() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
		assertEquals(testVehicle.wasParked(), true);
	}
	
	@Test
	/*
	 * check customer sattisfied after leaving park
	 */
	public void customerSattisfied() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_NORMAL_DURATION);
		assertEquals(testVehicle.isSatisfied(), true);
	}
	
	@Test
	/*
	 * check vehicle is able to leave parked state early
	 * and departure time is still correct
	 */
	public void departParkEarlyDepartureTimeAdjusted() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitParkedState(DEPART_EARLY);
		assertEquals(testVehicle.getDepartureTime(), DEPART_EARLY);
	}
	
	//---------------------exit queued state tests----------------------\\
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when vehicle is not in the queue
	 */
	public void notQueuedExitQueueTest() throws VehicleException {
		testVehicle.exitQueuedState(DEPART_QUEUE);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when vehicle is parked
	 */
	public void vehicleParkedExitQueueTest() throws VehicleException {
		testVehicle.enterParkedState(PARK_ON_ARRIVAL_TIME, NORMAL_DURATION);
		testVehicle.exitQueuedState(DEPART_QUEUE);
	}
	
	@Test(expected = VehicleException.class)
	/*
	 * Throws exception when trying to exit queue if exitTime
	 * is not later than arrivalTime
	 */
	public void departQueueBadExitTime() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.exitQueuedState(DEPART_QUEUE_BAD_EXIT_TIME);
	}
	
	@Test
	/*
	 * check that car is no longer queued after exiting
	 */
	public void departedQueueTrue() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.exitQueuedState(DEPART_QUEUE);
		assertEquals(testVehicle.isQueued(), false);
	}
	
	@Test
	/*
	 * check that car was queued after leaving queue
	 */
	public void departedQueueWasQueuedTrue() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.exitQueuedState(DEPART_QUEUE);
		assertEquals(testVehicle.wasQueued(), true);
	}
	
	@Test
	/*
	 * customer dissatisfied after being in queue too long
	 */
	public void dissatisfiedCustomerLongQueue() throws VehicleException {
		testVehicle.enterQueuedState();
		testVehicle.exitQueuedState(DEPART_LONG_QUEUE);
		assertEquals(testVehicle.isSatisfied(), false);
	}
}
