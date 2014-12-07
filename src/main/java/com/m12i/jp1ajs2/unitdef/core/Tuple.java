package com.m12i.jp1ajs2.unitdef.core;

import java.util.Iterator;

import com.m12i.jp1ajs2.unitdef.util.Option;
import com.m12i.jp1ajs2.unitdef.util.ZeroIterator;

/**
 * JP1定義コードのユニット定義パラメータに見られるタプルもどきに対応するデータ型.
 * タプルもどきに格納された値には添字もしくはキーとなる文字列によってアクセスします。
 */
public interface Tuple extends Iterable<TupleEntry> {
	/**
	 * 添字を使ってタプルもどきに格納された値にアクセスする.
	 * 存在しない位置の添字を指定された場合、nullを返します。
	 * @param index 添字
	 * @return 格納されている値
	 */
	Option<String> get(int index);
	/**
	 * キーを使ってタプルもどきに格納された値にアクセスする.
	 * 存在しない位置の添字を指定された場合、nullを返します。
	 * @param key キー
	 * @return 格納されている値
	 */
	Option<String> get(String key);
	/**
	 * タプルもどきに格納された要素の数を返す.
	 * @return タプルの要素数
	 */
	int size();
	/**
	 * タプルもどきが空（要素数が0）であるかどうかを返す.
	 * @return 空かどうか（true:空である、false:空でない）
	 */
	boolean isEmpty();
	/**
	 * 空のタプルもどきインスタンス.
	 */
	public static final Tuple EMPTY_TUPLE = new Tuple(){
		@Override
		public Option<String> get(int index) {
			return Option.none();
		}
		@Override
		public Option<String> get(String key) {
			return Option.none();
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
		public Iterator<TupleEntry> iterator() {
			return new ZeroIterator<TupleEntry>();
		}
		
	};
}
