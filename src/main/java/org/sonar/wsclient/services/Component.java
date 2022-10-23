package org.sonar.wsclient.services;

import java.util.List;
import java.util.Map;

public class Component extends Model {

	  private String id;
	  private String key;
	  private String name;
	  private String qualifier;
	  private Map<String, Measure> measures;



	public Map<String, Measure> getMeasures() {
		return measures;
	}



  @Override
  public String toString() {
    return new StringBuilder()
      .append("[Measures: ")
      .append(measures.size())
      .append("]")
      .toString();
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



public String getName() {
	return name;
}



public String getQualifier() {
	return qualifier;
}



public void setKey(String key) {
	this.key = key;
}



public void setName(String name) {
	this.name = name;
}



public void setQualifier(String qualifier) {
	this.qualifier = qualifier;
}



public void setMeasures(Map<String, Measure> measures) {
	this.measures = measures;
}

}
