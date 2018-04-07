package org.bool.jpid;

import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;

public class TestPidUtils {

	@Test
	public void testPid() {
		Assert.assertTrue(PidUtils.getPid() > 0);
	}
	
	@Test
	public void testParseProcessName() {
		Assert.assertEquals(Long.valueOf(0), PidUtils.parsePid("0@google.com"));
		Assert.assertEquals(Long.valueOf(Long.MAX_VALUE), PidUtils.parsePid("9223372036854775807@GOOGLE.COM"));
	}

	@Test(expected=NullPointerException.class)
	public void testParseNullValue() {
		PidUtils.parsePid(null);
	}
	
	@Test(expected=NumberFormatException.class)
	public void testParseEmptyPid() {
		PidUtils.parsePid("@none");
	}
	
	@Test(expected=Exception.class)
	public void testParseEmptyValue() {
		PidUtils.parsePid("");
	}
	
	@Test
	public void testDeclaredFieldLookup() {
		Assert.assertNotNull(PidUtils.findField(StringWriter.class, "buf"));
	}
	
	@Test
	public void testParentFieldLookup() {
		Assert.assertNotNull(PidUtils.findField(StringWriter.class, "writeBuffer"));
	}
	
	@Test
	public void testProcessPid() throws IllegalAccessException {
		Assert.assertEquals(Long.valueOf(42), PidUtils.getPid(new TestPidProcess()));
		Assert.assertNull(PidUtils.getPid(new BaseTestProcess()));
	}
	
	@Test
	public void testCache() throws Exception {
		LongValueAccessor accessor1 = PidUtils.getPidAccessor(TestPidProcess.class);
		LongValueAccessor accessor2 = PidUtils.getPidAccessor(TestPidProcess.class);
		Assert.assertSame(accessor1, accessor2);
		
		LongValueAccessor accessor3 = PidUtils.getPidAccessor(BaseTestProcess.class);
		Assert.assertNotEquals(accessor1, accessor3);
		
		LongValueAccessor accessor4 = PidUtils.getPidAccessor(TestPidProcess.class);
		Assert.assertNotSame(accessor3, accessor4);
		Assert.assertNotSame(accessor1, accessor4);
		
		Assert.assertEquals(accessor1.getValue(new TestPidProcess()), accessor4.getValue(new TestPidProcess()));
	}
	
	private static class TestPidProcess extends BaseTestProcess {
		private Integer pid = 42;
	}
}
