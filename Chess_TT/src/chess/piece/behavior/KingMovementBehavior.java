package chess.piece.behavior;

import java.util.LinkedList;

import chess.game.*;

/**
 * Created by TomTaila on 06/12/2013.
 */
public class KingMovementBehavior extends MovementBehavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8844611356288462855L;

	@Override
	public LinkedList<Coordinates> getValidMoves(Game board, Player p, boolean firstMove, Coordinates pos) {
		initialize();

		int posX = pos.getX();
		int posY = pos.getY();

		boolean spaceLeft = (posX-1 > -1);
		boolean spaceRight = (posX+1 < 8);
		boolean spaceUp = (posY-1 > -1);
		boolean spaceDown = (posY+1 < 8);

		if(spaceLeft)
		{
			if(spaceUp && (board.isVacant(posX-1, posY-1) || board.isOccupiedByEnemy(p, posX-1, posY-1)))
			{
				validMoves.add(new Coordinates(posX-1, posY-1));
			}
			if((board.isVacant(posX-1, posY) || board.isOccupiedByEnemy(p, posX-1, posY)))
			{
				validMoves.add(new Coordinates(posX-1, posY));
			}
			if(spaceDown && (board.isVacant(posX-1, posY+1) || board.isOccupiedByEnemy(p, posX-1, posY+1)))
			{
				validMoves.add(new Coordinates(posX-1, posY+1));
			}
		}
		if(spaceRight)
		{
			if(spaceUp && (board.isVacant(posX+1, posY-1) || board.isOccupiedByEnemy(p, posX+1, posY-1)))
			{
				validMoves.add(new Coordinates(posX+1, posY-1));
			}
			if((board.isVacant(posX+1, posY) || board.isOccupiedByEnemy(p, posX+1, posY)))
			{
				validMoves.add(new Coordinates(posX+1, posY));
			}
			if(spaceDown && (board.isVacant(posX+1, posY+1) || board.isOccupiedByEnemy(p, posX+1, posY+1)))
			{
				validMoves.add(new Coordinates(posX+1, posY+1));
			}
		}
		if(spaceUp)
		{
			if(board.isVacant(posX, posY-1) || board.isOccupiedByEnemy(p, posX, posY-1)) validMoves.add(new Coordinates(posX, posY-1));
		}
		if(spaceDown)
		{
			if(board.isVacant(posX, posY+1) || board.isOccupiedByEnemy(p, posX, posY+1)) validMoves.add(new Coordinates(posX, posY+1));
		}

		if(p.playerID() == 1 && firstMove && board.getPiece(0, 7) != null)
		{
			if(board.getPiece(0, 7).pieceType().equals("Rook") && board.getPiece(0, 7).firstMove() && board.getPiece(1, 7) == null && board.getPiece(2, 7) == null)
			{
				validMoves.add(new Coordinates(0, 7));
			}
		}
		else if(p.playerID() == 2 && firstMove && board.getPiece(7, 0) != null)
		{
			if(board.getPiece(7, 0).pieceType().equals("Rook") && board.getPiece(7, 0).firstMove() && board.getPiece(5, 0) == null && board.getPiece(6, 0) == null)
			{
				validMoves.add(new Coordinates(7, 0));
			}
		}
		
		//ACCOUNT FOR CHECK
		if(p.checked())
		{
			LinkedList<Coordinates> tempValidMoves = new LinkedList<Coordinates>();
//			findCheckBlockingMoves(board, p);
			
			checkBlockingMoves.add(new Coordinates(p.getKingPos().getX()-1, p.getKingPos().getY()-1));
			checkBlockingMoves.add(new Coordinates(p.getKingPos().getX(), p.getKingPos().getY()-1));
			checkBlockingMoves.add(new Coordinates(p.getKingPos().getX()+1, p.getKingPos().getY()-1));

			checkBlockingMoves.add(new Coordinates(p.getKingPos().getX()-1, p.getKingPos().getY()));
			checkBlockingMoves.add(new Coordinates(p.getKingPos().getX()+1, p.getKingPos().getY()));

			checkBlockingMoves.add(new Coordinates(p.getKingPos().getX()-1, p.getKingPos().getY()+1));
			checkBlockingMoves.add(new Coordinates(p.getKingPos().getX(), p.getKingPos().getY()+1));
			checkBlockingMoves.add(new Coordinates(p.getKingPos().getX()+1, p.getKingPos().getY()+1));
			
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
