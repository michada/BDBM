package es.uvigo.esei.sing.bdbm.operations.protein;

import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.operations.RetrieveSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinSearchEntry;

public class ProteinRetrieveSearchEntry extends RetrieveSearchEntry {
	@Override
	public void setDatabase(Database database) {
		if (database.getType() != SequenceType.PROTEIN)
			throw new IllegalArgumentException("protein database needed");
		
		super.setDatabase(database);
	}
	
	@Port(
		name = "Input Database",
		allowNull = false,
		direction = Direction.INPUT,
		order = 1
	)
	public void setProteinDatabase(ProteinDatabase database) {
		super.setDatabase(database);
	}
	
	@Port(direction = Direction.OUTPUT, order = 1000)
	public ProteinSearchEntry retrieve()
	throws InterruptedException, ExecutionException, IOException {
		return (ProteinSearchEntry) super.retrieve();
	}
}
