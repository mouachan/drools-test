package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="lhs")
@XmlAccessorType(XmlAccessType.FIELD)
public class Lhs {
	@XmlElement(name="expressions")
	private List<Expression> expressions = new ArrayList<Expression>();

	/**
	 * @return the lines
	 */
	public List<Expression> getExpressions() {
		return expressions;
	}

	/**
	 * @param lines
	 *            the lines to set
	 */
	public void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
	}

	public void addExpression(Expression expression) {
		this.expressions.add(expression);
	}

	public String buildLhs() {
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (Expression expression : expressions) {
			if (expression.getPrefix() != null && i != 0) {
				builder.append(expression.getPrefix() + " ");
			}
			if (expression.getObjectType() != null) {
				if (expression.getBindingType() != null)
					builder.append(" " + expression.getBindingType() + ":" + expression.getObjectType() + "(");
				else
					builder.append(" " + expression.getObjectType() + "(");
			}
			for (Constraint constraint : expression.getConstraints()) {
				if (constraint.getType() == "eval") {
					builder.append("eval(" + constraint.getContent() + ")");
					builder.append("\n");
				} else {
					builder.append(constraint.getPrefix());
					if (constraint.getBindingField() != null)
						builder.append(constraint.getVariable() + ":" + constraint.getBindingField());
					else
						builder.append(constraint.getLeft());
					builder.append(" "+constraint.getOperand());
					builder.append(" "+constraint.getRight());
				}
			}

			if (expression.getObjectType() != null) {
				builder.append(")");
				if (i != expressions.size())
					builder.append("\n");
			}
			i++;
		}
		return builder.toString();
	}
}
