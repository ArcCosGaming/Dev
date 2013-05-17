package graphics;
import static ResourceLoader.ImageLoader.loadTexture;

public class Texture {
	private int textureID, sections;
	
	public Texture(String path, int sections)
	{
		this.textureID = loadTexture(path);
		this.sections = sections;
	}
	
	public int getID() {
		return this.textureID;
	}
	
	public int getSections() {
		return this.sections;
	}
}
