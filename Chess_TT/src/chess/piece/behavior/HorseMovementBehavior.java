package chess.piece.behavior;

import java.util.LinkedList;

import chess.game.Coordinates;
import chess.game.Game;
import chess.game.Player;

/**
 * Created by TomTaila on 06/12/2013.
 */
public class HorseMovementBehavior extends MovementBehavior{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2013459384852042623L;

	@Override
	public LinkedList<Coordinates> getValidMoves(Game board, Player p, boolean firstMove, Coordinates pos) {
		initialize();

		int posX = pos.getX();
		int posY = pos.getY();

		if((posX+2 < 8) && (posY+1 < 8))
		{
			if(board.isVacant(posX+2, posY+1) || board.isOccupiedByEnemy(p, posX+2, posY+1)) 
			{
				validMoves.add(new Coordinates(posX+2, posY+1));
			}
		}
		if((posX+2 < 8) && (posY-1 > -1))
		{
			if(board.isVacant(posX+2, posY-1) || board.isOccupiedByEnemy(p, posX+2, posY-1)) 
			{
				validMoves.add(new Coordinates(posX+2, posY-1));
			}
		}
		if((posX+1 < 8) && (posY+2 < 8))
		{
			if(board.isVacant(posX+1, posY+2) || board.isOccupiedByEnemy(p, posX+1, posY+2)) 
			{
				validMoves.add(new Coordinates(posX+1, posY+2));
			}
		}
		if((posX+1 < 8) && (posY-2 > -1))
		{
			if(board.isVacant(posX+1, posY-2) || board.isOccupiedByEnemy(p, posX+1, posY-2)) 
			{
				validMoves.add(new Coordinates(posX+1, posY-2));
			}
		}
		//////////////////
		if((posX-2 > -1) && (posY+1 < 8))
		{
			if(board.isVacant(posX-2, posY+1) || board.isOccupiedByEnemy(p, posX-2, posY+1)) 
			{
				validMoves.add(new Coordinates(posX-2, posY+1));
			}
		}
		if((posX-2 > -1) && (posY-1 > -1))
		{
			if(board.isVacant(posX-2, posY-1) || board.isOccupiedByEnemy(p, posX-2, posY-1)) 
			{
				validMoves.add(new Coordinates(posX-2, posY-1));
			}
		}
		if((posX-1 > -1) && (posY+2 < 8))
		{
			if(board.isVacant(posX-1, posY+2) || board.isOccupiedByEnemy(p, posX-1, posY+2)) 
			{
				validMoves.add(new Coordinates(posX-1, posY+2));
			}
		}
		if((posX-1 > -1) && (posY-2 > -1))
		{
			if(board.isVacant(posX-1, posY-2) || board.isOccupiedByEnemy(p, posX-1, posY-2)) 
			{
				validMoves.add(new Coordinates(posX-1, posY-2));
			}
		}

		//ACCOUNT FOR CHECK
		if(p.checked())
		{
			LinkedList<Coordinates> tempValidMoves = new LinkedList<Coordinates>();
			findCheckBlockingMoves(board, p);
			if(!validMoves.isEmpty())
			{
				for(Coordinates c:validMoves)
				{
					if(checkBlockingMoves.contains(c)) tempValidMoves.add(c);
				}
			}
			validMoves = tempValidMoves;
		}

		return validMoves;
	}

}
