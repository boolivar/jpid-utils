package org.bool.jpid;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT.HANDLE;

public class ProcessIdAccessor implements LongValueAccessor {

	private final LongValueAccessor processHandleAccessor;

	public ProcessIdAccessor(LongValueAccessor processHandleAccessor) {
		this.processHandleAccessor = processHandleAccessor;
	}

	@Override
	public Long getValue(Process process) throws IllegalAccessException {
		Long handleValue = processHandleAccessor.getValue(process);
		Pointer pointer = Pointer.createConstant(handleValue);
		HANDLE handle = new HANDLE(pointer);
		return (long) Kernel32.INSTANCE.GetProcessId(handle);
	}
}
