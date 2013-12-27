package chess.piece.behavior;

import java.util.LinkedList;

import chess.game.Coordinates;
import chess.game.Game;
import chess.game.Player;

/**
 * Created by TomTaila on 06/12/2013.
 */
public class PawnMovementBehavior extends MovementBehavior{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3071589506010926021L;

	@Override
	public LinkedList<Coordinates> getValidMoves(Game board, Player p, boolean firstMove, Coordinates pos) {
		initialize();
		
		int posX = pos.getX();
		int posY = pos.getY();

		if(p.playerID() == 1)
		{
			if(posY-2 > -1)
			{
				if(firstMove && board.isVacant(posX, posY-2)) validMoves.add(new Coordinates(posX, posY-2));
			}
			//If first move and board is vacant for both spaces in front of pawn --> Valid
			if(firstMove && board.isVacant(posX, posY-2) && board.isVacant(posX, posY-1)) validMoves.add(new Coordinates(posX, posY-2));
			if(posY-1 > -1)
			{
				//If board vacant in space in front --> Valid
				if(board.isVacant(posX, posY-1)) validMoves.add(new Coordinates(posX, posY-1));
				//If board is occupied by enemy one space in front and to the left/right --> Valid
				if((posX-1 > -1) && (board.isOccupiedByEnemy(p, posX-1, posY-1))) validMoves.add(new Coordinates(posX-1, posY-1)); 
				if((posX+1 < 8) && (board.isOccupiedByEnemy(p, posX+1, posY-1))) validMoves.add(new Coordinates(posX+1, posY-1));
			}
		}
		else if(p.playerID() == 2)
		{
			if(posY+2 < 8)
			{
				if(firstMove && board.isVacant(posX, posY+2)) validMoves.add(new Coordinates(posX, posY+2));
			}
			
			if(posY+1 < 8)
			{
				if(board.isVacant(posX, posY+1)) validMoves.add(new Coordinates(posX, posY+1));
				if((posX-1 > -1) && (board.isOccupiedByEnemy(p, posX-1, posY+1))) validMoves.add(new Coordinates(posX-1, posY+1)); 
				if((posX+1 < 8) && (board.isOccupiedByEnemy(p, posX+1, posY+1))) validMoves.add(new Coordinates(posX+1, posY+1));
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