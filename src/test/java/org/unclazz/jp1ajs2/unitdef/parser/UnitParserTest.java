package org.unclazz.jp1ajs2.unitdef.parser;

import static org.junit.Assert.*;
import static org.unclazz.jp1ajs2.unitdef.TestUtils.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.Attributes;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.parser.UnitParser;
import org.unclazz.jp1ajs2.unitdef.query.Q;

public class UnitParserTest {

	private static final String simpleUnitDefString1 = "unit=XXXX0000,0000,BBBBB,CCCCC;"
			+ "{"
			+ "ty=g;" 
			+ "cm=\"これはコメントです。\";" 
			+ "}";

	private static final String nestedUnitDefString1 = "unit=XXXX0000,0003,BBBBB,CCCCC;\r\n"
			+ "{\r\n"
			+ "    ty=g;\r\n" 
			+ "    cm=\"これはコメントです。\";\r\n"
			+ "    unit=XXXX0001,0004,BBBBB,CCCCC;\r\n"
			+ "    {\r\n"
			+ "        ty=g;\r\n" 
			+ "        cm=\"これはコメントです。\";\r\n" 
			+ "    }\r\n"
			+ "    unit=XXXX0002,0005,BBBBB,CCCCC;\r\n"
			+ "    {\r\n"
			+ "        ty=g;\r\n" 
			+ "        cm=\"これはコメントです。\";\r\n" 
			+ "    }\r\n"
			+ "}\r\n";

	private static final String mockUnitDefParamString1 = "xx=ABCDEF,ABC123,HAS SPACE,\"QUOTED STRING\",123456,2013/01/01,00:00,();";

	@Test
	public void parseAttrはユニット定義属性を読み取って返す() throws InputExeption, ParseException {
		final Input in = Input.fromCharSequence(simpleUnitDefString1);
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
		assertThat(parser.parseAttr(in), is("0000"));
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
		final Input in = Input.fromCharSequence(mockUnitDefParamString1);
		final Parameter p = createParser().parseParam(in);
		assertThat(p.getName(), is("xx"));
		assertThat(p.getValues().size(), is(8));
	}

	@Test
	public void parseParamValueはユニット定義パラメータ値を読み取って返す() throws InputExeption, ParseException {
		final Input in = Input.fromCharSequence(mockUnitDefParamString1);
		final UnitParser parser = createParser();
		in.next(); // => 'x'
		in.next(); // => '='
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue().toString(),
				is("ABCDEF"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue().toString(),
				is("ABC123"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue().toString(),
				is("HAS SPACE"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue().toString(),
				is("QUOTED STRING"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue().toString(),
				is("123456"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue().toString(),
				is("2013/01/01"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue().toString(),
				is("00:00"));
		in.next();
		assertThat(parser.parseParamValue(in).getStringValue().toString(), is("()"));
	}

	@Test
	public void parseTupleはタプルもどきを読み取って返す() throws InputExeption, ParseException {
		final Input in = Input.fromCharSequence("(f=AAAAA,B=BBBBB,CCCCC) (AAAAA,X=BBBBB,Y=CCCCC) ()");
		final UnitParser parser = createParser();
		
		final Tuple t0 = parser.parseTuple(in);
		assertThat(t0.size(), is(3));
		assertThat(t0.get(0).toString(), is("AAAAA"));
		assertThat(t0.get(1).toString(), is("BBBBB"));
		assertThat(t0.get(2).toString(), is("CCCCC"));
		assertThat(t0.get("f").toString(), is("AAAAA"));
		assertThat(t0.get("B").toString(), is("BBBBB"));
		in.next();
		
		final Tuple t1 = parser.parseTuple(in);
		assertThat(t1.get(0).toString(), is("AAAAA"));
		assertThat(t1.get(1).toString(), is("BBBBB"));
		assertThat(t1.get(2).toString(), is("CCCCC"));
		assertThat(t1.get("X").toString(), is("BBBBB"));
		assertThat(t1.get("Y").toString(), is("CCCCC"));
		in.next();
		
		final Tuple t2 = parser.parseTuple(in);
		assertThat(t2.size(), is(0));
	}

	@Test
	public void parseUnitはユニット定義を再帰的に読み取って返す() throws InputExeption, ParseException {
		final Input in1 = Input.fromCharSequence(simpleUnitDefString1);
		final UnitParser parser1 = createParser();
		final Unit unit1 = parser1.parseUnit(in1, null);
		final Attributes unit1Attrs = unit1.getAttributes();
		assertThat(unit1Attrs.getUnitName(), is("XXXX0000"));
		assertThat(unit1Attrs.getPermissionMode().toString(), is("0000"));
		assertThat(unit1Attrs.getJP1UserName(), is("BBBBB"));
		assertThat(unit1Attrs.getResourceGroupName(), is("CCCCC"));
		assertThat(unit1.getParameters().size(), is(2));
		assertThat(unit1.getType(), is(UnitType.JOB_GROUP));
		assertThat(unit1.query(Q.ty().list()).get(0), is(UnitType.JOB_GROUP));
		assertThat(unit1.query(Q.cm().list()).get(0).toString(), is("これはコメントです。"));
		assertThat(unit1.getSubUnits().size(), is(0));

		final Input in2 = Input.fromCharSequence(nestedUnitDefString1);
		final UnitParser parser2 = createParser();
		final Unit unit2 = parser2.parseUnit(in2, null);
		final Attributes unit2Attrs = unit2.getAttributes();
		assertThat(unit2Attrs.getUnitName(), is("XXXX0000"));
		assertThat(unit2.getSubUnits().size(), is(2));
		final Unit unit2_1 = unit2.getSubUnits().get(0);
		final Unit unit2_2 = unit2.getSubUnits().get(1);
		assertThat(unit2_1.getAttributes().getUnitName(), is("XXXX0001"));
		assertThat(unit2_2.getAttributes().getUnitName(), is("XXXX0002"));
	}
	
	@Test
	public void parseはルートレベルのすべてのユニットを読み取って返す() throws InputExeption {
		final Input in = Input.fromCharSequence(simpleUnitDefString1 + '\n' + nestedUnitDefString1);
		final UnitParser p1 = createParser();
		final ParseResult<List<Unit>> r1 = p1.parse(in);
		assertThat(r1.get().size(), is(2));
	}

}
