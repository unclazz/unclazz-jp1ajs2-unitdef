package usertools.jp1ajs2.unitdef.core;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static usertools.jp1ajs2.unitdef.core.TestUtils.*;

import org.junit.Test;

import usertools.jp1ajs2.unitdef.core.Unit;
import usertools.jp1ajs2.unitdef.core.UnitType;

import com.m12i.code.parse.ParseException;

public class ParserTest_V2 {

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

	@Test
	public void parseUnitはユニット定義を再帰的に読み取って返す() throws ParseException {
//		final Parsable code1 = createCode(simpleUnitDefString1);
//		final Parser parser1 = with(code1);
		final Unit unit1 = withV2(simpleUnitDefString1);
		assertThat(unit1.getName(), is("XXXX0000"));
		assertThat(unit1.getPermissionMode().get(), is("AAAAA"));
		assertThat(unit1.getOwnerName().get(), is("BBBBB"));
		assertThat(unit1.getResourceGroupName().get(), is("CCCCC"));
		assertThat(unit1.getParams().size(), is(2));
		assertThat(unit1.getType(), is(UnitType.GROUP));
		assertThat(unit1.getComment().get(), is("これはコメントです。"));
		assertThat(unit1.getSubUnits().size(), is(0));

//		final Parsable code2 = createCode(nestedUnitDefString1);
//		final Parser parser2 = with(code2);
		final Unit unit2 = withV2(nestedUnitDefString1);
		assertThat(unit2.getName(), is("XXXX0000"));
		assertThat(unit2.getSubUnits().size(), is(2));
		final Unit unit2_1 = unit2.getSubUnits().get(0);
		final Unit unit2_2 = unit2.getSubUnits().get(1);
		assertThat(unit2_1.getName(), is("XXXX0001"));
		assertThat(unit2_2.getName(), is("XXXX0002"));
	}

}
