package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Map.Entry;

import pacman.controllers.Controller;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import pacman.entries.ghosts.GhostState;


public class GhostGuards extends Controller<EnumMap<GHOST,MOVE>>
{
	private EnumMap<GHOST, MOVE> myMoves=new EnumMap<GHOST, MOVE>(GHOST.class);
	public int timer = 0;
	public int lastScatterAt = 0;
	public int ticksPerSec = 30;
	
	private GhostState blinkyState = new GhostState(GHOST.BLINKY);
	private GhostState pinkyState = new GhostState(GHOST.PINKY);
	private GhostState inkyState = new GhostState(GHOST.INKY);
	private GhostState sueState = new GhostState(GHOST.SUE);
	
	
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue)
	{
		myMoves.clear();
		
		final EnumMap<GHOST, GhostState> ghostStates = new EnumMap<GHOST, GhostState>(GHOST.class);
		ghostStates.put(GHOST.BLINKY, blinkyState);
		ghostStates.put(GHOST.PINKY, pinkyState);
		ghostStates.put(GHOST.INKY, inkyState);
		ghostStates.put(GHOST.SUE, sueState);

		
		//First check if Pacman is in sight for each of the ghosts and update the globals
		for (Entry<GHOST, GhostState> gs : ghostStates.entrySet()) {
			final GhostState gState = gs.getValue();
			gState.checkPacmanInSight(game, ghostStates);
			}
		
		//Now based on the values of the globals, update the state of each of the ghosts
		// and reset global variables if needed
		for (Entry<GHOST, GhostState> gs : ghostStates.entrySet()) {
			final GhostState gState = gs.getValue();			
			gState.transitionState(game);
			
		}
		
		//Now based on the current state, perform the required move for each of the ghosts
		for (Entry<GHOST, GhostState> gs : ghostStates.entrySet()) {
			final GHOST g = gs.getKey();
			final GhostState gState = gs.getValue();
			final int[] gTarget = gState.getTarget(game);
			myMoves.put(g, getCustomNextMoveTowardsTarget(game, game.getGhostCurrentNodeIndex(g), gTarget[0], gTarget[1], game.getGhostLastMoveMade(g)));
		}		
		return myMoves;
	}
	
	
	// ------------ Some Helpers --------------------
		
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
	 * @return the Euclidean distance
	 */
	public double getCustomEuclideanDistance(Game game, int fromNodeIndex, int x, int y)
	{
		return Math.sqrt(Math.pow(game.getCurrentMaze().graph[fromNodeIndex].x - x,2) 
							+ Math.pow(game.getCurrentMaze().graph[fromNodeIndex].y - y,2));
	}
}