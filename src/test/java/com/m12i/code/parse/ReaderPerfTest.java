package com.m12i.code.parse;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.m12i.code.parse.EagerReader;
import com.m12i.code.parse.LazyReader;
import com.m12i.code.parse.Reader;

public class ReaderPerfTest {

	private static final String filePath = "aiueo.txt";
	
	public static long now() {
		return System.currentTimeMillis();
	}
	
	public static void printDelta(String msg, long start) {
		final long now = System.currentTimeMillis();
		System.out.println(msg + String.format(" (start: %d, end: %d, delta: %d)", start, now, now - start));
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		final FileOutputStream fos = new FileOutputStream(filePath);
		final BufferedOutputStream bos = new BufferedOutputStream(fos);
		final OutputStreamWriter osw = new OutputStreamWriter(bos, Charset.defaultCharset());
		final PrintWriter pr = new PrintWriter(osw);
		for (int i = 0; i < 10000; i++) {
			// あいうえお * 10
			pr.println("あいうえおあいうえおあいうえおあいうえおあいうえおあいうえおあいうえおあいうえおあいうえおあいうえお\r\n");
		}
		pr.close();
	}

	@Test
	public void EargerLoadParsable性能確認() throws FileNotFoundException, IOException {
		final long start0 = now();
		final Reader p0 = new EagerReader(new FileInputStream(filePath), Charset.defaultCharset().name());
		final StringBuilder sb0 = new StringBuilder();
		while (!p0.hasReachedEof()) {
			sb0.append(p0.lineNo()).append(p0.columnNo()).append(p0.current());
			p0.next();
		}
		printDelta("EargerLoadParsable性能確認[0]", start0);
		
		final long start1 = now();
		final Reader p1 = new EagerReader(new FileInputStream(filePath), Charset.defaultCharset().name());
		final StringBuilder sb1 = new StringBuilder();
		while (!p1.hasReachedEof()) {
			sb1.append(p1.lineNo()).append(p1.columnNo()).append(p1.current());
			p1.next();
		}
		printDelta("EargerLoadParsable性能確認[1]", start1);
		
		final long start2 = now();
		final Reader p2 = new EagerReader(new FileInputStream(filePath), Charset.defaultCharset().name());
		final StringBuilder sb2 = new StringBuilder();
		while (!p2.hasReachedEof()) {
			sb2.append(p2.lineNo()).append(p2.columnNo()).append(p2.current());
			p2.next();
		}
		printDelta("EargerLoadParsable性能確認[2]", start2);
		assertTrue(true);
	}

	@Test
	public void LazyLoadParsable性能確認() throws FileNotFoundException, IOException {
		final long start0 = now();
		final Reader p0 = new LazyReader(new FileInputStream(filePath), Charset.defaultCharset().name());
		final StringBuilder sb0 = new StringBuilder();
		while (!p0.hasReachedEof()) {
			sb0.append(p0.lineNo()).append(p0.columnNo()).append(p0.current());
			p0.next();
		}
		printDelta("LazyLoadParsable性能確認[0]", start0);
		
		final long start1 = now();
		final Reader p1 = new LazyReader(new FileInputStream(filePath), Charset.defaultCharset().name());
		final StringBuilder sb1 = new StringBuilder();
		while (!p1.hasReachedEof()) {
			sb1.append(p1.lineNo()).append(p1.columnNo()).append(p1.current());
			p1.next();
		}
		printDelta("LazyLoadParsable性能確認[1]", start1);
		
		final long start2 = now();
		final Reader p2 = new LazyReader(new FileInputStream(filePath), Charset.defaultCharset().name());
		final StringBuilder sb2 = new StringBuilder();
		while (!p2.hasReachedEof()) {
			sb2.append(p1.lineNo()).append(p1.columnNo()).append(p2.current());
			p2.next();
		}
		printDelta("LazyLoadParsable性能確認[2]", start2);
		assertTrue(true);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		new File(filePath).delete();
	}
	
}
