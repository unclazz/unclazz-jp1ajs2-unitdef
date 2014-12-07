package com.m12i.jp1ajs2.unitdef.core;

class Attrs {
	private final String name;
	private final String permissionMode;
	private final String ownerName;
	private final String resourceGroup;
	Attrs(final String name, final String permMode, final String ownName, final String resrcGroup) {
		this.name = name;
		this.permissionMode = permMode;
		this.ownerName = ownName;
		this.resourceGroup = resrcGroup;
	}
	public String getName() {
		return name;
	}
	public String getPermissionMode() {
		return permissionMode;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public String getResourceGroup() {
		return resourceGroup;
	}
}
