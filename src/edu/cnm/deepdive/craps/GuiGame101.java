/**
 * 
 */
package edu.cnm.deepdive.craps;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Yolanda Philgreen
 *
 */
public class GuiGame101 {
  JFrame frame;
  ImageIcon[] dieFaces;
  JButton roll1;
  JButton roll2;
  JButton play;
  JButton stop;
  JPanel content;
  
  
  
  public void createAndShowGui() {
    JFrame frame = new JFrame("Simple Time-Wasting Craps Game");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    dieFaces = new ImageIcon[6];
    for (int i = 0; i < 6; i++) {
      dieFaces[i] = createImageIcon("images/" + (i+1) +".png");
      
    }
 
    roll1 = new JButton(dieFaces[5]);
    roll2 = new JButton (dieFaces[5]);
    play = new JButton ("Roll!");
    stop = new JButton ("Stop!");
    roll1.setEnabled(false);
    roll2.setEnabled(false);
    play.setEnabled(false);
    stop.setEnabled(false);
    content = new JPanel();
    content.add(roll1);
    content.add(roll2);
    content.add(play);
    content.add(stop);
    content.setOpaque(true);
    frame.setContentPane(content);
    frame.pack();
    frame.setVisible(true);
  }
  
  private void play() {
    StateMachine101 croupier = new StateMachine101();
    croupier.setDisplay(new StateMachine101.Display() {
      @Override
      public void update(int[] roll) {
        roll1.setIcon(dieFaces[roll[0] - 1]);
        roll2.setIcon(dieFaces[roll[1] - 1]); 
      }
      
    });
    croupier.setPlayable(new StateMachine101.Playable() {
      @Override
      public boolean playAgain(int wins, int losses) {
        // TODO Auto-generated method stub
        return false;
      }
      
    });
    croupier.setContinuable(new StateMachine101.Continuable() {
      @Override
      public boolean continuePlay(int wins, int losses, int point) {
        return false;
      }
      
    });
  }
  
  private static ImageIcon createImageIcon(String path) {
    URL imgURL = GuiGame101.class.getClassLoader().getResource(path);
    return new ImageIcon(imgURL);  
  }
  /**
   * @param args
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        GuiGame101 game = new GuiGame101();
        game.createAndShowGui();      
      }
      
    });

  }

}
