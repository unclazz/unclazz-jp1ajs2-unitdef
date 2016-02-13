package org.unclazz.jp1ajs2.unitdef;

import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

/**
 * {@link ParameterValueQuery}のためのユーティリティ・クラス.
 */
public final class ParameterValueQueries {
	private ParameterValueQueries() {}
	
	private static final ParameterValueQuery<String> STRING =
			new ParameterValueQuery<String>() {
		@Override
		public String queryFrom(ParameterValue value) {
			return value.getRawCharSequence().toString();
		}
	};
	private static final ParameterValueQuery<CharSequence> CHAR_SEQUENCE =
			new ParameterValueQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return value.getRawCharSequence();
		}
	};
	private static final ParameterValueQuery<CharSequence> ESCAPED = 
			new ParameterValueQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return CharSequenceUtils.escape(value.getRawCharSequence());
		}
	};
	private static final ParameterValueQuery<CharSequence> QUOTED = 
			new ParameterValueQuery<CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return CharSequenceUtils.quote(value.getRawCharSequence());
		}
	};
	private static final ParameterValueQuery<Integer> INTEGER =
			new ParameterValueQuery<Integer>() {
		@Override
		public Integer queryFrom(ParameterValue value) {
			return Integer.parseInt(STRING.queryFrom(value));
		}
	};
	private static final ParameterValueQuery<Long> LONG_INTEGER =
			new ParameterValueQuery<Long>() {
		@Override
		public Long queryFrom(ParameterValue value) {
			return Long.parseLong(STRING.queryFrom(value));
		}
	};
	private static final ParameterValueQuery<Tuple> TUPLE =
			new ParameterValueQuery<Tuple>() {
		@Override
		public Tuple queryFrom(ParameterValue value) {
			return value.getTuple();
		}
	};
	
	/**
	 * ユニット定義パラメータの値を文字シーケンスとして取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static final ParameterValueQuery<CharSequence> charSequence() {
		return CHAR_SEQUENCE;
	}
	/**
	 * ユニット定義パラメータの値をエスケープ済み文字シーケンスとして取得するためのクエリを返す.
	 * エスケープにより文字シーケンス中の{@code "\""}は{@code "#\""}へ置き換えられる。
	 * また{@code "#"}は{@code "##"}へと置き換えられる。
	 * @return クエリ
	 */
	public static final ParameterValueQuery<CharSequence> escaped() {
		return ESCAPED;
	}
	/**
	 * ユニット定義パラメータの値を整数値として取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static final ParameterValueQuery<Integer> integer() {
		return INTEGER;
	}
	/**
	 * ユニット定義パラメータの値を整数値として取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static final ParameterValueQuery<Long> longInteger() {
		return LONG_INTEGER;
	}
	/**
	 * ユニット定義パラメータの値をエスケープされ二重引用符で囲われた文字シーケンスとして取得するためのクエリを返す.
	 * エスケープの規則は{@link #escaped()}と同じ。
	 * @return クエリ
	 */
	public static final ParameterValueQuery<CharSequence> quoted() {
		return QUOTED;
	}
	/**
	 * ユニット定義パラメータの値を文字列として取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static final ParameterValueQuery<String> string() {
		return STRING;
	}
	/**
	 * ユニット定義パラメータの値をタプルとして取得するためのクエリを返す.
	 * 対象のユニット定義パラメータ値の種別がタプル出ない場合は空のタプルが返される。
	 * @return クエリ
	 */
	public static final ParameterValueQuery<Tuple> tuple() {
		return TUPLE;
	}
}
