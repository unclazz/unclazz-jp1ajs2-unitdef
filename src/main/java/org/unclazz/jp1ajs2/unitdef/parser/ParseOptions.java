package org.unclazz.jp1ajs2.unitdef.parser;

/**
 * パース処理のオプションを指定するためのオブジェクト.
 */
public class ParseOptions {
	/**
	 * 行コメントの開始文字列.
	 */
	private String lineCommentStart = "//";
	/**
	 * ブロックコメントの開始文字列.
	 */
	private String blockCommentStart = "/*";
	/**
	 * ブロックコメントの終了文字列.
	 */
	private String blockCommentEnd = "*/";
	/**
	 * 二重引用符で囲われた文字列用のエスケープ文字.
	 */
	private char escapePrefixInDoubleQuotes = '\\';
	/**
	 * 一重引用符で囲われた文字列用のエスケープ文字.
	 */
	private char escapePrefixInSingleQuotes = '\\';
	/**
	 * バッククオートで囲われた文字列用のエスケープ文字.
	 */
	private char escapePrefixInBackQuotes = '\\';
	/**
	 * 空白文字をスキップする際にコメントもスキップするかどうか.
	 */
	private boolean skipCommentWithWhitespace = true;
	public String getLineCommentStart() {
		return lineCommentStart;
	}
	public void setLineCommentStart(String lineCommentStart) {
		this.lineCommentStart = lineCommentStart;
	}
	public String getBlockCommentStart() {
		return blockCommentStart;
	}
	public void setBlockCommentStart(String blockCommentStart) {
		this.blockCommentStart = blockCommentStart;
	}
	public String getBlockCommentEnd() {
		return blockCommentEnd;
	}
	public void setBlockCommentEnd(String blockCommentEnd) {
		this.blockCommentEnd = blockCommentEnd;
	}
	public char getEscapePrefixInDoubleQuotes() {
		return escapePrefixInDoubleQuotes;
	}
	public void setEscapePrefixInDoubleQuotes(char escapePrefixInDoubleQuotes) {
		this.escapePrefixInDoubleQuotes = escapePrefixInDoubleQuotes;
	}
	public char getEscapePrefixInSingleQuotes() {
		return escapePrefixInSingleQuotes;
	}
	public void setEscapePrefixInSingleQuotes(char escapePrefixInSingleQuotes) {
		this.escapePrefixInSingleQuotes = escapePrefixInSingleQuotes;
	}
	public char getEscapePrefixInBackQuotes() {
		return escapePrefixInBackQuotes;
	}
	public void setEscapePrefixInBackQuotes(char escapePrefixInBackQuotes) {
		this.escapePrefixInBackQuotes = escapePrefixInBackQuotes;
	}
	public boolean isSkipCommentWithWhitespace() {
		return skipCommentWithWhitespace;
	}
	public void setSkipCommentWithWhitespace(boolean skipCommentWithWhitespace) {
		this.skipCommentWithWhitespace = skipCommentWithWhitespace;
	}
}
