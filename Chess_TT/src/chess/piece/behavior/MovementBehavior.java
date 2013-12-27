package chess.piece.behavior;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

import chess.game.Coordinates;
import chess.game.Game;
import chess.game.Player;
import chess.piece.Piece;

/**
 * Created by TomTaila on 06/12/2013.
 */
public abstract class MovementBehavior implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2967060047827492624L;
	//Indicaters of where players king is being checked from & what type of piece is checking the player
	private int checkedFromPosX;
	private int checkedFromPosY;
	private String checkingPieceType;
	//The players king position
	private int playerKingPosX;
	private int playerKingPosY;
	//The list of possible moves that the player may make with this piece in order to get out of being checked
	protected LinkedList<Coordinates> checkBlockingMoves;
	//List of valid moves to be determined
	protected LinkedList<Coordinates> validMoves;
	
	protected void initialize()
	{
		checkedFromPosX = -1;
		checkedFromPosY = -1;
		playerKingPosX = -1;
		playerKingPosY = -1;
		checkingPieceType = "";
		checkBlockingMoves = new LinkedList<Coordinates>();
		validMoves = new LinkedList<Coordinates>();
	}
	
	//Finds and fills the list of all possible moves the piece in question may make
	public abstract LinkedList<Coordinates> getValidMoves(Game board, Player p, boolean firstMove, Coordinates pos);

	//Find and fills the list of all possible moves this piece may make in order to get the player out of being checked
	public void findCheckBlockingMoves(Game board, Player p)
	{
		//Clear the list of check blocking moves to start a fresh list
		checkBlockingMoves.clear();
		//Get all the enemies pieces
		ArrayList<Piece> enemyPieces = board.getEnemyPlayerPieces(p);
		//Locate the position of the players king
		playerKingPosX = p.getKingPos().getX();
		playerKingPosY = p.getKingPos().getY();
		
		//Look though the enemies list of pieces to find which piece it is that is checking our king
		int i = 0;
		while(i < enemyPieces.size())
		{
			if(enemyPieces.get(i).getValidMoves().contains(p.getKingPos()))
			{
				//Once found, record its type and its position
				checkingPieceType = enemyPieces.get(i).pieceType();
				checkedFromPosX = enemyPieces.get(i).getPos().getX();
				checkedFromPosY = enemyPieces.get(i).getPos().getY();
				i = enemyPieces.size();
			}
			i++;
		}
		// IF CHECKED BY A ROOK
		if(checkingPieceType.equals("Rook"))
		{
			blockCheckFromRook(board, p, checkedFromPosX, checkedFromPosY);
		}
		//IF CHECKED BY A BISHOP
		if(checkingPieceType.equals("Bishop"))// IF CHECKED BY A BISHOP
		{
			blockCheckFromBishop(board, p,checkedFromPosX, checkedFromPosY);
		}
		//IF CHECKED BY A QUEEN
		if(checkingPieceType.equals("Queen"))// IF CHECKED BY A BISHOP
		{
			blockCheckFromRook(board, p,checkedFromPosX, checkedFromPosY);
			blockCheckFromBishop(board, p,checkedFromPosX, checkedFromPosY);
		}
		//IF CHECKED BY A HORSE
		if(checkingPieceType.equals("Horse"))// IF CHECKED BY A BISHOP
		{
			blockCheckFromHorse(board, p,checkedFromPosX, checkedFromPosY);
		}
		//IF CHECKED BY A PAWN
		if(checkingPieceType.equals("Pawn"))
		{
			blockCheckFromPawn(board, p, checkedFromPosX, checkedFromPosY);
		}

	}

	private void blockCheckFromRook(Game board, Player p, int checkedFromPosX, int checkedFromPosY)
	{
		//If the checking rook is vertical to checked king
		if(checkedFromPosX == playerKingPosX)
		{
			if(checkedFromPosY < playerKingPosY)//Above checked king
			{
				//Possible check blocks are all squares directly above the king including the one occupied by the Rook
				for(int j = checkedFromPosY; j < playerKingPosY; j++)
				{
					checkBlockingMoves.add(new Coordinates(playerKingPosX, j));
				}
			}
			if(checkedFromPosY > playerKingPosY)//Below checked king
			{
				//Possible check blocks are all squares directly below the king including the one occupied by the Rook
				for(int j = checkedFromPosY; j > playerKingPosY; j--)
				{
					checkBlockingMoves.add(new Coordinates(playerKingPosX, j));
				}
			}
		}
		//If the checking rook is vertical to checked king
		if(checkedFromPosY == playerKingPosY)
		{
			if(checkedFromPosX < playerKingPosX)//Left of checked king
			{
				//Possible check blocks are all squares directly to the left the king including the one occupied by the Rook
				for(int j = checkedFromPosX; j < playerKingPosX; j++)
				{
					checkBlockingMoves.add(new Coordinates(j, playerKingPosY));
				}
			}
			if(checkedFromPosX > playerKingPosX)//Right of checked king
			{
				//Possible check blocks are all squares directly to the right the king including the one occupied by the Rook
				for(int j = checkedFromPosX; j > playerKingPosX; j--)
				{
					checkBlockingMoves.add(new Coordinates(j, playerKingPosY));
				}
			}
		}
	}

	private void blockCheckFromBishop(Game board, Player p, int checkedFromPosX, int checkedFromPosY)
	{
		int i = 0;
		if(checkedFromPosX > playerKingPosX)//i.e. checking bishop right to checked king
		{
			if(checkedFromPosY > playerKingPosY)//checking rook down-right to checked king
			{
				i = 0;
				while(checkedFromPosX-i > playerKingPosX)
				{
					checkBlockingMoves.add(new Coordinates(checkedFromPosX-i, checkedFromPosY-i));
					i++;
				}
			}
			if(checkedFromPosY < playerKingPosY)//checking rook up-right to checked king
			{
				i = 0;
				while(checkedFromPosX-i > playerKingPosX)
				{
					checkBlockingMoves.add(new Coordinates(checkedFromPosX-i, checkedFromPosY+i));
					i++;
				}
			}
		}
		if(checkedFromPosX < playerKingPosX)//i.e. checking bishop left to checked king
		{
			if(checkedFromPosY > playerKingPosY)//checking rook down-left to checked king
			{
				i = 0;
				while(checkedFromPosX+i < playerKingPosX)
				{
					checkBlockingMoves.add(new Coordinates(checkedFromPosX+i, checkedFromPosY-i));
					i++;
				}
			}
			if(checkedFromPosY < playerKingPosY)//checking rook up-left to checked king
			{
				i = 0;
				while(checkedFromPosX+i < playerKingPosX)
				{
					checkBlockingMoves.add(new Coordinates(checkedFromPosX+i, checkedFromPosY+i));
					i++;
				}
			}
		}
	}

	private void blockCheckFromHorse(Game board, Player p, int checkedFromPosX, int checkedFromPosY)
	{
		checkBlockingMoves.add(new Coordinates(checkedFromPosX, checkedFromPosY));
	}

	private void blockCheckFromPawn(Game board, Player p, int checkedFromPosX, int checkedFromPosY)
	{
		checkBlockingMoves.add(new Coordinates(checkedFromPosX, checkedFromPosY));
	}



}