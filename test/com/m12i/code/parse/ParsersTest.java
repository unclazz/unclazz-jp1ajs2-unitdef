package com.m12i.code.parse;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class ParsersTest {

	private static final Parsers p0 = new Parsers();
	
	private static Reader input(final String s) {
		try {
			return new LazyReader(new ByteArrayInputStream(s.getBytes()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void parseAbcTest00() {
		final Reader i0 = input("abc def ghi");
		final Result<String> r0 = p0.parseAbc(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is("abc"));
		assertThat(i0.columnNo(), is(4));
		
	}
	
	@Test
	public void parseAbcTest01() {
		final Reader i0 = input(" abc def ghi");
		final Result<String> r0 = p0.parseAbc(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is(""));
		assertThat(i0.columnNo(), is(1));
		
	}
	
	@Test
	public void parseAbcTest02() {
		final Reader i0 = input("abc123 def ghi");
		final Result<String> r0 = p0.parseAbc(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is("abc"));
		assertThat(i0.columnNo(), is(4));
		
	}
	
	@Test
	public void parseAbcTest04() {
		final Reader i0 = input("abc_$123 def ghi");
		final Result<String> r0 = p0.parseAbc(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is("abc"));
		assertThat(i0.columnNo(), is(4));
		
	}
	
	@Test
	public void parseAbc123Test00() {
		final Reader i0 = input("abc123 def ghi");
		final Result<String> r0 = p0.parseAbc123(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is("abc123"));
		assertThat(i0.columnNo(), is(7));
		
	}
	
	@Test
	public void parseAbc123Test01() {
		final Reader i0 = input(" abc def ghi");
		final Result<String> r0 = p0.parseAbc123(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is(""));
		assertThat(i0.columnNo(), is(1));
		
	}
	
	@Test
	public void parseAbc123Test02() {
		final Reader i0 = input("123abc def ghi");
		final Result<String> r0 = p0.parseAbc123(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is("123abc"));
		assertThat(i0.columnNo(), is(7));
		
	}
	
	@Test
	public void parseAbc123Test04() {
		final Reader i0 = input("abc123_$ def ghi");
		final Result<String> r0 = p0.parseAbc123(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is("abc123"));
		assertThat(i0.columnNo(), is(7));
		
	}
	
	@Test
	public void parseAbc123_$Test00() {
		final Reader i0 = input("abc123_$ def ghi");
		final Result<String> r0 = p0.parseAbc123_$(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is("abc123_$"));
		assertThat(i0.columnNo(), is(9));
		
	}
	
	@Test
	public void parseAbc123_$Test01() {
		final Reader i0 = input(" abc def ghi");
		final Result<String> r0 = p0.parseAbc123_$(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is(""));
		assertThat(i0.columnNo(), is(1));
		
	}
	
	@Test
	public void parseAbc123_$Test02() {
		final Reader i0 = input("123_$abc def ghi");
		final Result<String> r0 = p0.parseAbc123_$(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is("123_$abc"));
		assertThat(i0.columnNo(), is(9));
		
	}
	
	@Test
	public void parseAbc123_$Test04() {
		final Reader i0 = input("_$abc123 def ghi");
		final Result<String> r0 = p0.parseAbc123_$(i0);
		assertThat(r0.successful, is(true));
		assertThat(r0.value, is("_$abc123"));
		assertThat(i0.columnNo(), is(9));
		
	}
}
