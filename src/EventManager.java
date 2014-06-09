/**
 * EventManager.java
 * 
 * Interface between front-end and back-end.
 * Stores all child components of the JFrame
 */

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.TreeSet;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class EventManager implements ActionListener {
  
  static final String exdir = "examples\\";
  
  JLabel resetBotton, helpButton;
  
  JLabel step1, promptBN, promptDicts, promptSymbols;
  JComboBox optionsBN, optionsDicts;
  JButton lockButton;
  
  JTextArea editorBN; //Backus-Naur editor
  JScrollPane scrollPaneBN; //to contain the BN editor
  
  JLabel step2, promptTexts, step3;
  JComboBox optionsTexts, optionsSymbols;
  JButton analyzeButton, resetButton;
  
  JTextArea editorIn; //to contain the input text to be parsed
  JScrollPane scrollPaneIn; //to contain the text editor
  
  //status bar at the bottom
  JPanel statusPanel;
  JLabel statusLabel;
  
  //Help dialogue box
  HelpFrame helpFrame;
  ResultFrame resultFrame;
  
  public EventManager() {
    helpButton = new JLabel("<html><a href=\"#\">Help</a></html>");
    helpButton.setAlignmentX(SwingConstants.RIGHT);
    helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); //link
    helpButton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent me) {
        try {
          helpFrame = new HelpFrame();
        } catch (Exception e) {
          e.printStackTrace(); //???
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
          Desktop.getDesktop().browse(new URI("http://en.wikipedia.org/wiki/Backus%E2%80%93Naur_Form"));
        } catch (Exception e) {
          e.printStackTrace(); //???
        }
      }
    });
    
    editorBN = new JTextArea(); //Backus-Naur editor
    //editorBN.setLineWrap(true);
    //editorBN.setWrapStyleWord(true);
    editorBN.setDisabledTextColor(Color.darkGray);
    editorBN.setMargin(new Insets(5, 5, 5, 5)); //inside padding
    editorBN.setFont(Main.monospaceFont.deriveFont(13.0f));
    scrollPaneBN = new JScrollPane(editorBN);
    
    promptBN = new JLabel("<html>Enter the Backus-Naur form on the left,<br/>" +
                                  "Or load from file/examples:</html>");
    promptBN.setFont(Main.normalFont.deriveFont(13.0f));
    
    optionsBN = new JComboBox(); //for selecting examples
    optionsBN.setFont(Main.monospaceFont.deriveFont(13.0f));
    optionsBN.addActionListener(this);
    
    promptDicts = new JLabel("<html>Select a default dictionary,<br/>" +
                                  "Or load your custom one from file:</html>");
    promptDicts.setFont(Main.normalFont.deriveFont(13.0f));

    optionsDicts = new JComboBox(); //for selecting dictionaries
    optionsDicts.setFont(Main.monospaceFont.deriveFont(13.0f));
    
    lockButton = new JButton("Lock and Load");
    lockButton.setFont(Main.normalFont.deriveFont(Font.BOLD, 13.0f));
    lockButton.setActionCommand("Load");
    lockButton.addActionListener(this);
    
    step2 = new JLabel();
    step2.setHorizontalAlignment(SwingConstants.LEFT);
    step2.setFont(Main.normalFont.deriveFont(13.0f));
    step2.setText("<html><b>Step 2.</b> Load your input text to be analyzed.");
    
    promptTexts = new JLabel("<html>Load a block text to be analyzed,<br/>" +
                                    "From examples, or from file:</html>");
    promptTexts.setFont(Main.normalFont.deriveFont(13.0f));
    optionsTexts = new JComboBox(); //for loading input example texts
    optionsTexts.setFont(Main.monospaceFont.deriveFont(13.0f));
    optionsTexts.addActionListener(this);
    
    promptSymbols = new JLabel("<html>Select a symbol to match:</html>");
    promptSymbols.setFont(Main.normalFont.deriveFont(13.0f));
    optionsSymbols = new JComboBox();
    optionsSymbols.setFont(Main.monospaceFont.deriveFont(13.0f));
    
    step3 = new JLabel();
    step3.setHorizontalAlignment(SwingConstants.LEFT);
    step3.setFont(Main.normalFont.deriveFont(13.0f));
    step3.setText("<html><b>Step 3.</b> Break it down!");
    
    analyzeButton = new JButton("Analyze");
    analyzeButton.setFont(Main.normalFont.deriveFont(Font.BOLD, 13.0f));
    analyzeButton.setActionCommand("Analyze");
    analyzeButton.addActionListener(this);
    
    resetButton = new JButton("Reset All");
    resetButton.setFont(Main.normalFont.deriveFont(Font.BOLD, 13.0f));
    resetButton.setActionCommand("Reset");
    resetButton.addActionListener(this);
    
    editorIn = new JTextArea(); //Input text editor
    //editorIn.setLineWrap(true);
    //editorIn.setWrapStyleWord(true);
    editorIn.setDisabledTextColor(Color.darkGray);
    editorIn.setMargin(new Insets(5, 5, 5, 5)); //inside padding
    editorIn.setFont(Main.monospaceFont.deriveFont(13.0f));
    scrollPaneIn = new JScrollPane(editorIn);

    //Create a status bar at the bottom
    statusPanel = new JPanel();
    statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
    statusPanel.setPreferredSize(new Dimension(Main.WIDTH, 20));
    statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
    
    statusLabel = new JLabel("Status: Ready");
    statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
    statusPanel.add(statusLabel);
    
    loadExampleBN();
    loadExampleDicts();
    loadExampleTexts();
    refresh();
  }
  
  //load all files with .bn extension 
  private void loadExampleBN() {
    File dir = new File(exdir);
    optionsBN.addItem("None");
    for (File file : dir.listFiles()) {
      if (!file.isDirectory()) {
        String name = file.getName();
        if (name.endsWith(".bn")) {
          optionsBN.addItem(name.substring(0, name.length()));
        }
      }
    }
  }
  
  private void loadExampleDicts() {
    File dir = new File(exdir);
    optionsDicts.addItem("None");
    for (File file : dir.listFiles()) {
      if (!file.isDirectory()) {
        String name = file.getName();
        if (name.endsWith(".bnd")) {
          optionsDicts.addItem(name.substring(0, name.length()));
        }
      }
    }
  }
  
  private void loadExampleTexts() {
    optionsTexts.addItem("None");
    File dir = new File(exdir);
    for (File file : dir.listFiles()) {
      if (!file.isDirectory()) {
        String name = file.getName();
        if (name.endsWith(".txt")) {
          optionsTexts.addItem(name.substring(0, name.length()));
        }
      }
    }
  }
  
  enum State { step1, step2, help, results };
  State currState = State.step1;
  BackusNaur bn;
  String symbolToMatch;
  
  HashMap<String, TreeSet<String>> res; //temp map to store results
  
  /******************* PROCESS ACTIONS *******************/
  
  @Override
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource().equals(optionsBN)) { //Backus-Naur example option
      if (optionsBN.getSelectedItem().toString().startsWith("None")) return;
      try { //load example BN
        File f = new File(exdir + optionsBN.getSelectedItem());
        editorBN.setText(BackusNaur.fileToString(f));
        editorBN.setCaretPosition(editorBN.getDocument().getLength());
      } catch (Exception e) {
        JOptionPane.showMessageDialog(Main.f, e.getMessage(),
                                      "Error loading example file.",
                                      JOptionPane.WARNING_MESSAGE);
      }
      return;
    }
    if (ae.getSource().equals(optionsTexts)) { //Text example option
      if (optionsTexts.getSelectedItem().toString().startsWith("None")) return;
      try { //load example texts
        File f = new File(exdir + optionsTexts.getSelectedItem());
        editorIn.setText(BackusNaur.fileToString(f));
        editorIn.setCaretPosition(editorIn.getDocument().getLength());
      } catch (Exception e) {
        JOptionPane.showMessageDialog(Main.f, e.getMessage(),
                                      "Error loading example text.",
                                      JOptionPane.WARNING_MESSAGE);
      }
      return;
    }
    
    //Button clicks    
    if (ae.getActionCommand() == "Load") {
      try {
        //load dictionary
        if (optionsDicts.getSelectedItem().toString().startsWith("None")) {
          bn = new BackusNaur(editorBN.getText());
        } else {
          File f = new File(exdir + optionsDicts.getSelectedItem());
          bn = new BackusNaur(editorBN.getText() + "\n" +
                              BackusNaur.fileToString(f));
        }
      } catch (Exception e) {
        JOptionPane.showMessageDialog(Main.f, e.getMessage(),
                                      "Error parsing Backus-Naur Form!",
                                      JOptionPane.WARNING_MESSAGE);
        return;
      }
      
      statusLabel.setText("Successfully loaded Backus-Naur form!");
      currState = State.step2;
      
      //Add possible symbols to select from
      optionsSymbols.removeAll();
      for (String s : bn.defs.keySet()) optionsSymbols.addItem(s);

    } else if (ae.getActionCommand() == "Analyze") {
      boolean matched = false;
      try {
        res = new HashMap<String, TreeSet<String>>();
        matched = bn.matches(optionsSymbols.getSelectedItem().toString(),
                             editorIn.getText(), res);

      } catch (Exception e) {
        JOptionPane.showMessageDialog(Main.f, e.getMessage(),
                                      "Error encountered while matching text",
                                      JOptionPane.WARNING_MESSAGE);
        return;
      }
      
      
      if (resultFrame != null) resultFrame.dispose();
      resultFrame = new ResultFrame(matched, res);
      currState = State.results;
      
    } else if (ae.getActionCommand() == "Reset") {
      currState = State.step1;
      //editorBN.setText("");
      editorIn.setText("");
    }
    refresh();
  }
  
  private void refresh() {
    if (currState == State.step1) {
      //Enable step 1 components
      editorBN.setBackground(Color.white);
      editorBN.setEnabled(true);
      optionsBN.setEnabled(true);
      optionsDicts.setEnabled(true);
      lockButton.setEnabled(true);
      
      //Disable step 2 components
      editorIn.setBackground(Color.lightGray);
      editorIn.setEnabled(false);
      optionsTexts.setEnabled(false);
      optionsSymbols.setEnabled(false);
      analyzeButton.setEnabled(false);
      
      //Display status
      statusLabel.setText("Status: Ready");
    } else if (currState == State.step2) {
      //Disable step 1 components
      editorBN.setBackground(Color.lightGray);
      editorBN.setEnabled(false);
      optionsBN.setEnabled(false);
      optionsDicts.setEnabled(false);
      lockButton.setEnabled(false);
      
      //Enable step 2 components
      editorIn.setBackground(Color.white);
      editorIn.setEnabled(true);
      optionsTexts.setEnabled(true);
      optionsSymbols.setEnabled(true);
      analyzeButton.setEnabled(true);
    }
  }
}
