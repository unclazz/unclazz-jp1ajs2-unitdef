package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.unclazz.jp1ajs2.unitdef.Attributes;
import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

/**
 * {@link Unit}のためのビルダー.
 * <p>ビルダーは各種セッターが呼び出されたときと{@link #build()}メソッドが呼び出されたとき、
 * 最低限ユニット定義情報が備えるべき項目についてのチェックを行う。
 * ただしユニット種別ごと、ユニット定義パラメータごとの値のバリデーションは行わない。</p>
 */
public final class UnitBuilder {
	UnitBuilder() {}
	private FullQualifiedName fqn;
	private Attributes attributes;
	private final List<Parameter> parameterList = new LinkedList<Parameter>();
	private final List<Unit> subUnitList = new LinkedList<Unit>();
	private final Set<String> subUnitNameSet = new HashSet<String>();
	
	/**
	 * ユニット完全名を設定する.
	 * @param fqn ユニット完全名
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}の場合
	 */
	public UnitBuilder setFullQualifiedName(final FullQualifiedName fqn) {
		this.fqn = fqn;
		return this;
	}
	/**
	 * ユニット完全名を設定する.
	 * 指定された文字シーケンス配列の要素はユニット完全名のフラグメントとなる。
	 * @param fragments ユニット完全名のフラグメント
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}である場合か、配列に{@code null}が含まれる場合
	 * @throws IllegalArgumentException 引数の値が空の配列である場合と
	 * {@link FullQualifiedName}インスタンス生成時に同例外が発生した場合
	 */
	public UnitBuilder setFullQualifiedName(final CharSequence... fragments) {
		this.fqn = Builders.fullQualifiedName().addFragments(fragments).build();
		return this;
	}
	/**
	 * ユニット完全名を設定する.
	 * 指定された文字シーケンス・リストの要素はユニット完全名のフラグメントとなる。
	 * @param fragments ユニット完全名のフラグメント
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}である場合か、リストに{@code null}が含まれる場合
	 * @throws IllegalArgumentException 引数の値が空のリストである場合と
	 * {@link FullQualifiedName}インスタンス生成時に同例外が発生した場合
	 */
	public UnitBuilder setFullQualifiedName(final List<CharSequence> fragments) {
		this.fqn = Builders.fullQualifiedName().addFragments(fragments).build();
		return this;
	}
	/**
	 * ユニット属性パラメータを設定する.
	 * @param attrs ユニット属性パラメータ
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}である場合
	 */
	public UnitBuilder setAttributes(final Attributes attrs) {
		if (attrs == null) {
			throw new NullPointerException();
		}
		this.attributes = attrs;
		return this;
	}
	/**
	 * ユニット定義パラメータを追加する.
	 * @param parameter ユニット定義パラメータ
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}である場合
	 */
	public UnitBuilder addParameter(final Parameter parameter) {
		if (parameter == null) {
			throw new NullPointerException();
		}
		parameterList.add(parameter);
		return this;
	}
	/**
	 * ユニット定義パラメータを追加する.
	 * @param parameters ユニット定義パラメータ
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}である場合と配列に{@code null}が含まれる場合
	 */
	public UnitBuilder addParameters(final Parameter... parameters) {
		for (final Parameter Parameter : parameters) {
			if (Parameter == null) {
				throw new NullPointerException();
			}
			parameterList.add(Parameter);
		}
		return this;
	}
	/**
	 * ユニット定義パラメータを追加する.
	 * @param parameters ユニット定義パラメータ
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}である場合とリストに{@code null}が含まれる場合
	 */
	public UnitBuilder addParameters(final List<Parameter> parameters) {
		for (final Parameter Parameter : parameters) {
			if (Parameter == null) {
				throw new NullPointerException();
			}
			parameterList.add(Parameter);
		}
		return this;
	}
	/**
	 * サブユニットを追加する.
	 * @param unit サブユニット
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}である場合
	 */
	public UnitBuilder addSubUnit(final Unit unit) {
		if (unit == null) {
			throw new NullPointerException();
		}
		if (subUnitNameSet.add(unit.getAttributes().getUnitName())) {
			subUnitList.add(unit);
			return this;
		}
		throw new IllegalArgumentException("duplicated unit name");
	}
	/**
	 * サブユニットを追加する.
	 * @param units サブユニット
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}である場合と配列に{@code null}が含まれる場合
	 */
	public UnitBuilder addSubUnits(final Unit... units) {
		for (final Unit unit : units) {
			addSubUnit(unit);
		}
		return this;
	}
	/**
	 * サブユニットを追加する.
	 * @param units サブユニット
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}である場合とリストに{@code null}が含まれる場合
	 */
	public UnitBuilder addSubUnits(final List<Unit> units) {
		for (final Unit unit : units) {
			if (unit == null) {
				throw new NullPointerException();
			}
			subUnitList.add(unit);
		}
		return this;
	}
	/**
	 * 新しい{@link Unit}インスタンスを生成する.
	 * @return 新しい{@link Unit}インスタンス
	 * @throws NullPointerException ユニット完全名やユニット属性パラメータが未指定の場合
	 * @throws IllegalArgumentException ユニット定義パラメータtyが未指定の場合や
	 * ユニット完全名のユニット名とユニット属性パラメータのユニット名が不一致の場合
	 */
	public Unit build() {
		if (fqn == null || attributes == null) {
			throw new NullPointerException();
		}
		if (!hasParameterTY()) {
			throw new IllegalArgumentException("parameter \"ty\" must be specified.");
		}
		if (!hasConsistencyBetweenFqnAndAttributes()) {
			throw new IllegalArgumentException("unit must have consistency between "
					+ "unit-name of full-qualified-name and unit-name of attributes.");
		}
		return new DefaultUnit(fqn, attributes, parameterList, subUnitList);
	}
	
	private boolean hasParameterTY() {
		for (final Parameter p : parameterList) {
			if (p.getName().equals("ty")) {
				return true;
			}
		}
		return false;
	}
	
	private boolean hasConsistencyBetweenFqnAndAttributes() {
		final CharSequence fqnUnitName = fqn.getUnitName();
		final CharSequence attesUnitName = attributes.getUnitName();
		return CharSequenceUtils.contentsAreEqual(fqnUnitName, attesUnitName);
	}
}
