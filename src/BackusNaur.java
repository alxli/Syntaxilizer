import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;


/**
 * Extended Backus-Naur Form Implementation
 * 
 *    |   - OR operator: Either the LHS or the RHS occurs
 * 
 * Grouping Expressions:
 *    []  - Enclosed expression can occur 0 or 1 times
 *    {}  - Enclosed expression can occur 0 or more times
 *    {}* - Enclosed expression can occur 0 or more times
 *    {}+ - Enclosed expression can occur 1 or more times
 *    {}? - Enclosed expression can occur 0 or 1 times
 * 
 * Quantifiers:
 *    * - The preceding item can occur 0 or more times
 *    + - The preceding item can occur 1 or more times
 *    ? - The preceding item can occur 0 or 1 times
 */

public class BackusNaur {
  
  static void db(Object o) { if (true) System.err.println(o); }
  
  private class Definition {
    String lhs; //the symbol
    BranchExpr expr; //the expression
  }
  
  private class BranchExpr {
    //whether it has a RHS to be considered
    //otherwise, it is a singular Concat_Expr 
    boolean hasRHS;
    ConcatExpr expr; //null if hasRHS
    BranchExpr lhs, rhs;
    
    char quantifier;
  }
  
  private class ConcatExpr {
    //the things that it concatenates
    Vector<Item> items;
    
    char quantifier;
  }
  
  private class Item {
    String value;
    boolean isLiteral;
    
    public Item(String v, boolean l) {
      isLiteral = l;
      value = v;
    }
  }
  
  //splits into tokens
  private static Vector<String> getTokens(String s) {
    //Surround '{', '}', '[', ']', characters with spaces 
    s = s.replace("{", " { ").replace("}", " } ");
    s = s.replace("[", " [ ").replace("]", " ] ");
    
    Vector<String> tokens = new Vector<String>();
    
    boolean quoteOpened = false;
    String curr = "";
    for (int i = 0; i < s.length(); i++) {
      if ((!quoteOpened && s.charAt(i) == ' ') ||
          (!quoteOpened && s.charAt(i) == '"')) {
        if (curr.length() > 0) tokens.add(curr);
        curr = "";
        if (s.charAt(i) == '"') {
          quoteOpened = true;
        }
      } else {
        if (s.charAt(i) == '"') {
          if (quoteOpened) {
            quoteOpened = false;
            tokens.add(curr); //can be zero length
            curr = "";
          } else {
            if (curr.length() > 0) tokens.add(curr);
            curr = "";
            quoteOpened = true;
          }
        } else {
          curr += s.charAt(i);
        }
      }
    }
    if (curr.length() > 0) tokens.add(curr);
    return tokens;
  }
  
  private static boolean validVariable(String s) {
    return (s.charAt(0) == '<') &&
           (s.charAt(s.length() - 1) == '>');
  }

  /**
   * @params: tokens, and range of tokens [lo, hi) to consider
   */
  private ConcatExpr parseConcatExpr(Vector<String> tokens,
                                     int lo, int hi) {
    ConcatExpr expr = new ConcatExpr();
    expr.items = new Vector<Item>();
    for (int i = lo; i < hi; i++) {
      String v = tokens.get(i);
      if (v.length() >= 2 && v.charAt(0) == '<' && v.charAt(v.length() - 1) == '>') {
        expr.items.add(new Item(v.substring(1, v.length() - 1), false));
      } else {
        expr.items.add(new Item(v, true)); //string literal
      }
    }
    return expr;
  }
  
  /**
   * @params: tokens, and range of tokens [lo, hi) to consider
   */
  private BranchExpr parseBranchExpr(Vector<String> tokens,
                                     int lo, int hi) throws Exception {
    if (lo >= hi) return null; // ???
    db(lo + " " + hi);
    
    //find the last index of the OR symbol, before index hi
    //this is because OR operators works like ((A | B) | C), not (A | (B | C))
    int idx = tokens.lastIndexOf("|", hi - 1);
    if (idx == -1 || idx < lo) { //no OR signs in the range
      BranchExpr expr = new BranchExpr();
      expr.hasRHS = false;
      expr.expr = parseConcatExpr(tokens, lo, hi);
      return expr;
    }
    BranchExpr expr = new BranchExpr();
    expr.hasRHS = true;
    expr.lhs = parseBranchExpr(tokens, lo, idx);
    expr.rhs = parseBranchExpr(tokens, idx + 1, hi);
    return expr;
  }
  
  private Definition parseDefinition(Vector<String> tokens) throws Exception {   
    Definition def = new Definition();
    
    //check to make sure the LHS of the line is in angled brackets
    if (!validVariable(tokens.get(0))) {
      throw new Exception("1st token on each line must be enclosed in angle brackets.");
    }
    if (!tokens.get(1).equals("::=")) {
      throw new Exception("2nd token on each line must be \"::=\".");
    }
    def.lhs = tokens.get(0);
    
    db(tokens);
    
    def.expr = parseBranchExpr(tokens, 0, tokens.size());
    
    return def;
  }
  
  private Vector<Definition> defs = new Vector<Definition>();
  
  public BackusNaur(File f) {
    BufferedReader in;
    try {
      in = new BufferedReader(new FileReader(f));
      int line_num = 0;
      
      String line;
      Vector<String> tokens = new Vector<String>();
      
      while ((line = in.readLine()) != null) {
        line_num++;
        if ((line = line.trim()).isEmpty()) continue;
        
        Vector<String> lineTokens = getTokens(line);
        
        db(lineTokens);
        
        if (lineTokens.get(1).equals("::=")) {
          if (!tokens.isEmpty()) {
            try {
              defs.add(parseDefinition(tokens));
            } catch (Exception e) {
              System.err.println("Error parsing Backus-Naur file on line " + line_num);
              System.err.println("\t" + e.getMessage());
              e.printStackTrace();
            }
          }
          tokens = lineTokens;
        } else {
          tokens.addAll(lineTokens);
        }
      }
    } catch (IOException e) {
      System.err.println("Error loading Backus-Naur definition file.");
      System.err.println("\tCannot load file: " + f.getName());
      e.printStackTrace();
    }
  }
  
  
  public static void main(String[] args) {
    //System.out.println(getTokens("<opt-suffix-part> ::= \"Sr.\" | \"Jr.\" | <roman-numeral> | \"\""));
    BackusNaur bn = new BackusNaur(new File("test.bn"));
  }
}
