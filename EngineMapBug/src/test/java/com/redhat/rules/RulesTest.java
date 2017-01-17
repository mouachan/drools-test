package com.redhat.rules;

import java.util.ArrayList;

import org.junit.Test;
import org.kie.api.definition.type.FactType;
import org.apache.log4j.Logger;

import com.redhat.rule.runner.RulesRunner;

public class RulesTest {
	private static Logger logger = Logger.getLogger(RulesTest.class);
	private long initStartTime;
	private long runStartTime;

	@Test
	public void testWithCollection() {
		logger.info("Test BUG Rule");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("ruleArrayList.drl");
		FactType BookingType = runner.getKieBase().getFactType("com.redhat.rules", "Booking");
		FactType FlightType = runner.getKieBase().getFactType("com.redhat.rules","Flight");

		try {
			for (int i = 0; i < 100; i++) {
				logger.info("############Iteration : "+i+" ##################");
				Object booking = BookingType.newInstance();
				BookingType.set(booking, "customer", "Bob");

				Object flight = FlightType.newInstance();
				FlightType.set(flight, "airline", "1A");
				FlightType.set(flight, "number", 500);

				Object returnFlight = FlightType.newInstance();
				FlightType.set(returnFlight, "airline", "AF");
				FlightType.set(returnFlight, "number", 666);

				BookingType.set(booking, "flight", flight);
				BookingType.set(booking, "returnFlight", returnFlight);

				objects.add(returnFlight);
				objects.add(flight);
				objects.add(booking);

				logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
				runStartTime = System.currentTimeMillis();
				runner.setSession();
				runner.runRule(objects);
			
				logger.info("running rules elapsed time "
						+ (System.currentTimeMillis() - runStartTime) + "ms");
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testWithHashMap() {
		logger.info("Test BUG Rule");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("ruleMap.drl");
		FactType BookingType = runner.getKieBase().getFactType("com.redhat.rules", "Booking");
		FactType FlightType = runner.getKieBase().getFactType("com.redhat.rules","Flight");

		try {
			for (int i = 0; i < 100; i++) {
				logger.info("############Iteration : "+i+" ##################");
				Object booking = BookingType.newInstance();
				BookingType.set(booking, "customer", "Bob");

				Object flight = FlightType.newInstance();
				FlightType.set(flight, "airline", "1A");
				FlightType.set(flight, "number", 500);

				Object returnFlight = FlightType.newInstance();
				FlightType.set(returnFlight, "airline", "AF");
				FlightType.set(returnFlight, "number", 666);

				BookingType.set(booking, "flight", flight);
				BookingType.set(booking, "returnFlight", returnFlight);

				objects.add(returnFlight);
				objects.add(flight);
				objects.add(booking);

				logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
				runStartTime = System.currentTimeMillis();
				runner.setSession();
				runner.runRule(objects);
			
				logger.info("running rules elapsed time "
						+ (System.currentTimeMillis() - runStartTime) + "ms");
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testWithConcurrentMap() {
		logger.info("Test BUG Rule");
		ArrayList<Object> objects = new ArrayList<Object>();
		initStartTime = System.currentTimeMillis();
		RulesRunner runner = new RulesRunner();
		runner.initialisation("ruleConcurrentMap.drl");
		FactType BookingType = runner.getKieBase().getFactType("com.redhat.rules", "Booking");
		FactType FlightType = runner.getKieBase().getFactType("com.redhat.rules", "Flight");

		try {
			for (int i = 0; i < 100; i++) {
				logger.info("############Iteration : " + i + " ##################");
				Object booking = BookingType.newInstance();
				BookingType.set(booking, "customer", "Bob");

				Object flight = FlightType.newInstance();
				FlightType.set(flight, "airline", "1A");
				FlightType.set(flight, "number", 500);

				Object returnFlight = FlightType.newInstance();
				FlightType.set(returnFlight, "airline", "AF");
				FlightType.set(returnFlight, "number", 666);

				BookingType.set(booking, "flight", flight);
				BookingType.set(booking, "returnFlight", returnFlight);

				objects.add(returnFlight);
				objects.add(flight);
				objects.add(booking);

				logger.info("Init elapsed time case 1" + (System.currentTimeMillis() - initStartTime) + "ms");
				runStartTime = System.currentTimeMillis();
				runner.setSession();
				runner.runRule(objects);

				logger.info("running rules elapsed time " + (System.currentTimeMillis() - runStartTime) + "ms");
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
