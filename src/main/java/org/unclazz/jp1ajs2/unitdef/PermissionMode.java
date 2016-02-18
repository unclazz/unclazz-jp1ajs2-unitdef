package org.unclazz.jp1ajs2.unitdef;

import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;

/**
 * ユニット属性パラメータの許可モードを表わすインターフェース.
 * <p>ユニット定義上8進数4桁の数字で記述されるが、左端1桁以外は意味を持たない。
 * 左端1桁はジョブ実行時のユーザーの扱いを表す：</p>
 * <ul>
 * <li>{@code 0}から{@code 3}の場合、ジョブネットを登録したJP1ユーザをジョブ実行時のユーザとする</li>
 * <li>{@code 4}から{@code 7}の場合、ジョブを所有するJP1ユーザをジョブ実行時のユーザとする</li>
 * </ul>
 * <p>ユニット定義パラメータeuの指定がある場合はそちらが優先される。</p>
 * <p>このパラメータはユニット定義上パラメータが省略が可能である。
 * パラメータが省略されている場合、このインターフェースのメソッドは特殊な値を返す。</p>
 */
public interface PermissionMode {
	/**
	 * 許可モードが指定されている（省略されていない）かどうか判定する.
	 * @return 指定されている場合{@code true}
	 */
	boolean isSpecified();
	/**
	 * ユニット定義上で指定された文字列を返す.
	 * パラメータの指定が省略されていた場合は{@code ""}を返す。
	 * @return 文字列
	 */
	String getValue();
	/**
	 * パラメータの整数表現を返す.
	 * パラメータの指定が省略されていた場合は{@code -1}を返す。
	 * @return 整数
	 */
	int intValue();
	/**
	 * ジョブ実行時のユーザの扱いの指定値を返す.
	 * パラメータの指定が省略されていた場合は{@code null}を返す。
	 * @return ジョブ実行時のユーザの扱い
	 */
	ExecutionUserType getExecutionUserType();
}
