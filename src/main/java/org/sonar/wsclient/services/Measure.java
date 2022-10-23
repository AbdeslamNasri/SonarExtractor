package org.sonar.wsclient.services;

public class Measure {

	  private String metric;
	  private String value;
	  private Boolean bestValue;

public Measure(Object p) {
	 if(null !=p) {
		    WSUtils utils = WSUtils.getINSTANCE();
		    metric= utils.getString(p, "metric");
		    value= (String) utils.getField(p, "value");
		    try {
		    	bestValue= utils.getBoolean(p, "bestValue");  // true/false/null
		    }catch(Exception e) {
		    	 bestValue= null;
		    }
		 }
	
}

   @Override
  public String toString() {
    return new StringBuilder()
      .append(metric)
      .append("(")
      .append(value)
      .append(", ")
      .append(bestValue)
      .append(")")
      .toString();
  }



public String getMetric() {
	return metric;
}



public String getValue() {
	return value;
}



public Boolean getBestValue() {
	return bestValue;
}

}
