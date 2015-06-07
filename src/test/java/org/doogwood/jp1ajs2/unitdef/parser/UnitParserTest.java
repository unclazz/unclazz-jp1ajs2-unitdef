package org.doogwood.jp1ajs2.unitdef.parser;

import static org.junit.Assert.*;
import static org.doogwood.jp1ajs2.unitdef.TestUtils.*;
import static org.hamcrest.CoreMatchers.*;

import org.doogwood.jp1ajs2.unitdef.Param;
import org.doogwood.jp1ajs2.unitdef.Tuple;
import org.doogwood.jp1ajs2.unitdef.Unit;
import org.doogwood.jp1ajs2.unitdef.UnitType;
import org.doogwood.jp1ajs2.unitdef.parser.UnitParser;
import org.doogwood.parse.Input;
import org.doogwood.parse.InputExeption;
import org.doogwood.parse.ParseException;
import org.junit.Test;

public class UnitParserTest {

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
	public void parseAttrはユニット定義属性を読み取って返す() throws InputExeption, ParseException {
		final Input in = Input.fromString(simpleUnitDefString1);
		final UnitParser parser = createParser();
		in.next(); // => 'n'
		in.next(); // => 'i'
		in.next(); // => 't'
		in.next(); // => '='
		in.next(); // => 'X'
		assertThat("parseAttrはユニット定義属性を読み取って返す", parser.parseAttr(in)
				, is("XXXX0000"));
		assertThat("parseAttrを実行後の現在文字は属性区切り文字", in.current(), is(','));
		in.next();
		assertThat(parser.parseAttr(in), is("AAAAA"));
		assertThat(in.current(), is(','));
		in.next();
		assertThat(parser.parseAttr(in), is("BBBBB"));
		assertThat(in.current(), is(','));
		in.next();
		assertThat(parser.parseAttr(in), is("CCCCC"));
		assertThat(in.current(), is(';'));
	}

	@Test
	public void parseParamはユニット定義パラメータを読み取って返す() throws InputExeption, ParseException {
		final Input in = Input.fromString(mockUnitDefParamString1);
		final Param p = createParser().parseParam(in);
		assertThat(p.getName(), is("xx"));
		assertThat(p.getValues().size(), is(8));
	}

	@Test
	public void parseParamValueはユニット定義パラメータ値を読み取って返す() throws InputExeption, ParseException {
		final Input in = Input.fromString(mockUnitDefParamString1);
		final UnitParser parser = createParser();
		in.next(); // => 'x'
		in.next(); // => '='
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue(),
				is("ABCDEF"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue(),
				is("ABC123"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue(),
				is("HAS SPACE"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue(),
				is("QUOTED STRING"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue(),
				is("123456"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue(),
				is("2013/01/01"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue(),
				is("00:00"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue(), is("()"));
	}

	@Test
	public void parseTupleはタプルもどきを読み取って返す() throws InputExeption, ParseException {
		final Input in = Input.fromString("(f=AAAAA,B=BBBBB,CCCCC) (AAAAA,X=BBBBB,Y=CCCCC) ()");
		final UnitParser parser = createParser();
		
		final Tuple t0 = parser.parseTuple(in);
		assertThat(t0.size(), is(3));
		assertThat(t0.get(0).get(), is("AAAAA"));
		assertThat(t0.get(1).get(), is("BBBBB"));
		assertThat(t0.get(2).get(), is("CCCCC"));
		assertThat(t0.get("f").get(), is("AAAAA"));
		assertThat(t0.get("B").get(), is("BBBBB"));
		in.next();
		
		final Tuple t1 = parser.parseTuple(in);
		assertThat(t1.get(0).get(), is("AAAAA"));
		assertThat(t1.get(1).get(), is("BBBBB"));
		assertThat(t1.get(2).get(), is("CCCCC"));
		assertThat(t1.get("X").get(), is("BBBBB"));
		assertThat(t1.get("Y").get(), is("CCCCC"));
		in.next();
		
		final Tuple t2 = parser.parseTuple(in);
		assertThat(t2.size(), is(0));
		assertTrue(t2.get(1).isNothing());
		assertTrue(t2.get("X").isNothing());
	}

	@Test
	public void parseUnitはユニット定義を再帰的に読み取って返す() throws InputExeption, ParseException {
		final Input in1 = Input.fromString(simpleUnitDefString1);
		final UnitParser parser1 = createParser();
		final Unit unit1 = parser1.parseUnit(in1, null);
		assertThat(unit1.getName(), is("XXXX0000"));
		assertThat(unit1.getPermissionMode().get(), is("AAAAA"));
		assertThat(unit1.getOwnerName().get(), is("BBBBB"));
		assertThat(unit1.getResourceGroupName().get(), is("CCCCC"));
		assertThat(unit1.getParams().size(), is(2));
		assertThat(unit1.getType(), is(UnitType.GROUP));
		assertThat(unit1.getComment().get(), is("これはコメントです。"));
		assertThat(unit1.getSubUnits().size(), is(0));

		final Input in2 = Input.fromString(nestedUnitDefString1);
		final UnitParser parser2 = createParser();
		final Unit unit2 = parser2.parseUnit(in2, null);
		assertThat(unit2.getName(), is("XXXX0000"));
		assertThat(unit2.getSubUnits().size(), is(2));
		final Unit unit2_1 = unit2.getSubUnits().get(0);
		final Unit unit2_2 = unit2.getSubUnits().get(1);
		assertThat(unit2_1.getName(), is("XXXX0001"));
		assertThat(unit2_2.getName(), is("XXXX0002"));
	}

}
