package es.uvigo.esei.sing.bdbm.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.io.FileUtils;

import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.execution.BLASTBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.EMBOSSBinariesExecutor;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionResult;
import es.uvigo.esei.sing.bdbm.environment.execution.NCBIBinariesExecutor;
import es.uvigo.esei.sing.bdbm.fasta.DefaultFastaParser;
import es.uvigo.esei.sing.bdbm.fasta.FastaParserAdapter;
import es.uvigo.esei.sing.bdbm.persistence.BDBMRepositoryManager;
import es.uvigo.esei.sing.bdbm.persistence.DatabaseRepositoryManager;
import es.uvigo.esei.sing.bdbm.persistence.EntityAlreadyExistsException;
import es.uvigo.esei.sing.bdbm.persistence.EntityValidationException;
import es.uvigo.esei.sing.bdbm.persistence.ExportRepositoryManager;
import es.uvigo.esei.sing.bdbm.persistence.FastaRepositoryManager;
import es.uvigo.esei.sing.bdbm.persistence.SearchEntryRepositoryManager;
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

public class DefaultBDBMController implements BDBMController {
	private BDBMRepositoryManager repositoryManager;
	private BLASTBinariesExecutor blastBinariesExecutor;
	private EMBOSSBinariesExecutor embossBinariesExecutor;
	private NCBIBinariesExecutor ncbiBinariesExecutor;
	
	public DefaultBDBMController() {
		this(null, null, null, null);
	}
	
	public DefaultBDBMController(
		BDBMRepositoryManager repositoryManager, 
		BLASTBinariesExecutor blastBinariesExecutor,
		EMBOSSBinariesExecutor embossBinariesExecutor,
		NCBIBinariesExecutor ncbiBinariesExecutor
	) {
		this.repositoryManager = repositoryManager;
		this.blastBinariesExecutor = blastBinariesExecutor;
		this.embossBinariesExecutor = embossBinariesExecutor;
		this.ncbiBinariesExecutor = ncbiBinariesExecutor;
	}
	
	@Override
	public void setRepositoryManager(BDBMRepositoryManager repositoryManager) {
		this.repositoryManager = repositoryManager;
	}
	
	@Override
	public void setBlastBinariesExecutor(BLASTBinariesExecutor binariesExecutor) {
		this.blastBinariesExecutor = binariesExecutor;
	}
	
	@Override
	public void setEmbossBinariesExecutor(EMBOSSBinariesExecutor eBinariesExecutor) {
		this.embossBinariesExecutor = eBinariesExecutor;
	}
	
	@Override
	public void setNcbiBinariesExecutor(NCBIBinariesExecutor ncbiBinariesExecutor) {
		this.ncbiBinariesExecutor = ncbiBinariesExecutor;
	}
	
