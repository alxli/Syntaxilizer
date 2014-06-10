/**
 * A JFrame that displays the results of the matching.
 * 
 * @author    Alex Li <alextrovert@gmail.com>
 * @version   1.0
 */

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ResultFrame extends JFrame implements ActionListener {

  private static final long serialVersionUID = 1L;
  private JPanel resultPanel;
  private JTextArea resultArea;
  private JButton closeButton;
  
  /**
   * Constructor from matching results
   * @param matched       whether the match was successful
   * @param resultString  the formatted String of matches to display
   */
  public ResultFrame(boolean matched, String resultString) {
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
    resultArea.setText(resultString);
    JScrollPane resultScrollArea = new JScrollPane(resultArea);
    
    closeButton = new JButton("Done");
    closeButton.setFont(Main.normalFont.deriveFont(13.0f));
    closeButton.setActionCommand("Close");
    closeButton.addActionListener(this);
    
    //Add components to the JPanel using GridBagLayout
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridy = 0;
    c.weighty = 1;
    c.insets = new Insets(10, 0, 10, 0);
    c.anchor = GridBagConstraints.LINE_START;
    resultPanel.add(resultLabel, c);
    
    c.gridx = 0;
    c.gridy = 1;
    c.weighty = 20;
    resultPanel.add(resultScrollArea, c);
    
    c.gridx = 0;
    c.gridy = 2;
    c.weighty = 1;
    resultPanel.add(closeButton, c);
    
    this.add(resultPanel);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Close")) {
      this.dispose();
    }
  }
}
