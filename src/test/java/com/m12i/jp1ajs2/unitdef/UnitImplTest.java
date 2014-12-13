package com.m12i.jp1ajs2.unitdef;

import static com.m12i.jp1ajs2.unitdef.Params.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.Unit;
import com.m12i.jp1ajs2.unitdef.UnitType;
import com.m12i.jp1ajs2.unitdef.util.Maybe;

import static org.hamcrest.CoreMatchers.*;

public class UnitImplTest {
	
	private static final Unit minimalUnitDef = TestUtils.minimalUnitDef1();
	
	private static final Unit nestedUnitDef = TestUtils.nestedUnitDef1();
	
	private static final Unit jobnetUnitDef2 = TestUtils.jobnetUnitDef2();
	
	@Test
	public void nameはユニット属性パラメータからユニット名を読み取って返す() {
		assertThat(nestedUnitDef.getName(), is("XXXX0000"));
		assertThat(minimalUnitDef.getName(), is("XXXX0000"));
	}
	
	@Test
	public void permissionModeはユニット属性パラメータから許可モードを読み取って返す() {
		assertThat(nestedUnitDef.getPermissionMode().get(), is("AAAAA"));
		assertTrue(minimalUnitDef.getPermissionMode().isNothing());
	}
	
	@Test
	public void permissionModeはユニット属性パラメータからJP1ユーザ名を読み取って返す() {
		assertThat(nestedUnitDef.getOwnerName().get(), is("BBBBB"));
		assertTrue(minimalUnitDef.getOwnerName().isNothing());
	}
	
	@Test
	public void permissionModeはユニット属性パラメータからリソースグループ名を読み取って返す() {
		assertThat(nestedUnitDef.getResourceGroupName().get(), is("CCCCC"));
		assertTrue(minimalUnitDef.getResourceGroupName().isNothing());
	}

	@Test
	public void paramsはユニット定義パラメータからサブユニットの定義を除く情報を読み取って返す() {
		assertThat(nestedUnitDef.getParams().size(), is(9));
		assertThat(minimalUnitDef.getParams().size(), is(1));
	}

	@Test
	public void paramsはユニット定義パラメータからサブユニットの情報を読み取って返す() {
		assertThat(nestedUnitDef.getSubUnits().size(), is(2));
		assertThat(minimalUnitDef.getSubUnits().size(), is(0));
	}

	@Test
	public void params_Stringはサブユニットのうち第1引数で指定された名前のものを検索して返す() {
		assertThat(nestedUnitDef.getSubUnits("XXXX0001").get().getName(), is("XXXX0001"));
		assertThat(nestedUnitDef.getSubUnits("XXXX0002").get().getName(), is("XXXX0002"));
		assertTrue(nestedUnitDef.getSubUnits("XXXX0003").isNothing());
		assertTrue(minimalUnitDef.getSubUnits("XXXX0001").isNothing());
	}
	
	@Test 
	public void unitTypeはユニット定義パラメータの属性定義情報からユニット種別を読み取って返す() {
		assertThat(nestedUnitDef.getType(), is(UnitType.GROUP));
	}
	
	@Test 
	public void unitCommentはユニット定義パラメータの属性定義情報からコメントを読み取って返す() {
		assertThat(nestedUnitDef.getComment().get(), is("これはコメントです。"));
		assertThat(minimalUnitDef.getComment().isNothing(), is(true));
	}
	
	@Test 
	public void positionsはユニット定義パラメータのユニット構成定義情報からサブユニットの位置情報を読み取って返す() {
		assertThat(getElements(nestedUnitDef).size(), is(2));
		assertThat(getElements(minimalUnitDef).size(), is(0));
		
		final List<Element> pos = getElements(nestedUnitDef);
		for(final Element p : pos){
			assertThat(p.getHorizontalPixel(), is(p.getUnit().getName().equals("XXXX0001") ? 80 : 240));
			assertThat(p.getVerticalPixel(), is(p.getUnit().getName().equals("XXXX0001") ? 48 : 144));
			assertThat(p.getX(), is(p.getUnit().getName().equals("XXXX0001") ? 0 : 1));
			assertThat(p.getY(), is(p.getUnit().getName().equals("XXXX0001") ? 0 : 1));
		}
	}
	
	@Test
	public void edgesはユニット定義パラメータのジョブネット定義情報から関連線の情報を読み取って返す() {
		assertThat(getAnteroposteriorRelationship(nestedUnitDef).size(), is(2));
		assertThat(getAnteroposteriorRelationship(minimalUnitDef).size(), is(0));
		
		final Maybe<AnteroposteriorRelationship> edges = getAnteroposteriorRelationship(nestedUnitDef);
		for(final AnteroposteriorRelationship e : edges){
			if(e.getFrom().getName().equals("XXXX0001")){
				assertThat(e.getTo().getName(), is("XXXX0002"));
				assertThat(e.getType(), is(UnitConnectionType.SEQUENTIAL));
			}else{
				assertThat(e.getTo().getName(), is("XXXX0001"));
				assertThat(e.getType(), is(UnitConnectionType.CONDITIONAL));
			}
		}
	}
	
	@Test
	public void timeRequiredはユニット定義パラメータのジョブネット定義情報から実行所要時間の情報を読み取って返す(){
		assertThat(getFixedDuration(nestedUnitDef).get(), is(360));
		assertTrue(getFixedDuration(minimalUnitDef).isNothing());
	}
	
	@Test
	public void fullQualifiedNameはユニットの完全名を返す(){
		assertThat(nestedUnitDef.getFullQualifiedName(), is("/XXXX0000"));
		assertThat(nestedUnitDef.getSubUnits("XXXX0001").get().getFullQualifiedName(), is("/XXXX0000/XXXX0001"));
	}
}
