package es.uvigo.esei.sing.bdbm;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.uvigo.esei.sing.bdbm.environment.EnvironmentTestSuite;
import es.uvigo.esei.sing.bdbm.environment.execution.ExecutionTestSuite;
import es.uvigo.esei.sing.bdbm.operations.OperationsTestSuite;
import es.uvigo.esei.sing.bdbm.persistence.PersistenceTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ 
	EnvironmentTestSuite.class, 
	ExecutionTestSuite.class, 
	OperationsTestSuite.class,
	PersistenceTestSuite.class
})
public class BDBMTestSuite {}
