package es.uvigo.esei.sing.bdbm.persistence;

import java.io.IOException;

import es.uvigo.esei.sing.bdbm.environment.paths.RepositoryPaths;

public class DefaultBDBMRepositoryManager implements BDBMRepositoryManager {
	private final DatabaseRepositoryManager databaseManager;
	private final ExportRepositoryManager exportManager;
	private final FastaRepositoryManager fastaManager;
	private final SearchEntryRepositoryManager searchEntryManager;
	
	private final RepositoryManager<?>[] repositories;
	
	public DefaultBDBMRepositoryManager() {
		this.repositories = new RepositoryManager[] {
			this.databaseManager = new DefaultDatabaseRepositoryManager(),
			this.exportManager = new DefaultExportRepositoryManager(),
			this.fastaManager = new DefaultFastaRespositoryManager(),
			this.searchEntryManager = new DefaultSearchEntryRepositoryManager(),
		};
	}
	
	public DefaultBDBMRepositoryManager(RepositoryPaths repositoryPaths) throws IOException {
		this();
		
		this.setRepositoryPaths(repositoryPaths);
	}

	@Override
	public void setRepositoryPaths(RepositoryPaths repositoryPaths) throws IOException {
		for (RepositoryManager<?> repository : this.repositories) {
			repository.setRepositoryPaths(repositoryPaths);
		}
	}
	
	@Override
	public FastaRepositoryManager fasta() {
		return this.fastaManager;
	}

	@Override
	public DatabaseRepositoryManager database() {
		return this.databaseManager;
	}
	
	@Override
	public ExportRepositoryManager export() {
		return this.exportManager;
	}
	
	@Override
	public SearchEntryRepositoryManager searchEntry() {
		return this.searchEntryManager;
	}
	
	@Override
	public void shutdown() {
		for (RepositoryManager<?> repository : this.repositories) {
			repository.shutdown();
		}
	}
}
