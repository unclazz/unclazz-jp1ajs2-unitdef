package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.Tuple;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

/**
 * {@link ParameterValueQuery}のためのユーティリティ・クラス.
 */
public final class ParameterValueQueries {
	private ParameterValueQueries() {}
	
	private static final Query<ParameterValue,String> STRING =
			new Query<ParameterValue,String>() {
		@Override
		public String queryFrom(ParameterValue value) {
			return value.getString().toString();
		}
	};
	private static final Query<ParameterValue,CharSequence> CHAR_SEQUENCE =
			new Query<ParameterValue,CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return value.getString();
		}
	};
	private static final Query<ParameterValue,CharSequence> ESCAPED = 
			new Query<ParameterValue,CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return CharSequenceUtils.escape(value.getString());
		}
	};
	private static final Query<ParameterValue,CharSequence> QUOTED = 
			new Query<ParameterValue,CharSequence>() {
		@Override
		public CharSequence queryFrom(ParameterValue value) {
			return CharSequenceUtils.quote(value.getString());
		}
	};
	private static final Query<ParameterValue,Integer> INTEGER =
			new Query<ParameterValue,Integer>() {
		@Override
		public Integer queryFrom(ParameterValue value) {
			return Integer.parseInt(STRING.queryFrom(value));
		}
	};
	private static final Query<ParameterValue,Long> LONG_INTEGER =
			new Query<ParameterValue,Long>() {
		@Override
		public Long queryFrom(ParameterValue value) {
			return Long.parseLong(STRING.queryFrom(value));
		}
	};
	private static final Query<ParameterValue,Tuple> TUPLE =
			new Query<ParameterValue,Tuple>() {
		@Override
		public Tuple queryFrom(ParameterValue value) {
			return value.getTuple();
		}
	};
	
	/**
	 * ユニット定義パラメータの値を文字シーケンスとして取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static final Query<ParameterValue,CharSequence> charSequence() {
		return CHAR_SEQUENCE;
	}
	/**
	 * ユニット定義パラメータの値をエスケープ済み文字シーケンスとして取得するためのクエリを返す.
	 * エスケープにより文字シーケンス中の{@code "\""}は{@code "#\""}へ置き換えられる。
	 * また{@code "#"}は{@code "##"}へと置き換えられる。
	 * @return クエリ
	 */
	public static final Query<ParameterValue,CharSequence> escaped() {
		return ESCAPED;
	}
	/**
	 * ユニット定義パラメータの値を整数値として取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static final Query<ParameterValue,Integer> integer() {
		return INTEGER;
	}
	/**
	 * ユニット定義パラメータの値を整数値として取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static final Query<ParameterValue,Long> longInteger() {
		return LONG_INTEGER;
	}
	/**
	 * ユニット定義パラメータの値をエスケープされ二重引用符で囲われた文字シーケンスとして取得するためのクエリを返す.
	 * エスケープの規則は{@link #escaped()}と同じ。
	 * @return クエリ
	 */
	public static final Query<ParameterValue,CharSequence> quoted() {
		return QUOTED;
	}
	/**
	 * ユニット定義パラメータの値を文字列として取得するためのクエリを返す.
	 * @return クエリ
	 */
	public static final Query<ParameterValue,String> string() {
		return STRING;
	}
	/**
	 * ユニット定義パラメータの値をタプルとして取得するためのクエリを返す.
	 * 対象のユニット定義パラメータ値の種別がタプル出ない場合は空のタプルが返される。
	 * @return クエリ
	 */
	public static final Query<ParameterValue,Tuple> tuple() {
		return TUPLE;
	}
}
