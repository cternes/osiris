package de.slackspace.osiris.diagnosis;

import java.lang.reflect.Method;

import javax.interceptor.InvocationContext;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class InvocationContextParserTest {

	private InvocationContextParser cut = new InvocationContextParser();

	@Test
	public void whenGetFullMethodNameIsCalledFromClazzThenReturnThatClassWithMethodNameAsString() throws NoSuchMethodException, SecurityException {
		InvocationContext mockContext = Mockito.mock(InvocationContext.class);
		Method testMethod = this.getClass().getMethod("whenGetFullMethodNameIsCalledFromClazzThenReturnThatClassWithMethodNameAsString");
		Mockito.when(mockContext.getMethod()).thenReturn(testMethod);
		
		Assert.assertEquals("de.slackspace.osiris.diagnosis.InvocationContextParserTest:whenGetFullMethodNameIsCalledFromClazzThenReturnThatClassWithMethodNameAsString", cut.getFullMethodName(mockContext));
	}
	
	@Test
	public void whenGetParametersIsCalledFromMethodWithoutParametersThenReturnAnEmptyString() throws NoSuchMethodException, SecurityException {
		InvocationContext mockContext = Mockito.mock(InvocationContext.class);
		Method testMethod = this.getClass().getMethod("whenGetParametersIsCalledFromMethodWithoutParametersThenReturnAnEmptyString");
		Mockito.when(mockContext.getMethod()).thenReturn(testMethod);
		
		Assert.assertEquals("", cut.getParameters(mockContext));
	}
	
	@Test
	public void whenGetParametersIsCalledFromMethodWithSingleParameterThenReturnParameterAsString() throws NoSuchMethodException, SecurityException {
		InvocationContext mockContext = Mockito.mock(InvocationContext.class);
		Mockito.when(mockContext.getParameters()).thenReturn(new Object[] {"name"});
		
		Assert.assertEquals("name", cut.getParameters(mockContext));
	}
	
	@Test
	public void whenGetParametersIsCalledFromMethodWithMultipleParametersThenReturnParametersAsCommaSeperatedString() throws NoSuchMethodException, SecurityException {
		InvocationContext mockContext = Mockito.mock(InvocationContext.class);
		Mockito.when(mockContext.getParameters()).thenReturn(new Object[] {"name", 1000, true});
		
		Assert.assertEquals("name, 1000, true", cut.getParameters(mockContext));
	}

}
