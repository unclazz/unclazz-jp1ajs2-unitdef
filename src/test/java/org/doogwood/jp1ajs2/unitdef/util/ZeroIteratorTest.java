package org.doogwood.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;

import org.doogwood.jp1ajs2.unitdef.util.ZeroIterator;
import org.junit.Test;

public class ZeroIteratorTest {

	@Test
	public void testHasNext() {
		assertFalse(ZeroIterator.getInstance().hasNext());
	}

	@Test
	public void testNext() {
		assertNull(ZeroIterator.getInstance().next());
	}

	@Test
	public void testRemove() {
		ZeroIterator.getInstance().remove();
	}

}
