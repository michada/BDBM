package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTType;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export;
import es.uvigo.esei.sing.bdbm.persistence.entities.Export.ExportEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry.ProteinQuery;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;

public interface BLASTBinariesExecutor extends BinariesExecutor<BLASTBinaries> {
	public boolean checkBLASTBinaries(BLASTBinaries bBinaries);
	
	public ExecutionResult executeMakeBlastDB(File inputFasta, Database database)
	throws InterruptedException, ExecutionException, IOException;
	
	public ExecutionResult executeBlastDBAliasTool(Database database, Database[] databases)
	throws InterruptedException, ExecutionException, IOException;
	
	public ExecutionResult executeBlastDBCMD(Database database, SearchEntry searchEntry, String entry)
	throws InterruptedException, ExecutionException, IOException;
	
	public ExecutionResult executeBlastDBCMD(Database database, ExportEntry exportEntry, String entry)
	throws InterruptedException, ExecutionException, IOException;

	public ExecutionResult executeBlast(
		BLASTType blastType, 
		Database database,
		File queryFile, 
		Export export, 
		BigDecimal expectedValue,
		boolean filter, 
		String outputName
	) throws InterruptedException, ExecutionException, IOException;
	
	public ExecutionResult executeBlast(
		BLASTType blastType, 
		Database database, 
		SearchEntry.Query query,
		Export export,
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException;


	public ExecutionResult executeBlastN(
		NucleotideDatabase database, 
		File queryFile,
		NucleotideExport export,
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException;
	
	public ExecutionResult executeBlastN(
		NucleotideDatabase database, 
		NucleotideSearchEntry.NucleotideQuery query,
		NucleotideExport export,
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException;
	
	public ExecutionResult executeBlastP(
		ProteinDatabase database,
		File queryFile,
		ProteinExport export,
		BigDecimal expectedValue,
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException;

	public ExecutionResult executeBlastP(
		ProteinDatabase database,
		ProteinQuery query,
		ProteinExport export,
		BigDecimal expectedValue,
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException;
	
	public ExecutionResult executeTBlastX(
		NucleotideDatabase database, 
		File queryFile,
		NucleotideExport export,
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException;
	
	public ExecutionResult executeTBlastX(
		NucleotideDatabase database, 
		NucleotideSearchEntry.NucleotideQuery query,
		NucleotideExport export,
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException;
	
	public ExecutionResult executeTBlastN(
		NucleotideDatabase database, 
		File queryFile,
		NucleotideExport export,
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException;
	
	public ExecutionResult executeTBlastN(
		NucleotideDatabase database, 
		ProteinSearchEntry.ProteinQuery query,
		NucleotideExport export,
		BigDecimal expectedValue, 
		boolean filter,
		String outputName
	) throws InterruptedException, ExecutionException, IOException;
	
	public List<String> extractSignificantSequences(Export.ExportEntry entry)
	throws InterruptedException, ExecutionException, IOException;

}
