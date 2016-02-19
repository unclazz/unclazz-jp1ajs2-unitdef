package org.unclazz.jp1ajs2.unitdef.builder;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.PermissionMode;
import org.unclazz.jp1ajs2.unitdef.parameter.ExecutionUserType;

public class DefaultPermissionModeTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	@Test
	public void constructor_whenArgIsNull_throwsException() {
		// Arrange
		expected.expect(NullPointerException.class);
		
		// Act
		new DefaultPermissionMode(null);
		
		// Assert
	}
	
	@Test
	public void constructor_whenArgIsHex3_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		new DefaultPermissionMode("777");
		
		// Assert
	}
	
	@Test
	public void constructor_whenArgIsHex4_returnsInstance() {
		// Arrange
		
		// Act
		final PermissionMode r = new DefaultPermissionMode("7777");
		
		// Assert
		assertThat(r.intValue(), equalTo(7 * pow(8, 3) + 7 * pow(8, 2) + 7 * pow(8, 1) + 7 * pow(8, 0)));
	}
	
	private int pow(int a, int b) {
		return (int) Math.pow(a, b);
	}
	
	@Test
	public void constructor_whenArgIsHex5_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		new DefaultPermissionMode("77777");
		
		// Assert
	}
	
	@Test
	public void constructor_whenArgIsDecimal4_throwsException() {
		// Arrange
		expected.expect(IllegalArgumentException.class);
		
		// Act
		new DefaultPermissionMode("8888");
		
		// Assert
	}
	
	@Test
	public void getValue_whenInstanceHasBeenCreateBy0000_returns0000() {
		// Arrange
		
		// Act
		final PermissionMode r = new DefaultPermissionMode("0000");
		
		// Assert
		assertThat(r.getValue(), equalTo("0000"));
	}
	
	@Test
	public void getValue_whenInstanceHasBeenCreateBy7777_returns7777() {
		// Arrange
		
		// Act
		final PermissionMode r = new DefaultPermissionMode("7777");
		
		// Assert
		assertThat(r.getValue(), equalTo("7777"));
	}
	
	@Test
	public void getValue_whenInstanceRepresentsThatParameterIsNotSpecified_returnsEmptyString() {
		// Arrange
		
		// Act
		final PermissionMode r = DefaultPermissionMode.NOT_SPECIFIED;
		
		// Assert
		assertThat(r.getValue(), equalTo(""));
	}
	
	@Test
	public void intValue_whenInstanceRepresentsThatParameterIsNotSpecified_returnsMinus1() {
		// Arrange
		
		// Act
		final PermissionMode r = DefaultPermissionMode.NOT_SPECIFIED;
		
		// Assert
		assertThat(r.intValue(), equalTo(-1));
	}
	
	@Test
	public void isSpecified_whenInstanceRepresentsThatParameterIsNotSpecified_returnsEmptyString() {
		// Arrange
		
		// Act
		final PermissionMode r = DefaultPermissionMode.NOT_SPECIFIED;
		
		// Assert
		assertThat(r.isSpecified(), equalTo(false));
	}
	
	@Test
	public void getExecutionUserType_whenInstanceRepresentsThatParameterIsNotSpecified_returnsEmptyString() {
		// Arrange
		
		// Act
		final PermissionMode r = DefaultPermissionMode.NOT_SPECIFIED;
		
		// Assert
		assertThat(r.getExecutionUserType(), nullValue());
	}
	
	@Test
	public void getExecutionUserType_whenInstanceHasBeenCreateBy0000_returnsENTRY_USER() {
		// Arrange
		
		// Act
		final PermissionMode r = new DefaultPermissionMode("0000");
		
		// Assert
		assertThat(r.getExecutionUserType(), equalTo(ExecutionUserType.ENTRY_USER));
	}
	
	@Test
	public void getExecutionUserType_whenInstanceHasBeenCreateBy3000_returnsENTRY_USER() {
		// Arrange
		
		// Act
		final PermissionMode r = new DefaultPermissionMode("3000");
		
		// Assert
		assertThat(r.getExecutionUserType(), equalTo(ExecutionUserType.ENTRY_USER));
	}
	
	@Test
	public void getExecutionUserType_whenInstanceHasBeenCreateBy4000_returnsDEFINITION_USER() {
		// Arrange
		
		// Act
		final PermissionMode r = new DefaultPermissionMode("4000");
		
		// Assert
		assertThat(r.getExecutionUserType(), equalTo(ExecutionUserType.DEFINITION_USER));
	}
	
	@Test
	public void getExecutionUserType_whenInstanceHasBeenCreateBy7000_returnsDEFINITION_USER() {
		// Arrange
		
		// Act
		final PermissionMode r = new DefaultPermissionMode("7000");
		
		// Assert
		assertThat(r.getExecutionUserType(), equalTo(ExecutionUserType.DEFINITION_USER));
	}
}
