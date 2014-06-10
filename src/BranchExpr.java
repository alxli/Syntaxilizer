/**
 * BranchExpr represents a node in the syntax tree
 * 
 * If hasRHS is false, then BranchExpr is a leaf node.
 * Otherwise, the possible matches are two lhs or rhs BranchExpr.
 * The Vector expr stores a bunch of symbols or literals.
 * 
 * @author    Alex Li <alextrovert@gmail.com>
 * @version   1.0
 */

import java.util.Vector;

class BranchExpr {
  
  //A series of ConcatExpr - the text must match ALL OF THESE in order
  Vector<ConcatExpr> expr; //empty and not used if hasRHS
  
  boolean hasRHS;
  BranchExpr lhs, rhs;
    
  /**
   * Constructor
   * @param b   is this a junction or a leaf node in the syntax tree?
   */
  public BranchExpr(boolean b) {
    this.hasRHS = b;
    this.expr = new Vector<ConcatExpr>();
  }
  
  /**
   * Constructor
   * @param b   is this a junction or a leaf node in the syntax tree?
   * @param l   the left-hand side BranchExpr in the definition
   * @param r   the right-hand side BranchExpr in the definition
   */
  public BranchExpr(boolean b, BranchExpr l, BranchExpr r) {
    this.hasRHS = b;
    this.lhs = l;
    this.rhs = r;
  }
  
  /**
   * Add a ConcatExpr (only used for when hasRHS is false)
   * @param ce  the ConcatExpr to be added
   */
  public void add(ConcatExpr ce) {
    this.expr.add(ce);
  }
  
}
