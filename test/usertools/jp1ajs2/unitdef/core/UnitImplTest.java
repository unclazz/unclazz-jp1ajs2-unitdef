package usertools.jp1ajs2.unitdef.core;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import usertools.jp1ajs2.unitdef.ext.*;
import static org.hamcrest.CoreMatchers.*;
import static usertools.jp1ajs2.unitdef.util.Accessors.*;

public class UnitImplTest {
	
	private static final Unit minimalUnitDef = TestUtils.minimalUnitDef1();
	
	private static final Unit nestedUnitDef = TestUtils.nestedUnitDef1();
	
	@Test
	public void nameはユニット属性パラメータからユニット名を読み取って返す() {
		assertThat(nestedUnitDef.getName(), is("XXXX0000"));
		assertThat(minimalUnitDef.getName(), is("XXXX0000"));
	}
	
	@Test
	public void permissionModeはユニット属性パラメータから許可モードを読み取って返す() {
		assertThat(nestedUnitDef.getPermissionMode(), is("AAAAA"));
		assertNull(minimalUnitDef.getPermissionMode());
	}
	
	@Test
	public void permissionModeはユニット属性パラメータからJP1ユーザ名を読み取って返す() {
		assertThat(nestedUnitDef.getOwnerName(), is("BBBBB"));
		assertNull(minimalUnitDef.getOwnerName());
	}
	
	@Test
	public void permissionModeはユニット属性パラメータからリソースグループ名を読み取って返す() {
		assertThat(nestedUnitDef.getResourceGroupName(), is("CCCCC"));
		assertNull(minimalUnitDef.getResourceGroupName());
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
		assertThat(nestedUnitDef.getSubUnit("XXXX0001").getName(), is("XXXX0001"));
		assertThat(nestedUnitDef.getSubUnit("XXXX0002").getName(), is("XXXX0002"));
		assertNull(nestedUnitDef.getSubUnit("XXXX0003"));
		assertNull(minimalUnitDef.getSubUnit("XXXX0001"));
	}
	
	@Test 
	public void unitTypeはユニット定義パラメータの属性定義情報からユニット種別を読み取って返す() {
		assertThat(nestedUnitDef.getType(), is(UnitType.GROUP));
	}
	
	@Test 
	public void unitCommentはユニット定義パラメータの属性定義情報からコメントを読み取って返す() {
		assertThat(nestedUnitDef.getComment(), is("これはコメントです。"));
		assertThat(minimalUnitDef.getComment(), is(""));
	}
	
	@Test 
	public void positionsはユニット定義パラメータのユニット構成定義情報からサブユニットの位置情報を読み取って返す() {
		assertThat(elements(nestedUnitDef).size(), is(2));
		assertThat(elements(minimalUnitDef).size(), is(0));
		
		final List<Element> pos = elements(nestedUnitDef);
		for(final Element p : pos){
			assertThat(p.getHorizontalPixel(), is(p.getUnit().getName().equals("XXXX0001") ? 80 : 240));
			assertThat(p.getVerticalPixel(), is(p.getUnit().getName().equals("XXXX0001") ? 48 : 144));
			assertThat(p.getX(), is(p.getUnit().getName().equals("XXXX0001") ? 0 : 1));
			assertThat(p.getY(), is(p.getUnit().getName().equals("XXXX0001") ? 0 : 1));
		}
	}
	
	@Test
	public void edgesはユニット定義パラメータのジョブネット定義情報から関連線の情報を読み取って返す() {
		assertThat(arrows(nestedUnitDef).size(), is(2));
		assertThat(arrows(minimalUnitDef).size(), is(0));
		
		final List<Arrow> edges = arrows(nestedUnitDef);
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
		assertThat(fixedDuration(nestedUnitDef), is(360));
		assertThat(fixedDuration(minimalUnitDef), is(-1));
	}
	
	@Test
	public void fullQualifiedNameはユニットの完全名を返す(){
		assertThat(nestedUnitDef.getFullQualifiedName(), is("/XXXX0000"));
		assertThat(nestedUnitDef.getSubUnit("XXXX0001").getFullQualifiedName(), is("/XXXX0000/XXXX0001"));
	}

}
