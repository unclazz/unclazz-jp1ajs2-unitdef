package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.Attributes;

final class DefaultAttributes implements Attributes {
	private final String unitName;
	private final String permissionMode;
	private final String jp1UserName;
	private final String resourceGroupName;
	
	DefaultAttributes(final CharSequence unitName, final CharSequence permissionMode,
			final CharSequence jp1UserName, final CharSequence resourceGroupName) {
		if (unitName == null || unitName.length() == 0) {
			throw new NullPointerException();
		}
		this.unitName = unitName.toString();
		this.permissionMode = returnEmptyIfNull(permissionMode);
		this.jp1UserName = returnEmptyIfNull(jp1UserName);
		this.resourceGroupName = returnEmptyIfNull(resourceGroupName);
	}
	
	private String returnEmptyIfNull(CharSequence cs) {
		return cs == null ? "" : cs.toString();
	}
	
	public String getUnitName() {
		return unitName;
	}
	public String getPermissionMode() {
		return permissionMode;
	}
	public String getJP1UserName() {
		return jp1UserName;
	}
	public String getResourceGroupName() {
		return resourceGroupName;
	}
}
