package es.uvigo.esei.sing.bdbm.environment;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.sing.bdbm.environment.BLASTEnvironment;
import es.uvigo.esei.sing.bdbm.environment.binaries.BLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.DefaultBLASTBinaries;

public class DefaultBLASTBinariesTest {
	private static final String BASE_PATH = "path";
	private static final File BASE_DIRECTORY = new File("dir");
	
	private static final String CUSTOM_MAKEBLASTDB = "MAKEBLASTDB";
	private static final String CUSTOM_BLASTDBALIASTOOL = "BLASTDBALIASTOOL";
	private static final String CUSTOM_BLASTDBCMD = "BLASTDBCMD";
	private static final String CUSTOM_BLASTN = "BLASTN";
	private static final String CUSTOM_BLASTP = "BLASTP";
	private static final String CUSTOM_TBLASTX = "TBLASTX";
	private static final String CUSTOM_TBLASTN = "TBLASTN";
	
	private DefaultBLASTBinaries blastBinaries;
	private DefaultBLASTBinaries blastBinariesBasePath;
	private DefaultBLASTBinaries blastBinariesBaseDir;
	private DefaultBLASTBinaries blastBinariesCustom;

	@Before
	public void setUp() throws Exception {
		this.blastBinaries = new DefaultBLASTBinaries();
		this.blastBinariesBasePath = new DefaultBLASTBinaries(
			DefaultBLASTBinariesTest.BASE_PATH
		);
		this.blastBinariesBaseDir = new DefaultBLASTBinaries(
			DefaultBLASTBinariesTest.BASE_DIRECTORY
		);
		this.blastBinariesCustom = new DefaultBLASTBinaries(
			null,
			DefaultBLASTBinariesTest.CUSTOM_MAKEBLASTDB,
			DefaultBLASTBinariesTest.CUSTOM_BLASTDBALIASTOOL,
			DefaultBLASTBinariesTest.CUSTOM_BLASTDBCMD,
			DefaultBLASTBinariesTest.CUSTOM_BLASTN,
			DefaultBLASTBinariesTest.CUSTOM_BLASTP,
			DefaultBLASTBinariesTest.CUSTOM_TBLASTX,
			DefaultBLASTBinariesTest.CUSTOM_TBLASTN
		);
	}

	@After
	public void tearDown() throws Exception {
		this.blastBinaries = null;
		this.blastBinariesBasePath = null;
		this.blastBinariesBaseDir = null;
		this.blastBinariesCustom = null;
	}
	
	@Test
	public void testDefaultValues() {
		final BLASTEnvironment environment = 
			BLASTEnvironmentFactory.createEnvironment();
		
		DefaultBLASTBinariesTest.testValues(
			this.blastBinaries, 
			environment.getDefaultMakeBlastDB(), 
			environment.getDefaultBlastDBAliasTool(), 
			environment.getDefaultBlastDBCmd(), 
			environment.getDefaultBlastN(), 
			environment.getDefaultTBlastX(),
			environment.getDefaultTBlastN() 
		);
	}
	
	@Test
	public void testBasePathValues() {
		testBasePath(this.blastBinariesBasePath);
	}
	
	@Test
	public void testBaseDirectoryValues() {
		testBaseDirectory(this.blastBinariesBaseDir);
	}
	
	@Test
	public void testCustomValues() {
		DefaultBLASTBinariesTest.testValues(
			this.blastBinariesCustom, 
			CUSTOM_MAKEBLASTDB,
			CUSTOM_BLASTDBALIASTOOL,
			CUSTOM_BLASTDBCMD,
			CUSTOM_BLASTN,
			CUSTOM_TBLASTX,
			CUSTOM_TBLASTN
		);
	}
	
	@Test
	public void testSetters() {
		this.blastBinariesBaseDir.setMakeBlastDB(CUSTOM_MAKEBLASTDB);
		this.blastBinariesBaseDir.setBlastDBAliasTool(CUSTOM_BLASTDBALIASTOOL);
		this.blastBinariesBaseDir.setBlastDBCmd(CUSTOM_BLASTDBCMD);
		this.blastBinariesBaseDir.setBlastN(CUSTOM_BLASTN);
		this.blastBinariesBaseDir.setTBlastX(CUSTOM_TBLASTX);
		this.blastBinariesBaseDir.setTBlastN(CUSTOM_TBLASTN);

		DefaultBLASTBinariesTest.testValues(
			this.blastBinariesBaseDir, 
			CUSTOM_MAKEBLASTDB,
			CUSTOM_BLASTDBALIASTOOL,
			CUSTOM_BLASTDBCMD,
			CUSTOM_BLASTN,
			CUSTOM_TBLASTX,
			CUSTOM_TBLASTN
		);
	}
	
	@Test
	public void testSetDirectoryPath() {
		this.blastBinariesCustom.setBaseDirectory(BASE_PATH);
		
		testBasePath(this.blastBinariesCustom);
	}
	
	@Test
	public void testSetDirectory() {
		this.blastBinariesCustom.setBaseDirectory(BASE_DIRECTORY);
		
		testBaseDirectory(this.blastBinariesCustom);
	}
	
	private static void testBasePath(BLASTBinaries blastBinaries) {
		final BLASTEnvironment environment = 
			BLASTEnvironmentFactory.createEnvironment();
		
		DefaultBLASTBinariesTest.testValues(
			blastBinaries, 
			new File(BASE_PATH, environment.getDefaultMakeBlastDB()).getAbsolutePath(), 
			new File(BASE_PATH, environment.getDefaultBlastDBAliasTool()).getAbsolutePath(), 
			new File(BASE_PATH, environment.getDefaultBlastDBCmd()).getAbsolutePath(), 
			new File(BASE_PATH, environment.getDefaultBlastN()).getAbsolutePath(), 
			new File(BASE_PATH, environment.getDefaultTBlastX()).getAbsolutePath(), 
			new File(BASE_PATH, environment.getDefaultTBlastN()).getAbsolutePath() 
		);
	}
	
	private static void testBaseDirectory(BLASTBinaries blastBinaries) {
		final BLASTEnvironment environment = 
			BLASTEnvironmentFactory.createEnvironment();
		
		DefaultBLASTBinariesTest.testValues(
			blastBinaries, 
			new File(BASE_DIRECTORY, environment.getDefaultMakeBlastDB()).getAbsolutePath(), 
			new File(BASE_DIRECTORY, environment.getDefaultBlastDBAliasTool()).getAbsolutePath(), 
			new File(BASE_DIRECTORY, environment.getDefaultBlastDBCmd()).getAbsolutePath(), 
			new File(BASE_DIRECTORY, environment.getDefaultBlastN()).getAbsolutePath(), 
			new File(BASE_DIRECTORY, environment.getDefaultTBlastX()).getAbsolutePath(), 
			new File(BASE_DIRECTORY, environment.getDefaultTBlastN()).getAbsolutePath() 
		);
	}
	
	private static void testValues(
		BLASTBinaries bBinaries,
		String makeBlastDB,
		String blastDBAliasTool,
		String blastDBCmd,
		String blastN,
		String tBlastX,
		String tBlastN
	) {
		assertEquals(makeBlastDB, bBinaries.getMakeBlastDB());
		assertEquals(blastDBAliasTool, bBinaries.getBlastDBAliasTool());
		assertEquals(blastDBCmd, bBinaries.getBlastDBCmd());
		assertEquals(blastN, bBinaries.getBlastN());
		assertEquals(tBlastN, bBinaries.getTBlastN());
		assertEquals(tBlastX, bBinaries.getTBlastX());
	}
}
