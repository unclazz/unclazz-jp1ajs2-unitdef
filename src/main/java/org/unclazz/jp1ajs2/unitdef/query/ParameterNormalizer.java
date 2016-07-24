package org.unclazz.jp1ajs2.unitdef.query;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.ParameterValue;
import org.unclazz.jp1ajs2.unitdef.ParameterValueType;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.builder.Builders;
import org.unclazz.jp1ajs2.unitdef.builder.ParameterBuilder;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizer.NormalizerFactory;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizer.ThenClause;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizer.WhenThenList;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizer.WhenValueAtNAndClause;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizer.WhenValueAtNClause;
import org.unclazz.jp1ajs2.unitdef.query.ParameterNormalizer.WhenValueCountNClause;
import static org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils.*;
import org.unclazz.jp1ajs2.unitdef.util.Function;
import org.unclazz.jp1ajs2.unitdef.util.Predicate;

/**
 * デフォルト値が省略されるなどしているユニット定義パラメータの値を正規化するための特殊なクエリ・ファクトリ.
 * 
 * <p>JP1/AJS2のユニット定義ではスケジュールルールに関する設定のパラメータのほか多くの箇所でデフォルト値の省略が行われている。
 * このため定義情報の読み取りロジックはしばしば煩雑なものとなってしまう。この煩雑性をさけ、
 * ロジックのコーディングを可能な限りアプリケーションの主な関心のためのものとするため、このクエリ・ファクトリは用意された。</p>
 * 
 * <p>ファクトリの実装は{@link ParameterIterableQuery}クラスおよび
 * {@link SingleParameterNormalizer}（インスタンスは{@link ParameterQueries#normalize()}メソッドを通じて得られる）。</p>
 * 
 * <p>利用にあたってはまずwhen...系メソッドでパラメータ値の位置や数を指定し、equals...やmatches...などのメソッドで条件値を指定、
 * 続いてthen...系メソッドで条件にマッチしたパラメータに対する変更操作を指定する。when...系メソッドを呼び出すたびに、
 * 新しい正規化（変更操作）のエントリーの生成がはじまりthen...系メソッドによりエントリーが内部的に保存される。</p>
 * 
 * @param <Q> このファクトリが生成するクエリの型
 */
