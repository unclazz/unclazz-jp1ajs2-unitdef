package com.m12i.jp1ajs2.unitdef;

import java.util.ArrayList;
import java.util.List;

/**
 * すべてのユニット種別に共通するユニット定義パラメータを取得するためのヘルパー関数群.
 * 
 * @author mizuki.fujitani
 *
 */
public final class Helpers {
	
	private Helpers() {}

	public static Maybe<String> getStringValues(final Unit unit, final String paramName) {
		final List<String> list = new ArrayList<String>();
		for (final Param p : Units.getParams(unit, paramName)) {
			list.add(p.getValue());
		}
		return Maybe.wrap(list);
	}

	public static Maybe<Integer> getIntValues(final Unit unit, final String paramName) {
		final List<Integer> list = new ArrayList<Integer>();
		for (final Param p : Units.getParams(unit, paramName)) {
			try {
				list.add(Integer.parseInt(p.getValue(0).getStringValue()));
			} catch (final NumberFormatException e) {
				// Do nothing.
			}
		}
		return Maybe.wrap(list);
	}

	public static Maybe<Boolean> getBoolValues(final Unit unit, final String paramName) {
		final List<Boolean> list = new ArrayList<Boolean>();
		for (final Param p : Units.getParams(unit, paramName)) {
			final String v = p.getValue(0).getStringValue().toLowerCase();
			if (v.equals("y") || v.equals("yes") || v.equals("on") || v.equals("t") || v.equals("true") || v.equals("1")) {
				list.add(true);
			} else if(v.equals("n") || v.equals("no") || v.equals("off") || v.equals("f") || v.equals("false") || v.equals("0")) {
				list.add(false);
			}
		}
		return Maybe.wrap(list);
	}
}
