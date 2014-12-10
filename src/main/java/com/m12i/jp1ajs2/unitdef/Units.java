package com.m12i.jp1ajs2.unitdef;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.m12i.jp1ajs2.unitdef.parser.UnitParser;
import com.m12i.jp1ajs2.unitdef.parser.Input;
import com.m12i.jp1ajs2.unitdef.util.Maybe;
import com.m12i.jp1ajs2.unitdef.util.OneIterator;
import com.m12i.jp1ajs2.unitdef.util.ZeroIterator;

// TODO Javadoc, Test
public final class Units {
	private Units() {
	}

	public static ParseResult fromFile(final File f) {
		try {
			return fromInput(Input.fromFile(f));
		} catch (Exception e) {
			return ParseResult.failure(e);
		}
	}

	public static ParseResult fromFile(final File f, final Charset charset) {
		try {
			return fromInput(Input.fromFile(f, charset));
		} catch (Exception e) {
			return ParseResult.failure(e);
		}
	}

	public static ParseResult fromStream(final InputStream s) {
		try {
			return fromInput(Input.fromStream(s));
		} catch (Exception e) {
			return ParseResult.failure(e);
		}
	}

	public static ParseResult fromStream(final InputStream s, final Charset charset) {
		try {
			return fromInput(Input.fromStream(s, charset));
		} catch (Exception e) {
			return ParseResult.failure(e);
		}
	}

	public static ParseResult fromString(final String s) {
		return fromInput(Input.fromString(s));
	}
	
	private static ParseResult fromInput(final Input in) {
		try {
			return ParseResult.success(new UnitParser().parse(in));
		} catch (Exception e) {
			return ParseResult.failure(e);
		}
	}
	
	public static List<Unit> asList(Unit unit) {
		final ArrayList<Unit> list = new ArrayList<>();
		collectSubUnits(list, unit);
		return list;
	}
	
	private static void collectSubUnits(List<Unit> list, Unit unit) {
		list.add(unit);
		for (final Unit child : unit.getSubUnits()) {
			collectSubUnits(list, child);
		}
	}
	
	public static Maybe<Param> getParams(final Unit unit, final String paramName) {
		final List<Param> list = new ArrayList<Param>();
		for (final Param p : unit.getParams()) {
			if (paramName.equals(p.getName())) {
				list.add(p);
			}
		}
		return Maybe.wrap(list);
	}
	
	public static Maybe<Unit> getSubUnits(final Unit unit, final String unitName) {
		final List<Unit> list = new ArrayList<Unit>();
		for (final Unit u : unit.getSubUnits()) {
			if (unitName.equals(u.getName())) {
				list.add(u);
			}
		}
		return Maybe.wrap(list);
	}
	
	public static Maybe<Unit> getDescendentUnits(final Unit unit, final String unitName) {
		final List<Unit> list = new ArrayList<Unit>();
		for (final Unit u : asList(unit)	) {
			if (unitName.equals(u.getName())) {
				list.add(u);
			}
		}
		return Maybe.wrap(list);
	}
	
	public static final class ParseResult implements Iterable<Unit> {
		public static ParseResult success(Unit r) {
			return new ParseResult(null, r);
		}
		public static ParseResult failure(String message) {
			return new ParseResult(new RuntimeException(message), null);
		}
		public static ParseResult failure(Throwable error) {
			return new ParseResult(error, null);
		}
		
		private final Throwable l;
		private final Unit r;
		public ParseResult (Throwable error, Unit unit) {
			this.l = error;
			this.r = unit;
		}
		public boolean isFailure() {
			return l != null;
		}
		public boolean isSuccess() {
			return l == null;
		}
		public Throwable error() {
			if (isFailure()) {
				return l;
			}
			throw new RuntimeException();
		}
		public Unit unit() {
			if (isSuccess()) {
				return r;
			}
			throw new RuntimeException();
		}
		@Override
		public Iterator<Unit> iterator() {
			if (isFailure()) {
				return ZeroIterator.getInstance();
			} else {
				return new OneIterator<Unit>(r);
			}
		}
		@Override
		public String toString() {
			return (isFailure() ? "Failure(" : "Success(") + error() + ")";
		}
		@Override
		public boolean equals(Object other) {
			if (other == null || other.getClass() != ParseResult.class) {
				return false;
			}
			final ParseResult that = (ParseResult) other;
			if (this.isFailure() != that.isSuccess()) {
				return false;
			}
			return this.isFailure() ? (this.error().equals(that.error())) : (this.unit().equals(that.unit()));
		}
		@Override
		public int hashCode() {
			return 37 * (this.isFailure() ? this.error() : this.unit()).hashCode();
		}
	}
}