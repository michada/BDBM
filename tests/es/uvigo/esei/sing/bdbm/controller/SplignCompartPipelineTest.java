package es.uvigo.esei.sing.bdbm.controller;

import static org.apache.commons.io.FileUtils.contentEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uvigo.esei.sing.bdbm.environment.binaries.DefaultBLASTBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.DefaultBedToolsBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.DefaultCompartBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.DefaultEMBOSSBinaries;
import es.uvigo.esei.sing.bdbm.environment.binaries.DefaultSplignBinaries;
import es.uvigo.esei.sing.bdbm.environment.execution.BLASTBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.BedToolsBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.CompartBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.DefaultBLASTBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.DefaultBedToolsBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.DefaultCompartBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.DefaultEMBOSSBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.DefaultSplignBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.EMBOSSBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionResult;
import es.uvigo.esei.sing.bdbm.environment.execution.SplignBinaryToolsFactory;
import es.uvigo.esei.sing.bdbm.persistence.entities.DefaultNucleotideFasta;
import es.uvigo.esei.sing.bdbm.persistence.entities.NucleotideFasta;
import es.uvigo.esei.sing.bdbm.util.DirectoryUtils;

public class SplignCompartPipelineTest {
	private final static File FILES_PATH = new File("test_files/es/uvigo/ei/sing/bdbm/controller/");
	
	private final File inputReverseGenesFile = new File(FILES_PATH, "dmel.reversed.short");
	private final File inputReverseGenomeFile = new File(FILES_PATH, "dsim.reversed.short");
	
	private final File inputGenesBLASTDatabaseDirectory = new File(FILES_PATH, "dmel_db");
	private final File inputGenomeBLASTDatabaseDirectory = new File(FILES_PATH, "dsim_db");
	
	private final File inputShortDatabasesDirectory = new File(FILES_PATH, "short_db");
	
	private final File inputGenesFile = new File(inputGenesBLASTDatabaseDirectory, "dmel.short");
	private final File inputGenomeFile = new File(inputGenomeBLASTDatabaseDirectory, "dsim.short");
	
	private final NucleotideFasta inputGenesFasta = new DefaultNucleotideFasta(this.inputGenesFile);
	private final NucleotideFasta inputGenomeFasta = new DefaultNucleotideFasta(this.inputGenomeFile);
	
	private final File resultCdnaCompartments = new File(FILES_PATH, "1-cdna.compartments");
	private final File resultSplign = new File(FILES_PATH, "2-result.splign");
	private final File resultSplign5 = new File(FILES_PATH, "7-result.splign5");
	private final File resultBedtools = new File(FILES_PATH, "8-result.bedtools");
	private final File resultBedtoolsMerged = new File(FILES_PATH, "16-result.bedtools4");
	private final File resultBedtoolsNoConcatenate = new File(FILES_PATH, "16-result.bedtools4-no-concatenate");
	
	private SplignCompartPipeline pipeline;
	
	private Path outputFile;
	
	private Path reversedFastaFile;
	private Path reversedRenamedFastaFile;
	
	private Path genesDatabaseDirectory;
	private Path genomeDatabaseDirectory;
	
	private Path genesDatabaseDirectoryCopy;
	private Path genomeDatabaseDirectoryCopy;
	
	private Path shortDatabasesDirectoryCopy;
	private Path genesShortDatabasesDirectoryCopy;
	private Path genomeShortDatabasesDirectoryCopy;
	
	private Path splignCompartOutput;
	
