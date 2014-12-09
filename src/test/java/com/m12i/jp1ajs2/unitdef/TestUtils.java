package com.m12i.jp1ajs2.unitdef;

import com.m12i.jp1ajs2.unitdef.ParseUtils;
import com.m12i.jp1ajs2.unitdef.Unit;
import com.m12i.jp1ajs2.unitdef.parser.UnitParser;
import com.m12i.jp1ajs2.unitdef.parser.Input;

public final class TestUtils {
	private TestUtils(){}
	
	public static final String minimalUnitDefString1 = "unit=XXXX0000,,,;\r\n"
			+ "{\r\n"
			+ "	ty=g;\r\n"
			+ "}\r\n";

	public static final String minimalUnitDefString2 = "unit=XXXX0000,,,;\r\n"
			+ "{\r\n"
			+ "	ty=g;\r\n"
			+ "	t0=y;\r\n"
			+ "	t1=yes;\r\n"
			+ "	t2=on;\r\n"
			+ "	t3=t;\r\n"
			+ "	t4=true;\r\n"
			+ "	t5=1;\r\n"
			+ "	f0=n;\r\n"
			+ "	f1=no;\r\n"
			+ "	f2=off;\r\n"
			+ "	f3=f;\r\n"
			+ "	f4=false;\r\n"
			+ "	f5=0;\r\n"
			+ "	i0=0;\r\n"
			+ "	i1=1;\r\n"
			+ "	i2=2;\r\n"
			+ "	i3=+1;\r\n"
			+ "	i4=-1;\r\n"
			+ "	i5=0.0;\r\n"
			+ "	i6=1.0;\r\n"
			+ "	i7=x;\r\n"
			+ "}\r\n";

	public static final String nestedUnitDefString1 = "unit=XXXX0000,AAAAA,BBBBB,CCCCC;\r\n"
			+ "{\r\n"
			+ "	ty=g;\r\n"
			+ "	el=XXXX0001,g,+80 +48;\r\n" 
			+ "	el=XXXX0002,g,+240 +144;\r\n"
			+ "	ar=(f=XXXX0001,t=XXXX0002);\r\n" 
			+ "	ar=(f=XXXX0002,t=XXXX0001,con);\r\n" // 実際には相互リンクは許されない 
			+ "	cm=\"これはコメントです。\";\r\n"
			+ "	xx=ABCDEF,ABC123,HAS SPACE,\"QUOTED STRING\",123456,2013/01/01,00:00,();\r\n"
			+ "	un=foobar;\r\n"
			+ "	fd=360;\r\n"
			+ "	unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
			+ "	{\r\n"
			+ "		ty=g;\r\n" 
			+ "		cm=\"これはコメントです。\";\r\n" 
			+ "	}\r\n"
			+ "	unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
			+ "	{\r\n"
			+ "		ty=g;\r\n" 
			+ "		cm=\"これはコメントです。\";\r\n" 
			+ "	}\r\n"
			+ "}\r\n";
	
	public static final String jobnetUnitDefString1 = "unit=XXXX0000,AAAAA,BBBBB,CCCCC;\r\n"
			+ "{\r\n"
			+ "	ty=n;\r\n"
			+ "	el=XXXX0001,g,+80 +48;\r\n" 
			+ "	el=XXXX0002,g,+240 +144;\r\n"
			+ "	ar=(f=XXXX0001,t=XXXX0002);\r\n" 
			+ "	ar=(f=XXXX0002,t=XXXX0001,con);\r\n" // 実際には相互リンクは許されない 
			+ "	cm=\"これはコメントです。\";\r\n"
			+ "	xx=ABCDEF,ABC123,HAS SPACE,\"QUOTED STRING\",123456,2013/01/01,00:00,();\r\n"
			+ "	fd=360;\r\n"
			+ "	ha=n;\r\n"
			+ "	unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
			+ "	{\r\n"
			+ "		ty=n;\r\n" 
			+ "		cm=\"これはコメントです。\";\r\n" 
			+ "		fd=120;\r\n"
			+ "		ha=y;\r\n"
			+ "	}\r\n"
			+ "	unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
			+ "	{\r\n"
			+ "		ty=n;\r\n" 
			+ "		cm=\"これはコメントです。\";\r\n" 
			+ "	}\r\n"
			+ "}\r\n";
	
	public static final String jobnetUnitDefString2 = "unit=XXXX0000,AAAAA,BBBBB,CCCCC;\r\n"
			+ "{\r\n"
			+ "	ty=n;\r\n"
			+ "	el=XXXX0001,g,+80 +48;\r\n" 
			+ "	el=XXXX0002,g,+240 +144;\r\n"
			+ "	ar=(f=XXXX0001,t=XXXX0002);\r\n" 
			+ "	ar=(f=XXXX0002,t=XXXX0001,con);\r\n" // 実際には相互リンクは許されない 
			+ "	cm=\"これはコメントです。\";\r\n"
			+ "	xx=ABCDEF,ABC123,HAS SPACE,\"QUOTED STRING\",123456,2013/01/01,00:00,();\r\n"
			+ "	fd=360;\r\n"
			+ "	ha=n;\r\n"
			+ "	unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
			+ "	{\r\n"
			+ "		ty=n;\r\n" 
			+ "		cm=\"これはコメントです。\";\r\n" 
			+ "		fd=120;\r\n"
			+ "		ha=y;\r\n"
			+ "		unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
			+ "		{\r\n"
			+ "			ty=g;\r\n" 
			+ "			cm=\"これはコメントです。\";\r\n" 
			+ "			unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
			+ "			{\r\n"
			+ "				ty=g;\r\n" 
			+ "				cm=\"これはコメントです。\";\r\n" 
			+ "			}\r\n"
			+ "		}\r\n"
			+ "		unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
			+ "		{\r\n"
			+ "			ty=g;\r\n" 
			+ "			cm=\"これはコメントです。\";\r\n" 
			+ "		}\r\n"
			+ "	}\r\n"
			+ "	unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
			+ "	{\r\n"
			+ "		ty=n;\r\n" 
			+ "		cm=\"これはコメントです。\";\r\n" 
			+ "		unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
			+ "		{\r\n"
			+ "			ty=g;\r\n" 
			+ "			cm=\"これはコメントです。\";\r\n" 
			+ "		}\r\n"
			+ "		unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
			+ "		{\r\n"
			+ "			ty=g;\r\n" 
			+ "			cm=\"これはコメントです。\";\r\n" 
			+ "		}\r\n"
			+ "	}\r\n"
			+ "}\r\n";
	
	public static Unit minimalUnitDef1() {
		try {
			return ParseUtils.parse(Input.fromString(minimalUnitDefString1)).unit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Unit minimalUnitDef2() {
		try {
			return ParseUtils.parse(Input.fromString(minimalUnitDefString2)).unit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Unit nestedUnitDef1() {
		try {
			return ParseUtils.parse(Input.fromString(nestedUnitDefString1)).unit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Unit jobnetUnitDef1() {
		try {
			return ParseUtils.parse(Input.fromString(jobnetUnitDefString1)).unit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Unit jobnetUnitDef2() {
		try {
			return ParseUtils.parse(Input.fromString(jobnetUnitDefString2)).unit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static UnitParser createParser() {
		return new UnitParser();
	}
	
}
