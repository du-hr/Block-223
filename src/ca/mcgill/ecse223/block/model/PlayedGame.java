/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.block.model;
import java.io.Serializable;
import java.awt.geom.Rectangle2D;
import java.util.*;
import ca.mcgill.ecse223.block.model.BouncePoint.BounceDirection;

// line 11 "../../../../../Block223PlayMode.ump"
// line 114 "../../../../../Block223Persistence.ump"
// line 1 "../../../../../Block223States.ump"
public class PlayedGame implements Serializable
{

  //------------------------
  // STATIC VARIABLES
  //------------------------


  /**
   * at design time, the initial wait time may be adjusted as seen fit
   */
  public static final int INITIAL_WAIT_TIME = 20;
  private static int nextId = 1;
  public static final int NR_LIVES = 3;

  /**
   * the PlayedBall and PlayedPaddle are not in a separate class to avoid the bug in Umple that occurred for the second constructor of Game
   * no direct link to Ball, because the ball can be found by navigating to PlayedGame, Game, and then Ball
   */
  public static final int BALL_INITIAL_X = Game.PLAY_AREA_SIDE / 2;
  public static final int BALL_INITIAL_Y = Game.PLAY_AREA_SIDE / 2;

  /**
   * no direct link to Paddle, because the paddle can be found by navigating to PlayedGame, Game, and then Paddle
   * pixels moved when right arrow key is pressed
   */
  public static final int PADDLE_MOVE_RIGHT = 5;

  /**
   * pixels moved when left arrow key is pressed
   */
  public static final int PADDLE_MOVE_LEFT = -5;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //PlayedGame Attributes
  private int score;
  private int lives;
  private int currentLevel;
  private double waitTime;
  private String playername;
  private double ballDirectionX;
  private double ballDirectionY;
  private double currentBallX;
  private double currentBallY;
  private double currentPaddleLength;
  private double currentPaddleX;
  private double currentPaddleY;

  //Autounique Attributes
  private int id;

  //PlayedGame State Machines
  public enum PlayStatus { Ready, Moving, Paused, GameOver }
  private PlayStatus playStatus;

  //PlayedGame Associations
  private Player player;
  private Game game;
  private List<PlayedBlockAssignment> blocks;
  private transient BouncePoint bounce;
  private Block223 block223;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public PlayedGame(String aPlayername, Game aGame, Block223 aBlock223)
  {
    // line 43 "../../../../../Block223PlayMode.ump"
    boolean didAddGameResult = setGame(aGame);
       if (!didAddGameResult)
       {
          throw new RuntimeException("Unable to create playedGame due to game");
       }
    // END OF UMPLE BEFORE INJECTION
    score = 0;
    lives = NR_LIVES;
    currentLevel = 1;
    waitTime = INITIAL_WAIT_TIME;
    playername = aPlayername;
    resetBallDirectionX();
    resetBallDirectionY();
    resetCurrentBallX();
    resetCurrentBallY();
    currentPaddleLength = getGame().getPaddle().getMaxPaddleLength();
    resetCurrentPaddleX();
    currentPaddleY = Game.PLAY_AREA_SIDE - Paddle.VERTICAL_DISTANCE - Paddle.PADDLE_WIDTH;
    id = nextId++;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create playedGame due to game");
    }
    blocks = new ArrayList<PlayedBlockAssignment>();
    boolean didAddBlock223 = setBlock223(aBlock223);
    if (!didAddBlock223)
    {
      throw new RuntimeException("Unable to create playedGame due to block223");
    }
    setPlayStatus(PlayStatus.Ready);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setScore(int aScore)
  {
    boolean wasSet = false;
    score = aScore;
    wasSet = true;
    return wasSet;
  }

