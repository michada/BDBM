package es.uvigo.esei.sing.bdbm.operations.protein;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.BDBM;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;

@Operation(name = "List Proteins DB")
public class ListProteinsDB {
	@Port(direction = Direction.OUTPUT, order = 1000)
	public ProteinDatabase[] list() {
		return BDBM.getInstance().getController().listProteinDatabases();
	}
}
