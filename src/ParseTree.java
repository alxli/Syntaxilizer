import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;

//parse model in Backus-Naur form
public class ParseTree {
  
  public static boolean isPunc(char c) {
    return "~!@#$%^()_+`-={}|[]\\:\";'<>?,./\'".indexOf(c) != -1;
  }
  
  //splits a sentence to a list of words and punctuation
  //"Hello! I'm Bob" ==> {"Hello", "!", "I", "'", "m", "Bob"} 
  public static Vector<String> tokenize(String str) {
    Vector<String> tokens = new Vector<String>();
    
    String t[] = str.split("[\\s]");
    for (int i = 0; i < t.length; i++) {
      String curr = "";
      for (int j = 0; j < t[i].length(); j++) {
        if (isPunc(t[i].charAt(j))) {
          if (curr.length() > 0) {
            tokens.addElement(curr);
            curr = "";
          }
          tokens.addElement("" + t[i].charAt(j));
          t[i] = t[i].substring(j+1);
          if (t[i].length() > 0) {
            i--;
            //break;
          }
          break;
        }
        curr = curr + t[i].charAt(j);
      }
      if (curr.length() > 0) tokens.addElement(curr);
    }
    //System.err.println(tokens);
    return tokens;
  }
}
