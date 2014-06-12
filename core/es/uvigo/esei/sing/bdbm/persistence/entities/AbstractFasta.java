package es.uvigo.esei.sing.bdbm.persistence.entities;

import java.io.File;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;

public abstract class AbstractFasta extends AbstractSequenceEntity implements Fasta {
	public static Fasta newFasta(SequenceType sequenceType, File file) {
		switch (sequenceType) {
		case PROTEIN:
			return new DefaultProteinFasta(file);
		case NUCLEOTIDE:
			return new DefaultNucleotideFasta(file);
		default:
			throw new IllegalStateException("Unknown sequence type");
		}
	}
	
	protected AbstractFasta(SequenceType type, File file) {
		super(type, file);
	}

	@Override
	public File getFile() {
		return this.getBaseFile();
	}
	
	@Override
	public int compareTo(Fasta o) {
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
		if (!(obj instanceof Fasta))
			return false;
		
		Fasta other = (Fasta) obj;
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
