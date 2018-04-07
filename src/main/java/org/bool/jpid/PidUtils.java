package org.bool.jpid;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

public class PidUtils {
	
	private static volatile Map.Entry<Class<?>, LongValueAccessor> cache = null;
	
	public static Long getPid(Process process) throws IllegalAccessException {
		LongValueAccessor pidAccessor = getPidAccessor(process.getClass());
		if (pidAccessor != null) {
			return pidAccessor.getValue(process);
		}
		return null;
	}
	
	static LongValueAccessor getPidAccessor(Class<?> cls) {
		Entry<Class<?>, LongValueAccessor> e = cache;
		if (e != null && e.getKey() == cls) {
			return e.getValue();
		}
		
		LongValueAccessor accessor = createPidAccessor(cls);
		cache = new AbstractMap.SimpleImmutableEntry<>(cls, accessor);
		return accessor;
	}
	
	private static LongValueAccessor createPidAccessor(Class<?> cls) {
		LongValueAccessor pidAccessor = createValueAccessor(cls, "pid", FieldValueAccessor::new);
		if (pidAccessor != null) {
			return pidAccessor;
		}
		return createValueAccessor(cls, "handle", f -> new ProcessIdAccessor(new FieldValueAccessor(f)));
	}
	
	private static LongValueAccessor createValueAccessor(Class<?> cls, String name, Function<Field, LongValueAccessor> accessorFactory) {
		Field field = findField(cls, name);
		if (field != null) {
			field.setAccessible(true);
			return accessorFactory.apply(field);
		}
		return null;
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
