package com.m12i.jp1ajs2.unitdef.core;

import java.util.List;

public interface Param {
	/**
	 * @return ユニット定義パラメータ名
	 */
	String getName();
	/**
	 * @return ユニット定義パラメータ値のリスト
	 */
	List<ParamValue> getValues();
	/**
	 * @return ユニット定義パラメータ値
	 */
	String getValue();
}
