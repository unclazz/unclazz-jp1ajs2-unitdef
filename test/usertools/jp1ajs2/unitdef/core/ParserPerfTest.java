package usertools.jp1ajs2.unitdef.core;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserPerfTest {

	public static long now() {
		return System.currentTimeMillis();
	}
	
	public static void printDelta(String msg, long start) {
		final long now = System.currentTimeMillis();
		System.out.println(msg + String.format(" (start: %d, end: %d, delta: %d)", start, now, now - start));
	}
	
	@Test
	public void v1Test() {
		final long start0 = now();
		final Unit u0 = ParseUtils.parse(TestUtils.jobnetUnitDefString3).right();
		assertTrue(u0 != null);
		printDelta("v1 parser [0]", start0);
		
		final long start1 = now();
		final Unit u1 = ParseUtils.parse(TestUtils.jobnetUnitDefString3).right();
		assertTrue(u1 != null);
		printDelta("v1 parser [1]", start1);
		
		final long start2 = now();
		final Unit u2 = ParseUtils.parse(TestUtils.jobnetUnitDefString3).right();
		assertTrue(u2 != null);
		printDelta("v1 parser [2]", start2);
		
		final long start3 = now();
		final Unit u3 = ParseUtils.parse(TestUtils.jobnetUnitDefString3).right();
		assertTrue(u3 != null);
		printDelta("v2 parser [3]", start3);
		
		final long start4 = now();
		final Unit u4 = ParseUtils.parse(TestUtils.jobnetUnitDefString3).right();
		assertTrue(u4 != null);
		printDelta("v2 parser [4]", start4);
	}
	
	@Test
	public void v2Test() {
		final long start0 = now();
		final Unit u0 = ParseUtils2.parse(TestUtils.jobnetUnitDefString3).right();
		assertTrue(u0 != null);
		printDelta("v2 parser [0]", start0);
		
		final long start1 = now();
		final Unit u1 = ParseUtils2.parse(TestUtils.jobnetUnitDefString3).right();
		assertTrue(u1 != null);
		printDelta("v2 parser [1]", start1);
		
		final long start2 = now();
		final Unit u2 = ParseUtils2.parse(TestUtils.jobnetUnitDefString3).right();
		assertTrue(u2 != null);
		printDelta("v2 parser [2]", start2);
		
		final long start3 = now();
		final Unit u3 = ParseUtils2.parse(TestUtils.jobnetUnitDefString3).right();
		assertTrue(u3 != null);
		printDelta("v2 parser [3]", start3);
		
		final long start4 = now();
		final Unit u4 = ParseUtils2.parse(TestUtils.jobnetUnitDefString3).right();
		assertTrue(u4 != null);
		printDelta("v2 parser [4]", start4);
	}

}
