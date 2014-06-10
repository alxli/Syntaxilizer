/**
 * ConcatExpr represents a leaf node in the syntax tree
 * 
 * The text must match all of the Items in order.
 * An item is either a symbol or a literal value.
 * 
 * @author    Alex Li <alextrovert@gmail.com>
 * @version   1.0
 */


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
  
  Vector<Item> items;
  char quantifier; //For later releases
  
  /**
   * Constructor
   */
  public ConcatExpr() {
    items = new Vector<Item>();
  }
  
  /**
   * Appends an item to the ordered list of items that this much match
   * @param v   the value of the Item
   * @param l   whether the item is a literal
   */
  public void addItem(String v, boolean l) {
    items.add(new Item(v, l));
  }
  
}
