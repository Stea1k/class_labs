
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Snake {

	final int DIRECTION_UP = 0;
	final int DIRECTION_DOWN = 1;
	final int DIRECTION_LEFT = 2;
	final int DIRECTION_RIGHT = 3;  //These are completely arbitrary numbers. 

	private boolean hitWall = false;
	private boolean ateTail = false;
//	private boolean hitBlock = false;
//	private Block block = new Block();

	private int snakeSquares[][];  //represents all of the squares on the screen
	//NOT pixels!
	//A 0 means there is no part of the snake in this square
	//A non-zero number means part of the snake is in the square
	//The head of the snake is 1, rest of segments are numbered in order
//	private int blockSquares[][];
	

	private int currentHeading;  //Direction snake is going in, not direction user is telling snake to go
	private int lastHeading;    //Last confirmed movement of snake. See moveSnake method
	
	private int snakeSize;   //size of snake - how many segments?
	
	//TODO growth increment should be 1, not 2. Logic Error. 
	private int growthIncrement = 1; //how many squares the snake grows after it eats a kibble
	
	private int justAteMustGrowThisMuch = 0;

	private int maxX, maxY, squareSize;  
	private int snakeHeadX, snakeHeadY; //store coordinates of head - first segment

	public Snake(int maxX, int maxY, int squareSize){
		this.maxX = maxX;
		this.maxY = maxY;
		this.squareSize = squareSize;
		//Create and fill snakeSquares with 0s 
		snakeSquares = new int[maxX][maxY];
		fillSnakeSquaresWithZeros();
		createStartSnake();
//		fillBlockSquares();
	}

	protected void createStartSnake(){
		//snake starts as 3 horizontal squares in the center of the screen, moving left
		int screenXCenter = (int) maxX/2;  //Cast just in case we have an odd number
		int screenYCenter = (int) maxY/2;  //Cast just in case we have an odd number

		snakeSquares[screenXCenter][screenYCenter] = 1;
		snakeSquares[screenXCenter+1][screenYCenter] = 2;
		snakeSquares[screenXCenter+2][screenYCenter] = 3;

		snakeHeadX = screenXCenter;
		snakeHeadY = screenYCenter;

		snakeSize = 3;

		currentHeading = DIRECTION_LEFT;
		lastHeading = DIRECTION_LEFT;
		
		justAteMustGrowThisMuch = 0;
	}

	private void fillSnakeSquaresWithZeros() {
		for (int x = 0; x < this.maxX; x++){
			for (int y = 0 ; y < this.maxY ; y++) {
//				if(x == 0 && y == 0 || x == 0 && y == 9 
//				|| x == 9 && y == 0 || x == 9 && y == 9){
//				snakeSquares[x][y] = -1;
//				}else 
					snakeSquares[x][y] = 0;
			}
		}
	}

//	public void fillBlockSquares(){
//		for(int x: block.getBlockX()){
//			for(int y: block.getBlockY()){
//				blockSquares[x][y] = -1;
//			}
//		}
//	}
	public LinkedList<Point> segmentsToDraw(){
		//Return a list of the actual x and y coordinates of the top left of each snake segment
		//Useful for the Panel class to draw the snake
		LinkedList<Point> segmentCoordinates = new LinkedList<Point>();
		for (int segment = 1 ; segment <= snakeSize ; segment++ ) {
			//search array for each segment number
			for (int x = 0 ; x < maxX ; x++) {
				for (int y = 0 ; y < maxY ; y++) {
					if (snakeSquares[x][y] == segment){
						//make a Point for this segment's coordinates and add to list
						Point p = new Point(x * squareSize , y * squareSize);
						segmentCoordinates.add(p);
					}
				}
			}
		}
		return segmentCoordinates;

	}
	
	//Each directional method appears to control the snake to get it to move in one of four basic directions.
	//if statements block the snake from going back in itself or from making redundant movement.
	public void snakeUp(){
		if (currentHeading == DIRECTION_UP || currentHeading == DIRECTION_DOWN) { return; }
		currentHeading = DIRECTION_UP;
	}
	public void snakeDown(){
		if (currentHeading == DIRECTION_DOWN || currentHeading == DIRECTION_UP) { return; }
		currentHeading = DIRECTION_DOWN;
	}
	public void snakeLeft(){
		if (currentHeading == DIRECTION_LEFT || currentHeading == DIRECTION_RIGHT) { return; }
		currentHeading = DIRECTION_LEFT;
	}
	public void snakeRight(){
		if (currentHeading == DIRECTION_RIGHT || currentHeading == DIRECTION_LEFT) { return; }
		currentHeading = DIRECTION_RIGHT;
	}

//	public void	eatKibble(){
//		//record how much snake needs to grow after eating food
//		justAteMustGrowThisMuch += growthIncrement;
//	}

	protected void moveSnake() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		//Called every clock tick
		
		//Must check that the direction snake is being sent in is not contrary to current heading
		//So if current heading is down, and snake is being sent up, then should ignore.
		//Without this code, if the snake is heading up, and the user presses left then down quickly, the snake will back into itself.
		if (currentHeading == DIRECTION_DOWN && lastHeading == DIRECTION_UP) {
			currentHeading = DIRECTION_UP; //keep going the same way
		}
		else if (currentHeading == DIRECTION_UP && lastHeading == DIRECTION_DOWN) {
			currentHeading = DIRECTION_DOWN; //keep going the same way
		}
		else if (currentHeading == DIRECTION_LEFT && lastHeading == DIRECTION_RIGHT) {
			currentHeading = DIRECTION_RIGHT; //keep going the same way
		}
		else if (currentHeading == DIRECTION_RIGHT && lastHeading == DIRECTION_LEFT) {
			currentHeading = DIRECTION_LEFT; //keep going the same way
		}
		
		//Did you hit the wall, snake? 
		//Or eat your tail? Don't move. 

		//removed hitWall condition.
//		if (hitBlock == true || ateTail == true) {
//			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
//			return;
//		}
		
		//Use snakeSquares array, and current heading, to move snake

		//Put a 1 in new snake head square
		//increase all other snake segments by 1
		//set tail to 0 if snake did not just eat
		//Otherwise leave tail as is until snake has grown the correct amount 

		//Find the head of the snake - snakeHeadX and snakeHeadY

		//Increase all snake segments by 1
		//All non-zero elements of array represent a snake segment

		for (int x = 0 ; x < maxX ; x++) {
			for (int y = 0 ; y < maxY ; y++){
				if (snakeSquares[x][y] != 0) {
					snakeSquares[x][y]++;
				}
			}
		}

		//now identify where to add new snake head
		if (currentHeading == DIRECTION_UP) {		
			//Subtract 1 from Y coordinate so head is one square up
			snakeHeadY-- ;
		}
		else if (currentHeading == DIRECTION_DOWN) {		
			//Add 1 to Y coordinate so head is 1 square down
			snakeHeadY++ ;
		}
		else if (currentHeading == DIRECTION_LEFT) {		
			//Subtract 1 from X coordinate so head is 1 square to the left
			snakeHeadX -- ;
		}
		else if (currentHeading == DIRECTION_RIGHT) {		
			//Add 1 to X coordinate so head is 1 square to the right
			snakeHeadX ++ ;
		}

		//Does this make snake hit the wall?
		//original code:
//		if (snakeHeadX >= maxX || snakeHeadX < 0 || snakeHeadY >= maxY || snakeHeadY < 0 ) {
//			hitWall = true;	
//			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
//			return;
//		}
		
		//Mirrors the Snake for X and Y coordinates
		if (snakeHeadX >= maxX || snakeHeadX < 0){
			if(snakeHeadX >= maxX){
				snakeHeadX = 0;
			}else if(snakeHeadX < 0){
				snakeHeadX = maxX -1;
			}
		}
		if(snakeHeadY >= maxY || snakeHeadY < 0){ 
			if(snakeHeadY >= maxY){
				snakeHeadY = 0;
			}else if(snakeHeadY < 0){
				snakeHeadY = maxY -1;
			}
		}
		
		//Does this make the snake hit a block?
//		if(snakeHeadX == 0 && snakeHeadY == 0 || snakeHeadX == 9 && snakeHeadY == 9
//		|| snakeHeadX == 9 && snakeHeadY == 0 || snakeHeadX == 0 && snakeHeadY == 0){
//			hitBlock = true;
//			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
//			return;
//		}
		
		//Does this make the snake eat its tail?

		if (snakeSquares[snakeHeadX][snakeHeadY] != 0) {
			ateTail = true;
			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
			return;
		} else{
			//Otherwise, game is still on. Add new head
			snakeSquares[snakeHeadX][snakeHeadY] = 1; 
		}



		//If snake did not just eat, then remove tail segment
		//to keep snake the same length.
		//find highest number, which should now be the same as snakeSize+1, and set to 0
		
		if (justAteMustGrowThisMuch == 0) {
			for (int x = 0 ; x < maxX ; x++) {
				for (int y = 0 ; y < maxY ; y++){
					if (snakeSquares[x][y] == snakeSize+1) {
						snakeSquares[x][y] = 0;
					}
				}
			}
		}
		else {
			//Snake has just eaten. leave tail as is.  Decrease justAte... variable by 1.
			//Play audio file upon consumption of kibble.
//			File audioFile = new File("dragon_bit.wav");
//			 /////////////////////////////////////////////////////////////////////////////////////////////////////
//			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
//			AudioFormat format = audioStream.getFormat();
//			DataLine.Info info = new DataLine.Info(Clip.class, format);
//			Clip audioClip = (Clip) AudioSystem.getLine(info);
//			audioClip.open(audioStream);
//			audioClip.loop(0);
			justAteMustGrowThisMuch -- ;
			snakeSize ++;
		}
		lastHeading = currentHeading; //Update last confirmed heading
	}

