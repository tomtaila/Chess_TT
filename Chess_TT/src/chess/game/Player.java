package chess.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import android.util.Log;

import chess.piece.Piece;

/**
 * Created by TomTaila on 06/12/2013.
 */
public class Player implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8280071712826441744L;
	private int playerID;
	private Coordinates kingPos;
	private boolean checked;
	private boolean inCheckMate;
	private ArrayList<Piece> pieces;
	private ArrayList<Coordinates> validMoves;

	public Player(int playerID) {
		this.playerID = playerID;
		checked = false;
		inCheckMate = false;
		if(playerID == 1) kingPos = new Coordinates(3, 7);
		else kingPos = new Coordinates(4, 0);
		pieces = new ArrayList<Piece>(16);
		validMoves = new ArrayList<Coordinates>(64);
	}

	public Player(Player p)
	{
		playerID = p.playerID;
		checked = p.checked;
		inCheckMate = p.inCheckMate;
		kingPos = new Coordinates(p.kingPos);
		pieces = p.pieces;
		validMoves = p.validMoves;
	}

	public int playerID()
	{
		return playerID;
	}

	public boolean checked()
	{
		return checked;
	}

	public void setChecked(boolean inCheck)
	{
		this.checked = inCheck;
	}

	public boolean isInCheckMate() {return inCheckMate;}

	public void setInCheckMate(boolean inCheckMate)
	{
		this.inCheckMate = inCheckMate;
	}

	@SuppressWarnings("unchecked")
	public boolean checkMate(Game board)
	{
		boolean flag = true;
		ArrayList<Piece> tempPieces = (ArrayList<Piece>) pieces.clone();
		LinkedList<Coordinates> tempValidMoves = null;

		int startX, startY, destX, destY;

		Piece tempPlayerPiece = null;
		Piece tempEnemyPiece = null;
		boolean destOccupied = false;


		for(Piece piece : tempPieces)
		{
			tempValidMoves = (LinkedList<Coordinates>) piece.getValidMoves().clone();
			tempPlayerPiece = piece.clone();

			for(Coordinates c : tempValidMoves)
			{
				startX = tempPlayerPiece.getPos().getX();
				startY = tempPlayerPiece.getPos().getY();
				destX = c.getX();
				destY = c.getY();

				destOccupied = board.isOccupied(destX, destY);
				if(destOccupied) tempEnemyPiece = board.getPiece(destX, destY).clone();
				
				board.movePiece(startX, startY, destX, destY);
				board.getPiece(destX, destY).setPos(c);
				if(tempPlayerPiece.pieceType().equals("King")) 
				{
					Log.d("KP1", kingPos.toString());
					kingPos = c;
					Log.d("KP2", kingPos.toString());
				}

				board.updatePieceLists();
				board.updateValidMoves();
				board.check();

				if(!checked)
				{
					flag = false;
				}

				board.movePiece(destX, destY, startX, startY);
				board.getPiece(startX, startY).setPos(new Coordinates(startX, startY));
				if(tempPlayerPiece.pieceType().equals("King")) 
				{
					Log.d("KP3", kingPos.toString());
					kingPos = new Coordinates(startX, startY);
					Log.d("KP4", kingPos.toString());
				}
				board.updatePieceLists();
				board.updateValidMoves();
				board.check();

				if(destOccupied) board.addPiece(tempEnemyPiece, destX, destY);
				board.updatePieceLists();
				board.updateValidMoves();
				board.check();
			}
		}
		return flag;
	}

	public Coordinates getKingPos(){return kingPos;}

	public void setKingPos(Coordinates pos)
	{
		kingPos = pos;
	}

	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	public ArrayList<Coordinates> getValidMoves() {
		return validMoves;
	}

}
