package es.uvigo.esei.sing.bdbm.operations.nucleotide;

import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.operations.RetrieveSearchEntry;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideSearchEntry;

public class NucleotideRetrieveSearchEntry extends RetrieveSearchEntry {
	@Override
	public void setDatabase(Database database) {
		if (database.getType() != SequenceType.NUCLEOTIDE)
			throw new IllegalArgumentException("nucleotide database needed");
		
		super.setDatabase(database);
	}
	
	@Port(
		name = "Input Database",
		allowNull = false,
		direction = Direction.INPUT,
		order = 1
	)
	public void setNucleotideDatabase(NucleotideDatabase database) {
		super.setDatabase(database);
	}
	
	@Port(direction = Direction.OUTPUT, order = 1000)
	public NucleotideSearchEntry retrieve()
	throws InterruptedException, ExecutionException, IOException {
		return (NucleotideSearchEntry) super.retrieve();
	}
}