public interface ParameterNormalizer<Q extends ParameterNormalizer<Q>> {
	/**
	 * {@code whenValueAt(int)}系メソッドのあとに呼び出し可能なメソッドを提供するインターフェース.
	 * <p>ユニット定義パラメータの値を正規化するための特殊なクエリ・ファクトリ{@link ParameterNormalizer}とともに使用する。</p>
	 * @param <Q> ファクトリが生成するクエリの型
	 */
	public static interface WhenValueAtNClause<Q extends ParameterNormalizer<Q>> {
		WhenValueAtNAndClause<Q> consistsOfAlphabets();
		WhenValueAtNAndClause<Q> consistsOfDigits();
		WhenValueAtNAndClause<Q> contains(CharSequence cs);
		WhenValueAtNAndClause<Q> contentEquals(CharSequence cs);
		WhenValueAtNAndClause<Q> endsWith(CharSequence cs);
		WhenValueAtNAndClause<Q> equalsAnyOf(CharSequence...cs);
		WhenValueAtNAndClause<Q> matches(Pattern re);
		WhenValueAtNAndClause<Q> startsWith(CharSequence cs);
		WhenValueAtNAndClause<Q> typeIs(ParameterValueType t);
	}
	/**
	 * {@code whenValueAt(int).xxx(xxx)}系メソッドのあとに呼び出し可能なメソッドを提供するインターフェース.
	 * <p>ユニット定義パラメータの値を正規化するための特殊なクエリ・ファクトリ{@link ParameterNormalizer}とともに使用する。</p>
	 * @param <Q> ファクトリが生成するクエリの型
	 */
	public static interface WhenValueAtNAndClause<Q extends ParameterNormalizer<Q>> extends WhenValueAtNClause<Q>, ThenClause<Q> {
		WhenValueAtNClause<Q> whenValueAt(int i);
		WhenValueCountNClause<Q> whenValueCount(int c);
	}
	/**
	 * {@code whenValueCount(int)}メソッド呼び出しのあとに呼び出し可能なメソッドを提供するインターフェース.
	 * <p>ユニット定義パラメータの値を正規化するための特殊なクエリ・ファクトリ{@link ParameterNormalizer}とともに使用する。</p>
	 * @param <Q> ファクトリが生成するクエリの型
	 */
	public static interface WhenValueCountNClause<Q extends ParameterNormalizer<Q>> extends ThenClause<Q> {
		WhenValueAtNClause<Q> valueAt(int i);
	}
	/**
	 * {@code whenValueAt(int).xxx(xxx)}系メソッドや{@code whenValueCount(int)}メソッドのあとに
	 * 呼び出し可能なメソッドを提供するインターフェース.
	 * <p>ユニット定義パラメータの値を正規化するための特殊なクエリ・ファクトリ{@link ParameterNormalizer}とともに使用する。</p>
	 * @param <Q> ファクトリが生成するクエリの型
	 */
	public static interface ThenClause<Q extends ParameterNormalizer<Q>> {
		Q thenAppend(CharSequence cs);
		Q thenInsert(int i, CharSequence cs);
		Q thenPrepend(CharSequence cs);
		Q thenReplace(int i, CharSequence cs);
		Q thenReplaceAll(CharSequence... css);
		Q thenReplaceAll(Parameter cs);
	}
	public static interface NormalizerFactory<Q extends ParameterNormalizer<Q>> 
	extends Function<WhenThenList,Q>{
		Q apply(WhenThenList normalize);
	}
	public static final class WhenThenEntry {
		private final Predicate<Parameter> condition;
		private final Function<Parameter, Parameter> operation;
		WhenThenEntry(final Predicate<Parameter> cond
				, final Function<Parameter, Parameter> ope) {
			this.condition = cond;
			this.operation = ope;
		}
		Predicate<Parameter> getCondition() {
			return condition;
		}
		Function<Parameter, Parameter> getOperation() {
			return operation;
		}
	}
	public static final class WhenThenList 
	implements Iterable<WhenThenEntry>, Function<Parameter, Parameter> {
		public static WhenThenList getInstance() {
			return empty;
		}
		
		private static final WhenThenList empty = new WhenThenList(new 
						LinkedList<ParameterNormalizer.WhenThenEntry>());
		private final LinkedList<WhenThenEntry> list;
		private WhenThenList(LinkedList<WhenThenEntry> list) {
			this.list = list;
		}
		public WhenThenList cons(WhenThenEntry e) {
			final LinkedList<WhenThenEntry> newList = new LinkedList<WhenThenEntry>();
			newList.addAll(list);
			newList.addLast(e);
			return new WhenThenList(newList);
		}
		public WhenThenList cons(final Predicate<Parameter> cond
				, final Function<Parameter, Parameter> ope) {
			return cons(new WhenThenEntry(cond, ope));
		}
		public WhenThenList concat(WhenThenList l) {
			final LinkedList<WhenThenEntry> newList = new LinkedList<WhenThenEntry>();
			newList.addAll(list);
			newList.addAll(l.list);
			return new WhenThenList(newList);
		}
		@Override
		public Iterator<WhenThenEntry> iterator() {
			return list.iterator();
		}
		@Override
		public Parameter apply(Parameter t) {
			for (final WhenThenEntry e : list) {
				if (e.getCondition().test(t)) {
					return e.getOperation().apply(t);
				}
			}
			return t;
		}
	}
	WhenValueAtNClause<Q> whenValueAt(int i);
	WhenValueCountNClause<Q> whenValueCount(int c);
}


class QueryFactoryForParameterIterableQuery implements NormalizerFactory<ParameterIterableQuery> {
	private final Query<Unit,Iterable<Unit>> baseQuery;
	private final List<Predicate<Parameter>> preds;
	private final WhenThenList whenThenList;
	public QueryFactoryForParameterIterableQuery(Query<Unit,Iterable<Unit>> baseQuery,
			List<Predicate<Parameter>> preds, WhenThenList whenThenList) {
		this.baseQuery = baseQuery;
		this.preds = preds;
		this.whenThenList = whenThenList;
	}
	@Override
	public ParameterIterableQuery apply(WhenThenList normalize) {
		final WhenThenList n = whenThenList == null ? normalize : whenThenList.concat(normalize);
		return new ParameterIterableQuery(baseQuery, preds, n);
	}
}

