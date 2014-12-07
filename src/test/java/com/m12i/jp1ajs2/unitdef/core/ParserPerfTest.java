package com.m12i.jp1ajs2.unitdef.core;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.m12i.jp1ajs2.unitdef.core.ParseUtils;
import com.m12i.jp1ajs2.unitdef.core.Unit;

public class ParserPerfTest {

	private static final String filePath = "unitdef.txt";
	
	public static long now() {
		return System.currentTimeMillis();
	}
	
	public static void printDelta(String msg, long start) {
		final long now = System.currentTimeMillis();
		System.out.println(msg + String.format(" (start: %d, end: %d, delta: %d)", start, now, now - start));
//		System.gc();
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private static InputStream mkStream() {
		try {
			return new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Before
	public void setUp() throws Exception {
		final FileOutputStream fos = new FileOutputStream(filePath);
		final BufferedOutputStream bos = new BufferedOutputStream(fos);
		final OutputStreamWriter osw = new OutputStreamWriter(bos, Charset.defaultCharset());
		final PrintWriter pr = new PrintWriter(osw);
		pr.println("unit=XXXX0000,AAAAA,BBBBB,CCCCC;\r\n"
				+ "{\r\n"
				+ "    ty=g;\r\n"
				+ "    el=XXXX0001,g,+80 +48;\r\n" 
				+ "    el=XXXX0002,g,+240 +144;\r\n"
				+ "    ar=(f=XXXX0001,t=XXXX0002);\r\n" 
				+ "    ar=(f=XXXX0002,t=XXXX0001,con);\r\n" // 実際には相互リンクは許されない 
				+ "    cm=\"これはコメントです。\";\r\n"
				+ "    xx=ABCDEF,ABC123,HAS SPACE,\"QUOTED STRING\",123456,2013/01/01,00:00,();\r\n"
				+ "    un=foobar;\r\n"
				+ "    fd=360;\r\n"
				+ "    unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
				+ "    {\r\n"
				+ "        ty=g;\r\n" 
				+ "    ar=(f=XXXX0001,t=XXXX0002);\r\n" 
				+ "    ar=(f=XXXX0002,t=XXXX0001,con);\r\n" // 実際には相互リンクは許されない 
				+ "        cm=\"これはコメントです。\";\r\n" );
		printRecursively(pr, 3);
		pr.println("    }\r\n"
				+ "    unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
				+ "    {\r\n"
				+ "        ty=g;\r\n" 
				+ "    ar=(f=XXXX0001,t=XXXX0002);\r\n" 
				+ "    ar=(f=XXXX0002,t=XXXX0001,con);\r\n" // 実際には相互リンクは許されない 
				+ "        cm=\"これはコメントです。\";\r\n" 
				+ "    }\r\n"
				+ "}\r\n");
		pr.close();
	}
	
	@After
	public void tearDown() {
		System.out.println(String.format("path: %s / size: %d", filePath, new File(filePath).length()));
		new File(filePath).delete();
	}
	
	private static void printRecursively(PrintWriter pw, int depth) {
		for (int i = 0; i < 10; i ++) {
			pw.println("    unit=XXXX0001,AAAAA,BBBBB,CCCCC;\r\n"
					+ "    {\r\n"
					+ "        ty=g;\r\n" 
					+ "        cm=\"これはコメントです。\";\r\n" 
					+ "    ar=(f=XXXX0001,t=XXXX0002);\r\n" 
					+ "    ar=(f=XXXX0002,t=XXXX0001,con);\r\n" // 実際には相互リンクは許されない 
					+ "    }\r\n"
					+ "    unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
					+ "    {\r\n"
					+ "        ty=g;\r\n" 
					+ "    ar=(f=XXXX0001,t=XXXX0002);\r\n" 
					+ "    ar=(f=XXXX0002,t=XXXX0001,con);\r\n" // 実際には相互リンクは許されない 
					+ "        cm=\"これはコメントです。\";\r\n");

			if (depth > 0) {
				printRecursively(pw, depth - 1);
			}
			
			pw.println("    }\r\n"
					+ "    unit=XXXX0002,AAAAA,BBBBB,CCCCC;\r\n"
					+ "    {\r\n"
					+ "        ty=g;\r\n" 
					+ "        cm=\"これはコメントです。\";\r\n" 
					+ "    ar=(f=XXXX0001,t=XXXX0002);\r\n" 
					+ "    ar=(f=XXXX0002,t=XXXX0001,con);\r\n" // 実際には相互リンクは許されない 
					+ "    }\r\n");
		}
	}

	@Test
	public void v1Test() {
		final long start0 = now();
		final Unit u0 = ParseUtils.parse(mkStream()).unit();
		assertTrue(u0 != null);
		printDelta("v1 parser [0]", start0);
		
		final long start1 = now();
		final Unit u1 = ParseUtils.parse(mkStream()).unit();
		assertTrue(u1 != null);
		printDelta("v1 parser [1]", start1);
		
		final long start2 = now();
		final Unit u2 = ParseUtils.parse(mkStream()).unit();
		assertTrue(u2 != null);
		printDelta("v1 parser [2]", start2);
	}
//	
//	@Test
//	public void v2Test() {
//		final long start0 = now();
//		final Unit u0 = ParseUtils.parse(mkStream()).right();
//		assertTrue(u0 != null);
//		printDelta("v1 parser [0]", start0);
//		
//		final long start1 = now();
//		final Unit u1 = ParseUtils.parse(mkStream()).right();
//		assertTrue(u1 != null);
//		printDelta("v1 parser [1]", start1);
//		
//		final long start2 = now();
//		final Unit u2 = ParseUtils.parse(mkStream()).right();
//		assertTrue(u2 != null);
//		printDelta("v1 parser [2]", start2);
//
//	}

}
