package org.sonar.wsclient.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectAnalyses  extends Model {
	
	  //pageIndex":1,"pageSize":100,"total":2
	  private int pageIndex; //ps
	  private int pageSize; //ps
	  private int total; //ps
	  private List<ProjectAnalysis> analyses = new ArrayList<>();;

public ProjectAnalyses(Object p) {
	 if(null !=p) {
		    WSUtils utils = WSUtils.getINSTANCE();
		    pageIndex= utils.getInteger(p, "pageIndex");
		    pageSize= utils.getInteger(p, "pageSize");
		    total= utils.getInteger(p, "total");
		 }
	
}

public ProjectAnalysis getLastAnalysis() {
	if(null!= this.analyses && this.analyses.size()>0) {
	List<ProjectAnalysis> sortedList = this.analyses.stream().sorted(new Comparator<ProjectAnalysis>() {

		@Override
		public int compare(ProjectAnalysis o1, ProjectAnalysis o2) {
			return o2.getDate().compareTo(o1.getDate());
		}
	}).collect(Collectors.toList());
	return sortedList.get(0);
	}
	return null;
}

@Override
  public String toString() {
    return new StringBuilder()
      .append("ProjectAnalyses")
      .append("[")
      .append(this.analyses.size())
      .append("]")
      .toString();
  }



public List<ProjectAnalysis> getAnalyses() {
	return analyses;
}



public void setAnalyses(List<ProjectAnalysis> analyses) {
	this.analyses = analyses;
}
}
