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

public class ResultFrame extends JFrame {
  
  private HashMap<String, TreeSet<String>> res;
  private JPanel resultPanel;
  private JTextArea resultArea;
  
  public ResultFrame(boolean matched,
                     HashMap<String, TreeSet<String>> hm) {
    res = hm;
    
    this.setTitle("Analysis Results");
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setSize(Main.WIDTH/2, Main.HEIGHT - 100);
    this.setResizable(false);
    this.setLocationRelativeTo(null); //center window on screen
    
    resultPanel = new JPanel();
    resultPanel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    
    JLabel resultLabel = new JLabel();
    resultLabel.setFont(Main.normalFont.deriveFont(13.0f));
    if (matched) {
      resultPanel.setBackground(Color.green);
      resultLabel.setText("<html>Text successfully matched by automaton.<br/>" +
                          "See matches below:</html>");
    } else {
      resultPanel.setBackground(Color.red);
      resultLabel.setText("<html>Text could not be matched by automaton.<br/>" +
                          "See partial matches below:</html>");
    }
    
    resultArea = new JTextArea();
    resultArea.setEditable(false);
    resultArea.setFont(Main.monospaceFont.deriveFont(13.0f));
    resultArea.setText(resToString());
    JScrollPane resultScrollArea = new JScrollPane(resultArea);
    
    //smack the things onto the JPanel
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 0;
    c.weighty = 1;
    c.insets = new Insets(10, 0, 10, 0);
    c.anchor = GridBagConstraints.LINE_START;
    resultPanel.add(resultLabel, c);
    
    c.gridx = 0;
    c.gridy = 1;
    c.weighty = 10;
    resultPanel.add(resultScrollArea, c);
    
    this.add(resultPanel);
    this.setVisible(true);
  }
  
  private String resToString() {
    if (res == null) return "";
    String resultStr = "";
    for (String symbol : res.keySet()) {
      resultStr += "Matches for <" + symbol + ">:\n";
      TreeSet<String> matches = res.get(symbol);
      for (String match : matches)
        resultStr += ">>> " + match + "\n";
      resultStr += "\n";
    }
    return resultStr;
  }
}
