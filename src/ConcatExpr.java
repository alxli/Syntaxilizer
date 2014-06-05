import java.util.Vector;

class ConcatExpr {
  
  public class Item {
    String value;
    boolean isLiteral;
    
    public Item(String v, boolean l) {
      isLiteral = l;
      value = v;
    }
  }
  
  //the things that it concatenates
  Vector<Item> items;
  char quantifier;
  
  public ConcatExpr() {
    items = new Vector<Item>();
  }
  
  public void addItem(String v, boolean l) {
    items.add(new Item(v, l));
  }
}
