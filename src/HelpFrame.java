import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpFrame extends JFrame {
  
  private JPanel helpPanel;
  private JTextArea helpArea;
  
  public HelpFrame() {    
    this.setTitle("Help");
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setSize(Main.WIDTH/2, Main.HEIGHT - 100);
    this.setResizable(false);
    this.setLocationRelativeTo(null); //center window on screen
    
    helpPanel = new JPanel();
    
    helpArea = new JTextArea(100, 100);
    helpArea.setEditable(false);
    helpArea.setFont(Main.normalFont.deriveFont(13.0f));
    helpArea.setText("HELP");
    JScrollPane resultScrollArea = new JScrollPane(helpArea);
    
    this.add(helpPanel);
    this.setVisible(true);
  }
  
}
