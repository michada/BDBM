package es.uvigo.esei.sing.bdbm.environment.execution;

import es.uvigo.esei.sing.bdbm.environment.binaries.NCBIBinaries;

public interface NCBIBinariesExecutor extends BinariesExecutor<NCBIBinaries> {
	public boolean checkNCBIBinaries(NCBIBinaries bBinaries);
}
