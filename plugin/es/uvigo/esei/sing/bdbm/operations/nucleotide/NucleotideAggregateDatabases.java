package es.uvigo.esei.sing.bdbm.operations.nucleotide;

import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.BDBM;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.persistence.EntityAlreadyExistsException;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;

@Operation(name = "Aggregate Databases")
public class NucleotideAggregateDatabases {
	private NucleotideDatabase[] databases;
	private String outputDBName;
	
	@Port(
		name = "Input Databases",
		allowNull = false,
		direction = Direction.INPUT,
		order = 1
	)
	public void setDatabases(NucleotideDatabase[] databases) {
		this.databases = databases;
	}

	@Port(
		name = "Database Name",
		allowNull = false,
		direction = Direction.INPUT,
		order = 2
	)
	public void setOutputDBName(String outputDBName) {
		this.outputDBName = outputDBName;
	}
	
	@Port(order = 1000, direction = Direction.OUTPUT)
	public NucleotideDatabase aggregate()
	throws EntityAlreadyExistsException, IOException, InterruptedException, ExecutionException {
		final BDBMController controller = BDBM.getInstance().getController();
		
		return (NucleotideDatabase) controller.blastdbAliasTool(this.databases, outputDBName);
	}
}
