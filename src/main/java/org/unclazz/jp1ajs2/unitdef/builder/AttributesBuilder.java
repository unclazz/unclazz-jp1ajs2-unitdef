package org.unclazz.jp1ajs2.unitdef.builder;

import org.unclazz.jp1ajs2.unitdef.Attributes;
import org.unclazz.jp1ajs2.unitdef.PermissionMode;

public final class AttributesBuilder {
	AttributesBuilder() {}
	
	private CharSequence name;
	private CharSequence jp1UserName;
	private PermissionMode permissionMode;
	private CharSequence resourceGroupName;
	
	public AttributesBuilder setName(CharSequence cs) {
		this.name = cs;
		return this;
	}
	public AttributesBuilder setJP1UserName(CharSequence cs) {
		this.jp1UserName = cs;
		return this;
	}
	public AttributesBuilder setPermissionMode(PermissionMode pm) {
		this.permissionMode = pm;
		return this;
	}
	public AttributesBuilder setResourceGroupName(CharSequence cs) {
		this.resourceGroupName = cs;
		return this;
	}
	public Attributes build() {
		if (name == null) {
			throw new NullPointerException("unit name must be not-null.");
		}
		if (name.length() == 0) {
			throw new IllegalArgumentException("Empty unit name");
		}
		return new DefaultAttributes(name, permissionMode, jp1UserName, resourceGroupName);
	}
}
