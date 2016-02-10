package org.unclazz.jp1ajs2.unitdef.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;


/**
 * JP1/AJS2のユニット定義を文字列化するオブジェクト.
 */
public class Formatter extends UnitWalker<Appendable> {
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
	 * @param options オプション
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
			walk(unit, builder);
		} catch (final Exception e) {
			if (e.getCause() == null) {
				throw new RuntimeException(e);
			} else {
				throw new RuntimeException(e.getCause());
			}
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
			walk(unit, br);
			br.flush();
		} catch (final Exception e) {
			if (e instanceof IOException) {
				throw (IOException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
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
	 * @param context フォーマット中の文字列
	 * @param depth インデントの深さ
	 * @throws Exception 処理中に何らかのエラーが発生した場合
	 */
	protected void handleIndentation(final int depth, final Appendable context) throws Exception {
		if (useSpacesForTabs) {
			// ユニット定義の“深さ” x タブ幅 ぶんだけ半角空白文字を追加
			for (int i = 0; i < depth * tabWidth; i ++) {
				context.append(' ');
			}
		} else {
			// ユニット定義の“深さ” x タブ幅 ぶんだけタブ文字を追加
			for (int i = 0; i < depth; i ++) {
				context.append('\t');
			}
		}
	}
	
	protected void handleAttrs(final Unit unit, final Appendable context) throws Exception {
		// ユニット定義の開始
		context.append("unit=").append(unit.getAttributes().getUnitName());
		// 許可モードほかの属性をカンマ区切りで列挙
		context.append(',')
		.append(unit.getAttributes().getPermissionMode())
		.append(',')
		.append(unit.getAttributes().getJP1UserName())
		.append(',')
		.append(unit.getAttributes().getResourceGroupName());
		// ユニット定義属性の終了
		context.append(';');
	}
	
	protected void handleEol(final Appendable context) throws Exception {
        context.append(lineSeparator);
	}
	
	@Override
	protected void handleStart(Unit root, Appendable context) {
		// Do nothing.
	}

	@Override
	protected void handleUnitStart(Unit unit, int depth, Appendable context) {
		try {
			// 行頭のインデント
			handleIndentation(depth, context);
			// ユニット属性パラメータのフォーマット
			handleAttrs(unit, context);
			// 改行文字を挿入
			handleEol(context);
			// 行頭のインデント
			handleIndentation(depth, context);
			// パラメータ群・サブユニット群のまえに波括弧
			context.append('{');
			handleEol(context);
		} catch (final Exception e) {
			throw new CancelException(e, unit, depth);
		}
	}

	@Override
	protected void handleUnitEnd(Unit unit, int depth, Appendable context) {
		try {
			// 行頭のインデント
			handleIndentation(depth, context);
			// パラメータ群・サブユニット群のあとに波括弧
			context.append('}');
			handleEol(context);
		} catch (final Exception e) {
			throw new CancelException(e, unit, depth);
		}
	}

	@Override
	protected void handleParam(Unit unit, Parameter param, int depth, Appendable context) {
		try {
			handleIndentation(depth, context);
			// パラメータ名
			context.append(param.getName());
			// パラメータ値
			for (int i = 0; i < param.getValueCount(); i ++) {
				// 先頭の要素のまえには"="を、後続の要素のまえには","をそれぞれ挿入
				context.append(i == 0 ? '=' : ',').append(param.getValue(i).toString());
			}
			// 行末処理
			context.append(';');
			handleEol(context);
		} catch (final Exception e) {
			throw new CancelException(e, unit, depth);
		}
	}

	@Override
	protected void handleEnd(Unit root, Appendable context) {
		// Do nothing.
	}	
}
