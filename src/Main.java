import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

public class Main {

  static JFrame f;
  public static final int WIDTH = 800;
  public static final int HEIGHT = WIDTH/4*3; //4:3 aspect ratio
  
  static JPanel panel; //the main JPanel
  static JEditorPane editorBN; //Backus-Naur editor
  static JScrollPane scrollPaneBN; //to contain the textArea
  
  //status bar at the bottom
  static JPanel statusPanel;
  static JLabel statusLabel;

  static String normalFontName = "resources\\lucidasans.ttf";
  static String monospaceFontName = "resources\\consolas.ttf";
  static Font normalFont, monospaceFont;
  
  private static void createAndShowGUI() {
    f = new JFrame("Syntaxilizer - by Alex Li");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setSize(WIDTH, HEIGHT);
    f.setResizable(false);
    f.setLocationRelativeTo(null); //center window on screen
    f.setVisible(true);
    
    //create the panel and the layout
    panel = new JPanel();
    panel.setBackground(new Color(190, 200, 200));
    panel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
      
    //Add label for step1 - instructions
    JLabel step1 = new JLabel("", JLabel.CENTER);
    step1.setText("<html>1. Load the <a href=\"\">Backus-Naur Form</a>.</html>");
    step1.setCursor(new Cursor(Cursor.HAND_CURSOR)); //link
    step1.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        try {
          //TODO: provide help window for this specific Backus-Naur implementation
          Desktop.getDesktop().browse(new URI("http://en.wikipedia.org/wiki/Backus%E2%80%93Naur_Form"));
        } catch (URISyntaxException | IOException ex) {
          ex.printStackTrace(); //???
        }
      }
    });
    step1.setFont(normalFont.deriveFont(13.0f));
    step1.setHorizontalAlignment(SwingConstants.LEFT);
    
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    c.weightx = 0.5;
    c.anchor = GridBagConstraints.FIRST_LINE_START;
    c.insets = new Insets(10, 10, 10, 10);
    panel.add(step1, c);
    
    //create and customize the text field
    editorBN = new JEditorPane();
    editorBN.setPreferredSize(new Dimension(WIDTH - 100, 250));
    editorBN.setMargin(new Insets(3, 3, 3, 3)); //inside padding
    //textArea.setLineWrap(true);
    //textArea.setWrapStyleWord(true);
    editorBN.setFont(monospaceFont.deriveFont(13.0f));
    scrollPaneBN = new JScrollPane(editorBN);
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1.0;
    c.weighty = 1.0;
    c.insets = new Insets(0, 10, 10, 10);
    panel.add(scrollPaneBN, c);
    
    
    f.add(panel);
        
    // Create a status bar at the bottom
    statusPanel = new JPanel();
    statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    statusPanel.setPreferredSize(new Dimension(f.getWidth(), 20));
    statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
    
    statusLabel = new JLabel("Status Bar");
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    statusPanel.add(statusLabel);
    
    f.add(statusPanel, BorderLayout.SOUTH);
  }
	
  public static void main(String[] args) {
    //Load some fonts
    try {
      normalFont = Font.createFont(Font.TRUETYPE_FONT, new File(normalFontName));
      monospaceFont = Font.createFont(Font.TRUETYPE_FONT, new File(monospaceFontName));
    } catch (Exception e) {
      System.err.println("Error loading fonts!");
      e.printStackTrace();
    }
    
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        createAndShowGUI();
      }
    });
  }
}
