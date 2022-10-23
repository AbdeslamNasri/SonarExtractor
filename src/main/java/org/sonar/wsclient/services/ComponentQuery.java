package org.sonar.wsclient.services;

public final class ComponentQuery extends Query<Component> {
  public static final String BASE_URL = "/api/measures/component?metricKeys=ncloc,complexity,violations,sqale_index&componentKey=";

  private String componentKey;

/*  public ComponentQuery() {
  }*/
  public ComponentQuery(String componentKey) {
	  this.componentKey = componentKey;
  }

  @Override
  public String getUrl() {
    StringBuilder sb = new StringBuilder(BASE_URL);
    sb.append(this.componentKey);
    return sb.toString();
  }

  @Override
  public Class<Component> getModelClass() {
    return Component.class;
  }
/*
  public static ComponentQuery all() {
    return new ComponentQuery();
  }*/
  
public String getComponentKey() {
	return componentKey;
}
public void setComponentKey(String componentKey) {
	this.componentKey = componentKey;
}

}
