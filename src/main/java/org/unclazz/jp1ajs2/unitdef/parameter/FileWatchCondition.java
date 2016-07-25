package org.unclazz.jp1ajs2.unitdef.parameter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.unclazz.jp1ajs2.unitdef.Component;
import org.unclazz.jp1ajs2.unitdef.util.CharSequenceUtils;

/**
 * ユニット定義パラメータflwcを表わすオブジェクト.
 * {@link FileWatchConditionFlag}インスタンスを格納する。
 * {@link FileWatchCondition}のインスタンス生成時、
 * 引数に{@link FileWatchConditionFlag#CREATE}が指定されていない場合は自動的に補われる。
 * {@link FileWatchConditionFlag#SIZE}と{@link FileWatchConditionFlag#MODIFY}の同時設定はできない。
 */
public final class FileWatchCondition implements Iterable<FileWatchConditionFlag>, Component {
	/**
	 * インスタンスを返す.
	 * @param values 監視条件
	 * @return インスタンス
	 * @throws IllegalArgumentException 同時設定不可能な条件が含まれていた場合
	 */
	public static FileWatchCondition of(FileWatchConditionFlag... values) {
		final Set<FileWatchConditionFlag> set = new HashSet<FileWatchConditionFlag>(3);
		for (final FileWatchConditionFlag value : values) {
			set.add(value);
		}
		return new FileWatchCondition(set);
	}
	/**
	 * インスタンスを返す.
	 * @param values 監視条件
	 * @return インスタンス
	 * @throws IllegalArgumentException 同時設定不可能な条件が含まれていた場合
	 */
	public static FileWatchCondition of(Collection<FileWatchConditionFlag> values) {
		final Set<FileWatchConditionFlag> set = new HashSet<FileWatchConditionFlag>(3);
		set.addAll(values);
		return new FileWatchCondition(set);
	}
	
	private final Set<FileWatchConditionFlag> set;
	
	private FileWatchCondition(final Set<FileWatchConditionFlag> set) {
		set.add(FileWatchConditionFlag.CREATE);
		if (set.contains(FileWatchConditionFlag.SIZE) && set.contains(FileWatchConditionFlag.MODIFY)) {
			throw new IllegalArgumentException("both 's' and 'm' have been found in arguments.");
		}
		this.set = Collections.unmodifiableSet(set);
	}
	
	/**
	 * 格納されている監視条件の数を返す.
	 * いかなる場合にも{@link FileWatchConditionFlag#CREATE}は格納されているので、
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
	public boolean contains(FileWatchConditionFlag o) {
		return set.contains(o);
	}
	@Override
	public Iterator<FileWatchConditionFlag> iterator() {
		return set.iterator();
	}
	/**
	 * 格納されている監視条件の配列を返す.
	 * @return 監視条件の配列
	 */
	public FileWatchConditionFlag[] toArray() {
		return set.toArray(new FileWatchConditionFlag[0]);
	}
	/**
	 * 格納されている監視条件と引数で指定された監視条件とを含む新しいインスタンスを生成して返す.
	 * @param e 監視条件
	 * @return 新しいインスタンス
	 */
	public FileWatchCondition add(FileWatchConditionFlag e) {
		final HashSet<FileWatchConditionFlag> set = new HashSet<FileWatchConditionFlag>(3);
		set.addAll(this.set);
		set.add(e);
		return new FileWatchCondition(set);
	}
	/**
	 * 格納されている監視条件から引数で指定された監視条件を除いた新しいインスタンスを生成して返す.
	 * @param o 監視条件
	 * @return 新しいインスタンス
	 */
	public FileWatchCondition remove(FileWatchConditionFlag o) {
		final HashSet<FileWatchConditionFlag> set = new HashSet<FileWatchConditionFlag>(3);
		for (final FileWatchConditionFlag e : this) {
			if (!o.equals(e)) {
				set.add(e);
			}
		}
		return new FileWatchCondition(set);
	}

	@Override
	public CharSequence serialize() {
		final StringBuilder b = CharSequenceUtils.builder();
		b.append('c');
		if (set.contains(FileWatchConditionFlag.DELETE)) {
			b.append(':').append('d');
		}
		if (set.contains(FileWatchConditionFlag.SIZE)) {
			b.append(':').append('s');
		} else if (set.contains(FileWatchConditionFlag.MODIFY)) {
			b.append(':').append('m');
		}
		return b;
	}

	@Override
	public boolean contentEquals(CharSequence other) {
		return CharSequenceUtils.contentsAreEqual(this.serialize(), other);
	}

	@Override
	public boolean contentEquals(Component other) {
		return contentEquals(other.serialize());
	}
	
	@Override
	public String toString() {
		return serialize().toString();
	}
}
