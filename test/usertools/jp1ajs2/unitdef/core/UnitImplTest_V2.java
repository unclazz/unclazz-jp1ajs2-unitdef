package usertools.jp1ajs2.unitdef.core;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import usertools.jp1ajs2.unitdef.ext.*;
import static org.hamcrest.CoreMatchers.*;
import static usertools.jp1ajs2.unitdef.util.Accessors.*;

public class UnitImplTest_V2 {
	
	private static final Unit minimalUnitDef1 = ParseUtils2.parse(TestUtils.minimalUnitDefString1).right();
	
	private static final Unit nestedUnitDef1 = ParseUtils2.parse(TestUtils.nestedUnitDefString1).right();
	
	private static final Unit jobnetUnitDef2 = ParseUtils2.parse(TestUtils.jobnetUnitDefString2).right();
	
	@Test
	public void nameはユニット属性パラメータからユニット名を読み取って返す() {
		assertThat(nestedUnitDef1.getName(), is("XXXX0000"));
		assertThat(minimalUnitDef1.getName(), is("XXXX0000"));
	}
	
	@Test
	public void permissionModeはユニット属性パラメータから許可モードを読み取って返す() {
		assertThat(nestedUnitDef1.getPermissionMode().get(), is("AAAAA"));
		assertTrue(minimalUnitDef1.getPermissionMode().isNone());
	}
	
	@Test
	public void permissionModeはユニット属性パラメータからJP1ユーザ名を読み取って返す() {
		assertThat(nestedUnitDef1.getOwnerName().get(), is("BBBBB"));
		assertTrue(minimalUnitDef1.getOwnerName().isNone());
	}
	
	@Test
	public void permissionModeはユニット属性パラメータからリソースグループ名を読み取って返す() {
		assertThat(nestedUnitDef1.getResourceGroupName().get(), is("CCCCC"));
		assertTrue(minimalUnitDef1.getResourceGroupName().isNone());
	}

	@Test
	public void paramsはユニット定義パラメータからサブユニットの定義を除く情報を読み取って返す() {
		assertThat(nestedUnitDef1.getParams().size(), is(9));
		assertThat(minimalUnitDef1.getParams().size(), is(1));
	}

	@Test
	public void paramsはユニット定義パラメータからサブユニットの情報を読み取って返す() {
		assertThat(nestedUnitDef1.getSubUnits().size(), is(2));
		assertThat(minimalUnitDef1.getSubUnits().size(), is(0));
	}

	@Test
	public void params_Stringはサブユニットのうち第1引数で指定された名前のものを検索して返す() {
		assertThat(nestedUnitDef1.getSubUnit("XXXX0001").get().getName(), is("XXXX0001"));
		assertThat(nestedUnitDef1.getSubUnit("XXXX0002").get().getName(), is("XXXX0002"));
		assertTrue(nestedUnitDef1.getSubUnit("XXXX0003").isNone());
		assertTrue(minimalUnitDef1.getSubUnit("XXXX0001").isNone());
	}
	
	@Test 
	public void unitTypeはユニット定義パラメータの属性定義情報からユニット種別を読み取って返す() {
		assertThat(nestedUnitDef1.getType(), is(UnitType.GROUP));
	}
	
	@Test 
	public void unitCommentはユニット定義パラメータの属性定義情報からコメントを読み取って返す() {
		assertThat(nestedUnitDef1.getComment().get(), is("これはコメントです。"));
		assertThat(minimalUnitDef1.getComment().get(), is(""));
	}
	
	@Test 
	public void positionsはユニット定義パラメータのユニット構成定義情報からサブユニットの位置情報を読み取って返す() {
		assertThat(elements(nestedUnitDef1).size(), is(2));
		assertThat(elements(minimalUnitDef1).size(), is(0));
		
		final List<Element> pos = elements(nestedUnitDef1);
		for(final Element p : pos){
			assertThat(p.getHorizontalPixel(), is(p.getUnit().getName().equals("XXXX0001") ? 80 : 240));
			assertThat(p.getVerticalPixel(), is(p.getUnit().getName().equals("XXXX0001") ? 48 : 144));
			assertThat(p.getX(), is(p.getUnit().getName().equals("XXXX0001") ? 0 : 1));
			assertThat(p.getY(), is(p.getUnit().getName().equals("XXXX0001") ? 0 : 1));
		}
	}
	
	@Test
	public void edgesはユニット定義パラメータのジョブネット定義情報から関連線の情報を読み取って返す() {
		assertThat(arrows(nestedUnitDef1).size(), is(2));
		assertThat(arrows(minimalUnitDef1).size(), is(0));
		
		final List<Arrow> edges = arrows(nestedUnitDef1);
		for(final Arrow e : edges){
			if(e.getFrom().getName().equals("XXXX0001")){
				assertThat(e.getTo().getName(), is("XXXX0002"));
				assertThat(e.type(), is(UnitConnectionType.SEQUENTIAL));
			}else{
				assertThat(e.getTo().getName(), is("XXXX0001"));
				assertThat(e.type(), is(UnitConnectionType.CONDITIONAL));
			}
		}
	}
	
	@Test
	public void timeRequiredはユニット定義パラメータのジョブネット定義情報から実行所要時間の情報を読み取って返す(){
		assertThat(fixedDuration(nestedUnitDef1).get(), is(360));
		assertTrue(fixedDuration(minimalUnitDef1).isNone());
	}
	
	@Test
	public void fullQualifiedNameはユニットの完全名を返す(){
		assertThat(nestedUnitDef1.getFullQualifiedName(), is("/XXXX0000"));
		assertThat(nestedUnitDef1.getSubUnit("XXXX0001").get().getFullQualifiedName(), is("/XXXX0000/XXXX0001"));
	}
	
	@Test
	public void getDescendantUnitsは子孫要素のうちクエリにマッチするものを返す() {
		assertThat(jobnetUnitDef2.getDescendentUnits("ty == g").size(), is(5));
	}

}
