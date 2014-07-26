package com.m12i.query.parser;

class Expression {
	private final String prop;
	private final String value;
	private final Expression left;
	private final Operator op;
	private final Expression right;

	private Expression(String prop, String value, Expression left, Operator op, Expression right) {
		this.prop = prop;
		this.value = value;
		this.left = left;
		this.op = op;
		this.right = right;
	}
	public static Expression property(String prop) {
		return new Expression(prop, null, null, null, null);
	}
	public static Expression value(String value) {
		return new Expression(null, value, null, null, null);
	}
	public static Expression comparative(Expression propExpr, Operator binary, Expression valueExpr) {
		return new Expression(null, null, propExpr, binary, valueExpr);
	}
	public static Expression logical(Operator unary, Expression right) {
		return new Expression(null, null, null, unary, right);
	}
	public static Expression logical(Expression left, Operator binary, Expression right) {
		return new Expression(null, null, left, binary, right);
	}
	
	public boolean isLogical() {
		return op == Operator.AND || op == Operator.OR || op == Operator.NOT;
	}
	public final boolean isComparative() {
		return op != null && ! isLogical();
	}
	public boolean isProperty() {
		return prop != null;
	}
	public boolean isValue() {
		return value != null;
	}
	public boolean hasLeft() {
		return left != null;
	}
	public boolean hasRight() {
		return right != null;
	}
	public String getProperty() {
		return isComparative() ? getLeft().getProperty() : prop;
	}
	public String getValue() {
		return isComparative() ? getRight().getValue() : value;
	}
	public Expression getLeft() {
		return left;
	}
	public Operator getOperator() {
		return op;
	}
	public Expression getRight() {
		return right;
	}
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		toStringHelper(sb, this, 0);
		return sb.toString();
	}
	private static final String lineSep = System.lineSeparator();
	private void toStringHelper(StringBuilder sb, Expression expr, int depth) {
		padding(sb, depth);
		if (expr.isComparative()) {
			sb.append("comparative");
			sb.append(lineSep);
		} else if (expr.isLogical()) {
			sb.append("logical");
			sb.append(lineSep);
		} else if (expr.isValue()) {
			sb.append(String.format("value(%s)", expr.value));
			sb.append(lineSep);
		} else if (expr.isProperty()) {
			sb.append(String.format("property(%s)", expr.prop));
			sb.append(lineSep);
		}
		if (expr.hasLeft()) {
			toStringHelper(sb, expr.getLeft(), depth + 1);
		}
		if (expr.isLogical() || expr.isComparative()) {
			padding(sb, depth + 1);
			sb.append(String.format("operator(%s)", expr.getOperator().toString().toLowerCase()));
			sb.append(lineSep);
		}
		if (expr.hasRight()) {
			toStringHelper(sb, expr.getRight(), depth + 1);
		}
	}
	private void padding(StringBuilder sb, int depth) {
		for (int i = 0; i < depth * 4; i ++) {
			sb.append(' ');
		}
	}
}
