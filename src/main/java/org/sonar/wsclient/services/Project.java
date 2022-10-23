package org.sonar.wsclient.services;

public class Project {

	  private String organization;
	  private String id;
	  private String key;
	  private String name;
	  private String qualifier;
	  private String project;
	  private ProjectAnalysis lastAnalysis;

public Project(Object p) {
	 if(null !=p) {
		    WSUtils utils = WSUtils.getINSTANCE();
		    organization= utils.getString(p, "organization");
		    id= utils.getString(p, "id");
		    key= utils.getString(p, "project"); //key
		    name= utils.getString(p, "name");
		    qualifier= utils.getString(p, "qualifier");
		    project = utils.getString(p, "project");
		 }
	
}

  public String getOrganization() {
		return organization;
	}



	public void setOrganization(String organization) {
		this.organization = organization;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getKey() {
		return key;
	}



	public void setKey(String key) {
		this.key = key;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getQualifier() {
		return qualifier;
	}



	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}



	public String getProject() {
		return project;
	}



	public void setProject(String project) {
		this.project = project;
	}

  @Override
  public String toString() {
    return new StringBuilder()
      .append(name)
      .append("(")
      .append(key)
      .append(")")
      .toString();
  }

public ProjectAnalysis getLastAnalysis() {
	return lastAnalysis;
}

public void setLastAnalysis(ProjectAnalysis lastAnalysis) {
	this.lastAnalysis = lastAnalysis;
}
}
