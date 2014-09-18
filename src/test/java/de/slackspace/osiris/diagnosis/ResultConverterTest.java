package de.slackspace.osiris.diagnosis;

import javax.json.Json;
import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.Test;

public class ResultConverterTest {

	private ResultConverter cut = new ResultConverter();
	
	@Test
	public void nullShouldReturnEmptyString() {
		Assert.assertEquals("", cut.convertToString(null));
	}
	
	@Test
	public void shouldReturnInputString() {
		Assert.assertEquals("name", cut.convertToString("name"));
	}
	
	@Test
	public void shouldReturnInputNumber() {
		Assert.assertEquals("1000", cut.convertToString(1000));
	}
	
	@Test
	public void shouldReturnResponseMetaAsString() {
		Response response = Response.ok().build();
		
		Assert.assertEquals("meta[status=200, reason=OK]", cut.convertToString(response));
	}
	
	@Test
	public void shouldReturnResponseMetaAndEntityAsString() {
		String json = Json.createObjectBuilder()
			.add("id", "1")
			.add("name", "test")
			.build().toString();
		
		Response response = Response.ok().entity(json).build();
		
		Assert.assertEquals("meta[status=200, reason=OK] entity[{\"id\":\"1\",\"name\":\"test\"}]", cut.convertToString(response));
	}
}