class DefaultWhenValueCountNClause<Q extends ParameterNormalizer<Q>>
extends DefaultThenClause<Q>
implements WhenValueCountNClause<Q> {
	private static final class ValueCount implements Predicate<Parameter> {
		private final int c;
		ValueCount(int c) {
			this.c = c;
		}
		@Override
		public boolean test(Parameter t) {
			return t.getValues().size() == c;
		}
	}
	
	protected final int c;
	DefaultWhenValueCountNClause(NormalizerFactory<Q> queryFactory,
			WhenThenList whenThenList, 
			List<Predicate<Parameter>> currentWhenPreds,
			int c) {
		super(queryFactory, whenThenList, addLast(currentWhenPreds, new ValueCount(c)));
		this.c = c;
	}
	DefaultWhenValueCountNClause(NormalizerFactory<Q> queryFactory, int c) {
		this(queryFactory, WhenThenList.getInstance(), 
				Collections.<Predicate<Parameter>>emptyList(), c);
	}
	@Override
	public WhenValueAtNClause<Q> valueAt(int i) {
		return new DefaultWhenValueAtNClause<Q>(queryFactory, whenThenList, 
				currentWhenPreds, i);
	}
}

class DefaultWhenValueAtNAndClause<Q extends ParameterNormalizer<Q>> 
extends DefaultWhenValueAtNClause<Q>
implements WhenValueAtNAndClause<Q> {
	DefaultWhenValueAtNAndClause(NormalizerFactory<Q> queryFactory,
			WhenThenList whenThenList, 
			List<Predicate<Parameter>> currentWhenPreds, 
			int i) {
		super(queryFactory, whenThenList, currentWhenPreds, i);
	}
	
	@Override
	public WhenValueAtNClause<Q> whenValueAt(int i) {
		return new DefaultWhenValueAtNClause<Q>(queryFactory,
				whenThenList, currentWhenPreds, i);
	}
	@Override
	public WhenValueCountNClause<Q> whenValueCount(int c) {
		return new DefaultWhenValueCountNClause<Q>(queryFactory, 
				whenThenList, currentWhenPreds, c);
	}
	
}

