package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable.ChunkYield;
import org.unclazz.jp1ajs2.unitdef.util.ChunkLazyIterable.ChunkYieldCallable;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

/**
 * ユニットに問合せを行いユニット定義パラメータを返すクエリ.
 * 
 * <p>このクエリのインスタンスを得るには{@link UnitQueries}の提供する静的メソッドを利用する。
 * {@link Unit#query(Query)}メソッドをクエリに対して適用すると問合せが行われる：</p>
 * 
 * <pre> import static org.unclazz.jp1ajs2.unitdef.query.Queries.*;
 * Unit u = ...;
 * Iterable&lt;Parameter&gt; ui = u.query(parameters());
 * Iterable&lt;Parameter&gt; ui2 = u.query(children().theirParameters());</pre>
 * 
 * <p>クエリへの種々の条件追加、クエリのイミュータブルな特性、クエリが返す{@link Iterable}と
 * メソッド{@link #one()}・{@link #list()}については{@link UnitIterableQuery}と同様である。
 * 詳しくは{@link UnitIterableQuery}のドキュメントを参照のこと。</p>
 */
public class ParameterIterableQuery 
extends IterableQuerySupport<Unit, Parameter>
implements Query<Unit, Iterable<Parameter>>, 
ParameterNormalizer<ParameterIterableQuery> {

	private final Query<Unit,Iterable<Unit>> baseQuery;
	private final List<Predicate<Parameter>> preds;
	private final Normalize normalize;
	
	ParameterIterableQuery(final Query<Unit,Iterable<Unit>> q,
			final List<Predicate<Parameter>> preds,
			final Normalize normalize) {
		this.baseQuery = q;
		this.preds = preds;
		this.normalize = normalize;
	}
	ParameterIterableQuery(final Query<Unit,Iterable<Unit>> q) {
		this(q, Collections.<Predicate<Parameter>>emptyList(), null);
	}

	@Override
	public Iterable<Parameter> queryFrom(final Unit t) {
		// ChunkLazyIterableを用いて問合せ対象ユニットからパラメータを取得する
		final Iterable<Parameter> ps = ChunkLazyIterable
				.forEach(baseQuery.queryFrom(t),
				new ChunkYieldCallable<Unit, Parameter>(){
			@Override
			public ChunkYield<Parameter> yield(Unit item, int index) {
				return ChunkYield.yieldReturn(item.getParameters());
			}
		});
		// LazyIterableを用いてパラメータのそれぞれにPredicateで判断を加えつつ
		// その問合せの結果を呼び出し元に返す
		return LazyIterable.forEach
		(ps, new YieldCallable<Parameter,Parameter>() {
			@Override
			public Yield<Parameter> yield(Parameter item, int index) {
				if (normalize != null) {
					item = normalize.apply(item);
				}
				for (final Predicate<Parameter> pred : preds) {
					if (!pred.test(item)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}
	/**
	 * 問合せ結果のパラメータが持つパラメータ値を問合せるクエリを返す.
	 * @return クエリ
	 */
	public ParameterValueIterableQuery theirValues() {
		return new ParameterValueIterableQuery(this);
	}
	/**
	 * 問合せ結果のパラメータが持つパラメータ値を問合せるクエリを返す.
	 * @param at パラメータ値の位置
	 * @return クエリ
	 */
	public ParameterValueIterableQuery theirValues(final int at) {
		return new ParameterValueIterableQuery(this).at(at);
	}
	@Override
	public ParameterIterableQuery and(final Predicate<Parameter> pred) {
		final LinkedList<Predicate<Parameter>> newPreds = new LinkedList<Predicate<Parameter>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new ParameterIterableQuery(this.baseQuery, newPreds, normalize);
	}
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param n パラメータ名
	 * @return クエリ
	 */
	public ParameterIterableQuery nameEquals(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().equals(n1);
			}
		});
	}
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param n パラメータ名の部分文字列
	 * @return クエリ
	 */
	public ParameterIterableQuery nameStartsWith(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().startsWith(n1);
			}
		});
	}
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param n パラメータ名の部分文字列
	 * @return クエリ
	 */
	public ParameterIterableQuery nameEndsWith(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().endsWith(n1);
			}
		});
	}
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param n パラメータ名
	 * @return クエリ
	 */
	public ParameterIterableQuery nameContains(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().contains(n1);
			}
		});
	}
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param regex パラメータ名の正規表現パターン
	 * @return クエリ
	 */
	public ParameterIterableQuery nameMatches(final Pattern regex) {
		return and(new Predicate<Parameter>() {
			@Override
			public boolean test(final Parameter t) {
				return regex.matcher(t.getName()).matches();
			}
		});
	}
	/**
	 * パラメータ名の条件を追加したクエリを返す.
	 * @param regex パラメータ名の正規表現パターン
	 * @return クエリ
	 */
	public ParameterIterableQuery nameMatches(final String regex) {
		return nameMatches(Pattern.compile(regex));
	}
	/**
	 * パラメータ値の個数の条件を追加したクエリを返す.
	 * @param i パラメータ値の個数
	 * @return クエリ
	 */
	public ParameterIterableQuery valueCountEquals(final int i) {
		return and(new Predicate<Parameter>() {
			private final int i1 = i;
			@Override
			public boolean test(final Parameter t) {
				return t.getValues().size() == i1;
			}
		});
	}
	@Override
	public ParameterNormalizer.WhenValueAtNClause<ParameterIterableQuery> whenValueAt(int i) {
		final QueryFactoryForParameterIterableQuery factory =
				new QueryFactoryForParameterIterableQuery(baseQuery, preds, normalize);
		return new DefaultWhenValueAtNClause<ParameterIterableQuery>(factory, i);
	}
	@Override
	public ParameterNormalizer.WhenValueCountNClause<ParameterIterableQuery> whenValueCount(int c) {
		final QueryFactoryForParameterIterableQuery factory =
				new QueryFactoryForParameterIterableQuery(baseQuery, preds, normalize);
		return new DefaultWhenValueCountNClause<ParameterIterableQuery>(factory, c);
	}
}
