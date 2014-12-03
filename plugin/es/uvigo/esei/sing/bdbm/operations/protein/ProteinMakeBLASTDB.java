package es.uvigo.esei.sing.bdbm.operations.protein;

import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.operations.MakeBLASTDB;
import es.uvigo.esei.sing.bdbm.persistence.EntityAlreadyExistsException;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.ProteinFasta;

@Operation(name = "Make BLAST DB")
public class ProteinMakeBLASTDB extends MakeBLASTDB {
	@Override
	public void setInputFasta(Fasta fasta) {
		if (fasta.getType() != SequenceType.PROTEIN)
			throw new IllegalArgumentException("protein fasta needed");
		super.setInputFasta(fasta);
	}
	
	@Port(
		name = "Input Fasta",
		allowNull = false,
		direction = Direction.INPUT,
		order = 1
	)
	public void setInputProteinFasta(ProteinFasta fasta) {
		super.setInputFasta(fasta);
	}
	
	@Override
	public ProteinDatabase makeblastdb()
	throws IllegalArgumentException, EntityAlreadyExistsException, ExecutionException, IOException, InterruptedException {
		return (ProteinDatabase) super.makeblastdb();
	}
}
