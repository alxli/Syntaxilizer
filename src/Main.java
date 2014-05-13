import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
  private static JFrame f;
  public static final int WIDTH = 640;
  public static final int HEIGHT = WIDTH/4*3; //4:3 aspect ratio
  
	private static void createAndShowGUI() {
    f = new JFrame("Syntaxilizer");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(WIDTH, HEIGHT);
    f.setResizable(false);
    f.setLocationRelativeTo(null); //center window on screen
    f.setVisible(true);
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
