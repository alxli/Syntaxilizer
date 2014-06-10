/**
 * JFrame that displays "help.html" from the resources folder
 * 
 * @author    Alex Li <alextrovert@gmail.com>
 * @version   1.0
 */

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class HelpFrame extends JFrame {
  
  private static final long serialVersionUID = 1L;
  private JEditorPane helpPane;
  
  /**
   * Constructor
   */
  public HelpFrame() {    
    this.setTitle("Syntaxilizer Help");
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.setSize(Main.WIDTH - 200, Main.HEIGHT - 100);
    this.setResizable(true);
    this.setLocationRelativeTo(null); //center window on screen
        
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
    } catch (Exception e) {
      helpPane.setText("Cannot load help file from resources\\help.html");
      e.printStackTrace();
    }
    helpPane.setFont(Main.normalFont.deriveFont(13.0f));
    JScrollPane scrollPane = new JScrollPane(helpPane);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setPreferredSize(new Dimension(250, 145));
    scrollPane.setMinimumSize(new Dimension(10, 10));
    
    this.add(scrollPane);
    this.setVisible(true);
  }
  
}
