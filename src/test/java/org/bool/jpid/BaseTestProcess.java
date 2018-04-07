package org.bool.jpid;

import java.io.InputStream;
import java.io.OutputStream;

class BaseTestProcess extends Process {

	@Override
	public OutputStream getOutputStream() {
		return null;
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public InputStream getErrorStream() {
		return null;
	}

	@Override
	public int waitFor() {
		return 0;
	}

	@Override
	public int exitValue() {
		return 0;
	}

	@Override
	public void destroy() {
	}
}