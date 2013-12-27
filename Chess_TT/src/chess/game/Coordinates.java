package chess.game;

import java.io.Serializable;

public class Coordinates implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1016750591941621552L;
	private int x, y;
	
	public Coordinates(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Coordinates(Coordinates c)
	{
		x = c.x;
		y = c.y;
	}
	
	
	@Override
	public Coordinates clone()
	{
		return new Coordinates(this);
	}
	
	
	@Override
	public String toString()
	{
		return "( " + x + ", " + y + ")";
	}
	
	public int getX(){return x;}
	public int getY(){return y;}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Coordinates)
		{
			Coordinates arg = (Coordinates) obj;
			return (x == arg.x && y == arg.y);
		}
		else return false;
	}
	
}
