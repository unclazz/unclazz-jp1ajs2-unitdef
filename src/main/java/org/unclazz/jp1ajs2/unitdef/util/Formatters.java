package org.unclazz.jp1ajs2.unitdef.util;

import org.unclazz.jp1ajs2.unitdef.util.DefaultFormatter.DefaultOptions;

/**
 * {@link Formatter}インターフェースのためのユーティリティ.
 */
public final class Formatters {
	private Formatters() {}
	
	/**
	 * フォーマッターを構築するためのビルダー.
	 */
	public static final class Builder {
		private Builder() {}
		
		private String lineSeparator = "\r\n";
		private boolean useSpacesForTabs = false;
		private boolean useIndentAndLineBreak = true;
		private int tabWidth = 4;
		/**
		 * 行区切り文字列を設定する.
		 * @param s 行区切り文字列
		 * @return ビルダー
		 */
		public Builder setLineSeparator(String s) {
			this.lineSeparator = s;
			return this;
		}
		/**
		 * タブの代わりに半角空白文字を使用するかどうかの設定をする.
		 * @param useSpacesForTabs 設定値
		 * @return ビルダー
		 */
		public Builder setUseSpacesForTabs(boolean useSpacesForTabs) {
			this.useSpacesForTabs = useSpacesForTabs;
			return this;
		}
		/**
		 * タブ幅を設定する.
		 * @param i タブ幅
		 * @return ビルダー
		 */
		public Builder setTabWidth(int i) {
			this.tabWidth = i;
			return this;
		}
		/**
		 * インデントと改行を使用するかどうかの設定をする.
		 * @param useSpacesForTabs 設定値
		 * @return ビルダー
		 */
		public Builder setUseIndentAndLineBreak(boolean useIndentAndLineBreak) {
			this.useIndentAndLineBreak = useIndentAndLineBreak;
			return this;
		}
		/**
		 * フォーマッターを構築する.
		 * @return フォーマッター
		 */
		public Formatter build() {
			final DefaultOptions ops = new DefaultOptions();
			ops.setLineSeparator(lineSeparator);
			ops.setTabWidth(tabWidth);
			ops.setUseSpacesForTabs(useSpacesForTabs);
			ops.setUseIndentAndLineBreak(useIndentAndLineBreak);
			return new DefaultFormatter(ops);
		}
	}
	
	/**
	 * デフォルトのオプションで初期化されたフォーマッター.
	 */
	public static final Formatter DEFAULT = new DefaultFormatter(new DefaultOptions());
	/**
	 * インデントと改行を行わないオプションで初期化されたフォーマッター.
	 */
	public static final Formatter NO_INDENT = builder().setUseIndentAndLineBreak(false).build();
	
	/**
	 * フォーマッターのビルダーを生成して返す.
	 * @return ビルダー
	 */
	public static Builder builder() {
		return new Builder();
	}
}
