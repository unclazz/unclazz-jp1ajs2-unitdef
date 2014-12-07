package com.m12i.jp1ajs2.unitdef.util;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import org.junit.Test;

import com.m12i.code.parse.EagerReader;
import com.m12i.code.parse.ParseError;
import com.m12i.jp1ajs2.unitdef.ext.EnvironmentVariable;
import com.m12i.jp1ajs2.unitdef.util.EnvParamParser;

public class EnvParamParserTest {
	// Empty
	private final String envDef0 = "";
	// Only \r or \n
	private final String envDef1 = "\r\n";
	// Single def
	private final String envDef2 = "NAME0=VALUE0";
	// Single def with white-spaces
	private final String envDef3 = " NAME0=VALUE0 ";
	// Single def with white-spaces and empty line
	private final String envDef4 = " NAME0=VALUE0\r\n ";
	// Multi def
	private final String envDef5 = "NAME0=VALUE0\r\nNAME1=VALUE1";
	// Multi def includes quoted string
	private final String envDef6 = "NAME0=\"VALUE0 \r\nAND...\"\r\nNAME1=VALUE1";
	// Multi def includes escaped chars
	private final String envDef7 = "NAME0=\"VALUE0 \r\nAND...\"\r\nNAME1=\"VALUE1\r\n #\"string escaped by ##(sharp) #\"\"";
	
	private List<EnvironmentVariable> parse(String s) {
		try {
			return new EnvParamParser().parse(new EagerReader(s));
		} catch (ParseError e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testEnvDef0() {
		assertTrue(parse(envDef0).isEmpty());
	}

	@Test
	public void testEnvDef1() {
		assertTrue(parse(envDef1).isEmpty());
	}

	@Test
	public void testEnvDef2() {
		final List<EnvironmentVariable> env = parse(envDef2);
		assertThat(env.size(), is(1));
		assertThat(env.get(0).getName(), is("NAME0"));
		assertThat(env.get(0).getValue(), is("VALUE0"));
	}

	@Test
	public void testEnvDef3() {
		final List<EnvironmentVariable> env = parse(envDef3);
		assertThat(env.size(), is(1));
		assertThat(env.get(0).getName(), is("NAME0"));
		assertThat(env.get(0).getValue(), is("VALUE0 ")); // with tailing space
	}

	@Test
	public void testEnvDef4() {
		final List<EnvironmentVariable> env = parse(envDef4);
		assertThat(env.size(), is(1));
		assertThat(env.get(0).getName(), is("NAME0"));
		assertThat(env.get(0).getValue(), is("VALUE0"));
	}

	@Test
	public void testEnvDef5() {
		final List<EnvironmentVariable> env = parse(envDef5);
		assertThat(env.size(), is(2));
		assertThat(env.get(0).getName(), is("NAME0"));
		assertThat(env.get(0).getValue(), is("VALUE0"));
		assertThat(env.get(1).getName(), is("NAME1"));
		assertThat(env.get(1).getValue(), is("VALUE1"));
	}

	@Test
	public void testEnvDef6() {
		final List<EnvironmentVariable> env = parse(envDef6);
		assertThat(env.size(), is(2));
		assertThat(env.get(0).getName(), is("NAME0"));
		assertThat(env.get(0).getValue(), is("VALUE0 \r\nAND..."));
		assertThat(env.get(1).getName(), is("NAME1"));
		assertThat(env.get(1).getValue(), is("VALUE1"));
	}

	@Test
	public void testEnvDef7() {
		final List<EnvironmentVariable> env = parse(envDef7);
		assertThat(env.size(), is(2));
		assertThat(env.get(0).getName(), is("NAME0"));
		assertThat(env.get(0).getValue(), is("VALUE0 \r\nAND..."));
		assertThat(env.get(1).getName(), is("NAME1"));
		assertThat(env.get(1).getValue(), is("VALUE1\r\n \"string escaped by #(sharp) \""));
	}
}
