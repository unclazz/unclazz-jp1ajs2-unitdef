package org.unclazz.jp1ajs2.unitdef.query;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

/**
 * 関連ユニットを問合せるクエリの抽象クラス.
 * <p>子ユニットや子孫ユニットを問合せるクエリの具象クラスのインスタンスは{@link UnitQueries}を通じて得られる。</p>
 */
public abstract class RelativeUnitsQuery extends FunctionalListUnitQuery<Unit> {
	RelativeUnitsQuery() {}
	
	/**
	 * いかなる変換も行わない関数オブジェクト.
	 */
	private static final Function<Unit, Unit> noop = new Function<Unit, Unit>() {
		@Override
		public Unit apply(final Unit target) {
			return target;
		}
	};
	
	@Override
	protected Function<Unit, Unit> function() {
		return noop;
	}
	
	/**
	 * 関連ユニットのうちでも指定されたユニット種別に該当するものだけを問合せるクエリを返す.
	 * @param t ユニット種別
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery typeIs(UnitType t) {
		return new ConditionalRelativeUnitsQuery(this, 
				ConditionalRelativeUnitsQuery.returnsUnitWhoseTypeIs(t));
	}
	
	/**
	 * 関連ユニットのうちでも指定された名前のユニットだけを問合せるクエリを返す.
	 * @param n ユニット名
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery nameEquals(String n) {
		return new ConditionalRelativeUnitsQuery(this, 
				ConditionalRelativeUnitsQuery.returnsUnitWhoseNameEquals(n));
	}
	
	/**
	 * 関連ユニットのうちでも指定された文字列で名前が始まるユニットだけを問合せるクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery nameStartsWith(String s) {
		return new ConditionalRelativeUnitsQuery(this, 
				ConditionalRelativeUnitsQuery.returnsUnitWhoseNameStartsWith(s));
	}
	
	/**
	 * 関連ユニットのうちでも指定された文字列で名前が終わるユニットだけを問合せるクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery nameEndsWith(String s) {
		return new ConditionalRelativeUnitsQuery(this, 
				ConditionalRelativeUnitsQuery.returnsUnitWhoseNameEndsWith(s));
	}
	
	/**
	 * 関連ユニットのうちでも指定された文字列が名前に含まれるユニットだけを問合せるクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery nameContains(String s) {
		return new ConditionalRelativeUnitsQuery(this, 
				ConditionalRelativeUnitsQuery.returnsUnitWhoseNameContains(s));
	}

	/**
	 * 関連ユニットのうちでも指定された完全名を持つユニットだけを問合せるクエリを返す.
	 * @param n ユニット完全名
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery fqnEquals(final String n) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoseFqnEquals(n));
	}

	/**
	 * 関連ユニットのうちでも指定された文字列で完全名が始まるユニットだけを問合せるクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery fqnStartsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoseFqnStartsWith(s));
	}

	/**
	 * 関連ユニットのうちでも指定された文字列で完全名が終わるユニットだけを問合せるクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery fqnEndsWith(final String s) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoseFqnEndsWith(s));
	}

	/**
	 * 関連ユニットのうちでも指定された文字列を完全名に含まれるユニットだけを問合せるクエリを返す.
	 * @param s 部分文字列
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery fqnContains(final String s) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoseFqnContains(s));
	}

	/**
	 * 関連ユニットのうちでもサブユニットを持つユニットだけを問合せるクエリを返す.
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery hasChildren() {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoHasChildren());
	}

	/**
	 * 関連ユニットのうちでも任意の条件を満たすサブユニットを持つユニットだけを問合せるクエリを返す.
	 * @param f サブユニットが条件を満たすかどうかテストする関数
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery hasChild(final Function<Unit, Boolean> f) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoHasChild(f));
	}

	/**
	 * 関連ユニットのうちでも指定された名前のサブユニットを持つユニットだけを問合せるクエリを返す.
	 * @param n サブユニットの名前
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery hasChild(final String n) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoHasChild(n));
	}

	/**
	 * 関連ユニットのうちでも指定された名前のユニット定義パラメータを持つユニットだけを問合せるクエリを返す.
	 * @param n パラメータ名
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery hasParameter(final String n) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoHasParameter(n));
	}

	/**
	 * 関連ユニットのうちでも任意の条件を満たすユニット定義パラメータを持つユニットだけを問合せるクエリを返す.
	 * @param f ユニット定義パラメータが条件を満たすかどうかテストする関数
	 * @return クエリ
	 */
	public ConditionalRelativeUnitsQuery hasParameter(final Function<Parameter, Boolean> f) {
		return new ConditionalRelativeUnitsQuery(this,  
				ConditionalRelativeUnitsQuery.returnsUnitWhoHasParameter(f));
	}
	
	/**
	 * 関連ユニットのユニット名を問合せるクエリを返す.
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
	 * 関連ユニットの完全名を問合せるクエリを返す.
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
