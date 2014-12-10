package com.m12i.jp1ajs2.unitdef;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.m12i.jp1ajs2.unitdef.parser.UnitParser;
import com.m12i.jp1ajs2.unitdef.parser.Input;

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
}