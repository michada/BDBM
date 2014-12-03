package es.uvigo.esei.sing.bdbm.operations.nucleotide;

import java.io.IOException;

import es.uvigo.ei.aibench.core.operation.annotation.Direction;
import es.uvigo.ei.aibench.core.operation.annotation.Operation;
import es.uvigo.ei.aibench.core.operation.annotation.Port;
import es.uvigo.esei.sing.bdbm.environment.SequenceType;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionException;
import es.uvigo.esei.sing.bdbm.operations.MakeBLASTDB;
import es.uvigo.esei.sing.bdbm.persistence.EntityAlreadyExistsException;
import es.uvigo.esei.sing.bdbm.persistence.entities.Fasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideDatabase;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;

@Operation(name = "Make BLAST DB")
public class NucleotideMakeBLASTDB extends MakeBLASTDB {
	@Override
	public void setInputFasta(Fasta fasta) {
		if (fasta.getType() != SequenceType.NUCLEOTIDE)
			throw new IllegalArgumentException("nucleotide fasta needed");
		
		super.setInputFasta(fasta);
	}
	
	@Port(
		name = "Input Fasta",
		allowNull = false,
		direction = Direction.INPUT,
		order = 1
	)
	public void setInputFasta(NucleotideFasta fasta) {
		super.setInputFasta(fasta);
	}

	@Override
	@Port(order = 1000, direction = Direction.OUTPUT)
	public NucleotideDatabase makeblastdb()
	throws IllegalArgumentException, EntityAlreadyExistsException, ExecutionException, IOException, InterruptedException {
		return (NucleotideDatabase) super.makeblastdb();
	}
}
