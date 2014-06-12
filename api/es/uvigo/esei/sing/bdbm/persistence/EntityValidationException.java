package es.uvigo.esei.sing.bdbm.persistence;

public class EntityValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private final Object entity;

	public EntityValidationException(Object entity) {
		this.entity = entity;
	}

	public EntityValidationException(String message, Object entity) {
		super(message);
		this.entity = entity;
	}

	public EntityValidationException(Throwable cause, Object entity) {
		super(cause);
		this.entity = entity;
	}

	public EntityValidationException(String message, Throwable cause, Object entity) {
		super(message, cause);
		this.entity = entity;
	}

	public Object getEntity() {
		return entity;
	}
}
