package com.m12i.jp1ajs2.unitdef.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.TestUtils;
import com.m12i.jp1ajs2.unitdef.util.Formatter;

public class FormatterTest {
	
	@Test
	public void formatはユニット定義文字列を返す() {
		final Formatter f = new Formatter();
		assertThat(f.format(TestUtils.minimalUnitDef1()), is(TestUtils.minimalUnitDefString1));
		assertThat(f.format(TestUtils.minimalUnitDef2()), is(TestUtils.minimalUnitDefString2));
		assertThat(f.format(TestUtils.jobnetUnitDef1()), is(TestUtils.jobnetUnitDefString1));
		assertThat(f.format(TestUtils.nestedUnitDef1()), is(TestUtils.nestedUnitDefString1));
	}

}
