package org.bool.jpid;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;

public class PidUtils {
	
	public static Long getPid(Process process) throws IllegalAccessException {
		Field field = getPidField(process.getClass());
		if (field != null) {
			field.setAccessible(true);
			return ((Number) field.get(process)).longValue();
		}
		return null;
	}
	
	private static Field getPidField(Class<?> cls) {
		Field field = getField(cls, "pid");
		if (field != null) {
			return field;
		}
		return getField(cls, "handle");
	}
	
	static Field getField(Class<?> cls, String name) {
		while (cls != null) {
			try {
				return cls.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				cls = cls.getSuperclass();
			}
		}
		return null;
	}
	
	public static Long getPid() throws RuntimeException {
		RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
		String processName = runtimeMxBean.getName();
		return parsePid(processName);
	}
	
	static Long parsePid(String processName) {
		int atIndex = processName.indexOf('@');
		String pid = processName.substring(0, atIndex);
		return Long.valueOf(pid);
	}
}
