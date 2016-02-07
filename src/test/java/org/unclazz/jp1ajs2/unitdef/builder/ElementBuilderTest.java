package org.unclazz.jp1ajs2.unitdef.builder;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.unclazz.jp1ajs2.unitdef.parameter.Element;
import org.unclazz.jp1ajs2.unitdef.parameter.UnitType;

public class ElementBuilderTest {

	@Rule
	public final ExpectedException expected = ExpectedException.none();
	
	@Test
	public void build_whenUnitNameNoSpecified_throwsException() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		b.setUnitType(UnitType.PC_JOB).setXCoord(1).setYCoord(2);
		expected.expect(NullPointerException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenUnitNameIsEmpty_throwsException() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		b.setUnitName("").setUnitType(UnitType.PC_JOB).setXCoord(1).setYCoord(2);
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenUnitTypeNoSpecified_throwsException() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		b.setUnitName("foo").setXCoord(1).setYCoord(2);
		expected.expect(NullPointerException.class);
		
		// Act
		b.build();
		
		// Assert
	}
	
	@Test
	public void build_whenUnitNameAndUnitTypeHaveBeenSet_returnsParameterInstance() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		b.setUnitName("foo").setUnitType(UnitType.GROUP).setXCoord(1).setYCoord(2);
		
		// Act
		final Element r = b.build();
		
		// Assert
		assertThat(r.getUnitName(), equalTo("foo"));
		assertThat(r.getUnitType(), equalTo(UnitType.GROUP));
		assertThat(r.getXCoord(), equalTo(1));
		assertThat(r.getYCoord(), equalTo(2));
		assertThat(r.getHPixel(), equalTo(80 + 160 * 1));
		assertThat(r.getVPixel(), equalTo(48 + 96 * 2));
	}
	
	@Test
	public void setVPixel_when0Specified_throwsException() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.setVPixel(0);
		
		// Assert
	}
	
	@Test
	public void setVPixel_when47Specified_throwsException() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.setVPixel(47);
		
		// Assert
	}
	
	@Test
	public void setVPixel_when48Specified_returnsVoid() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		
		// Act
		b.setVPixel(48);
		
		// Assert
	}
	
	@Test
	public void setVPixel_when9552Specified_returnsVoid() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		
		// Act
		b.setVPixel(9552);
		
		// Assert
	}
	
	@Test
	public void setVPixel_when9553Specified_throwsException() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.setVPixel(9553);
		
		// Assert
	}
	
	@Test
	public void setHPixel_when0Specified_throwsException() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.setHPixel(0);
		
		// Assert
	}
	
	@Test
	public void setHPixel_when79Specified_throwsException() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.setHPixel(79);
		
		// Assert
	}
	
	@Test
	public void setHPixel_when80Specified_returnsVoid() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		
		// Act
		b.setHPixel(80);
		
		// Assert
	}
	
	@Test
	public void setHPixel_when15920Specified_returnsVoid() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		
		// Act
		b.setHPixel(15920);
		
		// Assert
	}
	
	@Test
	public void setHPixel_when15921Specified_throwsException() {
		// Arrange
		final ElementBuilder b = Builders.parameterEL();
		expected.expect(IllegalArgumentException.class);
		
		// Act
		b.setHPixel(15921);
		
		// Assert
	}
}
