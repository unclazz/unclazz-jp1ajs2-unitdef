package com.m12i.jp1ajs2.unitdef.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import com.m12i.jp1ajs2.unitdef.Param;
import com.m12i.jp1ajs2.unitdef.Unit;


/**
 * JP1/AJS2のユニット定義を文字列化するオブジェクト.
 */
public class Formatter {
	/**
	 * JP1/AJS2のユニット定義を文字列化する際のオプション.
	 */
	public static class FormatOptions {
		private String lineSeparator = "\r\n";
		private boolean useSpacesForTabs = false;
		private int tabWidth = 4;
		/**
		 * 行区切り文字列を設定する.
		 * @param s 行区切り文字列
		 */
		public void setLineSeparator(String s) {
			this.lineSeparator = s;
		}
		/**
		 * タブの代わりに半角空白文字を使用するかどうかの設定をする.
		 * @param useSpacesForTabs 設定値
		 */
		public void setUseSpacesForTabs(boolean useSpacesForTabs) {
			this.useSpacesForTabs = useSpacesForTabs;
		}
		/**
		 * タブ幅を設定する.
		 * @param i タブ幅
		 */
		public void setTabWidth(int i) {
			this.tabWidth = i;
		}
		/**
		 * 行区切り文字列を取得する.
		 * @return 行区切り文字列
		 */
		public String getLineSeparator() {
			return lineSeparator;
		}
		/**
		 * タブの代わりに半角空白文字を使用するかどうかの設定値を取得する.
		 * @return 設定値
		 */
		public boolean getUseSpacesForTabs() {
			return useSpacesForTabs;
		}
		/**
		 * タブ幅を取得する.
		 * @return タブ幅
		 */
		public int getTabWidth() {
			return tabWidth;
		}
	}
	/**
	 * フォーマットしたユニット定義情報断片を追記するインターフェース.<br>
	 * <p>{@link StringBuilder}や{@link BufferedWriter}への参照を抽象化し、
	 * フォーマット化処理の重複を避けるためのインターフェース。
	 * 各種{@code append(...)}メソッドは戻り値として自分自身（レシーバ・オブジェクト）を返さなくてはならない。</p>
	 */
	public static interface Appender {
		Appender append(final CharSequence cs) throws Exception;
		Appender append(final Object o) throws Exception;
		Appender append(final char c) throws Exception;
	}
	
	private static final class StringBuilderAppender implements Appender {
		private final StringBuilder buff;
		StringBuilderAppender(final StringBuilder buff) {
			this.buff = buff;
		}
		@Override
		public Appender append(CharSequence cs) {
			buff.append(cs);
			return this;
		}
		@Override
		public Appender append(Object o) {
			buff.append(o);
			return this;
		}
		@Override
		public Appender append(char c) {
			buff.append(c);
			return this;
		}
	}
	
	private static final class BufferedWriterAppender implements Appender {
		private final BufferedWriter buff;
		BufferedWriterAppender(final BufferedWriter buff) {
			this.buff = buff;
		}
		@Override
		public Appender append(CharSequence cs) throws IOException {
			buff.append(cs);
			return this;
		}
		@Override
		public Appender append(Object o) throws IOException {
			buff.append(o.toString());
			return this;
		}
		@Override
		public Appender append(char c) throws IOException {
			buff.append(c);
			return this;
		}
	}
	
	private final String lineSeparator;
	private final boolean useSpacesForTabs;
	private final int tabWidth;
	
	/**
	 * デフォルトのフォーマット・オプションでフォーマッタを初期化する.
	 */
	public Formatter() {
		this(new FormatOptions());
	}
	
	/**
	 * カスタマイズしたフォーマット・オプションでフォーマッタを初期化する.
	 * @param options
	 */
	public Formatter(final FormatOptions options) {
		lineSeparator = options.getLineSeparator();
		useSpacesForTabs = options.getUseSpacesForTabs();
		tabWidth = options.getTabWidth();
	}
	