	@Override
	public boolean exists(SequenceEntity entity) {
		if (entity instanceof Database) {
			return this.repositoryManager.database().exists((Database) entity);
		} else if (entity instanceof Fasta) {
			return this.repositoryManager.fasta().exists((Fasta) entity);
		} else if (entity instanceof SearchEntry) {
			return this.repositoryManager.searchEntry().exists((SearchEntry) entity);
		} else if (entity instanceof Export) {
			return this.repositoryManager.export().exists((Export) entity);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean delete(SequenceEntity entity) throws IOException {
		if (entity instanceof Database) {
			return this.delete((Database) entity);
		} else if (entity instanceof Fasta) {
			return this.delete((Fasta) entity);
		} else if (entity instanceof SearchEntry) {
			return this.delete((SearchEntry) entity);
		} else if (entity instanceof Query) {
			return this.delete((Query) entity);
		} else if (entity instanceof Export) {
			return this.delete((Export) entity);
		} else if (entity instanceof ExportEntry) {
			return this.delete((ExportEntry) entity);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean delete(Database database) throws IOException {
		return this.repositoryManager.database().delete(database);
	}
	
	@Override
	public boolean delete(Fasta fasta) throws IOException {
		return this.repositoryManager.fasta().delete(fasta);
	}
	
	@Override
	public boolean delete(SearchEntry search) throws IOException {
		return this.repositoryManager.searchEntry().delete(search);
	}
	
	@Override
	public boolean delete(Query query) throws IOException {
		final SearchEntry searchEntry = query.getSearchEntry();
		
		searchEntry.deleteQuery(query);
		if (searchEntry.listQueries().isEmpty()) {
			return this.delete(searchEntry);
		} else {
			return true;
		}
	}
	
	@Override
	public boolean delete(Export export) throws IOException {
		return this.repositoryManager.export().delete(export);
	}
	
	@Override
	public boolean delete(ExportEntry exportEntry) throws IOException {
		final Export export = exportEntry.getExport();
		
		export.deleteExportEntry(exportEntry);
		if (export.listEntries().isEmpty()) {
			return this.delete(export);
		} else {
			return true;
		}
	}
	
	@Override
	public ProteinDatabase[] listProteinDatabases() {
		return this.repositoryManager.database().listProtein();
	}
	
	@Override
	public NucleotideDatabase[] listNucleotideDatabases() {
		return this.repositoryManager.database().listNucleotide();
	}
	
	@Override
	public ProteinFasta[] listProteinFastas() {
		return this.repositoryManager.fasta().listProtein();
	}
	
	@Override
	public NucleotideFasta[] listNucleotideFastas() {
		return this.repositoryManager.fasta().listNucleotide();
	}
	
	@Override
	public ProteinSearchEntry[] listProteinSearchEntries() {
		return this.repositoryManager.searchEntry().listProtein();
	}
	
	@Override
	public NucleotideSearchEntry[] listNucleotideSearchEntries() {
		return this.repositoryManager.searchEntry().listNucleotide();
	}
	
	@Override
	public ProteinExport[] listProteinExports() {
		return this.repositoryManager.export().listProtein();
	}
	
	@Override
	public NucleotideExport[] listNucleotideExports() {
		return this.repositoryManager.export().listNucleotide();
	}
	
	@Override
	public Fasta importFasta(SequenceType sequenceType, File file) throws EntityAlreadyExistsException, IOException {
		final FastaRepositoryManager fastaManager = this.repositoryManager.fasta();
		final Fasta fasta = fastaManager.create(sequenceType, file.getName());
		
		try {
			FileUtils.copyFile(file, fasta.getFile());
			
			return fasta;
		} finally {
			if (!fastaManager.exists(fasta))
				fastaManager.delete(fasta);
		}
	}
	
	@Override
	public Database makeBlastDB(Fasta inputFasta, String outputDBName)
	throws ExecutionException, IllegalArgumentException, EntityAlreadyExistsException, IOException, InterruptedException {
		final DatabaseRepositoryManager databaseManager = this.repositoryManager.database();
		
		if (databaseManager.exists(inputFasta.getType(), outputDBName)) {
			throw new IllegalArgumentException("Database already exists: " + outputDBName);
		} else {
			final Database database = databaseManager.create(
				inputFasta.getType(), outputDBName
			);
		
			try {
				final ExecutionResult result = this.blastBinariesExecutor.executeMakeBlastDB(inputFasta.getFile(), database);
				
				if (result.getExitStatus() != 0) {
					databaseManager.delete(database);
					throw new ExecutionException(result.getExitStatus(), "Error executing makeBlastDB. Please, check error log.", "");
				}
				
				databaseManager.validate(database);
				
				return database;
			} catch (EntityValidationException e) {
				throw new ExecutionException(0, "Error executing makeBlastDB. Please, check error log", "");
			} finally {
				if (!databaseManager.exists(database)) {
					databaseManager.delete(database);
				}
			}
		}
	}
	
	@Override
	public Database blastdbAliasTool(Database[] databases, String outputDBName)
	throws EntityAlreadyExistsException, IOException, InterruptedException, ExecutionException {
		if (databases.length == 0)
			throw new IllegalArgumentException("databases can't be empty");
		
		// Database array validation and sequence type inference
		SequenceType sequenceType = null;
		for (Database database : databases) {
			if (database == null)
				throw new NullPointerException("databases can't contain null values");
			
			if (sequenceType == null) {
				sequenceType = database.getType();
			} else if (sequenceType != database.getType()) {
				throw new IllegalArgumentException("databases can't contain different sequence types");
			}
		}
		
		final DatabaseRepositoryManager databaseManager = this.repositoryManager.database();
		if (databaseManager.exists(sequenceType, outputDBName)) {
			throw new IllegalArgumentException("Database already exists: " + outputDBName);
		} else {
			final Database database = databaseManager.create(
				sequenceType, outputDBName
			);
			
			try {
				this.blastBinariesExecutor.executeBlastDBAliasTool(database, databases);
				
				return database;
			} finally {
				if (!databaseManager.exists(database)) {
					databaseManager.delete(database);
				}
			}
		}
	}
	
	@Override
	public SearchEntry retrieveSearchEntry(Database database, String accession)
	throws InterruptedException, ExecutionException, IOException {
		final SearchEntryRepositoryManager searchEntryManager = 
			this.repositoryManager.searchEntry();

		boolean dbHasAccession = false;
		for (String dbAccession : database.listAccessions()) {
			if (dbAccession.equalsIgnoreCase(accession)) {
				dbHasAccession = true;
				break;
			}
		}
		
		if (!dbHasAccession)
			throw new IllegalArgumentException(
				"Database " + database.getName() + " does not have accession " + accession
			);
		
		final SearchEntry entry = searchEntryManager
			.get(database.getType(), database.getName());
		
		try {
			this.blastBinariesExecutor.executeBlastDBCMD(database, entry, accession);
			
			return entry;
		} finally {
			if (!searchEntryManager.exists(entry)) {
				searchEntryManager.delete(entry);
			}
		}
	}
	
	@Override
	public NucleotideExport blastn(
		NucleotideDatabase database,
		File queryFile, 
		BigDecimal expectedValue, 
		boolean filter, 
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException {
		final ExportRepositoryManager exportManager = this.repositoryManager.export();
		final NucleotideExport export = exportManager.getNucleotide(
			database.getName()
		);
		
		try {
			this.repositoryManager.fasta().validateEntityPath(SequenceType.NUCLEOTIDE, queryFile);
			
			this.blastBinariesExecutor.executeBlastN(database, queryFile, export, expectedValue, filter, outputName);
			
			generateExportEntry(database, outputName, export, keepSingleSequenceFiles);
			
			return export;
		} catch (EntityValidationException e) {
			throw new IOException("Invalid query file: " + queryFile.getAbsolutePath());
		} finally {
			if (!exportManager.exists(export)) {
				exportManager.delete(export);
			}
		}
	}
	
	@Override
	public NucleotideExport blastn(
		NucleotideDatabase database, 
		NucleotideQuery query, 
		BigDecimal expectedValue,
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException {
		final ExportRepositoryManager exportManager = this.repositoryManager.export();
		final NucleotideExport export = exportManager.getNucleotide(
			database.getName()
		);
		
		try {
			this.blastBinariesExecutor.executeBlastN(database, query, export, expectedValue, filter, outputName);
			
			generateExportEntry(database, outputName, export, keepSingleSequenceFiles);
			
			return export;
		} finally {
			if (!exportManager.exists(export)) {
				exportManager.delete(export);
			}
		}
	}

	@Override
	public ProteinExport blastp(
		ProteinDatabase database,
		File queryFile, 
		BigDecimal expectedValue, 
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException {
		final ExportRepositoryManager exportManager = this.repositoryManager.export();
		final ProteinExport export = exportManager.getProtein(
			database.getName()
		);
		
		try {
			this.repositoryManager.fasta().validateEntityPath(SequenceType.PROTEIN, queryFile);
			this.blastBinariesExecutor.executeBlastP(
				database, queryFile, export, expectedValue, filter, outputName
			);

			generateExportEntry(database, outputName, export, keepSingleSequenceFiles);
			
			return export;
		} catch (EntityValidationException e) {
			throw new IOException("Invalid query file: " + queryFile.getAbsolutePath());
		} finally {
			if (!exportManager.exists(export)) {
				exportManager.delete(export);
			}
		}
	}

	@Override
	public ProteinExport blastp(
		ProteinDatabase database,
		ProteinQuery query, 
		BigDecimal expectedValue, 
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException {
		final ExportRepositoryManager exportManager = this.repositoryManager.export();
		final ProteinExport export = exportManager.getProtein(
			database.getName()
		);
		
		try {
			this.blastBinariesExecutor.executeBlastP(
				database, query, export, expectedValue, filter, outputName
			);

			this.generateExportEntry(database, outputName, export, keepSingleSequenceFiles);
			
			return export;
		} finally {
			if (!exportManager.exists(export)) {
				exportManager.delete(export);
			}
		}
	}
	
	@Override
	public NucleotideExport tblastx(
		NucleotideDatabase database, 
		File queryFile, 
		BigDecimal expectedValue,
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException {
		final ExportRepositoryManager exportManager = this.repositoryManager.export();
		final NucleotideExport export = exportManager.getNucleotide(
			database.getName()
		);
		
		try {
			this.repositoryManager.fasta().validateEntityPath(SequenceType.NUCLEOTIDE, queryFile);
			
			this.blastBinariesExecutor.executeTBlastX(database, queryFile, export, expectedValue, filter, outputName);
				
			generateExportEntry(database, outputName, export, keepSingleSequenceFiles);
				
			return export;
		} catch (EntityValidationException e) {
			throw new IOException("Invalid query file: " + queryFile.getAbsolutePath());
		} finally {
			if (!exportManager.exists(export)) {
				exportManager.delete(export);
			}
		}
	}
	
	@Override
	public NucleotideExport tblastx(
		NucleotideDatabase database, 
		NucleotideQuery query, 
		BigDecimal expectedValue,
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException {
		final ExportRepositoryManager exportManager = this.repositoryManager.export();
		final NucleotideExport export = exportManager.getNucleotide(
			database.getName()
		);
		
		try {
			this.blastBinariesExecutor.executeTBlastX(database, query, export, expectedValue, filter, outputName);
			
			generateExportEntry(database, outputName, export, keepSingleSequenceFiles);
			
			return export;
		} finally {
			if (!exportManager.exists(export)) {
				exportManager.delete(export);
			}
		}
	}
	
	@Override
	public NucleotideExport tblastn(
		NucleotideDatabase database, 
		File queryFile, 
		BigDecimal expectedValue,
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException {
		final ExportRepositoryManager exportManager = this.repositoryManager.export();
		final NucleotideExport export = exportManager.getNucleotide(
			database.getName()
		);
		
		try {
			this.repositoryManager.fasta().validateEntityPath(SequenceType.PROTEIN, queryFile);
			
			this.blastBinariesExecutor.executeTBlastN(database, queryFile, export, expectedValue, filter, outputName);
			
			generateExportEntry(database, outputName, export, keepSingleSequenceFiles);
			
			return export;
		} catch (EntityValidationException e) {
			throw new IOException("Invalid query file: " + queryFile.getAbsolutePath());
		} finally {
			if (!exportManager.exists(export)) {
				exportManager.delete(export);
			}
		}
	}
	
	@Override
	public NucleotideExport tblastn(
		NucleotideDatabase database, 
		ProteinQuery query, 
		BigDecimal expectedValue,
		boolean filter,
		boolean keepSingleSequenceFiles,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException {
		final ExportRepositoryManager exportManager = this.repositoryManager.export();
		final NucleotideExport export = exportManager.getNucleotide(
			database.getName()
		);
		
		try {
			this.blastBinariesExecutor.executeTBlastN(database, query, export, expectedValue, filter, outputName);
			
			generateExportEntry(database, outputName, export, keepSingleSequenceFiles);
			
			return export;
		} finally {
			if (!exportManager.exists(export)) {
				exportManager.delete(export);
			}
		}
	}

	private void generateExportEntry(
		final Database database,
		final String outputName, 
		final Export export,
		final boolean keepSingleSequenceFiles
	) throws InterruptedException, ExecutionException, IOException {
		final ExportEntry exportEntry = export.getExportEntry(outputName);
		
		if (exportEntry == null) {
			throw new IllegalStateException("Missing output file");
		} else {
			final List<String> alignments = 
				this.blastBinariesExecutor.extractSignificantSequences(exportEntry);
			
			for (String alignment : alignments) {
				this.blastBinariesExecutor.executeBlastDBCMD(
					database, exportEntry, alignment
				);
			}
			
			for (File fastas : exportEntry.getSequenceFiles()) {
				FileUtils.write(
					exportEntry.getSummaryFastaFile(), 
					FileUtils.readFileToString(fastas), 
					true
				);
			}
			
			if (!keepSingleSequenceFiles)
				exportEntry.deleteSequenceFiles();
		}
	}
	
	@Override
	public NucleotideFasta getORF(
		NucleotideFasta fasta,
		int minSize,
		int maxSize,
		boolean noNewLines,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException {
		final FastaRepositoryManager fastaManager = this.repositoryManager.fasta();
		final NucleotideFasta orf = fastaManager.getNucleotide(outputName);
		
		if (fastaManager.exists(orf)) {
			throw new IllegalArgumentException("ORF already exists: " + outputName);
		} else {
			try {
				this.embossBinariesExecutor.executeGetORF(fasta, orf, minSize, maxSize);
				if (noNewLines) {
					reformatFasta(orf);
				}
				
				return orf;
			} finally {
				if (!fastaManager.exists(orf))
					fastaManager.delete(orf);
			}
		}
	}

	@Override
	public NucleotideFasta splignCompart(
		NucleotideFasta referenceFasta,
		NucleotideDatabase referenceDB,
		NucleotideFasta targetFasta,
		NucleotideDatabase targetDB,
		String outputName
	) throws IOException, InterruptedException, ExecutionException, IllegalStateException {
		final FastaRepositoryManager fastaManager = this.repositoryManager.fasta();
		final NucleotideFasta fasta = fastaManager.getNucleotide(outputName);
		
		if (fastaManager.exists(fasta)) {
			throw new IllegalArgumentException("Fasta already exists: " + outputName);
		} else {
			final ExecutionResult result = this.ncbiBinariesExecutor.splignCompart(
				referenceFasta, referenceDB, targetFasta, targetDB, fasta
			);
			
			if (result.getExitStatus() != 0) {
				if (fastaManager.exists(fasta))
					fastaManager.delete(fasta);
				
				throw new ExecutionException(result.getExitStatus(), "Error executing splignCompart", "splignCompart");
			} else {
				return fasta;
			}
		}
	}
	
	@Override
	public void reformatFasta(Fasta fasta) throws IOException {
		this.reformatFasta(fasta, 0);
	}
	
	@Override
	public void reformatFasta(Fasta fasta, final int fragmentLength) throws IOException {
		final File tmpFile = File.createTempFile("bdbm", "fasta");
		tmpFile.deleteOnExit();
		
		try (final PrintWriter pw = new PrintWriter(tmpFile)) {
			final DefaultFastaParser parser = new DefaultFastaParser();
			parser.addParseListener(new FastaParserAdapter() {
				String name;
				StringBuilder sequence = new StringBuilder();
				
				@Override
				public void sequenceNameRead(File file, String sequenceName) {
					name = sequenceName;
				}
				
				@Override
				public void sequenceFragmentRead(File file, String fragment) {
					sequence.append(fragment);
				}
				
				@Override
				public void sequenceEnd(File file) {
					pw.println(name);
					
					if (fragmentLength <= 0 || fragmentLength > sequence.length()) {
						pw.println(sequence);
					} else {
						for (int i = 0; i < sequence.length(); i += fragmentLength) {
							final int endIndex = Math.min(sequence.length(), i + fragmentLength);
							pw.println(sequence.substring(i, endIndex));
						}
					}
					
					pw.flush();
					name = null;
					sequence = new StringBuilder();
				}
			});
			
			parser.parse(fasta.getFile());
		}
		
		fasta.getFile().delete();
		FileUtils.moveFile(tmpFile, fasta.getFile());
	}
}
