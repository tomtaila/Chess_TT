package chess.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import chess.piece.Piece;
import chess.piece.PieceFactory;


/**
 * Created by TomTaila on 06/12/2013.
 */

public class Game implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1281499187405972270L;
	private PieceFactory pieceFactory;
	private Piece[][] board;
	private Player p1, p2;
	private int roundNum;
	private boolean checkMateReached;
	private Player winner;

	public Game()
	{
		board = new Piece[8][8];
		pieceFactory = new PieceFactory();
		p1 = new Player(1);
		p2 = new Player(2);
		roundNum = 1;
		checkMateReached = false;
		winner = new Player(3);
	}
	
	public Player getWinner()
	{
		return winner;
	}
	
	public void setWinner(Player p)
	{
		winner = p;
	}
	
	public void setCheckMateReached(boolean checkMateReached)
	{
		this.checkMateReached = checkMateReached;
	}
	
	public boolean getCheckMateReached()
	{
		return checkMateReached;
	}
	
	public Piece[][] getBoard(){return board;}

	public Piece getPiece(int x, int y) {
		return board[x][y];
	}

	public Player getPlayer(int playerID)
	{
		if(playerID == 1) return p1;
		else return p2;
	}

	public Player getEnemyPlayer(int playerID)
	{
		if(playerID == 1) return p2;
		else return p1;
	}
	
	public ArrayList<Piece> getEnemyPlayerPieces(Player friendlyPlayer)
	{
		if(friendlyPlayer.equals(p1)) return p2.getPieces();
		else return p1.getPieces();
	}

	public boolean addPiece(Piece piece, int x, int y) {
		if(x < 0 || x > 7 || y < 0 || y > 7) throw new IndexOutOfBoundsException("x and y coordinates must be between 0 and 7");
		if(board[x][y] == null)
		{
			board[x][y] = piece;
			piece.setPos(new Coordinates(x, y));
			updatePieceLists();
			updateValidMoves();
			return true;
		}
		else return false; //do nothing
	}

	public void replacePiece(Piece piece, int x, int y) {
		board[x][y] = piece;
		piece.setPos(new Coordinates(x, y));
		updatePieceLists();
		updateValidMoves();
	}

	public boolean removePiece(int x, int y) {
		if(x < 0 || x > 7 || y < 0 || y > 7) throw new IndexOutOfBoundsException("x and y coordinates must be between 0 and 7");
		if(board[x][y] == null) return false;
		else
		{
			board[x][y] = null;
			updatePieceLists();
			updateValidMoves();
			return true;
		}
	}

	public boolean movePiece(int startX, int startY, int destX, int destY)
	{
		if(startX < 0 || startX > 7 || destX < 0 || destX > 7 || startY < 0 || startY > 7 || destY < 0 || destY > 7) throw new IndexOutOfBoundsException("x and y coordinates must be between 0 and 7");

		if(isOccupied(startX, startY))
		{
			Piece temp = board[startX][startY];
			board[startX][startY] = null;
			board[destX][destY] = temp;
			board[destX][destY].setPos(new Coordinates(destX, destY));
			updatePieceLists();
			updateValidMoves();
			return true;
		}
		else return false;
	}

	public void updatePieceLists()
	{
		p1.getPieces().clear();
		p2.getPieces().clear();
		for(int i = 0; i < 8; i++)
		{
			for(int j = 0; j < 8; j++)
			{
				if(board[i][j] != null) 
				{
					if(board[i][j].getPlayer().equals(p1)) p1.getPieces().add(board[i][j]);
					else if(board[i][j].getPlayer().equals(p2)) p2.getPieces().add(board[i][j]);
				}
			}
		}
	}

	public void updateValidMoves()
	{
		p1.getValidMoves().clear(); 
		p2.getValidMoves().clear();
		for(Piece p:p1.getPieces()) 
		{
			p.updateValidMoves(this);
			p1.getValidMoves().addAll(p.getValidMoves());
		}
		for(Piece p:p2.getPieces()) 
		{
			p.updateValidMoves(this);
			p2.getValidMoves().addAll(p.getValidMoves());
		}
	}

	public void updateValidMoves(Player player)
	{
		if(player.equals(p1)) 
		{
			p1.getValidMoves().clear(); 
			for(Piece p: p1.getPieces()) 
			{
				p.updateValidMoves(this);
				p1.getValidMoves().addAll(p.getValidMoves());
			}
		}
		else if(player.equals(p2))
		{
			p1.getValidMoves().clear(); 
			for(Piece p: p2.getPieces()) 
			{
				p.updateValidMoves(this);
				p2.getValidMoves().addAll(p.getValidMoves());
			}
		}
		
	}

	public void check()
	{
		p1.setChecked(p2.getValidMoves().contains(p1.getKingPos()));
		p2.setChecked(p1.getValidMoves().contains(p2.getKingPos()));
	}

	public void check(Player p)
	{
		if(p.equals(p1)) p.setChecked(p2.getValidMoves().contains(p.getKingPos()));
		else if(p.equals(p2)) p.setChecked(p1.getValidMoves().contains(p.getKingPos()));
	}

	@Override
	public String toString()
	{
		String str = "";
		for(int i = 0; i < 8; i++)
		{
			str += Arrays.toString(board[i]);
			str += "\n";
		}
		return str.substring(0, str.length()-1);
	}

	@Override
	public boolean equals(Object obj)
	{
		boolean equals = true;
		if(obj == null) return false;
		if(obj instanceof Game)
		{
			Game arg = (Game) obj;
			for(int i = 0; i < 8; i++)
			{
				if(!Arrays.equals(arg.board[i], board[i])) return false;
			}
			return equals;
		}
		else return false;
	}

	public boolean isVacant(int x, int y) {
		return board[x][y] == null;
	}

	public boolean isOccupied(int x, int y) {
		return board[x][y] != null;
	}

	public boolean isOccupiedByAlly(Player p, int x, int y) {
		return board[x][y] != null && board[x][y].getPlayer().equals(p);
	}

	public boolean isOccupiedByEnemy(Player friendlyPlayer, int x, int y) {
		return board[x][y] != null && !board[x][y].getPlayer().equals(friendlyPlayer);
	}

	public void setUpBoard()
	{
		for(int j = 0; j < 5; j++)
		{
			if(j == 0)
			{
				board[j][7] =  pieceFactory.createPiece(p1, PieceFactory.ROOK, new Coordinates(j, 7));
				board[7-j][7] =  pieceFactory.createPiece(p1, PieceFactory.ROOK, new Coordinates(7-j, 7));
				board[j][0] =  pieceFactory.createPiece(p2, PieceFactory.ROOK, new Coordinates(j, 0));
				board[7-j][0] =  pieceFactory.createPiece(p2, PieceFactory.ROOK, new Coordinates(7-j, 0));

				board[j][6] = pieceFactory.createPiece(p1, PieceFactory.PAWN, new Coordinates(j, 6));
				board[7-j][6] = pieceFactory.createPiece(p1, PieceFactory.PAWN, new Coordinates(7-j, 6));
				board[j][1] = pieceFactory.createPiece(p2, PieceFactory.PAWN, new Coordinates(j, 1));
				board[7-j][1] = pieceFactory.createPiece(p2, PieceFactory.PAWN, new Coordinates(7-j, 1));
			}
			else if(j == 1)
			{
				board[j][7] =  pieceFactory.createPiece(p1, PieceFactory.HORSE, new Coordinates(j, 7));
				board[7-j][7] =  pieceFactory.createPiece(p1, PieceFactory.HORSE, new Coordinates(7-j, 7));
				board[j][0] =  pieceFactory.createPiece(p2, PieceFactory.HORSE, new Coordinates(j, 0));
				board[7-j][0] =  pieceFactory.createPiece(p2, PieceFactory.HORSE, new Coordinates(7-j, 0));

				board[j][6] = pieceFactory.createPiece(p1, PieceFactory.PAWN, new Coordinates(j, 6));
				board[7-j][6] = pieceFactory.createPiece(p1, PieceFactory.PAWN, new Coordinates(7-j, 6));
				board[j][1] = pieceFactory.createPiece(p2, PieceFactory.PAWN, new Coordinates(j, 1));
				board[7-j][1] = pieceFactory.createPiece(p2, PieceFactory.PAWN, new Coordinates(7-j, 1));
			}
			else if(j == 2)
			{
				board[j][7] =  pieceFactory.createPiece(p1, PieceFactory.BISHOP, new Coordinates(j, 7));
				board[7-j][7] =  pieceFactory.createPiece(p1, PieceFactory.BISHOP, new Coordinates(7-j, 7));
				board[j][0] =  pieceFactory.createPiece(p2, PieceFactory.BISHOP, new Coordinates(j, 0));
				board[7-j][0] =  pieceFactory.createPiece(p2, PieceFactory.BISHOP, new Coordinates(7-j, 0));

				board[j][6] = pieceFactory.createPiece(p1, PieceFactory.PAWN, new Coordinates(j, 6));
				board[7-j][6] = pieceFactory.createPiece(p1, PieceFactory.PAWN, new Coordinates(7-j, 6));
				board[j][1] = pieceFactory.createPiece(p2, PieceFactory.PAWN, new Coordinates(j, 1));
				board[7-j][1] = pieceFactory.createPiece(p2, PieceFactory.PAWN, new Coordinates(7-j, 1));
			}
			else if(j == 3)
			{
				board[j][7] =  pieceFactory.createPiece(p1, PieceFactory.KING, new Coordinates(j, 7));//****;
				board[j][0] =  pieceFactory.createPiece(p2, PieceFactory.QUEEN, new Coordinates(j, 0));

				board[j][6] = pieceFactory.createPiece(p1, PieceFactory.PAWN, new Coordinates(j, 6));
				board[j][1] = pieceFactory.createPiece(p2, PieceFactory.PAWN, new Coordinates(j, 1));
			}
			else if(j == 4)
			{
				board[j][7] =  pieceFactory.createPiece(p1, PieceFactory.QUEEN, new Coordinates(j, 7));
				board[j][0] =  pieceFactory.createPiece(p2, PieceFactory.KING, new Coordinates(j, 0));//****

				board[j][6] = pieceFactory.createPiece(p1, PieceFactory.PAWN, new Coordinates(j, 6));
				board[j][1] = pieceFactory.createPiece(p2, PieceFactory.PAWN, new Coordinates(j, 1));
			}
		}
		updatePieceLists();
		updateValidMoves();
	}
	
	public void updateRoundNum()
	{
		roundNum++;
	}

	public int getRoundNum()
	{
		return roundNum;
	}
	
}

