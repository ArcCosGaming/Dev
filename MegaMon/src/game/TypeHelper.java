package game;

import java.awt.Point;

public class TypeHelper
{
	final static String[] types = new String[] {"NORMAL", "FIGHTING", "FLYING", "POISON", "GROUND", "ROCK", "BUG", "GHOST", "FIRE", "WATER", "GRASS", "ELECTRIC", "PSYCHIC", "ICE", "DRAGON"};
	final static float[][] typesMulti = new float[][]{
													  {1f, 2f, 1f, 1f, 1f, 1f, 1f, 0f, 1f, 1f, 1f, 1f, 1f, 1f, 1f},
													  {1f, 1f, 2f, 1f, 1f, 0.5f, 0.5f, 0f, 1f, 1f, 1f, 1f, 2f, 1f, 1f},
													  {1f, 0.5f, 1f, 1f, 0f, 2f, 0.5f, 1f, 1f, 1f, 0.5f, 2f, 1f, 2f, 1f},
													  {1f, 0.5f, 1f, 0.5f, 1f, 1f, 1f, 2f, 0.5f, 2f, 2f, 0f, 1f, 2f, 1f},
													  {1f, 1f, 1f, 0.5f, 1f, 0.5f, 1f, 1f, 1f, 2f, 2f, 0f, 1f, 2f, 1f},
													  {0.5f, 2f, 0.5f, 0.5f, 2f, 1f, 1f, 1f, 0.5f, 2f, 2f, 1f, 1f, 1f, 1f},
													  {1f, 0.5f, 2f, 1f, 0.5f, 2f, 1f, 1f, 2f, 1f, 0.5f, 1f, 1f, 1f, 1f},
													  {0f, 0f, 1f, 0.5f, 1f, 1f, 0.5f, 2f, 1f, 1f, 1f, 1f, 1f, 1f, 1f},
													  {1f, 1f, 1f, 1f, 2f, 2f, 0.5f, 1f, 0.5f, 2f, 0.5f, 1f, 1f, 0.5f, 1f},
													  {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 0.5f, 0.5f, 2f, 2f, 1f, 0.5f, 1f},
													  {1f, 1f, 2f, 2f, 0.5f, 1f, 2f, 1f, 2f, 0.5f, 0.5f, 0.5f, 1f, 2f, 1f},
													  {1f, 1f, 0.5f, 1f, 2f, 1f, 1f, 1f, 1f, 1f, 1f, 0.5f, 1f, 1f, 1f},
													  {1f, 0.5f, 1f, 1f, 1f, 1f, 2f, 0f, 1f, 1f, 1f, 1f, 0.5f, 1f, 1f},
													  {1f, 2f, 1f, 1f, 1f, 2f, 1f, 1f, 2f, 1f, 1f, 1f, 1f, 0.5f, 1f, 1f},
													  {1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f, 0.5f, 0.5f, 0.5f, 0.5f, 1f, 2f, 2f}
													  };
	
	public static float getMultiplier(String defendType, String attackType)
	{
		try 
		{
			Point p = search(defendType, attackType);
			return typesMulti[(int) p.getX()][(int) p.getY()];
		}
		catch (TypeNotFoundException e)
		{
			e.printType();
		}
		return 1f;
	}
	public static float getMultiplier(String type1, String type2, String attackType)
	{
		return getMultiplier(type1, attackType) * getMultiplier(type2, attackType);
	}
	
	public static Point search(String defendType, String attackType) throws TypeNotFoundException
	{
		int x = -1;
		int y = -1;
		for (int i = 0; i < TypeHelper.types.length; i++)
			if (TypeHelper.types[i].equals(defendType))
			{
				x = i;
				break;
			}
		if (x == -1)
			throw new TypeNotFoundException(defendType);
		for (int i = 0; i < TypeHelper.types.length; i++)
			if (TypeHelper.types[i].equals(attackType))
			{
				y = i;
				break;
			}
		if (y == -1)
			throw new TypeNotFoundException(attackType);
		return new Point(x, y);
		
	}
	
	public static boolean isSTAB(String attackType, String type1, String type2)
	{
		return attackType.equals(type1) || attackType.equals(type2);
	}
	
	public static class TypeNotFoundException extends Exception
	{
		private static final long serialVersionUID = 1L;
		String type;
		
		public TypeNotFoundException(String type)
		{
			super();
			this.type = type;
		}
		
		public String getType()
		{
			return type;
		}
		
		public void printType()
		{
			System.out.println(type);
		}
	}
}
