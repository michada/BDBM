package es.uvigo.esei.sing.bdbm.environment.binaries;

public enum BLASTType {
	BLASTN("-dust"), BLASTP("-seg"), TBLASTX("-seg"), TBLASTN("-seg");

	private final String filterParam;

	private BLASTType(String filterParam) {
		this.filterParam = filterParam;
	}

	public String getFilterParam() {
		return filterParam;
	}

	String getBinary(BLASTBinaries bBinaries) {
		switch (this) {
		case BLASTN:
			return bBinaries.getBlastN();
		case BLASTP:
			return bBinaries.getBlastP();
		case TBLASTX:
			return bBinaries.getTBlastX();
		case TBLASTN:
			return bBinaries.getTBlastN();
		default:
			return null;
		}
	}
}
