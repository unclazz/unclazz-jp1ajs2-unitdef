package org.unclazz.jp1ajs2.unitdef.builder;

import java.util.regex.Pattern;

import org.unclazz.jp1ajs2.unitdef.PermissionMode;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;

class DefaultPermissionMode implements PermissionMode {
	private static final Pattern syntax = Pattern.compile("[0-7]{4}");
	
	public static final PermissionMode NONE_SPECIFIED = new PermissionMode() {
		@Override
		public int intValue() {
			throw new UnsupportedOperationException("permissin mode has not been sipecified.");
		}
		@Override
		public String getValue() {
			return "";
		}
		@Override
		public ExecutionUserType getExecutionUserType() {
			throw new UnsupportedOperationException("permissin mode has not been sipecified.");
		}
		@Override
		public String toString() {
			return "";
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
	public int intValue() {
		return Integer.parseInt(hex4, 4);
	}

	@Override
	public ExecutionUserType getExecutionUserType() {
		final char hex1 = hex4.charAt(3);
		return hex1 < '4' ? ExecutionUserType.ENTRY_USER : ExecutionUserType.DEFINITION_USER;
	}
	
	@Override
	public String toString() {
		return hex4;
	}
}
