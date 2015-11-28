package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class CommandLine {
	public static CommandLine of(CharSequence seq) {
		return new CommandLine(splitCommandLine(seq));
	}
	public static CommandLine of(String... fragments) {
		return new CommandLine(fragments);
	}
	
	private final String[] fragments;
	private CommandLine(final String[] fragments) {
		final String[] newFragments = new String[fragments.length];
		if (fragments.length == 0) {
			throw new IllegalArgumentException("Command line fragments must be not empty array");
		}
		for (int i = 0; i < newFragments.length; i ++) {
			final String trimmedFragment = fragments[i].trim();
			if (trimmedFragment.length() == 0) {
				throw new IllegalArgumentException("Command line fragment must be not empty string");
			}
			newFragments[i] = trimmedFragment;
		}
		this.fragments = newFragments;
	}
	
	public String getCommand() {
		return fragments[0];
	}
	
	public String[] getArguments() {
		return Arrays.copyOfRange(fragments, 1, fragments.length);
	}
	
	public String[] getFragments() {
		return Arrays.copyOf(fragments, fragments.length);
	}
	
	private static String[] splitCommandLine(CharSequence seq) {
		final int len = seq.length();
		final List<String> list = new LinkedList<String>();
		final StringBuilder buff = new StringBuilder();
		boolean quoted = false;
		boolean escaped = false;
		
		for (int i = 0; i < len; i ++) {
			final char ch = seq.charAt(i);
			if (escaped) {
				buff.append(ch);
				escaped = false;
			} else if (ch == '\\') {
				escaped = true;
			} else if (ch == '"') {
				if (quoted) {
					quoted = false;
				} else {
					quoted = true;
				}
				buff.append(ch);
			} else if (ch <= ' ') {
				if (quoted) {
					buff.append(ch);
				} else if (buff.length() > 0) {
					list.add(buff.toString());
					buff.setLength(0);
				} else {
					buff.setLength(0);
				}
			} else {
				buff.append(ch);
			}
		}
		if (quoted) {
			throw new IllegalArgumentException("Unclosed quoted string is found");
		}
		if (escaped) {
			throw new IllegalArgumentException("Escape target is not found");
		}
		if (buff.length() > 0) {
			list.add(buff.toString());
		}
		return list.toArray(new String[list.size()]);
	}
	
	@Override
	public String toString() {
		final StringBuilder buff = new StringBuilder();
		for (final String f : fragments) {
			if (buff.length() > 0) {
				buff.append(' ');
			}
			buff.append(f);
		}
		return buff.toString();
	}
}
