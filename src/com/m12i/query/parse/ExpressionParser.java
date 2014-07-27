package com.m12i.query.parse;

import com.m12i.code.parse.ParseException;
import com.m12i.code.parse.ParserTemplate;

class ExpressionParser extends ParserTemplate<Expression>{

	@Override
	protected Expression parseMain() throws ParseException {
		skipSpace();
		if (hasReachedEof()) {
			throw new ParseException("Empty string.", code());
		}
		return parseExpression(true);
	}
	
	private Expression parseExpression(boolean recursive) throws ParseException {
		skipSpace();
		if (currentIs('!')) {
			next();
			skipSpace();
			return Expression.logical(Operator.NOT, parseExpression(true));
		} else if(currentIs('(')) {
			next();
			final Expression e = parseExpression(true);
			currentMustBe(')');
			next();
			if (!recursive) {
				return e;
			} else {
				return parseLogical(e);
			}
		} else {
			final String prop = currentIsAnyOf('"', '\'') ? parseQuotedString() : parseNonQuotedString();
			skipSpace();
			final Operator op = parseComparativeOperator();
			skipSpace();
			final String value = (op == Operator.IS_NOT_NULL || op == Operator.IS_NULL) ? null : parseValue();
			final Expression expr0 = Expression.comparative(Expression.property(prop), op, Expression.value(value));
			if (!recursive) {
				return expr0;
			} else {
				return parseLogical(expr0);
			}
			
		}
	}
	
	private Expression parseLogical(Expression left) throws ParseException {
		skipSpace();
		if (currentIs('a')) {
			nextMustBe('n');
			nextMustBe('d');
			next();
			skipSpace();
			return Expression.logical(left, Operator.AND, parseExpression(false));
		} else if (currentIs('&')) {
			nextMustBe('&');
			next();
			skipSpace();
			return Expression.logical(left, Operator.AND, parseExpression(false));
		} else if (currentIs('o')) {
			nextMustBe('r');
			next();
			skipSpace();
			return Expression.logical(left, Operator.OR, parseExpression(false));
		} else if (currentIs('|')) {
			nextMustBe('|');
			next();
			skipSpace();
			return Expression.logical(left, Operator.OR, parseExpression(false));
		} else {
			return left;
		}
	}
	
	private String parseNonQuotedString() throws ParseException {
		final StringBuilder sb = new StringBuilder();
		while (!hasReachedEof()) {
			if (currentIsBetween('0', '9') || 
					currentIsBetween('A', 'Z') ||
					currentIsBetween('a', 'z') ||
					currentIsAnyOf('_', '-')) {
				sb.append(current());
				next();
			} else {
				break;
			}
		}
		return sb.toString();
	}
	
	private String parseValue() throws ParseException {
		if (currentIsAnyOf('"', '\'')) {
			return parseQuotedString();
		} else {
			final String value = parseNonQuotedString();
			return value.trim();
		}
	}
	
	private Operator parseComparativeOperator() throws ParseException {
		if (currentIs('=')) {
			nextMustBe('=');
			next();
			return Operator.EQUALS;
		} else if (currentIs('!')) {
			nextMustBe('=');
			next();
			return Operator.NOT_EQUALS;
		} else if (currentIs('^')) {
			nextMustBe('=');
			next();
			return Operator.STARTS_WITH;
		} else if (currentIs('$')) {
			nextMustBe('=');
			next();
			return Operator.ENDS_WITH;
		} else if (currentIs('*')) {
			nextMustBe('=');
			next();
			return Operator.CONTAINS;
		} else if (currentIs('i')) {
			next();
			currentMustBe('s');
			next();
			skipSpace();
			currentMustBe('n');
			next();
			if (currentIs('o')) {
				next();
				currentMustBe('t');
				next();
				skipSpace();
				currentMustBe('n');
				next();
				currentMustBe('u');
				next();
				currentMustBe('l');
				next();
				currentMustBe('l');
				next();
				return Operator.IS_NOT_NULL;
			} else if (currentIs('u')) {
				next();
				currentMustBe('l');
				next();
				currentMustBe('l');
				next();
				return Operator.IS_NULL;
			}
			
		}
		throw new ParseException("Invalid syntax found on comparative expression.", code());
	}
	
	@Override
	public char escapePrefixInSingleQuotes() {
		return '\\';
	}

	@Override
	public char escapePrefixInDoubleQuotes() {
		return '\\';
	}

	@Override
	public String lineCommentStart() {
		return null;
	}

	@Override
	public String blockCommentStart() {
		return null;
	}

	@Override
	public String blockCommentEnd() {
		return null;
	}

	@Override
	public boolean skipCommentWithSpace() {
		return false;
	}

}
