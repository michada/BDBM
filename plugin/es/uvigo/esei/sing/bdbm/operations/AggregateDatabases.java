package es.uvigo.esei.sing.bdbm.operations;

import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.BDBM;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.persistence.EntityAlreadyExistsException;
import es.uvigo.esei.sing.bdbm.persistence.entities.AbstractDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;

public class AggregateDatabases {
	private AbstractDatabase[] databases;
	private String outputDBName;
	
	@Port(
		name = "Input Databases",
		allowNull = false,
		direction = Direction.INPUT,
		order = 1
	)
	public void setDatabases(AbstractDatabase[] databases) {
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
	public Database aggregate()
	throws EntityAlreadyExistsException, IOException, InterruptedException, ExecutionException {
		final BDBMController controller = BDBM.getInstance().getController();
		
		return controller.blastdbAliasTool(this.databases, outputDBName);
	}
}