//	protected boolean didHitBlock(){
//		return hitBlock;
//	}

	protected boolean didEatTail(){
		return ateTail;
	}

	public boolean isSnakeSegment(int kibbleX, int kibbleY) {
		if (snakeSquares[kibbleX][kibbleY] == 0) {
			return false;
		}
		return true;
	}


//	public boolean isBlock(int kibbleX, int kibbleY){
//		if(snakeSquares[kibbleX][kibbleY] == 0){
//			return false;
//		}
//		return true;
//	}
	
	public boolean didEatKibble(Kibble kibble) {
		//Is this kibble in the snake? It should be in the same square as the snake's head
		if (kibble.getKibbleX() == snakeHeadX && kibble.getKibbleY() == snakeHeadY){
			justAteMustGrowThisMuch += growthIncrement;
			return true;
		}
		return false;
	}

	public String toString(){
		String textsnake = "";
		//This looks the wrong way around. Actually need to do it this way or snake is drawn flipped 90 degrees. 
		for (int y = 0 ; y < maxY ; y++) {
			for (int x = 0 ; x < maxX ; x++){
				textsnake = textsnake + snakeSquares[x][y];
			}
			textsnake += "\n";
		}
		return textsnake;
	}

	public boolean wonGame() {

		//If all of the squares have snake segments in, the snake has eaten so much kibble 
		//that it has filled the screen. Win!
		for (int x = 0 ; x < maxX ; x++) {
			for (int y = 0 ; y < maxY ; y++){
				if (snakeSquares[x][y] == 4) {
					//there is still empty space on the screen, so haven't won
					return false;
				}
			}
		}
		//But if we get here, the snake has filled the screen. win!
		SnakeGame.setGameStage(SnakeGame.GAME_WON);
		return true;
	}

	public void reset() {
//		hitBlock = false;
		ateTail = false;
		fillSnakeSquaresWithZeros();
//		fillBlockSquares();
		createStartSnake();
	}
	
	//TODO hitWall no longer valid.
	public boolean isGameOver() {
		if (ateTail){
			SnakeGame.setGameStage(SnakeGame.GAME_OVER);
			return true;
		}
		return false;
	}
}


