package es.uvigo.esei.sing.bdbm.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.execution.BLASTBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.EMBOSSBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.environment.execution.NCBIBinariesExecutor;
import es.uvigo.esei.sing.bdbm.persistence.BDBMRepositoryManager;
import es.uvigo.esei.sing.bdbm.persistence.EntityAlreadyExistsException;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export.ExportEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry.NucleotideQuery;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry.ProteinQuery;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry.Query;
import es.uvigo.esei.sing.bdbm.persistence.entities.SequenceEntity;

public interface BDBMController {
	public abstract void setRepositoryManager(BDBMRepositoryManager repositoryManager);
	public abstract void setBlastBinariesExecutor(BLASTBinariesExecutor binariesExecutor);
	public abstract void setEmbossBinariesExecutor(EMBOSSBinariesExecutor eBinariesExecutor);
	public abstract void setNcbiBinariesExecutor(NCBIBinariesExecutor eBinariesExecutor);

	public abstract boolean exists(SequenceEntity entity);

	public abstract boolean delete(SequenceEntity entity) throws IOException;
	public abstract boolean delete(Database database) throws IOException;
	public abstract boolean delete(Fasta fasta) throws IOException;
	public abstract boolean delete(SearchEntry search) throws IOException;
	public abstract boolean delete(Query query) throws IOException;
	public abstract boolean delete(Export export) throws IOException;
	public abstract boolean delete(ExportEntry exportEntry) throws IOException;

	public abstract ProteinDatabase[] listProteinDatabases();
	public abstract ProteinFasta[] listProteinFastas();
	public abstract ProteinSearchEntry[] listProteinSearchEntries();
	public abstract ProteinExport[] listProteinExports();
	
	public abstract NucleotideDatabase[] listNucleotideDatabases();
	public abstract NucleotideFasta[] listNucleotideFastas();
	public abstract NucleotideSearchEntry[] listNucleotideSearchEntries();
	public abstract NucleotideExport[] listNucleotideExports();

	public abstract Fasta importFasta(SequenceType sequenceType, File file)
		throws EntityAlreadyExistsException, IOException;

	public abstract Database makeBlastDB(Fasta inputFasta, String outputDBName)
		throws ExecutionException, IllegalArgumentException, EntityAlreadyExistsException, IOException, InterruptedException;

	public abstract Database blastdbAliasTool(Database[] databases, String outputDBName)
		throws EntityAlreadyExistsException, IOException, InterruptedException, ExecutionException;

	public abstract SearchEntry retrieveSearchEntry(Database database, String accession)
		throws InterruptedException, ExecutionException, IOException;

	public abstract NucleotideExport blastn(
		NucleotideDatabase database,
		NucleotideQuery query,
		BigDecimal expectedValue, 
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException;
	
	public abstract NucleotideExport blastn(
		NucleotideDatabase database,
		File queryFile,
		BigDecimal expectedValue, 
		boolean filter, 
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException;

	public abstract ProteinExport blastp(
		ProteinDatabase database,
		ProteinQuery query, 
		BigDecimal expectedValue, 
		boolean keepSingleSequenceFiles,
		boolean filter,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException;
	
	public abstract ProteinExport blastp(
		ProteinDatabase database,
		File queryFile,
		BigDecimal expectedValue, 
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException;

	public abstract NucleotideExport tblastx(
		NucleotideDatabase database,
		NucleotideQuery query,
		BigDecimal expectedValue, 
		boolean filter, 
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException;

	public abstract NucleotideExport tblastx(
		NucleotideDatabase database,
		File queryFile,
		BigDecimal expectedValue, 
		boolean filter, 
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException;

	public abstract NucleotideExport tblastn(
		NucleotideDatabase database,
		ProteinQuery query,
		BigDecimal expectedValue,
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException;

	public abstract NucleotideExport tblastn(
		NucleotideDatabase database,
		File queryFile,
		BigDecimal expectedValue,
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException;

	public abstract NucleotideFasta getORF(
		NucleotideFasta fasta, 
		int minSize, int maxSize, 
		boolean noNewLine,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException;
	
	public abstract NucleotideFasta splignCompart(
		NucleotideFasta genomeFasta,
		NucleotideDatabase genomeDB,
		NucleotideFasta cdsFasta,
		NucleotideDatabase cdsDB,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException;
	
	public abstract void reformatFasta(Fasta fasta) throws IOException;
	public abstract void reformatFasta(Fasta fasta, int fragmentLength) throws IOException;
}