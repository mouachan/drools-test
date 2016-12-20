package com.amadeus.drools.rule.parser;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
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
import org.drools.compiler.lang.descr.FunctionDescr;
import org.drools.compiler.lang.descr.ImportDescr;
import org.drools.compiler.lang.descr.OrDescr;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.compiler.lang.descr.PatternDescr;
import org.drools.compiler.lang.descr.RelationalExprDescr;
import org.drools.compiler.lang.descr.RuleDescr;
import org.drools.compiler.lang.descr.TypeDeclarationDescr;
import org.kie.api.KieServices;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.internal.builder.conf.LanguageLevelOption;

import com.amadeus.drools.rule.model.Consequence;
import com.amadeus.drools.rule.model.Constraint;
import com.amadeus.drools.rule.model.Expression;
import com.amadeus.drools.rule.model.Function;
import com.amadeus.drools.rule.model.Parameter;
import com.amadeus.drools.rule.model.Rule;
import com.amadeus.drools.rule.model.RuleAttribute;
import com.amadeus.drools.rule.model.RuleSet;

/**
 * @author mouachan
 *
 */
public class RuleParser {
	private static Logger logger = Logger.getLogger(RuleParser.class);
	private DrlExprParser drlexpparser = new DrlExprParser(LanguageLevelOption.DRL6);
	protected String filename;
	protected PackageDescr packageDescr = null;
	protected Rule rule = new Rule();
	protected RuleSet ruleset = new RuleSet();

