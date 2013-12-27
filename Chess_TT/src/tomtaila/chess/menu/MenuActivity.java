package tomtaila.chess.menu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import tomtaila.chess.R;
import tomtaila.chess.game.GameActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import chess.game.Game;

public class MenuActivity extends FragmentActivity{

	private LinkedList<Button> menuList;
	private MenuButtonArrayAdapter menuButtonArrayAdapter;
	private MainMenuFragment menuFragment;
	private Button newGameButton, exitGameButton, continueGameButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_menu);

		initialize();

		setUpMenu();
	}

	public LinkedList<Button> getMenuButtons()
	{
		return menuList;
	}

	private void initialize()
	{
		//Get references to fragments
		FragmentManager fragmentManager = getFragmentManager();
		menuFragment = (MainMenuFragment)fragmentManager.findFragmentById(R.id.MainMenuFragment);

		//Initialize list
		menuList = new LinkedList<Button>();

		//Create an ArrayAdapter to bind the List to ListViews
		menuButtonArrayAdapter = new MenuButtonArrayAdapter(this, R.layout.menu_buttons, menuList);

		newGameButton = new Button(this);
		exitGameButton = new Button(this);
		continueGameButton = new Button(this);
		newGameButton.setText("New game");
		exitGameButton.setText("Exit game");
		continueGameButton.setText("Continue");

		menuFragment.setListAdapter(menuButtonArrayAdapter);
		menuButtonArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume()
	{
		setUpMenu();
		super.onResume();
	}

	private void setUpMenu()
	{
		menuList.clear();
		menuList.add(newGameButton);
		menuList.add(exitGameButton);
		if(loadGame() != null)
		{
			menuList.add(continueGameButton);
		}

		menuButtonArrayAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public class MenuButtonArrayAdapter extends ArrayAdapter<Button>{

		private int resource;


		public MenuButtonArrayAdapter(Context context, int resource, List<Button> objects) {
			super(context, resource, objects);
			this.resource = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View menuView;

			final String text = getItem(position).getText().toString();

			if(convertView == null)
			{
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inflater);
				menuView = layoutInflater.inflate(resource, parent, false);
			}
			else menuView = (View) convertView;

			final Button b = (Button)menuView.findViewById(R.id.menu_button);
			b.setText(text);
			b.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(text.equals(getResources().getString(R.string.exit_game)))
					{
						finish();
					}
					else if(text.equals(getResources().getString(R.string.new_game)))
					{
						if(loadGame() != null)
						{
							class GameExistsDialogFragment extends DialogFragment {
								@Override
								public Dialog onCreateDialog(Bundle savedInstanceState) {
									// Use the Builder class for convenient dialog construction
									AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
									builder.setMessage(R.string.game_exists)
									.setPositiveButton(R.string.new_game, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											restartGameFile();
											Intent intent = new Intent(MenuActivity.this, GameActivity.class);
											startActivity(intent);
										}
									})
									.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int id) {
											// User cancelled the dialog
										}
									});
									// Create the AlertDialog object and return it
									return builder.create();
								}
							}
							GameExistsDialogFragment dialog = new GameExistsDialogFragment();
							dialog.show(getFragmentManager(), text);
						}
						else
						{
							Intent intent = new Intent(MenuActivity.this, GameActivity.class);
							startActivity(intent);
						}
					}
					else if(text.equals(getResources().getString(R.string.continue_game)))
					{
						Intent intent = new Intent(MenuActivity.this, GameActivity.class);
						startActivity(intent);
					}
				}
			});

			return menuView;
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
