package com.amadeus.droolsfeature.rules;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.amadeus.droolsfeature.model.Booking;
import com.amadeus.droolsfeature.model.BzrFlightDeclare;
import com.amadeus.droolsfeature.runner.RulesRunner;

public class OptimRulesUpdateTest {

	private RulesRunner runner = null;
	private ArrayList<Object> objects;
	private Logger logger = Logger.getLogger(OptimRulesUpdateTest.class);
	long initStartTime;
	long runStartTime;
	Booking booking = null;
	BzrFlightDeclare bzrfd = null;

	private void createObjects() {
		booking = new Booking();
		bzrfd = new BzrFlightDeclare();
		bzrfd.setAirline("1A");
		booking.setFlight(bzrfd);
		objects = new ArrayList<Object>();
		objects.add(booking);
		objects.add(booking.getFlight());
	}
	private void runRule(){
		RulesRunner runner = new RulesRunner();
		runner.initialisation("init.drl");
		runner.setSession();
		objects = new ArrayList<Object>();
		runner.runRule(objects);
	}
	@Test
	public void testOptimUsingUpdateExistAndFrom() {
		logger.info("Test with from");
		runRule();
		runner = new RulesRunner();
		RulesRunner runner2 = new RulesRunner();
		initStartTime = System.currentTimeMillis();
		runner.initialisation("rules2.drl");
		logger.info("Init elapsed time " + (System.currentTimeMillis() - initStartTime) + "ms");
		long initStartTime2 = System.currentTimeMillis();
		runner2.initialisation("rules.drl");
		logger.info("Init 2 elapsed time " + (System.currentTimeMillis() - initStartTime2) + "ms");
		long runStartTime2;
		for (int i = 0; i < 100000; i++) {
			createObjects();
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			runner.addAgendaListener();
			runner.runRule(objects);
			logger.info("running rules 2 elapsed time using update exist and from "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
			createObjects();
			runStartTime2 = System.currentTimeMillis();
			runner2.setSession();
			runner2.addAgendaListener();
			runner2.runRule(objects);
			logger.info("running rules 1 elapsed time using insert "
					+ (System.currentTimeMillis() - runStartTime2) + "ms");
		}

	}
	
	@Test
	public void testOptimUsingInsertAndExist() {
		logger.info("Test without from");
		runner = new RulesRunner();
		initStartTime = System.currentTimeMillis();
		runner.initialisation("rules.drl");
		logger.info("Init elapsed time " + (System.currentTimeMillis() - initStartTime) + "ms");
		for (int i = 0; i < 10; i++) {
			createObjects();
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			runner.addAgendaListener();
			runner.runRule(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
		}
	}
	
/*	@Test
	public void testOptimUsingFromAndMemberOf() {
		logger.info("Test without from");
		runner = new RulesRunner();
		initStartTime = System.currentTimeMillis();
		runner.initialisation("rules3.drl");
		logger.info("Init elapsed time " + (System.currentTimeMillis() - initStartTime) + "ms");
		for (int i = 0; i < 100000; i++) {
			createObjects();
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			runner.addAgendaListener();
			runner.runRule(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
		}
	}*/

}
