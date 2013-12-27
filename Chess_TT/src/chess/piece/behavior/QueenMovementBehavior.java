package chess.piece.behavior;

import java.util.LinkedList;

import chess.game.Coordinates;
import chess.game.Game;
import chess.game.Player;

/**
 * Created by TomTaila on 06/12/2013.
 */
public class QueenMovementBehavior extends MovementBehavior{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3270673276199718126L;

	@Override
	public LinkedList<Coordinates> getValidMoves(Game board, Player p, boolean firstMove, Coordinates pos) {
		initialize();

		BishopMovementBehavior bishopMovementBehavior = new BishopMovementBehavior();
		RookMovementBehavior rookMovementBehavior = new RookMovementBehavior();
		validMoves.addAll(bishopMovementBehavior.getValidMoves(board, p, firstMove, pos));
		validMoves.addAll(rookMovementBehavior.getValidMoves(board, p, firstMove, pos));

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
