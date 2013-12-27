package chess.piece;

import java.io.Serializable;
import java.util.LinkedList;

import chess.game.Coordinates;
import chess.game.Game;
import chess.game.Player;
import chess.piece.behavior.MovementBehavior;

/**
 * Created by TomTaila on 06/12/2013.
 */
public class Piece implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7167296692502190615L;
	private Player player;
	private MovementBehavior movementBehavior;
	private String pieceType;
	private boolean firstMove;
	private Coordinates pos;
	private LinkedList<Coordinates> validMoves;

	public Piece(Player player, MovementBehavior movementBehavior, String pieceType, Coordinates pos) {
		this.player = player;
		this.movementBehavior = movementBehavior;
		this.pieceType = pieceType;
		firstMove = true;
		validMoves = new LinkedList<Coordinates>();
		this.pos = pos;
	}

	public Piece(Piece p)
	{
		player = p.player;
		movementBehavior = p.movementBehavior;
		pieceType = p.pieceType;
		firstMove = p.firstMove;
		validMoves = p.validMoves;
		pos = p.pos;
	}

	@Override
	public Piece clone()
	{
		return new Piece(this);
	}

	public boolean move(Game board, int startX, int startY, int destX, int destY)
	{
		//Make a new temporary piece that is a clone of the one to be moved
		Piece tempPlayerPiece = new Piece(board.getPiece(startX, startY));
		//is the destination vacant
		boolean isVacantAtDest = board.isVacant(destX, destY);
		//Make an enemyPiece which will be used to clone any piece occupying the destination coordinates
		Piece tempEnemyPiece = null;//if vacant at dest then null
		if(!isVacantAtDest) tempEnemyPiece = new Piece(board.getPiece(destX, destY));//else clone piece at dest
		board.check();//check to see if any players are checked
		if(validMoves.contains(new Coordinates(destX, destY)))//If moving to the dest is a valid move for the piece
		{
			board.check();
			board.movePiece(startX, startY, destX, destY);//move the piece
			if(tempPlayerPiece.pieceType.equals("King"))//if piece being moved is the king then update the king position of player
			{
				board.getPiece(destX, destY).player.setKingPos(new Coordinates(destX, destY));
			}
			board.check();//recheck
			if(player.checked())//if player is in checked position
			{
				//reinsert piece originally moved into its original position
				board.addPiece(tempPlayerPiece, startX, startY);
				if(tempPlayerPiece.pieceType.equals("King"))//if it was a king, reupdate its position to original
				{
					board.getPiece(startX, startY).player.setKingPos(new Coordinates(startX, startY));
				}
				board.updateValidMoves();//update every pieces valid moves again
				tempPlayerPiece.pos = new Coordinates(startX, startY);//update the pieces posittion again
				board.check();//recheck
				if(!isVacantAtDest)//if there was an enemy piece at the destination to start with that got deleted, replace it
				{
					tempEnemyPiece.setPos(new Coordinates(destX, destY));
					board.replacePiece(tempEnemyPiece, destX, destY);
					board.check();
				}
				else board.removePiece(destX, destY);//else remove the piece now there

				return false;
			}
			else 
			{
				if(pieceType.equals("Pawn"))
				{
					if(player.playerID() == 1 && destY == 0)
					{
						board.replacePiece(new PieceFactory().createPiece(player, PieceFactory.QUEEN, new Coordinates(destX, destY)), destX, destY);
						board.check();
					}
					else if(player.playerID() == 2 && destY == 7)
					{
						board.replacePiece(new PieceFactory().createPiece(player, PieceFactory.QUEEN, new Coordinates(destX, destY)), destX, destY);
						board.check();
					}
				}
				if(firstMove && pieceType.equals("King"))
				{
					if(player.playerID() == 1 && destY == 7 && tempEnemyPiece != null)
					{
						if(tempEnemyPiece.pieceType.equals("Rook") && tempEnemyPiece.firstMove)
						{
							board.addPiece(new PieceFactory().createPiece(player, PieceFactory.ROOK, new Coordinates(startX, startY)), startX, startY);
							board.check();
						}
					}
					else if(player.playerID() == 2 &&  destY == 0 && tempEnemyPiece != null)
					{
						if(tempEnemyPiece.pieceType.equals("Rook") && tempEnemyPiece.firstMove)
						{
							board.addPiece(new PieceFactory().createPiece(player, PieceFactory.ROOK, new Coordinates(startX, startY)), startX, startY);
							board.check();
						}
					}
				}

				firstMove = false;
				return true;
			}
		}
		else return false;
	}

	public boolean firstMove()
	{
		return firstMove;
	}

	public void setPos(Coordinates pos)
	{
		this.pos = pos;
	}

	public String pieceType()
	{
		return pieceType;
	}

	@Override
	public String toString()
	{
		return pieceType + "//" + player.playerID();
	}

	public Coordinates getPos(){return pos;}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null) return false;
		if(obj instanceof Piece)
		{
			Piece arg = (Piece) obj;
			return (arg.pieceType.equals(pieceType) && arg.player.equals(player));
		}
		else return false;
	}

	public Player getPlayer() {
		return player;
	}

	public void updateValidMoves(Game board) {
		validMoves = movementBehavior.getValidMoves(board, player, firstMove, pos);
	}

	public LinkedList<Coordinates> getValidMoves()
	{
		return validMoves;
	}
}