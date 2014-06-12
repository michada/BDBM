package es.uvigo.esei.sing.bdbm.persistence;

import java.io.File;
import java.io.IOException;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;

abstract class EntityBuilder<T extends SequenceEntity> {
	public abstract T create(SequenceType sequenceType, File file);
	
	public static SearchEntry createUnwatchedSearchEntry(SequenceType sequenceType, File file) 
	throws IOException {
		return AbstractSearchEntry.newSearchEntry(sequenceType, file, false);
	}
	
	public final static EntityBuilder<Database> db() {
		return new EntityBuilder<Database>() {
			@Override
			public Database create(SequenceType sequenceType, File file) {
				return AbstractDatabase.newDatabase(sequenceType, file);
			}
		};
	}
	
	public final static EntityBuilder<SearchEntry> searchEntry() {
		return new EntityBuilder<SearchEntry>() {
			@Override
			public SearchEntry create(SequenceType sequenceType, File file) {
				try {
					return AbstractSearchEntry.newSearchEntry(sequenceType, file);
				} catch (IOException e) {
					throw new RuntimeException("Error creating search entry", e);
				}
			}
		};
	}
	
	public final static EntityBuilder<Export> export() {
		return new EntityBuilder<Export>() {
			@Override
			public Export create(SequenceType sequenceType, File file) {
				return AbstractExport.newExport(sequenceType, file);
			}
		};
	}
	
	public final static EntityBuilder<Fasta> fasta() {
		return new EntityBuilder<Fasta>() {
			@Override
			public Fasta create(SequenceType sequenceType, File file) {
				return AbstractFasta.newFasta(sequenceType, file);
			}
		};
	}
}