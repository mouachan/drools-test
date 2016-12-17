package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

public class Rhs {
	List<Consequence> consequences = new ArrayList<Consequence>();

	/**
	 * @return the consequences
	 */
	public List<Consequence> getConsequences() {
		return consequences;
	}

	/**
	 * @param consequences
	 *            the consequences to set
	 */
	public void setConsequences(List<Consequence> consequences) {
		this.consequences = consequences;
	}

	public void addConsequence(Consequence consequence) {
		this.consequences.add(consequence);
	}

	public String buildRhs() {
		StringBuilder builder = new StringBuilder();
		int index = 0;
		for (Consequence consequence : this.consequences) {
			builder.append("  "+consequence.getText() + ";");
			index++;
			if (index != consequences.size())
				builder.append("\n");
		}
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Rhs [consequences=");
		builder.append(consequences);
		builder.append("]");
		return builder.toString();
	}

}
