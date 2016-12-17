package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

public class Lhs {
	private List<Line> lines = new ArrayList<Line>();

	/**
	 * @return the lines
	 */
	public List<Line> getLines() {
		return lines;
	}

	/**
	 * @param lines
	 *            the lines to set
	 */
	public void setLines(List<Line> lines) {
		this.lines = lines;
	}

	public void addLine(Line line) {
		this.lines.add(line);
	}

	public String buildLhs() {
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (Line line : lines) {
			if (line.getPrefix() != null && i != 0) {
				builder.append(line.getPrefix() + " ");
			}
			if (line.getType() == "eval") {
				builder.append("eval(");
			}
			if (line.getObjectType() != null) {
				builder.append("  "+line.getObjectType() + "(");
			}
			for (Constraint constraint : line.getConstraints()) {
				builder.append(constraint.getPrefix());
				builder.append(constraint.getLeft());
				builder.append(constraint.getOperand());
				builder.append(constraint.getRight());
			}
			if (line.getType() == "eval") {
				builder.append(")");
			}
			if (line.getObjectType() != null) {
				builder.append(")");
				if (i != lines.size())
					builder.append("\n");
			}
			i++;
		}
		return builder.toString();
	}
}
