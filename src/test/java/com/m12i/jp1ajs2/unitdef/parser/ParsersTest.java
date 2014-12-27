package com.m12i.jp1ajs2.unitdef.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.parser.Parsers;
import com.m12i.jp1ajs2.unitdef.parser.Input;
import com.m12i.jp1ajs2.unitdef.parser.Parsers.Options;

import static org.hamcrest.CoreMatchers.is;

public class ParsersTest {

	private static final Parsers p0 = new Parsers();
	private static final Parsers p1;
	static {
		final Options o1 = new Options();
		o1.setEscapePrefixInDoubleQuotes('"');
		o1.setEscapePrefixInSingleQuotes('\'');
		p1 = new Parsers(o1);
	}
	
	@Test
	public void parseAbcTest00() {
		final Input i0 = Input.fromString("abc def ghi");
		final String r0 = p0.parseAbc(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc"));
		assertThat(i0.columnNo(), is(4));
		
	}
	
	@Test
	public void parseAbcTest01() {
		final Input i0 = Input.fromString(" abc def ghi");
		final String r0 = p0.parseAbc(i0);
		assertThat(r0.length() == 0, is(true));
		assertThat(r0, is(""));
		assertThat(i0.columnNo(), is(1));
		
	}
	
	@Test
	public void parseAbcTest02() {
		final Input i0 = Input.fromString("abc123 def ghi");
		final String r0 = p0.parseAbc(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc"));
		assertThat(i0.columnNo(), is(4));
		
	}
	
	@Test
	public void parseAbcTest03() {
		final Input i0 = Input.fromString("abc_$123 def ghi");
		final String r0 = p0.parseAbc(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc"));
		assertThat(i0.columnNo(), is(4));
		
	}
	
	@Test
	public void parseAbc123Test00() {
		final Input i0 = Input.fromString("abc123 def ghi");
		final String r0 = p0.parseAbc123(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123"));
		assertThat(i0.columnNo(), is(7));
		
	}
	
	@Test
	public void parseAbc123Test01() {
		final Input i0 = Input.fromString(" abc def ghi");
		final String r0 = p0.parseAbc123(i0);
		assertThat(r0.length() == 0, is(true));
		assertThat(r0.length() > 0, is(false));
		assertThat(r0, is(""));
		assertThat(i0.columnNo(), is(1));
		
	}
	
	@Test
	public void parseAbc123Test02() {
		final Input i0 = Input.fromString("123abc def ghi");
		final String r0 = p0.parseAbc123(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("123abc"));
		assertThat(i0.columnNo(), is(7));
		
	}
	
	@Test
	public void parseAbc123Test03() {
		final Input i0 = Input.fromString("abc123_$ def ghi");
		final String r0 = p0.parseAbc123(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123"));
		assertThat(i0.columnNo(), is(7));
		
	}
	
	@Test
	public void parseAbc123_$Test00() {
		final Input i0 = Input.fromString("abc123_$ def ghi");
		final String r0 = p0.parseAbc123_$(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123_$"));
		assertThat(i0.columnNo(), is(9));
		
	}
	
	@Test
	public void parseAbc123_$Test01() {
		final Input i0 = Input.fromString(" abc def ghi");
		final String r0 = p0.parseAbc123_$(i0);
		assertThat(r0.length(), is(0));
		assertThat(i0.columnNo(), is(1));
		
	}
	
	@Test
	public void parseAbc123_$Test02() {
		final Input i0 = Input.fromString("123_$abc def ghi");
		final String r0 = p0.parseAbc123_$(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("123_$abc"));
		assertThat(i0.columnNo(), is(9));
		
	}
	
	@Test
	public void parseAbc123_$Test03() {
		final Input i0 = Input.fromString("_$abc123 def ghi");
		final String r0 = p0.parseAbc123_$(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("_$abc123"));
		assertThat(i0.columnNo(), is(9));
		
	}

	@Test
	public void parseQuotedStringTest00() {
		final Input i0 = Input.fromString("abc def ghi");
		try {
			@SuppressWarnings("unused")
			final String r0 = p0.parseQuotedString(i0);
			fail();
		} catch (final Exception e) {
			// OK
		}
		assertThat(i0.columnNo(), is(1));
		
	}

	@Test
	public void parseQuotedStringTest01() {
		final Input i0 = Input.fromString("\"\"abc def ghi");
		final String r0 = p0.parseQuotedString(i0);
		assertThat(r0.length(), is(0));
		assertThat(i0.columnNo(), is(3));
		
	}

	@Test
	public void parseQuotedStringTest02() {
		final Input i0 = Input.fromString("\"abc def\" ghi");
		final String r0 = p0.parseQuotedString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc def"));
		assertThat(i0.columnNo(), is(10));
	}

	@Test
	public void parseQuotedStringTest04() {
		final Input i0 = Input.fromString("'''abc ''def''' ghi");
		final String r0 = p1.parseQuotedString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("'abc 'def'"));
		assertThat(i0.columnNo(), is(16));
	}
	
	@Test
	public void parseQuotedStringTest03() {
		final Input i0 = Input.fromString("\"\\\"abc \\\"def\\\"\" ghi");
		final String r0 = p0.parseQuotedString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("\"abc \"def\""));
		assertThat(i0.columnNo(), is(16));
	}
	
	@Test
	public void parseRawStringTest00() {
		final Input i0 = Input.fromString("abc def ghi");
		final String r0 = p0.parseRawString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc"));
		assertThat(i0.columnNo(), is(4));
	}

	@Test
	public void parseRawStringTest01() {
		final Input i0 = Input.fromString(" abc def ghi");
		final String r0 = p0.parseRawString(i0);
		assertThat(r0.length(), is(0));
		assertThat(i0.columnNo(), is(1));
	}

	@Test
	public void parseRawStringTest02() {
		final Input i0 = Input.fromString("abc123_$ def ghi");
		final String r0 = p0.parseRawString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123_$"));
		assertThat(i0.columnNo(), is(9));
	}

	@Test
	public void parseRawStringTest03() {
		final Input i0 = Input.fromString("abc123_$-@*:;>< def ghi");
		final String r0 = p0.parseRawString(i0);
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123_$-@*:;><"));
		assertThat(i0.columnNo(), is(r0.length() + 1));
	}
	
	@Test
	public void parseUntilTest00() {
		final Input i0 = Input.fromString("abc123 def ghi");
		final String r0 = p0.parseUntil(i0, 'f', 'g');
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123 de"));
		assertThat(i0.columnNo(), is(r0.length() + 1));
		
	}
	
	@Test
	public void parseUntilTest01() {
		final Input i0 = Input.fromString("abc123 ghi def");
		final String r0 = p0.parseUntil(i0, 'f', 'g');
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123 "));
		assertThat(i0.columnNo(), is(r0.length() + 1));
		
	}
	
	@Test
	public void parseUntilTest02() {
		final Input i0 = Input.fromString("abc123 ghi def");
		final String r0 = p0.parseUntil(i0, 'x');
		assertThat(r0.length() > 0, is(true));
		assertThat(r0.length() == 0, is(false));
		assertThat(r0, is("abc123 ghi def"));
		assertThat(i0.hasReachedEof(), is(true));
	}
	
	@Test
	public void parseNumberTest00() {
		final Input i0 = Input.fromString("abc def ghi");
		try {
			@SuppressWarnings("unused")
			final Double r0 = p0.parseNumber(i0);
			fail();
		} catch (final Exception e) {
			// OK.
		}
		assertThat(i0.columnNo(), is(1));
		
	}
	
	@Test
	public void parseNumberTest01() {
		final Input i0 = Input.fromString("123abc def ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123d));
		assertThat(i0.columnNo(), is(4));
		
	}
	
	@Test
	public void parseNumberTest02() {
		final Input i0 = Input.fromString("123.456abc def ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123.456d));
		assertThat(i0.columnNo(), is(8));
		
	}
	
	@Test
	public void parseNumberTest03() {
		final Input i0 = Input.fromString("+123abc def ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123d));
		assertThat(i0.columnNo(), is(5));
		
	}
	
	@Test
	public void parseNumberTest04() {
		final Input i0 = Input.fromString("-123abc def ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(-123d));
		assertThat(i0.columnNo(), is(5));
		
	}
	
	@Test
	public void parseNumberTest05() {
		final Input i0 = Input.fromString("123.456ef ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123.456d));
		assertThat(i0.columnNo(), is(8));
		
	}
	
	@Test
	public void parseNumberTest06() {
		final Input i0 = Input.fromString("123.456e+1f ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123.456e+1d));
		assertThat(i0.columnNo(), is(11));
		
	}
	
	@Test
	public void parseNumberTest07() {
		final Input i0 = Input.fromString("123.456e-1f ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123.456e-1d));
		assertThat(i0.columnNo(), is(11));
		
	}
	
	@Test
	public void parseNumberTest08() {
		final Input i0 = Input.fromString(".456e-10f ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(.456e-10d));
		assertThat(i0.columnNo(), is(9));
		
	}
	
	@Test
	public void parseNumberTest09() {
		final Input i0 = Input.fromString("123.e-10f ghi");
		final Double r0 = p0.parseNumber(i0);
		assertThat(r0, is(123.e-10d));
		assertThat(i0.columnNo(), is(9));
		
	}
	
	@Test
	public void skipCommentTest00() {
		final Input i0 = Input.fromString("123.e-10f ghi");
		p0.skipComment(i0);
		assertThat(i0.columnNo(), is(1));
	}

	@Test
	public void skipCommentTest01() {
		final Input i0 = Input.fromString("//123.e-10f ghi");
		p0.skipComment(i0);
		assertThat(i0.hasReachedEof(), is(true));
	}

	@Test
	public void skipCommentTest02() {
		final Input i0 = Input.fromString("//123\r\n.e-10f ghi");
		p0.skipComment(i0);
		assertThat(i0.hasReachedEof(), is(false));
		assertThat(i0.current(), is('.'));
	}

	@Test
	public void skipCommentTest03() {
		final Input i0 = Input.fromString("/*123*/.e-10f ghi");
		p0.skipComment(i0);
		assertThat(i0.hasReachedEof(), is(false));
		assertThat(i0.current(), is('.'));
	}

	@Test
	public void skipCommentTest04() {
		final Input i0 = Input.fromString("/*123\r\n456*/ def ghi");
		p0.skipComment(i0);
		assertThat(i0.hasReachedEof(), is(false));
		assertThat(i0.current(), is(' '));
	}

	@Test
	public void skipWordTest01() {
		final Input i0 = Input.fromString("123456 def ghi");
		p0.skipWord(i0, "123");
		assertThat(i0.hasReachedEof(), is(false));
		assertThat(i0.current(), is('4'));
	}

	@Test
	public void skipWordTest02() {
		final Input i0 = Input.fromString("123456 def ghi");
		i0.next();
		try {
			p0.skipWord(i0, "123");
			fail();
		} catch (final Exception e) {
			// OK
		}
		assertThat(i0.hasReachedEof(), is(false));
		assertThat(i0.current(), is('2'));
	}
	
	@Test
	public void checkTest00() {
		final Input i0 = Input.fromString("123 456 def ghi");
		p0.check(i0, '1');
		assertTrue(true);
	}
	
	@Test
	public void checkTest01() {
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
	public void checkWordTest00() {
		final Input i0 = Input.fromString("123 456 def ghi");
		p0.checkWord(i0, "123");
		assertTrue(true);
	}
	
	@Test
	public void checkWordTest01() {
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
	public void checkWordTest02() {
		final Input i0 = Input.fromString("123\r\n 456 def ghi");
		p0.checkWord(i0, "123");
		assertTrue(true);
	}
	
	@Test
	public void checkWordTest03() {
		final Input i0 = Input.fromString("123\r\n 456 def ghi");
		try {
			p0.checkWord(i0, "123\r");
		} catch (Exception e) {
			fail();
		}
	}

}
