import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main {
  static JFrame f;
  public static final int WIDTH = 720;
  public static final int HEIGHT = WIDTH/4*3; //4:3 aspect ratio
  
  static JPanel panel;
  static JTextField field;
  
	private static void createAndShowGUI() {
    f = new JFrame("Syntaxilizer");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(WIDTH, HEIGHT);
    f.setResizable(false);
    f.setLocationRelativeTo(null); //center window on screen
    f.setVisible(true);
    
    //create the panel and the layout
    panel = new JPanel();
    panel.setLayout(new GridLayout(2, 1));
    
    //customize and add the text field
    field = new JTextField();
    //field.setFont(new Font(""));
    
    panel.add(field);
    
    
    f.add(panel);
  }
	
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
    
    ParseTree.tokenize("The quick brown fox jumped over the lazy dog.");
  }
}
