package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * {@link Function}によりフィルタと変換を行いその結果を返すクエリ.
 * <p>具象クラスは{@link #source(NodeKind)}により入力ソースを規定し、
 * {@link #function()}により入力ソースから得られたオブジェクトの変換とフィルタリングを規定する。</p>
 * <p>このクエリは問合せ対象XMLノードを引数にして{@link #source(NodeKind)}を呼び出す。
 * そしてそこから返されたオブジェクトを引数として{@link #function()}が返す{@link Function}を実行する。
 * {@link Function#apply(Object)}を呼び出した結果が{@code null}以外であればそのオブジェクトは問合せ結果となる。
 * 一方結果が{@code null}であった場合、問合せ結果も{@code null}となる。</p>
 *
 * @param <A> 関数の引数の型
 * @param <B> 関数の戻り値の型
 */
public abstract class FunctionalUnitQuery<A,B> implements UnitQuery<B> {
	/**
	 * 関数の適用対象となるオブジェクトを返す.
	 * <p>このメソッドの戻り値は{@code null}であってはならない。</p>
	 * @param n XMLノード
	 * @return 任意の型のオブジェクト
	 */
	protected abstract A source(Unit n);
	/**
	 * XMLノードから抽出されたオブジェクトをフィルタし変換するための関数を返す.
	 * <p>{@link #source(NodeKind)}から返された
	 * 当該オブジェクトに対して適用された関数が{@code null}を返した場合、
	 * クエリの問合せ結果も{@code null}となる。</p>
	 * @return 関数オブジェクト
	 */
	protected abstract Function<A,B> function();
	
	@Override
	public B queryFrom(final Unit n) {
		final Function<A,B> func = function();
		final A a = source(n);
		if (a == null) {
			return null;
		}
		return func.apply(a);
	}
	
	/**
	 * レシーバのクエリが内包する関数に対して引数で指定された関数を合成して新しいクエリをつくる.
	 * @param g 別の関数
	 * @return 新しいクエリ
	 * @param <C> 別の関数の戻り値の型
	 */
	public<C> FunctionalUnitQuery<A,C> and(final Function<B,C> g) {
		final FunctionalUnitQuery<A,B> base = this;
		return new FunctionalUnitQuery<A, C>() {
			@Override
			public A source(Unit n) {
				return base.source(n);
			}
			@Override
			public Function<A, C> function() {
				return SyntheticFunction.synthesize(base.function(), g);
			}
		};
	}
}
