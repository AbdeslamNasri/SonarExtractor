package org.sonar.wsclient.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonar.wsclient.JdkUtils;
import org.sonar.wsclient.services.Project;
import org.sonar.wsclient.services.ProjectAnalyses;
import org.sonar.wsclient.services.ProjectAnalysis;
import org.sonar.wsclient.services.Projects;
import org.sonar.wsclient.services.ProjectsAnalysesQuery;
import org.sonar.wsclient.services.ProjectsQuery;
import org.sonar.wsclient.services.WSUtils;
import org.sonar.wsclient.unmarshallers.UnmarshalException;
import org.sonar.wsclient.unmarshallers.Unmarshaller;
import org.sonar.wsclient.unmarshallers.Unmarshallers;

class ReadProjectAnalysesTest {

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
	    	ProjectAnalyses result = null;
	    	ProjectAnalysis pa = null;
    		ProjectsAnalysesQuery query = ProjectsAnalysesQuery.all();
	        try {
	          Unmarshaller<ProjectAnalyses> unmarshaller = Unmarshallers.forModel(query.getModelClass());
	          result = unmarshaller.toModel(json);
	          pa = result.getLastAnalysis();
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
