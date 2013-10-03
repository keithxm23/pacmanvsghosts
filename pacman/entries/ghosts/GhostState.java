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

public class GhostState {
	
	public enum state {
		CHASE,
		SCOUT
	}
	
	private GHOST ghost;
	private state current;
	public static int lastSeenPacmanAt;
	public static int pacmanSeenBy = 0;
	public static int chaseCoolDown = 0;
	
	public GhostState(GHOST ghost) {
		this.ghost = ghost;
		current = state.SCOUT;
	}
	
	public void checkPacmanInSight(Game game, EnumMap<GHOST, GhostState> ghosts)
	{
		int pacmanNode = game.getPacmanCurrentNodeIndex();
		int pacmanX = game.getNodeXCood(pacmanNode);
		int pacmanY = game.getNodeYCood(pacmanNode);
		int ghostNode = game.getGhostCurrentNodeIndex(ghost);
		int ghostX = game.getNodeXCood(ghostNode);
		int ghostY = game.getNodeYCood(ghostNode);
		
		//To check if Pacman is in sight of a ghost
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
			chaseCoolDown = 30*5; //approx 5 seconds
		}		
	}
	
	public void transitionState(Game game, EnumMap<GHOST, GhostState> ghosts)
	{
		if ((pacmanSeenBy > 0) || (chaseCoolDown > 0))
		{
			current = state.CHASE;
			System.out.println(ghost + " is now CHASING!");
		}
		else
		{
			current = state.SCOUT;
			System.out.println(ghost + " is now SCOUTING.");
		}
		
	}
	
	public int [] getTarget(Game game)
	{
		pacmanSeenBy = 0;
		chaseCoolDown--;
		switch(current)
		{
		case CHASE:
			return new int [] {game.getNodeXCood(lastSeenPacmanAt), game.getNodeYCood(lastSeenPacmanAt)};
		case SCOUT:
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



