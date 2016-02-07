package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * ユニット定義パラメータteもしくはscを表わすオブジェクト.
 * <p>teはUNIXジョブにおける実行ファイル名、scはPCジョブにおける実行ファイル名である。
 * いずれも実際には「ファイル名」という域を超えておりコマンドライン文字列の指定が可能となっている。
 * teの場合上限は1023文字、scの場合上限は511文字であるが、
 * このような文脈依存の制約についてはこのオブジェクトでは関知しない。</p>
 */
public final class CommandLine {
	/**
	 * インスタンスを返す.
	 * <p>空白が含まれている場合、コマンドと引数、引数と引数を区切る文字として認識される。
	 * ダブルクオテーションにより、空白文字を含む文字列を単一のコマンドもしくは引数として示すことができる。
	 * またダブルクオテーションで囲われた文字列内でダブルクオテーションを使用する場合は
	 * 直前にバックスラッシュを付与してエスケープする。
	 * バックスラッシュそのものはバックスラッシュによりエスケープする。</p>
	 * 
	 * @param seq コマンドライン文字列
	 * @return インスタンス
	 */
	public static CommandLine of(CharSequence seq) {
		return new CommandLine(splitCommandLine(seq));
	}
	/**
	 * インスタンスを返す.
	 * 空の配列や{@code null}を含む配列、空文字列を含む配列は無効である。
	 * 配列の最初の要素はコマンド（もしくは本来の意味での実行ファイル名）として、
	 * また後続の要素はコマンドの引数として認識される。
	 * @param fragments コマンドライン
	 * @return インスタンス
	 */
	public static CommandLine of(String... fragments) {
		return new CommandLine(fragments);
	}
	
	private final String[] fragments;
	private CommandLine(final String[] fragments) {
		final String[] newFragments = new String[fragments.length];
		if (fragments.length == 0) {
			throw new IllegalArgumentException("Command line fragments must be not empty array");
		}
		for (int i = 0; i < newFragments.length; i ++) {
			final String trimmedFragment = fragments[i].trim();
			if (trimmedFragment.length() == 0) {
				throw new IllegalArgumentException("Command line fragment must be not empty string");
			}
			newFragments[i] = trimmedFragment;
		}
		this.fragments = newFragments;
	}
	
	/**
	 * コマンド（もしくは本来の意味での実行ファイル名）を返す.
	 * @return コマンド
	 */
	public String getCommand() {
		return fragments[0];
	}
	/**
	 * コマンドライン引数を返す.
	 * @return コマンドライン引数
	 */
	public String[] getArguments() {
		return Arrays.copyOfRange(fragments, 1, fragments.length);
	}
	/**
	 * コマンドラインを構成するコマンドと引数を合わせた文字列配列を返す.
	 * @return コマンドラインの文字列配列
	 */
	public String[] getFragments() {
		return Arrays.copyOf(fragments, fragments.length);
	}
	
	private static String[] splitCommandLine(CharSequence seq) {
		final int len = seq.length();
		final List<String> list = new LinkedList<String>();
		final StringBuilder buff = new StringBuilder();
		boolean quoted = false;
		boolean escaped = false;
		
		for (int i = 0; i < len; i ++) {
			final char ch = seq.charAt(i);
			if (escaped) {
				buff.append(ch);
				escaped = false;
			} else if (ch == '\\') {
				escaped = true;
			} else if (ch == '"') {
				if (quoted) {
					quoted = false;
				} else {
					quoted = true;
				}
				buff.append(ch);
			} else if (ch <= ' ') {
				if (quoted) {
					buff.append(ch);
				} else if (buff.length() > 0) {
					list.add(buff.toString());
					buff.setLength(0);
				} else {
					buff.setLength(0);
				}
			} else {
				buff.append(ch);
			}
		}
		if (quoted) {
			throw new IllegalArgumentException("Unclosed quoted string is found");
		}
		if (escaped) {
			throw new IllegalArgumentException("Escape target is not found");
		}
		if (buff.length() > 0) {
			list.add(buff.toString());
		}
		return list.toArray(new String[list.size()]);
	}
	
	/**
	 * コマンドラインの文字列表現を返す.
	 */
	@Override
	public String toString() {
		final StringBuilder buff = new StringBuilder();
		for (final String f : fragments) {
			if (buff.length() > 0) {
				buff.append(' ');
			}
			buff.append(f);
		}
		return buff.toString();
	}
}
