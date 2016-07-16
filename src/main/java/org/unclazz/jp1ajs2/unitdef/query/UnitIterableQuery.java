package org.unclazz.jp1ajs2.unitdef.query;

import static org.unclazz.jp1ajs2.unitdef.query.QueryUtils.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.Yield;
import org.unclazz.jp1ajs2.unitdef.util.LazyIterable.YieldCallable;

/**
 * ユニットに問合せを行い子ユニット（直接の下位ユニット）や子孫ユニット（直接・間接の下位ユニット）を返すクエリ.
 * 
 * <p>このクエリのインスタンスを得るには{@link UnitQueries}の提供する静的メソッドを利用する。
 * {@link Unit#query(Query)}メソッドをクエリに対して適用すると問合せが行われる：</p>
 * 
 * <pre> import static org.unclazz.jp1ajs2.unitdef.query.Queries.*;
 * Unit u = ...;
 * Iterable&lt;Unit&gt; ui = u.query(children());</pre>
 * 
 * <p>このオブジェクト自身が提供するメソッドを通じてクエリに種々の条件を追加することが可能である。
 * これらの条件は内部的に記憶されて問い合わせの時に利用される。
 * クエリはイミュータブルでありステートレスであるので、複雑な条件を設定したインスタンスの参照を保持しておくことで、
 * 複数の異なるユニットに対して繰り返し問合せを行うことができる。</p>
 * 
 * <pre> Query&lt;Unit,Iterable&gt; q0 = children().hasChildren();
 * Query&lt;Unit,Iterable&gt; q1 = q0.typeIs(UnitType.PC_JOB);
 * Query&lt;Unit,Iterable&gt; q2 = q1.hasParameter("cm");
 * Iterable&lt;Unit&gt; ui = u.query(q1); // cmパラメータを持つこと という条件は付かない</pre>
 * 
 * <p>{@link #queryFrom(Unit)}メソッドから返えされる{@link Iterable}は遅延評価に基づき値を返す。
 * 問合せのロジックの起動は可能な限り遅らせられるので、仮に1つ取得するだけで{@link Iterable}を破棄したとしても、
 * そのために消費されるCPUとメモリのコストは当該の1ユニットを問合せるのに必要な分だけである。</p>
 * 
 * <p>なおこのように問合せ結果のうち最初の1つだけを取得したい場合は、
 * {@link #one()}もしくはそのオーバーロードを呼び出して{@link OneQuery}のインスタンスを得ると便利である。
 * また問合せ結果として遅延評価{@link Iterable}の代わりに正格評価{@link List}を取得したい場合は、
 * {@link #list()}メソッドを呼び出してクエリ{@link ListQuery}のインスタンスを得るとよい。</p>
 * 
 * <pre> Unit u2 = u.query(children().one());
 * List&lt;Unit&gt; ul = u.query(children().list());</pre>
 */
public class UnitIterableQuery extends IterableQuerySupport<Unit,Unit> implements Query<Unit, Iterable<Unit>> {
	private final Function<Unit, Iterable<Unit>> func;
	private final List<Predicate<Unit>> preds;
	
	UnitIterableQuery(final Function<Unit, Iterable<Unit>> func, final List<Predicate<Unit>> preds) {
		assertNotNull(func, "argument must not be null.");
		assertNotNull(preds, "argument must not be null.");
		
		this.func = func;
		this.preds = preds;
	}
	UnitIterableQuery(final Function<Unit, Iterable<Unit>> func) {
		this(func, Collections.<Predicate<Unit>>emptyList());
	}

