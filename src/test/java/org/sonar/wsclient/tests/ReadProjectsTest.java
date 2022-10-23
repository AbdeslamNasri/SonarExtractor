package org.sonar.wsclient.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonar.wsclient.JdkUtils;
import org.sonar.wsclient.services.Project;
import org.sonar.wsclient.services.Projects;
import org.sonar.wsclient.services.ProjectsQuery;
import org.sonar.wsclient.services.WSUtils;
import org.sonar.wsclient.unmarshallers.UnmarshalException;
import org.sonar.wsclient.unmarshallers.Unmarshaller;
import org.sonar.wsclient.unmarshallers.Unmarshallers;

class ReadProjectsTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void test() {
	    	Projects result = null;
    		ProjectsQuery query = ProjectsQuery.all();
	        try {
	          Unmarshaller<Projects> unmarshaller = Unmarshallers.forModel(query.getModelClass());
	          result = unmarshaller.toModel(json);
	        } catch (Exception e) {
	          throw new UnmarshalException(query, json, e);
	        }
	        assertNotNull(result);
	}
	// TODO : Set your JSON string here before testing
	private String json ="";
	  static {
		    WSUtils.setInstance(new JdkUtils());
		  }
}
