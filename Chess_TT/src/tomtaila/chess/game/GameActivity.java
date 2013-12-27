package tomtaila.chess.game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import tomtaila.chess.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import chess.game.Game;

public class GameActivity extends Activity{

	protected static String GAME = "Game state";
	protected Game game;
	protected GameView GV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initialize();
		setContentView(R.layout.game_activity);

		GV = (GameView)findViewById(R.id.gameView);
		GV.setOnTouchListener(GV);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		savedInstanceState.putSerializable(GAME, game);
		super.onSaveInstanceState(savedInstanceState);
	}

	public Game getGame()
	{
		return game;
	}

	public boolean saveGame()
	{
		try {
			FileOutputStream fos = this.openFileOutput("game.ser", Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(game);
			oos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public Game loadGame()
	{
		try {
			FileInputStream fis = this.openFileInput("game.ser");
			ObjectInputStream is = new ObjectInputStream(fis);
			Object readObject = is.readObject();
			is.close();

			if(readObject != null && readObject instanceof Game) {
				return (Game) readObject;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	protected void initialize()
	{
		game = loadGame();
		if(game == null) 
		{
			game = new Game();
			game.setUpBoard();
		}
	}

	public boolean restartGameFile()
	{
		try {
			FileOutputStream fos = this.openFileOutput("game.ser", Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(null);
			oos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