class DefaultWhenValueAtNClause<Q extends ParameterNormalizer<Q>> 
extends DefaultThenClause<Q> implements WhenValueAtNClause<Q> {
	private static final class PredicateValuAtN implements Predicate<Parameter>{
		private final int i;
		private final Predicate<ParameterValue> predPV;
		PredicateValuAtN(int i, Predicate<ParameterValue> predPV) {
			this.i = i;
			this.predPV = predPV;
		}
		@Override
		public boolean test(Parameter p) {
			if (!(i < p.getValues().size())) {
				return false;
			}
			return predPV.test(p.getValues().get(i));
		}
	}
	private static final class Matches implements Predicate<ParameterValue> {
		private final Pattern re;
		Matches(Pattern re) {
			this.re = re;
		}
		Matches(String re) {
			this.re = Pattern.compile(re);
		}
		@Override
		public boolean test(ParameterValue t) {
			return re.matcher(t.getStringValue()).matches();
		}
	}
	private static final class Contains implements Predicate<ParameterValue> {
		private final CharSequence cs;
		Contains(CharSequence cs) {
			this.cs = cs;
		}
		@Override
		public boolean test(ParameterValue t) {
			return arg0ContainsArg1(t.getStringValue(), cs);
		}
	}
	private static final class ContentEquals implements Predicate<ParameterValue> {
		private final CharSequence cs;
		ContentEquals(CharSequence cs) {
			this.cs = cs;
		}
		@Override
		public boolean test(ParameterValue t) {
			return contentsAreEqual(t.getStringValue(), cs);
		}
	}
	private static final class EndsWith implements Predicate<ParameterValue> {
		private final CharSequence cs;
		EndsWith(CharSequence cs) {
			this.cs = cs;
		}
		@Override
		public boolean test(ParameterValue t) {
			return arg0EndsWithArg1(t.getStringValue(), cs);
		}
	}
	private static final class StartsWith implements Predicate<ParameterValue> {
		private final CharSequence cs;
		StartsWith(CharSequence cs) {
			this.cs = cs;
		}
		@Override
		public boolean test(ParameterValue t) {
			return arg0StartsWithArg1(t.getStringValue(), cs);
		}
	}
	private static final class EqualsAnyOf implements Predicate<ParameterValue> {
		private final CharSequence[] css;
		EqualsAnyOf(CharSequence... css) {
			this.css = css;
		}
		@Override
		public boolean test(ParameterValue t) {
			for (final CharSequence cs : css) {
				if (contentsAreEqual(t.getStringValue(), cs)) {
					return true;
				}
			}
			return false;
		}
	}
	private static final class TypeIs implements Predicate<ParameterValue> {
		private final ParameterValueType ty;
		TypeIs(ParameterValueType t) {
			this.ty = t;
		}
		@Override
		public boolean test(ParameterValue t) {
			return t.getType() == ty;
		}
	}
	
	protected final int i;
	DefaultWhenValueAtNClause(NormalizerFactory<Q> queryFactory,
			WhenThenList whenThenList, 
			List<Predicate<Parameter>> currentWhenPreds, 
			int i) {
		super(queryFactory, whenThenList, currentWhenPreds);
		this.i = i;
	}
	DefaultWhenValueAtNClause(NormalizerFactory<Q> queryFactory,int i) {
		super(queryFactory, WhenThenList.getInstance(), 
				Collections.<Predicate<Parameter>>emptyList());
		this.i = i;
	}
	
	protected WhenValueAtNAndClause<Q> whenValueAtNAndClause(Predicate<ParameterValue> newItem) {
		return new DefaultWhenValueAtNAndClause<Q>(queryFactory, whenThenList, 
				addLast(currentWhenPreds, new PredicateValuAtN(i, newItem)), i);
	}
	
	@Override
	public WhenValueAtNAndClause<Q> consistsOfAlphabets() {
		return whenValueAtNAndClause(new Matches("[a-z]+"));
	}

	@Override
	public WhenValueAtNAndClause<Q> consistsOfDigits() {
		return whenValueAtNAndClause(new Matches("[0-9]+"));
	}

	@Override
	public WhenValueAtNAndClause<Q> contains(CharSequence cs) {
		return whenValueAtNAndClause(new Contains(cs));
	}

	@Override
	public WhenValueAtNAndClause<Q> contentEquals(CharSequence cs) {
		return whenValueAtNAndClause(new ContentEquals(cs));
	}

	@Override
	public WhenValueAtNAndClause<Q> endsWith(CharSequence cs) {
		return whenValueAtNAndClause(new EndsWith(cs));
	}

	@Override
	public WhenValueAtNAndClause<Q> equalsAnyOf(CharSequence... css) {
		return whenValueAtNAndClause(new EqualsAnyOf(css));
	}

	@Override
	public WhenValueAtNAndClause<Q> matches(Pattern re) {
		return whenValueAtNAndClause(new Matches(re));
	}

	@Override
	public WhenValueAtNAndClause<Q> startsWith(CharSequence cs) {
		return whenValueAtNAndClause(new StartsWith(cs));
	}

	@Override
	public WhenValueAtNAndClause<Q> typeIs(ParameterValueType t) {
		return whenValueAtNAndClause(new TypeIs(t));
	}
}

