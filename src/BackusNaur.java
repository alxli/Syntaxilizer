import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Vector;

/**
 * Extended Backus-Naur Form Implementation
 * 
 *    |   - OR operator: Either the LHS or the RHS occurs
 * 
 * Grouping Expressions:
 *    {}  - Enclosed expression can occur 0 or more times
 *    {}* - Enclosed expression can occur 0 or more times
 *    {}+ - Enclosed expression can occur 1 or more times
 *    {}? - Enclosed expression can occur 0 or 1 times
 *    []  - Enclosed expression can occur 0 or 1 times
 * 
 * Quantifiers:
 *    * - The preceding item can occur 0 or more times
 *    + - The preceding item can occur 1 or more times
 *    ? - The preceding item can occur 0 or 1 times
 */

public class BackusNaur {
  
  static void db(Object o) { if (true) System.err.println(o); }
  
  //Where all the definitions are stored - HashMap<symbol, expression>
  private HashMap<String, BranchExpr> defs = new HashMap<String, BranchExpr>();
  
  private class BranchExpr {
    //whether it has a RHS to be considered
    //otherwise, it is a singular Concat_Expr 
    boolean hasRHS;
    Vector<ConcatExpr> expr; //null or empty if hasRHS
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
  
  /****************** CONSTRUCTION ******************/
  
  //constructor from file
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
        
        //db(lineTokens);
        
