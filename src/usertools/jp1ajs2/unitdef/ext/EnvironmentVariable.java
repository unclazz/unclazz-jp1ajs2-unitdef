package usertools.jp1ajs2.unitdef.ext;

public class EnvironmentVariable {
	private final String name;
	private final String value;
	public EnvironmentVariable(final String name, final String value) {
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public String getValue() {
		return value;
	}
}