	@Override
	public Iterable<Unit> queryFrom(Unit t) {
		assertNotNull(t, "argument must not be null.");
		
		return LazyIterable.forEach(func.apply(t), new YieldCallable<Unit,Unit>() {
			@Override
			public Yield<Unit> yield(final Unit item, int index) {
				for (final Predicate<Unit> pred : preds) {
					if (!pred.test(item)) {
						return Yield.yieldVoid();
					}
				}
				return Yield.yieldReturn(item);
			}
		});
	}
	/**
	 * 問合せの結果のユニットが持つユニット定義パラメータを問合せるクエリを返す.
	 * @return クエリ
	 */
	public ParameterIterableQuery theirParameters() {
		return new ParameterIterableQuery(this);
	}
	/**
	 * 問合せの結果のユニットが持つユニット定義パラメータを問合せるクエリを返す.
	 * @param name パラメータ名
	 * @return クエリ
	 */
	public ParameterIterableQuery theirParameters(final String name) {
		return new ParameterIterableQuery(this).nameEquals(name);
	}
	/**
	 * 問合せの結果のユニットが持つ完全名を問合せるクエリを返す.
	 * @return クエリ
	 */
	public IterableQuery<Unit, FullQualifiedName> theirFqn() {
		return new TypedValueIterableQuery<Unit, Unit, FullQualifiedName>(this,
		new Query<Unit, FullQualifiedName>() {
			@Override
			public FullQualifiedName queryFrom(Unit t) {
				return t.getFullQualifiedName();
			}
		});
	}
	/**
	 * 問合せの結果のユニットが持つユニット名を問合せるクエリを返す.
	 * @return クエリ
	 */
	public IterableQuery<Unit, String> theirName() {
		return new TypedValueIterableQuery<Unit, Unit, String>(this,
		new Query<Unit, String>() {
			@Override
			public String queryFrom(Unit t) {
				return t.getName();
			}
		});
	}
	@Override
	public UnitIterableQuery and(final Predicate<Unit> pred) {
		assertNotNull(pred, "argument must not be null.");
		
		final LinkedList<Predicate<Unit>> newPreds = new LinkedList<Predicate<Unit>>();
		newPreds.addAll(this.preds);
		newPreds.addLast(pred);
		return new UnitIterableQuery(this.func, newPreds);
	}
	/**
	 * ユニット種別の条件を追加したクエリを返す.
	 * @param t ユニット種別
	 * @return クエリ
	 */
	public UnitIterableQuery typeIs(final UnitType t) {
		assertNotNull(t, "argument must not be null.");
		
		return and(new Predicate<Unit>() {
			private final UnitType t1 = t;
			@Override
			public boolean test(final Unit u) {
				return u.getType().equals(t1);
			}
		});
	}
	/**
	 * ユニット完全名の条件を追加したクエリを返す.
	 * @param n ユニット完全名
	 * @return クエリ
	 */
	public UnitIterableQuery fqnEquals(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return u.getFullQualifiedName().toString().equals(n);
			}
		});
	}
	/**
	 * ユニット完全名の条件を追加したクエリを返す.
	 * @param n ユニット完全名の部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery fqnStartsWith(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return u.getFullQualifiedName().toString().startsWith(n);
			}
		});
	}
	/**
	 * ユニット完全名の条件を追加したクエリを返す.
	 * @param n ユニット完全名の部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery fqnEndsWith(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return u.getFullQualifiedName().toString().endsWith(n);
			}
		});
	}
	/**
	 * ユニット完全名の条件を追加したクエリを返す.
	 * @param n ユニット完全名の部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery fqnContains(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return u.getFullQualifiedName().toString().contains(n);
			}
		});
	}
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param n ユニット名
	 * @return クエリ
	 */
	public UnitIterableQuery nameEquals(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			private final String n1 = n;
			@Override
			public boolean test(final Unit u) {
				return u.getName().equals(n1);
			}
		});
	}
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param n ユニット名の部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery nameStartsWith(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			private final String n1 = n;
			@Override
			public boolean test(final Unit u) {
				return u.getName().startsWith(n1);
			}
		});
	}
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param n ユニット名の部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery nameEndsWith(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			private final String n1 = n;
			@Override
			public boolean test(final Unit u) {
				return u.getName().endsWith(n1);
			}
		});
	}
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param n ユニット名の部分文字列
	 * @return クエリ
	 */
	public UnitIterableQuery nameContains(final String n) {
		assertNotNull(n, "argument must not be null.");
		assertFalse(n.isEmpty(), "argument must not be empty.");
		
		return and(new Predicate<Unit>() {
			private final String n1 = n;
			@Override
			public boolean test(final Unit u) {
				return u.getName().contains(n1);
			}
		});
	}
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param regex ユニット名の正規表現パターン
	 * @return クエリ
	 */
	public UnitIterableQuery nameMatches(final Pattern regex) {
		assertNotNull(regex, "argument must not be null.");

		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return regex.matcher(u.getName()).matches();
			}
		});
	}
	/**
	 * ユニット名の条件を追加したクエリを返す.
	 * @param regex ユニット名の正規表現パターン
	 * @return クエリ
	 */
	public UnitIterableQuery nameMatches(final String regex) {
		return nameMatches(Pattern.compile(regex));
	}
	/**
	 * 子ユニット（直接の下位ユニット）の条件を追加したクエリを返す.
	 * @return クエリ
	 */
	public UnitIterableQuery hasChildren() {
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				return !u.getSubUnits().isEmpty();
			}
		});
	}
	/**
	 * 子ユニット（直接の下位ユニット）の条件を追加したクエリを返す.
	 * @param query 子ユニットに適用され判定結果を返すクエリ
	 * @return クエリ
	 */
	public UnitIterableQuery hasChildren(final Query<Unit,?> query) {
		assertNotNull(query, "argument must not be null.");
		return and(new Predicate<Unit>() {
			@Override
			public boolean test(final Unit u) {
				final Object o = query.queryFrom(u);
				if (o == null) {
					return false;
				} else if (o instanceof Boolean) {
					return (Boolean) o;
				} else if (o instanceof Iterable) {
					return ((Iterable<?>) o).iterator().hasNext();
				}
				return true;
			}
		});
	}
	/**
	 * ユニット定義パラメータの条件を追加したクエリのファクトリを返す.
	 * @param name パラメータ名
	 * @return ファクトリ
	 */
	public UnitIterableQueryParameterValueConditionFactotry hasParameter(final String name) {
		assertNotNull(name, "argument must not be null.");
		assertFalse(name.isEmpty(), "argument must not be empty.");
		
		return new UnitIterableQueryParameterValueConditionFactotry(func, preds, name);
	}
}
