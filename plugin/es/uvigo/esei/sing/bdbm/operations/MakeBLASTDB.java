package es.uvigo.esei.sing.bdbm.operations;

import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.BDBM;
import es.uvigo.esei.sing.bdbm.controller.BDBMController;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.persistence.EntityAlreadyExistsException;
import es.uvigo.esei.sing.bdbm.persistence.entities.Database;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;

@Operation(name = "Make BLAST DB")
public class MakeBLASTDB {
	private Fasta fasta;
	private String outputDBName;
//	private SequenceType sequenceType;
	
	@Port(
		name = "Input Fasta",
		allowNull = false,
		direction = Direction.INPUT,
		order = 1
	)
	public void setInputFasta(Fasta fasta) {
		this.fasta = fasta;
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
//
//	@Port(
//		name = "Sequence Type",
//		allowNull = false,
//		direction = Direction.INPUT,
//		defaultValue = "NUCLEOTIDE",
//		order = 3
//	)
//	public void setSequenceType(SequenceType sequenceType) {
//		this.sequenceType = sequenceType;
//	}
	
	@Port(order = 1000, direction = Direction.OUTPUT)
	public Database makeblastdb()
	throws IllegalArgumentException, EntityAlreadyExistsException, ExecutionException, IOException, InterruptedException {
		final BDBMController controller = BDBM.getInstance().getController();
		
		return controller.makeBlastDB(this.fasta, outputDBName);
	}
}
