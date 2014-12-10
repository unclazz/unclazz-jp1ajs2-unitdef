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

	/**
	 * 第1引数で指定されたユニットのユニット定義パラメータのうちから第2引数で指定された名前に合致する1件を返す.
	 * 指定された名前に合致するものがない場合、{@link None}を返します。
	 * @param unit 対象ユニット
	 * @param targetParamName 対象のユニット定義パラメータ名
	 * @return ユニット定義パラメータ
	 */
	public static Maybe<Param> findParamOne(final Unit unit,
			final String targetParamName) {
		notNull(unit);
		notNull(targetParamName);
		for (final Param p : unit.getParams()) {
			if (p.getName().equals(targetParamName)) {
				return Maybe.wrap(p);
			}
		}
		return Maybe.nothing();
	}
	
	public static String findParamOne(final Unit unit, final String paramName, final String defaultValue) {
		final Maybe<Param> p = findParamOne(unit, paramName);
		return p.isNothing() ? defaultValue : p.get().getValues().get(0).getStringValue();
	}
	
	public static int findParamOne(final Unit unit, final String paramName, final int defaultValue) {
		final Maybe<Param> p = findParamOne(unit, paramName);
		return p.isNothing() ? defaultValue : Integer.parseInt(p.get().getValues().get(0).getStringValue());
	}
	
	public static boolean findParamOne(final Unit unit, final String paramName, final boolean defaultValue) {
		final Maybe<Param> p = findParamOne(unit, paramName);
		if (p.isNothing()) {
			return defaultValue;
		} else {
			final String v = p.get().getValues().get(0).getStringValue().toLowerCase();
			if (v.equals("y") || v.equals("yes") || v.equals("on") || v.equals("t") || v.equals("true") || v.equals("1")) {
				return true;
			} else if(v.equals("n") || v.equals("no") || v.equals("off") || v.equals("f") || v.equals("false") || v.equals("0")) {
				return false;
			} else {
				return defaultValue;
			}
		}
	}
	
	public static String findParamOneAsStringValue(final Unit unit, final String paramName) {
		return findParamOne(unit, paramName, null);
	}

	public static Integer findParamOneAsIntValue(final Unit unit, final String paramName, final Integer defaultValue) {
		return findParamOne(unit, paramName, defaultValue);
	}
	
	public static Integer findParamOneAsIntValue(final Unit unit, final String paramName) {
		return findParamOneAsIntValue(unit, paramName, null);
	}

	public static List<String> findParamAllAsStringValues(final Unit unit, final String paramName) {
		final ArrayList<String> l = new ArrayList<String>();
		for (final Param p : Units.getParams(unit, paramName)) {
			l.add(p.getValue());
		}
		return l;
	}

	private static void notNull(final Object o) {
		if (o == null) {
			throw new IllegalArgumentException();
		}
	}
}