  public boolean setLives(int aLives)
  {
    boolean wasSet = false;
    lives = aLives;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentLevel(int aCurrentLevel)
  {
    boolean wasSet = false;
    currentLevel = aCurrentLevel;
    wasSet = true;
    return wasSet;
  }

  public boolean setWaitTime(double aWaitTime)
  {
    boolean wasSet = false;
    waitTime = aWaitTime;
    wasSet = true;
    return wasSet;
  }

  public boolean setPlayername(String aPlayername)
  {
    boolean wasSet = false;
    playername = aPlayername;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setBallDirectionX(double aBallDirectionX)
  {
    boolean wasSet = false;
    ballDirectionX = aBallDirectionX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetBallDirectionX()
  {
    boolean wasReset = false;
    ballDirectionX = getDefaultBallDirectionX();
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setBallDirectionY(double aBallDirectionY)
  {
    boolean wasSet = false;
    ballDirectionY = aBallDirectionY;
    wasSet = true;
    return wasSet;
  }

  public boolean resetBallDirectionY()
  {
    boolean wasReset = false;
    ballDirectionY = getDefaultBallDirectionY();
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentBallX(double aCurrentBallX)
  {
    boolean wasSet = false;
    currentBallX = aCurrentBallX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentBallX()
  {
    boolean wasReset = false;
    currentBallX = getDefaultCurrentBallX();
    wasReset = true;
    return wasReset;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentBallY(double aCurrentBallY)
  {
    boolean wasSet = false;
    currentBallY = aCurrentBallY;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentBallY()
  {
    boolean wasReset = false;
    currentBallY = getDefaultCurrentBallY();
    wasReset = true;
    return wasReset;
  }

  public boolean setCurrentPaddleLength(double aCurrentPaddleLength)
  {
    boolean wasSet = false;
    currentPaddleLength = aCurrentPaddleLength;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetDefaulted */
  public boolean setCurrentPaddleX(double aCurrentPaddleX)
  {
    boolean wasSet = false;
    currentPaddleX = aCurrentPaddleX;
    wasSet = true;
    return wasSet;
  }

  public boolean resetCurrentPaddleX()
  {
    boolean wasReset = false;
    currentPaddleX = getDefaultCurrentPaddleX();
    wasReset = true;
    return wasReset;
  }

  public int getScore()
  {
    return score;
  }

  public int getLives()
  {
    return lives;
  }

  public int getCurrentLevel()
  {
    return currentLevel;
  }

  public double getWaitTime()
  {
    return waitTime;
  }

  /**
   * added here so that it only needs to be determined once
   */
  public String getPlayername()
  {
    return playername;
  }

  /**
   * 0/0 is the top left corner of the play area, i.e., a directionX/Y of 0/1 moves the ball down in a straight line
   */
  public double getBallDirectionX()
  {
    return ballDirectionX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultBallDirectionX()
  {
    return getGame().getBall().getMinBallSpeedX();
  }

  public double getBallDirectionY()
  {
    return ballDirectionY;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultBallDirectionY()
  {
    return getGame().getBall().getMinBallSpeedY();
  }

  /**
   * the position of the ball is at the center of the ball
   */
  public double getCurrentBallX()
  {
    return currentBallX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentBallX()
  {
    return BALL_INITIAL_X;
  }

  public double getCurrentBallY()
  {
    return currentBallY;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentBallY()
  {
    return BALL_INITIAL_Y;
  }

  public double getCurrentPaddleLength()
  {
    return currentPaddleLength;
  }

  /**
   * the position of the paddle is at its top right corner
   */
  public double getCurrentPaddleX()
  {
    return currentPaddleX;
  }
  /* Code from template attribute_GetDefaulted */
  public double getDefaultCurrentPaddleX()
  {
    return (Game.PLAY_AREA_SIDE - currentPaddleLength) / 2;
  }

  public double getCurrentPaddleY()
  {
    return currentPaddleY;
  }

  public int getId()
  {
    return id;
  }

  public String getPlayStatusFullName()
  {
    String answer = playStatus.toString();
    return answer;
  }

  public PlayStatus getPlayStatus()
  {
    return playStatus;
  }

  public boolean play()
  {
    boolean wasEventProcessed = false;
    
    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Ready:
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      case Paused:
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean pause()
  {
    boolean wasEventProcessed = false;
    
    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Moving:
        setPlayStatus(PlayStatus.Paused);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean move()
  {
    boolean wasEventProcessed = false;
    
    PlayStatus aPlayStatus = playStatus;
    switch (aPlayStatus)
    {
      case Moving:
        if (hitPaddle())
        {
        // line 17 "../../../../../Block223States.ump"
          doHitPaddleOrWall();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        if (isOutOfBoundsAndLastLife())
        {
        // line 18 "../../../../../Block223States.ump"
          doOutOfBounds();
          setPlayStatus(PlayStatus.GameOver);
          wasEventProcessed = true;
          break;
        }
        if (isOutOfBounds())
        {
        // line 19 "../../../../../Block223States.ump"
          doOutOfBounds();
          setPlayStatus(PlayStatus.Paused);
          wasEventProcessed = true;
          break;
        }
        if (hitLastBlockAndLastLevel())
        {
        // line 20 "../../../../../Block223States.ump"
          doHitBlock();
          setPlayStatus(PlayStatus.GameOver);
          wasEventProcessed = true;
          break;
        }
        if (hitLastBlock())
        {
        // line 21 "../../../../../Block223States.ump"
          doHitBlockNextLevel();
          setPlayStatus(PlayStatus.Ready);
          wasEventProcessed = true;
          break;
        }
        if (hitBlock())
        {
        // line 22 "../../../../../Block223States.ump"
          doHitBlock();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        if (hitWall())
        {
        // line 23 "../../../../../Block223States.ump"
          doHitPaddleOrWall();
          setPlayStatus(PlayStatus.Moving);
          wasEventProcessed = true;
          break;
        }
        // line 24 "../../../../../Block223States.ump"
        doHitNothingAndNotOutOfBounds();
        setPlayStatus(PlayStatus.Moving);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void setPlayStatus(PlayStatus aPlayStatus)
  {
    playStatus = aPlayStatus;

    // entry actions and do activities
    switch(playStatus)
    {
      case Ready:
        // line 12 "../../../../../Block223States.ump"
        doSetup();
        break;
      case GameOver:
        // line 30 "../../../../../Block223States.ump"
        doGameOver();
        break;
    }
  }
  /* Code from template association_GetOne */
  public Player getPlayer()
  {
    return player;
  }

  public boolean hasPlayer()
  {
    boolean has = player != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetMany */
  public PlayedBlockAssignment getBlock(int index)
  {
    PlayedBlockAssignment aBlock = blocks.get(index);
    return aBlock;
  }

  public List<PlayedBlockAssignment> getBlocks()
  {
    List<PlayedBlockAssignment> newBlocks = Collections.unmodifiableList(blocks);
    return newBlocks;
  }

  public int numberOfBlocks()
  {
    int number = blocks.size();
    return number;
  }

  public boolean hasBlocks()
  {
    boolean has = blocks.size() > 0;
    return has;
  }

  public int indexOfBlock(PlayedBlockAssignment aBlock)
  {
    int index = blocks.indexOf(aBlock);
    return index;
  }
  /* Code from template association_GetOne */
  public BouncePoint getBounce()
  {
    return bounce;
  }

  public boolean hasBounce()
  {
    boolean has = bounce != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Block223 getBlock223()
  {
    return block223;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setPlayer(Player aPlayer)
  {
    boolean wasSet = false;
    Player existingPlayer = player;
    player = aPlayer;
    if (existingPlayer != null && !existingPlayer.equals(aPlayer))
    {
      existingPlayer.removePlayedGame(this);
    }
    if (aPlayer != null)
    {
      aPlayer.addPlayedGame(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    if (aGame == null)
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      existingGame.removePlayedGame(this);
    }
    game.addPlayedGame(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfBlocks()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public PlayedBlockAssignment addBlock(int aX, int aY, Block aBlock)
  {
    return new PlayedBlockAssignment(aX, aY, aBlock, this);
  }

  public boolean addBlock(PlayedBlockAssignment aBlock)
  {
    boolean wasAdded = false;
    if (blocks.contains(aBlock)) { return false; }
    PlayedGame existingPlayedGame = aBlock.getPlayedGame();
    boolean isNewPlayedGame = existingPlayedGame != null && !this.equals(existingPlayedGame);
    if (isNewPlayedGame)
    {
      aBlock.setPlayedGame(this);
    }
    else
    {
      blocks.add(aBlock);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeBlock(PlayedBlockAssignment aBlock)
  {
    boolean wasRemoved = false;
    //Unable to remove aBlock, as it must always have a playedGame
    if (!this.equals(aBlock.getPlayedGame()))
    {
      blocks.remove(aBlock);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addBlockAt(PlayedBlockAssignment aBlock, int index)
  {  
    boolean wasAdded = false;
    if(addBlock(aBlock))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBlocks()) { index = numberOfBlocks() - 1; }
      blocks.remove(aBlock);
      blocks.add(index, aBlock);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveBlockAt(PlayedBlockAssignment aBlock, int index)
  {
    boolean wasAdded = false;
    if(blocks.contains(aBlock))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfBlocks()) { index = numberOfBlocks() - 1; }
      blocks.remove(aBlock);
      blocks.add(index, aBlock);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addBlockAt(aBlock, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetUnidirectionalOptionalOne */
  public boolean setBounce(BouncePoint aNewBounce)
  {
    boolean wasSet = false;
    bounce = aNewBounce;
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setBlock223(Block223 aBlock223)
  {
    boolean wasSet = false;
    if (aBlock223 == null)
    {
      return wasSet;
    }

    Block223 existingBlock223 = block223;
    block223 = aBlock223;
    if (existingBlock223 != null && !existingBlock223.equals(aBlock223))
    {
      existingBlock223.removePlayedGame(this);
    }
    block223.addPlayedGame(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (player != null)
    {
      Player placeholderPlayer = player;
      this.player = null;
      placeholderPlayer.removePlayedGame(this);
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePlayedGame(this);
    }
    while (blocks.size() > 0)
    {
      PlayedBlockAssignment aBlock = blocks.get(blocks.size() - 1);
      aBlock.delete();
      blocks.remove(aBlock);
    }
    
    bounce = null;
    Block223 placeholderBlock223 = block223;
    this.block223 = null;
    if(placeholderBlock223 != null)
    {
      placeholderBlock223.removePlayedGame(this);
    }
  }

  // line 120 "../../../../../Block223Persistence.ump"
   public static  void reinitializeAutouniqueID(List<PlayedGame> playedGames){
    nextId = 0;
    for(PlayedGame g: playedGames) {
      if (g.getId() > nextId) {
        nextId = g.getId();
      }
    }
    nextId++;
  }


  /**
   * Guards
   */
  // line 37 "../../../../../Block223States.ump"
   private boolean hitPaddle(){
    BouncePoint bp = calculateBouncePointPaddle();
    if (bp != null) {
    	if(bp.getX() == currentBallX+ ballDirectionX && bp.getY() == currentBallY+ ballDirectionY){
    		setBounce(null);
    		return false;
    	}
    	else {
    		setBounce(bp);
    		return true;
    	
    	}				
	}
    return false;
  }

  // line 53 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointPaddle(){
    ArrayList<BouncePoint> bounceList = new ArrayList<>();
	   double x1 = getCurrentBallX(), y1 = getCurrentBallY();
	   double directionX = getBallDirectionX(), directionY = getBallDirectionY();
	   double x2 = x1 + directionX , y2 = y1 + directionY;
       double ballRadius = Ball.BALL_DIAMETER/2;
	   
	   Rectangle2D paddleBox = new Rectangle2D.Double(getCurrentPaddleX() - ballRadius, getCurrentPaddleY() - ballRadius,
		       getCurrentPaddleLength() + 2 * ballRadius, Paddle.PADDLE_WIDTH + ballRadius);
	   
	   if(paddleBox.intersectsLine(x1,y1,x2,y2)) {
		   if(calculateBouncePointPaddleTop(x1,y1,x2,y2)!=null) {
			   bounceList.add(calculateBouncePointPaddleTop(x1,y1,x2,y2));
		   }
		   if(calculateBouncePointPaddleLeft(x1,y1,x2,y2)!=null) {
			   bounceList.add(calculateBouncePointPaddleLeft(x1,y1,x2,y2));
		   }
		   if(calculateBouncePointPaddleRight(x1,y1,x2,y2)!=null) {
			   bounceList.add(calculateBouncePointPaddleRight(x1,y1,x2,y2));
		   }
		   if(calculateBouncePointPaddleTopRight(x1,y1,x2,y2)!=null) {
			   bounceList.add(calculateBouncePointPaddleTopRight(x1,y1,x2,y2));
		   }
		   if(calculateBouncePointPaddleTopLeft(x1,y1,x2,y2)!=null) {
			   bounceList.add(calculateBouncePointPaddleTopLeft(x1,y1,x2,y2));
		   }
		   		  
	   }
	   BouncePoint bp = findClosestBouncePoint(bounceList);
	   setBounce(null);
    return bp;
  }

  // line 85 "../../../../../Block223States.ump"
   private BouncePoint findClosestBouncePoint(ArrayList<BouncePoint> bounceList){
    setBounce(null);
	   if(bounceList.size()==0) {
		   return null;
	   }
	   else if(bounceList.size()==1){
		   return bounceList.remove(0);
	   }
	   else {
		   BouncePoint bp = bounceList.remove(0);
		   for(BouncePoint cur: bounceList) {
			   if(cur!=null && (getDistanceBetweenTwoPoints(getCurrentBallX(),getCurrentBallY(),cur.getX(),cur.getY()) <
					   getDistanceBetweenTwoPoints(getCurrentBallX(),getCurrentBallY(),bp.getX(),bp.getY()))) {
				   bp = cur;
			   }
		   }
		   
		   return bp;
	   }
  }

  // line 105 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointPaddleTop(double x1, double y1, double x2, double y2){
    double bpX , bpY;
	   double ballRadius = Ball.BALL_DIAMETER/2;
      Rectangle2D A = new Rectangle2D.Double(getCurrentPaddleX(), getCurrentPaddleY() - ballRadius, getCurrentPaddleLength(), ballRadius);
      setBounce(null);
      if(A.intersectsLine(x1, y1, x2, y2)) {
    	  bpY = Game.PLAY_AREA_SIDE -(Paddle.VERTICAL_DISTANCE + Paddle.PADDLE_WIDTH + ballRadius);
    	  if(x2-x1 == 0) {
    		  bpX = x1;
    	  }
    	  else {
    		  double m = (y2 - y1) / (x2 - x1);
    		  bpX = (bpY - y1)/m + x1;
    		  
    	  }
          return new BouncePoint(bpX, bpY, BounceDirection.FLIP_Y);
      }
      return null;
  }

  // line 125 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointPaddleLeft(double x1, double y1, double x2, double y2){
    double bpX , bpY;
	  double ballRadius = Ball.BALL_DIAMETER/2;
	  Rectangle2D B = new Rectangle2D.Double(getCurrentPaddleX()- ballRadius, getCurrentPaddleY(), ballRadius, ballRadius);
      setBounce(null);
      if(B.intersectsLine(x1, y1, x2, y2)) {
   	      bpX = getCurrentPaddleX() - ballRadius;
   	      if(y2-y1==0) {
   	    	  bpY = y1;
   	      }
   	      else {
   	    	  double m = (y2-y1)/(x2-x1);
   	    	  bpY = m * (bpX - x1) + y1;
   	      }
          return new BouncePoint(bpX, bpY, BounceDirection.FLIP_X);
      }
      return null;
  }

  // line 144 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointPaddleRight(double x1, double y1, double x2, double y2){
    double bpX , bpY;
	   double ballRadius = Ball.BALL_DIAMETER/2;
	   Rectangle2D C = new Rectangle2D.Double(getCurrentPaddleX() + getCurrentPaddleLength(), getCurrentPaddleY(), ballRadius, ballRadius);
       setBounce(null);
      if(C.intersectsLine(x1, y1, x2, y2)) {
   	      bpX = getCurrentPaddleX() +getCurrentPaddleLength() + ballRadius;
   	      if(y2-y1==0) {
   	    	  bpY = y1;
   	      }
   	      else {
   	    	  double m = (y2-y1)/(x2-x1);
   	    	  bpY = m * (bpX - x1) + y1;
   	      }
          return new BouncePoint(bpX, bpY, BounceDirection.FLIP_X);
      }
      return null;
  }

  // line 163 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointPaddleTopRight(double x1, double y1, double x2, double y2){
    setBounce(null);
	   double ballRadius = Ball.BALL_DIAMETER/2;
	   double x3 = getCurrentPaddleX() + getCurrentPaddleLength() + ballRadius, y3 = getCurrentPaddleY() , x4 = getCurrentPaddleX() + getCurrentPaddleLength() , y4 = getCurrentPaddleY() - ballRadius;
	   double bpX = getLineQuarterCircleSegmentsIntersctionX(x1,y1,x2,y2,x3,y3,x4,y4) , bpY = getLineQuarterCircleSegmentsIntersctionY(x1,y1,x2,y2,x3,y3,x4,y4);
	   if(isOnQuarterCircleSegment(bpX,bpY,x3,y3,x4,y4)) {
		   if(getBallDirectionX()>=0) {
			   return new BouncePoint(bpX,bpY,BounceDirection.FLIP_Y);
		   }
		   else {
			   return new BouncePoint(bpX,bpY,BounceDirection.FLIP_X);

		   }
	   }
	   return null;
  }

  // line 179 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointPaddleTopLeft(double x1, double y1, double x2, double y2){
    setBounce(null);
	   double ballRadius = Ball.BALL_DIAMETER/2;
	   double x3 = getCurrentPaddleX() - ballRadius, y3 = getCurrentPaddleY() , x4 = getCurrentPaddleX() , y4 = getCurrentPaddleY() - ballRadius;
	   double bpX = getLineQuarterCircleSegmentsIntersctionX(x1,y1,x2,y2,x3,y3,x4,y4) , bpY = getLineQuarterCircleSegmentsIntersctionY(x1,y1,x2,y2,x3,y3,x4,y4);
	   if(isOnQuarterCircleSegment(bpX,bpY,x3,y3,x4,y4)) {
		   if(getBallDirectionX()>=0) {
			   return new BouncePoint(bpX,bpY,BounceDirection.FLIP_X);
		   }
		   else {
			   return new BouncePoint(bpX,bpY,BounceDirection.FLIP_Y);

		   }
	   }
	   return null;
  }

  // line 195 "../../../../../Block223States.ump"
   private double getDistanceBetweenTwoPoints(double x1, double y1, double x2, double y2){
    return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1,2));
  }

  // line 198 "../../../../../Block223States.ump"
   private boolean isOutOfBoundsAndLastLife(){
    boolean outOfBounds = false;
    if (this.lives == 1) {
      outOfBounds = isBallOutOfBounds();
    }
    return outOfBounds;
  }

  // line 206 "../../../../../Block223States.ump"
   private boolean isOutOfBounds(){
    return isBallOutOfBounds();
  }

  // line 210 "../../../../../Block223States.ump"
   private boolean isBallOutOfBounds(){
    int radiusOfBall = Ball.BALL_DIAMETER / 2;
    //checks if ball touches Zone D (bottom of the playing area)
    return (currentBallY + radiusOfBall) >= (Game.PLAY_AREA_SIDE - radiusOfBall);
  }

  // line 216 "../../../../../Block223States.ump"
   private boolean hitLastBlockAndLastLevel(){
    game = getGame();
    int nrLevels = game.numberOfLevels();
    setBounce(null);
    if (nrLevels == currentLevel) {
    	int nrBlocks = numberOfBlocks();
    	if (nrBlocks == 1) {
    		PlayedBlockAssignment block = getBlock(0);
    		BouncePoint bp = calculateBouncePointBlock(block);
    		if (bp != null) { // added null bp check from sample solution v.4
    			if(bp.getX() == currentBallX+ ballDirectionX && bp.getY() == currentBallY+ ballDirectionY)
    				return false;
    			else {
        			setBounce(bp);
        			return true;				
    			}
    		}
    	}
    }
    return false;
  }


  /**
   * returns null if no bounce points found
   */
  // line 239 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointBlock(PlayedBlockAssignment block){
    double blockSize = Block.SIZE; //20
	  double ballRadius = Ball.BALL_DIAMETER/2; //5
	  BouncePoint bouncePoint = null;
	  BouncePoint A = new BouncePoint(0,0,null);
	  BouncePoint B = new BouncePoint(0,0,null);
	  BouncePoint C = new BouncePoint(0,0,null);
	  BouncePoint D = new BouncePoint(0,0,null);
	  BouncePoint E = new BouncePoint(0,0,null);
	  BouncePoint F = new BouncePoint(0,0,null);
	  BouncePoint G = new BouncePoint(0,0,null);
	  BouncePoint H = new BouncePoint(0,0,null);
	  currentBallX = getCurrentBallX();
	  currentBallY = getCurrentBallY();
	  ballDirectionX = getBallDirectionX();
	  ballDirectionY = getBallDirectionY();
	  double blockX = block.getX();
	  double blockY = block.getY();
	  Rectangle2D rect = new Rectangle2D.Double(blockX-ballRadius, blockY-ballRadius, blockSize+2*ballRadius, blockSize+2*ballRadius);
	  if (rect.intersectsLine(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY) == false) {
		  return null;
	  }
	  else {
		  ArrayList<BouncePoint> bps = new ArrayList<BouncePoint>(8);
		  A.setX(getLineSegmentsIntersctionX(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX, blockY-ballRadius, blockX+blockSize, blockY-ballRadius));
		  A.setY(getLineSegmentsIntersctionY(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX, blockY-ballRadius, blockX+blockSize, blockY-ballRadius));
		  A.setDirection(getLineSegmentsIntersctionBounceDirection(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX, blockY-ballRadius, blockX+blockSize, blockY-ballRadius));
		  bps.add(A);
		  B.setX(getLineSegmentsIntersctionX(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX-ballRadius, blockY, blockX-ballRadius, blockY+blockSize));
		  B.setY(getLineSegmentsIntersctionY(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX-ballRadius, blockY, blockX-ballRadius, blockY+blockSize));
		  B.setDirection(getLineSegmentsIntersctionBounceDirection(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX-ballRadius, blockY, blockX-ballRadius, blockY+blockSize));
		  bps.add(B);
		  C.setX(getLineSegmentsIntersctionX(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX+blockSize+ballRadius, blockY, blockX+blockSize+ballRadius, blockY+blockSize));
		  C.setY(getLineSegmentsIntersctionY(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX+blockSize+ballRadius, blockY, blockX+blockSize+ballRadius, blockY+blockSize));
		  C.setDirection(getLineSegmentsIntersctionBounceDirection(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX+blockSize+ballRadius, blockY, blockX+blockSize+ballRadius, blockY+blockSize));
		  bps.add(C);
		  D.setX(getLineSegmentsIntersctionX(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX, blockY+blockSize+ballRadius, blockX+blockSize, blockY+blockSize+ballRadius));
		  D.setY(getLineSegmentsIntersctionY(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX, blockY+blockSize+ballRadius, blockX+blockSize, blockY+blockSize+ballRadius));
		  D.setDirection(getLineSegmentsIntersctionBounceDirection(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX, blockY+blockSize+ballRadius, blockX+blockSize, blockY+blockSize+ballRadius));
		  bps.add(D);
		  E.setX(getLineQuarterCircleSegmentsIntersctionX(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX-ballRadius, blockY, blockX, blockY-ballRadius));
		  E.setY(getLineQuarterCircleSegmentsIntersctionY(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX-ballRadius, blockY, blockX, blockY-ballRadius));
		  E.setDirection(getLineQuarterCircleSegmentsIntersctionBlockLeftSideBounceDirection(currentBallX, currentBallY, currentBallX + ballDirectionX, currentBallY + ballDirectionY, blockX - ballRadius, blockY, blockX, blockY - ballRadius));
		  bps.add(E);
		  F.setX(getLineQuarterCircleSegmentsIntersctionX(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX+blockSize+ballRadius, blockY, blockX+blockSize, blockY-ballRadius));
		  F.setY(getLineQuarterCircleSegmentsIntersctionY(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX+blockSize+ballRadius, blockY, blockX+blockSize, blockY-ballRadius));
		  F.setDirection(getLineQuarterCircleSegmentsIntersctionBlockRightSideBounceDirection(currentBallX, currentBallY, currentBallX + ballDirectionX, currentBallY + ballDirectionY, blockX + blockSize + ballRadius, blockY, blockX + blockSize, blockY - ballRadius));
		  bps.add(F);
		  G.setX(getLineQuarterCircleSegmentsIntersctionX(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX-ballRadius, blockY+blockSize, blockX, blockY+blockSize+ballRadius));
		  G.setY(getLineQuarterCircleSegmentsIntersctionY(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX-ballRadius, blockY+blockSize, blockX, blockY+blockSize+ballRadius));
		  G.setDirection(getLineQuarterCircleSegmentsIntersctionBlockLeftSideBounceDirection(currentBallX, currentBallY, currentBallX + ballDirectionX, currentBallY + ballDirectionY, blockX - ballRadius, blockY + blockSize, blockX, blockY + blockSize + ballRadius));
		  bps.add(G);
		  H.setX(getLineQuarterCircleSegmentsIntersctionX(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX+blockSize+ballRadius, blockY+blockSize, blockX+blockSize, blockY+blockSize+ballRadius));
		  H.setY(getLineQuarterCircleSegmentsIntersctionY(currentBallX, currentBallY, currentBallX+ballDirectionX, currentBallY+ballDirectionY, blockX+blockSize+ballRadius, blockY+blockSize, blockX+blockSize, blockY+blockSize+ballRadius));
		  H.setDirection(getLineQuarterCircleSegmentsIntersctionBlockRightSideBounceDirection(currentBallX, currentBallY, currentBallX + ballDirectionX, currentBallY + ballDirectionY, blockX + blockSize + ballRadius, blockY + blockSize, blockX + blockSize, blockY + blockSize + ballRadius));
		  bps.add(H);
		  ArrayList<BouncePoint> possibleBouncePointsOnOneBlock = new ArrayList<BouncePoint>();
		  for (int i = 0; i < 8; i++) {
			//(0,0) is impossible to be the bounce point, thus use (0,0) for null bounce point
		    //If x (or y) = 0, then the bounce point is null. Discard. 
			  if (bps.get(i).getX() != 0 || bps.get(i).getY() != 0){
				  possibleBouncePointsOnOneBlock.add(bps.get(i));
			  }
		  }
		  if (possibleBouncePointsOnOneBlock.isEmpty()) {
			  return null;
		  }
		  else if (possibleBouncePointsOnOneBlock.size() == 1) {
			  bouncePoint = possibleBouncePointsOnOneBlock.get(0);
		  }
		  else if (isCloser(possibleBouncePointsOnOneBlock.get(0),possibleBouncePointsOnOneBlock.get(1)) == true) {
			  bouncePoint = possibleBouncePointsOnOneBlock.get(0);
		  }
		  else {
			  bouncePoint = possibleBouncePointsOnOneBlock.get(1);
		  }		  
	  }
	  bouncePoint.setHitBlock(block);
	return bouncePoint;
  }


  /**
   * If the bounce point is null (x=0,y=0), it will only be assigned a null direction.
   * For quarter circle F and H in sample solution v.4
   */
  // line 323 "../../../../../Block223States.ump"
   private BounceDirection getLineQuarterCircleSegmentsIntersctionBlockRightSideBounceDirection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    BounceDirection bounceDirection = null;
	ballDirectionX = getBallDirectionX();
	if (getLineQuarterCircleSegmentsIntersctionY(x1,y1,x2,y3,x3,y3,x4,y4)==0 && getLineQuarterCircleSegmentsIntersctionX(x1,y1,x2,y3,x3,y3,x4,y4)==0) {
		return null;
	}
	else if (ballDirectionX > 0 ) {
		bounceDirection = BounceDirection.FLIP_Y;
	}
	else if (ballDirectionX < 0) {
		bounceDirection = BounceDirection.FLIP_X;	
	}
	return bounceDirection;
  }


  /**
   * If the bounce point is null (x=0,y=0), it will only be assigned a null direction.
   * For quarter circle F and H in sample solution v.4
   */
  // line 341 "../../../../../Block223States.ump"
   private BounceDirection getLineQuarterCircleSegmentsIntersctionBlockLeftSideBounceDirection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    BounceDirection bounceDirection = null;
	ballDirectionX = getBallDirectionX();
	if (getLineQuarterCircleSegmentsIntersctionY(x1,y1,x2,y3,x3,y3,x4,y4)==0 && getLineQuarterCircleSegmentsIntersctionX(x1,y1,x2,y3,x3,y3,x4,y4)==0) {
		return null;
	}
	else if (ballDirectionX > 0 ) {
		bounceDirection = BounceDirection.FLIP_X;
	}
	else if (ballDirectionX < 0) {
		bounceDirection = BounceDirection.FLIP_Y;	
	}
	return bounceDirection;
  }


  /**
   * If there is no intersection, return 0.
   */
  // line 358 "../../../../../Block223States.ump"
   private double getLineQuarterCircleSegmentsIntersctionY(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    double lineQuarterCircleSegmentsIntersctionY = 0;
	double lineCircleIntersectionX = getLineCircleIntersectionX(x1, y1, x2, y2, x3, y3, x4, y4);
	double lineCircleIntersectionY = getLineCircleIntersectionY(x1, y1, x2, y2, x3, y3, x4, y4);
	if (isOnLineSegment(lineCircleIntersectionX, lineCircleIntersectionY, x1, y1, x2, y2) == false
			|| isOnQuarterCircleSegment(lineCircleIntersectionX, lineCircleIntersectionY, x3, y3, x4,
					y4) == false) {
		return 0;
	} else {
		lineQuarterCircleSegmentsIntersctionY = lineCircleIntersectionY;
	}
	return lineQuarterCircleSegmentsIntersctionY;
  }


  /**
   * If there is no intersection, return 0.
   */
  // line 374 "../../../../../Block223States.ump"
   private double getLineQuarterCircleSegmentsIntersctionX(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    double lineQuarterCircleSegmentsIntersctionX = 0;
	double lineCircleIntersectionX = getLineCircleIntersectionX(x1, y1, x2, y2, x3, y3, x4, y4);
	double lineCircleIntersectionY = getLineCircleIntersectionY(x1, y1, x2, y2, x3, y3, x4, y4);
	if (isOnLineSegment(lineCircleIntersectionX, lineCircleIntersectionY, x1, y1, x2, y2) == false
			|| isOnQuarterCircleSegment(lineCircleIntersectionX, lineCircleIntersectionY, x3, y3, x4,
					y4) == false) {
		return 0;
	} else {
		lineQuarterCircleSegmentsIntersctionX = lineCircleIntersectionX;
	}
	return lineQuarterCircleSegmentsIntersctionX;
  }


  /**
   * Method to determine whether the intersection point is on the quarter circle 
   * E,F,G,or H without using multiple methods: 
   * If the point is on the respective circle segment, then the line segment from center
   * of circle (x4,y3) to (pX,pY) must have exactly one intersection with the line segment
   * from (x3,y3) to (x4,y4).
   * Discard the cases of (pX,pY) duplicates with (x3,y3) or (x4,y4).
   */
  // line 397 "../../../../../Block223States.ump"
   private boolean isOnQuarterCircleSegment(double pX, double pY, double x3, double y3, double x4, double y4){
    boolean isOnQuarterCircleSegment = false;
	if (pX == x3 || pX == x4 || pY == y3 || pY == y4) {
		return false;
	}
	else {
		double point_x = getLineSegmentsIntersctionX(x4,y3,pX,pY,x3,y3,x4,y4);
		double point_y = getLineSegmentsIntersctionY(x4,y3,pX,pY,x3,y3,x4,y4);
		if(point_x*point_y == 0) {
			return false;
		}
		else {
			isOnQuarterCircleSegment = true;
		}
	}
	return isOnQuarterCircleSegment;
  }


  /**
   * center of circle : (x4,y3)
   * If there is no intersection, return 0.
   */
  // line 418 "../../../../../Block223States.ump"
   private double getLineCircleIntersectionY(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    double ballRadius = Ball.BALL_DIAMETER/2; //5
	double lineCircleIntersectionY = 0;
	// case1: vertical line parallel is tangent with the circle. Discard
	if (x1 == x2 && x1==x4 && x2==x4) {
		return 0;
	}
	// case2: horizontal line is tangent with the circle. Discard.
	else if (y1==y2&&y1==y3&&y2==y3) {
		return 0;
	}
	// case3: line and circle are away (return 0). Discard.
	else if (getDistanceFromPointToLine(x4,y3,x1,y1,x2,y2) > ballRadius) {
		return 0;
	}
	//case4: horizontal line intersects with circle. The intersection that is closest to (x1,y1) is valid
	else if (y1==y2) {
		lineCircleIntersectionY = y1;
	}
	//case5: vertical line intersects with circle. The intersection that is closest to (x1,y1) is valid
	else if (x1==x2) {
		double ponitY_1 = y3 - Math.sqrt(Math.pow(ballRadius,2)-Math.pow((x4-x1),2));
		double ponitY_2 = y3 + Math.sqrt(Math.pow(ballRadius,2)-Math.pow((x4-x1),2));
		if (Math.abs(y1-ponitY_1)<Math.abs(y1-ponitY_2)) {
			lineCircleIntersectionY = ponitY_1;
		}
		else {
			lineCircleIntersectionY = ponitY_2;
		}
	}
	// case6: line (non-horizontal and non-vertical) and circle are tangent
	/*
	 * The equation of the line is y= mx+c
	 * The circle: (x-p)^2 + (y-q)^2 = r^2
	 * Intersection Equation: (m^2+1)x^2+2(mc-mq-p)x+(q^2-r^2+p^2-2cq+c^2)=0
	 */
	else if (getDistanceFromPointToLine(x4,y3,x1,y1,x2,y2) == ballRadius) {
		double m = (y2-y1)/(x2-x1);
		double c = y2 - m*x2;
		double A = Math.pow(m,2)+1;
		double B = 2 * (m*c-m*y3-x4);
		//double C = Math.pow(y3,2)-Math.pow(5,2)+Math.pow(x4,2)-2*c*y3+Math.pow(c,2);
		lineCircleIntersectionY = m*(-B/(2*A))+c; //B^2-4AC = 0
	}
	// case7: line (non-horizontal and non-vertical) and circle intersects. 
	//The intersection that is closest to (x1,y1) is valid.
	else if (getDistanceFromPointToLine(x4,y3,x1,y1,x2,y2) < ballRadius) {
		double m = (y2-y1)/(x2-x1);
		double c = y2 - m*x2;
		double A = Math.pow(m,2)+1;
		double B = 2 * (m*c-m*y3-x4);
		double C = Math.pow(y3,2)-Math.pow(ballRadius,2)+Math.pow(x4,2)-2*c*y3+Math.pow(c,2);
		double point_1_X = (-B + Math.sqrt(Math.pow(B,2)-4*A*C))/(2*A);
		double point_1_Y = m*point_1_X + c;
		double point_2_X = (-B - Math.sqrt(Math.pow(B,2)-4*A*C))/(2*A);
		double point_2_Y = m*point_2_X + c;
		double d1 = Math.sqrt(Math.pow((x1-point_1_X),2)+Math.pow((y1-point_1_Y),2));
		double d2 = Math.sqrt(Math.pow((x1-point_2_X),2)+Math.pow((y1-point_2_Y),2));
		if (d1<d2) {
			lineCircleIntersectionY = point_1_Y;
		}
		else {
			lineCircleIntersectionY = point_2_Y;
		}
	}
	return lineCircleIntersectionY;
  }


  /**
   * center of circle : (x4,y3)
   * If there is no intersection, return 0.
   */
  // line 489 "../../../../../Block223States.ump"
   private double getLineCircleIntersectionX(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    double ballRadius = Ball.BALL_DIAMETER/2; //5
	double lineCircleIntersectionX = 0;
	// case1: vertical line parallel is tangent with the circle. Discard
	if (x1 == x2 && x1==x4 && x2==x4) {
		return 0;
	}
	// case2: horizontal line is tangent with the circle. Discard.
	else if (y1==y2&&y1==y3&&y2==y3) {
		return 0;
	}
	// case3: line and circle are away (return 0). Discard.
	else if (getDistanceFromPointToLine(x4,y3,x1,y1,x2,y2) > ballRadius) {
		return 0;
	}
	//case4: horizontal line intersects with circle. The intersection that is closest to (x1,y1) is valid
	else if (y1==y2) {
		double ponitX_1 = x4 - Math.sqrt(Math.pow(ballRadius,2)-Math.pow((y3-y1),2));
		double ponitX_2 = x4 + Math.sqrt(Math.pow(ballRadius,2)-Math.pow((y3-y1),2));
		if (Math.abs(x1-ponitX_1)<Math.abs(x1-ponitX_2)) {
			lineCircleIntersectionX = ponitX_1;
		}
		else {
			lineCircleIntersectionX = ponitX_2;
		}
	}
	//case5: vertical line intersects with circle. The intersection that is closest to (x1,y1) is valid
	else if (x1==x2) {
		lineCircleIntersectionX = x1;

	}
	// case6: line (non-horizontal and non-vertical) and circle are tangent
	/*
	 * The equation of the line is y= mx+c
	 * The circle: (x-p)^2 + (y-q)^2 = r^2
	 * Intersection Equation: (m^2+1)x^2+2(mc-mq-p)x+(q^2-r^2+p^2-2cq+c^2)=0
	 */
	else if (getDistanceFromPointToLine(x4,y3,x1,y1,x2,y2) == ballRadius) {
		double m = (y2-y1)/(x2-x1);
		double c = y2 - m*x2;
		double A = Math.pow(m,2)+1;
		double B = 2 * (m*c-m*y3-x4);
		//double C = Math.pow(y3,2)-Math.pow(5,2)+Math.pow(x4,2)-2*c*y3+Math.pow(c,2);
		lineCircleIntersectionX = -B/(2*A); //B^2-4AC = 0
	}
	// case7: line (non-horizontal and non-vertical) and circle intersects. 
	//The intersection that is closest to (x1,y1) is valid.
	else if (getDistanceFromPointToLine(x4,y3,x1,y1,x2,y2) < ballRadius) {
		double m = (y2-y1)/(x2-x1);
		double c = y2 - m*x2;
		double A = Math.pow(m,2)+1;
		double B = 2 * (m*c-m*y3-x4);
		double C = Math.pow(y3,2)-Math.pow(ballRadius,2)+Math.pow(x4,2)-2*c*y3+Math.pow(c,2);
		double point_1_X = (-B + Math.sqrt(Math.pow(B,2)-4*A*C))/(2*A);
		double point_1_Y = m*point_1_X + c;
		double point_2_X = (-B - Math.sqrt(Math.pow(B,2)-4*A*C))/(2*A);
		double point_2_Y = m*point_2_X + c;
		double d1 = Math.sqrt(Math.pow((x1-point_1_X),2)+Math.pow((y1-point_1_Y),2));
		double d2 = Math.sqrt(Math.pow((x1-point_2_X),2)+Math.pow((y1-point_2_Y),2));
		if (d1<d2) {
			lineCircleIntersectionX = point_1_X;
		}
		else {
			lineCircleIntersectionX = point_2_X;
		}
	}
	return lineCircleIntersectionX;
  }


  /**
   * The equation of the line ax + by + c = 0.
   * The coordinate of the point is (x1, y1)
   * The formula for distance between the point and the line in 2-D is given by:
   * Distance = (| a*x1 + b*y1 + c |) / (sqrt( a*a + b*b))
   */
  // line 563 "../../../../../Block223States.ump"
   private double getDistanceFromPointToLine(double x4, double y3, double x1, double y1, double x2, double y2){
    // line: ax+by+c=0
	double a = y2-y1;
	double b = x1-x2;
	double c = x2*y1-x1*y2;
	double distance = Math.abs(((a * x4 + b * y3 + c)) /  (Math.sqrt(a * a + b * b))); 
	return distance;
  }


  /**
   * If the bounce point is null (x=0,y=0), it will only be assigned a null direction.
   */
  // line 574 "../../../../../Block223States.ump"
   private BounceDirection getLineSegmentsIntersctionBounceDirection(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    BounceDirection bounceDirection = null;
	if (getLineSegmentsIntersctionY(x1,y1,x2,y2,x3,y3,x4,y4)==0 && getLineSegmentsIntersctionX(x1,y1,x2,y2,x3,y3,x4,y4)==0) {
		return null;
	}
	// for line A and line D in sample solution v.4
	else if (y3 == y4) {
		bounceDirection = BounceDirection.FLIP_Y;
	}
	// for line B and line C in sample solution v.4
	else if (x3 == x4) {
		bounceDirection = BounceDirection.FLIP_X;	
	}
	return bounceDirection;
  }


  /**
   * If there is no intersection, return 0.
   */
  // line 593 "../../../../../Block223States.ump"
   private double getLineSegmentsIntersctionY(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    double lineSegmentsIntersctionY = 0;
		boolean end_1 = isOnLineSegment(x1,y1,x3,y3,x4,y4);
		boolean end_2 = isOnLineSegment(x2,y2,x3,y3,x4,y4);
		boolean end_3 = isOnLineSegment(x3,y3,x1,y1,x2,y2);
		boolean end_4 = isOnLineSegment(x4,y4,x1,y1,x2,y2);
		if (end_1) {
			return y1;
		}
		if (end_2) {
			return y2;
		}
		if (end_3) {
			return y3;
		}
		if (end_4) {
			return y4;
		}
		if (isparallel(x1, y1, x2, y2, x3, y3, x4, y4)) {
			return 0;
		} else {
			double linesIntersectionX = getLinesIntersectionX(x1, y1, x2, y2, x3, y3, x4, y4);
			double linesIntersectionY = getLinesIntersectionY(x1, y1, x2, y2, x3, y3, x4, y4);
			if (isOnLineSegment(linesIntersectionX, linesIntersectionY, x1, y1, x2, y2) == false
					|| isOnLineSegment(linesIntersectionX, linesIntersectionY, x3, y3, x4, y4) == false) {
				return 0;
			} else {
				lineSegmentsIntersctionY = linesIntersectionY;
			}
		}
		return lineSegmentsIntersctionY;
  }


  /**
   * If there is no intersection, return 0.
   */
  // line 628 "../../../../../Block223States.ump"
   private double getLineSegmentsIntersctionX(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    double lineSegmentsIntersctionX = 0;
	boolean end_1 = isOnLineSegment(x1,y1,x3,y3,x4,y4);
	boolean end_2 = isOnLineSegment(x2,y2,x3,y3,x4,y4);
	boolean end_3 = isOnLineSegment(x3,y3,x1,y1,x2,y2);
	boolean end_4 = isOnLineSegment(x4,y4,x1,y1,x2,y2);
	if (end_1) {
		return x1;
	}
	if (end_2) {
		return x2;
	}
	if (end_3) {
		return x3;
	}
	if (end_4) {
		return x4;
	}
	if (isparallel(x1,y1,x2,y2,x3,y3,x4,y4)) {
		return 0;
	}
	else {
		double linesIntersectionX = getLinesIntersectionX(x1,y1,x2,y2,x3,y3,x4,y4);
		double linesIntersectionY = getLinesIntersectionY(x1,y1,x2,y2,x3,y3,x4,y4);
		if (isOnLineSegment(linesIntersectionX,linesIntersectionY,x1,y1,x2,y2) == false 
				|| isOnLineSegment(linesIntersectionX,linesIntersectionY,x3,y3,x4,y4) == false) {
			return 0;
		}
		else {
			lineSegmentsIntersctionX = linesIntersectionX;
		}
	}
	return lineSegmentsIntersctionX;
  }

  // line 665 "../../../../../Block223States.ump"
   private boolean isOnLineSegment(double pX, double pY, double x1, double y1, double x2, double y2){
    boolean isOnLineSegment = false;
	if (x1 == x2 && y1 == y2) {
		return false;
	}
	// vertical line segment
	else if (x1 == x2) {
		double topEndPointY = Math.max(y1,y2);
		double bottomEndPointY = Math.min(y1,y2);
		if (pX == x1 && pY <= topEndPointY && pY >= bottomEndPointY) {
			isOnLineSegment = true;
		}
	}
	// horizontal line segment
	else if (y1 == y2) {
		double leftEndPointX = Math.min(x1,x2);
		double rightEndPointX = Math.max(x1,x2);
		if (pY == y1 && pX >= leftEndPointX && pX <= rightEndPointX) {
			isOnLineSegment = true;
		}
	}
	//line segment with negative slope
	else if ((y2-y1)/(x2-x1) < 0 ) {
		double upperLeftEndPointX = Math.min(x1,x2);
		double upperLeftEndPointY = Math.max(y1,y2);
		double lowerRightEndPointX = Math.max(x1,x2);
		double lowerRightEndPointY = Math.min(y1,y2);
		if (pX >= upperLeftEndPointX && pX <= lowerRightEndPointX && pY <= upperLeftEndPointY && pY >= lowerRightEndPointY) {
			isOnLineSegment = true;
		}
	}
	//line segment with positive slope
	else if ((y2-y1)/(x2-x1) > 0) {
		double upperRightEndPointX = Math.max(x1,x2);
		double upperRightEndPointY = Math.max(y1,y2);
		double lowerLeftEndPointX = Math.min(x1,x2);
		double lowerLeftEndPointY = Math.min(y1,y2);
		if (pX >= lowerLeftEndPointX && pX <= upperRightEndPointX && pY >= lowerLeftEndPointY && pY <= upperRightEndPointY) {
			isOnLineSegment = true;
		}
	}
	return isOnLineSegment;
  }

  // line 710 "../../../../../Block223States.ump"
   private double getLinesIntersectionY(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    double intersectionY;
	if (isparallel(x1,y1,x2,y2,x3,y3,x4,y4)) {
		return 0;
	}
	else {
		intersectionY = ((x1*y2-y1*x2)*(y3-y4)-(y1-y2)*(x3*y4-y3*x4))/((x1-x2)*(y3-y4)-(y1-y2)*(x3-x4));
	}
	return intersectionY;
  }

  // line 722 "../../../../../Block223States.ump"
   private double getLinesIntersectionX(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    double intersectionX;
	if (isparallel(x1,y1,x2,y2,x3,y3,x4,y4)) {
		return 0;
	}
	else {
		intersectionX = ((x1*y2-y1*x2)*(x3-x4)-(x1-x2)*(x3*y4-y3*x4))/((x1-x2)*(y3-y4)-(y1-y2)*(x3-x4));
	}
	return intersectionX;
  }

  // line 733 "../../../../../Block223States.ump"
   private boolean isparallel(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4){
    if (((x1-x2)*(y3-y4)-(y1-y2)*(x3-x4))==0) {
		return true;
	}
	return false;
  }

  // line 740 "../../../../../Block223States.ump"
   private boolean hitLastBlock(){
    int nrBlocks = numberOfBlocks();
	   setBounce(null);
	   if (nrBlocks == 1){
		   PlayedBlockAssignment block = getBlock(0);
   		   BouncePoint bp = calculateBouncePointBlock(block); 
      		if (bp != null) { // added null bp check from sample solution v.4
    			if(bp.getX() == currentBallX+ ballDirectionX && bp.getY() == currentBallY+ ballDirectionY)
    				return false;
    			else {
        			setBounce(bp);
        			return true;				
    			}
    		}
	   }
    return false;
  }

  // line 758 "../../../../../Block223States.ump"
   private boolean hitBlock(){
    int nrBlocks = numberOfBlocks();
	   setBounce(null);
	   for (int i = 0; i <= nrBlocks -1; i++) {
		   PlayedBlockAssignment block = getBlock(i);
		   BouncePoint bp = calculateBouncePointBlock(block);
			   bounce = getBounce();
			   boolean closer = isCloser(bp,bounce);
			   if (closer == true) {
		    		if (bp != null) { // added null bp check from sample solution v.4
		    			if(bp.getX() == currentBallX+ ballDirectionX && bp.getY() == currentBallY+ ballDirectionY)
		    				return false;
		    			else {
		        			setBounce(bp);;				
		    			}
		    		}
			   }		   
	   }
    return (bounce != null);
  }

  // line 779 "../../../../../Block223States.ump"
   private boolean isCloser(BouncePoint first, BouncePoint second){
    boolean isCloser = false;
	  if (first==null) {
		  isCloser = false;
	  }
	  else if (second==null) {
		  isCloser = true;
	  }
	  else {
		  currentBallX = getCurrentBallX();
		  currentBallY = getCurrentBallY();
		  double firstX = first.getX();
		  double firstY =  first.getY();
		  double secondX = second.getX();
		  double secondY = second.getY();
		  double distanceBallFirst = Math.sqrt(Math.pow((firstX-currentBallX),2) + Math.pow((firstY - currentBallY),2));
		  double distanceBallSecond = Math.sqrt(Math.pow((secondX-currentBallX),2) + Math.pow((secondY - currentBallY),2));
		  if(distanceBallFirst < distanceBallSecond) {
			  isCloser = true;
		  }
	  }
	return isCloser;
  }

  // line 804 "../../../../../Block223States.ump"
   private boolean hitWall(){
    BouncePoint bp = calculateBouncePointWall();
    if (bp != null) {
    	if(bp.getX() == currentBallX + ballDirectionX && bp.getY() == currentBallY + ballDirectionY){
    		setBounce(null);
    		return false;
    	}
    	else {
    		setBounce(bp);
    		return true;
    	
    	}				
	}
    return false;
  }

  // line 819 "../../../../../Block223States.ump"
   private BouncePoint calculateBouncePointWall(){
    ArrayList<BouncePoint> bounceList = new ArrayList<>();
	   BouncePoint bp = null;
	   //Current ball position
	   double ballX1 = getCurrentBallX(), ballY1 = getCurrentBallY();
	   //Next position of ball
	   double ballX2 = ballX1 + getBallDirectionX(), ballY2 = ballY1 + getBallDirectionY();
	   double ballRadius = Ball.BALL_DIAMETER/2.0;
	   double m = (ballY2-ballY1)/(ballX2-ballX1); // slope of ball 

	   //Rectangle zones
	   Rectangle2D A = new Rectangle2D.Double(0, 0, ballRadius, Game.PLAY_AREA_SIDE - ballRadius); //left
	   Rectangle2D B = new Rectangle2D.Double(ballRadius, 0, Game.PLAY_AREA_SIDE - 2 * ballRadius, ballRadius);//top
	   Rectangle2D C = new Rectangle2D.Double(Game.PLAY_AREA_SIDE - ballRadius, 0, ballRadius, Game.PLAY_AREA_SIDE - ballRadius);//right

	   if(A.intersectsLine(ballX1, ballY1, ballX2, ballY2)) {
		   double bouncePointY = m*(ballRadius - ballX1) + ballY1;
		   if(bouncePointY == 5) {
			   bounceList.add(new BouncePoint(ballRadius, ballRadius, BounceDirection.FLIP_BOTH));
		   }
		   else {
			   bounceList.add(new BouncePoint(ballRadius, bouncePointY, BounceDirection.FLIP_X));
		   }
	   }
	   if(B.intersectsLine(ballX1, ballY1, ballX2, ballY2)) {
		   double bouncePointX = ((ballRadius - ballY1)/m) + ballX1;
		   if(bouncePointX == 5) {
			   bounceList.add(new BouncePoint(ballRadius, ballRadius, BounceDirection.FLIP_BOTH));
		   }
		   else if(bouncePointX == 385) {
			   bounceList.add(new BouncePoint(Game.PLAY_AREA_SIDE - ballRadius, ballRadius, BounceDirection.FLIP_BOTH));
		   }
		   else {
			   bounceList.add(new BouncePoint(bouncePointX, ballRadius, BounceDirection.FLIP_Y));
		   }
	   }
	   if(C.intersectsLine(ballX1, ballY1, ballX2, ballY2)){
		   double bouncePointY = m *(Game.PLAY_AREA_SIDE- ballRadius - ballX1) + ballY1;
		   if(bouncePointY == 5) {
			   bounceList.add(new BouncePoint(Game.PLAY_AREA_SIDE - ballRadius, ballRadius, BounceDirection.FLIP_BOTH));
		   }
		   else {
			   bounceList.add(new BouncePoint(Game.PLAY_AREA_SIDE - ballRadius, bouncePointY, BounceDirection.FLIP_X));
		   }
	   }
	   
	   bp = findClosestBouncePoint(bounceList);
	   setBounce(null);
	   return bp;
  }


  /**
   * Actions
   */
  // line 872 "../../../../../Block223States.ump"
   private void doSetup(){
    resetCurrentBallX();
		resetCurrentBallY();
		resetBallDirectionX();
		resetBallDirectionY();
		resetCurrentPaddleX();
		getGame();
		Level level = game.getLevel(currentLevel - 1);
		List<BlockAssignment> blockassn = level.getBlockAssignments();

		for (BlockAssignment b : blockassn) {
			new PlayedBlockAssignment(
					Game.WALL_PADDING + (Block.SIZE + Game.COLUMNS_PADDING) * (b.getGridHorizontalPosition() - 1),
					Game.WALL_PADDING + (Block.SIZE + Game.ROW_PADDING) * (b.getGridVerticalPosition() - 1),
					b.getBlock(), this);
		}
		
		Random random = new Random();
		List<Integer> tempX = new ArrayList<Integer>();
		List<Integer> tempY = new ArrayList<Integer>();
		
		while(numberOfBlocks() < game.getNrBlocksPerLevel()) {
			int gridX = random.nextInt(15) + 1; // Random is [0, x)
			int gridY = random.nextInt(15) + 1;
			
			while(isBlockAlreadyAtPos(blockassn, tempX, tempY, gridX, gridY)) {
				gridX++;
				if(gridX > 15) {
					gridX = 1;
					gridY++;
					if (gridY > 15) {
						gridY = 1;
					}
				}
			}
			tempX.add(gridX);
			tempY.add(gridY);
			new PlayedBlockAssignment(
					Game.WALL_PADDING + (Block.SIZE + Game.COLUMNS_PADDING) * (gridX - 1),
					Game.WALL_PADDING + (Block.SIZE + Game.ROW_PADDING) * (gridY - 1),
					getRandomBlock(), this);
			
			
		}
  }

  // line 918 "../../../../../Block223States.ump"
   private Block getRandomBlock(){
    Random random = new Random();
		int num = random.nextInt(getGame().getBlocks().size()); // Random is [0, x)
		return getGame().getBlock(num);
  }

  // line 924 "../../../../../Block223States.ump"
   private boolean isBlockAlreadyAtPos(List<BlockAssignment> ba, List<Integer> tempX, List<Integer> tempY, int x, int y){
    for(BlockAssignment b: ba) {
			if(b.getGridHorizontalPosition() == x && b.getGridVerticalPosition() == y) {
				return true;
			}
		}
		for(int i = 0; i < tempX.size(); i++) {
    		if(tempX.get(i) == x && tempY.get(i) == y) {
    		return true;
    		}	
    	}
		return false;
  }

  // line 938 "../../../../../Block223States.ump"
   private void doHitPaddleOrWall(){
    bounceBall();
  }

  // line 942 "../../../../../Block223States.ump"
   private void doOutOfBounds(){
    this.setLives(this.lives - 1);
    this.resetCurrentBallX();
    this.resetCurrentBallY();
    this.resetBallDirectionX();
    this.resetBallDirectionY();
    this.resetCurrentPaddleX();
  }

  // line 951 "../../../../../Block223States.ump"
   private void doHitBlock(){
    score = getScore();
    bounce = getBounce();
    PlayedBlockAssignment pBlock = bounce.getHitBlock();
    Block block = pBlock.getBlock();
    int bSocre = block.getPoints(); //corrected from "block.getScore()" in sample solution v.3
    setScore(score + bSocre);
    pBlock.delete();
    bounceBall();
  }

  // line 962 "../../../../../Block223States.ump"
   private void doHitBlockNextLevel(){
    doHitBlock();
    int level = getCurrentLevel();
    setCurrentLevel(level + 1);
    setCurrentPaddleLength(getGame().getPaddle().getMaxPaddleLength() 
    		- (getGame().getPaddle().getMaxPaddleLength() - getGame().getPaddle().getMinPaddleLength()) / (getGame().numberOfLevels() - 1)*(getCurrentLevel() - 1));
    setWaitTime(INITIAL_WAIT_TIME * Math.pow(getGame().getBall().getBallSpeedIncreaseFactor(), (getCurrentLevel()-1)));
  }

  // line 971 "../../../../../Block223States.ump"
   private void doHitNothingAndNotOutOfBounds(){
    double x = getCurrentBallX();
    double y = getCurrentBallY();
    double dx = getBallDirectionX();
    double dy = getBallDirectionY();
    setCurrentBallX(x + dx);
    setCurrentBallY(y + dy);
  }

  // line 980 "../../../../../Block223States.ump"
   private void doGameOver(){
    if (this.player != null) {
     HallOfFameEntry hof = new HallOfFameEntry(score, playername, player, game, block223);
     game.setMostRecentEntry(hof);
    }
    this.delete();
  }

  // line 987 "../../../../../Block223States.ump"
   private void bounceBall(){
    BouncePoint bp = getBounce();
       BounceDirection bd = bp.getDirection();
       int signX, signY;
       double oldBallDirection, in, out;

       //Get sign of X and Y direction vectors with 0 included as positive direction to account for offset in the game
       if(getBallDirectionX()>=0) {
           signX=1;
       }
       else {
           signX=-1;
       }

       if(getBallDirectionY()>=0) {
           signY=1;
       }
       else {
           signY=-1;
       }

       //Set new ball direction vector
       if(bd == BounceDirection.FLIP_X) {
           in = bp.getX() - getCurrentBallX();
           out = getBallDirectionX() - in;
           oldBallDirection = getBallDirectionX();
           
           if(out!=0) {
               setBallDirectionX(-1 * getBallDirectionX());
               setBallDirectionY(getBallDirectionY() + signY *  0.1 * Math.abs(getBallDirectionX()));
           }

       }
       else if(bd == BounceDirection.FLIP_Y) {
    	   in = bp.getY() - getCurrentBallY();
    	   out = getBallDirectionY() - in;
    	   oldBallDirection = getBallDirectionY();
    	   
    	   if(out!=0) {
        	   setBallDirectionX(getBallDirectionX() + signX * 0.1 *Math.abs(getBallDirectionY()));
        	   setBallDirectionY(-1 * getBallDirectionY());
    	   }
       }
       else{//FLIP_BOTH
           in = bp.getY() - getCurrentBallY();
           out = getBallDirectionY() - in;
           oldBallDirection = getBallDirectionY();
           
           if(out!=0) {
               setBallDirectionX(-1 * getBallDirectionX());
               setBallDirectionY(-1 * getBallDirectionY());
           }

       }
       
           //Move ball by ball direction vector
           setCurrentBallX(bp.getX() + out / oldBallDirection * getBallDirectionX());  
           setCurrentBallY(bp.getY() + out / oldBallDirection * getBallDirectionY());
           setBounce(null);
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "," +
            "score" + ":" + getScore()+ "," +
            "lives" + ":" + getLives()+ "," +
            "currentLevel" + ":" + getCurrentLevel()+ "," +
            "waitTime" + ":" + getWaitTime()+ "," +
            "playername" + ":" + getPlayername()+ "," +
            "ballDirectionX" + ":" + getBallDirectionX()+ "," +
            "ballDirectionY" + ":" + getBallDirectionY()+ "," +
            "currentBallX" + ":" + getCurrentBallX()+ "," +
            "currentBallY" + ":" + getCurrentBallY()+ "," +
            "currentPaddleLength" + ":" + getCurrentPaddleLength()+ "," +
            "currentPaddleX" + ":" + getCurrentPaddleX()+ "," +
            "currentPaddleY" + ":" + getCurrentPaddleY()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "player = "+(getPlayer()!=null?Integer.toHexString(System.identityHashCode(getPlayer())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "bounce = "+(getBounce()!=null?Integer.toHexString(System.identityHashCode(getBounce())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "block223 = "+(getBlock223()!=null?Integer.toHexString(System.identityHashCode(getBlock223())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 117 "../../../../../Block223Persistence.ump"
  private static final long serialVersionUID = 8597675110221231714L ;

  
}