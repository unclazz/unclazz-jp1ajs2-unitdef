package org.unclazz.jp1ajs2.unitdef;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

public final class SubscriptedQueryFactory {
	private final int i;
	private final String paramName;
	SubscriptedQueryFactory(final String paramName, final int i) {
		this.paramName = paramName;
		this.i = i;
	}
	
	public UnitQuery<Integer> integer() {
		return new UnitQuery<Integer>() {
			@Override
			public List<Integer> queryFrom(Unit unit) {
				final List<Integer> l = new LinkedList<Integer>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(p.getValue(i, ParameterValueQueries.integer()));
				}
				return l;
			}
		};
	}
	
	public UnitQuery<Integer> integer(final int defaultValue) {
		return new UnitQuery<Integer>() {
			@Override
			public List<Integer> queryFrom(Unit unit) {
				final List<Integer> l = new LinkedList<Integer>();
				for (final Parameter p : unit.getParameters(paramName)) {
					try {
						l.add(p.getValue(i, ParameterValueQueries.integer()));
					} catch (final RuntimeException e) {
						l.add(defaultValue);
					}
				}
				return l;
			}
		};
	}
	
	public UnitQuery<Long> longInteger(final long defaultValue) {
		return new UnitQuery<Long>() {
			@Override
			public List<Long> queryFrom(Unit unit) {
				final List<Long> l = new LinkedList<Long>();
				for (final Parameter p : unit.getParameters(paramName)) {
					try {
						l.add(p.getValue(i, ParameterValueQueries.longInteger()));
					} catch (final RuntimeException e) {
						l.add(defaultValue);
					}
				}
				return l;
			}
		};
	}
	
	public UnitQuery<Long> longInteger() {
		return new UnitQuery<Long>() {
			@Override
			public List<Long> queryFrom(Unit unit) {
				final List<Long> l = new LinkedList<Long>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(p.getValue(i, ParameterValueQueries.longInteger()));
				}
				return l;
			}
		};
	}
	
	public UnitQuery<CharSequence> charSequence() {
		return new UnitQuery<CharSequence>() {
			@Override
			public List<CharSequence> queryFrom(Unit unit) {
				final List<CharSequence> l = new LinkedList<CharSequence>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(p.getValue(i, ParameterValueQueries.charSequence()));
				}
				return l;
			}
		};
	}
	
	public UnitQuery<String> string() {
		return new UnitQuery<String>() {
			@Override
			public List<String> queryFrom(Unit unit) {
				final List<String> l = new LinkedList<String>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(p.getValue(i, ParameterValueQueries.string()));
				}
				return l;
			}
		};
	}
	
	public UnitQuery<Boolean> is(final CharSequence s) {
		return new UnitQuery<Boolean>() {
			@Override
			public List<Boolean> queryFrom(Unit unit) {
				final List<Boolean> l = new LinkedList<Boolean>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(p.getValue(i).contentEquals(s));
				}
				return l;
			}
		};
	}
	
	public UnitQuery<Boolean> contains(final CharSequence s) {
		return new UnitQuery<Boolean>() {
			@Override
			public List<Boolean> queryFrom(Unit unit) {
				final List<Boolean> l = new LinkedList<Boolean>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(CharSequenceUtils.contains(p.getValue(i).getRawCharSequence(), s));
				}
				return l;
			}
		};
	}
	
	public UnitQuery<Boolean> startsWith(final CharSequence s) {
		return new UnitQuery<Boolean>() {
			@Override
			public List<Boolean> queryFrom(Unit unit) {
				final List<Boolean> l = new LinkedList<Boolean>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(CharSequenceUtils.startsWith(p.getValue(i).getRawCharSequence(), s));
				}
				return l;
			}
		};
	}
	
	public UnitQuery<MatchResult> matches(final Pattern pattern) {
		return new UnitQuery<MatchResult>() {
			@Override
			public List<MatchResult> queryFrom(Unit unit) {
				final List<MatchResult> l = new LinkedList<MatchResult>();
				for (final Parameter p : unit.getParameters(paramName)) {
					final Matcher m = pattern.matcher(p.getValue(i).getRawCharSequence());
					if (m.matches()) {
						l.add(m);
					}
				}
				return l;
			}
		};
	}
	
	public UnitQuery<MatchResult> matches(final String pattern) {
		return new UnitQuery<MatchResult>() {
			private final Pattern compiled = Pattern.compile(pattern);
			@Override
			public List<MatchResult> queryFrom(Unit unit) {
				final List<MatchResult> l = new LinkedList<MatchResult>();
				for (final Parameter p : unit.getParameters(paramName)) {
					final Matcher m = compiled.matcher(p.getValue(i).getRawCharSequence());
					if (m.matches()) {
						l.add(m);
					}
				}
				return l;
			}
		};
	}
}
