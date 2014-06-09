/**
 * HelpFrame.java
 * 
 * Simple frame to contain an HTML help page
 */

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HelpFrame extends JFrame {
  
  private JPanel helpPanel;
  private JEditorPane helpPane;
  
  public HelpFrame() {    
    this.setTitle("Syntaxilizer Help");
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setSize(Main.WIDTH - 200, Main.HEIGHT - 100);
    this.setResizable(true);
    this.setLocationRelativeTo(null); //center window on screen
    
    helpPanel = new JPanel();
    
    helpPane = new JEditorPane();
    helpPane.setMargin(new Insets(10, 10, 10, 10));
    helpPane.setEditable(false);
    helpPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
    helpPane.addHyperlinkListener(new HyperlinkListener() {
      public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
          if (Desktop.isDesktopSupported()) {
            try {
              Desktop.getDesktop().browse(e.getURL().toURI());
            } catch (Exception e1) {
              e1.printStackTrace();
            }
          }
        }
      }
    });
    try {
      helpPane.setPage((new File("resources\\help.html")).toURI().toURL());
    } catch (IOException e) {
      helpPane.setText("Cannot load help file from resources\\help.html");
      e.printStackTrace();
    }
    helpPane.setFont(Main.normalFont.deriveFont(13.0f));
    JScrollPane editorScrollPane = new JScrollPane(helpPane);
    editorScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    editorScrollPane.setPreferredSize(new Dimension(250, 145));
    editorScrollPane.setMinimumSize(new Dimension(10, 10));
    
    this.add(editorScrollPane);
    this.setVisible(true);
  }
  
}