	@Before
	public void setUp() throws Exception {
		// Tools path should be configured before launching the tests
		
		this.pipeline = new SplignCompartPipeline();
		
		final EMBOSSBinaryToolsFactory eFactory = 
			new DefaultEMBOSSBinaryToolsFactory();
		eFactory.setBinaries(new DefaultEMBOSSBinaries());
		
		final BLASTBinaryToolsFactory bFactory =
			new DefaultBLASTBinaryToolsFactory();
		bFactory.setBinaries(new DefaultBLASTBinaries());
		
		final SplignBinaryToolsFactory sFactory =
			new DefaultSplignBinaryToolsFactory();
		sFactory.setBinaries(new DefaultSplignBinaries("ncbi"));
		
		final CompartBinaryToolsFactory cFactory =
			new DefaultCompartBinaryToolsFactory();
		cFactory.setBinaries(new DefaultCompartBinaries("ncbi"));
		
		final BedToolsBinaryToolsFactory btFactory =
			new DefaultBedToolsBinaryToolsFactory();
		btFactory.setBinaries(new DefaultBedToolsBinaries());
		
		this.pipeline.setEMBOSSBinaries(eFactory.createExecutor());
		this.pipeline.setBLASTBinariesExecutor(bFactory.createExecutor());
		this.pipeline.setSplignBinaries(sFactory.createExecutor());
		this.pipeline.setCompartBinaries(cFactory.createExecutor());
		this.pipeline.setBedToolsBinaries(btFactory.createExecutor());
		
		this.reversedFastaFile = Files.createTempFile("bdbm", "reversed");
		this.reversedRenamedFastaFile = Files.createTempFile("bdbm", "reversed_renamed");
		
		this.splignCompartOutput = Files.createTempFile("bdbm", "splign_compart");
		
		this.outputFile = Files.createTempFile("bdbm", "output");
		
		this.genesDatabaseDirectory = Files.createTempDirectory("bdbm_genes_db");
		this.genomeDatabaseDirectory = Files.createTempDirectory("bdbm_genome_db");
		
		this.genesDatabaseDirectoryCopy = Files.createTempDirectory("bdbm_genes_db_cp");
		this.genomeDatabaseDirectoryCopy = Files.createTempDirectory("bdbm_genes_db_cp");
		this.shortDatabasesDirectoryCopy = Files.createTempDirectory("short_db");
		
		FileUtils.copyDirectory(
			this.inputGenesBLASTDatabaseDirectory,
			this.genesDatabaseDirectoryCopy.toFile()
		);
		FileUtils.copyDirectory(
			this.inputGenomeBLASTDatabaseDirectory,
			this.genomeDatabaseDirectoryCopy.toFile()
		);
		FileUtils.copyDirectory(
			this.inputShortDatabasesDirectory, 
			this.shortDatabasesDirectoryCopy.toFile()
		);
		
		this.genesShortDatabasesDirectoryCopy = this.shortDatabasesDirectoryCopy.resolve("dmel.short");
		this.genomeShortDatabasesDirectoryCopy = this.shortDatabasesDirectoryCopy.resolve("genome_both");
	}

	@After
	public void tearDown() throws Exception {
		Files.deleteIfExists(this.outputFile);
		Files.deleteIfExists(this.reversedFastaFile);
		Files.deleteIfExists(this.reversedRenamedFastaFile);
		Files.deleteIfExists(this.splignCompartOutput);
		
		DirectoryUtils.deleteIfExists(this.genesDatabaseDirectory);
		DirectoryUtils.deleteIfExists(this.genomeDatabaseDirectory);
		DirectoryUtils.deleteIfExists(this.genesDatabaseDirectoryCopy);
		DirectoryUtils.deleteIfExists(this.genomeDatabaseDirectoryCopy);
		DirectoryUtils.deleteIfExists(this.shortDatabasesDirectoryCopy);
	}

	@Test
	public void testReverseAndMergeGenome() throws Exception {
		final ExecutionResult result = this.pipeline.reverseAndMerge(
			this.inputGenomeFasta,
			this.reversedFastaFile.toFile(),
			this.reversedRenamedFastaFile.toFile(),
			this.outputFile.toFile()
		);

		assertNull(result);
		
		assertTrue(contentEquals(this.inputReverseGenomeFile, this.outputFile.toFile()));
	}

	@Test
	public void testReverseAndMerge() throws Exception {
		final ExecutionResult result = this.pipeline.reverseAndMerge(
			this.inputGenesFasta,
			this.reversedFastaFile.toFile(),
			this.reversedRenamedFastaFile.toFile(),
			this.outputFile.toFile()
		);
		
		assertNull(result);
		
		assertTrue(contentEquals(this.inputReverseGenesFile, this.outputFile.toFile()));
	}
	
	@Test
	public void testMakeBlastDBGenes() throws Exception {
		final ExecutionResult result = this.pipeline.makeBlastDB(
			this.inputGenesFile, this.genesDatabaseDirectory.toFile());

		assertNull(result);
		
		assertEqualBLASTDatabases(inputGenesBLASTDatabaseDirectory, this.genesDatabaseDirectory.toFile());
	}
	
	@Test
	public void testMakeBlastDBGenome() throws Exception {
		final ExecutionResult result = this.pipeline.makeBlastDB(
			this.inputGenomeFile, this.genomeDatabaseDirectory.toFile());

		assertNull(result);
		
		assertEqualBLASTDatabases(inputGenomeBLASTDatabaseDirectory, this.genomeDatabaseDirectory.toFile());
	}
	
