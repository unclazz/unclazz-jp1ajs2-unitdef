package org.doogwood.jp1ajs2.unitdef;

/**
 * 送信先メールアドレス
 */
public interface MailAddress {
	AddressType getType();
	String getAddress();

	/**
	 * 送信先メールアドレス・タイプ
	 */
	public static enum AddressType {
		TO, CC, BCC;
	}
}
