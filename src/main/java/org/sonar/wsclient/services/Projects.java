package org.sonar.wsclient.services;

import java.util.List;

public class Projects extends Model {

	  private List<Project> projects;
	  private Paging paging;


  public Paging getPaging() {
		return paging;
	}



	public List<Project> getProjects() {
		return projects;
	}



  @Override
  public String toString() {
    return new StringBuilder()
      .append("[Projects: ")
      .append(projects.size())
      .append(", Page: ")
      .append(paging.getPageIndex())
      .append("]")
      .toString();
  }



public void setProjects(List<Project> projects) {
	this.projects = projects;
}



public void setPaging(Paging paging) {
	this.paging = paging;
}
}