	@Test
	public void testMkldsGenes() throws Exception {
		final ExecutionResult result = this.pipeline.mklds(
			this.genesDatabaseDirectoryCopy.toAbsolutePath().toString());
		
		assertNull(result);
		
		assertLDS(
			this.genesDatabaseDirectoryCopy.resolve("_SplignLDS_").toFile()
		);
	}
	
	@Test
	public void testMkldsGenome() throws Exception {
		final ExecutionResult result = this.pipeline.mklds(
			this.genomeDatabaseDirectoryCopy.toAbsolutePath().toString());

		assertNull(result);
		
		assertLDS(
			this.genomeDatabaseDirectoryCopy.resolve("_SplignLDS_").toFile()
		);
	}
	
	@Test
	public void testLdsdirToBed() throws Exception {
		this.pipeline.ldsdirToBed(this.resultSplign, this.outputFile.toFile());
		
		assertTrue(contentEquals(this.resultSplign5, this.outputFile.toFile()));
	}
	
	@Test
	public void testBedtools() throws Exception {
		final ExecutionResult result = this.pipeline.bedtools(
			this.genomeShortDatabasesDirectoryCopy.toAbsolutePath().toString(),
			this.resultSplign5.getAbsolutePath(),
			this.outputFile.toAbsolutePath().toString()
		);

		assertNull(result);
		
		assertTrue(contentEquals(this.resultBedtools, this.outputFile.toFile()));
	}
	
	@Test
	public void testMergeSequences() throws Exception {
		this.pipeline.mergeSequences(this.resultBedtools, this.outputFile.toFile(), true);
		
		assertTrue(contentEquals(this.resultBedtoolsMerged, this.outputFile.toFile()));
	}
	
	@Test
	public void testMergeSequencesNoConcatenate() throws Exception {
		this.pipeline.mergeSequences(this.resultBedtools, this.outputFile.toFile(), false);
		
		assertTrue(contentEquals(this.resultBedtoolsNoConcatenate, this.outputFile.toFile()));
	}
	
	@Test
	public void testCompart() throws Exception {
		final ExecutionResult result = this.pipeline.compart(
			this.genesShortDatabasesDirectoryCopy.toAbsolutePath().toString(),
			this.genomeShortDatabasesDirectoryCopy.toAbsolutePath().toString(),
			this.outputFile.toFile()
		);

		assertNull(result);
		
		assertTrue(contentEquals(
			this.resultCdnaCompartments,
			this.outputFile.toFile()
		));
	}
	
	@Test
	public void testLdsdir() throws Exception {
		this.pipeline.ldsdir(
			this.shortDatabasesDirectoryCopy.toAbsolutePath().toString(),
			this.resultCdnaCompartments.getAbsolutePath(),
			this.outputFile.toFile()
		);
		
		assertTrue(contentEquals(
			this.resultSplign,
			this.outputFile.toFile()
		));
	}
	
	private static void assertLDS(File db1) throws IOException {
		final String[] filenames = new String[] {
			"file_filename.idx",
			"lds_annot2obj.db",
			"lds_annotation.db",
			"lds_file.db",
			"lds_object.db",
			"lds_objecttype.db",
			"lds_seq_id_list.db",
			"obj_seqid_int.idx",
			"obj_seqid_txt.idx"
		};
		
		for (String filename : filenames) {
			assertTrue("File " + filename + " does not exists", new File(db1, filename).exists());
		}
	}
	
	private static void assertEqualBLASTDatabases(File db1, File db2) throws IOException {
		// nin is not compared, as varies between executions.
		final String[] extensions = { "nhr", "nog", "nsd", "nsi", "nsq" };
		
		for (String extension : extensions) {
			final File db1File = getFileWithExtension(db1, extension);
			final File db2File = getFileWithExtension(db2, extension);
			
			if (!db1File.exists())
				fail(String.format("No .%s file in %s database", extension, db1));
			if (!db2File.exists())
				fail(String.format("No .%s file in %s database", extension, db2));
			
			if (!contentEquals(db1File, db2File)) {
				fail(String.format("Files %s and %s are different", db1File, db2File));
			}
		}
	}
	
	private static File getFileWithExtension(File directory, final String extension) {
		final FileFilter filter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isFile() && pathname.getName().endsWith(extension);
			}
		};
		
		final File[] files = directory.listFiles(filter);
		if (files.length == 0) {
			fail(String.format("No file .%s found on %s directory", extension, directory));
		} else if (files.length > 1) {
			fail(String.format("More than one file .%s found on %s directory", extension, directory));
		}
		
		return files[0];
	}
}
