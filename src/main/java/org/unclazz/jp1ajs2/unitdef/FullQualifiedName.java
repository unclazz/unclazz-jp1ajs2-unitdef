package org.unclazz.jp1ajs2.unitdef;

import java.util.List;

/**
 * ユニット完全名を表わすインターフェース.
 */
public interface FullQualifiedName {
	/**
	 * 完全名を構成するフラグメントを返す.
	 * @return フラグメント
	 */
	List<CharSequence> getFragments();
	/**
	 * 上位のユニットの完全名を返す.
	 * @return 上位ユニットの完全名
	 */
	FullQualifiedName getSuperUnitName();
	/**
	 * 完全名のうち当該ユニット名に当たる文字シーケンスを返す.
	 * @return ユニット名
	 */
	CharSequence getUnitName();
	/**
	 * 完全名の末尾に下位ユニットの名称を連結した新しい完全名を返す.
	 * @param name 下位ユニットの名称
	 * @return 下位ユニットの完全名
	 * @throws NullPointerException 引数の値として{@code null}が指定された場合
	 * @throws IllegalArgumentException 引数の値として空の文字シーケンスが指定された場合、
	 * もしくは文字シーケンスが{@code '/'}を含んでいる場合
	 */
	FullQualifiedName getSubUnitName(final CharSequence name);
}
