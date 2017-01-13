package com.amadeus.droolsfeature.rules;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kie.api.definition.type.FactType;

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

	private void createObjects() {
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

	@Test
	public void testAgendaGroupProblem() {
		logger.info("Test agenda group problem");
		objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		runner = new RulesRunner();
		runner.initialisation("hatemrule.drl");
		FactType marketType = runner.getKieBase().getFactType("com.amadeus.droolsfeature.rules", "BzrMarketDeclare");
		try {
			Object market = marketType.newInstance();
			marketType.set(market, "airport", "LIL");
			objects.add(market);
			logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
			runStartTime = System.currentTimeMillis();
			runner.setSession();
			runner.getSession().getAgenda().getAgendaGroup("abr.keywords").setFocus();
			runner.runRule(objects);
			logger.info("running rules elapsed time using insert and exist "
					+ (System.currentTimeMillis() - runStartTime) + "ms");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
