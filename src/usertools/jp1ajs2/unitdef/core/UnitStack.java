package usertools.jp1ajs2.unitdef.core;

import java.util.LinkedList;

final class UnitStack {
	private final StringBuilder buff = new StringBuilder();
	private final LinkedList<String> d = new LinkedList<String>();
	void push(final String s) {
		d.addLast(s);
	}
	String pop() {
		return d.removeLast();
	}
	String peek() {
		return d.peekLast();
	}
	String fqn() {
		buff.setLength(0);
		for (final String s : d) {
			buff.append('/').append(s);
		}
		return buff.toString();
	}
}
