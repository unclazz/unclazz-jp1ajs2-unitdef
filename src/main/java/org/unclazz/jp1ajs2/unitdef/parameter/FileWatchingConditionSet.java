package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;
import org.unclazz.jp1ajs2.unitdef.util.CharSequential;

/**
 * ユニット定義パラメータflwcを表わすオブジェクト.
 * {@link FileWatchingCondition}インスタンスを格納する。
 * {@link FileWatchingConditionSet}のインスタンス生成時、
 * 引数に{@link FileWatchingCondition#CREATE}が指定されていない場合は自動的に補われる。
 * {@link FileWatchingCondition#SIZE}と{@link FileWatchingCondition#MODIFY}の同時設定はできない。
 */
public final class FileWatchingConditionSet implements Iterable<FileWatchingCondition>, CharSequential {
	/**
	 * インスタンスを返す.
	 * @param values 監視条件
	 * @return インスタンス
	 * @throws 同時設定不可能な条件が含まれていた場合
	 */
	public static FileWatchingConditionSet of(FileWatchingCondition... values) {
		final Set<FileWatchingCondition> set = new HashSet<FileWatchingCondition>(3);
		for (final FileWatchingCondition value : values) {
			set.add(value);
		}
		return new FileWatchingConditionSet(set);
	}
	/**
	 * インスタンスを返す.
	 * @param values 監視条件
	 * @return インスタンス
	 * @throws 同時設定不可能な条件が含まれていた場合
	 */
	public static FileWatchingConditionSet of(Collection<FileWatchingCondition> values) {
		final Set<FileWatchingCondition> set = new HashSet<FileWatchingCondition>(3);
		set.addAll(values);
		return new FileWatchingConditionSet(set);
	}
	
	private final Set<FileWatchingCondition> set;
	
	private FileWatchingConditionSet(final Set<FileWatchingCondition> set) {
		set.add(FileWatchingCondition.CREATE);
		if (set.contains(FileWatchingCondition.SIZE) && set.contains(FileWatchingCondition.MODIFY)) {
			throw new IllegalArgumentException("both 's' and 'm' have been found in arguments.");
		}
		this.set = Collections.unmodifiableSet(set);
	}
	
	/**
	 * 格納されている監視条件の数を返す.
	 * いかなる場合にも{@link FileWatchingCondition#CREATE}は格納されているので、
	 * このメソッドが返す数値が{@code 0}になることはない。
	 * @return 監視条件の数
	 */
	public int size() {
		return set.size();
	}
	/**
	 * 指定された監視条件が含まれているかどうかを判定する.
	 * @param o 監視条件
	 * @return 含まれている場合{@code true}
	 */
	public boolean contains(FileWatchingCondition o) {
		return set.contains(o);
	}
	@Override
	public Iterator<FileWatchingCondition> iterator() {
		return set.iterator();
	}
	/**
	 * 格納されている監視条件の配列を返す.
	 * @return 監視条件の配列
	 */
	public FileWatchingCondition[] toArray() {
		return set.toArray(new FileWatchingCondition[0]);
	}
	/**
	 * 格納されている監視条件と引数で指定された監視条件とを含む新しいインスタンスを生成して返す.
	 * @param e 監視条件
	 * @return 新しいインスタンス
	 */
	public FileWatchingConditionSet add(FileWatchingCondition e) {
		final HashSet<FileWatchingCondition> set = new HashSet<FileWatchingCondition>(3);
		set.addAll(this.set);
		set.add(e);
		return new FileWatchingConditionSet(set);
	}
	/**
	 * 格納されている監視条件から引数で指定された監視条件を除いた新しいインスタンスを生成して返す.
	 * @param e 監視条件
	 * @return 新しいインスタンス
	 */
	public FileWatchingConditionSet remove(FileWatchingCondition o) {
		final HashSet<FileWatchingCondition> set = new HashSet<FileWatchingCondition>(3);
		for (final FileWatchingCondition e : this) {
			if (!o.equals(e)) {
				set.add(e);
			}
		}
		return new FileWatchingConditionSet(set);
	}

	@Override
	public CharSequence toCharSequence() {
		final StringBuilder b = CharSequenceUtils.builder();
		b.append('c');
		if (set.contains(FileWatchingCondition.DELETE)) {
			b.append(':').append('d');
		}
		if (set.contains(FileWatchingCondition.SIZE)) {
			b.append(':').append('s');
		} else if (set.contains(FileWatchingCondition.MODIFY)) {
			b.append(':').append('m');
		}
		return b;
	}

	@Override
	public boolean contentEquals(CharSequence other) {
		return CharSequenceUtils.contentsAreEqual(this.toCharSequence(), other);
	}

	@Override
	public boolean contentEquals(CharSequential other) {
		return contentEquals(other.toCharSequence());
	}
	
	@Override
	public String toString() {
		return toCharSequence().toString();
	}
}
