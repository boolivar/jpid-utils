package org.bool.jpid;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.util.Map;

public class PidUtils {
	
	public static Long getPid(Process process) throws IllegalAccessException {
		LongValueAccessor pidAccessor = getPidAccessor(process.getClass());
		return pidAccessor.getValue(process);
	}
	
	public static LongValueAccessor cache(Map<Class<? extends Process>, LongValueAccessor> map) {
		return process -> map.computeIfAbsent(process.getClass(), PidUtils::getPidAccessor).getValue(process);
	}
	
	public static LongValueAccessor getPidAccessor(Class<? extends Process> cls) {
		Field field;
		if ((field = findField(cls, "pid")) != null) {
			field.setAccessible(true);
			return new FieldValueAccessor(field);
		}
		if ((field = findField(cls, "handle")) != null) {
			field.setAccessible(true);
			return new ProcessIdAccessor(new FieldValueAccessor(field));
		}
		throw new RuntimeException("Unsupported process class: " + cls);
	}
	
	static Field findField(Class<?> cls, String name) {
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
