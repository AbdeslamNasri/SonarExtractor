package org.sonar.wsclient.services;

public final class ProjectsQuery extends Query<Projects> {
  public static final String BASE_URL = "/api/components/search?qualifiers=TRK,BRC&ps=";

  private int pageSize = 500;

  private ProjectsQuery() {
  }

  private ProjectsQuery(int pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  public String getUrl() {
    StringBuilder sb = new StringBuilder(BASE_URL);
    sb.append(this.pageSize);
    return sb.toString();
  }

  @Override
  public Class<Projects> getModelClass() {
    return Projects.class;
  }

  public static ProjectsQuery all() {
    return new ProjectsQuery();
  }

}
