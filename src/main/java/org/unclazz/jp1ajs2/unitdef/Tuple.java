package org.unclazz.jp1ajs2.unitdef;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.CharSequential;

/**
 * ユニット定義パラメータで使用されるタプルもどきに対応するデータ型.
 * <p><code>(k0=v0,k1=v1)</code>のようなキーと値のペアの集合であるケースと
 * <code>(v0,v1)</code>のような値のみのタプルであるケースがある。</p>
 * <p>タプルもどきに格納された値には添字もしくはキーとなる文字列によってアクセスできる。</p>
 */
public interface Tuple extends Iterable<Tuple.Entry>, CharSequential {
	/**
	 * タプルもどきのエントリ.
	 */
	public static interface Entry extends CharSequential {
		/**
		 * キーを持つエントリであれば{@code true}を返す.
		 * @return 判定結果
		 */
		boolean hasKey();
		/**
		 * キーを返す.
		 * キーが存在しないエントリの場合{@code ""}が返される。
		 * @return キー
		 */
		String getKey();
		/**
		 * 値を返す.
		 * 返される値は{@code null}でないことが保証されている。
		 * @return 値
		 */
		CharSequence getValue();
	}
	
	/**
	 * 空のタプルもどきインスタンス.
	 */
	public static final Tuple EMPTY_TUPLE = new Tuple(){
		@Override
		public CharSequence get(int index) {
			throw new IndexOutOfBoundsException();
		}
		@Override
		public CharSequence get(String key) {
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
		@Override
		public Set<String> keySet() {
			return Collections.emptySet();
		}
	};
	
	/**
	 * 添字を使ってタプルもどきに格納された値にアクセスする.
	 * 返される値が{@code null}でないことは保証されている。
	 * @param index 添字
	 * @return 格納されている値
	 * @throws IndexOutOfBoundsException 添字に対応するエントリが存在しない場合
	 */
	CharSequence get(int index);
	/**
	 * キーを使ってタプルもどきに格納された値にアクセスする.
	 * 返される値が{@code null}でないことは保証されている。
	 * @param key キー
	 * @return 格納されている値
	 * @throws NoSuchElementException キーに対応するエントリが存在しない場合
	 */
	CharSequence get(String key);
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
	 * タプルもどきに格納されたエントリーのキーのセットを返す.
	 * @return キーのセット
	 */
	Set<String> keySet();
}
