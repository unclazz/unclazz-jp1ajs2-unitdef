package org.doogwood.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.doogwood.jp1ajs2.unitdef.util.OneIterator;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class OneIteratorTest {

	@Test
	public void testHasNext() {
		final Iterator<String> iter = new OneIterator<String>("hello");
		assertTrue(iter.hasNext());
		iter.next();
		assertFalse(iter.hasNext());
	}

	@Test
	public void testNext() {
		final Iterator<String> iter = new OneIterator<String>("hello");
		assertThat(iter.next(), is("hello"));
		assertNull(iter.next());
	}

	@Test
	public void testRemove() {
		final Iterator<String> iter = new OneIterator<String>("hello");
		iter.remove();
		assertTrue(iter.hasNext());
	}

}
