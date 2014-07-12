package usertools.jp1ajs2.unitdef.core;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static usertools.jp1ajs2.unitdef.core.TestUtils.*;

import org.junit.Test;

import usertools.jp1ajs2.unitdef.core.Param;
import usertools.jp1ajs2.unitdef.core.Parser;
import usertools.jp1ajs2.unitdef.core.Tuple;
import usertools.jp1ajs2.unitdef.core.Unit;
import usertools.jp1ajs2.unitdef.core.UnitType;

import com.m12i.code.parse.Parsable;
import com.m12i.code.parse.ParseException;

public class ParserTest {

	private static final String simpleUnitDefString1 = "unit=XXXX0000,AAAAA,BBBBB,CCCCC;"
			+ "{"
			+ "ty=g;" 
			+ "cm=\"これはコメントです。\";" 
			+ "}";

	private static final String nestedUnitDefString1 = "unit=XXXX0000,AAAAA,BBBBB,CCCCC;\r\n"
			+ "{\r\n"
			+ "    ty=g;\r\n" 
			+ "    cm=\"これはコメントです。\";\r\n"
			+ "    unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
			+ "    {\r\n"
			+ "        ty=g;\r\n" 
			+ "        cm=\"これはコメントです。\";\r\n" 
			+ "    }\r\n"
			+ "    unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
			+ "    {\r\n"
			+ "        ty=g;\r\n" 
			+ "        cm=\"これはコメントです。\";\r\n" 
			+ "    }\r\n"
			+ "}\r\n";

	private static final String mockUnitDefParamString1 = "xx=ABCDEF,ABC123,HAS SPACE,\"QUOTED STRING\",123456,2013/01/01,00:00,();";

	@Test
	public void parseAttrはユニット定義属性を読み取って返す() throws ParseException {
		final Parsable code = createCode(simpleUnitDefString1);
		final Parser parser = with(code);
		parser.currentMustBe('u');
		parser.nextMustBe('n');
		parser.nextMustBe('i');
		parser.nextMustBe('t');
		parser.nextMustBe('=');
		parser.nextMustBe('X');
		assertThat("parseAttrはユニット定義属性を読み取って返す", parser.parseAttr()
				, is("XXXX0000"));
		assertThat("parseAttrを実行後の現在文字は属性区切り文字", code.current(), is(','));
		code.next();
		assertThat(parser.parseAttr(), is("AAAAA"));
		assertThat(code.current(), is(','));
		code.next();
		assertThat(parser.parseAttr(), is("BBBBB"));
		assertThat(code.current(), is(','));
		code.next();
		assertThat(parser.parseAttr(), is("CCCCC"));
		assertThat(code.current(), is(';'));
	}

	@Test
	public void parseParamはユニット定義パラメータを読み取って返す() throws ParseException {
		final Parsable code = createCode(mockUnitDefParamString1);
		final Param p = with(code).parseParam();
		assertThat(p.getName(), is("xx"));
		assertThat(p.getValues().size(), is(8));
	}

	@Test
	public void parseParamValueはユニット定義パラメータ値を読み取って返す() throws ParseException {
		final Parsable code = createCode(mockUnitDefParamString1);
		final Parser parser = with(code);
		parser.currentMustBe('x');
		parser.nextMustBe('x');
		parser.nextMustBe('=');
		parser.next();
		assertThat(parser.parseParamValue().getStringValue(),
				is("ABCDEF"));
		parser.currentMustBe(',');
		code.next();
		assertThat(parser.parseParamValue().getStringValue(),
				is("ABC123"));
		parser.currentMustBe(',');
		code.next();
		assertThat(parser.parseParamValue().getStringValue(),
				is("HAS SPACE"));
		parser.currentMustBe(',');
		code.next();
		assertThat(parser.parseParamValue().getStringValue(),
				is("QUOTED STRING"));
		parser.currentMustBe(',');
		code.next();
		assertThat(parser.parseParamValue().getStringValue(),
				is("123456"));
		parser.currentMustBe(',');
		code.next();
		assertThat(parser.parseParamValue().getStringValue(),
				is("2013/01/01"));
		parser.currentMustBe(',');
		code.next();
		assertThat(parser.parseParamValue().getStringValue(),
				is("00:00"));
		parser.currentMustBe(',');
		code.next();
		assertThat(parser.parseParamValue().getStringValue(), is("()"));
		parser.currentMustBe(';');
	}

	@Test
	public void parseTupleはタプルもどきを読み取って返す() throws ParseException {
		final Parsable code = createCode("(f=AAAAA,B=BBBBB,CCCCC) (AAAAA,X=BBBBB,Y=CCCCC) ()");
		final Parser parser = with(code);
		final Tuple t0 = parser.parseTuple();
		assertThat(t0.size(), is(3));
		assertThat(t0.get(0).get(), is("AAAAA"));
		assertThat(t0.get(1).get(), is("BBBBB"));
		assertThat(t0.get(2).get(), is("CCCCC"));
		assertThat(t0.get("f").get(), is("AAAAA"));
		assertThat(t0.get("B").get(), is("BBBBB"));
		parser.currentMustBe(' ');
		code.next();
		final Tuple t1 = parser.parseTuple();
		assertThat(t1.get(0).get(), is("AAAAA"));
		assertThat(t1.get(1).get(), is("BBBBB"));
		assertThat(t1.get(2).get(), is("CCCCC"));
		assertThat(t1.get("X").get(), is("BBBBB"));
		assertThat(t1.get("Y").get(), is("CCCCC"));
		parser.currentMustBe(' ');
		code.next();
		final Tuple t2 = parser.parseTuple();
		assertThat(t2.size(), is(0));
		assertTrue(t2.get(1).isNone());
		assertTrue(t2.get("X").isNone());
	}

	@Test
	public void parseUnitはユニット定義を再帰的に読み取って返す() throws ParseException {
		final Parsable code1 = createCode(simpleUnitDefString1);
		final Parser parser1 = with(code1);
		final Unit unit1 = parser1.parseUnit(null);
		assertThat(unit1.getName(), is("XXXX0000"));
		assertThat(unit1.getPermissionMode().get(), is("AAAAA"));
		assertThat(unit1.getOwnerName().get(), is("BBBBB"));
		assertThat(unit1.getResourceGroupName().get(), is("CCCCC"));
		assertThat(unit1.getParams().size(), is(2));
		assertThat(unit1.getType(), is(UnitType.GROUP));
		assertThat(unit1.getComment().get(), is("これはコメントです。"));
		assertThat(unit1.getSubUnits().size(), is(0));

		final Parsable code2 = createCode(nestedUnitDefString1);
		final Parser parser2 = with(code2);
		final Unit unit2 = parser2.parseUnit(null);
		assertThat(unit2.getName(), is("XXXX0000"));
		assertThat(unit2.getSubUnits().size(), is(2));
		final Unit unit2_1 = unit2.getSubUnits().get(0);
		final Unit unit2_2 = unit2.getSubUnits().get(1);
		assertThat(unit2_1.getName(), is("XXXX0001"));
		assertThat(unit2_2.getName(), is("XXXX0002"));
	}

}
