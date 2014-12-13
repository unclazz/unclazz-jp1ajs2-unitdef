package com.m12i.jp1ajs2.unitdef;

import static com.m12i.jp1ajs2.unitdef.Params.getAnteroposteriorRelationships;
import static com.m12i.jp1ajs2.unitdef.Params.getElements;
import static com.m12i.jp1ajs2.unitdef.Params.getFixedDuration;
import static com.m12i.jp1ajs2.unitdef.TestUtils.minimalUnitDef1;
import static com.m12i.jp1ajs2.unitdef.TestUtils.nestedUnitDef1;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.util.Maybe;

public class ParamsTest {

	@Test 
	public void testGetIntValues() {
		Maybe<Integer> vs = Params.getIntValues(nestedUnitDef1(), "fd");
		assertThat(vs.get(0), is(360));
	}
	
	@Test
	public void testGetStringValues() {
		Maybe<String> vs = Params.getStringValues(nestedUnitDef1(), "el");
		for (final String v : vs) {
			v.startsWith("XXXX000");
		}
	}
	
	@Test
	public void testGetAnteroposteriorRelationship() {
		assertThat(getAnteroposteriorRelationships(nestedUnitDef1()).size(), is(2));
		assertThat(getAnteroposteriorRelationships(minimalUnitDef1()).size(), is(0));
		
		final Maybe<AnteroposteriorRelationship> edges = getAnteroposteriorRelationships(nestedUnitDef1());
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
	public void testGetFixedDuration(){
		assertThat(getFixedDuration(nestedUnitDef1()).get(), is(360));
		assertTrue(getFixedDuration(minimalUnitDef1()).isNothing());
	}
	
	@Test 
	public void testGetElements() {
		assertThat(getElements(nestedUnitDef1()).size(), is(2));
		assertThat(getElements(minimalUnitDef1()).size(), is(0));
		
		final List<Element> pos = getElements(nestedUnitDef1());
		for(final Element p : pos){
			assertThat(p.getHorizontalPixel(), is(p.getUnit().getName().equals("XXXX0001") ? 80 : 240));
			assertThat(p.getVerticalPixel(), is(p.getUnit().getName().equals("XXXX0001") ? 48 : 144));
			assertThat(p.getX(), is(p.getUnit().getName().equals("XXXX0001") ? 0 : 1));
			assertThat(p.getY(), is(p.getUnit().getName().equals("XXXX0001") ? 0 : 1));
		}
	}
	
}