        if (lineTokens.size() > 1 && lineTokens.get(1).equals("::=")) {
          if (!lineTokens.isEmpty()) {
            try {
              parseDefinition(lineTokens);
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
  
  //constructor from String
  public BackusNaur(String s) {
    BufferedReader in;
    try {
      in = new BufferedReader(new StringReader(s));
      int line_num = 0;
      
      String line;
      Vector<String> tokens = new Vector<String>();
      
      while ((line = in.readLine()) != null) {
        line_num++;
        if ((line = line.trim()).isEmpty()) continue;
        
        Vector<String> lineTokens = getTokens(line);
        
        if (lineTokens.size() > 1 && lineTokens.get(1).equals("::=")) {
          if (!lineTokens.isEmpty()) {
            try {
              parseDefinition(lineTokens);
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
      System.err.println("Error loading Backus-Naur from text.");
      e.printStackTrace();
    }
  }
  
  //preprocesses s, also splitting it into tokens
  private static Vector<String> getTokens(String s) {
    //Surround '{', '}', '[', ']', characters with spaces
    //Convert [] to {}?, which is the same representation
    s = s.replace("{", " { ").replace("}", " } ");
    s = s.replace("[", " { ").replace("]", " } ? ");
    
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
  
  //parses the Vector of tokens and appends this to the list of tokens
  private void parseDefinition(Vector<String> tokens) throws Exception {   
    //check to make sure the LHS of the line is in angled brackets
    if (!validVariable(tokens.get(0))) {
      throw new Exception("1st token on each line must be enclosed in angle brackets.");
    }
    if (!tokens.get(1).equals("::=")) {
      throw new Exception("2nd token on each line must be \"::=\".");
    }
    defs.put(tokens.get(0).substring(1, tokens.get(0).length() - 1),
             parseBranchExpr(tokens, 2, tokens.size()));
  }
  
  /**
   * @params: tokens, and range of tokens [lo, hi) to consider
   */
  private BranchExpr parseBranchExpr(Vector<String> tokens,
                                     int lo, int hi) throws Exception {
    if (lo >= hi) return null; // ???
    //db(lo + " " + hi + ": " + tokens.subList(lo, hi));
    
    //find the last index of the OR symbol, before index hi
    //this is because OR operators works like ((A | B) | C), not (A | (B | C))
    int idx = tokens.lastIndexOf("|", hi - 1);
    if (idx == -1 || idx < lo) { //no OR signs in the range
      BranchExpr be = new BranchExpr();
      be.hasRHS = false;
      be.expr = new Vector<ConcatExpr>();
      
      int curr = lo;
      
      while (curr < hi) {
        if (tokens.get(curr).equals("{")) { //does an open curly bracket exist?
          //find right bracket
          int lidx = curr, ridx = tokens.indexOf("}", lidx);
          
          //check for existence and in range
          if (ridx >= hi || ridx == -1)
            throw new Exception("Mismatched brace quantifier {}.");
          
          //currently no support for multiple level brackets, e.g. {a{b}}
          if (tokens.indexOf("{", lidx + 1) != -1 &&
              tokens.indexOf("{", lidx + 1) < ridx) {
            throw new Exception("Currently only 1 level of brace quantifiers {} are supported.");
          }
          
          ConcatExpr ce = parseConcatExpr(tokens, lidx + 1, ridx);
          if (ridx < tokens.size()) {
            if (tokens.get(ridx).equals("*")) ce.quantifier = '*';
            else if (tokens.get(ridx).equals("+")) ce.quantifier = '+';
            else if (tokens.get(ridx).equals("?")) ce.quantifier = '?';
            else {
              ce.quantifier = '*';
              ridx--;
            }
            ridx++;
          }
          be.expr.add(ce);
          
          curr = ridx + 1; //skip the current position to after the close brace
          continue; //keep parsing!!!!!
        }
        //just parse a normal expression, up to the next open brace { in range
        int ridx = tokens.indexOf("{", curr + 1);
        if (ridx == -1 || ridx > hi) ridx = hi;
        be.expr.add(parseConcatExpr(tokens, curr, ridx));
        curr = ridx; //move on to the next ConcatExpr to be parsed
      }
      
      return be;
    }
    BranchExpr be = new BranchExpr();
    be.hasRHS = true;
    be.lhs = parseBranchExpr(tokens, lo, idx);
    be.rhs = parseBranchExpr(tokens, idx + 1, hi);
    return be;
  }
  
  /**
   * @params: tokens, and range of tokens [lo, hi) to consider
   */
  private ConcatExpr parseConcatExpr(Vector<String> tokens,
                                     int lo, int hi) {
    //db("concat: " + tokens.subList(lo, hi));
  
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
  
  private static boolean validVariable(String s) {
    return (s.charAt(0) == '<') &&
           (s.charAt(s.length() - 1) == '>');
  }
  
  /******************* VALIDATION *******************/
  
  private Vector<String> invalidSymbols = new Vector<String>();
  
  private boolean validate(ConcatExpr ce) {
    boolean valid = true;
    for (int i = 0; i < ce.items.size(); i++) {
      if (ce.items.get(i).isLiteral) continue;
      //make sure the symbol is already defined
      if (!defs.containsKey(ce.items.get(i).value)) {
        valid = false;
        invalidSymbols.add(ce.items.get(i).value);
      }
    }
    return valid;
  }
  
  private boolean validate(BranchExpr be) {
    if (be.hasRHS)
      return validate(be.lhs) && validate(be.rhs);
    boolean valid = true;
    for (int i = 0; i < be.expr.size(); i++) {
      if (!validate(be.expr.get(i))) valid = false;
    }
    return valid;
  }
  
  //validate the Backus-Naur form to make sure everything is defined.
  private boolean validate() {
    boolean valid = true;
    //loops through all definitions
    for (BranchExpr be : defs.values()) {
      //recurse the tree to ensure that all "symbols" are defined
      if (!validate(be)) valid = false;
    }
    if (!valid) System.err.println("Undefined symbols: " + invalidSymbols);
    return valid;
  }
  
  /****************** (Public) MATCHING INPUTS *******************/
  
  //returns whether the symbol s accepts a text t
  public boolean matches(String s, String t) throws Exception {
    if (!defs.containsKey(s))
      throw new Exception("Error: symbol " + s + " not defined.");
    
    
    return true;
  }
  
  /******************* Tests ******************/

  public static void main(String[] args) {
    //System.out.println(getTokens("<opt-suffix-part> ::= \"Sr.\" | \"Jr.\" | <roman-numeral> | \"\""));
    BackusNaur bn = new BackusNaur(new File("resources/test.bn"));
    db(bn.validate());
  }
}
