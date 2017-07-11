/**
 * 
 */
package edu.cnm.deepdive.craps;

import java.util.Arrays;

/**
 * @author Yolanda Philgreen 
 *
 */
public class ConsoleGame101 {

  /**
   * @param args
   */
  public static void main(String[] args) {
    StateMachine101 croupier = new StateMachine101();
    croupier.setDisplay((roll)-> {
      System.out.printf("Roll : %s %n", Arrays.toString(roll));
    });
    croupier.setPlayable((wins, losses)->  {
     String input =  System.console().readLine(
         "%d wins,%d losses. Play again? ([y]/n)", wins, losses);
     return (input.length() == 0||input.toLowerCase().charAt(0)== 'y');
    });
    croupier.setContinuable((wins, losses, point) -> {
      String input = System.console().readLine("Point is %d. Continue play? ([y]/n)", point);
      return (input.length() == 0 || input.toLowerCase().charAt(0) == 'y');
    });
    croupier.play();
  }

}
