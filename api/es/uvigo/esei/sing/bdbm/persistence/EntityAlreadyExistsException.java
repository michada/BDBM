package es.uvigo.esei.sing.bdbm.persistence;

import java.io.IOException;

public class EntityAlreadyExistsException extends IOException {
	private static final long serialVersionUID = 1L;

	private final Object entity;

	public EntityAlreadyExistsException(Object entity) {
		this.entity = entity;
	}

	public EntityAlreadyExistsException(String message, Object entity) {
		super(message);
		this.entity = entity;
	}

	public EntityAlreadyExistsException(Throwable cause, Object entity) {
		super(cause);
		this.entity = entity;
	}

	public EntityAlreadyExistsException(String message, Throwable cause, Object entity) {
		super(message, cause);
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}
}
