package com.m12i.jp1ajs2.unitdef;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.ZeroIterator;

public class ZeroIteratorTest {

	@Test
	public void hasNextはfalseを返す() {
		assertFalse(new ZeroIterator<String>().hasNext());
	}

	@Test
	public void nextはnullを返す() {
		assertNull(new ZeroIterator<String>().next());
	}

	@Test
	public void removeは何もしない() {
		new ZeroIterator<String>().remove();
	}

}
