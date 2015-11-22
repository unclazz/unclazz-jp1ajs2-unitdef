package org.unclazz.jp1ajs2.unitdef.parser;


import static org.junit.Assert.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.parser.Input;
import org.unclazz.jp1ajs2.unitdef.parser.InputExeption;
import org.unclazz.jp1ajs2.unitdef.parser.ParseException;
import org.unclazz.jp1ajs2.unitdef.parser.ParseHelper;
import org.unclazz.jp1ajs2.unitdef.parser.ParseOptions;

import static org.hamcrest.CoreMatchers.is;

public class ParseHelperTest {

	private static final ParseHelper p0 = new ParseHelper();
	private static final ParseHelper p1;
	static {
		final ParseOptions o1 = new ParseOptions();
		o1.setEscapePrefixInDoubleQuotes('"');
		o1.setEscapePrefixInSingleQuotes('\'');
		p1 = new ParseHelper(o1);
	}
	
	@Test
	public void parseAbcTest00() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc def ghi");
		final String r0 = p0.parseAbc(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc"));
		assertThat(i0.columnNumber(), is(4));
		
	}
	
	@Test
	public void parseAbcTest01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString(" abc def ghi");
		final String r0 = p0.parseAbc(i0);
		assertThat(r0.length() == 0, is(true));
		assertThat(r0, is(""));
		assertThat(i0.columnNumber(), is(1));
		
	}
	
	@Test
	public void parseAbcTest02() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc123 def ghi");
		final String r0 = p0.parseAbc(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc"));
		assertThat(i0.columnNumber(), is(4));
		
	}
	
	@Test
	public void parseAbcTest03() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc_$123 def ghi");
		final String r0 = p0.parseAbc(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc"));
		assertThat(i0.columnNumber(), is(4));
		
	}
	
	@Test
	public void parseAbc123Test00() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc123 def ghi");
		final String r0 = p0.parseAbc123(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123"));
		assertThat(i0.columnNumber(), is(7));
		
	}
	
	@Test
	public void parseAbc123Test01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString(" abc def ghi");
		final String r0 = p0.parseAbc123(i0);
		assertThat(r0.length() == 0, is(true));
		assertThat(r0.length() > 0, is(false));
		assertThat(r0, is(""));
		assertThat(i0.columnNumber(), is(1));
		
	}
	
	@Test
	public void parseAbc123Test02() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123abc def ghi");
		final String r0 = p0.parseAbc123(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("123abc"));
		assertThat(i0.columnNumber(), is(7));
		
	}
	
	@Test
	public void parseAbc123Test03() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc123_$ def ghi");
		final String r0 = p0.parseAbc123(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123"));
		assertThat(i0.columnNumber(), is(7));
		
	}
	
	@Test
	public void parseAbc123_$Test00() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc123_$ def ghi");
		final String r0 = p0.parseAbc123_$(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123_$"));
		assertThat(i0.columnNumber(), is(9));
		
	}
	
	@Test
	public void parseAbc123_$Test01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString(" abc def ghi");
		final String r0 = p0.parseAbc123_$(i0);
		assertThat(r0.length(), is(0));
		assertThat(i0.columnNumber(), is(1));
		
	}
	
	@Test
	public void parseAbc123_$Test02() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123_$abc def ghi");
		final String r0 = p0.parseAbc123_$(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("123_$abc"));
		assertThat(i0.columnNumber(), is(9));
		
	}
	
	@Test
	public void parseAbc123_$Test03() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("_$abc123 def ghi");
		final String r0 = p0.parseAbc123_$(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("_$abc123"));
		assertThat(i0.columnNumber(), is(9));
		
	}

	@Test
	public void parseQuotedStringTest00() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc def ghi");
		try {
			@SuppressWarnings("unused")
			final String r0 = p0.parseQuotedString(i0);
			fail();
		} catch (final Exception e) {
			// OK
		}
		assertThat(i0.columnNumber(), is(1));
		
	}

	@Test
	public void parseQuotedStringTest01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("\"\"abc def ghi");
		final String r0 = p0.parseQuotedString(i0);
		assertThat(r0.length(), is(0));
		assertThat(i0.columnNumber(), is(3));
		
	}

	@Test
	public void parseQuotedStringTest02() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("\"abc def\" ghi");
		final String r0 = p0.parseQuotedString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc def"));
		assertThat(i0.columnNumber(), is(10));
	}

	@Test
	public void parseQuotedStringTest04() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("'''abc ''def''' ghi");
		final String r0 = p1.parseQuotedString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("'abc 'def'"));
		assertThat(i0.columnNumber(), is(16));
	}
	
	@Test
	public void parseQuotedStringTest03() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("\"\\\"abc \\\"def\\\"\" ghi");
		final String r0 = p0.parseQuotedString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("\"abc \"def\""));
		assertThat(i0.columnNumber(), is(16));
	}
	
	@Test
	public void parseRawStringTest00() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc def ghi");
		final String r0 = p0.parseRawString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc"));
		assertThat(i0.columnNumber(), is(4));
	}

	@Test
	public void parseRawStringTest01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString(" abc def ghi");
		final String r0 = p0.parseRawString(i0);
		assertThat(r0.length(), is(0));
		assertThat(i0.columnNumber(), is(1));
	}

	@Test
	public void parseRawStringTest02() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc123_$ def ghi");
		final String r0 = p0.parseRawString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123_$"));
		assertThat(i0.columnNumber(), is(9));
	}

	@Test
	public void parseRawStringTest03() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc123_$-@*:;>< def ghi");
		final String r0 = p0.parseRawString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123_$-@*:;><"));
		assertThat(i0.columnNumber(), is(r0.length() + 1));
	}
	
	@Test
	public void parseUntilTest00() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc123 def ghi");
		final String r0 = p0.parseUntil(i0, 'f', 'g');
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123 de"));
		assertThat(i0.columnNumber(), is(r0.length() + 1));
		
	}
	
	@Test
	public void parseUntilTest01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc123 ghi def");
		final String r0 = p0.parseUntil(i0, 'f', 'g');
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123 "));
		assertThat(i0.columnNumber(), is(r0.length() + 1));
		
	}
	
	@Test
	public void parseUntilTest02() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc123 ghi def");
		final String r0 = p0.parseUntil(i0, 'x');
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123 ghi def"));
		assertThat(i0.reachedEOF(), is(true));
	}
	
	@Test
	public void parseNumberTest00() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("abc def ghi");
		try {
			@SuppressWarnings("unused")
			final Double r0 = p0.parseNumber(i0);
			fail();
		} catch (final Exception e) {
			// OK.
		}
		assertThat(i0.columnNumber(), is(1));
		
	}
	
	@Test
	public void parseNumberTest01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123abc def ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123d));
		assertThat(i0.columnNumber(), is(4));
		
	}
	
	@Test
	public void parseNumberTest02() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123.456abc def ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123.456d));
		assertThat(i0.columnNumber(), is(8));
		
	}
	
	@Test
	public void parseNumberTest03() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("+123abc def ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123d));
		assertThat(i0.columnNumber(), is(5));
		
	}
	
	@Test
	public void parseNumberTest04() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("-123abc def ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(-123d));
		assertThat(i0.columnNumber(), is(5));
		
	}
	
	@Test
	public void parseNumberTest05() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123.456ef ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123.456d));
		assertThat(i0.columnNumber(), is(8));
		
	}
	
	@Test
	public void parseNumberTest06() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123.456e+1f ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123.456e+1d));
		assertThat(i0.columnNumber(), is(11));
		
	}
	
	@Test
	public void parseNumberTest07() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123.456e-1f ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123.456e-1d));
		assertThat(i0.columnNumber(), is(11));
		
	}
	
	@Test
	public void parseNumberTest08() throws InputExeption, ParseException {
		final Input i0 = Input.fromString(".456e-10f ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(.456e-10d));
		assertThat(i0.columnNumber(), is(9));
		
	}
	
	@Test
	public void parseNumberTest09() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123.e-10f ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123.e-10d));
		assertThat(i0.columnNumber(), is(9));
		
	}
	
	@Test
	public void skipCommentTest00() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123.e-10f ghi");
		p0.skipComment(i0);
		assertThat(i0.columnNumber(), is(1));
	}

	@Test
	public void skipCommentTest01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("//123.e-10f ghi");
		p0.skipComment(i0);
		assertThat(i0.reachedEOF(), is(true));
	}

	@Test
	public void skipCommentTest02() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("//123\r\n.e-10f ghi");
		p0.skipComment(i0);
		assertThat(i0.reachedEOF(), is(false));
		assertThat(i0.current(), is('.'));
	}

	@Test
	public void skipCommentTest03() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("/*123*/.e-10f ghi");
		p0.skipComment(i0);
		assertThat(i0.reachedEOF(), is(false));
		assertThat(i0.current(), is('.'));
	}

	@Test
	public void skipCommentTest04() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("/*123\r\n456*/ def ghi");
		p0.skipComment(i0);
		assertThat(i0.reachedEOF(), is(false));
		assertThat(i0.current(), is(' '));
	}

	@Test
	public void skipWordTest01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123456 def ghi");
		p0.skipWord(i0, "123");
		assertThat(i0.reachedEOF(), is(false));
		assertThat(i0.current(), is('4'));
	}

	@Test
	public void skipWordTest02() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123456 def ghi");
		i0.next();
		try {
			p0.skipWord(i0, "123");
			fail();
		} catch (final Exception e) {
			// OK
		}
		assertThat(i0.reachedEOF(), is(false));
		assertThat(i0.current(), is('2'));
	}
	
	@Test
	public void checkTest00() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123 456 def ghi");
		p0.check(i0, '1');
		assertTrue(true);
	}
	
	@Test
	public void checkTest01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123 456 def ghi");
		try {
			p0.check(i0, '2');
			fail();
		} catch (Exception e) {
			// Do nothing.
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void checkWordTest00() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123 456 def ghi");
		p0.checkWord(i0, "123");
		assertTrue(true);
	}
	
	@Test
	public void checkWordTest01() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123 456 def ghi");
		try {
			p0.checkWord(i0, "23");
			fail();
		} catch (Exception e) {
			// Do nothing.
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void checkWordTest02() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123\r\n 456 def ghi");
		p0.checkWord(i0, "123");
		assertTrue(true);
	}
	
	@Test
	public void checkWordTest03() throws InputExeption, ParseException {
		final Input i0 = Input.fromString("123\r\n 456 def ghi");
		try {
			p0.checkWord(i0, "123\r");
		} catch (Exception e) {
			fail();
		}
	}

}
