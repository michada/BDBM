package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;
import java.util.List;

public interface Database extends SequenceEntity, Comparable<Database> {
	public abstract File getDirectory();
	public abstract boolean isAggregated();
	public abstract boolean isRegular();
	public abstract List<String> listAccessions();
}