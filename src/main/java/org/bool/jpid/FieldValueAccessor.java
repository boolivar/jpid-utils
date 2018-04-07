package org.bool.jpid;

import java.lang.reflect.Field;

public class FieldValueAccessor implements LongValueAccessor {
	
	private final Field field;

	public FieldValueAccessor(Field field) {
		this.field = field;
	}

	@Override
	public Long getValue(Process process) throws IllegalAccessException {
		return ((Number) field.get(process)).longValue();
	}
}
