package es.uvigo.esei.sing.bdbm.operations;

import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.BDBM;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.SearchEntry;

@Operation(name = "Retrieve Search Entry")
public class RetrieveSearchEntry {
	private Database database;
	private String accession;
	
	@Port(
		name = "Input Database",
		allowNull = false,
		direction = Direction.INPUT,
		order = 1
	)
	public void setDatabase(Database database) {
		this.database = database;
	}
	
	@Port(
		name = "Accession",
		allowNull = false,
		direction = Direction.INPUT,
		order = 2
	)
	public void setAccession(String accession) {
		this.accession = accession;
	}
	
	@Port(direction = Direction.OUTPUT, order = 1000)
	public SearchEntry retrieve()
	throws InterruptedException, ExecutionException, IOException {
		final BDBMController controller = BDBM.getInstance().getController();
		
		return controller.retrieveSearchEntry(this.database, this.accession);
	}
}
