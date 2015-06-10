package org.doogwood.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import org.doogwood.jp1ajs2.unitdef.UnitType;
import org.junit.Test;

import static org.doogwood.jp1ajs2.unitdef.TestUtils.*;
import static org.hamcrest.CoreMatchers.*;

public class UnitImplTest {
	
	@Test
	public void nameはユニット属性パラメータからユニット名を読み取って返す() {
		assertThat(nestedUnitDef1().getName(), is("XXXX0000"));
		assertThat(minimalUnitDef1().getName(), is("XXXX0000"));
	}
	
	@Test
	public void permissionModeはユニット属性パラメータから許可モードを読み取って返す() {
		assertThat(nestedUnitDef1().getPermissionMode().get(), is("AAAAA"));
		assertTrue(minimalUnitDef1().getPermissionMode().isNotPresent());
	}
	
	@Test
	public void permissionModeはユニット属性パラメータからJP1ユーザ名を読み取って返す() {
		assertThat(nestedUnitDef1().getOwnerName().get(), is("BBBBB"));
		assertTrue(minimalUnitDef1().getOwnerName().isNotPresent());
	}
	
	@Test
	public void permissionModeはユニット属性パラメータからリソースグループ名を読み取って返す() {
		assertThat(nestedUnitDef1().getResourceGroupName().get(), is("CCCCC"));
		assertTrue(minimalUnitDef1().getResourceGroupName().isNotPresent());
	}

	@Test
	public void paramsはユニット定義パラメータからサブユニットの定義を除く情報を読み取って返す() {
		assertThat(nestedUnitDef1().getParams().size(), is(9));
		assertThat(minimalUnitDef1().getParams().size(), is(1));
	}

	@Test
	public void paramsはユニット定義パラメータからサブユニットの情報を読み取って返す() {
		assertThat(nestedUnitDef1().getSubUnits().size(), is(2));
		assertThat(minimalUnitDef1().getSubUnits().size(), is(0));
	}

	@Test
	public void params_Stringはサブユニットのうち第1引数で指定された名前のものを検索して返す() {
		assertThat(nestedUnitDef1().getSubUnits("XXXX0001").get(0).getName(), is("XXXX0001"));
		assertThat(nestedUnitDef1().getSubUnits("XXXX0002").get(0).getName(), is("XXXX0002"));
		assertTrue(nestedUnitDef1().getSubUnits("XXXX0003").isEmpty());
		assertTrue(minimalUnitDef1().getSubUnits("XXXX0001").isEmpty());
	}
	
	@Test 
	public void unitTypeはユニット定義パラメータの属性定義情報からユニット種別を読み取って返す() {
		assertThat(nestedUnitDef1().getType(), is(UnitType.GROUP));
	}
	
	@Test 
	public void unitCommentはユニット定義パラメータの属性定義情報からコメントを読み取って返す() {
		assertThat(nestedUnitDef1().getComment().get(), is("これはコメントです。"));
		assertThat(minimalUnitDef1().getComment().isNotPresent(), is(true));
	}
	
	@Test
	public void fullQualifiedNameはユニットの完全名を返す(){
		assertThat(nestedUnitDef1().getFullQualifiedName(), is("/XXXX0000"));
		assertThat(nestedUnitDef1().getSubUnits("XXXX0001").get(0).getFullQualifiedName(), is("/XXXX0000/XXXX0001"));
	}
}
