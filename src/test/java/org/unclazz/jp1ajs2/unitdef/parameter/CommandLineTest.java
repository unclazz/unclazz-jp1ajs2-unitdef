package org.unclazz.jp1ajs2.unitdef.parameter;

import static org.junit.Assert.*;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class CommandLineTest {

	@Test
	public void of_StringArray() {
		try {
			@SuppressWarnings("unused")
			CommandLine cmd = CommandLine.of(new String[0]);
			fail();
		} catch (Exception e) {
			// OK
		}
		try {
			@SuppressWarnings("unused")
			CommandLine cmd = CommandLine.of(new String[]{"foo", "", "bar"});
			fail();
		} catch (Exception e) {
			// OK
		}
		try {
			@SuppressWarnings("unused")
			CommandLine cmd = CommandLine.of(new String[]{"foo", null, "bar"});
			fail();
		} catch (Exception e) {
			// OK
		}
		
		final CommandLine cmd = CommandLine.of("foo.exe", "bar", "baz");
		assertThat(cmd.getCommand(), CoreMatchers.is("foo.exe"));
		assertThat(cmd.getArguments(), CoreMatchers.is(new String[]{"bar", "baz"}));
		assertThat(cmd.getFragments(), CoreMatchers.is(new String[]{"foo.exe", "bar", "baz"}));
		
		assertThat(CommandLine.of("foo.exe ", " bar", "baz  ").getFragments(),
				CoreMatchers.is(new String[]{"foo.exe", "bar", "baz"}));
	}

	@Test
	public void of_CharSequence() {
		try {
			@SuppressWarnings("unused")
			CommandLine cmd = CommandLine.of("");
			fail();
		} catch (Exception e) {
			// OK
		}
		try {
			@SuppressWarnings("unused")
			CommandLine cmd = CommandLine.of("  ");
			fail();
		} catch (Exception e) {
			// OK
		}
		try {
			@SuppressWarnings("unused")
			CommandLine cmd = CommandLine.of("\"foo bar");
			fail();
		} catch (Exception e) {
			// OK
		}
		try {
			@SuppressWarnings("unused")
			CommandLine cmd = CommandLine.of("foo bar\\");
			fail();
		} catch (Exception e) {
			// OK
		}
		
		final CommandLine cmd = CommandLine.of("foo.exe bar baz");
		assertThat(cmd.getCommand(), CoreMatchers.is("foo.exe"));
		assertThat(cmd.getArguments(), CoreMatchers.is(new String[]{"bar", "baz"}));
		assertThat(cmd.getFragments(), CoreMatchers.is(new String[]{"foo.exe", "bar", "baz"}));
		
		assertThat(CommandLine.of(" foo.exe  bar baz  ").getFragments(),
				CoreMatchers.is(new String[]{"foo.exe", "bar", "baz"}));
		
		assertThat(CommandLine.of("foo.exe \"bar baz\"").getFragments(),
				CoreMatchers.is(new String[]{"foo.exe", "\"bar baz\""}));
	}
	
	@Test
	public void toString_() {
		assertThat(CommandLine.of("foo bar baz").toString(), CoreMatchers.is("foo bar baz"));
		assertThat(CommandLine.of("foo \"bar baz\" ").toString(), CoreMatchers.is("foo \"bar baz\""));
		assertThat(CommandLine.of("foo \"bar\"baz").toString(), CoreMatchers.is("foo \"bar\"baz"));
	}
}
