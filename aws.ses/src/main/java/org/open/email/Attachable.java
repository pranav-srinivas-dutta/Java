package org.open.email;

import org.open.email.amazon.AttachmentException;

public interface Attachable {
	public void attachFile(String path) throws AttachmentException;

}
