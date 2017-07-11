package edu.cnm.deepdive.craps;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * @author 
 */
public class GuiGame 
  implements StateMachine.Playable, StateMachine.Continuable, StateMachine.Display {
  
  private static final String WINS_FORMAT ="Wins = %d";
  private static final String LOSSES_FORMAT = "LOSSES = %d";
  private static final String POINT_FORMAT = "Point = %d";
  private static final String ROLL_FORMAT = "Roll = %d";
  
  private ImageIcon[] dieFaces;
  private JButton roll1;
  private JButton roll2;
  private JButton play;
  private JButton stop;
  private JLabel wins;
  private JLabel losses;
  private JLabel point;
  private JLabel roll;

  private boolean uiSetup = false;
  private boolean playClicked = false;
  private boolean stopClicked = false;
  
  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    GuiGame game = new GuiGame();
    SwingUtilities.invokeLater(() -> game.createAndShowGui());
    game.play();
    System.exit(0);
  }

  private static ImageIcon createImageIcon(String path) {
    URL imgURL = GuiGame.class.getClassLoader().getResource(path);
    return new ImageIcon(imgURL);
  }

  private void createAndShowGui() {
    JFrame frame = new JFrame("Simple Time-Wasting Craps Game");
    JPanel dicePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JPanel textPanel = new JPanel (new FlowLayout(FlowLayout.CENTER));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    dieFaces = new ImageIcon[6];
    for (int i = 0; i < 6; i++) {
      dieFaces[i] = createImageIcon(String.format("images/%d.png", i + 1));
    }
    roll1 = new JButton(dieFaces[5]);
    roll2 = new JButton(dieFaces[5]);
    roll1.setEnabled(false);
    roll2.setEnabled(false);
    dicePanel.add(roll1);
    dicePanel.add(roll2);
    play = new JButton("Roll!");
    stop = new JButton("Stop!");
    disableButtons();
    play.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
       disableButtons();
        resumePlay();
      }
      
    });
    stop.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent evt) {
       disableButtons();
        stopPlay();
      }
      
    });
    
    buttonPanel.add(play);
    buttonPanel.add(stop);
    wins = new JLabel(String.format(WINS_FORMAT, 0));
    losses = new JLabel(String.format(LOSSES_FORMAT, 0));
    point = new JLabel(String.format(POINT_FORMAT, 0));
    roll = new JLabel (String.format(ROLL_FORMAT, 0));
    textPanel.add(wins);
    textPanel.add(losses);
    textPanel.add(point);
    textPanel.add(roll);
    wins.setVisible(false);
    losses.setVisible(false);
    point.setVisible(false);
    roll.setVisible(false);
    frame.add(dicePanel, BorderLayout.NORTH);
    frame.add(textPanel, BorderLayout.CENTER);
    frame.add(buttonPanel, BorderLayout.SOUTH);
    frame.pack();
    frame.setVisible(true);
    synchronized (this) {
      uiSetup = true;
      notify();
    }
  }

  private synchronized void play() {
    while (!uiSetup) {
      try {
        wait();
      } catch (InterruptedException ex){
        
      }
    }
    StateMachine croupier = new StateMachine();
    croupier.setDisplay(this);
    croupier.setPlayable(this);
    croupier.setContinuable(this);
    croupier.play();
  }
  
  @Override
  public void update(int[] roll) {
    SwingUtilities.invokeLater(new Runnable(){
      @Override
      public void run() {
        roll1.setIcon(dieFaces[roll[0] -1]);
        roll2.setIcon(dieFaces[roll[1]-1]);
      }
    });
    
  }

  @Override
  public boolean playAgain(int wins, int losses) {
    JLabel winsLabel = this.wins;
    JLabel lossesLabel = this.losses;
    JLabel pointLabel = this.point;
    SwingUtilities.invokeLater(() -> {
      pointLabel.setVisible(false);
      winsLabel.setText(String.format(WINS_FORMAT, wins));
      lossesLabel.setText(String.format(LOSSES_FORMAT, losses));
      winsLabel.setVisible(true);
      lossesLabel.setVisible(true);
    });
    return getUserResponse();
  }

  @Override
  public boolean continuePlay(int point) {
    JLabel pointLabel = this.point;
    SwingUtilities.invokeLater(() -> {
      pointLabel.setText(String.format(POINT_FORMAT, point));
     pointLabel.setVisible(true); 
    });
    return getUserResponse();
  }

  private synchronized boolean getUserResponse() {
    SwingUtilities.invokeLater(() -> enableButtons());
    while (! playClicked && ! stopClicked) {
      try {
        wait();
      } catch (InterruptedException ex) {
        
      }
    }
    boolean result = playClicked;
    playClicked = false;
    return result;
  }
  
  private void enableButtons() {
    play.setEnabled(true);
    play.setEnabled(true); 
  }
  private void disableButtons() {
    play.setEnabled(false);
    stop.setEnabled(false);
  }
  
  private synchronized void resumePlay(){
    playClicked = true;
    notify();
  }
  private synchronized void stopPlay() {
    stopClicked = true;
    notify();
  }
}
