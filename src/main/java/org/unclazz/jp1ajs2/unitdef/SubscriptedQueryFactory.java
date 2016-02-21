package org.unclazz.jp1ajs2.unitdef;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import static org.unclazz.jp1ajs2.unitdef.util.ListUtils.*;
import static org.unclazz.jp1ajs2.unitdef.ParameterValueQueries.*;

/**
 * 指定された位置にあるユニット定義パラメータ値を取得するためのクエリのファクトリ.
 * <p>このクラスのインスタンスは{@link NameSpecifiedParameterQuery#valueAt(int)}を呼び出すことで取得できる。
 * {@link NameSpecifiedParameterQuery}のインスタンスは{@link UnitQueries#parameter(String)}
 * を呼び出すことで取得できる。
 * したがって、例えば{@code unit.query(parameter("el").valueAt(0).asString())}といったかたちで利用することができる。</p>
 */
public final class SubscriptedQueryFactory {
	private final int i;
	private final String paramName;
	
	SubscriptedQueryFactory(final String paramName, final int i) {
		this.paramName = paramName;
		this.i = i;
	}
	
	/**
	 * ユニット定義パラメータ値を整数値として読み取るクエリを返す.
	 * @return クエリ
	 */
	public UnitQuery<Integer> asInt() {
		return new UnitQuery<Integer>() {
			@Override
			public List<Integer> queryFrom(Unit unit) {
				final List<Integer> l = linkedList();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(p.getValue(i).query(integer()));
				}
				return l;
			}
		};
	}
	
	/**
	 * ユニット定義パラメータ値を整数値として読み取るクエリを返す.
	 * このクエリは読み取りに失敗した場合、デフォルト値を返す。
	 * @param defaultValue デフォルト値
	 * @return クエリ
	 */
	public UnitQuery<Integer> asInt(final int defaultValue) {
		return new UnitQuery<Integer>() {
			@Override
			public List<Integer> queryFrom(Unit unit) {
				final List<Integer> l = linkedList();
				for (final Parameter p : unit.getParameters(paramName)) {
					try {
						l.add(p.getValue(i).query(integer()));
					} catch (final RuntimeException e) {
						l.add(defaultValue);
					}
				}
				return l;
			}
		};
	}
	
	/**
	 * ユニット定義パラメータ値を長整数値として読み取るクエリを返す.
	 * このクエリは読み取りに失敗した場合、デフォルト値を返す。
	 * @param defaultValue デフォルト値
	 * @return クエリ
	 */
	public UnitQuery<Long> asLong(final long defaultValue) {
		return new UnitQuery<Long>() {
			@Override
			public List<Long> queryFrom(Unit unit) {
				final List<Long> l = new LinkedList<Long>();
				for (final Parameter p : unit.getParameters(paramName)) {
					try {
						l.add(p.getValue(i).query(ParameterValueQueries.longInteger()));
					} catch (final RuntimeException e) {
						l.add(defaultValue);
					}
				}
				return l;
			}
		};
	}
	
	/**
	 * ユニット定義パラメータ値を長整数値として読み取るクエリを返す.
	 * このクエリは読み取りに失敗した場合、デフォルト値を返す。
	 * @return クエリ
	 */
	public UnitQuery<Long> longInteger() {
		return new UnitQuery<Long>() {
			@Override
			public List<Long> queryFrom(Unit unit) {
				final List<Long> l = new LinkedList<Long>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(p.getValue(i).query(ParameterValueQueries.longInteger()));
				}
				return l;
			}
		};
	}
	
	/**
	 * ユニット定義パラメータ値を文字シーケンスとして読み取るクエリを返す.
	 * @return クエリ
	 */
	public UnitQuery<CharSequence> asCharSequence() {
		return new UnitQuery<CharSequence>() {
			@Override
			public List<CharSequence> queryFrom(Unit unit) {
				final List<CharSequence> l = new LinkedList<CharSequence>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(p.getValue(i).query(charSequence()));
				}
				return l;
			}
		};
	}
	
	/**
	 * ユニット定義パラメータ値をタプルとして読み取るクエリを返す.
	 * @return クエリ
	 */
	public UnitQuery<Tuple> asTuple() {
		return new UnitQuery<Tuple>() {
			@Override
			public List<Tuple> queryFrom(Unit unit) {
				final List<Tuple> l = new LinkedList<Tuple>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(p.getValue(i).query(tuple()));
				}
				return l;
			}
		};
	}
	
	/**
	 * ユニット定義パラメータ値を文字列として読み取るクエリを返す.
	 * @return クエリ
	 */
	public UnitQuery<String> asString() {
		return new UnitQuery<String>() {
			@Override
			public List<String> queryFrom(Unit unit) {
				final List<String> l = new LinkedList<String>();
				for (final Parameter p : unit.getParameters(paramName)) {
					l.add(p.getValue(i).query(string()));
				}
				return l;
			}
		};
	}
	
	/**
	 * ユニット定義パラメータ値と指定された文字シーケンスが内容的に等しいか判定するクエリを返す.
	 * @param s 文字シーケンス
	 * @return クエリ
	 */
	public UnitQuery<Boolean> contentEquals(final CharSequence s) {
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
	
	/**
	 * ユニット定義パラメータ値が指定された文字シーケンスを含むか判定するクエリを返す.
	 * @param s 文字シーケンス
	 * @return クエリ
	 */
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
	
	/**
	 * ユニット定義パラメータ値が指定された文字シーケンスから始まるか判定するクエリを返す.
	 * @param s 文字シーケンス
	 * @return クエリ
	 */
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
	
	/**
	 * ユニット定義パラメータ値が指定されたパターンにマッチするか判定するクエリを返す.
	 * @param pattern 正規表現パターン
	 * @return クエリ
	 */
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
	
	/**
	 * ユニット定義パラメータ値が指定されたパターンにマッチするか判定するクエリを返す.
	 * @param pattern 正規表現パターン
	 * @return クエリ
	 */
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
