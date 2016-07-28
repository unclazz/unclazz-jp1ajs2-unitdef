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
import org.unclazz.jp1ajs2.unitdef.query.ParameterConditionalModifier.ModifierFactory;
import org.unclazz.jp1ajs2.unitdef.query.ParameterConditionalModifier.ThenClause;
import org.unclazz.jp1ajs2.unitdef.query.ParameterConditionalModifier.WhenThenList;
import org.unclazz.jp1ajs2.unitdef.query.ParameterConditionalModifier.WhenValueAtNAndClause;
import org.unclazz.jp1ajs2.unitdef.query.ParameterConditionalModifier.WhenValueAtNClause;
import org.unclazz.jp1ajs2.unitdef.query.ParameterConditionalModifier.WhenValueCountNClause;
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
 * {@link SingleParameterConditionalModifier}（インスタンスは{@link ParameterQueries#normalize()}メソッドを通じて得られる）。</p>
 * 
 * <p>利用にあたってはまずwhen...系メソッドでパラメータ値の位置や数を指定し、equals...やmatches...などのメソッドで条件値を指定、
 * 続いてthen...系メソッドで条件にマッチしたパラメータに対する変更操作を指定する。when...系メソッドを呼び出すたびに、
 * 新しい正規化（変更操作）のエントリーの生成がはじまりthen...系メソッドによりエントリーが内部的に保存される。</p>
 * 
 * @param <Q> このファクトリが生成するクエリの型
 */
public interface ParameterConditionalModifier<Q extends ParameterConditionalModifier<Q>> {
	/**
	 * {@code whenValueAt(int)}系メソッドのあとに呼び出し可能なメソッドを提供するインターフェース.
	 * <p>ユニット定義パラメータの値を正規化するための特殊なクエリ・ファクトリ{@link ParameterConditionalModifier}とともに使用する。</p>
	 * @param <Q> ファクトリが生成するクエリの型
	 */
	public static interface WhenValueAtNClause<Q extends ParameterConditionalModifier<Q>> {
		/**
		 * パラメータ値がアルファベットのみから構成されるという条件を追加する.
		 * @return {@link WhenValueAtNAndClause}インスタンス
		 */
		WhenValueAtNAndClause<Q> consistsOfAlphabets();
		/**
		 * パラメータ値が数字のみから構成されるという条件を追加する.
		 * @return {@link WhenValueAtNAndClause}インスタンス
		 */
		WhenValueAtNAndClause<Q> consistsOfDigits();
		/**
		 * パラメータ値が指定された文字列を含むという条件を追加する.
		 * @return {@link WhenValueAtNAndClause}インスタンス
		 */
		WhenValueAtNAndClause<Q> contains(CharSequence cs);
		/**
		 * パラメータ値が指定された文字列と一致するという条件を追加する.
		 * @return {@link WhenValueAtNAndClause}インスタンス
		 */
		WhenValueAtNAndClause<Q> contentEquals(CharSequence cs);
		/**
		 * パラメータ値が指定された文字列で終わるという条件を追加する.
		 * @return {@link WhenValueAtNAndClause}インスタンス
		 */
		WhenValueAtNAndClause<Q> endsWith(CharSequence cs);
		/**
		 * パラメータ値が指定された文字列のいずれかと一致するという条件を追加する.
		 * @return {@link WhenValueAtNAndClause}インスタンス
		 */
		WhenValueAtNAndClause<Q> equalsAnyOf(CharSequence...cs);
		/**
		 * パラメータ値が指定された正規表現にマッチするという条件を追加する.
		 * @return {@link WhenValueAtNAndClause}インスタンス
		 */
		WhenValueAtNAndClause<Q> matches(Pattern re);
		/**
		 * パラメータ値が指定された文字列で始まるという条件を追加する.
		 * @return {@link WhenValueAtNAndClause}インスタンス
		 */
		WhenValueAtNAndClause<Q> startsWith(CharSequence cs);
		/**
		 * パラメータ値が指定されたタイプであるという条件を追加する.
		 * @return {@link WhenValueAtNAndClause}インスタンス
		 */
		WhenValueAtNAndClause<Q> typeIs(ParameterValueType t);
	}
	/**
	 * {@code whenValueAt(int).equals...(...)}系メソッドのあとに呼び出し可能なメソッドを提供するインターフェース.
	 * <p>ユニット定義パラメータの値を正規化するための特殊なクエリ・ファクトリ{@link ParameterConditionalModifier}とともに使用する。</p>
	 * @param <Q> ファクトリが生成するクエリの型
	 */
	public static interface WhenValueAtNAndClause<Q extends ParameterConditionalModifier<Q>> extends WhenValueAtNClause<Q>, ThenClause<Q> {
		WhenValueAtNClause<Q> whenValueAt(int i);
		WhenValueCountNClause<Q> whenValueCount(int c);
	}
	/**
	 * {@code whenValueCount(int)}メソッド呼び出しのあとに呼び出し可能なメソッドを提供するインターフェース.
	 * <p>ユニット定義パラメータの値を正規化するための特殊なクエリ・ファクトリ{@link ParameterConditionalModifier}とともに使用する。</p>
	 * @param <Q> ファクトリが生成するクエリの型
	 */
	public static interface WhenValueCountNClause<Q extends ParameterConditionalModifier<Q>> extends ThenClause<Q> {
		WhenValueAtNClause<Q> valueAt(int i);
	}
	/**
	 * {@code whenValueAt(int).equals...(...)}系メソッドや{@code whenValueCount(int)}メソッドのあとに
	 * 呼び出し可能なメソッドを提供するインターフェース.
	 * <p>ユニット定義パラメータの値を正規化するための特殊なクエリ・ファクトリ{@link ParameterConditionalModifier}とともに使用する。</p>
	 * @param <Q> ファクトリが生成するクエリの型
	 */
	public static interface ThenClause<Q extends ParameterConditionalModifier<Q>> {
		/**
		 * 指定された文字列を末尾のパラメータ値として追加するというロジックを追加する.
		 * @param cs 文字列
		 * @return クエリ
		 */
		Q thenAppend(CharSequence cs);
		/**
		 * 指定された位置に指定された文字列をパラメータ値として挿入するというロジックを追加する.
		 * @param i 位置
		 * @param cs 文字列
		 * @return クエリ
		 */
		Q thenInsert(int i, CharSequence cs);
		/**
		 * 指定された文字列を0番目のパラメータ値として追加するというロジックを追加する.
		 * @param cs 文字列
		 * @return クエリ
		 */
		Q thenPrepend(CharSequence cs);
		/**
		 * 指定された指定された位置のパラメータ値を指定された文字列で置換するというロジックを追加する.
		 * @param i 位置
		 * @param cs 文字列
		 * @return クエリ
		 */
		Q thenReplace(int i, CharSequence cs);
		/**
		 * パラメータ値のリスト全体を指定された文字列で置換するというロジックを追加する.
		 * @param css 文字列の配列
		 * @return クエリ
		 */
		Q thenReplaceAll(CharSequence... css);
		/**
		 * パラメータ値のリスト全体を指定されたパラメータ値で置換するというロジックを追加する.
		 * @param p パラメータ値
		 * @return クエリ
		 */
		Q thenReplaceAll(Parameter p);
	}
	/**
	 * {@code then...(...)}系メソッドの呼び出しとともに実行されクエリを生成するファクトリ関数.
	 * <p>ユニット定義パラメータの値を正規化するための特殊なクエリ・ファクトリ{@link ParameterConditionalModifier}とともに使用する。</p>
	 * @param <Q> ファクトリが生成するクエリの型
	 */
	public static interface ModifierFactory<Q extends ParameterConditionalModifier<Q>> 
	extends Function<WhenThenList,Q>{
		@Override
		Q apply(WhenThenList normalize);
	}
	/**
	 * 正規化（変更操作）を起動する条件と正規化のロジックそのものを格納するオブジェクト.
	 */
	static final class WhenThenEntry {
		private final Predicate<Parameter> condition;
		private final Function<Parameter, Parameter> operation;
		public WhenThenEntry(final Predicate<Parameter> cond
				, final Function<Parameter, Parameter> ope) {
			if (cond == null | ope == null) {
				throw new NullPointerException(String.
						format("constructor arguments must not be null. "
								+ "cond = %s, ope = %s.", cond, ope));
			}
			this.condition = cond;
			this.operation = ope;
		}
		/**
		 * @return 正規化の条件
		 */
		public Predicate<Parameter> getCondition() {
			return condition;
		}
		/**
		 * @return 正規化のロジック
		 */
		public Function<Parameter, Parameter> getOperation() {
			return operation;
		}
	}
	/**
	 * {@link WhenThenEntry}を要素とするリスト.
	 * <p>正規化（変更操作）を起動する条件と正規化のロジックそのものの格納と、
	 * それらに基づくユニット定義パラメータの正規化の機能を提供する。
	 * このリストはイミュータブルである。</p>
	 */
	static final class WhenThenList 
	implements Iterable<WhenThenEntry>, Function<Parameter, Parameter> {
		/**
		 * 初期値として空のリストを返す.
		 * @return リスト
		 */
		public static WhenThenList emptyInstance() {
			return empty;
		}
		
		private static final WhenThenList empty = new WhenThenList(new 
						LinkedList<ParameterConditionalModifier.WhenThenEntry>());
		private final LinkedList<WhenThenEntry> list;
		private WhenThenList(LinkedList<WhenThenEntry> list) {
			this.list = list;
		}
		
		/**
		 * リストの末尾に引数で指定された要素を追加した新しいリストを返す.
		 * @param e 追加対象の要素
		 * @return 新しいリスト
		 */
		public WhenThenList cons(WhenThenEntry e) {
			final LinkedList<WhenThenEntry> newList = new LinkedList<WhenThenEntry>();
			newList.addAll(list);
			newList.addLast(e);
			return new WhenThenList(newList);
		}
		/**
		 * リストの末尾に引数で指定された正規化条件と正規化ロジックからなる要素を追加した新しいリストを返す.
		 * @param cond 条件
		 * @param ope ロジック
		 * @return 新しいリスト
		 */
		public WhenThenList cons(final Predicate<Parameter> cond
				, final Function<Parameter, Parameter> ope) {
			return cons(new WhenThenEntry(cond, ope));
		}
		/**
		 * リストの末尾に別のリストを結合した新しいリストを返す.
		 * @param other 別のリスト 
		 * @return 新しいリスト
		 */
		public WhenThenList concat(WhenThenList other) {
			final LinkedList<WhenThenEntry> newList = new LinkedList<WhenThenEntry>();
			newList.addAll(list);
			newList.addAll(other.list);
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
	/**
	 * 引数で指定された位置のパラメータ値について条件指定を開始する.
	 * @param i 位置
	 * @return {@link WhenValueAtNClause}のインスタンス
	 */
	WhenValueAtNClause<Q> whenValueAt(int i);
	/**
	 * 引数で指定された個数のパラメータが存在するかの条件を指定する.
	 * @param c 個数
	 * @return {@link WhenValueAtNClause}のインスタンス
	 */
	WhenValueCountNClause<Q> whenValueCount(int c);
}


class QueryFactoryForParameterIterableQuery implements ModifierFactory<ParameterIterableQuery> {
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
		return new DefaultParameterIterableQuery(baseQuery, preds, n);
	}
}

class DefaultWhenValueCountNClause<Q extends ParameterConditionalModifier<Q>>
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
	DefaultWhenValueCountNClause(ModifierFactory<Q> queryFactory,
			WhenThenList whenThenList, 
			List<Predicate<Parameter>> currentWhenPreds,
			int c) {
		super(queryFactory, whenThenList, addLast(currentWhenPreds, new ValueCount(c)));
		this.c = c;
	}
	DefaultWhenValueCountNClause(ModifierFactory<Q> queryFactory, int c) {
		this(queryFactory, WhenThenList.emptyInstance(), 
				Collections.<Predicate<Parameter>>emptyList(), c);
	}
	@Override
	public WhenValueAtNClause<Q> valueAt(int i) {
		return new DefaultWhenValueAtNClause<Q>(queryFactory, whenThenList, 
				currentWhenPreds, i);
	}
}

class DefaultWhenValueAtNAndClause<Q extends ParameterConditionalModifier<Q>> 
extends DefaultWhenValueAtNClause<Q>
implements WhenValueAtNAndClause<Q> {
	DefaultWhenValueAtNAndClause(ModifierFactory<Q> queryFactory,
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

class DefaultWhenValueAtNClause<Q extends ParameterConditionalModifier<Q>> 
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
	DefaultWhenValueAtNClause(ModifierFactory<Q> queryFactory,
			WhenThenList whenThenList, 
			List<Predicate<Parameter>> currentWhenPreds, 
			int i) {
		super(queryFactory, whenThenList, currentWhenPreds);
		this.i = i;
	}
	DefaultWhenValueAtNClause(ModifierFactory<Q> queryFactory,int i) {
		super(queryFactory, WhenThenList.emptyInstance(), 
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

class DefaultThenClause<Q extends ParameterConditionalModifier<Q>> implements ThenClause<Q> {
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
	protected final ModifierFactory<Q> queryFactory;
	protected final List<Predicate<Parameter>> currentWhenPreds;
	
	DefaultThenClause(ModifierFactory<Q> queryFactory, 
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
