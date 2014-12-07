package usertools.jp1ajs2.unitdef.ext;

import usertools.jp1ajs2.unitdef.util.ValueResolver;

public enum ConnectorOrderingSyncOption {
	SYNC, ASYNC;
	
	public static final ValueResolver<ConnectorOrderingSyncOption> VALUE_RESOLVER = new ValueResolver<ConnectorOrderingSyncOption>() {
		@Override
		public ConnectorOrderingSyncOption resolve(String rawValue) {
			return rawValue.equals("y") ? SYNC : ASYNC;
		}
	};
}
