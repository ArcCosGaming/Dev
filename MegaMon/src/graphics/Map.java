package graphics;

import java.awt.Point;
import java.util.Hashtable;

import ResourceLoader.RegionBuilder;
public class Map
{
	private static Hashtable<Point, String> regionNames = new Hashtable<Point, String>();
	public final static int regionHeight = 1024;
	public final static int regionWidth = 1024;
	private Hashtable<Point, Region> regions = new Hashtable<Point, Region>();
	private Point currentRegion;
	private RegionBuilder rb;
	
	public Map()
	{
		makeRegionNames();
		this.rb = new RegionBuilder(this);
		this.currentRegion = new Point();
		this.addRegion(new Region("test2", getRegionX(0), getRegionY(0), 0, 0));
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++)
				if (i != 0 || j != 0)
					this.rb.built(regionNames.get(new Point(i, j)), getRegionX(i), getRegionY(j), i, j);
	}
	
	public void render(int absX, int absY, int frame)
	{
		this.updateRegion(absX, absY);
		//this.regions.get(currentRegion).render(absX, absY, frame);
		for (int i = -1; i <= 1; i++)
			for (int j = -1; j <= 1; j++)
				if (this.regions.get(getRegion(i, j)) != null)
					this.regions.get(getRegion(i, j)).render(absX, absY, frame);
	}
	
	public int getPriority()
	{
		return 0;
	}
	
	private void updateRegion(int posX, int posY)
	{
		Region current = this.regions.get(currentRegion);
		if (current != null) {
			int getPosX = 0;//getPosXValue(current.getPosX());
			int getPosY = 0;//getPosYValue(current.getPosY());
			int isPosX = getPosX;
			int isPosY = getPosY;
			if (posX < isPosX-(current.getWidth()/2))
				getPosX -= regionWidth; 
			else if (posX > isPosX+(current.getWidth()/2))
				getPosX += regionWidth; 
			if (posY < isPosY-(current.getHeight()/2))
				getPosY -= regionHeight;
			else if (posY > isPosY+(current.getHeight()/2))
				getPosY += regionHeight;
			if (getPosY != isPosY || getPosX != isPosX) {
				this.currentRegion = new Point(getPosX, getPosY);
				current = this.regions.get(currentRegion);
				if (current != null)
					for (int i = -1; i <= 1; i++)
						for (int j = -1; j <= 1; j++)
							if (this.regions.get(getRegion(i, j)) == null)
							{
								this.rb.built(regionNames.get(new Point(current.getRegionX()+i, current.getRegionY()+j)), getRegionX(current.getRegionX()+i), getRegionY(current.getRegionY()+j), current.getRegionX()+i, current.getRegionY()+j);
							}
			}
		}
	}
	
	public void addRegion(Region region) {
		Point p = new Point();
		this.regions.put(p, region);
	}
	
	public static int getRegionX(int posX) {
		return regionWidth*posX;
	}
	
	public static int getRegionByX(int posX) {
		return posX/regionWidth;
	}
	
	public static int getRegionY(int posY) {
		return regionHeight*posY;
	}
	
	public static int getRegionByY(int posY) {
		return posY/regionWidth;
	}
	
	public Point getRegion(int dX, int dY) {
		int posX = (int) this.currentRegion.getX()+dX*regionWidth;
		int posY = (int) this.currentRegion.getY()+dY*regionHeight;
		return new Point(posX, posY);
	}
	
	private static void makeRegionNames() {
		regionNames.put(new Point(-2, -2), "test2");
		regionNames.put(new Point(-2, -1), "test");
		regionNames.put(new Point(-2, 0), "test2");
		regionNames.put(new Point(-2, 1), "test");
		regionNames.put(new Point(-2, 2), "test2");
		regionNames.put(new Point(-1, -2), "test");
		regionNames.put(new Point(-1, -1), "test2");
		regionNames.put(new Point(-1, 0), "test");
		regionNames.put(new Point(-1, 1), "test2");
		regionNames.put(new Point(-1, 2), "test");
		regionNames.put(new Point(0, -2), "test2");
		regionNames.put(new Point(0, -1), "test");
		regionNames.put(new Point(0, 0), "test2");
		regionNames.put(new Point(0, 1), "test");
		regionNames.put(new Point(0, 2), "test2");
		regionNames.put(new Point(1, -2), "test");
		regionNames.put(new Point(1, -1), "test2");
		regionNames.put(new Point(1, 0), "test");
		regionNames.put(new Point(1, 1), "test2");
		regionNames.put(new Point(1, 2), "test");
		regionNames.put(new Point(2, -2), "test2");
		regionNames.put(new Point(2, -1), "test");
		regionNames.put(new Point(2, 0), "test2");
		regionNames.put(new Point(2, 1), "test");
		regionNames.put(new Point(2, 2), "test2");
	}
}