class DefaultThenClause<Q extends ParameterNormalizer<Q>> implements ThenClause<Q> {
	private static ParameterBuilder parameterBuilder(final String name) {
		return Builders.parameter().setName(name);
	}
	private static final class Append implements Function<Parameter, Parameter> {
		private final CharSequence cs;
		private Append(CharSequence cs) {
			this.cs = cs;
		}
		@Override
		public Parameter apply(Parameter t) {
			final ParameterBuilder b = parameterBuilder(t.getName());
			for (final ParameterValue v2 : t.getValues()) {
				b.addValue(v2);
			}
			b.addRawCharSequence(cs);
			return b.build();
		}
	}
	private static final class Insert implements Function<Parameter, Parameter> {
		private final int i;
		private final CharSequence cs;
		private Insert(int i, CharSequence cs) {
			this.i = i;
			this.cs = cs;
		}
		@Override
		public Parameter apply(Parameter t) {
			final ParameterBuilder b = parameterBuilder(t.getName());
			int i2 = 0;
			for (final ParameterValue v2 : t.getValues()) {
				if (i == i2 ++) {
					b.addRawCharSequence(cs);
				}
				b.addValue(v2);
			}
			return b.build();
		}
	}
	private static final class Prepend implements Function<Parameter, Parameter> {
		private final CharSequence cs;
		private Prepend(CharSequence cs) {
			this.cs = cs;
		}
		@Override
		public Parameter apply(Parameter t) {
			final ParameterBuilder b = parameterBuilder(t.getName());
			b.addRawCharSequence(cs);
			for (final ParameterValue v2 : t.getValues()) {
				b.addValue(v2);
			}
			return b.build();
		}
	}
	private static final class Replace implements Function<Parameter, Parameter> {
		private final int i;
		private final CharSequence cs;
		private Replace(int i, CharSequence cs) {
			this.i = i;
			this.cs = cs;
		}
		@Override
		public Parameter apply(Parameter t) {
			final ParameterBuilder b = parameterBuilder(t.getName());
			int i2 = 0;
			for (final ParameterValue v2 : t.getValues()) {
				if (i == i2 ++) {
					b.addRawCharSequence(cs);
				} else {
					b.addValue(v2);
				}
			}
			return b.build();
		}
	}
	private static final class ReplaceAll implements Function<Parameter, Parameter> {
		private final CharSequence[] css;
		private ReplaceAll(CharSequence... css) {
			this.css = css;
		}
		@Override
		public Parameter apply(Parameter t) {
			final ParameterBuilder b = parameterBuilder(t.getName());
			for (final CharSequence cs : css) {
				b.addRawCharSequence(cs);
			}
			return b.build();
		}
	}
	private static final class ReplaceAllWithP implements Function<Parameter, Parameter> {
		private final Parameter p;
		private ReplaceAllWithP(Parameter p) {
			this.p = p;
		}
		@Override
		public Parameter apply(Parameter t) {
			final ParameterBuilder b = parameterBuilder(t.getName());
			for (final ParameterValue v : p.getValues()) {
				b.addValue(v);
			}
			return b.build();
		}
	}
	private static class SyntheticPredicate implements Predicate<Parameter> {
		private List<Predicate<Parameter>> inners;
		SyntheticPredicate(List<Predicate<Parameter>> inners) {
			this.inners = inners;
		}
		@Override
		public boolean test(Parameter t) {
			for (final Predicate<Parameter> pred : inners) {
				if (!pred.test(t)) {
					return false;
				}
			}
			return true;
		}
	}
	
	protected final WhenThenList whenThenList;
	protected final NormalizerFactory<Q> queryFactory;
	protected final List<Predicate<Parameter>> currentWhenPreds;
	
	DefaultThenClause(NormalizerFactory<Q> queryFactory, 
			WhenThenList whenThenList,
			List<Predicate<Parameter>> currentWhenPreds) {
		this.queryFactory = queryFactory;
		this.whenThenList = whenThenList;
		this.currentWhenPreds = currentWhenPreds;
	}
	
	protected static List<Predicate<Parameter>> addLast(List<Predicate<Parameter>> list, Predicate<Parameter> item) {
		final LinkedList<Predicate<Parameter>> newList = new LinkedList<Predicate<Parameter>>();
		newList.addAll(list);
		newList.addLast(item);
		return newList;
	}
	
	private Q createQuery(Function<Parameter, Parameter> ope) {
		return queryFactory.apply(whenThenList.cons
		(new SyntheticPredicate(currentWhenPreds), ope));
	}
	
	@Override
	public Q thenAppend(CharSequence cs) {
		return createQuery(new Append(cs));
	}

	@Override
	public Q thenInsert(int i, CharSequence cs) {
		return createQuery(new Insert(i, cs));
	}

	@Override
	public Q thenPrepend(CharSequence cs) {
		return createQuery(new Prepend(cs));
	}

	@Override
	public Q thenReplace(int i, CharSequence cs) {
		return createQuery(new Replace(i, cs));
	}

	@Override
	public Q thenReplaceAll(CharSequence... css) {
		return createQuery(new ReplaceAll(css));
	}

	@Override
	public Q thenReplaceAll(Parameter p) {
		return createQuery(new ReplaceAllWithP(p));
	}
}
