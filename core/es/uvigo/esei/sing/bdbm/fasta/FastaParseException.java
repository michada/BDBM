package es.uvigo.esei.sing.bdbm.fasta;

public class FastaParseException extends Exception {
	private static final long serialVersionUID = 1L;

	public FastaParseException() {
	}

	public FastaParseException(String message) {
		super(message);
	}

	public FastaParseException(Throwable cause) {
		super(cause);
	}

	public FastaParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public FastaParseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
