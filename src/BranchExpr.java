import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

class BranchExpr {
  //A bunch of symbols or literals
  
  Vector<ConcatExpr> expr; //empty if hasRHS
  
  //whether it has a RHS to be considered
  //otherwise, it is a singular ConcatExpr 
  boolean hasRHS;
  BranchExpr lhs, rhs;
  
  char quantifier;

  public BranchExpr() {}
  
  public BranchExpr(boolean b) {
    this.hasRHS = b;
    this.expr = new Vector<ConcatExpr>();
  }
  
  public BranchExpr(boolean b, BranchExpr l, BranchExpr r) {
    this.hasRHS = b;
    this.lhs = l;
    this.rhs = r;
  }
  
  public void add(ConcatExpr ce) {
    this.expr.add(ce);
  }
  
}
