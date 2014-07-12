package usertools.jp1ajs2.unitdef.util;

import java.util.ArrayList;
import java.util.List;

import usertools.jp1ajs2.unitdef.core.Param;
import usertools.jp1ajs2.unitdef.core.Unit;

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
	 * 指定された名前に合致するものがない場合、{@code null}を返します。
	 * @param unit 対象ユニット
	 * @param targetParamName 対象のユニット定義パラメータ名
	 * @return ユニット定義パラメータ
	 */
	public static Option<Param> findParamOne(final Unit unit,
			final String targetParamName) {
		notNull(unit);
		notNull(targetParamName);
		for (final Param p : unit.getParams()) {
			if (p.getName().equals(targetParamName)) {
				return Option.some(p);
			}
		}
		return Option.none();
	}
	
	public static String findParamOne(final Unit unit, final String paramName, final String defaultValue) {
		final Option<Param> p = findParamOne(unit, paramName);
		return p.isNone() ? defaultValue : p.get().getValues().get(0).getStringValue();
	}
	
	public static int findParamOne(final Unit unit, final String paramName, final int defaultValue) {
		final Option<Param> p = findParamOne(unit, paramName);
		return p.isNone() ? defaultValue : Integer.parseInt(p.get().getValues().get(0).getStringValue());
	}
	
	public static boolean findParamOne(final Unit unit, final String paramName, final boolean defaultValue) {
		final Option<Param> p = findParamOne(unit, paramName);
		if (p.isNone()) {
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

	/**
	 * 第1引数で指定されたユニットのユニット定義パラメータのうちから第2引数で指定された名前に合致するすべてを返す.
	 * 指定された名前に合致するものがない場合、空のリストを返します。
	 * @param unit 対象ユニット
	 * @param targetParamName 対象のユニット定義パラメータ名
	 * @return ユニット定義パラメータ
	 */
	public static List<Param> findParamAll(final Unit unit,
			final String targetParamName) {
		notNull(unit);
		notNull(targetParamName);
		final List<Param> result = new ArrayList<Param>();
		for (final Param p : unit.getParams()) {
			if (p.getName().equals(targetParamName)) {
				result.add(p);
			}
		}
		return result;
	}
	
	public static List<String> findParamAllAsStringValues(final Unit unit, final String paramName) {
		final ArrayList<String> l = new ArrayList<String>();
		for (final Param p : findParamAll(unit, paramName)) {
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
