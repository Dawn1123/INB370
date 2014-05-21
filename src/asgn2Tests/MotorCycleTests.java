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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import asgn2Exceptions.VehicleException;
import asgn2Simulators.Constants;
import asgn2Vehicles.Car;
import asgn2Vehicles.MotorCycle;
import asgn2Vehicles.Vehicle;

/**
 * @author hogan
 *
 */
public class MotorCycleTests {

	private Vehicle testVehicle;
	
	private Integer vehicleCount = 1;
	
	private final String FIRST_MOTORCYCLE = "C" + vehicleCount.toString(),
						 SECOND_MOTORCYCLE = "C" + vehicleCount.toString();		 
	
	private final int NORMAL_ARRIVAL = 1,
					  TOO_LOW_ARRIVAL = 0,
					  PARK_ON_ARRIVAL_TIME = NORMAL_ARRIVAL,
					  PARK_LATER = PARK_ON_ARRIVAL_TIME + 1,
					  PARK_BELOW_ZERO = -1,
					  NORMAL_DURATION = NORMAL_ARRIVAL + 100,
					  TOO_LOW_DURATION = Constants.MINIMUM_STAY - 5,
					  DEPART_NORMAL_DURATION = NORMAL_ARRIVAL + NORMAL_DURATION,
					  DEPART_SHORT_DURATION = NORMAL_DURATION / 2,
					  DEPART_QUEUE = NORMAL_ARRIVAL + 25,
					  DEPART_QUEUE_BAD_EXIT_TIME = NORMAL_ARRIVAL - 1,
					  DEPART_LONG_QUEUE = NORMAL_ARRIVAL + 26;
	
	
	/*
	 * Before method constructing a new motorcycle
	 * @throws asgn2Exceptions.VehicleException
	 */
	@Before
	public void constructCar() throws VehicleException {
		testVehicle = new MotorCycle(FIRST_MOTORCYCLE, NORMAL_ARRIVAL);
		vehicleCount += 1;
	}
	
	//---------------------MotorCycle constructor tests----------------------\\
	
	@Test(expected = VehicleException.class)
	/*
	 * throws exception when arrival time is lower than 0
	 */
	public void tooLowArrival() throws VehicleException {
		testVehicle = new MotorCycle(SECOND_MOTORCYCLE, TOO_LOW_ARRIVAL);
	}
	
	@Test
	/*
	 * check vehID after constructing car
	 */
	public void getVehIDAfterConstruct() {
		assertEquals(testVehicle.getVehID(), FIRST_MOTORCYCLE);
	}
	
	@Test
	/*
	 * check arrival time after constructing car
	 */
	public void getArrivalTimeAfterConstruct() {
		assertEquals(testVehicle.getArrivalTime(), NORMAL_ARRIVAL);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link asgn2Vehicles.MotorCycle#MotorCycle(java.lang.String, int)}.
	 */
	@Test
	public void testMotorCycle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#Vehicle(java.lang.String, int)}.
	 */
	@Test
	public void testVehicle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getVehID()}.
	 */
	@Test
	public void testGetVehID() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getArrivalTime()}.
	 */
	@Test
	public void testGetArrivalTime() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterQueuedState()}.
	 */
	@Test
	public void testEnterQueuedState() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitQueuedState(int)}.
	 */
	@Test
	public void testExitQueuedState() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#enterParkedState(int, int)}.
	 */
	@Test
	public void testEnterParkedState() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState(int)}.
	 */
	@Test
	public void testExitParkedStateInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#exitParkedState()}.
	 */
	@Test
	public void testExitParkedState() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isParked()}.
	 */
	@Test
	public void testIsParked() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isQueued()}.
	 */
	@Test
	public void testIsQueued() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getParkingTime()}.
	 */
	@Test
	public void testGetParkingTime() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#getDepartureTime()}.
	 */
	@Test
	public void testGetDepartureTime() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#wasQueued()}.
	 */
	@Test
	public void testWasQueued() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#wasParked()}.
	 */
	@Test
	public void testWasParked() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#isSatisfied()}.
	 */
	@Test
	public void testIsSatisfied() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link asgn2Vehicles.Vehicle#toString()}.
	 */
	@Test
	public void testToString() {
		fail("Not yet implemented"); // TODO
	}

}
