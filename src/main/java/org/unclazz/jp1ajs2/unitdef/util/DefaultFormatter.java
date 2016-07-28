package org.unclazz.jp1ajs2.unitdef.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.unclazz.jp1ajs2.unitdef.Parameter;
import org.unclazz.jp1ajs2.unitdef.Unit;

final class DefaultFormatter extends UnitWalker<Appendable> implements Formatter {
	public static class DefaultOptions implements Options {
		private String lineSeparator = "\r\n";
		private boolean useSpacesForTabs = false;
		private boolean useIndentAndLineBreak = true;
		private int tabWidth = 4;
		public void setLineSeparator(String s) {
			this.lineSeparator = s;
		}
		public void setUseSpacesForTabs(boolean useSpacesForTabs) {
			this.useSpacesForTabs = useSpacesForTabs;
		}
		public void setTabWidth(int i) {
			this.tabWidth = i;
		}
		public void setUseIndentAndLineBreak(boolean useIndentAndLineBreak) {
			this.useIndentAndLineBreak = useIndentAndLineBreak;
		}
		public String getLineSeparator() {
			return lineSeparator;
		}
		public boolean getUseSpacesForTabs() {
			return useSpacesForTabs;
		}
		public int getTabWidth() {
			return tabWidth;
		}
		public boolean getUseIndentAndLineBreak() {
			return useIndentAndLineBreak;
		}
	}
	
	private final String lineSeparator;
	private final boolean useSpacesForTabs;
	private final int tabWidth;
	private final boolean useIndentAndLineBreak;
	
	DefaultFormatter(final Options options) {
		lineSeparator = options.getLineSeparator();
		useSpacesForTabs = options.getUseSpacesForTabs();
		tabWidth = options.getTabWidth();
		useIndentAndLineBreak = options.getUseIndentAndLineBreak();
	}
	
	@Override
	public CharSequence format(final Unit unit) {
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
		return builder;
	}
	
	@Override
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
	
	@Override
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
		if (!useIndentAndLineBreak) {
			return;
		}
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
		.append(unit.getAttributes().getPermissionMode().toString())
		.append(',')
		.append(unit.getAttributes().getJP1UserName())
		.append(',')
		.append(unit.getAttributes().getResourceGroupName());
		// ユニット定義属性の終了
		context.append(';');
	}
	
	protected void handleEol(final Appendable context) throws Exception {
		if (useIndentAndLineBreak) {
			context.append(lineSeparator);
		}
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
			for (int i = 0; i < param.getValues().size(); i ++) {
				// 先頭の要素のまえには"="を、後続の要素のまえには","をそれぞれ挿入
				context.append(i == 0 ? '=' : ',').append(param.getValues().get(i).toString());
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
