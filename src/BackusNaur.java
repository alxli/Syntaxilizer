/**
 * Back-end that implements Backus-Naur matching algorithms.
 * The Backus-Naur Form (BNF) is implemented here as a syntax tree.
 * A BranchExpr represents a non-leaf node (Union)
 * A ConcatExpr represents a leaf node of the tree (Concatenation)
 * 
 * @author    Alex Li <alextrovert@gmail.com>
 * @version   1.0
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

public class BackusNaur {
  
  static void db(Object o) { if (true) System.err.println(o); }
  
  //Where all the definitions are stored - HashMap<symbol, expression>
  public TreeMap<String, BranchExpr> defs = new TreeMap<String, BranchExpr>();
  //list of symbol for reference, in the order that they are listed
  public Vector<String> symbols = new Vector<String>();
  
  /*************************** Construction ***************************/
  
  /**
   * Loads a File into a String, including newlines
   * @param f   a file in the current relative directory
   * @return a  string of the file
   */
  public static String fileToString(File f) throws Exception {
    StringBuilder sb = new StringBuilder();
    String line;
    try {
      BufferedReader in = new BufferedReader(new FileReader(f));
      while ((line = in.readLine()) != null) sb.append(line + "\n");
    } catch (Exception e) {
      throw new Exception("Error loading Backus-Naur definition file." +
                          "\tCannot load file: " + f.getName());
    }
    return sb.toString();
  }
  
  /**
   * Constructor from file
   * @param f   a file in the current relative directory
   */
  public BackusNaur(File f) throws Exception {
    this(fileToString(f));
  }
  
  /**
   * Constructor from file
   * @param s   a string of defintiions, separated by new lines
   */
  public BackusNaur(String s) throws Exception {
    BufferedReader in;
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
            throw new Exception("Line " + line_num + ": " + e.getMessage());
          }
        }
        tokens = lineTokens;
      } else {
        tokens.addAll(lineTokens);
      }
    }
    if (defs.isEmpty())
      throw new Exception("Error: No definitions were recognized.");
    this.validate();
  }
  
  /**
   * Preprocess a Backus-Naur definition line, splitting it into tokens.
   * @param s   a line in the Backus-Naur input definition
   * @return v  a vector of tokens for the input string
   */
  private static Vector<String> getTokens(String s) {
    //Surround '{', '}', '[', ']', characters with spaces
    //Convert [] to {}?, which is the same representation
    s = s.replace("{", " { ").replace("}", " } ");
    s = s.replace("[", " { ").replace("]", " } ? ");
    
    //replace pipe | characters that are not in quotes
    s = s.replaceAll("[^\\w\"<>']+\\|[^\\w\"<>']+", " | ");
    
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
          if (quoteOpened) { //close the quote
            quoteOpened = false;
            //split the entire quote into tokens
            String[] currTokens = curr.split(" ");
            for (String token : currTokens) tokens.add(token);
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
  
  /**
   * Parses a definition from tokens, adding it to def, the collection of definitions
   * @param tokens  the list of tokens
   */
  private void parseDefinition(Vector<String> tokens) throws Exception {   
    //check to make sure the LHS of the line is in angled brackets
    if (!validVariable(tokens.get(0)))
      throw new Exception("1st token on each line must be enclosed in angle brackets.");
    if (!tokens.get(1).equals("::="))
      throw new Exception("2nd token on each line must be \"::=\".");
    //take off the angle brackets
    String symbol = tokens.get(0).substring(1, tokens.get(0).length() - 1);
    if (defs.containsKey(symbol))
      throw new Exception("Symbol <" + symbol + "> already declared.");
    
    //Current do not support recursive definitions
    //or quantifiers as in Extended Backus-Naur form
    for (int i = 2; i < tokens.size(); i++) {
      if (tokens.get(i).equals(tokens.get(0)))
        throw new Exception("Recursive definitions are currently unsupported.");
      if (tokens.get(i).equals("{") || tokens.get(i).equals("}") ||
          tokens.get(i).equals("{") || tokens.get(i).equals("}"))
        throw new Exception("Brace {} quantifiers are currently unsupported.");
    }
    if (tokens.size() < 2)
      throw new Exception("Too few tokens on the line!");
    defs.put(symbol, parseBranchExpr(tokens, 2, tokens.size()));
    symbols.add(symbol);
  }
  
  /**
   * Creates a BranchExpr from a range [lo, hi) in a list of tokens
   * @param: tokens a list of tokens from which to get the definition
   * @param: lo     lower index in the list to consider, inclusive
   * @param: hi     upper index in the list to consider, exclusive
   */
  private BranchExpr parseBranchExpr(Vector<String> tokens, int lo, int hi) throws Exception {
    if (lo >= hi) return null;
    //find the last index of the OR symbol, before index hi
    //this is because OR operators works like ((A | B) | C), not (A | (B | C))
    int idx = tokens.lastIndexOf("|", hi - 1);
    if (idx == -1 || idx < lo) { //no OR signs in the range
      BranchExpr be = new BranchExpr(false);
     
      int curr = lo;
      while (curr < hi) {
        if (tokens.get(curr).equals("{")) { //does an open curly bracket exist?
          
          int lidx = curr, ridx = tokens.indexOf("}", lidx); //find right bracket
          
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
          be.add(ce);
          curr = ridx + 1; //skip the current position to after the close brace
          continue; //keep parsing!!!!!
        }
        //just parse a normal expression, up to the next open brace { in range
        int ridx = tokens.indexOf("{", curr + 1);
        if (ridx == -1 || ridx > hi) ridx = hi;
        be.add(parseConcatExpr(tokens, curr, ridx));
        curr = ridx; //move on to the next ConcatExpr to be parsed
      }
      return be;
    }
    BranchExpr be = new BranchExpr(true,
                                   parseBranchExpr(tokens, lo, idx),
                                   parseBranchExpr(tokens, idx + 1, hi));
    return be;
  }
  
  /**
   * Creates a ConcatExpr from a range [lo, hi) in a list of tokens
   * @param: tokens a list of tokens from which to get the definition
   * @param: lo     lower index in the list to consider, inclusive
   * @param: hi     upper index in the list to consider, exclusive
   */
  private ConcatExpr parseConcatExpr(Vector<String> tokens, int lo, int hi) {
    ConcatExpr expr = new ConcatExpr();
    for (int i = lo; i < hi; i++) {
      String v = tokens.get(i);
      if (v.length() >= 2 && v.charAt(0) == '<' && v.charAt(v.length() - 1) == '>') {
        expr.addItem(v.substring(1, v.length() - 1), false); //symbol
      } else {
        expr.addItem(v, true); //literal value
      }
    }
    return expr;
  }
  
  private static boolean validVariable(String s) {
    return (s.charAt(0) == '<') && (s.charAt(s.length() - 1) == '>');
  }
  
  /**************************** Validation ****************************/
  
  private HashSet<String> invalidSymbols = new HashSet<String>();
  
  /**
   * Validate the current BNF, ensuring that all symbols are defined
   * @throws Exception  a message specifying which symbols are undefined
   */
  private void validate() throws Exception {
    boolean valid = true;
    //loops through all definitions, for each definition:
    //  recurse the tree to ensure that all of its symbols are defined
    for (BranchExpr be : defs.values())
      if (!validate(be)) valid = false;
    if (!valid) 
      throw new Exception("Undefined symbol(s): " + invalidSymbols + "\n" +
                          "Maybe you should load some dictionaries?");
  }
  
  /**
   * @param be  BranchExpr to check validity
   * @return    whether be's symbols are all defined
   */
  private boolean validate(BranchExpr be) {
    if (be.hasRHS)
      return validate(be.lhs) && validate(be.rhs);
    boolean valid = true;
    for (int i = 0; i < be.expr.size(); i++)
      if (!validate(be.expr.get(i))) valid = false;
    return valid;
  }
  
  /**
   * @param ce  ConcatExpr to check validity
   * @return    whether ce's symbols are all defined
   */
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
  
  /*************************** Matching Input Strings ***************************/
  
  //temporary structure to store results that are displayed
  private HashMap<String, TreeSet<String>> results;
  
  //maximum search time (ticks) and maximum depth
  private static final long MAX_TIME = 2L*1000000000L; //2 seconds
  private static final int MAX_DEPTH = 2000; //maximum recursion depth
  private long startTime; //System.nanoTime() when the search started
  private boolean recursedTooDeep, searchedTooLong;
  
  /**
   * Performs a matching attempt, storing all found matches into m, regardless of matching success
   * @param s   symbol in to be matched, which must be a key of defs
   * @param t   text of the user to be tokenized and matched
   * @param m   an <em>already instantiated</em> HashMap in which the result is stored
   * @return    whether the definition for the symbol s matches the text t
   */
  public boolean matches(String s, String t, HashMap<String, TreeSet<String>> m) throws Exception {
    if (!defs.containsKey(s))
      throw new Exception("Error: symbol <" + s + "> not defined.");
    
    //remove all non-word characters 0-9, a-z, A-Z, then split into tokens
    Vector<String> tTokens = new Vector<String>(
                                  Arrays.asList(t.replaceAll("[.,!@#$%^&*()]", "").split("\\s+")));
    recursedTooDeep = false;
    searchedTooLong = false;
    results = m;
    startTime = System.nanoTime();
    int endidx = match(defs.get(s), tTokens, 0, 0);
    if (recursedTooDeep)
      throw new Exception("Note: Some searches terminated early due too recursion too deep.");
    if (searchedTooLong)
      throw new Exception("Note: Search took too long and was terminated early.");
    if (endidx == tTokens.size()) {
      String toks = vectorToStr(tTokens, 0, tTokens.size());
      if (results.get(s) == null) results.put(s, new TreeSet<String>());
      results.get(s).add(toks);
    }
    return endidx == tTokens.size();
  }
  
  /**
   * Recursive helper function for matching BranchExpr
   * @param ce      BranchExpr to attempt to match
   * @param tokens  list of tokens
   * @param lo      the index of the first element in tokens to start the matching
   * @param depth   the current depth of the recursion, used to terminate early
   * @return        one more than the index up to where ce is matched in tokens
   */
  private int match(BranchExpr be, Vector<String> tokens, int lo, int depth) {
    if (lo == tokens.size()) return lo;
    if (System.nanoTime() - startTime > MAX_TIME) {
      searchedTooLong = true;
      return -1;
    }
    if (depth > MAX_DEPTH) {
      recursedTooDeep = true;
      return -1;
    }
    if (!be.hasRHS) {
      int id = lo;
      for (int i = 0; i < be.expr.size(); i++) {
        if (id >= tokens.size()) return -1;
        id = match(be.expr.get(i), tokens, id, depth + 1);
        if (id < 0) return id;
      }
      return id;
    }
    return Math.max(match(be.lhs, tokens, lo, depth + 1),
                    match(be.rhs, tokens, lo, depth + 1));
  }
  
  /**
   * Recursive helper function for matching ConcatExpr
   * @param ce      ConcatExpr to attempt to match
   * @param tokens  list of tokens
   * @param lo      the index of the first element in tokens to start the matching
   * @param depth   the current depth of the recursion, used to terminate early
   * @return        one more than the index up to where ce is matched in tokens
   */
  private int match(ConcatExpr ce, Vector<String> tokens, int lo, int depth) {
    int id = lo;
    for (int i = 0; i < ce.items.size(); i++) {
      if (id >= tokens.size()) return -1;
      if (ce.items.get(i).isLiteral) {
        if (!tokens.get(id).toLowerCase().equals(ce.items.get(i).value.toLowerCase()))
          return -1;
        id++; //move to next token
      } else { //try to match the symbol
        int prev = id;
        id = match(defs.get(ce.items.get(i).value), tokens, id, depth + 1);
        if (id < 0) return id;
        if (!results.containsKey(ce.items.get(i).value))
          results.put(ce.items.get(i).value, new TreeSet<String>());
        results.get(ce.items.get(i).value).add(vectorToStr(tokens, prev, id));
        if (id >= tokens.size()) return id;
      }
    }
    return id;
  }
  
  /**
   * Converts a Vector of the results to a String to be displayed in ResultFrame
   * @param v   Vector of Strings to concatenate
   * @param lo  lower bound of indices to consider, inclusive
   * @param hi  upper bound of indices to consider, exclusive
   * @return    the converted String
   */
  private static String vectorToStr(Vector<String> v, int lo, int hi) {
    if (lo < 0 || hi > v.size()) return "";
    String res = "[ ";
    for (int i = lo; i < hi; i++) res += v.get(i) + " ";
    return res + "]";
  }
  
  /*************************** Testing ***************************/

  public static void main(String[] args) throws Exception {
    BackusNaur bn = new BackusNaur(new File("resources/test.bn"));   
    HashMap<String, TreeSet<String>> res = new HashMap<String, TreeSet<String>>();
    boolean matched = bn.matches("fruit", "apple", res);
    if (matched) System.out.println("matched");
    for (String symbol : res.keySet()) {
      System.out.printf("Matches for <%s>:\n", symbol);
      TreeSet<String> matches = res.get(symbol);
      for (String match : matches) {
        System.out.println(">>> " + match);
      }
      System.out.println();
    }
  }
}
