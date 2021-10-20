package store;

import java.util.List;

import d3e.core.ListExt;

public class D3EQuery {
  String query;
  List<Object> args = ListExt.List();
  D3EQuery pre;
  
  public D3EQuery() {
    // TODO Auto-generated constructor stub
  }
  
  public void addPreQuery(D3EQuery q) {
    if (this.pre != null) {
      this.pre.addPreQuery(q);
      return;
    }
    this.pre = q;
  }
  
  public void setQuery(String q) {
    this.query = q;
  }
  
  public void setArgs(List<Object> args) {
    this.args = args;
  }
}
