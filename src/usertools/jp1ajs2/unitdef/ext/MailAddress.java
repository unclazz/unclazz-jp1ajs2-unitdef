package usertools.jp1ajs2.unitdef.ext;

public interface MailAddress {
	AddressType getType();
	String getAddress();
	
	public static enum AddressType {
		TO, CC, BCC;
	}
}
