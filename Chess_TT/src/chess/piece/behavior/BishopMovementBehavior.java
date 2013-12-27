package chess.piece.behavior;

import java.util.LinkedList;

import chess.game.Coordinates;
import chess.game.Game;
import chess.game.Player;

/**
 * Created by TomTaila on 06/12/2013.
 */
public class BishopMovementBehavior extends MovementBehavior{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7820127920148592633L;

	@Override
	public LinkedList<Coordinates> getValidMoves(Game board, Player p, boolean firstMove, Coordinates pos) {
		initialize();
		
		int posX = pos.getX();
		int posY = pos.getY();
		while((posX+1 < 8) && (posY+1 < 8))//GOING DOWN-RIGHT
		{
			posX++;
			posY++;
			if(board.isVacant(posX, posY)) validMoves.add(new Coordinates(posX, posY));
			else if(board.isOccupiedByEnemy(p, posX, posY))
			{
				validMoves.add(new Coordinates(posX, posY));
				posX = 8;//break
			}
			else posX = 8;//break
		}

		posX = pos.getX();
		posY = pos.getY();
		while((posX+1 < 8) && (posY-1 > -1))//GOING UP-RIGHT
		{
			posX++;
			posY--;
			if(board.isVacant(posX, posY)) validMoves.add(new Coordinates(posX, posY));
			else if(board.isOccupiedByEnemy(p, posX, posY))
			{
				validMoves.add(new Coordinates(posX, posY));
				posX = 8;//break
			}
			else posX = 8;//break
		}

		posX = pos.getX();
		posY = pos.getY();
		while((posX-1 > -1) && (posY+1 < 8))//GOING DOWN-LEFT
		{
			posX--;
			posY++;
			if(board.isVacant(posX, posY)) validMoves.add(new Coordinates(posX, posY));
			else if(board.isOccupiedByEnemy(p, posX, posY))
			{
				validMoves.add(new Coordinates(posX, posY));
				posY = 8;//break
			}
			else posY = 8;//break
		}

		posX = pos.getX();
		posY = pos.getY();
		while((posX-1 > -1) && (posY-1 > -1))//GOING UP-LEFT
		{
			posX--;
			posY--;
			if(board.isVacant(posX, posY)) validMoves.add(new Coordinates(posX, posY));
			else if(board.isOccupiedByEnemy(p, posX, posY))
			{
				validMoves.add(new Coordinates(posX, posY));
				posY = -1;//break
			}
			else posY = -1;//break
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