	/**
	 * ユニット定義情報オブジェクトをフォーマットする.
	 * @param unit ユニット
	 * @return フォーマットしたユニット定義
	 */
	public String format(final Unit unit) {
		// ヘルパー関数を呼び出してフォーマットを実行
		final StringBuilder builder = new StringBuilder();
		try {
			formatUnit(new StringBuilderAppender(builder), 0, unit);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		return builder.toString();
	}
	
	/**
	 * ユニット定義情報オブジェクトをフォーマットし出力ストリームに書き出す.
	 * @param unit ユニット定義
	 * @param out 出力ストリーム
	 * @param charset キャラクターセット
	 * @throws IOException I/Oエラーが発生した場合
	 */
	public void format(final Unit unit, final OutputStream out, final Charset charset) throws IOException {
		final BufferedWriter br = new BufferedWriter(new OutputStreamWriter(out, charset));
		try {
			formatUnit(new BufferedWriterAppender(br), 0, unit);
		} catch (final Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
		br.close();
	}
	
	/**
	 * ユニット定義情報オブジェクトをフォーマットし出力ストリームに書き出す.<br>
	 * <p>キャラクターセットにはシステムのデフォルトが使用される。</p>
	 * @param unit ユニット定義
	 * @param out 出力ストリーム
	 * @throws IOException I/Oエラーが発生した場合
	 */
	public void format(final Unit unit, final OutputStream out) throws IOException {
		format(unit, out, Charset.defaultCharset());
	}

	/**
	 * 指定されたインデントの深さに基づきタブ文字もしくは半角空白文字を追加する.
	 * @param builder フォーマット中の文字列
	 * @param depth インデントの深さ
	 * @return フォーマット中の文字列（インデント追加済み）
	 * @throws Exception 処理中に何らかのエラーが発生した場合
	 */
	protected void appendSpaces(final Appender builder, final int depth) throws Exception {
		if (useSpacesForTabs) {
			// ユニット定義の“深さ” x タブ幅 ぶんだけ半角空白文字を追加
			for (int i = 0; i < depth * tabWidth; i ++) {
				builder.append(' ');
			}
		} else {
			// ユニット定義の“深さ” x タブ幅 ぶんだけタブ文字を追加
			for (int i = 0; i < depth; i ++) {
				builder.append('\t');
			}
		}
	}
	/**
	 * ユニット定義をフォーマットする.<br>
	 * <p>ユニット定義を構成する情報──ユニット属性パラメータ、ユニット定義パラメータ、下位ユニット定義──を文字列化する。
	 * これらの要素の行頭のインデントや行末の改行文字の付加はこのメソッドの責任範囲となる。</p>
	 * <p>デフォルトの実装では、ユニット属性パラメータのフォーマットは{@link #formatAttrs(StringBuilder, Unit)}に、
	 * ユニット定義パラメータのフォーマットは{@link #formatParam(StringBuilder, Param)}に、それぞれ委譲される。
	 * 下位ユニットのフォーマットはこのメソッド自身の再帰呼び出しにより行われる。</p>
	 * @param builder フォーマットした文字列を追記するビルダー
	 * @param depth インデントの深さ
	 * @param unit ユニット属性定義
	 * @throws Exception 処理中に何らかのエラーが発生した場合
	 */
	protected void formatUnit(final Appender builder, final int depth, final Unit unit) throws Exception {
		// 行頭のインデント
		appendSpaces(builder, depth);
		// ユニット属性パラメータのフォーマット
		formatAttrs(builder, unit);
		// 改行文字を挿入
		builder.append(lineSeparator);
		// 行頭のインデント
		appendSpaces(builder, depth);
		// パラメータ群・サブユニット群のまえに波括弧
		builder.append('{').append(lineSeparator);
		// パラメータ群の列挙
		for (final Param p : unit.getParams()) {
			// 行頭のインデント
			appendSpaces(builder, depth + 1);
			// ヘルパー関数で個々のパラメータをフォーマット
			formatParam(builder, p);
			// 改行文字を挿入
			builder.append(lineSeparator);
		}
		// サブユニット群の列挙
		for (final Unit u : unit.getSubUnits()) {
			// 再帰呼び出しによりサブユニットをフォーマット
			formatUnit(builder, depth + 1, u);
		}
		// 行頭のインデント
		appendSpaces(builder, depth);
		// パラメータ群・サブユニット群のあとに波括弧
		builder.append('}').append(lineSeparator);
	}
	
	/**
	 * ユニット属性パラメータ（{@code "unit=(unit name),(permission mode),(owner name),(resource group name);" }）をフォーマットする.<br>
	 * <p>行頭のインデントや行末の改行文字の付与はこのメソッドの呼び出し元で行われるので、
	 * このメソッドでは純粋にユニット属性パラメータ単体を文字列化することに関心を向ければよい。</p>
	 * @param builder フォーマットした文字列を追記するビルダー
	 * @param unit ユニット属性定義
	 * @throws Exception 処理中に何らかのエラーが発生した場合
	 */
	protected void formatAttrs(final Appender builder, final Unit unit) throws Exception {
		// ユニット定義の開始
		builder.append("unit=").append(unit.getName());
		// 許可モードほかの属性をカンマ区切りで列挙
		builder.append(',')
		.append(unit.getPermissionMode().getOrElse(""))
		.append(',')
		.append(unit.getOwnerName().getOrElse(""))
		.append(',')
		.append(unit.getResourceGroupName().getOrElse(""));
		// ユニット定義属性の終了
		builder.append(';');
	}
	
	/**
	 * ユニット定義パラメータ（例：{@code "ty=g;"}）をフォーマットする.<br>
	 * <p>行頭のインデントや行末の改行文字の付与はこのメソッドの呼び出し元で行われるので、
	 * このメソッドでは純粋にユニット定義パラメータ単体を文字列化することに関心を向ければよい。</p>
	 * @param builder フォーマットした文字列を追記するビルダー
	 * @param param ユニット定義パラメータ
	 * @throws Exception 処理中に何らかのエラーが発生した場合
	 */
	protected void formatParam(final Appender builder, final Param param) throws Exception {
		// パラメータ名
		builder.append(param.getName());
		// パラメータ値
		for (int i = 0; i < param.getValues().size(); i ++) {
			// 先頭の要素のまえには"="を、後続の要素のまえには","をそれぞれ挿入
			builder.append(i == 0 ? '=' : ',').append(param.getValues().get(i).toString());
		}
		// 行末処理
		builder.append(';');
	}	
}
