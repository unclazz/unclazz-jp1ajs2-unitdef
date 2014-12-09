package com.m12i.jp1ajs2.unitdef.ext;


public enum ConnectorOrderingSyncOption {
	SYNC, ASYNC;
	
	public static final ValueResolver<ConnectorOrderingSyncOption> VALUE_RESOLVER = new ValueResolver<ConnectorOrderingSyncOption>() {
		@Override
		public ConnectorOrderingSyncOption resolve(String rawValue) {
			return rawValue.equals("y") ? SYNC : ASYNC;
		}
	};
}
