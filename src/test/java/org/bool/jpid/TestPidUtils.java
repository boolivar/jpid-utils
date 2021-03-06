package org.bool.jpid;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

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
	
	@Test(expected=RuntimeException.class)
	public void testUnsupportedProcessClass() throws IllegalAccessException {
		PidUtils.getPid(new BaseTestProcess());
	}
	
	@Test
	public void testCache() throws IllegalAccessException {
		Map<Class<? extends Process>, LongValueAccessor> map = new HashMap<>();
		LongValueAccessor cache = PidUtils.cache(map);
		
		Assert.assertEquals(Long.valueOf(42), cache.getValue(new TestPidProcess()));
		Assert.assertEquals(Long.valueOf(42), cache.getValue(new TestPidProcess()));
		
		Assert.assertEquals(1, map.size());
		Assert.assertNotNull(map.get(TestPidProcess.class));
	}
	
	@Test
	public void testProcessPid() throws IllegalAccessException {
		Assert.assertEquals(Long.valueOf(42), PidUtils.getPid(new TestPidProcess()));
	}
	
	private static class TestPidProcess extends BaseTestProcess {
		private Integer pid = 42;
	}
}
