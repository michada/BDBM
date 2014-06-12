package es.uvigo.esei.sing.bdbm.persistence;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	DefaultDatabaseManagerTest.class,
	DefaultFastaManagerTest.class,
	DefaultExportManagerTest.class,
	DefaultSearchEntryManagerTest.class
})
public class PersistenceTestSuite {

}
