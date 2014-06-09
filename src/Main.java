import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Main {

  static JFrame f, loading;
  static final int WIDTH = 800;
  static final int HEIGHT = WIDTH/4*3; //4:3 aspect ratio
  
  static JPanel panel; //the main JPanel
  
  static EventManager em;
  
  static String normalFontName = "resources\\lucidasans.ttf";
  static String monospaceFontName = "resources\\consolas.ttf";
  static Font normalFont, monospaceFont;
  
  private static void createAndShowGUI() {
    f = new JFrame("Syntaxilizer - By Alex Li");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(WIDTH, HEIGHT);
    f.setResizable(false);
    f.setLocationRelativeTo(null); //center window on screen
    
    //create the panel and the layout
    panel = new JPanel();
    panel.setBackground(new Color(152, 186, 200));
    panel.setLayout(new GridBagLayout());
   
    //Set layouts and add things from EventManager to panel    
    GridBagConstraints c = new GridBagConstraints();
    
    c.fill = GridBagConstraints.BOTH;
    c.gridwidth = 1;
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(10, 10, 10, 0);
    c.anchor = GridBagConstraints.LINE_START;
    panel.add(em.step1, c);
    
    c.fill = GridBagConstraints.NONE;
    c.gridwidth = 1;
    c.gridx = 1;
    c.gridy = 0;
    c.insets = new Insets(10, 0, 10, 10);
    c.anchor = GridBagConstraints.LINE_END;
    panel.add(em.helpButton, c);
    
    c.fill = GridBagConstraints.BOTH;
    c.gridwidth = 1;
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 10;
    c.weighty = 5;
    c.insets = new Insets(0, 10, 10, 10);
    panel.add(em.scrollPaneBN, c);
    
    JPanel sidepanel1 = new JPanel();
    sidepanel1.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    sidepanel1.setBackground(new Color(130, 180, 200));
    sidepanel1.setLayout(new GridLayout(5, 1, 5, 5));
    sidepanel1.add(em.promptBN);
    sidepanel1.add(em.optionsBN);
    sidepanel1.add(em.promptDicts);
    sidepanel1.add(em.optionsDicts);
    sidepanel1.add(em.lockButton);
    
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 1;
    c.gridy = 1;
    c.weightx = 3;
    c.weighty = 0;
    panel.add(sidepanel1, c);
    
    c.gridx = 0;
    c.gridy = 3;
    c.weighty = 1;
    c.anchor = GridBagConstraints.LINE_START;
    c.insets = new Insets(0, 10, 0, 10);
    panel.add(em.step2, c);

    c.fill = GridBagConstraints.BOTH;
    c.gridwidth = 1;
    c.gridheight = 2;
    c.gridx = 0;
    c.gridy = 4;
    c.weightx = 10;
    c.weighty = 10;
    c.insets = new Insets(10, 10, 10, 10);
    panel.add(em.scrollPaneIn, c);
    
    JPanel sidepanel2 = new JPanel();
    sidepanel2.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    sidepanel2.setBackground(new Color(130, 180, 200));
    sidepanel2.setLayout(new GridLayout(6, 1, 5, 5));
    sidepanel2.add(em.promptTexts);
    sidepanel2.add(em.optionsTexts);
    sidepanel2.add(em.promptSymbols);
    sidepanel2.add(em.optionsSymbols);
    sidepanel2.add(em.step3);
    sidepanel2.add(em.analyzeButton);
    c.fill = GridBagConstraints.BOTH;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.gridx = 1;
    c.gridy = 4;
    c.weightx = 3;
    c.weighty = 5;
    c.insets = new Insets(10, 10, 0, 10);
    panel.add(sidepanel2, c);
    
    JPanel sidepanel3 = new JPanel();
    sidepanel3.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    sidepanel3.setBackground(new Color(130, 180, 200));
    sidepanel3.setLayout(new GridLayout(1, 1, 5, 5));
    sidepanel3.add(em.resetButton);
    c.fill = GridBagConstraints.BOTH;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.gridx = 1;
    c.gridy = 5;
    c.weightx = 3;
    c.weighty = 2;
    c.insets = new Insets(10, 10, 10, 10);
    panel.add(sidepanel3, c);
    
    f.add(panel);
    f.add(em.statusPanel, BorderLayout.SOUTH);
    
    loading.dispose();
    f.setVisible(true);
  }
	
  public static void main(String[] args) {
    //Display a loading screen
    loading = new JFrame("Syntaxilizer - By Alex Li");
    loading.getContentPane().add(new JLabel(new ImageIcon("resources\\loading.png")));
    loading.setUndecorated(true);
    loading.setSize(500, 100);
    loading.setBackground(new Color(0, 0, 0, 0));
    loading.setResizable(false);
    loading.setLocationRelativeTo(null); //center window on screen
    loading.setVisible(true);
    
    //Load some fonts (must be done BEFORE EventManager)
    try {
      normalFont = Font.createFont(Font.TRUETYPE_FONT, new File(normalFontName));
      monospaceFont = Font.createFont(Font.TRUETYPE_FONT, new File(monospaceFontName));
    } catch (Exception e) {
      System.err.println("Error loading fonts!");
      e.printStackTrace();
    }
    
    //Create the EventManager, which stores all the components
    //and also listens to actions, linking it to the back-end
    em = new EventManager();
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });    
  }
}
