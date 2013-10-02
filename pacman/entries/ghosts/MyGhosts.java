package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Random;
import java.util.Map.Entry;

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
	public int timer = 0;
	public int lastScatterAt = -1;
	public int ticksPerSec = 30; //since 30ticks seemed to equal 1 second
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
	{
		myMoves.clear();
		
		//Check if GlobalReversal occurred so as to trigger scatter mode
		if (game.getTimeOfLastGlobalReversal() != lastScatterAt)
		{
			lastScatterAt = game.getTimeOfLastGlobalReversal();
			timer = 7 * ticksPerSec;
		}
		
		if (timer > 0)
			{
			//Enter Scatter Mode
			timer--;
			System.out.println("In scatter Mode " + game.getTimeOfLastGlobalReversal()+ "  " + game.getTotalTime());
			myMoves.put(GHOST.BLINKY, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.BLINKY),
					getNodeIndexByCood(game, maxX, minX),game.getGhostLastMoveMade(GHOST.BLINKY),DM.PATH));
			myMoves.put(GHOST.PINKY, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.PINKY),
					getNodeIndexByCood(game, minX, minX),game.getGhostLastMoveMade(GHOST.PINKY),DM.PATH));
			myMoves.put(GHOST.INKY, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.INKY),
					getNodeIndexByCood(game, maxX, maxY),game.getGhostLastMoveMade(GHOST.INKY),DM.PATH));
			myMoves.put(GHOST.SUE, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.SUE),
					getNodeIndexByCood(game, minX, maxY),game.getGhostLastMoveMade(GHOST.SUE),DM.PATH));
			
		}
		else
		{
			//Enter chase mode
			
			//BLINKY Code here:
			myMoves.put(GHOST.BLINKY, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.BLINKY),
					game.getPacmanCurrentNodeIndex(),game.getGhostLastMoveMade(GHOST.BLINKY),DM.PATH));
			
			System.out.println(game.getNodeXCood(game.getPacmanCurrentNodeIndex()) + " " + game.getNodeYCood(game.getPacmanCurrentNodeIndex()));
			
			//PINKY Code here:
			int newX, newY;
			int xy [] = getZCoodsInFrontOfPacman(game, 4*2);
			newX = xy[0];
			newY = xy[1];
			myMoves.put(GHOST.PINKY, getCustomNextMoveTowardsTarget(game, game.getGhostCurrentNodeIndex(GHOST.PINKY),
					newX, newY, game.getGhostLastMoveMade(GHOST.PINKY)));
			
			
			
			//INKY Code here:
			xy = getZCoodsInFrontOfPacman(game, 2*2);
			newX = xy[0];
			newY = xy[1];
	
			int tmpX = newX - game.getNodeXCood(game.getGhostCurrentNodeIndex(GHOST.BLINKY));
			int tmpY = newY - game.getNodeYCood(game.getGhostCurrentNodeIndex(GHOST.BLINKY));
			int inkyX, inkyY;
			inkyX = newX + tmpX;
			inkyY = newY + tmpY;
			
	
			myMoves.put(GHOST.INKY, getCustomNextMoveTowardsTarget(game, game.getGhostCurrentNodeIndex(GHOST.INKY),
				inkyX, inkyY, game.getGhostLastMoveMade(GHOST.INKY)));
	
			
			//SUE Code here:
			if (game.getEuclideanDistance(game.getGhostCurrentNodeIndex(GHOST.SUE), game.getPacmanCurrentNodeIndex()) > 8*2)
			{
				myMoves.put(GHOST.SUE, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.SUE),
						game.getPacmanCurrentNodeIndex(), game.getGhostLastMoveMade(GHOST.SUE),DM.PATH));
			}
			else
			{
				myMoves.put(GHOST.SUE, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(GHOST.SUE),
						getNodeIndexByCood(game, minX, maxY), game.getGhostLastMoveMade(GHOST.SUE),DM.PATH));
			}
					
			//if a ghost is frightened, perform pseudorandom moves
			for(GHOST ghost : GHOST.values())	//for each ghost
			{			
				if(game.isGhostEdible(ghost))		//if ghost is frightened
				{
					myMoves.put(ghost ,allMoves[rnd.nextInt(allMoves.length)]); //pesudorandomly select a move
				}
			}
		}
		return myMoves;
	}
	
	public int getNodeIndexByCood(Game game, int x, int y)
	{
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

	
	public int [] getZCoodsInFrontOfPacman(Game game, int z)
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
		default: 
		newX = game.getNodeXCood(game.getPacmanCurrentNodeIndex());
		newY =  game.getNodeYCood(game.getPacmanCurrentNodeIndex());
		break;
		}
		
		return new int [] {newX, newY};
	}
	
		
	/**
	 * Gets the next move towards target not considering directions opposing the last move made.
	 *
	 * @param Game object
	 * @param fromNodeIndex The node index from which to move (i.e., current position)
	 * @param x co-ordinate of the target
	 * @param y co-ordinate of the target
	 * @param lastMoveMade The last move made
	 * @return The approximate next move towards target (chosen greedily)
	 */
	public MOVE getCustomNextMoveTowardsTarget(Game game, int fromNodeIndex, int x, int y, MOVE lastMoveMade)
	{
		MOVE move=null;

		double minDistance=Integer.MAX_VALUE;

		for(Entry<MOVE,Integer> entry : game.getCurrentMaze().graph[fromNodeIndex].allNeighbourhoods.get(lastMoveMade).entrySet())
		{
			double distance=getCustomEuclideanDistance(game, entry.getValue(), x, y);
								
			if(distance<minDistance)
			{
				minDistance=distance;
				move=entry.getKey();	
			}
		}
		return move;
	}
	
	/**
	 * Returns the EUCLEDIAN distance between two nodes in the current mazes[gs.curMaze].
	 *
	 * @param Game object
	 * @param fromNodeIndex the from node index
	 * @param x co-ordinate of target
	 * @param y co-ordinate of target
	 * @return the euclidean distance
	 */
	public double getCustomEuclideanDistance(Game game, int fromNodeIndex, int x, int y)
	{
		return Math.sqrt(Math.pow(game.getCurrentMaze().graph[fromNodeIndex].x - x,2) 
							+ Math.pow(game.getCurrentMaze().graph[fromNodeIndex].y - y,2));
	}
}