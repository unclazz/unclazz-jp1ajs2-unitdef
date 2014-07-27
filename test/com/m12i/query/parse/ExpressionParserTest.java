package com.m12i.query.parse;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import com.m12i.code.parse.ParseException;
import com.m12i.query.parse.Expression;
import com.m12i.query.parse.ExpressionParser;
import com.m12i.query.parse.Operator;

public class ExpressionParserTest {

	private static final ExpressionParser parser = new ExpressionParser();
	
	private static Expression parse(String expr) {
		try {
			return parser.parse(expr);
		} catch (ParseException e) {
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void parseTest00() {
		try {
			parser.parse("");
			fail();
		} catch(Exception e) {
			// Do nothing.
		}
		try {
			parser.parse(" ");
			fail();
		} catch(Exception e) {
			// Do nothing.
		}
		try {
			parser.parse(" \r\n ");
			fail();
		} catch(Exception e) {
			// Do nothing.
		}
	}
	
	@Test
	public void parseTest01() {
		final Expression expr = parse("a == 1");
		assertTrue(expr.isComparative());
		assertTrue(expr.hasLeft());
		assertTrue(expr.hasRight());
		assertThat(expr.getProperty(), is("a"));
		assertThat(expr.getOperator(), is(Operator.EQUALS));
		assertThat(expr.getValue(), is("1"));
	}
	
	@Test
	public void parseTest02() {
		final Expression expr = parse("a != 1");
		assertTrue(expr.isComparative());
		assertThat(expr.getProperty(), is("a"));
		assertThat(expr.getOperator(), is(Operator.NOT_EQUALS));
		assertThat(expr.getValue(), is("1"));
	}
	
	@Test
	public void parseTest03() {
		final Expression expr = parse("a ^= 1");
		assertTrue(expr.isComparative());
		assertThat(expr.getProperty(), is("a"));
		assertThat(expr.getOperator(), is(Operator.STARTS_WITH));
		assertThat(expr.getValue(), is("1"));
	}
	
	@Test
	public void parseTest04() {
		final Expression expr = parse("a $= 1");
		assertTrue(expr.isComparative());
		assertThat(expr.getProperty(), is("a"));
		assertThat(expr.getOperator(), is(Operator.ENDS_WITH));
		assertThat(expr.getValue(), is("1"));
	}

	@Test
	public void parseTest05() {
		final Expression expr = parse("a *= 1");
		assertTrue(expr.isComparative());
		assertThat(expr.getProperty(), is("a"));
		assertThat(expr.getOperator(), is(Operator.CONTAINS));
		assertThat(expr.getValue(), is("1"));
	}

	@Test
	public void parseTest06() {
		final Expression expr = parse("a is null");
		assertTrue(expr.isComparative());
		assertThat(expr.getProperty(), is("a"));
		assertThat(expr.getOperator(), is(Operator.IS_NULL));
		assertNull(expr.getValue());
	}

	@Test
	public void parseTest07() {
		final Expression expr = parse("a is not null");
		assertTrue(expr.isComparative());
		assertThat(expr.getProperty(), is("a"));
		assertThat(expr.getOperator(), is(Operator.IS_NOT_NULL));
		assertNull(expr.getValue());
	}

	@Test
	public void parseTest08() {
		final Expression expr0 = parse("a_0 == hello_world");
		assertThat(expr0.getProperty(), is("a_0"));
		assertThat(expr0.getValue(), is("hello_world"));
		final Expression expr1 = parse("a-1 == hello-world");
		assertThat(expr1.getProperty(), is("a-1"));
		assertThat(expr1.getValue(), is("hello-world"));
		final Expression expr2 = parse("\"a 2\" == \"hello world\"");
		assertThat(expr2.getProperty(), is("a 2"));
		assertThat(expr2.getValue(), is("hello world"));
		final Expression expr3 = parse("'a 3' == 'hello world'");
		assertThat(expr3.getProperty(), is("a 3"));
		assertThat(expr3.getValue(), is("hello world"));
		final Expression expr4 = parse("'a\\' 4' == 'hello world\\''");
		assertThat(expr4.getProperty(), is("a' 4"));
		assertThat(expr4.getValue(), is("hello world'"));
	}
	
	@Test
	public void parseTest10() {
		final Expression expr = parse("!(a == 1)");
		assertTrue(expr.isLogical());
		assertFalse(expr.hasLeft());
		assertTrue(expr.hasRight());
		assertThat(expr.getOperator(), is(Operator.NOT));
		assertTrue(expr.getRight().isComparative());
		assertThat(expr.getRight().getProperty(), is("a"));
		assertThat(expr.getRight().getOperator(), is(Operator.EQUALS));
		assertThat(expr.getRight().getValue(), is("1"));
	}
	
	@Test
	public void parseTest11() {
		final Expression expr = parse("a == 1 and b != 2");
		assertTrue(expr.isLogical());
		assertTrue(expr.hasLeft());
		assertTrue(expr.hasRight());
		assertThat(expr.getOperator(), is(Operator.AND));
		assertTrue(expr.getRight().isComparative());
		assertThat(expr.getRight().getProperty(), is("b"));
		assertThat(expr.getRight().getOperator(), is(Operator.NOT_EQUALS));
		assertThat(expr.getRight().getValue(), is("2"));
	}
	
	@Test
	public void parseTest12() {
		final Expression expr = parse("a == 1 && (b != 2)");
		assertTrue(expr.isLogical());
		assertTrue(expr.hasLeft());
		assertTrue(expr.hasRight());
		assertThat(expr.getOperator(), is(Operator.AND));
		assertTrue(expr.getRight().isComparative());
		assertThat(expr.getRight().getProperty(), is("b"));
		assertThat(expr.getRight().getOperator(), is(Operator.NOT_EQUALS));
		assertThat(expr.getRight().getValue(), is("2"));
	}
	
	@Test
	public void parseTest13() {
		final Expression expr = parse("a == 1 or b != 2");
		assertTrue(expr.isLogical());
		assertTrue(expr.hasLeft());
		assertTrue(expr.hasRight());
		assertThat(expr.getOperator(), is(Operator.OR));
		assertTrue(expr.getRight().isComparative());
		assertThat(expr.getRight().getProperty(), is("b"));
		assertThat(expr.getRight().getOperator(), is(Operator.NOT_EQUALS));
		assertThat(expr.getRight().getValue(), is("2"));
	}
	
	@Test
	public void parseTest14() {
		final Expression expr = parse("(a == 1) || b != 2");
		assertTrue(expr.isLogical());
		assertTrue(expr.hasLeft());
		assertTrue(expr.hasRight());
		assertThat(expr.getOperator(), is(Operator.OR));
		assertTrue(expr.getRight().isComparative());
		assertThat(expr.getRight().getProperty(), is("b"));
		assertThat(expr.getRight().getOperator(), is(Operator.NOT_EQUALS));
		assertThat(expr.getRight().getValue(), is("2"));
	}
	
	@Test
	public void parseTest15() {
		final Expression expr = parse("a == 1 and (b != 2 or c != 3)");
		assertTrue(expr.isLogical());
		assertTrue(expr.getRight().isLogical());
		assertThat(expr.getRight().getOperator(), is(Operator.OR));
	}
	
	@Test
	public void parseTest16() {
		final Expression expr = parse("(a == 1 and b != 2) or c != 3");
		assertTrue(expr.isLogical());
		assertTrue(expr.getLeft().isLogical());
		assertThat(expr.getLeft().getOperator(), is(Operator.AND));
	}
	
	@Test
	public void parseTest17() {
		final Expression expr0 = parse("a == 1 and b != 2 or c != 3");
		
		// logical
		//    logical
		//        comparative
		//            property(a)
		//            operator(equals)
		//            value(1)
		//        operator(and)
		//        comparative
		//            property(b)
		//            operator(not_equals)
		//            value(2)
		//    operator(or)
		//    comparative
		//        property(c)
		//        operator(not_equals)
		//        value(3)

		assertTrue(expr0.isLogical());
		assertTrue(expr0.getLeft().isLogical());
		assertTrue(expr0.getLeft().getLeft().isComparative());
		assertTrue(expr0.getLeft().getRight().isComparative());
		assertTrue(expr0.getRight().isComparative());

		final Expression expr1 = parse("(a == 1 and b != 2) or c != 3");
		assertTrue(expr0.toString().equals(expr1.toString()));
	}
}
