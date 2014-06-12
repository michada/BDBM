package es.uvigo.esei.sing.bdbm.environment.execution;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.sing.bdbm.environment.binaries.DefaultBLASTBinaries;

public class DefaultBinariesCheckerTest {
	private static final String BLAST_BIN_PATH = 
		"/opt/BLAST/ncbi-blast-2.2.27+/bin";
	
	private DefaultBLASTBinariesChecker checker;
	private DefaultBLASTBinariesChecker badChecker;

	@Before
	public void setUp() throws Exception {
		this.checker = new DefaultBLASTBinariesChecker();
		this.badChecker = new DefaultBLASTBinariesChecker();
		
		this.checker.setBinaries(
			new DefaultBLASTBinaries(
				new File(DefaultBinariesCheckerTest.BLAST_BIN_PATH)
			)
		);
		
		this.badChecker.setBinaries(
			new DefaultBLASTBinaries(new File("tmp"))
		);
	}

	@After
	public void tearDown() throws Exception {
		this.checker = null;
		this.badChecker = null;
	}

	@Test
	public void testCheckAll() throws BinaryCheckException {
		this.checker.checkAll();
	}

	@Test
	public void testCheckMakeBlastDB() throws BinaryCheckException {
		this.checker.checkMakeBlastDB();
	}

	@Test
	public void testCheckBlastDBAliasTool() throws BinaryCheckException {
		this.checker.checkBlastDBAliasTool();
	}

	@Test
	public void testCheckBlastDBCmd() throws BinaryCheckException {
		this.checker.checkBlastDBCmd();
	}

	@Test
	public void testCheckBlastN() throws BinaryCheckException {
		this.checker.checkBlastN();
	}

	@Test
	public void testCheckTBlastX() throws BinaryCheckException {
		this.checker.checkTBlastX();
	}

	@Test
	public void testCheckTBlastN() throws BinaryCheckException {
		this.checker.checkTBlastN();
	}

	@Test(expected = BinaryCheckException.class)
	public void testCheckAllFail() throws BinaryCheckException {
		this.badChecker.checkAll();
	}

	@Test(expected = BinaryCheckException.class)
	public void testCheckMakeBlastDBFail() throws BinaryCheckException {
		this.badChecker.checkMakeBlastDB();
	}

	@Test(expected = BinaryCheckException.class)
	public void testCheckBlastDBAliasToolFail() throws BinaryCheckException {
		this.badChecker.checkBlastDBAliasTool();
	}

	@Test(expected = BinaryCheckException.class)
	public void testCheckBlastDBCmdFail() throws BinaryCheckException {
		this.badChecker.checkBlastDBCmd();
	}

	@Test(expected = BinaryCheckException.class)
	public void testCheckBlastNFail() throws BinaryCheckException {
		this.badChecker.checkBlastN();
	}

	@Test(expected = BinaryCheckException.class)
	public void testCheckTBlastXFail() throws BinaryCheckException {
		this.badChecker.checkTBlastX();
	}

	@Test(expected = BinaryCheckException.class)
	public void testCheckTBlastNFail() throws BinaryCheckException {
		this.badChecker.checkTBlastN();
	}
}
