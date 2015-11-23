package org.unclazz.jp1ajs2.unitdef;

/**
 * 送信先メールアドレス
 */
public interface MailAddress {
	MailAddressType getType();
	String getAddress();

	/**
	 * 送信先メールアドレス・タイプ
	 */
	public static enum MailAddressType {
		FROM, TO, CC, BCC;
	}
}
