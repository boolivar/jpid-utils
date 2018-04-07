package org.bool.jpid;

public interface LongValueAccessor {
	Long getValue(Process process) throws IllegalAccessException;
}
