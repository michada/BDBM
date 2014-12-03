package es.uvigo.esei.sing.bdbm.datatypes;

import java.io.File;
import java.util.Observable;

import es.uvigo.ei.aibench.core.datatypes.annotation.Datatype;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;

@Datatype(
	namingMethod = "getName",
	renameable = false,
	removable = false
)
public abstract class SequenceEntityWrapper<T extends SequenceEntity> extends Observable implements Wrapper<T>, SequenceEntity {
	protected final T wrappedEntity;
	
	@SuppressWarnings("unchecked")
	public static <SE extends SequenceEntity> SequenceEntityWrapper<SE> wrap(SE entity) {
		if (entity instanceof NucleotideDatabase) {
			return (SequenceEntityWrapper<SE>) new NucleotideDatabaseWrapper((NucleotideDatabase) entity);
		} else if (entity instanceof NucleotideFasta) {
			return (SequenceEntityWrapper<SE>) new NucleotideFastaWrapper((NucleotideFasta) entity);
		} else if (entity instanceof NucleotideExport) {
			return (SequenceEntityWrapper<SE>) new NucleotideExportWrapper((NucleotideExport) entity);
		} else if (entity instanceof NucleotideSearchEntry) {
			return (SequenceEntityWrapper<SE>) new NucleotideSearchEntryWrapper((NucleotideSearchEntry) entity);
		} else if (entity instanceof ProteinDatabase) {
			return (SequenceEntityWrapper<SE>) new ProteinDatabaseWrapper((ProteinDatabase) entity);
		} else if (entity instanceof ProteinFasta) {
			return (SequenceEntityWrapper<SE>) new ProteinFastaWrapper((ProteinFasta) entity);
		} else if (entity instanceof ProteinExport) {
			return (SequenceEntityWrapper<SE>) new ProteinExportWrapper((ProteinExport) entity);
		} else if (entity instanceof ProteinSearchEntry) {
			return (SequenceEntityWrapper<SE>) new ProteinSearchEntryWrapper((ProteinSearchEntry) entity);
		} else {
			throw new IllegalArgumentException("Unknown entity");
		}
	}
	
	public static <SE extends SequenceEntity> SE unwrap(SequenceEntityWrapper<SE> wrapper) {
		return wrapper.getWrapped();
	}
	
	public static Database unwrapToBase(NucleotideDatabaseWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static Fasta unwrapToBase(NucleotideFastaWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static SearchEntry unwrapToBase(NucleotideSearchEntryWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static Export unwrapToBase(NucleotideExportWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static Database unwrapToBase(ProteinDatabaseWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static Fasta unwrapToBase(ProteinFastaWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static SearchEntry unwrapToBase(ProteinSearchEntryWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static Export unwrapToBase(ProteinExportWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static NucleotideDatabase unwrap(NucleotideDatabaseWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static NucleotideFasta unwrap(NucleotideFastaWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static NucleotideSearchEntry unwrap(NucleotideSearchEntryWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static NucleotideExport unwrap(NucleotideExportWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static ProteinDatabase unwrap(ProteinDatabaseWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static ProteinFasta unwrap(ProteinFastaWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static ProteinSearchEntry unwrap(ProteinSearchEntryWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public static ProteinExport unwrap(ProteinExportWrapper wrapper) {
		return wrapper.getWrapped();
	}
	
	public SequenceEntityWrapper(T wrappedEntity) {
		this.wrappedEntity = wrappedEntity;
	}

	@Override
	public File getBaseFile() {
		return this.wrappedEntity.getBaseFile();
	}

	@Override
	public SequenceType getType() {
		return this.wrappedEntity.getType();
	}

	@Override
	public String getName() {
		return this.wrappedEntity.getName();
	}
	
	@Override
	public T getWrapped() {
		return this.wrappedEntity;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result
//				+ ((wrappedEntity == null) ? 0 : wrappedEntity.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass()) {
//			if (this.wrappedEntity == null) 
//				return false;
//			else if (this.wrappedEntity.getClass() == obj.getClass()) 
//				return this.wrappedEntity.equals(obj);
//			else
//				return false;
//		} else {
//			SequenceEntityWrapper<?> other = (SequenceEntityWrapper<?>) obj;
//			if (wrappedEntity == null) {
//				return other.wrappedEntity == null;
//			} else {
//				return wrappedEntity.equals(other.wrappedEntity);
//			}
//		}
//	}
}
