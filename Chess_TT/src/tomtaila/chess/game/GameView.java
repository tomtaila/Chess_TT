package tomtaila.chess.game;

import java.util.Arrays;

import tomtaila.chess.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;
import chess.game.Game;

public class GameView extends View implements OnTouchListener{

	//GAME VARIABLES
	private Game game;
	private String currentPieceType;
	private int currentPlayerID;
	private Rect currentRect;

	//DRAW VARIABLES
	private Paint boardPaint, selectedSquarePaint, turnIndicaterPaint, borderPaint;
	private int lightTileColor, darkTileColor, indicaterColor, selectedTileColor;
	private Bitmap pawn_w_BMP, pawn_b_BMP, rook_w_BMP, rook_b_BMP, bishop_w_BMP, bishop_b_BMP, horse_w_BMP, horse_b_BMP, queen_w_BMP, queen_b_BMP, king_w_BMP, king_b_BMP;
	private int posX, posY;
	private float leftPosX, topPosY;
	private float squareSideLength;
	private float borderSquareSideLength;
	private float miniSquareLength;
	private float indicaterHeight, indicaterWidth;
	private Rect indicater;

	//TOUCH VARIABLES
	private boolean[][] selected;
	private boolean touched;
	private int firstTouchX, firstTouchY, secondTouchX, secondTouchY;
	private boolean ranOnce;

	//Toast
	private Toast checkToast;
	private String isCheckedString;
	
	//Dialog
	private AlertDialog.Builder builder;
	private AlertDialog checkMateDialog;
	
	public GameView(Context context) {
		super(context);
		initialize(context);
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize(context);
	}

	public GameActivity getActivity()
	{
		try
		{
			return (GameActivity) getContext();
		}
		catch(ClassCastException e)
		{
			throw new ClassCastException("NOT GAMEACTIVITY");
		}
	}

