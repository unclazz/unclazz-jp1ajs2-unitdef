package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.List;

import org.unclazz.jp1ajs2.unitdef.FullQualifiedName;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

import static org.unclazz.jp1ajs2.unitdef.util.ListUtils.*;

/**
 * {@link FullQualifiedName}のためのビルダー.
 */
public final class FullQualifiedNameBuilder {
	FullQualifiedNameBuilder() {}
	
	private final List<CharSequence> list = linkedList();
	
	/**
	 * 完全名のフラグメントを追加する.
	 * @param fragment フラグメント
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}である場合
	 * @throws IllegalArgumentException 引数の文字シーケンスの長さが{@code 0}であるか、
	 * 文字シーケンスに{@code '/'}が含まれいてる場合
	 */
	public FullQualifiedNameBuilder addFragment(CharSequence fragment) {
		if (fragment == null) {
			throw new NullPointerException("fragment of fqn must not be null.");
		}
		if (fragment.length() == 0) {
			throw new IllegalArgumentException("fragment of fqn must not be empty.");
		}
		if (CharSequenceUtils.indexOf(fragment, '/') != -1) {
			throw new IllegalArgumentException("unit-name must not contain '/'.");
		}
		list.add(fragment);
		return this;
	}
	/**
	 * 完全名のフラグメントを追加する.
	 * @param fragments フラグメント
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}であるか、要素に{@code null}が含まれている場合
	 * @throws IllegalArgumentException 引数の文字シーケンスの長さが{@code 0}であるか、
	 * 文字シーケンスに{@code '/'}が含まれいてる場合
	 */
	public FullQualifiedNameBuilder addFragments(final List<CharSequence> fragments) {
		for (final CharSequence cs : fragments) {
			addFragment(cs);
		}
		return this;
	}
	/**
	 * 完全名のフラグメントを追加する.
	 * @param fragments フラグメント
	 * @return ビルダー
	 * @throws NullPointerException 引数の値が{@code null}であるか、要素に{@code null}が含まれている場合
	 * @throws IllegalArgumentException 引数の文字シーケンスの長さが{@code 0}であるか、
	 * 文字シーケンスに{@code '/'}が含まれいてる場合
	 */
	public FullQualifiedNameBuilder addFragments(final CharSequence... fragments) {
		for (final CharSequence cs : fragments) {
			addFragment(cs);
		}
		return this;
	}
	/**
	 * 新しい{@link FullQualifiedName}インスタンスを生成する.
	 * @return 新しい{@link FullQualifiedName}インスタンス
	 */
	public FullQualifiedName build() {
		notNullAndNotEmpty(list);
		return new DefaultFullQualifiedName(list);
	}
	
	private void notNullAndNotEmpty(final List<CharSequence> args) {
		if (args.isEmpty()) {
			throw new IllegalArgumentException();
		}
		for (final CharSequence arg : args) {
			if (arg == null) {
				throw new NullPointerException();
			}
			if (arg.length() == 0) {
				throw new IllegalArgumentException();
			}
		}
	}
}
