import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class EventManager {
  
  JLabel helpButton;
  
  JLabel step1, optionsPromptBN, optionsPromptDicts;
  JComboBox optionsBN, optionsDicts;
  JButton lockButton;
  
  JEditorPane editorBN; //Backus-Naur editor
  JScrollPane scrollPaneBN; //to contain the BN editor
  
  JLabel step2, optionsPromptTexts, step3;
  JComboBox optionsTexts;
  JButton analyzeButton, resetButton;
  
  JEditorPane editorIn; //to contain the input text to be parsed
  JScrollPane scrollPaneIn; //to contain the text editor
  
  //status bar at the bottom
  JPanel statusPanel;
  JLabel statusLabel;
  
  public EventManager() {
    helpButton = new JLabel("<html><a href=\"#\">Help</a></html>");
    helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); //link
    helpButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent me) {
        try {
          System.err.println("Help Clicked!");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
    
    step1 = new JLabel();
    step1.setHorizontalAlignment(SwingConstants.LEFT);
    step1.setFont(Main.normalFont.deriveFont(13.0f));
    step1.setText("<html><b>Step 1.</b> Load the automaton using <a href=\"\">Backus-Naur Form</a>.</html>");
    step1.setCursor(new Cursor(Cursor.HAND_CURSOR)); //link
    step1.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent me) {
        try {
          //TODO: provide help window for this specific Backus-Naur implementation
          Desktop.getDesktop().browse(new URI("http://en.wikipedia.org/wiki/Backus%E2%80%93Naur_Form"));
        } catch (Exception e) {
          e.printStackTrace(); //???
        }
      }
    });
    
    editorBN = new JEditorPane(); //Backus-Naur editor
    editorBN.setMargin(new Insets(5, 5, 5, 5)); //inside padding
    editorBN.setFont(Main.monospaceFont.deriveFont(13.0f));
    scrollPaneBN = new JScrollPane(editorBN);
    
    optionsPromptBN = new JLabel("<html>Enter the Backus-Naur form on the left,<br/>" +
                                  "Or load from file/examples:</html>");
    optionsPromptBN.setFont(Main.normalFont.deriveFont(13.0f));
    
    optionsBN = new JComboBox(); //for selecting examples
    
    optionsPromptDicts = new JLabel("<html>Select a default dictionary,<br/>" +
                                  "Or load your custom one from file:</html>");
    optionsPromptDicts.setFont(Main.normalFont.deriveFont(13.0f));

    optionsDicts = new JComboBox(); //for selecting dictionaries
    
    lockButton = new JButton("Lock and Load");
    lockButton.setFont(Main.normalFont.deriveFont(Font.BOLD, 13.0f));
    
    step2 = new JLabel();
    step2.setHorizontalAlignment(SwingConstants.LEFT);
    step2.setFont(Main.normalFont.deriveFont(13.0f));
    step2.setText("<html><b>Step 2.</b> Load your input text to be analyzed.");
    
    optionsPromptTexts = new JLabel("<html>Load a block text to be analyzed,<br/>" +
                                    "From examples, or from file:</html>");
    optionsTexts = new JComboBox(); //for selecting input texts
    
    step3 = new JLabel();
    step3.setHorizontalAlignment(SwingConstants.LEFT);
    step3.setFont(Main.normalFont.deriveFont(13.0f));
    step3.setText("<html><b>Step 3.</b> Break it down!");
    
    analyzeButton = new JButton("Analyze");
    analyzeButton.setFont(Main.normalFont.deriveFont(Font.BOLD, 13.0f));
    resetButton = new JButton("Reset All");
    resetButton.setFont(Main.normalFont.deriveFont(Font.BOLD, 13.0f));
    
    editorIn = new JEditorPane(); //Input text editor
    editorIn.setMargin(new Insets(5, 5, 5, 5)); //inside padding
    editorIn.setFont(Main.monospaceFont.deriveFont(13.0f));
    scrollPaneIn = new JScrollPane(editorIn);

    //Create a status bar at the bottom
    statusPanel = new JPanel();
    statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    statusPanel.setPreferredSize(new Dimension(Main.WIDTH, 20));
    statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
    
    statusLabel = new JLabel("Status: Ready!");
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    statusPanel.add(statusLabel);
  }
}
