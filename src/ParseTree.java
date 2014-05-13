import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;


public class ParseTree {
  
  private class Sentence {
    
  }
  
  public static boolean isPunc(char c) {
    return "~!@#$%^()_+`-={}|[]\\:\";'<>?,./\'".indexOf(c) != -1;
  }
  
  //splits a sentence to a list of words and punctuation
  //"Hello! I'm Bob" ==> {"Hello", "!", "I", "'", "m", "Bob"} 
  public static Vector<String> tokenize(String str) {
    Vector<String> tokens = new Vector<String>();
    
    String t[] = str.split("[\\s]");
    for (int i = 0; i < t.length; i++) {
      for (int j = 0; j < t[i].length(); j++) {
        if (isPunc(t[i].charAt(j))) {
          tokens.addElement("" + t[i].charAt(j));
        }
      }
    }
    
    return tokens;
  }
}
