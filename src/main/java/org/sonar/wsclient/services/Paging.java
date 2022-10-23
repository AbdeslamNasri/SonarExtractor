package org.sonar.wsclient.services;

public class Paging {

	  private int pageIndex;
	  private int pageSize;
	  private int total;

 public Paging(Object p) {
	 if(null !=p) {
	    WSUtils utils = WSUtils.getINSTANCE();
	    pageIndex= utils.getInteger(p, "pageIndex");
	    pageSize= utils.getInteger(p, "pageSize");
	    total= utils.getInteger(p, "total");
	 }
 }

  @Override
  public String toString() {
    return new StringBuilder()
      .append("Page")
      .append("{ pageIndex:")
      .append(pageIndex)
      .append(", pageSize:")
      .append(pageSize)
      .append(", total:")
      .append(total)
      .append(" }")
      .toString();
  }



public int getPageIndex() {
	return pageIndex;
}



public int getPageSize() {
	return pageSize;
}



public int getTotal() {
	return total;
}

}
