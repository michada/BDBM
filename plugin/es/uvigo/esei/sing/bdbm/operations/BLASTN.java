package es.uvigo.esei.sing.bdbm.operations;

import java.io.IOException;
import java.math.BigDecimal;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.BDBM;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideExport;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry;

@Operation(name = "blastn")
public class BLASTN {
	private NucleotideDatabase database;
	private NucleotideSearchEntry.NucleotideQuery query;
//	private String outputName;
	private BigDecimal expectedValue;
	private boolean filter;
	private boolean keepSingleFiles;
	private String outputName;
	
	@Port(
		name = "Database",
		direction = Direction.INPUT,
		allowNull = false,
		order = 1
	)
	public void setDatabase(NucleotideDatabase database) {
		this.database = database;
	}

	@Port(
		name = "Query",
		direction = Direction.INPUT,
		allowNull = false,
		order = 2
	)
	public void setQuery(NucleotideSearchEntry.NucleotideQuery query) {
		this.query = query;
	}

//	@Port(
//		name = "Output name",
//		direction = Direction.INPUT,
//		allowNull = false,
//		order = 3
//	)
//	public void setOutputName(String outputName) {
//		this.outputName = outputName;
//	}

	@Port(
		name = "Expected value",
		direction = Direction.INPUT,
		allowNull = false,
		defaultValue = "0.05",
		order = 4
	)
	public void setExpectedValue(BigDecimal expectedValue) {
		this.expectedValue = expectedValue;
	}

	@Port(
		name = "Filter",
		direction = Direction.INPUT,
		allowNull = false,
		defaultValue = "false",
		order = 5
	)
	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	@Port(
		name = "Keep Single Files",
		direction = Direction.INPUT,
		allowNull = false,
		defaultValue = "false",
		order = 6
	)
	public void setKeepSingleFiles(boolean keepSingleFiles) {
		this.keepSingleFiles = keepSingleFiles;
	}
	
	@Port(
		name = "Output name",
		direction = Direction.INPUT,
		allowNull = false,
		defaultValue = "false",
		order = 7
	)
	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}
	
	@Port(
		direction = Direction.OUTPUT,
		order = 1000
	)
	public NucleotideExport blastn() 
	throws IOException, InterruptedException, ExecutionException {
		final BDBMController controller = BDBM.getInstance().getController();
		
		return controller.blastn(this.database, this.query, this.expectedValue, this.filter, this.keepSingleFiles, this.outputName);
	}
}
