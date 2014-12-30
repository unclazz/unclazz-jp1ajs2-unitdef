package com.m12i.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.util.ZeroIterator;

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
