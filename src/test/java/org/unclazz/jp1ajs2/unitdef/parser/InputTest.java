package org.unclazz.jp1ajs2.unitdef.parser;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.unclazz.jp1ajs2.unitdef.parser.Input;
import org.unclazz.jp1ajs2.unitdef.parser.InputExeption;

public class InputTest {

	@Test
	public void withEmptyString_testEeachedEof() throws InputExeption {
		final Input p = Input.fromString(""); 
		assertTrue(p.reachedEOF());
	}

	@Test
	public void withEmptyString_testReachedEol() throws InputExeption {
		final Input p = Input.fromString(""); 
		assertTrue(p.reachedEOL());
		assertFalse(p.unlessEOL());
	}

	@Test
	public void withEmptyString_testColumnNo() throws InputExeption {
		final Input p = Input.fromString("");
		assertThat(p.columnNumber(), is(1));
	}

	@Test
	public void withEmptyString_testCurrent() throws InputExeption {
		final Input p = Input.fromString("");
		assertThat(p.current(), is('\u0000'));
	}

	@Test
	public void withEmptyString_testLine() throws InputExeption {
		final Input p = Input.fromString("");
		assertTrue(p.reachedEOF());
		assertNull(p.line());
	}

	@Test
	public void withEmptyString_testLineNo() throws InputExeption {
		final Input p = Input.fromString("");
		assertThat(p.lineNumber(), is(1));
	}

	@Test
	public void withEmptyString_testNext() throws InputExeption {
		final Input p = Input.fromString("");
		assertThat(p.next(), is('\u0000'));
	}

