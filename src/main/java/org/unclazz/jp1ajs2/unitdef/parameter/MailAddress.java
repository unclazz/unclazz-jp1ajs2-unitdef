package org.unclazz.jp1ajs2.unitdef.parameter;

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
		
		public static MailAddressType valueOfCode(final String code) {
			final String uppercasedCode = code.toUpperCase();
			for (final MailAddressType type : values()) {
				if (type.name().equals(uppercasedCode)) {
					return type;
				}
			}
			throw new IllegalArgumentException(String.format("unknown code \"%s\"", code));
		}
	}
}
