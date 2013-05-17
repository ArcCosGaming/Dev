package graphics;

import static org.lwjgl.opengl.Display.getDisplayMode;
import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2i;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import game.GameStarter;
import ResourceLoader.ImageLoader;

public class Region {
	private String regionName;
	private int regionID, height, width, posX, posY, regionX, regionY;
	private boolean hasUploaded;
	private ByteBuffer buff;
	
	public Region(String regionName, int posX, int posY, int regionX, int regionY) {
		this.regionName = regionName;
		this.regionID = ImageLoader.loadTexture("res/images/regions/" + regionName + ".png");
		this.hasUploaded = true;
		this.height = Map.regionHeight;
		this.width = Map.regionWidth;
		this.posX = posX;
		this.posY = posY;
		this.regionX = regionX;
		this.regionY = regionY;
	}
	public Region(String regionName, int posX, int posY, int regionX, int regionY, int textureID) {
		this.regionName = regionName;
		this.buff = ImageLoader.loadTexture("res/images/regions/" + regionName + ".png", textureID);
		this.hasUploaded = false;
		this.height = Map.regionHeight;
		this.width = Map.regionWidth;
		this.posX = posX;
		this.posY = posY;
		this.regionX = regionX;
		this.regionY = regionY;
	}
	
	public void render(int absX, int absY, int frame) {
		if (!this.hasUploaded) {
			this.regionID = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, this.regionID);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP);
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP);
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	        if (buff.capacity() == 1024*1024*4)
	        	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1024, 1024, 0, GL_RGBA, GL_UNSIGNED_BYTE, this.buff);
	        else
	        	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, 1024, 1024, 0, GL_RGB, GL_UNSIGNED_BYTE, this.buff);
			this.hasUploaded = true;
		}
		int drawX = 0;//GameStarter.getPosXValue(this.posX)-(absX-GraphicsEngine.getOriginX());
		int drawY = 0;//GameStarter.getPosYValue(this.posY)-(absY-GraphicsEngine.getOriginY());
		if (drawX >= -this.width/2 && drawX <= getDisplayMode().getWidth()+this.width/2 && drawY >= -this.height/2 && drawY <= getDisplayMode().getHeight()+this.height/2)
		{
			glPushMatrix();
				glEnable(GL_TEXTURE_2D);
				try
				{
					glBindTexture(GL_TEXTURE_2D, this.regionID);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				glTranslatef(drawX, drawY, 0);
				glBegin(GL_QUADS);
					glTexCoord2f(0, 0);
					glVertex2i(-this.width/2, this.height/2);
					glTexCoord2f(0, 1);
					glVertex2i(-this.width/2, -this.height/2);
					glTexCoord2f(1, 1);
					glVertex2i(this.width/2, -this.height/2);
					glTexCoord2f(1, 0);
					glVertex2i(this.width/2, this.height/2);
				glEnd();
				try
				{
					glBindTexture(GL_TEXTURE_2D, 0);

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				glDisable(GL_TEXTURE_2D);
			glPopMatrix();
		}
	}
	
	public int getPriority() {
		return 0;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public int getRegionX() {
		return this.regionX;
	}
	
	public int getRegionY() {
		return this.regionY;
	}
}
