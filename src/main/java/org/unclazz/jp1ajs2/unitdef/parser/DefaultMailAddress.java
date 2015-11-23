package org.unclazz.jp1ajs2.unitdef.parser;

import org.unclazz.jp1ajs2.unitdef.parameter.MailAddress;

class DefaultMailAddress implements MailAddress {
	private final MailAddressType type;
	private final String address;
	
	DefaultMailAddress(final MailAddressType type, final String address) {
		this.address = address;
		this.type = type;
	}

	@Override
	public MailAddressType getType() {
		return type;
	}
	@Override
	public String getAddress() {
		return address;
	}
}
