package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public abstract class AbstractORF extends AbstractSequenceEntity implements ORF {
	public static NucleotideORF newORF(File input) {
		return new DefaultNucleotideORF(input);
	}
	
	protected AbstractORF(SequenceType type, File file) {
		super(type, file);
	}

	@Override
	public File getFile() {
		return this.getBaseFile();
	}
	
	@Override
	public int compareTo(ORF o) {
		return this.getFile().compareTo(o.getFile());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.getFile() == null) ? 0 : this.getFile().hashCode());
		result = prime * result + ((this.getType() == null) ? 0 : this.getType().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ORF))
			return false;
		
		ORF other = (ORF) obj;
		if (this.getFile() == null) {
			if (other.getFile()!= null)
				return false;
		} else if (!this.getFile().equals(other.getFile()))
			return false;
		if (this.getType() != other.getType())
			return false;
		return true;
	}
}
