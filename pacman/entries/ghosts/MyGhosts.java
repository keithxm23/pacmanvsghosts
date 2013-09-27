package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getActions() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.ghosts.mypackage).
 */
public class MyGhosts extends Controller<EnumMap<GHOST,MOVE>>
{
	private EnumMap<GHOST, MOVE> myMoves=new EnumMap<GHOST, MOVE>(GHOST.class);
	private MOVE[] allMoves=MOVE.values();
	private Random rnd=new Random();
	public int minX = 4;
	public int minY = 4;
	public int maxX = 104;
	public int maxY = 116;
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
	{
		myMoves.clear();
		
		//Placing game logic here to play the game as the ghosts
		//BLINKY Code here:
		myMoves.put(GHOST.BLINKY, MOVE.LEFT);
		/*myMoves.put(GHOST.BLINKY, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.BLINKY),
				game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(GHOST.BLINKY),DM.PATH));
		*/
		
		//PINKY Code here:
		myMoves.put(GHOST.PINKY, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.PINKY),
				getZNodesInFrontOfPacman(game, 4), game.getGhostLastMoveMade(GHOST.PINKY),DM.PATH));
				
		//INKY Code here: TODO
		myMoves.put(GHOST.INKY, MOVE.LEFT);
						
		//SUE Code here:
		if (game.getEuclideanDistance(game.getGhostCurrentNodeIndex(GHOST.SUE), game.getPacmanCurrentNodeIndex()) > 8)
		{
			myMoves.put(GHOST.SUE, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.SUE),
					game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.SUE),DM.PATH));
		}
		else
		{
			myMoves.put(GHOST.SUE, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.SUE),
					getNodeIndexByCood(game, 4, 116), game.getGhostLastMoveMade(GHOST.SUE),DM.PATH));
		}
				
		/*
		for(GHOST ghost : GHOST.values())	//for each ghost
		{			
			if(game.doesGhostRequireAction(ghost))		//if ghost requires an action
			{
				myMoves.put(ghost,allMoves[rnd.nextInt(allMoves.length)]);
			}
		}
		*/
		
		return myMoves;
	}
	
	public int getNodeIndexByCood(Game game, int x, int y)
	{
		
//		System.out.println(x);
//		System.out.println(y);
//		System.out.println(" -- ");
		//TODO do a modulo on the coordinate so it doesn't become illegal
		for(Node n : game.getCurrentMaze().graph)
		{
			if(n.x == x && n.y == y)
			{
				return n.nodeIndex;
			}
		
		}
		System.out.println("Node not found with "+x+" and " +y);
		return -1;
	}

	
	public int getZNodesInFrontOfPacman(Game game, int z)
	{
		int newX, newY;
		switch(game.getPacmanLastMoveMade()){
		case UP: 
		newX = game.getNodeXCood(game.getPacmanCurrentNodeIndex());
		newY =  game.getNodeYCood(game.getPacmanCurrentNodeIndex())-z;
		break;
		case RIGHT:
		newX = game.getNodeXCood(game.getPacmanCurrentNodeIndex())+z;
		newY =  game.getNodeYCood(game.getPacmanCurrentNodeIndex());
		break;
		case DOWN: 
		newX = game.getNodeXCood(game.getPacmanCurrentNodeIndex());
		newY =  game.getNodeYCood(game.getPacmanCurrentNodeIndex())+z;
		break;
		case LEFT: 
		newX = game.getNodeXCood(game.getPacmanCurrentNodeIndex())-z;
		newY =  game.getNodeYCood(game.getPacmanCurrentNodeIndex());
		break;
		case NEUTRAL: 
		newX = game.getNodeXCood(game.getPacmanCurrentNodeIndex());
		newY =  game.getNodeYCood(game.getPacmanCurrentNodeIndex());
		break;
		default: 
		newX = game.getNodeXCood(game.getPacmanCurrentNodeIndex());
		newY =  game.getNodeYCood(game.getPacmanCurrentNodeIndex());
		break;
		}
		
		if (newX < minX)
		{
			newX=minX;
		}
		else if (newX > maxX)
		{
			newX = maxX;
		}
		
		if (newY < minY)
		{
			newY=minY;
		}
		else if (newY > maxY)
		{
			newY = maxY;
		}
		
		return getNodeIndexByCood(game, newX, newY);
	}
	
	
	
}