	public void setUp(String filepath) {
		KieServices kieServices = KieServices.Factory.get();
		KieResources kieResources = kieServices.getResources();
		Resource resource = kieResources.newFileSystemResource(filepath);

		DrlParser parser = new DrlParser();
		try {
			packageDescr = parser.parse(resource);
			ruleset.setNameSpace(packageDescr.getNamespace());
		} catch (DroolsParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * parse package descriptor and get functions
	 */
	private void parseFunctions() {
		for (FunctionDescr functionDescr : packageDescr.getFunctions()) {
			Function function = new Function();
			function.setName(functionDescr.getName());
			function.setReturnType(functionDescr.getReturnType());
			int index = 0;
			for (String paramName : functionDescr.getParameterNames()) {
				Parameter param = new Parameter();
				param.setName(paramName);
				param.setType(functionDescr.getParameterTypes().get(index));
				index++;
				function.addParameter(param);
			}
			function.setBody(functionDescr.getBody());
			ruleset.addFunction(function);
		}
	}

	/**
	 * Extract declare from package descriptor TODO implement decalre in the
	 * model
	 */
	private void parseDeclare() {
		for (TypeDeclarationDescr declareDescr : packageDescr.getTypeDeclarations()) {
			logger.debug(declareDescr.getFullTypeName());
		}
	}

	/**
	 * Extract import from package descriptor
	 */
	private void parseImport() {
		for (ImportDescr importDescr : packageDescr.getImports()) {
			ruleset.addImport(importDescr.getTarget());
		}
	}

	
	/**
	 * build eval expression from eval descriptor
	 * @param edesc
	 * @param prefix
	 * @param rule
	 */
	private void buildEvalExpression(EvalDescr edesc, String prefix, Rule rule) {
		Expression exp = new Expression();
		exp.setPrefix(prefix);
		exp.setType("eval");
		Constraint cons = new Constraint();
		cons.setContent(edesc.getContent().toString());
		cons.setType("eval");
		cons.setPrefix(prefix);
		exp.addConstraint(cons);
		rule.getLhs().addExpression(exp);
	}
	/**
	 * build pattern expression from patern descriptor
	 * @param pdesc
	 * @param prefix
	 * @param rule
	 */
	private void buildPatternExpression(PatternDescr pdesc, String prefix, Rule rule) {
		Expression expression = new Expression();
		expression.setBindingType(pdesc.getIdentifier());
		expression.setObjectType(pdesc.getObjectType());
		expression.setPrefix(prefix);
		for (BaseDescr bd : pdesc.getDescrs()) {
			buildCondition(bd, ",", expression);
		}
		rule.getLhs().addExpression(expression);
	}
	/**
	 * build or expression from or descriptor
	 * @param ordesc
	 * @param prefix
	 * @param rule
	 */
	private void buildOrExpression(OrDescr ordesc, String prefix, Rule rule) {
		int index = 0;
		for (BaseDescr bd : ordesc.getDescrs()) {
			if (bd instanceof EvalDescr)
				buildEvalExpression((EvalDescr) bd, prefix, rule);
			else if (bd instanceof AndDescr) {
				buildAndExpression((AndDescr) bd, prefix, rule);
			} else {
				if (index != 0)
					prefix = "or";
				else
					index++;
				buildPatternExpression((PatternDescr) bd, prefix, rule);
			}
		}
	}
	/**
	 * Build and expression from and descriptor
	 * @param adesc
	 * @param prefix
	 * @param rule
	 */
	private void buildAndExpression(AndDescr adesc, String prefix, Rule rule) {
		int index = 0;
		for (BaseDescr bd : adesc.getDescrs()) {
			if (bd instanceof EvalDescr)
				buildEvalExpression((EvalDescr) bd, prefix, rule);
			else {
				if (index != 0)
					prefix = "and";
				else
					index++;
				buildPatternExpression((PatternDescr) bd, prefix, rule);
			}
		}
	}

	/**
	 * build expression 
	 * @param desc
	 * @param prefix
	 * @param rule
	 */
	private void buildExpression(BaseDescr desc, String prefix, Rule rule) {
		if (desc instanceof EvalDescr)
			buildEvalExpression((EvalDescr) desc, prefix, rule);
		else if (desc instanceof PatternDescr)
			buildPatternExpression((PatternDescr) desc, prefix, rule);
		else if (desc instanceof OrDescr)
			buildOrExpression((OrDescr) desc, prefix, rule);
	}

	/**
	 * build and add condition to expression with a prefix
	 * 
	 * @param bd
	 * @param prefix
	 * @param expression
	 */
	private void buildCondition(BaseDescr bd, String prefix, Expression expression) {
		Constraint constraint = new Constraint();
		ExprConstraintDescr ecdescr = (ExprConstraintDescr) bd;
		ConstraintConnectiveDescr result = drlexpparser.parse(ecdescr.getExpression());
		RelationalExprDescr expr = (RelationalExprDescr) result.getDescrs().get(0);
		if (expr.getLeft() instanceof BindingDescr) {
			BindingDescr bdleft = (BindingDescr) expr.getLeft();
			constraint.setVariable(bdleft.getVariable());
			constraint.setBindingField(bdleft.getBindingField());
		}
		constraint.setLeft(expr.getLeft().toString());
		if (expression.getConstraints().size() == 0) {
			constraint.setIndex(0);
			constraint.setPrefix("");
		} else {
			constraint.setIndex((expression.getConstraints().size()) + 1);
			constraint.setPrefix(prefix);
		}
		constraint.setObjectType(expression.getObjectType());
		constraint.setOperand(expr.getOperator());
		constraint.setRight(expr.getRight().toString());
		expression.addConstraint(constraint);
	}

	/**
	 * build consequences
	 * 
	 * @param ruledescr
	 *            rule descriptor
	 * @param rule
	 */
	private void buildConsequesnce(RuleDescr ruledescr, Rule rule) {
		String[] cons = ruledescr.getConsequence().toString().trim().split(";");
		for (int i = 0; i < cons.length; i++) {
			Consequence consequence = new Consequence();
			consequence.setText(cons[i].trim());
			rule.getRhs().addConsequence(consequence);
		}
		// TODO implement named consequence
		for (Map.Entry<String, Object> namedConsequence : ruledescr.getNamedConsequences().entrySet())
			logger.debug(namedConsequence.getKey() + " : " + namedConsequence.getValue());
	}

	/**
	 * Parse rule
	 */
	private void parseRule() {
		String prefix = null;
		for (RuleDescr ruleDescr : packageDescr.getRules()) {
			Rule rule = new Rule();
			if (ruleDescr.isRule()) {
				rule.setName(ruleDescr.getName());
				// get all rule attributes
				for (String key : ruleDescr.getAttributes().keySet()) {
					AttributeDescr attribute = (AttributeDescr) ruleDescr.getAttributes().get(key);
					RuleAttribute attr = new RuleAttribute(attribute.getName(), attribute.getValue());
					rule.addAttribute(attr);
					logger.debug(attribute.getName() + " " + attribute.getValue());
				}
				for (BaseDescr desc : ruleDescr.getLhs().getDescrs()) {
					if (desc instanceof EvalDescr) {
						prefix = null;
					} else if (desc instanceof PatternDescr) {
						prefix = "";
					} else if (desc instanceof OrDescr) {
						prefix = "or";
					}
					buildExpression(desc, prefix, rule);
				}
				buildConsequesnce(ruleDescr, rule);
				ruleset.addRule(rule);
			}
		}
	}

	/**
	 * Parse Rule set descriptor, extract imports, functions, declare and rule
	 * 
	 * @return
	 */
	public RuleSet parseRuleSet() {
		parseImport();
		parseFunctions();
		parseDeclare();
		parseRule();
		return ruleset;
	}

	/**
	 * Convert Java object to JSON
	 * @param obj
	 * @return
	 */
	public String convertObjectToJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writeValueAsString(obj);
			return jsonInString;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
