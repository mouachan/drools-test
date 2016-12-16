package com.amadeus.drools.rule.model;

public class Constraint {
	private String objectType;
	private String left;
	private String operand;
	private String right;
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getOperand() {
		return operand;
	}
	public void setOperand(String operand) {
		this.operand = operand;
	}
	public String getRight() {
		return right;
	}
	public void setRight(String right) {
		this.right = right;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Constraint [objectType=");
		builder.append(objectType);
		builder.append(", left=");
		builder.append(left);
		builder.append(", operand=");
		builder.append(operand);
		builder.append(", right=");
		builder.append(right);
		builder.append("]");
		return builder.toString();
	}

	

}
