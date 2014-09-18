package de.slackspace.osiris.diagnosis.context;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ThreadLocalContextHolderTest {

	@Before
	public void init() {
		//we need to clean the threadLocal before every test
		ThreadLocalContextHolder.cleanupThread();
	}
	
	@Test
	public void shouldReturnInputTransactionId() {
		String uuid = UUID.randomUUID().toString();
		ThreadLocalContextHolder.putTransactionId(uuid);
		
		Assert.assertEquals(uuid, ThreadLocalContextHolder.getTransactionId());
	}
	
	@Test
	public void shouldReturnNull() {
		Assert.assertNull(ThreadLocalContextHolder.getTransactionId());
	}
}