	@Test
	public void atPositionOfCR_testColumnNo() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.columnNumber(), is(4));
	}

	@Test
	public void atPositionOfCR_testCurrent() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.current(), is('\r'));
	}

	@Test
	public void atPositionOfCR_testReachedEof() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.reachedEOF(), is(false));
	}

	@Test
	public void atPositionOfCR_testReachedEol() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.reachedEOL(), is(true));
	}

	@Test
	public void atPositionOfCR_testLine() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.line(), is("abc\r\n"));
		assertThat(p.line(false), is("abc\r\n"));
		assertThat(p.line(true), is("abc"));
	}

	@Test
	public void atPositionOfCR_testLineNo() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		assertThat(p.next(), is('b'));
		assertThat(p.next(), is('c'));
		assertThat(p.lineNumber(), is(1));
		assertThat(p.next(), is('\r'));
		assertThat(p.lineNumber(), is(1));
	}

	@Test
	public void atPositionOfCR_testNext() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		assertThat(p.next(), is('\n'));
	}

	@Test
	public void atPositionOfLF_testColumnNo() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.columnNumber(), is(5));
	}
	
	@Test
	public void atPositionOfLF_testCurrent() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.current(), is('\n'));
	}

	@Test
	public void atPositionOfLF_testReachedEof() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.reachedEOF(), is(false));
	}

	@Test
	public void atPositionOfLF_testReachedEol() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		assertThat(p.next(), is('b'));
		assertThat(p.next(), is('c'));
		assertThat(p.next(), is('\r'));
		assertThat(p.next(), is('\n'));
		assertThat(p.reachedEOL(), is(true));
	}

	@Test
	public void atPositionOfLF_testLine() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.line(), is("abc\r\n"));
		assertThat(p.line(false), is("abc\r\n"));
		assertThat(p.line(true), is("abc"));
	}

	@Test
	public void atPositionOfLF_testLineNo() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.lineNumber(), is(1));
	}

	@Test
	public void atPositionOfLF_testNext() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		assertThat(p.next(), is('1'));
	}

	@Test
	public void atLine2Column1_testColumnNo() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.columnNumber(), is(1));
	}

	@Test
	public void atLine2Column1_testCurrent() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		assertThat(p.next(), is('b'));
		assertThat(p.next(), is('c'));
		assertThat(p.next(), is('\r'));
		assertThat(p.next(), is('\n'));
		assertThat(p.next(), is('1'));
		assertThat(p.current(), is('1'));
	}

	@Test
	public void atLine2Column1_testReachedEof() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.reachedEOF(), is(false));
	}

	@Test
	public void atLine2Column1_testReachedEol() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.reachedEOL(), is(false));
	}

	@Test
	public void atLine2Column1_testLine() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.line(), is("123"));
	}

	@Test
	public void atLine2Column1_testLineNo() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.lineNumber(), is(2));
	}

	@Test
	public void atLine2Column1_testNext() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		assertThat(p.next(), is('2'));
	}

	@Test
	public void atEof_testColumnNo() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		p.next(); // '2'
		p.next(); // '3'
		p.next(); // EOF
		assertThat(p.columnNumber(), is(1));
	}

	@Test
	public void atEof_testCurrent() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		p.next(); // '2'
		p.next(); // '3'
		p.next(); // EOF
		assertThat(p.current(), is('\u0000'));
	}

	@Test
	public void atEof_testReachedEof() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		p.next(); // '2'
		p.next(); // '3'
		p.next(); // EOF
		assertThat(p.reachedEOF(), is(true));
	}

	@Test
	public void atEof_testReachedEol() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		assertThat(p.next(), is('b'));
		assertThat(p.next(), is('c'));
		assertThat(p.next(), is('\r'));
		assertThat(p.next(), is('\n'));
		assertThat(p.next(), is('1'));
		assertThat(p.next(), is('2'));
		assertThat(p.next(), is('3'));
		p.next(); // EOF
		assertThat(p.reachedEOL(), is(true));
	}

	@Test
	public void atEof_testLine() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		p.next(); // '2'
		p.next(); // '3'
		p.next(); // EOF
		assertNull(p.line());
	}

	@Test
	public void atEof_testLineNo() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		assertThat(p.next(), is('b'));
		assertThat(p.next(), is('c'));
		assertThat(p.next(), is('\r'));
		assertThat(p.next(), is('\n'));
		assertThat(p.lineNumber(), is(1));
		assertThat(p.next(), is('1'));
		assertThat(p.next(), is('2'));
		assertThat(p.next(), is('3'));
		p.next(); // EOF
		assertThat(p.lineNumber(), is(2));
	}

	@Test
	public void atEof_testNext() throws InputExeption {
		final Input p = Input.fromString("abc\r\n123");
		p.next(); // 'b'
		p.next(); // 'c'
		p.next(); // '\r'
		p.next(); // '\n'
		p.next(); // '1'
		p.next(); // '2'
		p.next(); // '3'
		p.next(); // EOF
		assertThat(p.next(), is('\u0000'));
	}

	@Test
	public void withStringIncludesEmptyLine_testCurrentEtc() throws InputExeption {
		final Input p0 = Input.fromString("\r\n\r\n");
		assertThat(p0.current(), is('\r'));
		assertThat(p0.columnNumber(), is(1));
		assertThat(p0.lineNumber(), is(1));
		assertThat(p0.reachedEOL(), is(true));
		
		assertThat(p0.next(), is('\n'));
		assertThat(p0.columnNumber(), is(2));
		assertThat(p0.lineNumber(), is(1));
		assertThat(p0.reachedEOL(), is(true));
		
		assertThat(p0.next(), is('\r'));
		assertThat(p0.columnNumber(), is(1));
		assertThat(p0.lineNumber(), is(2));
		assertThat(p0.reachedEOL(), is(true));

		assertThat(p0.next(), is('\n'));
		assertThat(p0.columnNumber(), is(2));
		assertThat(p0.lineNumber(), is(2));
		assertThat(p0.reachedEOL(), is(true));

		assertThat(p0.next(), is('\u0000'));
		assertThat(p0.reachedEOF(), is(true));

		final Input p1 = Input.fromString("\r\n");
		assertThat(p1.current(), is('\r'));
		assertThat(p1.columnNumber(), is(1));
		assertThat(p1.lineNumber(), is(1));
		assertThat(p1.reachedEOL(), is(true));

		assertThat(p1.next(), is('\n'));
		assertThat(p1.columnNumber(), is(2));
		assertThat(p1.lineNumber(), is(1));
		assertThat(p1.reachedEOL(), is(true));

		assertThat(p1.next(), is('\u0000'));
		assertThat(p1.reachedEOF(), is(true));

		final Input p2 = Input.fromString("\r\nABC");
		assertThat(p2.current(), is('\r'));
		assertThat(p2.columnNumber(), is(1));
		assertThat(p2.lineNumber(), is(1));
		assertThat(p2.reachedEOL(), is(true));

		assertThat(p2.next(), is('\n'));
		assertThat(p2.columnNumber(), is(2));
		assertThat(p2.lineNumber(), is(1));
		assertThat(p2.reachedEOL(), is(true));

		assertThat(p2.next(), is('A'));
		assertThat(p2.columnNumber(), is(1));
		assertThat(p2.lineNumber(), is(2));
		assertThat(p2.reachedEOL(), is(false));

		assertThat(p2.next(), is('B'));
		assertThat(p2.columnNumber(), is(2));
		assertThat(p2.lineNumber(), is(2));
		assertThat(p2.reachedEOL(), is(false));

		assertThat(p2.next(), is('C'));
		assertThat(p2.columnNumber(), is(3));
		assertThat(p2.lineNumber(), is(2));
		assertThat(p2.reachedEOL(), is(false));

		assertThat(p2.next(), is('\u0000'));
		assertThat(p2.reachedEOF(), is(true));

		final Input p3 = Input.fromString("\r\nABC\r\n");
		assertThat(p3.current(), is('\r'));
		assertThat(p3.columnNumber(), is(1));
		assertThat(p3.lineNumber(), is(1));
		assertThat(p3.reachedEOL(), is(true));

		assertThat(p3.next(), is('\n'));
		assertThat(p3.columnNumber(), is(2));
		assertThat(p3.lineNumber(), is(1));
		assertThat(p3.reachedEOL(), is(true));

		assertThat(p3.next(), is('A'));
		assertThat(p3.columnNumber(), is(1));
		assertThat(p3.lineNumber(), is(2));
		assertThat(p3.reachedEOL(), is(false));

		assertThat(p3.next(), is('B'));
		assertThat(p3.columnNumber(), is(2));
		assertThat(p3.lineNumber(), is(2));
		assertThat(p3.reachedEOL(), is(false));

		assertThat(p3.next(), is('C'));
		assertThat(p3.columnNumber(), is(3));
		assertThat(p3.lineNumber(), is(2));
		assertThat(p3.reachedEOL(), is(false));
		
		assertThat(p3.next(), is('\r'));
		assertThat(p3.columnNumber(), is(4));
		assertThat(p3.lineNumber(), is(2));
		assertThat(p3.reachedEOL(), is(true));

		assertThat(p3.next(), is('\n'));
		assertThat(p3.columnNumber(), is(5));
		assertThat(p3.lineNumber(), is(2));
		assertThat(p3.reachedEOL(), is(true));

		assertThat(p3.next(), is('\u0000'));
		assertThat(p3.reachedEOF(), is(true));

		final Input p4 = Input.fromString("\r\nABC\r\n\r\nDEF");
		assertThat(p4.current(), is('\r'));
		assertThat(p4.next(), is('\n'));
		assertThat(p4.next(), is('A'));
		assertThat(p4.next(), is('B'));
		assertThat(p4.next(), is('C'));
		assertThat(p4.next(), is('\r'));
		assertThat(p4.next(), is('\n'));
		assertThat(p4.next(), is('\r'));
		assertThat(p4.next(), is('\n'));
		assertThat(p4.next(), is('D'));
		assertThat(p4.next(), is('E'));
		assertThat(p4.next(), is('F'));
		assertThat(p4.next(), is('\u0000'));
		assertThat(p4.reachedEOF(), is(true));
	}

}
