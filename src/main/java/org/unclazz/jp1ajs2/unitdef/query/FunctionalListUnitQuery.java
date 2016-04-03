package org.unclazz.jp1ajs2.unitdef.query;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.unclazz.jp1ajs2.unitdef.Unit;

/**
 * {@link Function}によりフィルタと変換を行いその結果を返すクエリ.
 * <p>具象クラスは{@link #source(Unit)}により入力ソースを規定し、
 * {@link #function()}により入力ソースから得られたオブジェクトの変換とフィルタリングを規定する。</p>
 * <p>このクエリは問合せ対象ユニットを引数にして{@link #source(Unit)}を呼び出す。
 * そしてそこから返された{@link Iterable}の要素のそれぞれを引数として
 * {@link #function()}が返す{@link Function}を実行する。
 * {@link Function#apply(Object)}を呼び出した結果が{@code null}以外であれば
 * そのオブジェクトは問合せ結果のリストの要素となる。
 * 一方結果が{@code null}であった場合、その値は問合せ結果の{@link List}には含まれない。</p>
 *
 * @param <A> 関数の戻り値の型
 */
public abstract class FunctionalListUnitQuery<A> implements ListUnitQuery<A> {
	/**
	 * 関数の適用対象となるオブジェクトを内包した{@link Iterable}を返す.
	 * <p>{@link Iterable}インスタンスから取得された要素は{@link #finalize()}が返す関数の引数として利用される。
	 * この要素は{@code null}であってはならない。</p>
	 * @param u ユニット
	 * @return {@link Iterable}インスタンス
	 */
	protected abstract Iterable<Unit> source(Unit u);
	/**
	 * ユニットから抽出されたオブジェクトをフィルタし変換するための関数を返す.
	 * <p>{@link #source(Unit)}から返された
	 * 当該オブジェクトに対して適用された関数が{@code null}を返した場合、
	 * 当該オブジェクトに対応する値（つまり{@code null}）はクエリの問合せ結果から除外される。</p>
	 * @return 関数オブジェクト
	 */
	protected abstract Function<Unit,A> function();
	/**
	 * 問合せ結果の最初の1件だけを返すクエリを返す.
	 * @return クエリ
	 * @throws NoSuchElementException 問合せ結果が0件である場合
	 */
	public UnitQuery<A> one() {
		final ListUnitQuery<A> base = this;
		return new UnitQuery<A>() {
			@Override
			public A queryFrom(final Unit u) {
				final List<A> one = base.queryFrom(u);
				if (one.isEmpty()) {
					throw new NoSuchElementException("No item exsists in query result.");
				}
				return one.get(0);
			}
		};
	}
	
	@Override
	public List<A> queryFrom(final Unit u) {
		final LinkedList<A> result = new LinkedList<A>();
		final Function<Unit,A> func = function();
		for (final Unit a : source(u)) {
			final A b = func.apply(a);
			if (b != null) {
				result.addLast(b);
			}
		}
		return result;
	}
	
	/**
	 * レシーバのクエリが内包する関数に対して引数で指定された関数を合成して新しいクエリをつくる.
	 * @param g 別の関数
	 * @return 新しいクエリ
	 * @param <B> 別の関数の戻り値の型
	 */
	public<B> FunctionalListUnitQuery<B> and(final Function<A,B> g) {
		final FunctionalListUnitQuery<A> base = this;
		return new FunctionalListUnitQuery<B>() {
			@Override
			public Iterable<Unit> source(final Unit n) {
				return base.source(n);
			}
			@Override
			public Function<Unit, B> function() {
				return SyntheticFunction.synthesize(base.function(), g);
			}
		};
	}
}
