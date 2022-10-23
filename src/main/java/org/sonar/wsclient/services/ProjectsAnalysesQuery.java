package org.sonar.wsclient.services;

public final class ProjectsAnalysesQuery extends Query<ProjectAnalyses> {
  public static final String BASE_URL = "/api/project_analyses/search";

  private String project;
  private String from; // 2020-12-23
  private String to;
  private String category;
  private Integer page = 1; //p 1-based page number
  private Integer ps = 500; //ps
  

  private ProjectsAnalysesQuery() {
  }

  public ProjectsAnalysesQuery(String project, String from, String to, String category) {
    this.project = project;
    this.from = from;
    this.to = to;
    this.category=category;
  }

  @Override
  public String getUrl() {
    StringBuilder sb = new StringBuilder(BASE_URL);
    sb.append("?project=");
    sb.append(this.project);
    if(null!=this.from) {
    	sb.append("&from=");
    	sb.append(this.from);
    }
    if(null!=this.to) {
    	sb.append("&to=");
    	sb.append(this.to);
    }
    if(null!=this.category) {
    	sb.append("&category=");
    	sb.append(this.category);
    }
    if(null!=this.page) {
    	sb.append("&p=");
    	sb.append(this.page);
    }
    if(null!=this.ps) {
    	sb.append("&ps=");
    	sb.append(this.ps);
    }
    System.out.println("URL:" + sb.toString());
    return sb.toString();
  }

  @Override
  public Class<ProjectAnalyses> getModelClass() {
    return ProjectAnalyses.class;
  }

  public static ProjectsAnalysesQuery all() {
    return new ProjectsAnalysesQuery();
  }

public String getProject() {
	return project;
}

public void setProject(String project) {
	this.project = project;
}

public String getFrom() {
	return from;
}

public void setFrom(String from) {
	this.from = from;
}

public String getTo() {
	return to;
}

public void setTo(String to) {
	this.to = to;
}

public String getCategory() {
	return category;
}

public void setCategory(String category) {
	this.category = category;
}

public int getPage() {
	return page;
}

public void setPage(int page) {
	this.page = page;
}

public int getPs() {
	return ps;
}

public void setPs(int ps) {
	this.ps = ps;
}

}
