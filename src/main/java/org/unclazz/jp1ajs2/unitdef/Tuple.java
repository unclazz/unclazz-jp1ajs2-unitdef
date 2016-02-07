package org.unclazz.jp1ajs2.unitdef;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.CharSequential;

/**
 * ユニット定義パラメータで使用されるタプルもどきに対応するデータ型.
 * タプルもどきに格納された値には添字もしくはキーとなる文字列によってアクセスできる。
 */
public interface Tuple extends Iterable<Tuple.Entry>, CharSequential {
	/**
	 * 添字を使ってタプルもどきに格納された値にアクセスする.
	 * @param index 添字
	 * @return 格納されている値
	 * @throws IndexOutOfBoundsException 添字に対応するエントリが存在しない場合
	 */
	CharSequence get(int index);
	/**
	 * キーを使ってタプルもどきに格納された値にアクセスする.
	 * @param key キー
	 * @return 格納されている値
	 * @throws NoSuchElementException キーに対応するエントリが存在しない場合
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
			throw new IndexOutOfBoundsException();
		}
		@Override
		public CharSequence get(CharSequence key) {
			throw new NoSuchElementException();
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
		@Override
		public CharSequence toCharSequence() {
			return toString();
		}
		@Override
		public boolean contentEquals(CharSequence other) {
			return CharSequenceUtils.contentsAreEqual(toString(), other);
		}
		@Override
		public boolean contentEquals(CharSequential other) {
			return contentEquals(other.toCharSequence());
		}
	};
	/**
	 * タプルもどきのエントリー.
	 */
	public static interface Entry extends CharSequential {
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
