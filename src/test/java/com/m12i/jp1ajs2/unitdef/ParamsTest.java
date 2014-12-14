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

import com.m12i.jp1ajs2.unitdef.ExecutionCycle.CycleUnit;
import com.m12i.jp1ajs2.unitdef.StartDate.DesignationMethod;
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
		//ar=(f=先行ユニット名,t=後続ユニット名[,接続種別]);

		final Param p0_0 = TestUtils.paramMockWithReturnValue("ar", "(f=MOCK1000,t=MOCK1001)");
		final Param p0_1 = TestUtils.paramMockWithReturnValue("ar", "(f=MOCK1000,t=MOCK1001,seq)");
		final Param p0_2 = TestUtils.paramMockWithReturnValue("ar", "(f=MOCK1000,t=MOCK1001,con)");
		
		assertThat(Params.getAnteroposteriorRelationship(p0_0).getFrom().getName(), is("MOCK1000"));
		assertThat(Params.getAnteroposteriorRelationship(p0_0).getTo().getName(), is("MOCK1001"));
		assertThat(Params.getAnteroposteriorRelationship(p0_0).getType(), is(UnitConnectionType.SEQUENTIAL));
		assertThat(Params.getAnteroposteriorRelationship(p0_1).getType(), is(UnitConnectionType.SEQUENTIAL));
		assertThat(Params.getAnteroposteriorRelationship(p0_2).getType(), is(UnitConnectionType.CONDITIONAL));
	}
	
	/**
	 * {@link Params#getElement(Param)}のテスト.
	 */
	@Test
	public void testGetElement() {
		// el=ユニット名,ユニット種別,+H +V;
		
		final Param p0_0 = TestUtils.paramMockWithReturnValue("el", "MOCK1000,g,+400 +336");
		assertThat(Params.getElement(p0_0).getHorizontalPixel(), is(400));
		assertThat(Params.getElement(p0_0).getVerticalPixel(), is(336));
		assertThat(Params.getElement(p0_0).getX(), is(2));
		assertThat(Params.getElement(p0_0).getY(), is(3));
		assertThat(Params.getElement(p0_0).getUnit().getName(), is("MOCK1000"));
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
		final Param p0_2 = TestUtils.paramMockWithReturnValue("ey", "-1,12:12");
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
		// cy=[N,](n,{y|m|w|d});
		
		// null
		final Param p0_0 = TestUtils.paramMockWithReturnValue("cy", "(1,)");
		final Param p0_1 = TestUtils.paramMockWithReturnValue("cy", "(2,0)");
		final Param p0_2 = TestUtils.paramMockWithReturnValue("cy", "0,(0,)");
		final Param p0_3 = TestUtils.paramMockWithReturnValue("cy", "1,(1,0)");
		assertNull(Params.getExecutionCycle(p0_0));
		assertNull(Params.getExecutionCycle(p0_1));
		assertNull(Params.getExecutionCycle(p0_2));
		assertNull(Params.getExecutionCycle(p0_3));
		
		// getCycleUnit
		final Param p1_0 = TestUtils.paramMockWithReturnValue("cy", "(1,y)");
		final Param p1_1 = TestUtils.paramMockWithReturnValue("cy", "(20,m)");
		final Param p1_2 = TestUtils.paramMockWithReturnValue("cy", "0,(0,w)");
		final Param p1_3 = TestUtils.paramMockWithReturnValue("cy", "10,(1,d)");
		assertThat(Params.getExecutionCycle(p1_0).getCycleUnit(), is(CycleUnit.YEAR));
		assertThat(Params.getExecutionCycle(p1_1).getCycleUnit(), is(CycleUnit.MONTH));
		assertThat(Params.getExecutionCycle(p1_2).getCycleUnit(), is(CycleUnit.WEEK));
		assertThat(Params.getExecutionCycle(p1_3).getCycleUnit(), is(CycleUnit.DAY));
		
		// getInterval
		assertThat(Params.getExecutionCycle(p1_0).getInterval(), is(1));
		assertThat(Params.getExecutionCycle(p1_1).getInterval(), is(20));
		assertThat(Params.getExecutionCycle(p1_2).getInterval(), is(0));
		assertThat(Params.getExecutionCycle(p1_3).getInterval(), is(1));
		
		// getRuleNo
		assertThat(Params.getExecutionCycle(p1_0).getRuleNo(), is(1));
		assertThat(Params.getExecutionCycle(p1_1).getRuleNo(), is(1));
		assertThat(Params.getExecutionCycle(p1_2).getRuleNo(), is(0));
		assertThat(Params.getExecutionCycle(p1_3).getRuleNo(), is(10));
	}
	
	/**
	 * {@link Params#getLinkedRule(Param)}のテスト.
	 */
	@Test
	public void testGetLinkedRule() {
		// ln=[N,]n;
		
		// null
		final Param p0_0 = TestUtils.paramMockWithReturnValue("ln", "-1");
		final Param p0_1 = TestUtils.paramMockWithReturnValue("ln", "1,2,3");
		assertNull(Params.getLinkedRule(p0_0));
		assertNull(Params.getLinkedRule(p0_1));
		
		final Param p1_0 = TestUtils.paramMockWithReturnValue("ln", "0");
		final Param p1_1 = TestUtils.paramMockWithReturnValue("ln", "1");
		final Param p1_2 = TestUtils.paramMockWithReturnValue("ln", "10");
		final Param p1_3 = TestUtils.paramMockWithReturnValue("ln", "0,1");
		final Param p1_4 = TestUtils.paramMockWithReturnValue("ln", "1,10");
		final Param p1_5 = TestUtils.paramMockWithReturnValue("ln", "20,10");
		
		// getLinkedRuleNo
		assertThat(Params.getLinkedRule(p1_0).getLinkedRuleNo(), is(0));
		assertThat(Params.getLinkedRule(p1_1).getLinkedRuleNo(), is(1));
		assertThat(Params.getLinkedRule(p1_2).getLinkedRuleNo(), is(10));
		assertThat(Params.getLinkedRule(p1_3).getLinkedRuleNo(), is(1));
		assertThat(Params.getLinkedRule(p1_4).getLinkedRuleNo(), is(10));
		assertThat(Params.getLinkedRule(p1_5).getLinkedRuleNo(), is(10));
		
		// getLinkedRule
		assertThat(Params.getLinkedRule(p1_0).getRuleNo(), is(1));
		assertThat(Params.getLinkedRule(p1_1).getRuleNo(), is(1));
		assertThat(Params.getLinkedRule(p1_2).getRuleNo(), is(1));
		assertThat(Params.getLinkedRule(p1_3).getRuleNo(), is(0));
		assertThat(Params.getLinkedRule(p1_4).getRuleNo(), is(1));
		assertThat(Params.getLinkedRule(p1_5).getRuleNo(), is(20));
	}
	
	/**
	 * {@link Params#getMapSize(Param)}のテスト.
	 */
	@Test
	public void testGetMapSize() {
		// sz=横アイコン数×縦アイコン数;
		
		final Param p0_0 = TestUtils.paramMockWithReturnValue("sz", "A×B");
		final Param p0_1 = TestUtils.paramMockWithReturnValue("sz", "1×");
		final Param p0_2 = TestUtils.paramMockWithReturnValue("sz", "×1");
		assertNull(Params.getMapSize(p0_0));
		assertNull(Params.getMapSize(p0_1));
		assertNull(Params.getMapSize(p0_2));
		
		final Param p1_0 = TestUtils.paramMockWithReturnValue("sz", "0×1");
		final Param p1_1 = TestUtils.paramMockWithReturnValue("sz", "1×0");
		final Param p1_2 = TestUtils.paramMockWithReturnValue("sz", "10×20");
		
		assertThat(Params.getMapSize(p1_0).getHeight(), is(1));
		assertThat(Params.getMapSize(p1_1).getHeight(), is(0));
		assertThat(Params.getMapSize(p1_2).getHeight(), is(20));
		
		assertThat(Params.getMapSize(p1_0).getWidth(), is(0));
		assertThat(Params.getMapSize(p1_1).getWidth(), is(1));
		assertThat(Params.getMapSize(p1_2).getWidth(), is(10));
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
		final Param p0_2 = TestUtils.paramMockWithReturnValue("sy", "-1,12:12");
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
	 * {@link Params#getStartDate(Param)}のテスト.
	 */
	@Test
	public void testGetStartDate() {
		// sd=[N,]ud;
		// sd=[N,]en;
		// sd=[N,][[yyyy/]mm/][+|*|@]dd;
		// sd=[N,][[yyyy/]mm/][+|*|@]b[-DD];
		// sd=[N,][[yyyy/]mm/][+]{su|mo|tu|we|th|fr|sa} [:{n|b}]};
		
		final Param p0_0 = TestUtils.paramMockWithReturnValue("sd", "ud");
		final Param p0_1 = TestUtils.paramMockWithReturnValue("sd", "0,ud");
		assertThat(Params.getStartDate(p0_0).getRuleNo(), is(0));
		assertThat(Params.getStartDate(p0_0).getDesignationMethod(), is(DesignationMethod.UNDEFINED));
		assertThat(Params.getStartDate(p0_1).getRuleNo(), is(0));
		assertThat(Params.getStartDate(p0_1).getDesignationMethod(), is(DesignationMethod.UNDEFINED));
		
		final Param p1_0 = TestUtils.paramMockWithReturnValue("sd", "en");
		final Param p1_1 = TestUtils.paramMockWithReturnValue("sd", "0,en");
		final Param p1_2 = TestUtils.paramMockWithReturnValue("sd", "1,en");
		assertThat(Params.getStartDate(p1_0).getRuleNo(), is(1));
		assertThat(Params.getStartDate(p1_0).getDesignationMethod(), is(DesignationMethod.ENTRY_DATE));
		assertThat(Params.getStartDate(p1_1).getRuleNo(), is(0));
		assertThat(Params.getStartDate(p1_1).getDesignationMethod(), is(DesignationMethod.ENTRY_DATE));
		assertThat(Params.getStartDate(p1_2).getRuleNo(), is(1));
		assertThat(Params.getStartDate(p1_2).getDesignationMethod(), is(DesignationMethod.ENTRY_DATE));

		final StartDate sd2_0 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2014/12/14"));
		final StartDate sd2_1 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "12/14"));
		final StartDate sd2_2 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "14"));
		assertThat(sd2_0.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd2_1.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd2_2.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd2_0.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH));
		assertThat(sd2_1.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH));
		assertThat(sd2_2.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH));
		assertThat(sd2_0.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd2_1.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd2_2.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd2_0.getDd(), is(14));
		assertThat(sd2_1.getDd(), is(14));
		assertThat(sd2_2.getDd(), is(14));
		assertThat(sd2_0.getMm(), is(12));
		assertThat(sd2_1.getMm(), is(12));
		assertNull(sd2_2.getMm());
		assertThat(sd2_0.getYyyy(), is(2014));
		assertNull(sd2_1.getYyyy());
		assertNull(sd2_2.getYyyy());
		assertThat(sd2_0.getRuleNo(), is(1));
		assertThat(sd2_1.getRuleNo(), is(1));
		assertThat(sd2_2.getRuleNo(), is(1));

		final StartDate sd2_3 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2,2014/12/14"));
		final StartDate sd2_4 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "20,12/14"));
		final StartDate sd2_5 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "200,14"));
		assertThat(sd2_3.getRuleNo(), is(2));
		assertThat(sd2_4.getRuleNo(), is(20));
		assertThat(sd2_5.getRuleNo(), is(200));

		final StartDate sd3_0 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2014/12/+14"));
		final StartDate sd3_1 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "12/*14"));
		final StartDate sd3_2 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "@14"));
		assertThat(sd3_0.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd3_1.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd3_2.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd3_0.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH));
		assertThat(sd3_1.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH));
		assertThat(sd3_2.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH));
		assertThat(sd3_0.getCountingMethod(), is(StartDate.CountingMethod.RELATIVE));
		assertThat(sd3_1.getCountingMethod(), is(StartDate.CountingMethod.BUSINESS_DAY));
		assertThat(sd3_2.getCountingMethod(), is(StartDate.CountingMethod.NON_BUSINESS_DAY));

		final StartDate sd3_3 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "20, 2014/12/+14"));
		final StartDate sd3_4 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "200,  12/*14"));
		final StartDate sd3_5 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2,@14"));
		assertThat(sd3_3.getRuleNo(), is(20));
		assertThat(sd3_4.getRuleNo(), is(200));
		assertThat(sd3_5.getRuleNo(), is(2));

		final StartDate sd4_0 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2014/12/+b"));
		final StartDate sd4_1 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "12/*b-14"));
		final StartDate sd4_2 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "@b-0"));
		assertThat(sd4_0.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd4_1.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd4_2.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd4_0.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH_INVERSELY));
		assertThat(sd4_1.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH_INVERSELY));
		assertThat(sd4_2.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH_INVERSELY));
		assertThat(sd4_0.getCountingMethod(), is(StartDate.CountingMethod.RELATIVE));
		assertThat(sd4_1.getCountingMethod(), is(StartDate.CountingMethod.BUSINESS_DAY));
		assertThat(sd4_2.getCountingMethod(), is(StartDate.CountingMethod.NON_BUSINESS_DAY));
		assertNull(sd4_0.getDd());
		assertThat(sd4_1.getDd(), is(14));
		assertThat(sd4_2.getDd(), is(0));
		
		final StartDate sd4_3 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2014/12/b"));
		final StartDate sd4_4 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "12/b-14"));
		final StartDate sd4_5 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "b-0"));
		assertThat(sd4_3.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd4_4.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd4_5.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd4_3.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH_INVERSELY));
		assertThat(sd4_4.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH_INVERSELY));
		assertThat(sd4_5.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_MONTH_INVERSELY));
		assertThat(sd4_3.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd4_4.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd4_5.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));

		final StartDate sd5_0 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2014/12/su"));
		final StartDate sd5_1 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "12/mo"));
		final StartDate sd5_2 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "mo"));
		final StartDate sd5_3 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "mo :b"));
		final StartDate sd5_4 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "mo:b"));
		final StartDate sd5_5 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2014/12/+su"));
		final StartDate sd5_6 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2014/12/su :1"));
		final StartDate sd5_7 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2014/12/+su :2"));
		final StartDate sd5_8 = Params.getStartDate(TestUtils.paramMockWithReturnValue("sd", "2014/12/+su:2"));
		assertThat(sd5_0.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd5_1.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd5_2.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd5_3.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd5_4.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd5_5.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd5_6.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd5_7.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd5_8.getDesignationMethod(), is(DesignationMethod.SCHEDULED_DATE));
		assertThat(sd5_0.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_WEEK));
		assertThat(sd5_1.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_WEEK));
		assertThat(sd5_2.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_WEEK));
		assertThat(sd5_3.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_LAST_WEEK));
		assertThat(sd5_4.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_LAST_WEEK));
		assertThat(sd5_5.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_WEEK));
		assertThat(sd5_6.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_WEEK));
		assertThat(sd5_7.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_WEEK));
		assertThat(sd5_8.getTimingMethod(), is(StartDate.TimingMethod.DAY_OF_WEEK));
		assertThat(sd5_0.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd5_1.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd5_2.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd5_3.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd5_4.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd5_5.getCountingMethod(), is(StartDate.CountingMethod.RELATIVE));
		assertThat(sd5_6.getCountingMethod(), is(StartDate.CountingMethod.ABSOLUTE));
		assertThat(sd5_7.getCountingMethod(), is(StartDate.CountingMethod.RELATIVE));
		assertThat(sd5_8.getCountingMethod(), is(StartDate.CountingMethod.RELATIVE));
		assertNull(sd5_0.getWeekNo());
		assertNull(sd5_1.getWeekNo());
		assertNull(sd5_2.getWeekNo());
		assertNull(sd5_3.getWeekNo());
		assertNull(sd5_4.getWeekNo());
		assertNull(sd5_5.getWeekNo());
		assertThat(sd5_6.getWeekNo(), is(Integer.valueOf(1)));
		assertThat(sd5_7.getWeekNo(), is(Integer.valueOf(2)));
		assertThat(sd5_8.getWeekNo(), is(Integer.valueOf(2)));
	}
	
	/**
	 * {@link Params#getStartTime(Param)}のテスト.
	 */
	@Test
	public void testGetStartTime() {
		// st=[N,][+]hh:mm;
		
		final StartTime st0_0 = Params.getStartTime(TestUtils.paramMockWithReturnValue("st", "01:11"));
		final StartTime st0_1 = Params.getStartTime(TestUtils.paramMockWithReturnValue("st", "+11:01"));
		final StartTime st0_2 = Params.getStartTime(TestUtils.paramMockWithReturnValue("st", "2,11:1"));
		final StartTime st0_3 = Params.getStartTime(TestUtils.paramMockWithReturnValue("st", "20,+01:11"));
		
		assertThat(st0_0.getHh(), is(1));
		assertThat(st0_1.getHh(), is(11));
		assertThat(st0_2.getHh(), is(11));
		assertThat(st0_3.getHh(), is(1));
		
		assertThat(st0_0.getMi(), is(11));
		assertThat(st0_1.getMi(), is(1));
		assertThat(st0_2.getMi(), is(1));
		assertThat(st0_3.getMi(), is(11));
		
		assertThat(st0_0.getRuleNo(), is(1));
		assertThat(st0_1.getRuleNo(), is(1));
		assertThat(st0_2.getRuleNo(), is(2));
		assertThat(st0_3.getRuleNo(), is(20));
		
		assertThat(st0_0.isRelative(), is(false));
		assertThat(st0_1.isRelative(), is(true));
		assertThat(st0_2.isRelative(), is(false));
		assertThat(st0_3.isRelative(), is(true));
	}

}
