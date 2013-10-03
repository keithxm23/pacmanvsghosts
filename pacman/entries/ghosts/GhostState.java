package pacman.entries.ghosts;

import java.util.EnumMap;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class GhostState {
	
	public enum state {
		CHASE,
		PATROL
	}
	
	private GHOST ghost;
	private state current;
	public static int lastSeenPacmanAt; //Holds NodeIndex for where Pacman was last seen
	public static int pacmanSeenBy = 0; //Counts number of Ghosts that Pacman is in the line-of-sight of
	public static int chaseCoolDown = 0; //Counts seconds until all Ghosts forget about Pacman and end CHASE
	
	public GhostState(GHOST ghost) {
		this.ghost = ghost;
		current = state.PATROL; //When the game stats Ghosts are initialized to PATROL state and head to their corners
	}
	
	/**
	 * Given the Game object, check line-of-sight and update globals
	 *
	 * @param EnumMap<GHOST, GhostState> Mapping of Ghost to their respective GhostStates
	 * @param Game The game object
	 */
	public void checkPacmanInSight(Game game, EnumMap<GHOST, GhostState> ghosts)
	{
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		int pacmanX = game.getNodeXCood(pacmanNode);
		int pacmanY = game.getNodeYCood(pacmanNode);
		int ghostNode = game.getGhostCurrentNodeIndex(ghost);
		int ghostX = game.getNodeXCood(ghostNode);
		int ghostY = game.getNodeYCood(ghostNode);
		
		//To check if Pacman is in sight of a ghost:
		//Check if Pacman and Ghost are
		// - in the same X OR in the same Y coordiante
		// - AND the Euclidean distance and PATH distance is the same. - This would infer there are no obstructing walls and the Ghost IS facing Pacman
		if (((pacmanX == ghostX) || (pacmanY == ghostY))
				&& (game.getDistance(ghostNode, pacmanNode, game.getGhostLastMoveMade(ghost), DM.PATH) 
						== game.getDistance(ghostNode, pacmanNode, DM.EUCLID)))
		{
			
			System.out.println("Ghost xy: " +ghostX+","+ghostY+" Pac xy: "+pacmanX+","+pacmanY+ ". manh, euc"+game.getDistance(ghostNode, pacmanNode, game.getGhostLastMoveMade(ghost), DM.MANHATTAN)+","+game.getDistance(ghostNode, pacmanNode, DM.EUCLID));
			lastSeenPacmanAt = pacmanNode;
			pacmanSeenBy++;
			chaseCoolDown = 30*5; //150 game ticks = approx 3-4 seconds
		}		
	}
	
	/**
	 * Given the Game object, update the states of the respective ghosts
	 *
	 * @param Game The game object
	 */
	public void transitionState(Game game)
	{
		//Enter CHASE state if Pacman is seen by at least 1 ghost or the chaseCoolDown is still on
		if ((pacmanSeenBy > 0) || (chaseCoolDown > 0))
		{
			current = state.CHASE;
			System.out.println(ghost + " is now CHASING!");
		}
		//Else enter the PATROL state
		else
		{
			current = state.PATROL;
			System.out.println(ghost + " is now PATROLING.");
		}
		
	}
	
	/**
	 * Given the Game object, returns the x and y coordinates for the ghosts
	 *
	 * @param Game The game object
	 * @return Int [] A 2 element array holding x and y coordinates
	 */
	public int [] getTarget(Game game)
	{
		pacmanSeenBy = 0;
		chaseCoolDown--;
		switch(current)
		{
		case CHASE:
			//While in CHASE state, target Pacman's current location
			return new int [] {game.getNodeXCood(lastSeenPacmanAt), game.getNodeYCood(lastSeenPacmanAt)};
		case PATROL:
			//Else each ghost takes up a corner of the map. Same corners assigned to each Ghost as they are in the Traditional AI's Scatter mode
			switch(ghost)
			{
			case BLINKY:
				return new int [] {104, 4};
			case PINKY:
				return new int [] {4, 4};
			case INKY:
				return new int [] {104, 116};
			case SUE:
				return new int [] {4, 116};
			}
		}
		return null; 
	}
}