package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
 * <p><pre> import static org.unclazz.jp1ajs2.unitdef.query.Queries.*;
 * Unit u = ...;
 * Iterable&lt;Parameter&gt; ui = u.query(parameters());
 * Iterable&lt;Parameter&gt; ui2 = u.query(children().theirParameters());</pre></p>
 * 
 * <p>クエリへの種々の条件追加、クエリのイミュータブルな特性、クエリが返す{@link Iterable}と
 * メソッド{@link #one()}・{@link #list()}については{@link UnitIterableQuery}と同様である。
 * 詳しくは{@link UnitIterableQuery}のドキュメントを参照のこと。</p>
 */
public class ParameterIterableQuery 
extends AbstractItrableQuery<Unit, Parameter>
implements Query<Unit, Iterable<Parameter>> {

	private final UnitIterableQuery baseQuery;
	private final List<Predicate<Parameter>> preds;
	
	ParameterIterableQuery(final UnitIterableQuery q, final List<Predicate<Parameter>> preds) {
		this.baseQuery = q;
		this.preds = preds;
	}
	ParameterIterableQuery(final UnitIterableQuery q) {
		this(q, Collections.<Predicate<Parameter>>emptyList());
	}

	@Override
	public Iterable<Parameter> queryFrom(final Unit t) {
		final Iterable<Parameter> ps = ChunkLazyIterable
				.forEach(baseQuery.queryFrom(t),
				new ChunkYieldCallable<Unit, Parameter>(){
			@Override
			public ChunkYield<Parameter> yield(Unit item, int index) {
				return ChunkYield.yieldReturn(item.getParameters());
			}
		});
		
		return LazyIterable.forEach
		(ps, new YieldCallable<Parameter,Parameter>() {
			@Override
			public Yield<Parameter> yield(Parameter item, int index) {
				for (final Predicate<Parameter> pred : preds) {
					if (!pred.test(item)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}
	
	public ParameterValueIterableQuery theirValues() {
		return new ParameterValueIterableQuery(this);
	}
	
	public ParameterIterableQuery and(final Predicate<Parameter> pred) {
		final LinkedList<Predicate<Parameter>> newPreds = new LinkedList<Predicate<Parameter>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new ParameterIterableQuery(this.baseQuery, newPreds);
	}
	
	public ParameterIterableQuery nameEquals(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().equals(n1);
			}
		});
	}
	
	public ParameterIterableQuery nameStartsWith(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().startsWith(n1);
			}
		});
	}
	
	public ParameterIterableQuery nameEndsWith(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().endsWith(n1);
			}
		});
	}
	
	public ParameterIterableQuery nameContains(final String n) {
		return and(new Predicate<Parameter>() {
			private final String n1 = n;
			@Override
			public boolean test(final Parameter t) {
				return t.getName().contains(n1);
			}
		});
	}
	
	public ParameterIterableQuery name(final Predicate<String> test) {
		return and(new Predicate<Parameter>() {
			private final Predicate<String> p1 = test;
			@Override
			public boolean test(final Parameter t) {
				return p1.test(t.getName());
			}
		});
	}
	
	public ParameterIterableQuery valueCountEquals(final int i) {
		return and(new Predicate<Parameter>() {
			private final int i1 = i;
			@Override
			public boolean test(final Parameter t) {
				return t.getValues().size() == i1;
			}
		});
	}
	
	public ParameterIterableQuery valueCount(final Predicate<Integer> test) {
		return and(new Predicate<Parameter>() {
			private final Predicate<Integer> p1 = test;
			@Override
			public boolean test(final Parameter t) {
				return p1.test(t.getValues().size());
			}
		});
	}
}
