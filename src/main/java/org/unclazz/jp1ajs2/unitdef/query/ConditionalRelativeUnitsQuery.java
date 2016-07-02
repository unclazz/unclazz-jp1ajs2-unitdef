package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;
import org.unclazz.jp1ajs2.unitdef.query2.Queries;
import org.unclazz.jp1ajs2.unitdef.query2.Query;

/**
 * 関連ユニットのうち何かしらの条件を満たすユニットを問合せるクエリ.
 * <p>このクラスのインスタンスは{@link RelativeUnitsQuery}を通じて得られる。</p>
 * <p>名前がand...で始まる各種メソッドを利用することで、
 * このクラスのインスタンスが初期化される際に指定された条件に、新たな条件を付け加えることができる。
 * こうして合成されたクエリの問合せ結果には、当初の条件と新たな条件のいずれをも満たすユニットだけが含まれる。</p>
 */
public final class ConditionalRelativeUnitsQuery extends FunctionalListUnitQuery<Unit> {
	private final RelativeUnitsQuery q;
	private final Function<Unit, Unit> f;
	
	ConditionalRelativeUnitsQuery(final RelativeUnitsQuery q, final Function<Unit, Unit> f) {
		// RelativeUnitsQueryとFunctionをもとにインスタンスを初期化する
		// RelativeUnitsQuery#function()は...#noopを返すだけとわかっているので関数合成は不要
		this.q = q;
		this.f = f;
	}
	ConditionalRelativeUnitsQuery(final ConditionalRelativeUnitsQuery c, final Function<Unit, Unit> g) {
		// ConditionalRelativeUnitsQueryをもとにインスタンスを初期化する
		this.q = c.q;
		this.f = SyntheticFunction.synthesize(c.f, g);
	}
	
	@Override
	protected Function<Unit, Unit> function() {
		return f;
	}
	
	@Override
	protected Iterable<Unit> source(Unit u) {
		return q.source(u);
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseTypeIs(final UnitType t) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getType().equals(t) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseNameEquals(final String n) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getName().equals(n) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseNameStartsWith(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getName().startsWith(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseNameEndsWith(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getName().endsWith(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseNameContains(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getName().contains(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseFqnEquals(final String n) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getFullQualifiedName().toString().equals(n) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseFqnStartsWith(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getFullQualifiedName().toString().startsWith(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseFqnEndsWith(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getFullQualifiedName().toString().endsWith(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoseFqnContains(final String s) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getFullQualifiedName().toString().contains(s) ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoHasChildren() {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getSubUnits().size() > 0 ? target : null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoHasChild(final Function<Unit, Boolean> f) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				for (final Unit s : target.getSubUnits()) {
					if (f.apply(s)) {
						return target;
					}
				}
				return null;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoHasChild(final String n) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				return target.getSubUnit(n) == null ? null : target;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoHasParameter(final String n) {
		return new Function<Unit, Unit>() {
			private final Query<Unit, Parameter> q = Queries.parameters().nameEquals(n).one(true);
			@Override
			public Unit apply(Unit target) {
				return target.query(q) == null ? null : target;
			}
		};
	}
	
	static final Function<Unit, Unit> returnsUnitWhoHasParameter(final Function<Parameter, Boolean> f) {
		return new Function<Unit, Unit>() {
			@Override
			public Unit apply(Unit target) {
				for (final Parameter p : target.getParameters()) {
					if (f.apply(p)) {
						return target;
					}
				}
				return null;
			}
		};
	}
	
	/**
	 * もとの条件にユニット種別の条件を加えた新しいクエリを返す.
	 * @param t ユニット種別
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andTypeIs(final UnitType t) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseTypeIs(t));
	}

	/**
	 * もとの条件にユニット名の条件を加えた新しいクエリを返す.
	 * @param n ユニット名
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andNameEquals(final String n) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseNameEquals(n));
	}

	/**
	 * もとの条件にユニット名の条件を加えた新しいクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andNameStartsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseNameStartsWith(s));
	}

	/**
	 * もとの条件にユニット名の条件を加えた新しいクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andNameEndsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseNameEndsWith(s));
	}

	/**
	 * もとの条件にユニット名の条件を加えた新しいクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andNameContains(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseNameEndsWith(s));
	}

	/**
	 * もとの条件に完全名の条件を加えた新しいクエリを返す.
	 * @param n ユニット完全名
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andFqnEquals(final String n) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseFqnEquals(n));
	}

	/**
	 * もとの条件に完全名の条件を加えた新しいクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andFqnStartsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseFqnStartsWith(s));
	}

	/**
	 * もとの条件に完全名の条件を加えた新しいクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andFqnEndsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseFqnEndsWith(s));
	}

	/**
	 * もとの条件に完全名の条件を加えた新しいクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andFqnContains(final String s) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoseFqnEndsWith(s));
	}

	/**
	 * もとの条件にサブユニットの条件を加えた新しいクエリを返す.
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andHasChildren() {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoHasChildren());
	}

	/**
	 * もとの条件にサブユニットの条件を加えた新しいクエリを返す.
	 * @param f サブユニットが条件を満たすかどうかテストする関数
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andHasChild(final Function<Unit, Boolean> f) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoHasChild(f));
	}

	/**
	 * もとの条件にサブユニットの条件を加えた新しいクエリを返す.
	 * @param n サブユニットの名前
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andHasChild(final String n) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoHasChild(n));
	}

	/**
	 * もとの条件にユニット定義パラメータの条件を加えた新しいクエリを返す.
	 * @param n パラメータ名
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andHasParameter(final String n) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoHasParameter(n));
	}

	/**
	 * もとの条件にユニット定義パラメータの条件を加えた新しいクエリを返す.
	 * @param f ユニット定義パラメータが条件を満たすかどうかテストする関数
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery andHasParameter(final Function<Parameter, Boolean> f) {
		return new ConditionalRelativeUnitsQuery(this, returnsUnitWhoHasParameter(f));
	}
	
	/**
	 * ユニット名を問合せるクエリを返す.
	 * @return クエリ
	 */
	public FunctionalListUnitQuery<String> asName() {
		return and(new Function<Unit, String>() {
			@Override
			public String apply(final Unit target) {
				return target.getName();
			}
		});
	}
	
	/**
	 * ユニット完全名を問合せるクエリを返す.
	 * @return クエリ
	 */
	public FunctionalListUnitQuery<FullQualifiedName> asFullQualifiedName() {
		return and(new Function<Unit, FullQualifiedName>() {
			@Override
			public FullQualifiedName apply(final Unit target) {
				return target.getFullQualifiedName();
			}
		});
	}
}
