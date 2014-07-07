package usertools.jp1ajs2.unitdef.core;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.code.parse.Parsable;
import com.m12i.code.parse.ParseException;

import static org.hamcrest.CoreMatchers.*;
import static usertools.jp1ajs2.unitdef.core.TestUtils.*;

public class ParsableTest {

	/**
	 * {@value}
	 */
	private static final String alphabetString1 = "a";

	/**
	 * {@value}
	 */
	private static final String alphabetString2 = "abc";

	/**
	 * {@value}
	 */
	private static final String alphabetString3 = " abc def\r\nghi  jkl ";

	@Test
	public void currentはEOF到達後にはNULL文字を返す() {
		final Parsable emptyCode = createCode("");
		assertThat(emptyCode.current(), is('\u0000'));
		
		final Parsable alphabetCode1 = createCode(alphabetString1);
		assertTrue(alphabetCode1.current() == 'a');
		alphabetCode1.next();
		assertTrue(alphabetCode1.current() == '\u0000');
	}
	
	@Test
	public void currentは初期状態では1文字目を返す() {
		final Parsable alphabetCode2 = createCode(alphabetString2);
		assertTrue(alphabetCode2.current() == 'a');
	}
	
	@Test
	public void currentはnextで前進後の現在位置の文字を返す() {
		final Parsable alphabetCode2 = createCode(alphabetString2);
		alphabetCode2.next();
		assertTrue(alphabetCode2.current() == 'b');
	}
	
	@Test
	public void columnNoは初期状態では1を返す() throws ParseException {
		final Parsable alphabetCode1 = createCode(alphabetString1);
		assertTrue(alphabetCode1.columnNo() == 1);
	}
	
	@Test
	public void columnNoはEOF到達後には1を返す() {
		final Parsable emptyCode = createCode("");
		assertTrue(emptyCode.columnNo() == 1);
		
		final Parsable alphabetCode1 = createCode(alphabetString1);
		assertTrue(alphabetCode1.columnNo() == 1);
		alphabetCode1.next(); // \u0000
		assertTrue(alphabetCode1.columnNo() == 1);
	}
	
	@Test
	public void columnNoは現在文字が各行の中で何文字目にあるかを返す() {
		final Parsable alphabetCode1 = createCode(alphabetString3);
		assertSame(1, alphabetCode1.columnNo());
		alphabetCode1.next(); // a
		alphabetCode1.next(); // b
		alphabetCode1.next(); // c
		alphabetCode1.next(); // _
		alphabetCode1.next(); // d
		alphabetCode1.next(); // e
		assertSame(7, alphabetCode1.columnNo());
		alphabetCode1.next(); // f
		assertSame(8, alphabetCode1.columnNo());
		alphabetCode1.next(); // \r
		alphabetCode1.next(); // \n
		alphabetCode1.next(); // g
		assertSame(1, alphabetCode1.columnNo());
	}
	
	@Test
	public void lineはEOF到達後にはNULLを返す() {
		final Parsable emptyCode = createCode("");
		assertNull(emptyCode.line());
		
		final Parsable alphabetCode1 = createCode(alphabetString1);
		alphabetCode1.next(); // \u0000
		assertNull(alphabetCode1.line());
	}
	
	@Test
	public void lineは初期状態では1行目を返す() {
		final Parsable alphabetCode1 = createCode(alphabetString1);
		assertTrue("a".equals(alphabetCode1.line()));
		alphabetCode1.next(); // \u0000
		assertNull(alphabetCode1.line());
	}
	
	@Test
	public void lineは現在文字が各行目にあるかを返す() {
		final Parsable alphabetCode1 = createCode(alphabetString3);
		assertEquals(" abc def", alphabetCode1.line());
		alphabetCode1.next(); // a
		alphabetCode1.next(); // b
		alphabetCode1.next(); // c
		alphabetCode1.next(); // _
		alphabetCode1.next(); // d
		alphabetCode1.next(); // e
		assertEquals(" abc def", alphabetCode1.line());
		alphabetCode1.next(); // f
		assertEquals(" abc def", alphabetCode1.line());
		alphabetCode1.next(); // \r
		alphabetCode1.next(); // \n
		alphabetCode1.next(); // g
		assertEquals("ghi  jkl ", alphabetCode1.line());
	}
	
	@Test
	public void nextはEOF到達後にはNULL文字を返す() {
		final Parsable emptyCode = createCode("");
		assertSame('\u0000', emptyCode.next());		
	}

	@Test
	public void nextは現在位置を1文字前進させてその位置の文字返す() {
		final Parsable alphabetCode1 = createCode(alphabetString2);
		assertTrue(alphabetCode1.next() == 'b');
	}
}
