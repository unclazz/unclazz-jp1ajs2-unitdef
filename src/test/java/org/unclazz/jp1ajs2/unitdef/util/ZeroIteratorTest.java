package org.unclazz.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;

import java.util.NoSuchElementException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.util.ZeroIterator;

public class ZeroIteratorTest {
	
	@Rule
	public final ExpectedException expected = ExpectedException.none();

	@Test
	public void hasNext_always_returnsFalse() {
		assertFalse(ZeroIterator.getInstance().hasNext());
	}

	@Test
	public void next_always_throwsException() {
		expected.expect(NoSuchElementException.class);
		ZeroIterator.getInstance().next();
	}

	@Test
	public void remove_always_throwsException() {
		expected.expect(UnsupportedOperationException.class);
		ZeroIterator.getInstance().remove();
	}

}
