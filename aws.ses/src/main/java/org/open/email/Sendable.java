package org.open.email;

import org.open.email.amazon.SendException;

public interface Sendable {
	public String send(String... recipients) throws SendException;

}
