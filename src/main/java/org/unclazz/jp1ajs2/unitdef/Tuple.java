package org.unclazz.jp1ajs2.unitdef;

import java.util.Collections;
import java.util.Iterator;

/**
 * ユニット定義パラメータで使用されるタプルもどきに対応するデータ型.
 * タプルもどきに格納された値には添字もしくはキーとなる文字列によってアクセスできる。
 */
public interface Tuple extends Iterable<Tuple.Entry> {
	/**
	 * 添字を使ってタプルもどきに格納された値にアクセスする.
	 * @param index 添字
	 * @return 格納されている値
	 */
	CharSequence get(int index);
	/**
	 * キーを使ってタプルもどきに格納された値にアクセスする.
	 * @param key キー
	 * @return 格納されている値
	 */
	CharSequence get(CharSequence key);
	/**
	 * タプルもどきに格納された要素の数を返す.
	 * @return タプルの要素数
	 */
	int size();
	/**
	 * タプルもどきが空（要素数が0）であるかどうかを返す.
	 * @return {@code true}:空である、{@code false}:空でない
	 */
	boolean isEmpty();
	/**
	 * 空のタプルもどきインスタンス.
	 */
	public static final Tuple EMPTY_TUPLE = new Tuple(){
		@Override
		public CharSequence get(int index) {
			return null;
		}
		@Override
		public CharSequence get(CharSequence key) {
			return null;
		}
		@Override
		public int size() {
			return 0;
		}
		@Override
		public boolean isEmpty() {
			return true;
		}
		@Override
		public String toString() {
			return "()";
		}
		@Override
		public Iterator<Tuple.Entry> iterator() {
			return Collections.emptyIterator();
		}
	};
	/**
	 * タプルもどきのエントリー.
	 */
	public static interface Entry {
		/**
		 * キーを返す.
		 * @return キー
		 */
		String getKey();
		/**
		 * 値を返す.
		 * @return 値
		 */
		CharSequence getValue();
	}
}
