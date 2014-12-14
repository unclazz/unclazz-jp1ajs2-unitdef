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
	/**
	 * {@link Params#getIntValues(Unit, String)}のテスト.
	 */
	@Test 
	public void testGetIntValues() {
		Maybe<Integer> vs = Params.getIntValues(nestedUnitDef1(), "fd");
		assertThat(vs.get(0), is(360));
	}
	
	/**
	 * {@link Params#getStringValues(Unit, String)}のテスト.
	 */
	@Test
	public void testGetStringValues() {
		Maybe<String> vs = Params.getStringValues(nestedUnitDef1(), "el");
		for (final String v : vs) {
			v.startsWith("XXXX000");
		}
	}
	
	/**
	 * {@link Params#getAnteroposteriorRelationships(Unit)}のテスト.
	 */
	@Test
	public void testGetAnteroposteriorRelationships() {
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
	
	/**
	 * {@link Params#getFixedDuration(Unit)}のテスト.
	 */
	@Test
	public void testGetFixedDuration(){
		assertThat(getFixedDuration(nestedUnitDef1()).get(), is(360));
		assertTrue(getFixedDuration(minimalUnitDef1()).isNothing());
	}
	
	/**
	 * {@link Params#getElements(Unit)}のテスト.
	 */
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
	
	/**
	 * {@link Params#getAnteroposteriorRelationship(Param)}のテスト.
	 */
	@Test
	public void testGetAnteroposteriorRelationship() {
		// TODO
	}
	
	/**
	 * {@link Params#getElement(Param)}のテスト.
	 */
	@Test
	public void testGetElement() {
		// TODO
	}
	
	/**
	 * {@link Params#getEndDelayingTime(Param)}のテスト.
	 */
	@Test
	public void testGetEndDelayingTime() {
		// ey=[N,]hh:mm|{M|U|C}mmmm;
		
		// null
		final Param p0_0 = TestUtils.paramMockWithReturnValue("ey", "");
		final Param p0_1 = TestUtils.paramMockWithReturnValue("ey", "1,");
		final Param p0_2 = TestUtils.paramMockWithReturnValue("ey", ",12:12");
		final Param p0_3 = TestUtils.paramMockWithReturnValue("ey", "M1:01");
		assertNull(Params.getEndDelayingTime(p0_0));
		assertNull(Params.getEndDelayingTime(p0_1));
		assertNull(Params.getEndDelayingTime(p0_2));
		assertNull(Params.getEndDelayingTime(p0_3));
		
		// timingMethod
		final Param p1_0 = TestUtils.paramMockWithReturnValue("ey", "1:01");
		final Param p1_1 = TestUtils.paramMockWithReturnValue("ey", "M1");
		final Param p1_2 = TestUtils.paramMockWithReturnValue("ey", "U2");
		final Param p1_3 = TestUtils.paramMockWithReturnValue("ey", "C20");
		assertThat(Params.getEndDelayingTime(p1_0).getTimingMethod(), is(EndDelayingTime.TimingMethod.ABSOLUTE));
		assertThat(Params.getEndDelayingTime(p1_1).getTimingMethod(), is(EndDelayingTime.TimingMethod.RELATIVE_WITH_ROOT_START_TIME));
		assertThat(Params.getEndDelayingTime(p1_2).getTimingMethod(), is(EndDelayingTime.TimingMethod.RELATIVE_WITH_SUPER_START_TIME));
		assertThat(Params.getEndDelayingTime(p1_3).getTimingMethod(), is(EndDelayingTime.TimingMethod.RELATIVE_WITH_THEMSELF_START_TIME));
		
		// getHh, getMi
		final Param p2_0 = TestUtils.paramMockWithReturnValue("ey", "1:01");
		final Param p2_1 = TestUtils.paramMockWithReturnValue("ey", "M1");
		final Param p2_2 = TestUtils.paramMockWithReturnValue("ey", "U2");
		final Param p2_3 = TestUtils.paramMockWithReturnValue("ey", "C20");
		assertThat(Params.getEndDelayingTime(p2_0).getHh(), is(1));
		assertThat(Params.getEndDelayingTime(p2_0).getMi(), is(1));
		assertThat(Params.getEndDelayingTime(p2_1).getMi(), is(1));
		assertThat(Params.getEndDelayingTime(p2_2).getMi(), is(2));
		assertThat(Params.getEndDelayingTime(p2_3).getMi(), is(20));
		
		// getRuleNo
		final Param p3_0 = TestUtils.paramMockWithReturnValue("ey", "1:01");
		final Param p3_1 = TestUtils.paramMockWithReturnValue("ey", "M1");
		final Param p3_2 = TestUtils.paramMockWithReturnValue("ey", "0,U2");
		final Param p3_3 = TestUtils.paramMockWithReturnValue("ey", "1,C20");
		final Param p3_4 = TestUtils.paramMockWithReturnValue("ey", "2,1:01");
		assertThat(Params.getEndDelayingTime(p3_0).getRuleNo(), is(1));
		assertThat(Params.getEndDelayingTime(p3_1).getRuleNo(), is(1));
		assertThat(Params.getEndDelayingTime(p3_2).getRuleNo(), is(0));
		assertThat(Params.getEndDelayingTime(p3_3).getRuleNo(), is(1));
		assertThat(Params.getEndDelayingTime(p3_4).getRuleNo(), is(2));
	}
	
	/**
	 * {@link Params#getExecutionCycle(Param)}のテスト.
	 */
	@Test
	public void testGetExecutionCycle() {
		// TODO
	}
	
	/**
	 * {@link Params#getLinkedRule(Param)}のテスト.
	 */
	@Test
	public void testGetLinkedRule() {
		// TODO
	}
	
	/**
	 * {@link Params#getMapSize(Param)}のテスト.
	 */
	@Test
	public void testGetMapSize() {
		// TODO
	}
	
	/**
	 * {@link Params#getStartDate(Param)}のテスト.
	 */
	@Test
	public void testGetStartDate() {
		// TODO
	}
	
	/**
	 * {@link Params#getStartDelayingTime(Param)}のテスト.
	 */
	@Test
	public void testGetStartDelayingTime() {
		// sy=[N,]hh:mm|{M|U|C}mmmm;
		
		// null
		final Param p0_0 = TestUtils.paramMockWithReturnValue("sy", "");
		final Param p0_1 = TestUtils.paramMockWithReturnValue("sy", "1,");
		final Param p0_2 = TestUtils.paramMockWithReturnValue("sy", ",12:12");
		final Param p0_3 = TestUtils.paramMockWithReturnValue("sy", "M1:01");
		assertNull(Params.getStartDelayingTime(p0_0));
		assertNull(Params.getStartDelayingTime(p0_1));
		assertNull(Params.getStartDelayingTime(p0_2));
		assertNull(Params.getStartDelayingTime(p0_3));
		
		// timingMethod
		final Param p1_0 = TestUtils.paramMockWithReturnValue("sy", "1:01");
		final Param p1_1 = TestUtils.paramMockWithReturnValue("sy", "M1");
		final Param p1_2 = TestUtils.paramMockWithReturnValue("sy", "U2");
		final Param p1_3 = TestUtils.paramMockWithReturnValue("sy", "C20");
		assertThat(Params.getStartDelayingTime(p1_0).getTimingMethod(), is(StartDelayingTime.TimingMethod.ABSOLUTE));
		assertThat(Params.getStartDelayingTime(p1_1).getTimingMethod(), is(StartDelayingTime.TimingMethod.RELATIVE_WITH_ROOT_START_TIME));
		assertThat(Params.getStartDelayingTime(p1_2).getTimingMethod(), is(StartDelayingTime.TimingMethod.RELATIVE_WITH_SUPER_START_TIME));
		assertThat(Params.getStartDelayingTime(p1_3).getTimingMethod(), is(StartDelayingTime.TimingMethod.RELATIVE_WITH_THEMSELF_START_TIME));
		
		// getHh, getMi
		final Param p2_0 = TestUtils.paramMockWithReturnValue("sy", "1:01");
		final Param p2_1 = TestUtils.paramMockWithReturnValue("sy", "M1");
		final Param p2_2 = TestUtils.paramMockWithReturnValue("sy", "U2");
		final Param p2_3 = TestUtils.paramMockWithReturnValue("sy", "C20");
		assertThat(Params.getStartDelayingTime(p2_0).getHh(), is(1));
		assertThat(Params.getStartDelayingTime(p2_0).getMi(), is(1));
		assertThat(Params.getStartDelayingTime(p2_1).getMi(), is(1));
		assertThat(Params.getStartDelayingTime(p2_2).getMi(), is(2));
		assertThat(Params.getStartDelayingTime(p2_3).getMi(), is(20));
		
		// getRuleNo
		final Param p3_0 = TestUtils.paramMockWithReturnValue("sy", "1:01");
		final Param p3_1 = TestUtils.paramMockWithReturnValue("sy", "M1");
		final Param p3_2 = TestUtils.paramMockWithReturnValue("sy", "0,U2");
		final Param p3_3 = TestUtils.paramMockWithReturnValue("sy", "1,C20");
		final Param p3_4 = TestUtils.paramMockWithReturnValue("sy", "2,1:01");
		assertThat(Params.getStartDelayingTime(p3_0).getRuleNo(), is(1));
		assertThat(Params.getStartDelayingTime(p3_1).getRuleNo(), is(1));
		assertThat(Params.getStartDelayingTime(p3_2).getRuleNo(), is(0));
		assertThat(Params.getStartDelayingTime(p3_3).getRuleNo(), is(1));
		assertThat(Params.getStartDelayingTime(p3_4).getRuleNo(), is(2));
	}
	
	/**
	 * {@link Params#getStartTime(Param)}のテスト.
	 */
	@Test
	public void testGetStartTime() {
		// TODO
	}

}
