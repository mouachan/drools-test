package com.amadeus.droolsfeature.rules;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.amadeus.droolsfeature.model.Booking;
import com.amadeus.droolsfeature.model.BzrFlightDeclare;
import com.amadeus.droolsfeature.runner.RulesRunner;

public class OptimRulesInsertTest {

	private RulesRunner runner = null;
	private ArrayList<Object> objects;
	private Logger logger = Logger.getLogger(OptimRulesInsertTest.class);
	long initStartTime;
	long runStartTime;
	Booking booking = null;
	BzrFlightDeclare bzrfd = null;

	private void createObjects(){
		booking = new Booking();
		bzrfd = new BzrFlightDeclare();
		bzrfd.setAirline("1A");
		booking.setFlight(bzrfd);
		objects = new ArrayList<Object>();
		objects.add(booking);
		objects.add(booking.getFlight());
	}

	@Test
	public void testOptimUsingInsertAndExist() {
		logger.info("Test without from");
		for (int i = 0; i < 10; i++) {
			createObjects();
			initStartTime = System.currentTimeMillis();
			runner = new RulesRunner();
			runner.initialisation("rules.drl");
			logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
			runStartTime = System.currentTimeMillis();
			runner.runRule(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
		}
	}

}
