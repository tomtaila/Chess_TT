package chess.piece;

import java.io.Serializable;

import chess.game.Coordinates;
import chess.game.Player;
import chess.piece.behavior.BishopMovementBehavior;
import chess.piece.behavior.HorseMovementBehavior;
import chess.piece.behavior.KingMovementBehavior;
import chess.piece.behavior.PawnMovementBehavior;
import chess.piece.behavior.QueenMovementBehavior;
import chess.piece.behavior.RookMovementBehavior;

/**
 * Created by TomTaila on 06/12/2013.
 */
public class PieceFactory implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8781207819916626089L;
	public static final int PAWN = 0;
    public static final int ROOK = 1;
    public static final int BISHOP = 2;
    public static final int HORSE = 3;
    public static final int KING = 4;
    public static final int QUEEN = 5;

    public Piece createPiece(Player player, int pieceType, Coordinates pos)
    {
        switch(pieceType)
        {
            case PAWN : return new Piece(player, new PawnMovementBehavior(), "Pawn", pos);
            case ROOK : return new Piece(player, new RookMovementBehavior(), "Rook", pos);
            case BISHOP : return new Piece(player, new BishopMovementBehavior(), "Bishop", pos);
            case HORSE : return new Piece(player, new HorseMovementBehavior(), "Horse", pos);
            case KING : return new Piece(player, new KingMovementBehavior(), "King", pos);
            case QUEEN : return new Piece(player, new QueenMovementBehavior(), "Queen", pos);
            default: return null;
        }
    }

}