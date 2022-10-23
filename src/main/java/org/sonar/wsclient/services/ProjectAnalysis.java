package org.sonar.wsclient.services;

public class ProjectAnalysis {

	  private String key;
	  private String date;
	  private String projectVersion;

public ProjectAnalysis(Object p) {
	 if(null !=p) {
		    WSUtils utils = WSUtils.getINSTANCE();
		    key= utils.getString(p, "key");
		    date= utils.getString(p, "date");
		    projectVersion= utils.getString(p, "projectVersion");
		 }
	
}

  @Override
  public String toString() {
    return new StringBuilder()
      .append(key)
      .append("(")
      .append(date)
      .append(", ")
      .append(projectVersion)
      .append(")")
      .toString();
  }

public String getKey() {
	return key;
}

public void setKey(String key) {
	this.key = key;
}

public String getDate() {
	return date;
}

public void setDate(String date) {
	this.date = date;
}

public String getProjectVersion() {
	return projectVersion;
}

public void setProjectVersion(String projectVersion) {
	this.projectVersion = projectVersion;
}
}
