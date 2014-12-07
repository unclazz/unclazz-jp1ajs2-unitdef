package com.m12i.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.util.OneIterator;

import static org.hamcrest.CoreMatchers.*;

public class OneIteratorTest {

	@Test
	public void hasNextは最初だけtrueを返す() {
		final Iterator<String> iter = new OneIterator<String>("hello");
		assertTrue(iter.hasNext());
		iter.next();
		assertFalse(iter.hasNext());
	}

	@Test
	public void nextは最初だけnull以外を返す() {
		final Iterator<String> iter = new OneIterator<String>("hello");
		assertThat(iter.next(), is("hello"));
		assertNull(iter.next());
	}

	@Test
	public void removeは何もしない() {
		final Iterator<String> iter = new OneIterator<String>("hello");
		iter.remove();
		assertTrue(iter.hasNext());
	}

}
