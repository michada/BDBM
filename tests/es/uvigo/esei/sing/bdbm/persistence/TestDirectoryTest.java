package es.uvigo.esei.sing.bdbm.persistence;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import es.uvigo.esei.sing.bdbm.TestDirectory;

public class TestDirectoryTest {
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		TestDirectory.create();
	}

	@AfterClass
	public static void tearDownAfterClass() throws IOException {
		TestDirectory.delete();
	}
}