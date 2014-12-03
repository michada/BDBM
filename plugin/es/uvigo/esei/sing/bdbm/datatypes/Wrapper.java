package es.uvigo.esei.sing.bdbm.datatypes;

import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;

public interface Wrapper<T extends SequenceEntity> {
	public abstract T getWrapped();
}
