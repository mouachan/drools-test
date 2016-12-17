package com.amadeus.drools.rule.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.drools.compiler.compiler.DrlExprParser;
import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.lang.descr.AndDescr;

import org.drools.compiler.lang.descr.AttributeDescr;
import org.drools.compiler.lang.descr.BaseDescr;
import org.drools.compiler.lang.descr.BindingDescr;
import org.drools.compiler.lang.descr.ConstraintConnectiveDescr;
import org.drools.compiler.lang.descr.EvalDescr;
import org.drools.compiler.lang.descr.ExprConstraintDescr;
import org.drools.compiler.lang.descr.OrDescr;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.compiler.lang.descr.PatternDescr;
import org.drools.compiler.lang.descr.RelationalExprDescr;
import org.drools.compiler.lang.descr.RuleDescr;
import org.jbpm.services.task.commands.GetAllCommentsCommand;
import org.kie.api.KieServices;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.internal.builder.conf.LanguageLevelOption;

import com.amadeus.drools.rule.model.Attribute;
import com.amadeus.drools.rule.model.Consequence;
import com.amadeus.drools.rule.model.Constraint;
import com.amadeus.drools.rule.model.Line;
import com.amadeus.drools.rule.model.Rule;

public class RuleParser {
	private static Logger logger = Logger.getLogger(RuleParser.class);
	private DrlParser dp = new DrlParser(LanguageLevelOption.DRL6);
	private DrlExprParser drlexpparser = new DrlExprParser(LanguageLevelOption.DRL6);

	protected String filename;
	protected PackageDescr packageDescr = null;
	protected Rule rule = new Rule();
	private static final java.util.regex.Pattern evalRegexp = java.util.regex.Pattern.compile("^eval\\s*\\(",
			java.util.regex.Pattern.MULTILINE);

	private static final java.util.regex.Pattern identifierRegexp = java.util.regex.Pattern
			.compile("([\\p{L}_$][\\p{L}\\p{N}_$]*)");

	private static final java.util.regex.Pattern getterRegexp = java.util.regex.Pattern
			.compile("get([\\p{L}_][\\p{L}\\p{N}_]*)\\(\\s*\\)");

	public void setUp(String filepath) {
		KieServices kieServices = KieServices.Factory.get();
		KieResources kieResources = kieServices.getResources();
		Resource resource = kieResources.newFileSystemResource(filepath);

		DrlParser parser = new DrlParser();
		try {
			packageDescr = parser.parse(resource);
		} catch (DroolsParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Build expression constraint from
	 * 
	 * @param slc
	 * @return
	 */
	private void addCondition(BaseDescr slc, String prefix, Line line) {
		Constraint constraint = new Constraint();
		ExprConstraintDescr ecdescr = (ExprConstraintDescr) slc;
		logger.debug(" Constraint = " + ecdescr);
		ConstraintConnectiveDescr result = drlexpparser.parse(ecdescr.getExpression());

		RelationalExprDescr expr = (RelationalExprDescr) result.getDescrs().get(0);
		/*
		 * if (expr.getLeft() instanceof BindingDescr) { BindingDescr binddescr
		 * = (BindingDescr) expr.getLeft(); logger.info("Binding field " +
		 * binddescr.getBindingField()); logger.info("Variable " +
		 * binddescr.getVariable()); } else {
		 */
		if (line.getConstraints().size() == 0) {
			constraint.setIndex(0);
			constraint.setPrefix("");
		} else {
			constraint.setIndex((line.getConstraints().size()) + 1);
			constraint.setPrefix(prefix);
		}

		constraint.setObjectType(line.getObjectType());
		constraint.setLeft(expr.getLeft().toString());

		logger.debug("Left Expression : " + expr.getLeft());

		constraint.setOperand(expr.getOperator());
		logger.debug("Operator : " + expr.getOperator());

		constraint.setRight(expr.getRight().toString());
		logger.debug("Right expression : " + expr.getRight());
		// }
		line.addConstraint(constraint);
	}

	private void buildConsequesnce(RuleDescr rule, Rule rulem) {
		logger.debug(rule.getConsequence());
		logger.debug(rule.getConsequenceLine());
		logger.debug(rule.getConsequenceOffset());
		logger.debug(rule.getConsequencePattern());
		String[] cons = rule.getConsequence().toString().trim().split(";");
		for (int i = 0; i < cons.length; i++) {
			Consequence consequence = new Consequence();
			consequence.setText(cons[i].trim());
			rulem.getRhs().addConsequence(consequence);
		}
		//TODO implement named consequence
		for (Map.Entry<String, Object> namedConsequence : rule.getNamedConsequences().entrySet())
			logger.debug(namedConsequence.getKey() + " : " + namedConsequence.getValue());
	}

	public List<Rule> parseDrlRule() {
		List<RuleDescr> rules = packageDescr.getRules();
		List<Rule> rulesm = new ArrayList<Rule>();
		Line line;
		for (RuleDescr rule : rules) {
			Rule rulem = new Rule();
			if (rule.isRule()) {
				rulem.setName(rule.getName());
				// get all rule attributes
				for (String key : rule.getAttributes().keySet()) {
					AttributeDescr attribute = (AttributeDescr) rule.getAttributes().get(key);
					Attribute attr = new Attribute(attribute.getName(), attribute.getValue());
					rulem.addAttribute(attr);
					logger.debug(attribute.getName() + " " + attribute.getValue());
				}
				// get all conditions

				if (rule.getLhs() instanceof AndDescr) {
					for (BaseDescr desc : rule.getLhs().getDescrs()) {	 
						// Eval conditions
						if (desc instanceof EvalDescr) {
							EvalDescr evaldescr = (EvalDescr) desc;
							// TODO implement eval conditon
							logger.debug(evaldescr.getContent().toString());
						} // AND conditions
						else if (desc instanceof PatternDescr) {
							PatternDescr pdescr = (PatternDescr) desc;
							line = new Line();
							line.setPrefix("");
							line.setObjectType(pdescr.getObjectType());
							for (BaseDescr slc : pdescr.getSlottedConstraints()) {
								addCondition(slc, ",", line);
								logger.debug(line.toString());
								// rulem.getConstraints().add(getCondition(slc));
							}
							rulem.getLhs().addLine(line);
						}
						// Or conditions
						else if (desc instanceof OrDescr) {
							OrDescr ordescr = (OrDescr) desc;
							logger.info(desc.getLine());
							int index = 0;
							for (BaseDescr d : ordescr.getDescrs()) {
								PatternDescr pdescr = (PatternDescr) d;
								line = new Line();
								if(index!=0)
									line.setPrefix("or");
								else index++;
								line.setObjectType(pdescr.getObjectType());
								for (BaseDescr slc : pdescr.getSlottedConstraints()) {
									addCondition(slc, ",", line);
								}
								rulem.getLhs().addLine(line);

							}
						}
					}
				}
				// get all consequences
				 buildConsequesnce(rule, rulem);
			}
			rulesm.add(rulem);
		}
		return rulesm;
	}

}
