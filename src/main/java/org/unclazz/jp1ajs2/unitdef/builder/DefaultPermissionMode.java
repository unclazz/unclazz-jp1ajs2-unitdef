package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.PermissionMode;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;

class DefaultPermissionMode implements PermissionMode {
	private static final Pattern syntax = Pattern.compile("[0-7]{4}");
	
	public static final PermissionMode NOT_SPECIFIED = new PermissionMode() {
		@Override
		public int intValue() {
			return -1;
		}
		@Override
		public String getValue() {
			return "";
		}
		@Override
		public ExecutionUserType getExecutionUserType() {
			return null;
		}
		@Override
		public String toString() {
			return "";
		}
		@Override
		public boolean isSpecified() {
			return false;
		}
	};
	
	private final String hex4;
	
	DefaultPermissionMode(String hex4) {
		if (! syntax.matcher(hex4).matches()) {
			throw new IllegalArgumentException("permission mode value must be 4-digit hex.");
		}
		this.hex4 = hex4;
	}

	@Override
	public String getValue() {
		return hex4;
	}
	
	@Override
	public boolean isSpecified() {
		return true;
	}

	@Override
	public int intValue() {
		return Integer.parseInt(hex4, 4);
	}

	@Override
	public ExecutionUserType getExecutionUserType() {
		final char hex1 = hex4.charAt(0);
		return hex1 < '4' ? ExecutionUserType.ENTRY_USER : ExecutionUserType.DEFINITION_USER;
	}
	
	@Override
	public String toString() {
		return hex4;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hex4 == null) ? 0 : hex4.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DefaultPermissionMode other = (DefaultPermissionMode) obj;
		if (!hex4.equals(other.hex4))
			return false;
		return true;
	}
}
