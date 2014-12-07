package com.m12i.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.util.Option;

public class OptionTest {

	@Test
	public void 静的メソッドnoneはNoneを返す() {
		assertTrue(Option.none().isNone());
		assertTrue(Option.none().equals(Option.NONE));
	}

	@Test
	public void 静的メソッドsomeは第1引数をラップしたSomeを返す() {
		assertTrue(Option.some("hello").isSome());
		assertTrue(Option.some("hello").get().equals("hello"));
	}

	@Test
	public void 静的メソッドwrapは第1引数に応じてSomeもしくはNoneを返す() {
		assertTrue(Option.wrap(null).isNone());
		assertTrue(Option.wrap("hello").isSome());
		assertTrue(Option.wrap("hello").get().equals("hello"));
	}

}
