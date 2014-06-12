package es.uvigo.esei.sing.bdbm.persistence;

public interface BDBMRepositoryManager extends Repository {
	public abstract DatabaseRepositoryManager database();

	public abstract FastaRepositoryManager fasta();

	public abstract ExportRepositoryManager export();

	public abstract SearchEntryRepositoryManager searchEntry();
	
	public abstract ORFRepositoryManager orf();
}
