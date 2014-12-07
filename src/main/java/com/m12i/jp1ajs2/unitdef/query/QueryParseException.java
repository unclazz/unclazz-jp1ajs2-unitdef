package com.m12i.jp1ajs2.unitdef.query;

/**
 * クエリ内容に問題があり解析に失敗したことを示す例外.
 */
public class QueryParseException extends Exception {
	private static final long serialVersionUID = 1506417149680232859L;
	QueryParseException(Throwable cause) {
		super(cause);
	}
}
