package com.m12i.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.util.ZeroIterator;

public class ZeroIteratorTest {

	@Test
	public void hasNextはfalseを返す() {
		assertFalse(ZeroIterator.getInstance().hasNext());
	}

	@Test
	public void nextはnullを返す() {
		assertNull(ZeroIterator.getInstance().next());
	}

	@Test
	public void removeは何もしない() {
		ZeroIterator.getInstance().remove();
	}

}
