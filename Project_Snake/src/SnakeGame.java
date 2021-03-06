
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Timer;

import javax.swing.*;

public class SnakeGame extends JFrame{

	//changed pixels from 501 x 501 to 505 x 528
	public final static int xPixelMaxDimension = 505;  //Pixels in window. 501 to have 50-pixel squares plus 1 to draw a border on last square
	public final static int yPixelMaxDimension = 528;

	public static int xSquares ;
	public static int ySquares ;

	public static int squareSize = 50;

	protected static Snake snake ;
	
	protected static boolean hardMode = false;
	
	protected boolean gethardMode(){
		return this.hardMode;
	}
	protected static void setHardMode(){
		if(hardMode == false){
			hardMode = true;
		}else hardMode = false;
	}

//	protected static boolean moreSquares = false;
//	
//	protected static void setMoreSquares(){
//		if(moreSquares == false && lessSquares == false){
//			moreSquares = true;
//		}else moreSquares = false;
//	}
//	
//	protected static boolean lessSquares = false;
//	
//	protected static void setLessSquares(){
//		if(lessSquares == false && moreSquares == false){
//			lessSquares = true;
//		}else moreSquares = false;
//	}
//	
//	protected static void resetSquares(){
//		moreSquares = false;
//		lessSquares = false;
//	}
	
	protected static Kibble kibble;

	protected static Score score;
	
	static final int BEFORE_GAME = 1;
	static final int DURING_GAME = 2;
	static final int GAME_OVER = 3;
	static final int GAME_WON = 4;   //The values are not important. The important thing is to use the constants 
	//instead of the values so you are clear what you are setting. Easy to forget what number is Game over vs. game won
	//Using constant names instead makes it easier to keep it straight. Refer to these variables 
	//using statements such as SnakeGame.GAME_OVER 

	private static int gameStage = BEFORE_GAME;  //use this to figure out what should be happening. 
	//Other classes like Snake and DrawSnakeGamePanel will need to query this, and change it's value

	protected static long clockInterval = 500; //controls game speed
	//Every time the clock ticks, the snake moves
	//This is the time between clock ticks, in milliseconds
	//1000 milliseconds = 1  second.
	
	static JFrame snakeFrame;
	static JFrame menuFrame;

	static JMenuBar menuBar;
	static JMenu snakeMenu;
	
	static DrawSnakeGamePanel snakePanel;
	//Framework for this class adapted from the Java Swing Tutorial, FrameDemo and Custom Painting Demo. You should find them useful too.
	//http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/FrameDemoProject/src/components/FrameDemo.java
	//http://docs.oracle.com/javase/tutorial/uiswing/painting/step2.html

	private static void createAndShowGUI() {
		//Create and set up the window.//
		snakeFrame = new JFrame();
		
		snakeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Size of panel in pixels
		snakeFrame.setSize(xPixelMaxDimension, yPixelMaxDimension);
		
		//TODO change undecorated from true to false. Title Bar no longer hidden.
		snakeFrame.setUndecorated(false); //hide title bar
		snakeFrame.setVisible(true);
		snakeFrame.setResizable(false);

		snakePanel = new DrawSnakeGamePanel(snake, kibble, score);
		snakePanel.setFocusable(true);
		snakePanel.requestFocusInWindow(); //required to give this component the focus so it can generate KeyEvents

		snakeFrame.add(snakePanel);
		snakePanel.addKeyListener(new GameControls(snake));

		setGameStage(BEFORE_GAME);

		snakeFrame.setVisible(true);
	}

	private static void initializeGame() {
		//code meant to change the square size on the screen. does not work.
//		if(SnakeGame.moreSquares == true){
//			SnakeGame.squareSize = 25;
//		}else if(SnakeGame.lessSquares == true){
//			SnakeGame.squareSize = 100;
//		}else SnakeGame.squareSize = 50;
		
		//set up score, snake and first kibble
		xSquares = xPixelMaxDimension / squareSize;
		ySquares = yPixelMaxDimension / squareSize;

		//set the snake
		snake = new Snake(xSquares, ySquares, squareSize);
		kibble = new Kibble(snake);
		score = new Score();

		//game stage is set to the start menu
		gameStage = BEFORE_GAME;
	}

	//generates a new game with the option to include hard mode.
	protected static void newGame() {
		Timer timer = new Timer();
		//sets hard mode clock interval if relevant.
		if(hardMode == true){
			SnakeGame.clockInterval = 200;
		}else SnakeGame.clockInterval = 500;
		//sets the game clock.
		GameClock clockTick = new GameClock(snake, kibble, score, snakePanel);
		timer.scheduleAtFixedRate(clockTick, 0 , clockInterval);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initializeGame();
				createAndShowGUI();
			}
		});
	}

	//returns the stage of the game.
	public static int getGameStage() {
		return gameStage;
	}

	//if the game is over, returns a true value.
	public static boolean gameEnded() {
		if (gameStage == GAME_OVER || gameStage == GAME_WON){
			return true;
		//if the game was won, sets the next game to hard mode and returns true.
		}else if(gameStage == GAME_WON){
			SnakeGame.hardMode = true;
			return true;
		}else return false;
	}

	//sets the game stage to the stage input.
	public static void setGameStage(int gameStage) {
		SnakeGame.gameStage = gameStage;
	}
}
