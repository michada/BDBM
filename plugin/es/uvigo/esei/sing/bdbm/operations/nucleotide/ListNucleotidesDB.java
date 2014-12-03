package es.uvigo.esei.sing.bdbm.operations.nucleotide;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.BDBM;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;

@Operation(name = "List Nucleotides DB")
public class ListNucleotidesDB {
	@Port(direction = Direction.OUTPUT, order = 1000)
	public NucleotideDatabase[] list() {
		return BDBM.getInstance().getController().listNucleotideDatabases();
	}
}
