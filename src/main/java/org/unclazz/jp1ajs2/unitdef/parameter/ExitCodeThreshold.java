package org.unclazz.jp1ajs2.unitdef.parameter;

/**
 * ユニット定義パラメータtho（異常終了閾値）やwth（警告終了閾値）を表わすオブジェクト.
 * 値は0を含む非負の整数値により表され、上限は{@code Integer.MAX_VALUE}である。
 * ある終了コードが異常終了に該当するのか警告終了に該当するのかそのいずれでもない（正常終了）のかは、
 * 当該のユニットに指定されたthoとwthの2値の組み合わせで決まる。
 * したがってこのオブジェクトのみでは異常/警告の判断はできない。
 */
public final class ExitCodeThreshold extends DefaultIntegral {
	/**
	 * インスタンスを返す.
	 * 引数として渡される整数値は{@code (0 < val && val < Integer.MAX_VALUE)}の条件を満たす必要がある。
	 * @param val 整数値
	 * @return インスタンス
	 */
	public static final ExitCodeThreshold of(final int val) {
		if (val < 0) {
			throw new IllegalArgumentException("Threshold value must be between 0 and 2147483647");
		}
		return new ExitCodeThreshold(val);
	}
	
	private ExitCodeThreshold(int val) {
		super(val);
	}
	/**
	 * 引数で指定された値が閾値を超えているかどうか判定する.
	 * @param val 判定対象の値
	 * @return 閾値を超えている場合{@code true}
	 */
	public boolean isExceededWith(int val) {
		return intValue() < val;
	}
}
