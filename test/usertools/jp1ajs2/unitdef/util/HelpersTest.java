package usertools.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;

import usertools.jp1ajs2.unitdef.core.TestUtils;
import usertools.jp1ajs2.unitdef.core.Unit;
import usertools.jp1ajs2.unitdef.util.Helpers;

public class HelpersTest {

	private static final Unit minimalUnitDef1 = TestUtils.minimalUnitDef1();
	
	private static final Unit minimalUnitDef2 = TestUtils.minimalUnitDef2();
	
	private static final Unit nestedUnitDef1 = TestUtils.nestedUnitDef1();
	
	@Test
	public void formatはユニット定義文字列を返す() {
		assertThat(Helpers.format(TestUtils.minimalUnitDef1()), is(TestUtils.minimalUnitDefString1));
		assertThat(Helpers.format(TestUtils.minimalUnitDef2()), is(TestUtils.minimalUnitDefString2));
		assertThat(Helpers.format(TestUtils.jobnetUnitDef1()), is(TestUtils.jobnetUnitDefString1));
		assertThat(Helpers.format(TestUtils.nestedUnitDef1()), is(TestUtils.nestedUnitDefString1));
	}
	
	@Test
	public void findParamAllは第2引数で指定した名前のパラメータのリストを返す() {
		assertThat(Helpers.findParamAll(nestedUnitDef1, "el").size(), is(2));
		assertThat("対象のユニットがそのパラメータを持たない場合0を返す", Helpers.findParamAll(minimalUnitDef1, "el").size(), is(0));
	}
	
	@Test
	public void findParamAllAsStringValuesは第2引数で指定した名前のパラメータの文字列値のリストを返す() {
		assertThat(Helpers.findParamAllAsStringValues(nestedUnitDef1, "el").size(), is(2));
		assertThat(Helpers.findParamAllAsStringValues(nestedUnitDef1, "ty").get(0), is("g"));
		assertThat("対象のユニットがそのパラメータを持たない場合0を返す", Helpers.findParamAll(minimalUnitDef1, "el").size(), is(0));
	}
	
	@Test
	public void findParamOneは第2引数で指定した名前のパラメータを1つ返す() {
		assertThat("対象ユニットが同名パラメータを複数持つ場合もいずれか1つだけを返す", Helpers.findParamOne(nestedUnitDef1, "el").get().getName(), is("el"));
		assertTrue(Helpers.findParamOne(minimalUnitDef1, "el").isNone());
	}
	
	@Test
	public void findParamOne_booleanは第2引数で指定した名前のパラメータの真偽値を1つ返す() {
		assertThat(Helpers.findParamOne(minimalUnitDef2, "t0", false), is(true));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "t1", false), is(true));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "t2", false), is(true));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "t3", false), is(true));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "t4", false), is(true));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "t5", false), is(true));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "t6", false), is(false));
		
		assertThat(Helpers.findParamOne(minimalUnitDef2, "f0", true), is(false));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "f1", true), is(false));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "f2", true), is(false));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "f3", true), is(false));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "f4", true), is(false));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "f5", true), is(false));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "f6", true), is(true));
	}
	
	@Test
	public void findParamOne_intは第2引数で指定した名前のパラメータの整数値を1つ返す() {
		assertThat(Helpers.findParamOne(minimalUnitDef2, "i0", 1), is(0));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "i1", 0), is(1));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "i2", 0), is(2));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "i3", 0), is(1));
		assertThat(Helpers.findParamOne(minimalUnitDef2, "i4", 0), is(-1));
		try {
			Helpers.findParamOne(minimalUnitDef2, "i5", 0);
		} catch(NumberFormatException e) {
			assertTrue(true);
		}
		try {
			Helpers.findParamOne(minimalUnitDef2, "i6", 0);
		} catch(NumberFormatException e) {
			assertTrue(true);
		}
		try {
			Helpers.findParamOne(minimalUnitDef2, "i7", 0);
		} catch(NumberFormatException e) {
			assertTrue(true);
		}
		assertThat(Helpers.findParamOne(minimalUnitDef2, "i8", 8), is(8));
	}
}