	private void initialize(Context context)
	{
		game = getActivity().getGame();

		Resources r = getResources();

		pawn_w_BMP = BitmapFactory.decodeResource(r, R.drawable.pawn_w);
		pawn_b_BMP = BitmapFactory.decodeResource(r, R.drawable.pawn_b);
		rook_w_BMP = BitmapFactory.decodeResource(r, R.drawable.rook_w);
		rook_b_BMP = BitmapFactory.decodeResource(r, R.drawable.rook_b);
		bishop_w_BMP = BitmapFactory.decodeResource(r, R.drawable.bishop_w);
		bishop_b_BMP = BitmapFactory.decodeResource(r, R.drawable.bishop_b);
		horse_w_BMP = BitmapFactory.decodeResource(r, R.drawable.horse_w);
		horse_b_BMP = BitmapFactory.decodeResource(r, R.drawable.horse_b);
		queen_w_BMP = BitmapFactory.decodeResource(r, R.drawable.queen_w);
		queen_b_BMP = BitmapFactory.decodeResource(r, R.drawable.queen_b);
		king_w_BMP = BitmapFactory.decodeResource(r, R.drawable.king_w);
		king_b_BMP = BitmapFactory.decodeResource(r, R.drawable.king_b);
		
		lightTileColor = r.getColor(R.color.white_square);
		darkTileColor = r.getColor(R.color.dark_square);
		indicaterColor = r.getColor(R.color.transparent_white);
		selectedTileColor = r.getColor(R.color.selected_tile);

		boardPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		boardPaint.setColor(lightTileColor);
		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setColor(Color.BLACK);

		selectedSquarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		selectedSquarePaint.setColor(selectedTileColor);
		currentRect = new Rect();

		turnIndicaterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		turnIndicaterPaint.setColor(indicaterColor);
		indicater = new Rect();
		indicaterHeight = 0;
		indicaterWidth = 0;

		selected = new boolean[8][8];
		for(boolean[] boolArray : selected)
		{
			Arrays.fill(boolArray, false);
		}

		touched = false;
		firstTouchX = 0;
		firstTouchY = 0;
		secondTouchX = 0;
		secondTouchY = 0;

		ranOnce = false;
		
		isCheckedString = "is in check!";
		
		builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Player "+game.getWinner().playerID() + " wins!\n" + r.getString(R.string.game_over));
		builder.setNegativeButton(r.getString(R.string.see_checkmate), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Do nothing
			}
		});
		builder.setPositiveButton(r.getString(R.string.home), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				initialize(getContext());
				getActivity().finish();
			}
		});
		checkMateDialog = builder.create();
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		//Color background
		squareSideLength = (int) ((Math.min(getMeasuredWidth(), getMeasuredHeight()))*0.95);
		borderSquareSideLength = (int) (squareSideLength*1.025);
		miniSquareLength = squareSideLength/8;

		indicaterHeight = (int) ((Math.min(getMeasuredWidth(), getMeasuredHeight()))*0.05);
		indicaterWidth = miniSquareLength;

		canvas.drawRect(posX-(borderSquareSideLength/2), posY-(borderSquareSideLength/2), posX+(borderSquareSideLength/2), posY+(borderSquareSideLength/2), borderPaint);
		
		if(!ranOnce)
		{
			invalidate();
			ranOnce = true;
		}

		if(game.getRoundNum()%2 == 1)
		{
			indicater.set((int)(posX-(indicaterWidth*0.75)),(int)(posY+(squareSideLength/2)), (int)(posX+(indicaterWidth*0.75)), (int) (posY+(squareSideLength/2)+indicaterHeight));
			canvas.drawRect(indicater, turnIndicaterPaint);
		}
		else
		{
			indicater.set((int)(posX-(indicaterWidth*0.75)),(int)(posY-((squareSideLength/2)+indicaterHeight)), (int)(posX+(indicaterWidth*0.75)), (int) (posY-(squareSideLength/2)));
			canvas.drawRect(indicater, turnIndicaterPaint);
		}

		posX = getMeasuredWidth()/2;
		posY = getMeasuredHeight()/2;
		leftPosX = posX-(squareSideLength/2);
		topPosY = posY-(squareSideLength/2);

		for(int j = 0; j < 8; j++)
		{
			for(int i = 0; i < 8; i++)
			{
				currentRect.set((int)leftPosX, (int)topPosY,(int) (leftPosX+miniSquareLength), (int)(topPosY+miniSquareLength));
				canvas.drawRect(leftPosX, topPosY, leftPosX+(miniSquareLength), topPosY+(miniSquareLength), boardPaint);
				
				if(game.isOccupied(i, j))
				{
					currentPieceType = game.getPiece(i, j).pieceType();
					currentPlayerID = game.getPiece(i, j).getPlayer().playerID();

					if(currentPieceType.equals("Pawn"))
					{
						if(selected[i][j])
						{
							canvas.drawRect(leftPosX, topPosY, leftPosX+(miniSquareLength), topPosY+(miniSquareLength), selectedSquarePaint);
						}
						if(currentPlayerID == 1) canvas.drawBitmap(pawn_w_BMP, null, currentRect, boardPaint);
						else canvas.drawBitmap(pawn_b_BMP, null, currentRect, boardPaint);
					}
					else if(currentPieceType.equals("Rook"))
					{
						if(selected[i][j])
						{
							canvas.drawRect(leftPosX, topPosY, leftPosX+(miniSquareLength), topPosY+(miniSquareLength), selectedSquarePaint);
						}
						if(currentPlayerID == 1) canvas.drawBitmap(rook_w_BMP, null, currentRect, boardPaint);
						else canvas.drawBitmap(rook_b_BMP, null, currentRect, boardPaint);
					}
					else if(currentPieceType.equals("Horse"))
					{
						if(selected[i][j])
						{
							canvas.drawRect(leftPosX, topPosY, leftPosX+(miniSquareLength), topPosY+(miniSquareLength), selectedSquarePaint);
						}
						if(currentPlayerID == 1) canvas.drawBitmap(horse_w_BMP, null, currentRect, boardPaint);
						else canvas.drawBitmap(horse_b_BMP, null, currentRect, boardPaint);
					}
					else if(currentPieceType.equals("Bishop"))
					{
						if(selected[i][j])
						{
							canvas.drawRect(leftPosX, topPosY, leftPosX+(miniSquareLength), topPosY+(miniSquareLength), selectedSquarePaint);
						}
						if(currentPlayerID == 1) canvas.drawBitmap(bishop_w_BMP, null, currentRect, boardPaint);
						else canvas.drawBitmap(bishop_b_BMP, null, currentRect, boardPaint);
					}
					else if(currentPieceType.equals("Queen"))
					{
						if(selected[i][j])
						{
							canvas.drawRect(leftPosX, topPosY, leftPosX+(miniSquareLength), topPosY+(miniSquareLength), selectedSquarePaint);
						}
						if(currentPlayerID == 1) canvas.drawBitmap(queen_w_BMP, null, currentRect, boardPaint);
						else canvas.drawBitmap(queen_b_BMP, null, currentRect, boardPaint);
					}
					else if(currentPieceType.equals("King"))
					{
						if(selected[i][j])
						{
							canvas.drawRect(leftPosX, topPosY, leftPosX+(miniSquareLength), topPosY+(miniSquareLength), selectedSquarePaint);
						}
						if(currentPlayerID == 1) canvas.drawBitmap(king_w_BMP, null, currentRect, boardPaint);
						else canvas.drawBitmap(king_b_BMP, null, currentRect, boardPaint);
					}
				}

				leftPosX = leftPosX+(miniSquareLength);
				switchSquareColor();
			}
			leftPosX = posX-(squareSideLength/2);
			topPosY = topPosY+(miniSquareLength);
			switchSquareColor();
		}
		topPosY = posY-(squareSideLength/2);
	}

	public void switchSquareColor()
	{
		if(boardPaint.getColor() == darkTileColor) boardPaint.setColor(lightTileColor);
		else boardPaint.setColor(darkTileColor);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		//NOTE: Chess board should be a square that should fill as much space as possible

		//Find the length of both sides
		int measureWidth = measure(widthMeasureSpec);
		int measureHeight = measure(heightMeasureSpec);

		setMeasuredDimension(measureWidth, measureHeight);
	}

	public int measure(int measureSpec)
	{
		int result = 0;

		//Decode the measurement specifications
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		//If the parent passes no layout requirements to its child...
		if(specMode == MeasureSpec.UNSPECIFIED)
		{
			//Return a default size of 200 if no bounds are specified
			result = 200;
		}
		else 
		{
			//As you want to fill the space available space return all the available space
			result = specSize;
		}

		return result;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(game.getCheckMateReached())
		{
			checkMateDialog.show();
			return true;
		}
		else
		{
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				float touchX = event.getX();
				float touchY = event.getY();
				if( (touchX > leftPosX && touchX < (leftPosX+(8*miniSquareLength))) && (touchY > topPosY && touchY < (topPosY+(8*miniSquareLength))) )
				{
					if(!touched)
					{
						firstTouchX = (int)((touchX-leftPosX)/miniSquareLength);
						firstTouchY = (int)((touchY-topPosY)/miniSquareLength);
						if(game.getRoundNum()%2 == 1)
						{
							if(game.isOccupiedByAlly(game.getPlayer(1), firstTouchX, firstTouchY))
							{
								if(selected[firstTouchX][firstTouchY]) selected[firstTouchX][firstTouchY] = false;
								else selected[firstTouchX][firstTouchY] = true;
								touched = true;
							}
						}
						else if(game.getRoundNum()%2 == 0)
						{
							if(game.isOccupiedByAlly(game.getPlayer(2), firstTouchX, firstTouchY))
							{
								if(selected[firstTouchX][firstTouchY]) selected[firstTouchX][firstTouchY] = false;
								else selected[firstTouchX][firstTouchY] = true;
								touched = true;
							}
						}
					}
					else
					{
						secondTouchX = (int)((touchX-leftPosX)/miniSquareLength);
						secondTouchY = (int)((touchY-topPosY)/miniSquareLength);
						if(game.getPiece(firstTouchX, firstTouchY).move(game, firstTouchX, firstTouchY, secondTouchX, secondTouchY))
						{
							game.updateRoundNum();
							if(game.getRoundNum()%2==1)
							{
								if(game.getPlayer(1).checked())
								{
									checkToast = Toast.makeText(getContext(), "Player 1 " + isCheckedString, Toast.LENGTH_LONG);
									checkToast.show();
								}
								game.getPlayer(1).setInCheckMate(game.getPlayer(1).checkMate(game));
								if(game.getPlayer(1).isInCheckMate())
								{
									game.setWinner(game.getPlayer(2));
									turnIndicaterPaint.setColor(getResources().getColor(R.color.transparent_red));
									builder.setMessage("Player "+game.getWinner().playerID() + " wins!\n" + getResources().getString(R.string.game_over));
									checkMateDialog = builder.create();
									checkMateDialog.show();
									game.setCheckMateReached(true);
								}
							}
							else if(game.getRoundNum()%2==0)
							{
								if(game.getPlayer(2).checked())
								{
									checkToast = Toast.makeText(getContext(), "Player 2 " + isCheckedString, Toast.LENGTH_LONG);
									checkToast.show();
								}
								game.getPlayer(2).setInCheckMate(game.getPlayer(2).checkMate(game));
								if(game.getPlayer(2).isInCheckMate())
								{
									game.setWinner(game.getPlayer(1));
									turnIndicaterPaint.setColor(getResources().getColor(R.color.transparent_red));
									builder.setMessage("Player "+game.getWinner().playerID() + " wins!\n" + getResources().getString(R.string.game_over));
									checkMateDialog = builder.create();
									checkMateDialog.show();
									game.setCheckMateReached(true);
								}
							}
							getActivity().saveGame();
						}
						selected[firstTouchX][firstTouchY] = false;

						touched = false;
					}
				}
			}
			game.updatePieceLists();
			game.updateValidMoves();
			invalidate();
			return true;
		}
	}

	
	
	
}
